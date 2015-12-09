package com.eb.incident.models;

import com.eb.incident.models.PhaseDefinition;
import java.util.List;
import com.eb.incident.models.FormTemplate;
import com.eb.incident.models.BroadcastTemplate;

public class PhaseTemplate {

	private long templateId;
	private List<PhaseDefinition> phaseDefinitions;
	private BroadcastTemplate broadcastTemplate;
	private FormTemplate formTemplate;
	
	public PhaseTemplate() {
	}

	public List<PhaseDefinition> getPhaseDefinitions() {
		return this.phaseDefinitions;
	}

	public void setPhaseDefinitions(List<PhaseDefinition> in) {
		this.phaseDefinitions = in;
	}

	public FormTemplate getFormTemplate() {
		return this.formTemplate;
	}

	public void setFormTemplate(FormTemplate in) {
		this.formTemplate = in;
	}

	public BroadcastTemplate getBroadcastTemplate() {
		return this.broadcastTemplate;
	}

	public void setBroadcastTemplate(BroadcastTemplate in) {
		this.broadcastTemplate = in;
	}

	public long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(long in) {
		this.templateId = in;
	}

}

