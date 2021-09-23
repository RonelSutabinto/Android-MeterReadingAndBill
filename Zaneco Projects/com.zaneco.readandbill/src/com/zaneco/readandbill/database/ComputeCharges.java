package com.zaneco.readandbill.database;

import android.content.Context;
import android.util.Log;

import com.androidapp.mytools.objectmanager.DoubleManager;

public class ComputeCharges extends com.generic.readandbill.database.ComputeCharges{
	private Consumer consumer;
	private Rates rate;
	private ConsumerDataSource dsConsumer;
	private RateDataSource dsRate;
	private UnpaidDataSource dsUnpaid;
	
	public ComputeCharges(Context context, Consumer consumer) {
		super(context, consumer);
        dsUnpaid = new UnpaidDataSource(context);
        dsRate = new RateDataSource(context);
        setConsumer(consumer);
	}

    public Consumer getConsumer() {
		return consumer;
	}	
	public void setConsumer(Consumer consumer) {
        super.setConsumer(consumer);
		this.consumer = consumer;
		this.rate = dsRate.getRate(consumer.getRateCode());
	}
	public Rates getRates() {
		return rate;
	}
	public void setRates(Rates rates) {
		this.rate = rates;
	}

    public double comKilowatthour() {
        double kilowatthour = getKilowatthour();
        //========Temporarily remove this statement==================
        // if (kilowatthour <= 14 && consumer.getRateCode().equals("R")) kilowatthour = 15;
        //===========================================================
        return kilowatthour;
    }

    @Override
    public Double genSys() {
        return DoubleManager.rRound(comKilowatthour() * rate.getGenSys());
    }

    @Override
    public Double hostComm() {
        return DoubleManager.rRound(comKilowatthour() * rate.getHostComm());
    }

    @Override
    public Double tcSystem(){
        return DoubleManager.rRound(comKilowatthour() * rate.getTcSystem());
    }

    @Override
    public Double systemLoss(){
        return DoubleManager.rRound(comKilowatthour() * rate.getSystemLoss());
    }

    @Override
    public Double dcDistribution(){
        return DoubleManager.rRound(comKilowatthour() * rate.getDcDistribution());
    }

    @Override
    public Double scSupplySys(){
        return DoubleManager.rRound(comKilowatthour() * rate.getScSupplySys());
    }

    @Override
    public Double mcSystem(){
        return DoubleManager.rRound(comKilowatthour() * rate.getMcSys());
    }

    @Override
    public Double reinvestmentFundSustCapex(){
        return DoubleManager.rRound(comKilowatthour() * rate.getReinvestmentFundSustCapex());
    }

    @Override
    public Double ucme(){
        return DoubleManager.rRound(comKilowatthour() * rate.getUcme());
    }

    @Override
    public Double ucec(){
        return DoubleManager.rRound(comKilowatthour() * rate.getUcec());
    }

    @Override
    public Double powerActRateRed(){
        return DoubleManager.rRound(comKilowatthour() * rate.getParr());
    }

    @Override
    public Double prevYearAdjPowerCost(){
        return DoubleManager.rRound(comKilowatthour() * rate.getPrevYearAdjPowerCost());
    }

    

	public Double ucNPCStrandedDebts() {
		return DoubleManager.rRound(comKilowatthour() * rate.getUcNpcStrandedDebts());
	}

	public Double ucNPCStrandedContCost(){
		return DoubleManager.rRound(comKilowatthour() * rate.getUcNpcStrandedContCost());
	}
	
	public Double ucDUStrandedContCost(){
		return DoubleManager.rRound(comKilowatthour() * rate.getUcDuStrandedContCost());
	}
	
	public Double ucEqTaxesAndRoyalties(){
		return DoubleManager.rRound(comKilowatthour() * rate.getUcEqTaxesAndRoyalties());
	}

	public Double ucCrossSubRemoval() {
		return DoubleManager.rRound(reading.getDemand() * rate.getUcCrossSubsidyRemoval());
	}

	public Double icCrossSubsidy(){
		return DoubleManager.rRound(comKilowatthour() * rate.getIcCrossSubsidy());
	}

	public Double transSysAncRefund(){
		return DoubleManager.rRound(comKilowatthour() * rate.getTransSysAncRefund());
	}
	
	public Double flatRateWattage(){
		//temporary remove this statement================================
		//return DoubleManager.rRound(comKilowatthour() * rate.getGram());
		return 0.0;
	}
	
	public Double transDemAncRefund(){
		return DoubleManager.rRound(comKilowatthour() * rate.getTransDemAncRefund());
	}

	public Double evatTransAncRefund(){
		return DoubleManager.rRound(comKilowatthour() * rate.getEvatTransAncRefund());
	}
	public double getRptTaxFromR(){
		 //double result = 0;
		 //result = consumer.getRptTax();         
		 return DoubleManager.rRound(comKilowatthour() * consumer.getRptTax());
	}	
	public Double FitallCost(){
		return DoubleManager.rRound(comKilowatthour() * rate.getFitall());
	}
	public Double totalPower(){
		return super.totalPower() +				
				transSysAncRefund() +
				transDemAncRefund();
	}
	
