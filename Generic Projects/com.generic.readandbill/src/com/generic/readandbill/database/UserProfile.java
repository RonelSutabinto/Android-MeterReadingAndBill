package com.generic.readandbill.database;

public class UserProfile {
	protected long id;
	protected String name;
	
	public UserProfile() {
		this.id = -1;
		this.name = "";
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
