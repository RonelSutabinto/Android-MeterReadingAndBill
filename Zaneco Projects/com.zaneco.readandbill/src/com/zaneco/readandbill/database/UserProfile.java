package com.zaneco.readandbill.database;

public class UserProfile extends com.generic.readandbill.database.UserProfile{
	protected String billmonth;
	protected String telno;
	
	public UserProfile() {
		super();
		this.billmonth = "";
		this.telno = "";
	}
	
	public String getBillmonth() {
		return billmonth;
	}
	public void setBillmonth(String billmonth) {
		this.billmonth = billmonth;
	}
	
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno_) {
		this.telno = telno_;
	}
	
	
}
