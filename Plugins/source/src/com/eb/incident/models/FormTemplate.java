package com.eb.incident.models;

import java.util.List;

public class FormTemplate {

	private List<FormVariableItem> formVariableItems;
	
	public FormTemplate() {
	}
	
	public List<FormVariableItem> getFormVariableItems() {
		return this.formVariableItems;
	}

	public void setFormVariableItems(List<FormVariableItem> in) {
		this.formVariableItems = in;
	}
}

