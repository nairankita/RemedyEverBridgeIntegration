package com.vyomlabs.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.ActiveLink;
import com.bmc.arsys.api.ActiveLinkAction;
import com.bmc.arsys.api.ArchiveInfo;
import com.bmc.arsys.api.CharacterField;
import com.bmc.arsys.api.CharacterFieldLimit;
import com.bmc.arsys.api.CloseWindowAction;
import com.bmc.arsys.api.Constants;
import com.bmc.arsys.api.DisplayInstanceMap;
import com.bmc.arsys.api.DisplayPropertyMap;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.EntryListInfo;
import com.bmc.arsys.api.EscalationTime;
import com.bmc.arsys.api.Filter;
import com.bmc.arsys.api.FilterAction;
import com.bmc.arsys.api.Form;
import com.bmc.arsys.api.OutputInteger;
import com.bmc.arsys.api.OverlaidInfo;
import com.bmc.arsys.api.PermissionInfo;
import com.bmc.arsys.api.QualifierInfo;
import com.bmc.arsys.api.RegularFieldMapping;
import com.bmc.arsys.api.RegularForm;
import com.bmc.arsys.api.SetFieldsFromForm;
import com.bmc.arsys.api.Value;
/*import com.bmc.arsys.apiext.definition.DefinitionExport;
import com.bmc.arsys.apiext.definition.DefinitionImport;
import com.bmc.arsys.apiext.definition.DefinitionItemType;
import com.bmc.arsys.apiext.definition.DefinitionOptions;
import com.bmc.arsys.apiext.definition.DefinitionOptions.Operation;
import com.bmc.arsys.apiext.definition.RelatedType;
*/
public class ARUtils {
	
	private final static Logger log = Logger.getLogger(ARUtils.class.getName());
	
	/**
	 * Gets sleep time from configuration file and pauses the execution of
	 * thread for given number of milliseconds.
	 * 
	 * Sometimes, if we create an entity and try to fetch it immidiately, 
	 * the process fails. Introducing sleep fixes the problem. 
	 */
	public static void sleep() {
		try {
			ConfigReader cr = new ConfigReader();
			Thread.sleep(cr.getSleepTime());
		} catch(Exception exp) {
			exp.printStackTrace();
		}
	}
		
	/**
	 * Logs-in to the system with credentials from configuration file.
	 * @return Filled ARServerUser object with data if sucess, null otherwise.
	 */
	public static ARServerUser login() {
		ConfigReader cr = new ConfigReader();
		ARServerUser server = new ARServerUser();
        server.setServer(cr.getARServer());
        server.setUser(cr.getUsername());
        server.setPassword(cr.getPassword());
        log.info("Connecting to AR Server...");
		try {
			server.verifyUser();
			log.info("Connected to AR Server : "+cr.getARServer());
		} catch (ARException e) {
			e.printStackTrace();
			return null;
		}		
		return server;
	}
	
	/**
	 * Logs out of the system.
	 * @param server ARServerUser object represting logged in user.
	 */
	public static void logout(ARServerUser server) {
    	if(server != null) {
    		log.info("Logging out from AR Server...");
    		server.logout();
    		log.info("Logged out from AR Server.");
    	}
	}
	
