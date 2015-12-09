package com.eb.incident.models;

import java.util.List;

public class Incident {

	private String name;
	private String incidentAction;
	private List<IncidentPhase> incidentPhases;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIncidentAction() {
		return incidentAction;
	}
	public void setIncidentAction(String incidentAction) {
		this.incidentAction = incidentAction;
	}
	public List<IncidentPhase> getIncidentPhases() {
		return incidentPhases;
	}
	public void setIncidentPhases(List<IncidentPhase> incidentPhases) {
		this.incidentPhases = incidentPhases;
	}
	
}