	public Double scForDiscount(){
		return super.scForDiscount(); 
			  
	}
	public Double OtherSeniorCitizenRateAdj(){
		Double result = 0.0;
		
		result = DoubleManager.rRound(comKilowatthour_A() * rate.getOtherSeniorCitizenRateAdj());
		if (rate.getRateCode().equals("R") && comKilowatthour()<= rate.getScKiloWattHourLimit() &&
				consumer.isSeniorCitizen()){
				 result = 0.0;
		}else if (lifelineDiscSubs() < 0) {
			result = 0.0;
		}
		
		return result;
	}
	public Double seniorCitizenDiscSubs(){
		Double result = 0.0;
		if (rate.getScSwitch()){
			if (rate.getRateCode().equals("R") && rate.getScKiloWattHourLimit() >= comKilowatthour() &&
				consumer.isSeniorCitizen()){
				result = seniorCitizenDiscRate();
				result = DoubleManager.rRound((scForDiscount() * result * (rate.getSeniorCitizenDiscount()/100)));
			} else {
				if (lifelineDiscSubs() > 0) {
					result = DoubleManager.rRound(comKilowatthour() * rate.getSeniorCitizenSubsidy());
				}
			}
		}
		return result;
	}
	public Double evatPARec(){
		return DoubleManager.rRound(PARecovery() * (rate.evat / 100));
	}
	public Double VATmcc(){
		return super.VATmcc();
	}
    
	public String vatUB()
	{
		String s="";
	    s = super.evatDistribution().toString()+"\n"+dcDemand().toString()+"\n"+
	    		dcDistribution().toString()+"\n"+
				scRetailCust().toString()+"\n"+
				scSupplySys().toString()+"\n"+
				mcRetailCust().toString()+"\n"+
				mcSystem().toString()+"\n"+
				icCrossSubsidy().toString()+"\n"+
				lifelineDiscSubs().toString()+"\n"+
				OtherSeniorCitizenRateAdj().toString()+"\n"+
				seniorCitizenDiscSubs().toString()+"\n"+				
				VATmcc().toString()+"\n"+ 
				evatPARec().toString();
	    return s;
	}
	
	public Double evatDistribution(){	
	      	    
		return DoubleManager.rRound((super.evatDistribution() +
				dcDemand() +
				dcDistribution() +
				scRetailCust() +
				scSupplySys() +
				mcRetailCust() +
				mcSystem() +
				icCrossSubsidy() +
				lifelineDiscSubs() +
				OtherSeniorCitizenRateAdj() +
				seniorCitizenDiscSubs()+				
				VATmcc()) * (rate.evat / 100))+ evatPARec();
	}
	
	public Double evatGenCo(){
		return DoubleManager.rRound(comKilowatthour() * rate.getEvatGenCo());
	}
	
	public Double evatTransCo() {
		return DoubleManager.rRound(comKilowatthour() * rate.getEvatTransCo());
	}
	
	public Double evatSystemLoss() {
		return DoubleManager.rRound(comKilowatthour() * rate.getEvatSystemLoss());
	}
	
	public Double evat(){
		return evatDistribution() +
			   evatGenCo() +
			   evatTransCo() +
			   evatSystemLoss();
	}
	
	public Double evatDiscount(){
		Double result = 0.0;
		if (consumer.getEvatDiscount() > 0) {
			//result = (evatDistribution() * ((consumer.getEvatDiscount() / 100) * -1));
		}
		return result;
	}
	
	public Double pantawidSubsidy(){
		if (consumer.getPantawidSubsidy() < currentBill()) {
			return consumer.getPantawidSubsidy();
		} else {
			return currentBill();
		}
	}
	
	 public Double gram(){
	    if (consumer.getisGram()==1){
	    		return DoubleManager.rRound(comKilowatthour() * rate.getGram());
	    	}else{
	    		return 0.00;
	    	}
	    }
	
	public Double trate(){
		double trate_ = 0.0000;
				
		trate_ = rate.getGenSys()+
				 rate.getHostComm()+
				 rate.getForex()+
				 rate.getTcSystem()+
				 rate.getSystemLoss()+
				 rate.getDcDistribution()+
				 rate.getScSupplySys()+
				 rate.getMcSys()+
				 rate.getUcme()+
				 rate.getUcec()+
				 rate.getLifeLineSubsidy()+
				 rate.getParr()+
				 rate.getSeniorCitizenSubsidy()+
				 rate.getReinvestmentFundSustCapex()+
				 rate.getEvatGenCo()+
				 rate.getEvatTransCo()+
				 rate.getEvatSystemLoss()+
				 rate.getUcNpcStrandedContCost()+
				 rate.getIcCrossSubsidy()+
				 rate.getFitall()+
				 rate.getOtherGenRateAdj()+
				 rate.getOtherTransCostAdj()+
				 rate.getOtherSystemLossCostAdj()+
				 rate.getOtherLifelineRateCostAdj()+
				 rate.getOtherSeniorCitizenRateAdj()+
				 rate.getPArecovery()+
				 rate.getUcNpcStrandedDebts();
		
		return trate_;
	}
	
