package com.eb.incident.models;

import com.eb.incident.enums.NotificationType;

public class BroadcastTemplate {

	private BroadcastContacts broadcastContacts;
	private NotificationType type;

	public BroadcastTemplate() {
	}

	public BroadcastContacts getBroadcastContacts() {
		return this.broadcastContacts;
	}

	public void setBroadcastContacts(BroadcastContacts in) {
		this.broadcastContacts = in;
	}

	public NotificationType getType() {
		return this.type;
	}

	public void setType(NotificationType in) {
		this.type = in;
	}

}

