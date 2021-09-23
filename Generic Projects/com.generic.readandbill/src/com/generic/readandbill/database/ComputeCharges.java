package com.generic.readandbill.database;

import com.androidapp.mytools.objectmanager.DoubleManager;

import android.content.Context;

import java.text.DecimalFormat;

public class ComputeCharges {

	private Consumer consumer;
	protected Reading reading;
	private Rates rate;
	private RateDataSource dsRates;
		
	
	public ComputeCharges(Context context, Consumer consumer) {		
		dsRates = new RateDataSource(context);
		setConsumer(consumer);
	}

    public void setConsumer(Consumer consumer){
		this.consumer = consumer;
		this.rate = dsRates.getRate(consumer.getRateCode());
	}
	
	public void setReading(Reading reading){
		this.reading = reading;
	}
	
	public Double diff(){
		double result = 0;
        DecimalFormat myFormatter = new DecimalFormat("########0.00");
		if (reading.getFeedBackCode().equals("A")) {
			return reading.getReading();
		} else if (reading.getFeedBackCode().equals("R")){
			if (consumer.getInitialReading() < 1000000 && consumer.getInitialReading() >= 100000){
				result = 1000000 - consumer.getInitialReading(); 
			} else if (consumer.getInitialReading() < 100000 && consumer.getInitialReading() >= 10000){
				result = 100000 - consumer.getInitialReading();
			} else if (consumer.getInitialReading() < 10000 && consumer.getInitialReading() >= 1000){
				result = 10000 - consumer.getInitialReading();
			} else if (consumer.getInitialReading() < 1000) {
				result = 1000- consumer.getInitialReading();
			}
			result += reading.getReading();
		} else {
			result = reading.getReading() - consumer.getInitialReading();
		}
        result = Double.parseDouble(myFormatter.format(result));
		return result;
	}
	
	/*public double getCMKilowatthour(){
		double kwhr = 0.0;
		if (consumer.getCmSwitch()== true) {
			kwhr = consumer.getCmReading() - consumer.getCmInitialReading();
			kwhr = consumer.getCmMultiplier() * kwhr;
		}
		return kwhr; 
	}*/
	
	public double getKilowatthour(){
			
		double kilowatthour=0.00;
		double CMKw;
		double CMmul = consumer.getCmMultiplier(); 
		
		//if(consumer.getRateCode().equals("H")) ==remove this statement
		kilowatthour = diff() * consumer.getMultiplier();		
		
		if (CMmul== 0) CMmul = 1; 
		if (consumer.getCmSwitch()){
			CMKw = (consumer.getCmReading() - consumer.getCmInitialReading()) * CMmul;	
			kilowatthour += CMKw;
		}   
		
		//Temporarily remove this statement=================== 
		//if (kilowatthour < 15 && kilowatthour >=0 && consumer.getRateCode().equals("R")) kilowatthour = 15;
		//====================================================
		
		return kilowatthour;
	}
	
	public double getKilowatthourRprt(){
		//return diff() * consumer.getMultiplier();		
		double kilowatthour;		
		kilowatthour = diff() * consumer.getMultiplier();	
		
		return kilowatthour;
	}
	
	public double getDemandTmp(){
		return consumer.getDemand();
	}
	
	public Double genSys() {
		return DoubleManager.rRound(getKilowatthour() * rate.getGenSys());
	}


	public Double hostComm() {
		return DoubleManager.rRound(getKilowatthour() * rate.getHostComm());
	}
	
	public Double tcDemand() {
		//return DoubleManager.rRound(reading.getDemand() * rate.getTcDemand());
		if (consumer.getRateCode().equals("H")){
			return DoubleManager.rRound(reading.getDemand() * rate.getTcDemand());
		}else{
			return 0.0;
		}
		
	}
	
	public Double tcSystem(){
		return DoubleManager.rRound(getKilowatthour() * rate.getTcSystem());
	}
	
	public Double systemLoss(){
		return DoubleManager.rRound(getKilowatthour() * rate.getSystemLoss());
	}
	
	public Double dcDemand() {
		//return DoubleManager.rRound(reading.getDemand() * rate.getDcDemand());
		if (consumer.getRateCode().equals("H")){
			return DoubleManager.rRound(reading.getDemand() * rate.getDcDemand());
		}else{
			return 0.0;
		}
		
	}
	
	public Double dcDistribution(){
		return DoubleManager.rRound(getKilowatthour() * rate.getDcDistribution());
	}
	
	public Double scRetailCust(){
		return DoubleManager.rRound(rate.getScRetailCust());
	}
	
	public Double scSupplySys(){
		return DoubleManager.rRound(getKilowatthour() * rate.getScSupplySys());
	}
	
	public Double mcRetailCust(){
		return DoubleManager.rRound(rate.getMcRetailCust());
	}
	
	public Double mcSystem(){
		return DoubleManager.rRound(getKilowatthour() * rate.getMcSys());
	}
	
	public Double reinvestmentFundSustCapex(){
		return DoubleManager.rRound(getKilowatthour() * rate.getReinvestmentFundSustCapex());
	}
	
	public Double VATmcc(){
		return DoubleManager.rRound(getKilowatthour() * rate.getReinvestmentFundSustCapex());
	}
	
