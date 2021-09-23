package com.generic.readandbill.database;

public class Rates {
	protected long id;	
	protected String rateCode;
	protected double genSys;
	protected double hostComm;
	protected double systemLoss;
	protected double parr;	
	protected double tcDemand;
	protected double tcSystem;		
	protected double dcDemand;
	protected double dcDistribution;	
	protected double scRetailCust;
	protected double scSupplySys;	
	protected double mcRetailCust;
	protected double mcSys;	
	protected double lifeLineSubsidy;
	protected boolean scSwitch;
	protected double seniorCitizenSubsidy;
	protected double scKiloWattHourLimit;
	protected double seniorCitizenDiscount;	
	protected double reinvestmentFundSustCapex;
	protected double prevYearAdjPowerCost;	
	protected double ucec;
	protected double ucme;	
	protected double forex;
	protected double OtherGenRateAdj;
	protected double OtherTransCostAdj;
	protected double OtherTransDemandCostAdj;
	protected double OtherSystemLossCostAdj;
	protected double OtherLifelineRateCostAdj;
	protected double OtherSeniorCitizenRateAdj;	
	protected double PArecovery;
	protected double Finalvat;
	protected double Withholdingtax;
	protected double rentalWitholdingTax;

	public Rates() {
		this.id = -1;
		this.rateCode = "";
		this.genSys = 0;
		this.hostComm = 0;
		this.systemLoss = 0;
		this.parr = 0;
		
		this.tcDemand = 0;
		this.tcSystem = 0;
		
		this.dcDemand = 0;
		this.dcDistribution = 0;
		this.scRetailCust = 0;
		this.scSupplySys = 0;
		this.mcRetailCust = 0;
		this.mcSys = 0;
		
		this.lifeLineSubsidy = 0;
		this.scSwitch = false;
		this.seniorCitizenSubsidy = 0;
		this.scKiloWattHourLimit = 0;
		this.seniorCitizenDiscount = 0;
		this.reinvestmentFundSustCapex = 0;
		this.prevYearAdjPowerCost = 0;		
		this.ucec = 0;
		this.ucme = 0;		
		this.forex = 0;		
		this.OtherGenRateAdj = 0;
		this.OtherTransCostAdj = 0;
		this.OtherTransDemandCostAdj = 0;
		this.OtherSystemLossCostAdj = 0;
		this.OtherLifelineRateCostAdj = 0;
		this.OtherSeniorCitizenRateAdj = 0;
		this.PArecovery = 0;
		this.Finalvat = 0;
		this.Withholdingtax = 0;
		this.rentalWitholdingTax = 0;
	}
	public double getRentalWitholding(){
		return rentalWitholdingTax;
	}
	public void setRentalWitholding(double rentalWT){
		this.rentalWitholdingTax = rentalWT;
	}
	public double getFinalvat(){
		return Finalvat;
	}
	public void setFinalvat(double finalv){
		this.Finalvat = finalv;
	}
	public double getWithholdingtax(){
		return Withholdingtax;
	}
	public void setWithholdingtax(double withholdingt){
		this.Withholdingtax = withholdingt;
		
	}
	public double getPArecovery(){
		return PArecovery;
	}
	public void setPArecovery(double paR){
		this.PArecovery = paR;
	}
	
	public String getRateCode() {
		return rateCode;
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}
	public double getGenSys() {
		return genSys;
	}
	public void setGenSys(double genSys) {
		this.genSys = genSys;
	}
	public double getHostComm() {
		return hostComm;
	}
	public void setHostComm(double hostComm) {
		this.hostComm = hostComm;
	}	
	public double getTcDemand() {
		return tcDemand;
	}
	public void setTcDemand(double tcDemand) {
		this.tcDemand = tcDemand;
	}
	public double getTcSystem() {
		return tcSystem;
	}
	public void setTcSystem(double tcSystem) {
		this.tcSystem = tcSystem;
	}
	public double getSystemLoss() {
		return systemLoss;
	}
	public void setSystemLoss(double systemLoss) {
		this.systemLoss = systemLoss;
	}
	public double getDcDemand() {
		return dcDemand;
	}
	public void setDcDemand(double dcDemand) {
		this.dcDemand = dcDemand;
	}
	public double getDcDistribution() {
		return dcDistribution;
	}
	public double getScRetailCust() {
		return scRetailCust;
	}
	public void setScRetailCust(double scRetailCust) {
		this.scRetailCust = scRetailCust;
	}
	public double getScSupplySys() {
		return scSupplySys;
	}
	public void setScSupplySys(double scSupplySys) {
		this.scSupplySys = scSupplySys;
	}
	public double getMcRetailCust() {
		return mcRetailCust;
	}
	public void setMcRetailCust(double mcRetailCust) {
		this.mcRetailCust = mcRetailCust;
	}
	public double getMcSys() {
		return mcSys;
	}
	public void setMcSys(double mcSys) {
		this.mcSys = mcSys;
	}	
	public double getUcme() {
		return ucme;
	}
	public void setUcme(double ucme) {
		this.ucme = ucme;
	}
	
