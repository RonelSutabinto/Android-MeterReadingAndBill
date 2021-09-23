package com.generic.readandbill.database;

import android.text.format.Time;

public class Reading {

	protected long id;
	protected long idConsumer;
	protected double reading;
	protected long transactionDate;	
	protected Boolean isPrinted;
	protected String feedBackCode;	
	protected double demand;
	protected double demandRdng;
	protected long fieldFinding;
	protected double seniorCitizenDiscount;
	protected double kilowatthour;
	protected String remarks;
	protected double totalbill;
	protected String atmref;
	
	protected Boolean isAM;
	protected String readingDate;

	public Reading() {
		android.text.format.Time myTime = new android.text.format.Time();
		myTime.setToNow();
		this.id = -1;
		this.idConsumer = -1;
		this.reading = 0;
		this.transactionDate = 0;
		this.isPrinted = false;
		this.feedBackCode = "";
		this.demand = 0;
		this.demandRdng = 0;
		this.fieldFinding = -1;
		this.seniorCitizenDiscount = 0;
		this.kilowatthour = 0;
		this.totalbill = 0;
		this.remarks = "";
		this.isAM = false;
		this.readingDate = "";
		this.atmref = "";
	}
	
	public String getAtmref(){
		return atmref;
	}
	
	public void setAtmref(String atmref){
		this.atmref = atmref;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdConsumer() {
		return idConsumer;
	}

	public void setidConsumer(long idConsumer) {
		this.idConsumer = idConsumer;
	}

	public double getReading() {
		if (this != null){
			return reading;
		} else {
			return 0;
		}
	}

	public void setReading(double reading) {
		this.reading = reading;
	}
	
    public double getDemandRdng()
	{		
		if (this != null){
			return demandRdng;
		} else {
			return 0;
		}
	}
	public void setDemandRdng(double demandrdng)
	{
		this.demandRdng = demandrdng;
	}

	public double getDemand() {
		if (this != null) {
			return demand;
		} else {
			return 0.0;
		}
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Boolean getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(Boolean isPrinted) {
		this.isPrinted = isPrinted;
	}

	public String getFeedBackCode() {
		return feedBackCode;
	}

	public void setFeedBackCode(String feedBackCode) {
		this.feedBackCode = feedBackCode;
	}

	public long getFieldFinding() {
		return fieldFinding;
	}

	public void setFieldFinding(long fieldFinding) {
		this.fieldFinding = fieldFinding;
	}

	public double getSeniorCitizenDiscount() {
		return seniorCitizenDiscount;
	}

	public void setSeniorCitizenDiscount(double seniorCitizenDiscount) {
		this.seniorCitizenDiscount = seniorCitizenDiscount;
	}

	public double getKilowatthour() {
		return kilowatthour;
	}

	public void setKilowatthour(double kilowatthour) {
		this.kilowatthour = kilowatthour;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getTotalbill() {
		return totalbill;
	}

	public void setTotalbill(double totalbill) {
		this.totalbill = totalbill;
	}
	
	public Boolean getIsAM() {
		return isAM;
	}

	public void setIsAM(Boolean isAM) {
		this.isAM = isAM;
	}

	public String getReadingDate() {
		return readingDate;
	}

	public void setReadingDate(String readingDate) {
		this.readingDate = readingDate;
	}
}