	/**
	 * Retrieves entry with given id for given Form.
	 * 
	 * @param server		Object representing logged in user.
	 * @param formName		Form under consideration.
	 * @param requestId		Identifier of the entry.
	 * @return Filled Entry if success, null otherwise.
	 */
	public static Entry getEntry(ARServerUser server, String formName, String requestId) {
		try {
			String s = "'1' = \"" + requestId + "\"";
			QualifierInfo q = server.parseQualification(formName, s);
			int[] fieldIds = {1, 8};
			Entry searchEntry = server.getOneEntryObject(formName, q, null, fieldIds, true, new OutputInteger());
			String returnRequestId = searchEntry.get(1).toString();
			if(requestId.equals(returnRequestId)) return searchEntry;
			else return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts status from status id to understandable string.
	 * @param statusId	status id.
	 * @return String corresponding to given id.
	 */
	public static String getApprovalStatus(int statusId) {
		switch(statusId) {
			case 0: 
				return "Pending";
			case 1: 
				return "Approved";
			case 2: 
				return "Rejected";
			case 3: 
				return "Hold";
			case 4: 
				return "More Information";
			case 5: 
				return "Cancelled";
			case 6: 
				return "Error";
			case 7: 
				return "Closed";
		}
		return "Unknown";
	}

	/**
	 * Creates a Regular form.
	 * @param formName	Form name
	 * @param server	ARSeverUser object for logged-in user.
	 * @return RegularFrom object if successful, null otherwise.
	 */
	public static RegularForm createRegularForm(String formName, ARServerUser server) {
    	try {
    		System.out.format("Creating Form %s.\n", formName);
    		ConfigReader cr = new ConfigReader();
    		RegularForm regularForm = new RegularForm();
    		
    		regularForm.setName(formName);
    		regularForm.setOwner(cr.getUsername());
    		regularForm.setDefaultVUI("DefaultView");
    		
    		// Set visible permission and public group.
    		List<PermissionInfo> piList = new ArrayList<PermissionInfo>();
    		PermissionInfo pi = new PermissionInfo();
    		pi.setGroupID((int)Constants.AR_GROUP_ID_PUBLIC);
    		pi.setPermissionValue(Constants.AR_PERMISSIONS_VISIBLE);
    		piList.add(pi);
    		regularForm.setPermissions(piList);
    		
    		server.createForm(regularForm);    
    		System.out.format("Form %s successfully created.\n", formName);
    		
    		return regularForm;
    		
    	} catch(Exception exp) {
    		exp.printStackTrace();
    		return null;
    	}
    }
	
	/**
	 * Creates an active link for given form.
	 * @param activeLinkName	Active link name
	 * @param formName			Associated form name
	 * @param server			ARSeverUser object for logged-in user
	 * @return ActiveLink       Handle to the active link created , NULL otherwise
	 */
	public static ActiveLink createActiveLink(String activeLinkName, String formName, ARServerUser server) {
		try{
			ActiveLink activeLink = new ActiveLink();
    		activeLink.setName(activeLinkName);
    		
    		// Add actions
    		List<ActiveLinkAction> actionList = new ArrayList<ActiveLinkAction>();
    		CloseWindowAction alc = new CloseWindowAction();
			actionList.add(alc);
			activeLink.setActionList(actionList);
			
			// Add reference to form
    		List<String> formList = new ArrayList<String>();
    		formList.add(formName);
    		activeLink.setFormList(formList);
    		
    		server.createActiveLink(activeLink);
    		System.out.format("ActiveLink %s successfully created.\n", activeLinkName);
    		return activeLink;
		} catch(Exception exp) {
    		exp.printStackTrace();
    		return null;
    	}
	}
	
	/**
	 * Creates a character field on form
	 * @param fieldName        Name of the character field to be created
	 * @param fieldID          Database ID of the field to be created
	 * @param formName         Name of the form on which to create the field
	 * @param server           ARServerUser object for logged-in user
	 * @return CharacterField  Handle to the created field, NULL otherwise 
	 */
	public static CharacterField createCharField(String fieldName, int fieldID, String formName, ARServerUser server) {
		try {
			CharacterField charField = new CharacterField();
			charField.setName(fieldName);
			charField.setFieldID(fieldID);
			
    		CharacterFieldLimit cfl = new CharacterFieldLimit();
    		cfl.setFullTextOption(Constants.AR_FULLTEXT_OPTIONS_INDEXED);
    		cfl.setLengthUnits(Constants.AR_LENGTH_UNIT_BYTE);
    		cfl.setMaxLength(100);
    		cfl.setMenuStyle(Constants.AR_MENU_OVERWRITE);
    		cfl.setQBEMatch(Constants.AR_QBE_MATCH_EQUAL);
    		charField.setFieldLimit(cfl);
			
			// Making sure field gets added to the DefaultView.
			// To add field in default view, need to put default view id in field's property map.
			List<Integer> viewList = server.getListView(formName, 0);
			int viewid = viewList.get(0);
			
			DisplayInstanceMap dim = new DisplayInstanceMap();
			DisplayPropertyMap propMap = new DisplayPropertyMap();
			propMap.put(Constants.AR_DPROP_LABEL, new Value(fieldName));
			dim.put(viewid, propMap);
			
			charField.setDisplayInstance(dim);
		
			RegularFieldMapping rfm = new RegularFieldMapping();
			charField.setFieldMap(rfm);
			// Mandatory Field
			charField.setFieldOption(1);
			
			// Any user can enter data
			charField.setCreateMode(1);
			charField.setForm(formName);
			
			RegularFieldMapping fieldMap = new RegularFieldMapping();
			charField.setFieldMap(fieldMap);
			
			server.createField(charField, false);
			System.out.format("Field %s successfully added.\n", charField.getName());    	
    	
			return charField;
		} catch(Exception exp) {
			exp.printStackTrace();
    		return null;
    	}
	}
	
	/**
	 * Creates form overlay
	 * @param formName	Name of the form for which overlay needs to be created.
	 * @param server	ARServerUser object for logged-in user
	 * @return true if successful, false otherwise
	 */
	public static boolean createFormOverlay(String formName, ARServerUser server) {
    	try {
    		System.out.println("Inside FORM OVERLAY Creation Section");
    		
    			OverlaidInfo oi = new OverlaidInfo();
    			oi.setName(formName);
    			oi.setFormName(formName);
    			oi.setObjType(Constants.AR_STRUCT_ITEM_SCHEMA);
    			oi.setInheritMask(6);
    			oi.setAppendMask(0);
    			
    			server.setBaseOverlayFlag(false);
    			server.setOverlayFlag(true);
    			server.setOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));
    			server.setDesignOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));
    			server.createOverlay(oi);
    			System.out.println("FORM OVERLAY Created Successfully!");
    			return true;
    	} catch(Exception exp){
    		exp.printStackTrace();
    		return false;
    	 	}
    }
	
	/**
	 * Creates view overlay
	 * @param formName	Name of the form for which overlay needs to be created
	 * @param server	ARServerUser object for logged-in user
	 * @return true if successful, false otherwise
	 */
	public static boolean createViewOverlay(String formName, ARServerUser server){
    	try {
    		System.out.println("Inside FORM_VIEW Overlay Creation Section");
    		
    		String fN = formName+"__o";
    		List<Integer> viewList = server.getListView(formName, 0);
    		int viewid = viewList.get(0);
    		
    		OverlaidInfo oi = new OverlaidInfo();
    		
    		// initialise OverlaidInfo
    		oi.setObjType(Constants.AR_STRUCT_ITEM_VUI);
    		oi.setName(fN);
    		oi.setFormName(formName); //fN);
    		oi.setId(viewid);
    		oi.setAppendMask(0);    		

    		server.setBaseOverlayFlag(false);
    		server.setOverlayFlag(true);
    		server.setOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));
    		server.setDesignOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));

    		// workaround for slow servers.
    		ARUtils.sleep();
    		server.createOverlay(oi);
    		System.out.println("FORM_VIEW OVERLAY Created Successfully!");
    		return true;
    	} catch(Exception exp) {
    		System.out.println(exp);
    		return false;
    	}
    }
	
	/**
	 * Creates Archive for specific time.
	 * @param formName	Name of the form for which Archive needs to be created
	 * @param server    ARServerUser object for logged-in user
	 * @return true if successful, false otherwise
	 */
	public static boolean createArchive(String formName, ARServerUser server){
    	try {
    		System.out.println("Inside Archive Creation Section");
    		// Create bit sets for time
    		BitSet monthDays = new BitSet(31);
    		monthDays.set(9);
    		
    		BitSet weekDays = new BitSet(7);
    		weekDays.set(4);
    		
    		BitSet hours = new BitSet(24);
    		hours.set(3);
    		EscalationTime et = new EscalationTime(monthDays, weekDays, hours, 10);
    		Form theForm = server.getForm(formName);
    		
    		System.out.format("Got form: %s.\n", formName);
    		ArchiveInfo ai = new ArchiveInfo();
    		ai.setEnable(true);
    		ai.setArchiveType(ArchiveInfo.AR_ARCHIVE_FORM);
    		ai.setArchiveTmInfo(et);
    		ai.setArchiveFrom(formName);
    		ai.setArchiveDest(formName + "Archive");
    		theForm.setArchiveInfo(ai);
    		server.setForm(theForm);
    		System.out.println("FORM_VIEW OVERLAY Created Successfully!");
    		
    		return true;
    	} catch(Exception exp) {
    		exp.printStackTrace();
    		return false;
    	}
    }
	
	/**
	 * Switches mode from "Base Development" mode to "Best Practice" mode.
	 * @param server ARSeverUser object for logged-in user.
	 * @return true if success, false otherwise.
	 */
	public static boolean switchToBestPracticeMode(ARServerUser server) {
		try {
			server.setBaseOverlayFlag(false);
			server.setOverlayFlag(true);
			server.setOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));
			server.setDesignOverlayGroup(String.valueOf(Constants.AR_GROUP_ID_ADMINISTRATOR));
			return true;
		} catch(Exception exp) {
    		exp.printStackTrace();
    		return false;
    	}
	}
	
	/**
	 * Delete an entry in the form
	 * @param formName      Name of the form from which to delete the entry
	 * @param qualifier	    Qualifier to fetch the entry
	 * @param server        ARServerUser object for logged-in user
	 * @return boolean      true if entry is deleted successfully, false otherwise
	 */
	public static boolean deleteEntry(String formName, String qualifier, ARServerUser server) {
		boolean retVal = true;
    	try {
	    	String entryid = ""; 
	    	OutputInteger nMatches = new OutputInteger();
	        QualifierInfo qual = server.parseQualification(formName, qualifier);
	        
	    	List <EntryListInfo>entries=server.getListEntry(formName, qual, 0, 100, null, null, false, nMatches);
	    	for (EntryListInfo entryListInfo : entries) {
	            entryid=entryListInfo.getEntryID();
	            break;
	    	}
	    	// Last parameter
	    	// deleteOption - specify 0 for this parameter (reserved for future use).
	    	server.deleteEntry(formName, entryid, 0);
    	} catch(Exception e) {
	    	e.printStackTrace();
	    	retVal = false;
    	}
    	
    	return retVal;
    }
	
	/**
	 * Modifies short description for an entry with specified qualification.
	 * @param formName			Name of the form under consideration
	 * @param qualifier			Search criteria for entry.
	 * @param newDescription	New description for the entry.
	 * @param server			ARServerUser object for logged-in user
	 * @return	true if entry is deleted successfully, false otherwise
	 */
	public static boolean modifyEntryShortDescription(String formName, String qualifier, String newDescription, ARServerUser server) {
		boolean retVal = true;
    	try {
	    	String entryid=""; 
	    	OutputInteger nMatches = new OutputInteger();
	        QualifierInfo qual = server.parseQualification(formName, qualifier);
	        
	    	List <EntryListInfo>entries=server.getListEntry(formName,qual, 0, 100, null, null, false, nMatches);
	    	for (EntryListInfo entryListInfo : entries) {
	            entryid=entryListInfo.getEntryID();
	            break;
	    	}
	    	
	    	Entry entry = server.getEntry(formName, entryid, null);
			entry.put(Constants.AR_CORE_SHORT_DESCRIPTION, new Value(newDescription));
			server.setEntry(formName, entryid, entry, null, 0);
			System.out.format("Entry #%s modified successfully.\n", entryid);
			
    	} catch(Exception e) {
	    	e.printStackTrace();
	    	retVal = false;
    	}
    	return retVal;
    }
	
	/**
	 * Returns entry for a specific search
	 * @param formName	Form under consideration.
	 * @param qualifier	Search criteria.
	 * @param server ARServerUser object for logged-in user
	 * @return	true if entry is deleted successfully, false otherwise
	 */
	public static Entry getEntry(String formName, String qualifier, ARServerUser server) {
		try {
	    	String entryid=""; 
	    	OutputInteger nMatches = new OutputInteger();
	        QualifierInfo qual = server.parseQualification(formName, qualifier);
	        
	    	List<EntryListInfo> entries = server.getListEntry(formName,qual, 0, 100, null, null, false, nMatches);
	    	if(nMatches.intValue() == 0) return null;
	    	
	    	for (EntryListInfo entryListInfo : entries) {
	            entryid = entryListInfo.getEntryID();
	            break;
	    	}
	    	
	    	return server.getEntry(formName, entryid, null);
    	} catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
    	}
	}

	/**
	 * Exports form to given file.
	 * @param fileName	File name for form to export.
	 * @param formName	Form to be exported.
	 * @return true if successfully exported the form, false otherwise.
	 */
	/*public static boolean exportForm(String fileName, String formName) {
    	try {
    		ConfigReader cr = new ConfigReader();
    		
    		// set definition option details.
	  		DefinitionOptions dos = new DefinitionOptions();
	  		dos.setServer(cr.getARServer());
	  		dos.setUser(cr.getUsername());
	  		dos.setPassword(cr.getPassword());
	  		dos.add(DefinitionItemType.FORM, formName, RelatedType.ALL);
	  		dos.setFileName(cr.getBaseFilePath() + fileName);
	  		dos.setOperation(Operation.EXPORT);
	  		
	  		DefinitionExport de = new DefinitionExport();
	  		de.setOptions(dos);
	  		de.exportObjects();
	  		
	  		return true;
    	} catch (Exception e){
    		e.printStackTrace();
    		return false;
		}
    }
	*/
	/**
	 * 
	 * @param fileName	Form definition file name.
	 * @param formName	Form to be imported.
	 * @return true if successfully imported the form, false otherwise.
	 */
	/* public static boolean importForm(String fileName, String formName) {
    	try {
    		ConfigReader cr = new ConfigReader();
    		
    		// set definition option details.
	  		DefinitionOptions dos = new DefinitionOptions();
	  		dos.setServer(cr.getARServer());
	  		dos.setUser(cr.getUsername());
	  		dos.setPassword(cr.getPassword());
	  		dos.add(DefinitionItemType.FORM, formName, RelatedType.ALL);
	  		dos.setFileName(cr.getBaseFilePath() + fileName);
	  		dos.setOperation(Operation.IMPORT);
	  		
	  		DefinitionImport di = new DefinitionImport();
	  		di.setOptions(dos);
	  	    di.getOptions().setOverwrite(true);
	  		di.importObjects();
	  		
	  		return true;
    	} catch (Exception e){
    		e.printStackTrace();
    		return false;
		}
    }
	*/
	/**
	 * Creates filter on form1.
	 * @param filterName	Name of the filter.
	 * @param formName1		Form name on which the filter will get created
	 * @param formName2		Second form under consideration.
	 * @param qualification	criteria for the filter.
	 * @param server		ARServerUser object for logged-in user
	 * @return	true if successful, false otherwise.
	 */
	public static boolean createFilter(String filterName, String formName1, String formName2, String qualification, ARServerUser server) {
		try {
			ConfigReader cr = new ConfigReader();
			Filter filter = new Filter();
			filter.setName(filterName);
			filter.setOwner(cr.getUsername());
			filter.setOrder(100);
			
			List<String> formList = new ArrayList<String>();
			formList.add(formName1);
			filter.setFormList(formList);
			filter.setPrimaryForm(formName1);
			filter.setEnable(true);
			filter.setOpSet(Constants.AR_OPERATION_SET);

			SetFieldsFromForm sfff = new SetFieldsFromForm();
			sfff.setFromServer(cr.getARServer());
			sfff.setReadValuesFrom(formName2);

			QualifierInfo qi = new QualifierInfo();
			qi = server.parseSetIfQualification(formName1, formName2, qualification);
			sfff.setSetIfQualification(qi);
			sfff.setNoMatchOption(1);
			sfff.setMultiMatchOption(3);
			sfff.setAssignmentByMatchingIds(true);
		  
			List<FilterAction> actionListX = new ArrayList<FilterAction>();
			actionListX.add(sfff);
			filter.setActionList(actionListX);      		  
			server.createFilter(filter);
			System.out.format("FILTER %s successfully created.\n", filterName);
			return true;
		} catch(Exception exp) {
			exp.printStackTrace();
			return false;
		}
    }
	
	public void myUtilityMethod() {
	}
}