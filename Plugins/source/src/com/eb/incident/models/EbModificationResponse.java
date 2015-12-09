package com.eb.incident.models;

public class EbModificationResponse {
	private String message;
	private String id;
	private String baseUri;
	private String instanceUri;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public String getInstanceUri() {
		return instanceUri;
	}
	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}
	
	
}
