package com.eb.incident.controllers;

import com.eb.utils.Authenticator;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

public class EbIncidentCreate {
	private static EbIncidentCreate instance = null;

	private EbIncidentCreate() {
	}

	public static EbIncidentCreate getInstance() {
		if (instance == null)
			instance = new EbIncidentCreate();

		return instance;
	}

	public ArrayList<Value> createIncidentinEB(List<Value> paramList) {
		ConfigReader cr = new ConfigReader();
		String targetUri = cr.getBaseUri() + "incidents/" + cr.getOrganizationId();
		System.out.println("Target URI :\n" + targetUri);

		Authenticator auth = new Authenticator(cr.getUsername(), cr.getPassword());
		String cred = auth.getEncodedCredentials().toString();
		System.out.println("Credentials :\n" + cred);

		String templateId = cr.getTemplateId();
		String templateName = cr.getTemplateName();
		System.out.println("Template Name : " + templateName + ", Template ID : " + templateId);

		String groupId = cr.getGroupIdForNotifications();
		System.out.println("Group Id For Notifications : " + groupId);

		String incidentId = ((Value) paramList.get(0)).getValue().toString();
		String company = ((Value) paramList.get(1)).getValue().toString();
		String customer = ((Value) paramList.get(2)).getValue().toString(); // Contact
																			// of
																			// everbridge
		String summary = ((Value) paramList.get(4)).getValue().toString();
		String notes = paramList.get(3).getValue() != null ? ((Value) paramList.get(3)).getValue().toString().trim()
				: summary;
		String priority = "P_" + ((Value) paramList.get(5)).getValue().toString();

		if (notes == null || "".equals(notes))
			notes = summary;

		System.out.println("Parameters : \nincidentId : " + incidentId);
		System.out.println("company : " + company);
		System.out.println("customer : " + customer);
		System.out.println("notes : " + notes);
		System.out.println("summary : " + summary);
		System.out.println("priority : " + priority);

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(targetUri);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("Authorization", cred);

		PhaseDefinition phaseDefinition = new PhaseDefinition();
		phaseDefinition.setId(EBIncidentPhase.Launch.getPhaseId());
		List<PhaseDefinition> phaseDefnList = new ArrayList<PhaseDefinition>();
		phaseDefnList.add(phaseDefinition);

		List<Long> groupsList = new ArrayList<>();
		groupsList.add(new Long(groupId));
		// List<Long> contactsList = new ArrayList<>();
		// contactsList.add(1323854949519628L);

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
		formVariableItem.setVariableId(884011643700411L);
		formVariableItem.setVariableName("Incident ID");
		valList = new ArrayList<String>();
		valList.add(incidentId);
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

		/*
		 * if("2203425597039357".equals(templateId)) { formVariableItem = new
		 * FormVariableItem(); formVariableItem.setVariableId(884011643700407L);
		 * formVariableItem.setVariableName("CI"); valList = new
		 * ArrayList<String>(); valList.add(configItem);
		 * formVariableItem.setVal(valList);
		 * formVariableItemList.add(formVariableItem); }
		 * 
		 * formVariableItem = new FormVariableItem();
		 * formVariableItem.setVariableId(884011643700470L);
		 * formVariableItem.setVariableName("Prod Cat - Tier 1"); valList = new
		 * ArrayList<String>(); valList.add(prodCatTier1);
		 * formVariableItem.setVal(valList);
		 * formVariableItemList.add(formVariableItem);
		 */

		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700408L);
		formVariableItem.setVariableName("Incident Summary");
		valList = new ArrayList<String>();
		valList.add(summary);
		formVariableItem.setVal(valList);
		formVariableItemList.add(formVariableItem);

		formVariableItem = new FormVariableItem();
		formVariableItem.setVariableId(884011643700409L);
		formVariableItem.setVariableName("Incident Notes");
		valList = new ArrayList<String>();
		valList.add(notes);
		formVariableItem.setVal(valList);
		formVariableItemList.add(formVariableItem);

		FormTemplate formTemplate = new FormTemplate();
		formTemplate.setFormVariableItems(formVariableItemList);

		PhaseTemplate phaseTemplate = new PhaseTemplate();
		phaseTemplate.setTemplateId(new Long(templateId));
		phaseTemplate.setFormTemplate(formTemplate);
		phaseTemplate.setPhaseDefinitions(phaseDefnList);
		phaseTemplate.setBroadcastTemplate(broadcastTemplate);

		IncidentPhase incidentPhase = new IncidentPhase();
		incidentPhase.setPhaseTemplate(phaseTemplate);

		List<IncidentPhase> incidentPhaseList = new ArrayList<IncidentPhase>();
		incidentPhaseList.add(incidentPhase);

		Incident i = new Incident();
		i.setName(incidentId + " - " + summary);
		i.setIncidentAction("Launch");
		i.setIncidentPhases(incidentPhaseList);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		System.out.println("Json Input :\n" + gson.toJson(i));

		Response postResponse = webTarget.request(MediaType.APPLICATION_JSON).header("Authorization", cred)
				.post(Entity.entity(gson.toJson(i), MediaType.APPLICATION_JSON));

		String output = postResponse.readEntity(String.class);
		System.out.println("Output :\n" + output);

		EbModificationResponse postResponseJson = gson.fromJson(output, EbModificationResponse.class);
		if (postResponseJson == null)
			System.out.println("Json Response is null");

		ArrayList<Value> returnList = new ArrayList<Value>();

		if (postResponse.getStatus() != 200) {
			System.out.println("Failed : HTTP error code : " + postResponse.getStatus());
			throw new RuntimeException("Failed : HTTP error code : " + postResponse.getStatus());
		}

		String eb_Id = postResponseJson.getId() != null ? postResponseJson.getId() : null;
		System.out.println("ID : " + eb_Id);
		String eb_InstanceUri = postResponseJson.getInstanceUri() != null ? postResponseJson.getInstanceUri() : null;
		System.out.println("Instance Uri : " + eb_InstanceUri);
		eb_InstanceUri = "https://manager.everbridge.net/incidents/incident/" + eb_Id + "/notifications/";
		System.out.println("Constructed Instance Uri : " + eb_InstanceUri);

		if (postResponse.getStatus() == 200 && postResponseJson != null) {
			returnList.add(new Value(eb_Id));
			returnList.add(new Value(eb_InstanceUri));
		}

		return returnList;
	}

}
