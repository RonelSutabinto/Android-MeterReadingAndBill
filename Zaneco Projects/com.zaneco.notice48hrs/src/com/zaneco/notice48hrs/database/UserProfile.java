package com.zaneco.notice48hrs.database;

public class UserProfile extends com.generic.notice48hrs.database.UserProfile{
    protected String address;
    protected String name;
    protected String addressB;

    public UserProfile() {
        super();
        this.address = "";
        this.name = "";
        this.addressB="";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }    
    
    public String getAddressB() {
        return addressB;
    }

    public void setAddressB(String address) {
        this.addressB = address;
    }  
  
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
