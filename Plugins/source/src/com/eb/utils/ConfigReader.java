package com.eb.utils;

import java.util.Properties;

public class ConfigReader {
	private String baseUri;
	private String organizationId;
	private String username;
	private String password;
	private String templateName;
	private String templateId;
	private String groupIdForNotifications;
	private int sleepTime;
	
	public ConfigReader() {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
			username = properties.get("username").toString();
			password = properties.get("password").toString();
			baseUri = properties.get("baseUri").toString();
			organizationId = properties.get("organizationId").toString();
			templateId = properties.get("templateId").toString();
			templateName = properties.get("templateName").toString();
			sleepTime = Integer.parseInt(properties.get("sleeptime").toString());
			groupIdForNotifications = properties.get("groupIdForNotifications").toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getBaseUri() {
		return baseUri;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public int getSleepTime() {
		return sleepTime;
	}

	public String getTemplateName() {
		return templateName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getGroupIdForNotifications() {
		return groupIdForNotifications;
	}
	
	
}
