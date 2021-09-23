package com.zaneco.readandbill.database;

public class Billhistory {
	long code_bh;
	String accountnumber_bh;
	String name_bh;
	double presentreading_bh;
	double previousreading_bh;
	double diff_bh;	
	String billmonth_bh;
	double totalbill_bh;
	
	public Billhistory(){
		this.code_bh = 0;
		this.accountnumber_bh ="";
		this.name_bh="";
		this.presentreading_bh=0;
		this.previousreading_bh=0;
		this.diff_bh=0;		
		this.billmonth_bh="";
		this.totalbill_bh=0;
	}
	
	
	public long getCode(){
		return code_bh;
	}
	public void setCode(long code){
		this.code_bh = code;
	}
	
	public String getAccountnumber(){
		return accountnumber_bh;
	}
	public void setAccountnumber(String accountnumber){
		this.accountnumber_bh = accountnumber;
	}
	public String getName(){
		return name_bh;
	}
	public void setName(String name){
		this.name_bh = name;
	}
	public double getPresentreading(){
		return presentreading_bh;
	}
	public void setPresentreading(double presentreading){
		this.presentreading_bh = presentreading;
	}
	public double getPreviousreading(){
		return previousreading_bh;
	}
	public void setPreviousreading(double previousreading){
		this.previousreading_bh = previousreading;
	}
	public double getDiff(){
		return diff_bh;
	}
	public void setDiff(double diff){
		this.diff_bh = diff;
	}
	public String getBillmonth(){
		return billmonth_bh;
	}
	public void setBillmonth(String billmonth){
		this.billmonth_bh = billmonth;
	}
	public double getTotalbill(){
		return totalbill_bh;
	}
	public void setTotalbill(double totalbill){
		this.totalbill_bh = totalbill;
	}
}
