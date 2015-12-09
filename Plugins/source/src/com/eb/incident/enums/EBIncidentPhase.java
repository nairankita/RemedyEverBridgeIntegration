package com.eb.incident.enums;

public enum EBIncidentPhase {
	Launch(1001),Update(1002),CloseWithNotification(1003),CloseWithoutNotification(1003);
	
	private int phaseId;

	private EBIncidentPhase(int phaseId) {
		this.phaseId = phaseId;
	}

	public int getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(int phaseId) {
		this.phaseId = phaseId;
	}
}
