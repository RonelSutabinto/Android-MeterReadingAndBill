package com.zaneco.readandbill.database;

public class HighPrev {
	
	String accountnumber_bh;
	String name_bh;
	double presentreadingKWH_bh;
	double previousreadingKWH_bh;
	String presentreadingDate_bh;
	String previousreadingDate_bh;
	double diff_bh;	
	double multiplier_bh;
	double kilowatthour_bh;
	double demand_bh;
	double totalbill_bh;
	String billmonth_bh;
	String wratecode_bh;
	String readingtype_bh;
	
	public HighPrev(){
		this.accountnumber_bh = "";
		this.name_bh = "";
		this.presentreadingKWH_bh = 0;
		this.previousreadingKWH_bh = 0;
		this.presentreadingDate_bh = "";
		this.previousreadingDate_bh = "";
		this.diff_bh = 0;
		this.multiplier_bh = 0;
		this.kilowatthour_bh = 0;
		this.demand_bh = 0;
		this.totalbill_bh = 0;
		this.billmonth_bh = "";
		this.wratecode_bh = "";
		this.readingtype_bh = "";
	}
	
	public String getAccountnumber(){
		return accountnumber_bh;
	}
	public void setAccountnumber(String accntnumber){
		this.accountnumber_bh = accntnumber;
	}
	public String getName(){
		return name_bh;
	}
	public void setName(String name){
		this.name_bh = name;
	}
	public double getPresentreadingKWH(){
		return presentreadingKWH_bh;
	}
	public void setPresentreadingKWH(double presentreading){
		this.presentreadingKWH_bh = presentreading;
	}
	public double getPreviousreadingKWH(){
		return previousreadingKWH_bh;
	}
	public void setPreviousreadingKWH(double previousreading){
		this.previousreadingKWH_bh = previousreading;
	}
	public String getPresentreadingDate(){
		return presentreadingDate_bh;
	}
	public void setPresentreadingDate(String presentreadingDate){
		this.presentreadingDate_bh = presentreadingDate;
	}
	public String getPreviousreadingDate(){
		return previousreadingDate_bh;
	}
	public void setPreviousreadingDate(String previousreadingDate){
		this.previousreadingDate_bh = previousreadingDate;
	}
	public double getDiff(){
		return diff_bh;
	}
	public void setDiff(double diff){
		this.diff_bh = diff;
	}
	public double getMultiplier(){
		return multiplier_bh;
	}
	public void setMultiplier(double multiplier){
		this.multiplier_bh = multiplier;
	}
	public double getKilowatthour(){
		return kilowatthour_bh;
	}
	public void setKilowatthour(double KWH){
		this.kilowatthour_bh = KWH;
	}
	public double getDemand(){
		return demand_bh;
	}
	public void setDemand(double demand){
		this.demand_bh = demand;
	}
	public double getTotalbill(){
		return totalbill_bh;
	}
	public void setTotalbill(double totalbill){
		this.totalbill_bh = totalbill;
	}
	public String getBillmonth(){
		return billmonth_bh;
	}
	public void setBillmonth(String billmonth){
		this.billmonth_bh = billmonth;
	}
	public String getWratecode(){
		return wratecode_bh;
	}
	public void setWratecode(String ratecode){
		this.wratecode_bh = ratecode;
	}
	public String getReadingtype(){
		return readingtype_bh;
	}
	public void setReadingtype(String readingtype){
		this.readingtype_bh = readingtype;
	}
}
