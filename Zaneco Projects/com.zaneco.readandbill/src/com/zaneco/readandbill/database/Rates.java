package com.zaneco.readandbill.database;

public class Rates extends com.generic.readandbill.database.Rates{
			
	double ucNpcStrandedContCost;	
	double ucNpcStrandedDebts;	
	double ucDuStrandedContCost;	
	double ucEqTaxesAndRoyalties;	
	double ucCrossSubsidyRemoval;
	double icCrossSubsidy;
	double gram;
	double transSysAncRefund;	
	double transDemAncRefund;	
	double evatTransAncRefund;
	double evat;
	double evatGenCo;
	double evatTransCo;
	double evatSystemLoss;	
	double fitall;
	
	public Rates() {
		super();		
		this.ucNpcStrandedContCost = 0;
		this.ucNpcStrandedDebts = 0;
		this.ucDuStrandedContCost = 0;
		this.ucEqTaxesAndRoyalties = 0;
		this.ucCrossSubsidyRemoval = 0;
		this.icCrossSubsidy = 0;
		this.gram = 0;
		this.transSysAncRefund = 0;
		this.transDemAncRefund = 0;
		this.evatTransAncRefund = 0;
		this.evat = 0;
		this.evatGenCo = 0;
		this.evatTransCo = 0;
		this.evatSystemLoss = 0;		
		this.fitall = 0;
	}
	
	
	public double getUcNpcStrandedContCost() {
		return ucNpcStrandedContCost;
	}
	public void setUcNpcStrandedContCost(double ucNpcStrandedContCost) {
		this.ucNpcStrandedContCost = ucNpcStrandedContCost;
	}
	public double getUcNpcStrandedDebts() {
		return ucNpcStrandedDebts;
	}
	public void setUcNpcStrandedDebts(double ucNpcStrandedDebts) {
		this.ucNpcStrandedDebts = ucNpcStrandedDebts;
	}	
	public double getUcDuStrandedContCost() {
		return ucDuStrandedContCost;
	}
	public void setUcDuStrandedContCost(double ucDuStrandedContCost) {
		this.ucDuStrandedContCost = ucDuStrandedContCost;
	}
	public double getUcEqTaxesAndRoyalties() {
		return ucEqTaxesAndRoyalties;
	}
	public void setUcEqTaxesAndRoyalties(double ucEqTaxesAndRoyalties) {
		this.ucEqTaxesAndRoyalties = ucEqTaxesAndRoyalties;
	}
	public double getUcCrossSubsidyRemoval() {
		return ucCrossSubsidyRemoval;
	}
	public void setUcCrossSubsidyRemoval(double ucCrossSubsidyRemoval) {
		this.ucCrossSubsidyRemoval = ucCrossSubsidyRemoval;
	}
	public double getIcCrossSubsidy() {
		return icCrossSubsidy;
	}
	public void setIcCrossSubsidy(double icCrossSubsidy) {
		this.icCrossSubsidy = icCrossSubsidy;
	}
	public double getGram() {
		return gram;
	}
	public void setGram(double gram) {
		this.gram = gram;
	}
	public double getTransSysAncRefund() {
		return transSysAncRefund;
	}
	public void setTransSysAncRefund(double transSysAncRefund) {
		this.transSysAncRefund = transSysAncRefund;
	}	
	public double getTransDemAncRefund() {
		return transDemAncRefund;
	}
	public void setTransDemAncRefund(double transDemAncRefund) {
		this.transDemAncRefund = transDemAncRefund;
	}
	public double getEvatTransAncRefund() {
		return evatTransAncRefund;
	}
	public void setEvatTransAncRefund(double evatTransAncRefund) {
		this.evatTransAncRefund = evatTransAncRefund;
	}
	public double getEvat() {
		return evat;
	}
	public void setEvat(double evat) {
		this.evat = evat;
	}
	public double getEvatGenCo() {
		return evatGenCo;
	}
	public void setEvatGenCo(double evatGenCo) {
		this.evatGenCo = evatGenCo;
	}
	public double getEvatTransCo() {
		return evatTransCo;
	}
	public void setEvatTransCo(double evatTransCo) {
		this.evatTransCo = evatTransCo;
	}
	public double getEvatSystemLoss() {
		return evatSystemLoss;
	}
	public void setEvatSystemLoss(double evatSystemLoss) {
		this.evatSystemLoss = evatSystemLoss;
	}
	public double getFitall(){
		return fitall;
	}
	public void setFitall(double rateFitall){
		this.fitall = rateFitall;
	}
	
	//=====================================================
	public void setEvatSystemLossTransco(double evatSystemLossTransco) {
		this.evatSystemLoss = evatSystemLossTransco;
	}
}