	public Double OUrate(){			
		Double rateou = (rate.getOtherGenRateAdj()+
						 rate.getOtherTransCostAdj()+
						 rate.getOtherSystemLossCostAdj()+
						 rate.getOtherLifelineRateCostAdj()+
						 rate.getOtherSeniorCitizenRateAdj());
		
		return rateou;
	}
	
	public Double OUtotal(){
		Double lifelineOther = rate.getOtherLifelineRateCostAdj();
		Double seniorCit = rate.getOtherSeniorCitizenRateAdj();
		
		if(lifelineDiscSubs()<=0)
			lifelineOther = 0.0;
		
		if(seniorCitizenDiscSubs()<=0)
			seniorCit = 0.0;
		
		Double ou_t = DoubleManager.rRound((rate.getOtherGenRateAdj()+
				 rate.getOtherTransCostAdj()+
				 rate.getOtherSystemLossCostAdj()+
				 lifelineOther+
				 seniorCit)* comKilowatthour() );		
		return ou_t;
	}
	
	public Double OUrated(){	
		return rate.getOtherTransDemandCostAdj();
	}
		
	
	public String rateStr(){
		String rateStr = "";
		rateStr = "getGenSys "+rate.getGenSys() +"\n"+
				"getHostComm "+rate.getHostComm()+"\n"+
				"getForex "+rate.getForex()+"\n"+
				"getTcSystem "+ rate.getTcSystem()+"\n"+
				"getSystemLoss "+rate.getSystemLoss()+"\n"+
				"getDcDistribution "+rate.getDcDistribution()+"\n"+
				"getScSupplySys "+rate.getScSupplySys()+"\n"+
				"getMcSys "+rate.getMcSys()+"\n"+
				"getUcme "+rate.getUcme()+"\n"+
				"getUcec "+rate.getUcec()+"\n"+
				"getLifeLineSubsidy "+rate.getLifeLineSubsidy()+"\n"+
				"getParr "+rate.getParr()+"\n"+
				"getSeniorCitizenDiscount "+rate.getSeniorCitizenDiscount()+"\n"+
				"getReinvestmentFundSustCapex "+rate.getReinvestmentFundSustCapex()+"\n"+
				"getEvatGenCo "+rate.getEvatGenCo()+"\n"+
				"getEvatTransCo "+rate.getEvatTransCo()+"\n"+
				"getEvatSystemLoss "+rate.getEvatSystemLoss()+"\n"+
				"getUcNpcStrandedContCost "+rate.getUcNpcStrandedContCost()+"\n"+
				"getIcCrossSubsidy "+rate.getIcCrossSubsidy()+"\n"+
				"getFitall "+rate.getFitall()+"\n"+
				""+rate.getOtherGenRateAdj()+"\n"+
				"getOtherGenRateAdj "+rate.getOtherTransCostAdj()+"\n"+
				"getOtherSystemLossCostAdj "+rate.getOtherSystemLossCostAdj()+"\n"+
				"getOtherLifelineRateCostAdj "+rate.getOtherLifelineRateCostAdj()+"\n"+
				"getOtherSeniorCitizenRateAdj "+rate.getOtherSeniorCitizenRateAdj()+"\n"+
				"getPArecovery "+rate.getPArecovery()+"\n"+
				"getUcNpcStrandedDebts "+rate.getUcNpcStrandedDebts();
		return rateStr;
		
	}
	
	
	public Double currentBill(){
		return super.currentBill() +			   
			   DoubleManager.rRound(ucNPCStrandedContCost() +
			   ucNPCStrandedDebts() +
			   ucDUStrandedContCost() +
			   ucEqTaxesAndRoyalties() +
			   ucCrossSubRemoval() +
			   icCrossSubsidy() +
			   transSysAncRefund() +
			   transDemAncRefund() +
			   evat() +   
			   //evatDiscount() +
			   consumer.getOcAmount1() +
			   consumer.getOcAmount2() +
			   consumer.getOcAmount3() +
			   consumer.getTransformerRental() +
			   flatRateWattage() +			   
			   evatTransAncRefund() +			   
			   //consumer.getPantawidRecovery() +
			   seniorCitizenDiscSubs() +
			   getRptTaxFromR()+			   
			   OtherGenRateAdj()+
			   OtherTransCostAdj()+
			   OtherSystemLossCostAdj()+
			   OtherLifelineRateCostAdj()+
			   OtherSeniorCitizenRateAdj()+
			   //OUtotal()+
			   FitallCost()+
			   gram());
	}
	
	
	
	public Double pantawidBalance(){
		return consumer.getPantawidSubsidy() - pantawidSubsidy();
	}
	
	public Double totalBill() {
		Double result = 0.0;
		//if (pantawidSubsidy() < currentBill()) {
		//	result = (currentBill() - pantawidSubsidy());
		//}
		result = currentBill();
		return result + dsUnpaid.getTotalUnpaid(consumer.getCode());
	}
	
}
