package com.eb.incident.enums;

public enum NotificationType {
	Standard("Standard"), Polling("Polling"), Conference("Conference"), Quota(
			"Quota");

	private String name;

	private NotificationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