	public double getUcec() {
		return ucec;
	}
	public void setUcec(double ucec) {
		this.ucec = ucec;
	}	
	public double getParr() {
		return parr;
	}
	public void setParr(double parr) {
		this.parr = parr;
	}
	public double getLifeLineSubsidy() {
		return lifeLineSubsidy;
	}
	public void setLifeLineSubsidy(double lifeLineSubsidy) {
		this.lifeLineSubsidy = lifeLineSubsidy;
	}		
	public Boolean getScSwitch() {
		return scSwitch;
	}
	public void setScSwitch(Boolean scSwitch) {
		this.scSwitch = scSwitch;
	}
	public double getScKiloWattHourLimit() {
		return scKiloWattHourLimit;
	}
	public void setScKiloWattHourLimit(double scKiloWattHourLimit) {
		this.scKiloWattHourLimit = scKiloWattHourLimit;
	}
	public double getSeniorCitizenSubsidy() {
		return seniorCitizenSubsidy;
	}
	public void setSeniorCitizenSubsidy(double seniorCitizenSubsidy) {
		this.seniorCitizenSubsidy = seniorCitizenSubsidy;
	}
	public double getSeniorCitizenDiscount() {
		return seniorCitizenDiscount;
	}
	public void setSeniorCitizenDiscount(double seniorCitizenDiscount) {
		this.seniorCitizenDiscount = seniorCitizenDiscount;
	}	
	public double getPrevYearAdjPowerCost() {
		return prevYearAdjPowerCost;
	}	
	public void setDcDistribution(double dcDistribution) {
		this.dcDistribution = dcDistribution;
	}
	public void setPrevYearAdjPowerCost(double prevYearAdjPowerCost) {
		this.prevYearAdjPowerCost = prevYearAdjPowerCost;
	}
	public double getReinvestmentFundSustCapex() {
		return reinvestmentFundSustCapex;
	}	
	public void setReinvestmentFundSustCapex(double reinvestmentFundSustCapex) {
		this.reinvestmentFundSustCapex = reinvestmentFundSustCapex;
	}
	
	public double getForex() {
		return forex;
	}
	public void setForex(double forex) {
		this.forex = forex;
	}
	
	public double getOtherGenRateAdj(){
		  return OtherGenRateAdj;
	}
	public void setOtherGenRateAdj(double OtherGenRateAdj){
		this.OtherGenRateAdj = OtherGenRateAdj;
	}
	
	public double getOtherTransCostAdj(){
		return OtherTransCostAdj;
	}	
	public void setOtherTransCostAdj(double OtherTransCostAdj){
		this.OtherTransCostAdj = OtherTransCostAdj;
	}
	
	public double getOtherTransDemandCostAdj(){
		return OtherTransDemandCostAdj;
	}	
	public void setOtherTransDemandCostAdj(double OtherTransDemandCostAdj){
		this.OtherTransDemandCostAdj = OtherTransDemandCostAdj;
	}
	
	public double getOtherSystemLossCostAdj(){
		return OtherSystemLossCostAdj;
	}
	public void setOtherSystemLossCostAdj(double OtherSystemLossCostAdj){
		this.OtherSystemLossCostAdj = OtherSystemLossCostAdj;
	}
	
	public double getOtherLifelineRateCostAdj(){
		return OtherLifelineRateCostAdj;
	}
	public void setOtherLifelineRateCostAdj(double OtherLifelineRateCostAdj){
		this.OtherLifelineRateCostAdj = OtherLifelineRateCostAdj;
	}
	
	public double getOtherSeniorCitizenRateAdj(){
		return OtherSeniorCitizenRateAdj;
	}
	public void setOtherSeniorCitizenRateAdj(double OtherSeniorCitizenRateAdj){
		this.OtherSeniorCitizenRateAdj = OtherSeniorCitizenRateAdj;
	}
	

}
