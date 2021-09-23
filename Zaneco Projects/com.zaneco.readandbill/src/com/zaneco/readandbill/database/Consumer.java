package com.zaneco.readandbill.database;

public class Consumer extends com.generic.readandbill.database.Consumer {

	protected double evatDiscount;
	protected String ocCode1;
	protected double ocAmount1;
	protected String ocCode2;
	protected double ocAmount2;
	protected String ocCode3;
	protected double ocAmount3;	
	protected double pantawidSubsidy;
	protected String pantawidRecoveryRef;
	protected double pantawidRecovery;
	protected Integer seniorCitizenNumMon;
	protected double differentialKwhr;
	protected double differentialKw;
	protected double rpttax;
	
	

	public Consumer() {
		super();

		this.evatDiscount = 0;		
		this.ocCode1 = "";
		this.ocAmount1 = 0;
		this.ocCode2 = "";
		this.ocAmount2 = 0;
		this.ocCode3 = "";
		this.ocAmount3 = 0;		
		this.pantawidSubsidy = 0;
		this.pantawidRecoveryRef = "";
		this.pantawidRecovery = 0;
		this.seniorCitizenNumMon = 0;
		this.differentialKwhr = 0;
		this.differentialKw = 0;
		this.rpttax = 0;
		
		
	}

	

	public Consumer(String accountNumber, String name, String address,
			String meterSerial) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.address = address;
		this.meterSerial = meterSerial;
	}

	public Integer getSeniorCitizenNumMon() {
		return seniorCitizenNumMon;
	}

	public void setSeniorCitizenNumMon(Integer seniorCitizenNumMon) {
		this.seniorCitizenNumMon = seniorCitizenNumMon;
	}

	public double getPantawidSubsidy() {
		return pantawidSubsidy;
	}

	public void setPantawidSubsidy(double pantawidSubsidy) {
		this.pantawidSubsidy = pantawidSubsidy;
	}

	
	public String getOcCode1() {
		return ocCode1;
	}

	public void setOcCode1(String ocCode1) {
		this.ocCode1 = ocCode1;
	}

	public String getOcCode2() {
		return ocCode2;
	}

	public void setOcCode2(String ocCode2) {
		this.ocCode2 = ocCode2;
	}

	public String getOcCode3() {
		return ocCode3;
	}

	public void setOcCode3(String ocCode3) {
		this.ocCode3 = ocCode3;
	}

	public double getOcAmount1() {
		return ocAmount1;
	}

	public void setOcAmount1(double ocAmount1) {
		this.ocAmount1 = ocAmount1;
	}

	public double getOcAmount2() {
		return ocAmount2;
	}

	public void setOcAmount2(double ocAmount2) {
		this.ocAmount2 = ocAmount2;
	}

	public double getOcAmount3() {
		return ocAmount3;
	}

	public void setOcAmount3(double ocAmount3) {
		this.ocAmount3 = ocAmount3;
	}

	public boolean isSeniorCitizen() {
		return seniorCitizenNumMon > 0;
	}

	public double getDifferentialKwhr() {
		return differentialKwhr;
	}

	public void setDifferentialKwhr(double differentialKwhr) {
		this.differentialKwhr = differentialKwhr;
	}

	public double getDifferentialKw() {
		return differentialKw;
	}

	public void setDifferentialKw(double differentialKw) {
		this.differentialKw = differentialKw;
	}

	public double getEvatDiscount() {
		return evatDiscount;
	}

	public void setEvatDiscount(double evatDiscount) {
		this.evatDiscount = evatDiscount;
	}

	public String getPantawidRecoveryRef() {
		return pantawidRecoveryRef;
	}

	public void setPantawidRecoveryRef(String pantawidRecoveryRef) {
		this.pantawidRecoveryRef = pantawidRecoveryRef;
	}

	public double getPantawidRecovery() {
		return pantawidRecovery;
	}

	public void setPantawidRecovery(double pantawidRecovery) {
		this.pantawidRecovery = pantawidRecovery;
	}

	public double getRptTax() {
		return rpttax;
	}

	public void setRptTax(double rpttax) {
		this.rpttax = rpttax;
	}

}
