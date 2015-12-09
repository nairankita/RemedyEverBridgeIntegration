package com.vyomlabs.utils;

import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigReader {
	private final static Logger log = Logger.getLogger(ConfigReader.class.getName());
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getARServer() {
		return arserver;
	}
	public int getSleepTime() {
		return sleepTime;
	}
	public String getBaseFilePath() {
		return baseFilePath;
	}	
	public String getTimeZone() {
		return timeZone;
	}

	private String username;
	private String password;
	private String arserver;
	private int sleepTime;
	private String baseFilePath;
	private String timeZone;
		
	public ConfigReader() {
		try {
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
			username = props.get("username").toString();
			password = props.get("password").toString();
			arserver = props.get("arserver").toString();
			arserver = props.get("arserver").toString();
			timeZone = props.get("TimeZone").toString();
			
			log.info("Properties >> USER : "+username+", PASSWORD : "+password+", SERVER : "+arserver+", Time Zone : "+timeZone);
//			baseFilePath = props.get("basefilepath").toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}