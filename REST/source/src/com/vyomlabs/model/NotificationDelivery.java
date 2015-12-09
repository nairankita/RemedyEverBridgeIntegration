package com.vyomlabs.model;

import java.util.List;

public class NotificationDelivery {
	private long id;
	private String name;
	private long organizationId;
	private String incidentStatus;
	private boolean hasJournal;
	private int duration;
	private PhaseStatus phaseStatus;
	private List<Response> responses;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public String getIncidentStatus() {
		return incidentStatus;
	}
	public void setIncidentStatus(String incidentStatus) {
		this.incidentStatus = incidentStatus;
	}
	public boolean isHasJournal() {
		return hasJournal;
	}
	public void setHasJournal(boolean hasJournal) {
		this.hasJournal = hasJournal;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public PhaseStatus getPhaseStatus() {
		return phaseStatus;
	}
	public void setPhaseStatus(PhaseStatus phaseStatus) {
		this.phaseStatus = phaseStatus;
	}
	public List<Response> getResponses() {
		return responses;
	}
	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
	@Override
	public String toString()  {
		return "{\nid : "+this.id+",\nname : "+this.name+",\norganizationId : "+this.organizationId+",\nincidentStatus : "+this.incidentStatus+",\nhasJournal : "+this.hasJournal+",\nduration : "+this.duration+"\nphaseStatus: "+this.phaseStatus.toString()+"\n}";
	}
}
