package com.generic.notice48hrs.database;

public class UserProfile {
	protected String nameB;


    public UserProfile() {
        this.nameB = "";
    }

    public String getName() {
		return nameB;
	}
	public void setName(String name) {
		this.nameB = name;
	}
}
