package com.eb.incident.models;

import java.util.List;

public class FormVariableItem {

	private List<String> val;
	private String variableName;
	private long variableId;
	
	public FormVariableItem() {
	}

	public List<String> getVal() {
		return this.val;
	}

	public void setVal(List<String> in) {
		this.val = in;
	}

	public String getVariableName() {
		return this.variableName;
	}

	public void setVariableName(String in) {
		this.variableName = in;
	}	

	public long getVariableId() {
		return this.variableId;
	}

	public void setVariableId(long in) {
		this.variableId = in;
	}

}

