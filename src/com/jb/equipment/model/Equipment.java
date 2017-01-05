package com.jb.equipment.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jb.model.PersistClass;

public class Equipment extends PersistClass {
	
	@JsonProperty("GUID")
	private String guid;

	
	@JsonProperty("CLSID")
	private String clsID;
	
	
	@JsonProperty("APPID")
	private String appID;



	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getClsID() {
		return clsID;
	}

	public void setClsID(String clsID) {
		this.clsID = clsID;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}



}