	public Double ucme(){
		return DoubleManager.rRound(getKilowatthour() * rate.getUcme());
	}
	
	public Double ucec(){
		return DoubleManager.rRound(getKilowatthour() * rate.getUcec());
	}
	
	public Double powerActRateRed(){
		return DoubleManager.rRound(getKilowatthour() * rate.getParr()); 
	}
	
	public Double prevYearAdjPowerCost(){
		return DoubleManager.rRound(getKilowatthour() * rate.getPrevYearAdjPowerCost());
	}
   
    public double comKilowatthour_A() {
        double kilowatthour = getKilowatthour();
        //if (kilowatthour <= 14 && consumer.getRateCode().equals("R")) kilowatthour = 15;
        return kilowatthour;
    }
    public Double PARecovery(){
    	return DoubleManager.rRound(comKilowatthour_A() * rate.getPArecovery());
    }
	//Start OverAndUnder===========================
	//=============================================
	public Double OtherGenRateAdj(){
		return DoubleManager.rRound(comKilowatthour_A() * rate.getOtherGenRateAdj());
	}
	public Double OtherTransCostAdj(){
		return DoubleManager.rRound(comKilowatthour_A() * rate.getOtherTransCostAdj());
	}
	public Double OtherTransDemandCostAdj(){
		return DoubleManager.rRound(reading.getDemand() * rate.getOtherTransDemandCostAdj());
	}
	public Double OtherSystemLossCostAdj(){
		return DoubleManager.rRound(comKilowatthour_A() * rate.getOtherSystemLossCostAdj());
	}
	public Double OtherLifelineRateCostAdj(){
		Double result = 0.0;
		
		result = DoubleManager.rRound(comKilowatthour_A() * rate.getOtherLifelineRateCostAdj());		
		if (rate.getRateCode().equals("R") && getKilowatthour() <= 20){
			result = 0.0;
		}
		return result;		
	}
	
	public Double forex() {
		return DoubleManager.rRound(comKilowatthour_A() * rate.getForex());
	}
	//End OverAndUnder=============================
	//============================================= 
	public Double totalPower(){
		return genSys() +
				hostComm() +
				forex() +
				tcDemand() +
				tcSystem() +
				systemLoss() +
				dcDemand() +
				dcDistribution() +
				scSupplySys() +
				mcRetailCust() +
				mcSystem();
	}
	
	public Double evatDistribution(){
		return forex() +
			   OtherLifelineRateCostAdj();
	}
	public Double scForDiscount(){
		return genSys() +
			   hostComm() +
			   forex() +
			   tcDemand() +
			   tcSystem() +
			   systemLoss() +
			   dcDemand() +
			   dcDistribution() +
			   scRetailCust() +
			   scSupplySys() +
			   mcRetailCust() +
			   mcSystem(); 
	}
	 
	public Double lifelineDiscSubs(){
		Double result = 0.0;
		if (rate.getRateCode().equals("R") && getKilowatthour() <= 20){
			if (getKilowatthour() <= 20 && getKilowatthour() > 19){
				result = -0.05;
			} else if (getKilowatthour() <= 19 && getKilowatthour() > 18){
				result = -0.1;
			} else if (getKilowatthour() <= 18 && getKilowatthour() > 17){
				result = -0.2;
			} else if (getKilowatthour() <= 17 && getKilowatthour() > 16){
				result = -0.3;
			} else if (getKilowatthour() <= 16 && getKilowatthour() > 15){
				result = -0.4;
			} else if (getKilowatthour() <= 15) {
				result = -0.5; 
			}  
			result = DoubleManager.rRound(result * totalPower());
		} else {
			result = DoubleManager.rRound(getKilowatthour() * rate.getLifeLineSubsidy());
		}
		return result;
	}
	 
	public Double seniorCitizenDiscRate(){
		Double result = -1.0;
		if(rate.getRateCode().equals("R") && getKilowatthour()<=20){			
			if (getKilowatthour() <= 20 && getKilowatthour() > 19){
				result = -0.95;
			} else if (getKilowatthour() <= 19 && getKilowatthour() > 18){
				result = -0.9;
			} else if (getKilowatthour() <= 18 && getKilowatthour() > 17){
				result = -0.8;
			} else if (getKilowatthour() <= 17 && getKilowatthour() > 16){
				result = -0.7;
			} else if (getKilowatthour() <= 16 && getKilowatthour() > 15){
				result = -0.6;
			} else if (getKilowatthour() <= 15) {
				result = -0.5;
			}
		}
		return result;
	}
	
	public Double currentBill(){
		return DoubleManager.rRound(genSys() +
			   hostComm() +
			   tcDemand() +
			   tcSystem() +
			   systemLoss() +
			   dcDemand() +
			   dcDistribution() +
			   scRetailCust() +
			   scSupplySys() +
			   mcRetailCust() +
			   mcSystem() +
			   ucme() +
			   ucec() +
			   powerActRateRed() +
			   lifelineDiscSubs() +
			   prevYearAdjPowerCost() +
			   reinvestmentFundSustCapex()+
			   forex() +			   
			   OtherTransDemandCostAdj() +			  
			   PARecovery());
			  
	}		
}
