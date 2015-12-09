package com.eb.incident.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.eb.utils.Authenticator;
import com.bmc.arsys.api.Value;
import com.eb.incident.enums.EBIncidentPhase;
import com.eb.incident.enums.EBbIncidentPriority;
import com.eb.incident.enums.NotificationType;
import com.eb.incident.models.BroadcastContacts;
import com.eb.incident.models.BroadcastTemplate;
import com.eb.incident.models.EbModificationResponse;
import com.eb.incident.models.FormTemplate;
import com.eb.incident.models.FormVariableItem;
import com.eb.incident.models.Incident;
import com.eb.incident.models.IncidentPhase;
import com.eb.incident.models.PhaseDefinition;
import com.eb.incident.models.PhaseTemplate;
import com.eb.utils.ConfigReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EbIncidentUpdate {
	private static EbIncidentUpdate instance = null;
	private EbIncidentUpdate(){}
	public static EbIncidentUpdate getInstance() {
		if(instance == null)
			instance = new EbIncidentUpdate();
		
		return instance;
	}
	
	public ArrayList<Value> updateIncidentInEB(List<Value> paramList) {
		ConfigReader cr = new ConfigReader();		
		
		Authenticator auth = new Authenticator(cr.getUsername(), cr.getPassword());
		String cred = auth.getEncodedCredentials().toString();
		System.out.println("Credentials :\n"+cred);
		
		String templateId = cr.getTemplateId();
		String templateName = cr.getTemplateName();
		System.out.println("Template Name : "+templateName+", Template ID : "+templateId);
		
		String groupId =cr.getGroupIdForNotifications();
		System.out.println("Group Id For Notifications : "+groupId);
		
		String incidentId = ((Value)paramList.get(0)).getValue().toString();
		String ebIncidentId = ((Value)paramList.get(1)).getValue().toString();
		String company = ((Value)paramList.get(2)).getValue().toString();
		String customer = ((Value)paramList.get(3)).getValue().toString(); // Contact of everbridge		
		String summary = ((Value)paramList.get(5)).getValue().toString();
		String notes = ((Value)paramList.get(4)).getValue().toString().trim();
		String priority = "P_"+((Value)paramList.get(6)).getValue().toString();
		String status = ((Value)paramList.get(7)).getValue().toString();
		
		System.out.println("Parameters : \nRemedy Incident ID : "+incidentId);
		System.out.println("EB Incident ID : "+ebIncidentId);
		System.out.println("company : "+company);
		System.out.println("customer : "+customer);
		System.out.println("notes : "+notes);
		System.out.println("summary : "+summary);
		System.out.println("priority : "+priority);		
		System.out.println("status : "+status);
		
		String targetUri = cr.getBaseUri()+"incidents/"+cr.getOrganizationId()+"/"+ebIncidentId;
		System.out.println("Target URI :\n"+targetUri);
		
		Client client = ClientBuilder.newClient();		
		WebTarget webTarget = client.target(targetUri);		
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("Authorization", cred);
		
		PhaseDefinition phaseDefinition = new PhaseDefinition();
		phaseDefinition.setId(EBIncidentPhase.Update.getPhaseId());		
		List<PhaseDefinition> phaseDefnList = new ArrayList<PhaseDefinition>();
		phaseDefnList.add(phaseDefinition);		
		
		List<Long> groupsList = new ArrayList<>();
		groupsList.add(new Long(groupId));
		List<Long> contactsList = new ArrayList<>();
		contactsList.add(1323854949519628L);
		
		BroadcastContacts broadcastContacts = new BroadcastContacts();
		broadcastContacts.setContactIds(null);		
		broadcastContacts.setGroupIds(groupsList);
		broadcastContacts.setFilterIds(null);
		broadcastContacts.setExcludedContactIds(null);		
		BroadcastTemplate broadcastTemplate = new BroadcastTemplate();
		broadcastTemplate.setBroadcastContacts(broadcastContacts);		
		broadcastTemplate.setType(NotificationType.Standard);
		
		List<FormVariableItem> formVariableItemList = new ArrayList<FormVariableItem>();
		FormVariableItem formVariableItem = null;
		List<String> valList = null;	
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(1323816294812646L);
		formVariableItem.setVariableName("Update");	
		valList = new ArrayList<String>();
		if("4".equals(status))  //Resolved 4
			valList.add("Incident is resolved.");
		else
			valList.add(notes);
		formVariableItem.setVal(valList);		
		formVariableItemList.add(formVariableItem);
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700462L);
		formVariableItem.setVariableName("Company");
		valList = new ArrayList<String>();
		valList.add(company);
		formVariableItem.setVal(valList);		
		formVariableItemList.add(formVariableItem);
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700482L);
		formVariableItem.setVariableName("Contact");
		valList = new ArrayList<String>();
		valList.add(customer);
		formVariableItem.setVal(valList);		
		formVariableItemList.add(formVariableItem);
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700406L);
		formVariableItem.setVariableName("Priority");
		valList = new ArrayList<String>();
		valList.add(EBbIncidentPriority.valueOf(priority).getName());
		formVariableItem.setVal(valList);
		formVariableItemList.add(formVariableItem);
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700408L);
		formVariableItem.setVariableName("Incident Summary");
		valList = new ArrayList<String>();
		valList.add(summary);
		formVariableItem.setVal(valList);	
		formVariableItemList.add(formVariableItem);
		
		FormTemplate formTemplate = new FormTemplate();
		formTemplate.setFormVariableItems(formVariableItemList);
		
		PhaseTemplate phaseTemplate = new PhaseTemplate();
		phaseTemplate.setTemplateId(884011643700116L);
		phaseTemplate.setFormTemplate(formTemplate);
		phaseTemplate.setPhaseDefinitions(phaseDefnList);
		phaseTemplate.setBroadcastTemplate(broadcastTemplate);		
		
		IncidentPhase incidentPhase = new IncidentPhase();
		incidentPhase.setPhaseTemplate(phaseTemplate);
		
		List<IncidentPhase> incidentPhaseList = new ArrayList<IncidentPhase>();
		incidentPhaseList.add(incidentPhase);
		
		Incident i = new Incident();
		i.setName(incidentId+" - "+summary);
		i.setIncidentAction("Update");	
		i.setIncidentPhases(incidentPhaseList);
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("Json Input :\n"+gson.toJson(i));
        
        Response putResponse =
				webTarget.request(MediaType.APPLICATION_JSON).header("Authorization", cred)
		                .put(Entity.entity(gson.toJson(i), MediaType.APPLICATION_JSON));        
        
        String output = putResponse.readEntity(String.class);
		System.out.println("Output :\n"+output);	
		
		EbModificationResponse putResponseJson = gson.fromJson(output,EbModificationResponse.class);   
		if(putResponseJson == null)
			System.out.println("Json Response is null");
		
		ArrayList<Value> returnList = new ArrayList<Value>();
		
        if (putResponse.getStatus() != 200) {
        	System.out.println("Failed : HTTP error code : "+ putResponse.getStatus());
        	returnList.add(new Value("Error in Update."));
		}        
        
        String eb_Id = putResponseJson.getId() != null ? putResponseJson.getId() : null;
        String eb_InstanceUri = putResponseJson.getInstanceUri() != null ? putResponseJson.getInstanceUri() : null;
        System.out.println("ID : "+eb_Id);
		System.out.println("Instance Uri : "+eb_InstanceUri);
        
        if (putResponse.getStatus() == 200 && putResponseJson != null) {
        	returnList.add(new Value("Incident Update Successful."));
        }
		
		return returnList;
	}	
}
