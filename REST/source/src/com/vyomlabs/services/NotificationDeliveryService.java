package com.vyomlabs.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.OutputInteger;
import com.bmc.arsys.api.QualifierInfo;
import com.bmc.arsys.api.Value;
import com.vyomlabs.model.NotificationDelivery;
import com.vyomlabs.model.Response;
import com.vyomlabs.model.RestPostResponse;
import com.vyomlabs.utils.ARUtils;
import com.vyomlabs.utils.ConfigReader;

public class NotificationDeliveryService {
	final Logger log = Logger.getLogger(NotificationDeliveryService.class.getName());
	private ARServerUser server =  ARUtils.login();
	
	private NotificationDeliveryService() {
	}

	private static NotificationDeliveryService instance = null;

	public static NotificationDeliveryService getInstance() {
		if (instance == null)
			instance = new NotificationDeliveryService();
		return instance;
	}

	public RestPostResponse createNotifDelivery(NotificationDelivery nd) {
		log.info("START createNotifDelivery");		
		RestPostResponse response = new RestPostResponse();
		
		List<Response> responses = nd.getResponses();
		Response r = null;
		String name[] = new String[2];
		if(responses.size() != 0) {
			r = responses.get(0);
			name[0] = r.getFirstName();
			name[1] = r.getLastName();
		}
		String message = "";
		
		String notificationId = null;			
		int[] fieldIds = { 1 };
		QualifierInfo qual1; OutputInteger nMatches = new OutputInteger();
		
		Entry searchPeopleEntry = null;
		try {
			log.debug("Qualification on CTM:People >> ");	
			log.debug("CTM:People >> qualOnPeopleForm : "+"'1000000019' =  \"" + name[0].trim() + "\" AND '1000000018' = \"" + name[1].trim() + "\"");
			QualifierInfo qualOnPeopleForm = server.parseQualification("CTM:People","'1000000019' =  \"" + name[0].trim() + "\" AND '1000000018' = \"" + name[1].trim() + "\"");
			if(qualOnPeopleForm == null) {
				log.debug("CTM:People >> parseQualification NULL.");
			}
			log.debug("CTM:People >> parseQualification completed.");
			
			searchPeopleEntry = server.getOneEntryObject("CTM:People", qualOnPeopleForm, null, fieldIds, true, nMatches);
			
			String fetchedPeopleEntryID = searchPeopleEntry.get(1) != null ? searchPeopleEntry.get(1).toString() : null;
			
			if(fetchedPeopleEntryID == null) {
				message = "User \""+name[0].trim()+" "+name[1].trim()+"\" does not exists in Remedy People Form.";				
				response.setStatus("Failure");
				response.setMessage(message);
				log.debug(message);
			}
			else {
				Entry peopleEntryValueList = server.getEntry("CTM:People", fetchedPeopleEntryID, null);
				String login_id = peopleEntryValueList.get(4).toString();
				log.debug("CTM:People >> Login ID : "+login_id);
				
				log.debug("Qualification on EB_Response_table >> " + "'536870913' =  \"" + nd.getId() + "\"");
				qual1 = server.parseQualification("EB_Response_table", "'536870913' =  \"" + nd.getId() + "\"");
				log.debug("EB_Response_table >> parseQualification completed.");
				
				Entry searchEntry = server.getOneEntryObject("EB_Response_table", qual1, null, fieldIds, true, nMatches);
				log.info("EB_Response_table >> Entry Id found : "+searchEntry.getEntryId());
				
				if (searchEntry.getEntryId() == null) {
					log.info("Updating Assignee");
					
					qual1 = server.parseQualification("HPD:Help Desk", "'536870924' =  \"" +nd.getId() + "\"");
					searchEntry = server.getOneEntryObject("HPD:Help Desk", qual1, null, fieldIds, true, nMatches);
					String fetchedEntryID = searchEntry.get(1).toString();
					Entry entryFieldValueList = server.getEntry("HPD:Help Desk", fetchedEntryID, null);
					String entryID = entryFieldValueList.get(1).toString();
					
					Entry modifyEntry = new Entry();
					modifyEntry.put(1000000076, new Value("MODIFY"));
					modifyEntry.put(4, new Value(login_id));
					modifyEntry.put(1000000218, new Value(name[0] + " " + name[1]));
					
					server.setEntry("HPD:Help Desk", entryID, modifyEntry, null, 0);
					message = "Updated Assignee as \""+name[0].trim()+" "+name[1].trim()+"\".";
					log.info(message);
				}
				
				Entry newEntry = new Entry();
				newEntry.put(536870939, new Value(nd.getPhaseStatus().getName())); // phase
				newEntry.put(536870940, new Value(name[0].trim()+" "+name[1].trim())); //  contact_name
				newEntry.put(536870941, new Value(r.getDeliveryMethod())); // delivery_method
				
				ConfigReader cr = new ConfigReader();
				String timeZone = cr.getTimeZone();
				TimeZone tz = TimeZone.getTimeZone(timeZone); 				
				String timeZoneDisplayName = tz.getDisplayName();
				String timeZoneId = tz.getID();				
				log.info("Time Zone : "+timeZoneDisplayName+", "+timeZoneId);
				long epoch = Long.parseLong( r.getTime() );
				Date requiredDate = new Date( epoch);				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				String convertedDate = sdf.format(requiredDate);
				log.info("After Epoch Conversion : "+convertedDate);				
				sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				sdf.setTimeZone(tz);
				convertedDate = sdf.format(requiredDate);
				log.info("After TimeZone Conversion : "+convertedDate);
				newEntry.put(536870914, new Value(convertedDate+" "+timeZoneId)); // /time
				
				newEntry.put(536870915, new Value(r.getAttemptNumber())); // attempt_number
				newEntry.put(536870913, new Value(String.valueOf(nd.getId()))); // eb_incident_id
//				newEntry.put(536870916, new Value(String.valueOf(nd.getId()))); // eb_incident_id
				String interfaceEntryID = server.createEntry("EB_Response_table", newEntry); // creating_new_entry_in_the_form_with_the_above_value
				
				// Get notification Id from Entry ID
				Entry searchEntryCreate = server.getEntry("EB_Response_table", interfaceEntryID, null);
				notificationId = searchEntryCreate.get(1).toString();				
				if (notificationId != null) {
					message = message+"Acknowledgement created successfully.";
					response.setStatus("Success");
					response.setMessage(message);
					log.info(message);
				}
				ARUtils.logout(server);
				return response;
			}
		} catch (Exception e) {
			log.debug("CATCH");
			message = "Failed to create acknowledgement. Exception : "+e;			
			response.setStatus("Failure");
			response.setMessage(message);
			log.debug(message);
			ARUtils.logout(server);
			return response;
		}
//		finally {
//			log.debug("FINALLY");
//			ARUtils.logout(server);
//			if (notificationId != null) {
//				message = message+"Acknowledgement created successfully.";
//				response.setStatus("Success");
//				response.setMessage(message);
//				log.info(message);
//			}
//			
//			return response;
//		}		
		
		return response;
	}

}