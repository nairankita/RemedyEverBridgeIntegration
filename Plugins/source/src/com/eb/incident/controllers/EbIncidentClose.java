package com.eb.incident.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmc.arsys.api.Value;
import com.eb.incident.models.EbModificationResponse;
import com.eb.incident.models.Incident;
import com.eb.utils.Authenticator;
import com.eb.utils.ConfigReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EbIncidentClose {
	private static EbIncidentClose instance = null;
	private EbIncidentClose(){}
	public static EbIncidentClose getInstance() {
		if(instance == null)
			instance = new EbIncidentClose();
		
		return instance;
	}
	
	public ArrayList<Value> closeIncidentInEB(List<Value> paramList) {
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
		String summary = ((Value)paramList.get(2)).getValue().toString();
		String statusReason = ((Value)paramList.get(3)).getValue() != null ? "R_"+((Value)paramList.get(3)).getValue().toString() : null; // Cause of everbridge		
		String resolution = ((Value)paramList.get(4)).getValue().toString();
		
		System.out.println("Parameters : \nRemedy Incident ID : "+incidentId);
		System.out.println("EB Incident ID : "+ebIncidentId);
		System.out.println("summary : "+summary);		
		System.out.println("statusReason : "+statusReason);
		System.out.println("resolution : "+resolution);
		
		String targetUri = cr.getBaseUri()+"incidents/"+cr.getOrganizationId()+"/"+ebIncidentId;
		System.out.println("Target URI :\n"+targetUri);
		
		Client client = ClientBuilder.newClient();		
		WebTarget webTarget = client.target(targetUri);		
		
		/*PhaseDefinition phaseDefinition = new PhaseDefinition();
		phaseDefinition.setId(EBIncidentPhase.CloseWithoutNotification.getPhaseId());		
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
		formVariableItem.setVariableId(1323816294812644L);
		formVariableItem.setVariableName("Cause");	
		valList = new ArrayList<String>();
		valList.add(statusReason);
		formVariableItem.setVal(valList);		
		formVariableItemList.add(formVariableItem);
		
		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(1323816294812645L);
		formVariableItem.setVariableName("Resolution");	
		valList = new ArrayList<String>();
		valList.add(resolution);
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
		incidentPhaseList.add(incidentPhase);*/
		
		Incident i = new Incident();
		i.setName(incidentId+" - "+summary);
		i.setIncidentAction("CloseWithoutNotification");	
//		i.setIncidentPhases(incidentPhaseList);
		
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
        	returnList.add(new Value("Error in Close."));
		}        
        
        if (putResponse.getStatus() == 200 && putResponseJson != null) {
        	System.out.println("Incident Closed Successfully.");
        	returnList.add(new Value("Incident Closed Successfully."));
        }
		
		return returnList;
	}	
}
