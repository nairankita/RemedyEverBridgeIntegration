package com.eb.incident.models;

import java.util.List;

public class BroadcastContacts {

	private List<String> filterNames;
	private List<Long> contactIds;
	private List<Long> groupIds;
	private List<Long> excludedContactIds;
	private List<Long> filterIds;

	public BroadcastContacts() {
	}

	public List<String> getFilterNames() {
		return this.filterNames;
	}

	public void setFilterNames(List<String> in) {
		this.filterNames = in;
	}

	public List<Long> getContactIds() {
		return this.contactIds;
	}

	public void setContactIds(List<Long> in) {
		this.contactIds = in;
	}

	public List<Long> getGroupIds() {
		return this.groupIds;
	}

	public void setGroupIds(List<Long> in) {
		this.groupIds = in;
	}

	public List<Long> getExcludedContactIds() {
		return this.excludedContactIds;
	}

	public void setExcludedContactIds(List<Long> in) {
		this.excludedContactIds = in;
	}

	public List<Long> getFilterIds() {
		return this.filterIds;
	}

	public void setFilterIds(List<Long> in) {
		this.filterIds = in;
	}

}

