package com.eb.incident.enums;

public enum StatusReason {
	R_19000("Automated Resolution Reported"), R_15000("Customer Follow-Up Required"), R_11000("Future Enhancement"), R_14000("Monitoring Incident"),
	R_17000("No Further Action Required"), R_2200("Resolved by Causal Incident"), R_16000("Temporary Corrective Action");
	
	private String reason;

	private StatusReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
