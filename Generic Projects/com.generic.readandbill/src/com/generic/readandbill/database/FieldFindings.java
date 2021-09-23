package com.generic.readandbill.database;

public class FieldFindings {
	long id;	
	String fCode;
	String fDescription;
	
	public FieldFindings() {
		this.id = -1;
		this.fCode = "0";
		this.fDescription = "";
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getfCode() {
		return fCode;
	}
	public void setfCode(String fCode) {
		this.fCode = fCode;
	}
	public String getfDescription() {
		return fDescription;
	}
	public void setfDescription(String fDescription) {
		this.fDescription = fDescription;
	}
	public String toString(){
		return fCode.toString() + " : " + fDescription;
	}
}
