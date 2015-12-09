package com.eb.incident.enums;

public enum EBbIncidentPriority {
	P_0("Critical"), P_1("High"), P_2("Medium"), P_3("Low");
	
	private String name;

	private EBbIncidentPriority(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
