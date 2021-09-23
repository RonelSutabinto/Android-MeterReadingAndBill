package com.zaneco.readandbill;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;

import com.androidapp.mytools.objectmanager.DoubleManager;
import com.androidapp.mytools.objectmanager.StringManager;
import com.zaneco.readandbill.database.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatementGenerator extends
        com.generic.readandbill.StatementGenerator {
	
    private Consumer myConsumer;
    private UserProfile userProfile;

    protected UnpaidDataSource dsUnpaid;
    protected ComputeCharges compute;
    private Rates rate;
    protected DecimalFormat formatDemand = new DecimalFormat("######0.0#");
    protected DecimalFormat formatVat    = new DecimalFormat("##,###0.00");

    public StatementGenerator(Context context) {
        super(context, null);
        initializeObject(context);       
       
    }

    public StatementGenerator(Context context, ComputeCharges compute) {
        super(context, compute.getConsumer());
        initializeObject(context);
        this.compute = compute;
        this.myConsumer = compute.getConsumer();
        rate = compute.getRates();
    }

    private void initializeObject(Context context){
        //dsRate = new RateDataSource(context);
        dsUnpaid = new UnpaidDataSource(context);
        UserProfileDataSource dsUserProfile = new UserProfileDataSource(context);
        userProfile = dsUserProfile.getUserProfile();
    }
   
    /*public void setConsumer(Consumer consumer) {
		/*this.myConsumer = consumer;
		dsUnpaid = new UnpaidDataSource(context);
		reading = dsReading.getReading(consumer.getCode(),
				ReadingDataSource.CONSUMER_ID);
		rate = dsRate.getRate(consumer.getRateCode());
		if (compute == null) {
			compute = new ComputeCharges(context, consumer);
		}
        compute.setReading(reading);
        this.myConsumer = consumer;
	}                               */
  //=========Start image print===================================
    /*Code128 barcode = new Code128();     	
	barcode.setData("Ronel");     	
	barcode.setX(2); 
	
	try
	{
		// Generate barcode & print into Graphics2D object     	
  	  
    	//result.add(barcode.drawBarcode("Sample") + "\n");
		result.add(barcode.toString()+ "\n");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   */
    
    //===========end image print=========================================
    
    public List<String> generateSOA() {
        List<String> result = new ArrayList<String>();
        if (myConsumer != null) {
            for (String string : soaHeader()) {
                result.add(string);
                // result += string;
            }
            for (String string : soaBody()) {
                result.add(string);
                // result += string;
            }
            for (String string : soaFooter()) {
                result.add(string);
                // result += string;
            }
			/*
			 * result += soaHeader(); result += soaBody(); result +=
			 * soaFooter();
			 */
        }
        return result;
    }
    @Override
    public List<String> soaHeader() {  	
    	 
    	DecimalFormat dfRate = new DecimalFormat("###.####");
    	    	
        List<String> result = new ArrayList<String>();
        Double CMRead = myConsumer.getCmReading();
        Double CMIntlRead = myConsumer.getCmInitialReading();
        Double CMDemnd = myConsumer.getCmDemand();
        Double CMDiff = CMRead - CMIntlRead;
        String curr;
        String totalB;
        Double totalKilowatt = CMDiff + compute.diff();
        Double totalDemand = CMDemnd + reading.getDemand();
        Time now = new Time();
        now.setToNow();
        String myDate = userProfile.getBillmonth().substring(0, 2) + "/01/20"
                + userProfile.getBillmonth().substring(2, 4);       
        
        //========================================
    	//===================GetDevice Name=======
        String DName = "";
        if(SplashScreen.btPrinter.getDeviceN() != null)
        	DName = SplashScreen.btPrinter.getDeviceN();
        else 
        	DName ="";
        
    	if(DName.length()>=8){
           DName = DName.substring(0,8); 
        }else{
           DName="";
        }
    	//=========================================
        
        if (DName.equals("SPP-R310")) {        
        
	        result.add("Android Ver " + SplashScreen.version + "\n");
	        result.add(StringManager.centerJustify("ZAMBOANGA DEL NORTE ELECTRIC COOPERATIVE", 47) + "\n");        
	        result.add( StringManager.centerJustify("Dipolog City, Zamboanga del Norte", 47) + "\n");
	        result.add( StringManager.centerJustify("Contact No."+userProfile.getTelno(), 47) + "\n");
	        result.add(" \n");
	        	        
	        result.add(StringManager.centerJustify("STATEMENT OF ACCOUNT", 23) + "\n");              
	        result.add("Billing Month : " + billingPeriodStr(myDate) + "\n");
	        
	        
	        result.add("Billing Rate : ");
	        result.add(dfRate.format(compute.trate()) + "\n");
	        
	        result.add("Acct#: "
	                + myConsumer.getAccountNumber() + "  MTR #: "
	                + myConsumer.getMeterSerial() + "  Type: "
	                + myConsumer.getRateCode() + "\n");
	        result.add("Name: " + myConsumer.getName() + "\n");
	        result.add("Reference No.: ");
	                   
	        curr = compute.currentBill().toString();
	        totalB = compute.totalBill().toString(); 
	        if (curr.equals(totalB)){
	        	 result.add(reading.getAtmref());
	        }else{
	        	 result.add("N/A");        	
	        }
	        result.add("\n");        
	        result.add("Addr: " + myConsumer.getAddress() + "\n");
	
	        if (dsUnpaid.hasUnpaid(myConsumer.getCode())) {
	            for (String string : unpaidDues()) {
	                result.add(string);
	            }
	        }
	        
	        Double rCodetmp;
	        if (myConsumer.getRateCode().equals("H")){
	        	rCodetmp = myConsumer.getDemand();
	        }else{
	        	rCodetmp = 0.0;
	        }
	
	        result.add("Meter Reader: " + userProfile.getName() + "\n");
	        result.add("Period Covered: " + myConsumer.getInitialReadingDate() + "-"
	                + now.format("%D") + "\n");
	        result.add(StringManager.lineBreak());
	
	        result.add(StringManager.centerJustify("READING", 26) + StringManager.centerJustify("USAGE", 17)
	                + "\n");
	        result.add("MUL" + " " + "FB" + " " + StringManager.rightJustify("PRES", 8) + " "
	                + StringManager.rightJustify("PREV", 9) + " " + StringManager.rightJustify("KWH", 9) + " "
	                + StringManager.rightJustify("DEM", 7) + "\n");
	        result.add(StringManager.rightJustify(String.valueOf(myConsumer.getMultiplier()), 3) + " "
	                + StringManager.rightJustify(reading.getFeedBackCode(), 2) + " "
	                + StringManager.rightJustify(String.valueOf(reading.getReading()), 8) + " "
	                + StringManager.rightJustify(String.valueOf(myConsumer.getInitialReading()), 9)
	                + " " + StringManager.rightJustify(kilowattUsed.format(compute.getKilowatthourRprt()), 9)
	                //+ " " + StringManager.rightJustify(String.valueOf(rCodetmp), 7) + "\n");
	                + " " + StringManager.rightJustify(formatDemand.format(reading.getDemand()), 7) + "\n");
	        if (myConsumer.getCmSwitch()) {
	            result.add(StringManager.centerJustify("CM :", 6)
	                    + " "
	                    + StringManager.rightJustify(String.valueOf(CMRead), 8)
	                    + " "
	                    + StringManager.rightJustify(String.valueOf(CMIntlRead), 9)
	                    + " "
	                    + StringManager.rightJustify(
	                    kilowattUsed.format(CMDiff),
	                    9) + " "
	                    + StringManager.rightJustify(String.valueOf(CMDemnd), 7) + "\n");
	            result.add(StringManager.rightJustify("TOTAL KWH : ", 25) + " "
	                    + StringManager.rightJustify(kilowattUsed.format(totalKilowatt).toString(), 9) + " "
	                    + StringManager.rightJustify(kilowattUsed.format(totalDemand).toString(), 7) + "\n");
	        }
	        result.add(StringManager.lineBreak());
	
			
	        return result;
	        
        }else {
        	
        	result.add((char) 29 + "Android Ver " + SplashScreen.version + "\n");
            result.add((char) 28
                    + StringManager.centerJustify("ZAMBOANGA DEL NORTE ELECTRIC COOPERATIVE", 47)
                    + "\n");        
            result.add((char) 29
                    + StringManager.centerJustify("Dipolog City, Zamboanga del Norte", 47) + "\n");
            
            result.add((char) 29
                    + StringManager.centerJustify("Contact No."+userProfile.getTelno(), 47) + "\n");
            result.add((char) 10 + "\n"); 
                    
            
            result.add((char) 28 + StringManager.centerJustify("STATEMENT OF ACCOUNT", 47) + "\n");            
            result.add((char) 29 + "Billing Month : " + billingPeriodStr(myDate) + "\n");
            
            result.add((char) 28 +"Billing Rate : ");
	        result.add( dfRate.format(compute.trate()) + "\n");
 
            result.add( "Acct#: "
                    + myConsumer.getAccountNumber() + "  MTR #: "
                    + myConsumer.getMeterSerial() + "  Type: "
                    + myConsumer.getRateCode() + "\n");
            result.add("Name: " + myConsumer.getName() + "\n");
            result.add("Reference No.: ");
                       
            curr = compute.currentBill().toString();
            totalB = compute.totalBill().toString(); 
            if (curr.equals(totalB)){
            	 result.add(reading.getAtmref());
            }else{
            	 result.add("N/A");        	
            }
            result.add("\n");            
            result.add((char) 29 + "Addr: " + myConsumer.getAddress()
                    + "\n");

            if (dsUnpaid.hasUnpaid(myConsumer.getCode())) {
                for (String string : unpaidDues()) {
                    result.add(string);
                }
            }
            
            Double rCodetmp;
            if (myConsumer.getRateCode().equals("H")){
            	rCodetmp = myConsumer.getDemand();
            }else{
            	rCodetmp = 0.0;
            }

            result.add("Meter Reader: " + userProfile.getName() + "\n");
            result.add("Period Covered: " + myConsumer.getInitialReadingDate() + "-"
                    + now.format("%D") + "\n");
            result.add(StringManager.lineBreak());

            result.add(StringManager.centerJustify("READING", 26) + StringManager.centerJustify("USAGE", 17)
                    + "\n");
            result.add("MUL" + " " + "FB" + " " + StringManager.rightJustify("PRES", 8) + " "
                    + StringManager.rightJustify("PREV", 9) + " " + StringManager.rightJustify("KWH", 9) + " "
                    + StringManager.rightJustify("DEM", 7) + "\n");
            result.add(StringManager.rightJustify(String.valueOf(myConsumer.getMultiplier()), 3) + " "
                    + StringManager.rightJustify(reading.getFeedBackCode(), 2) + " "
                    + StringManager.rightJustify(String.valueOf(reading.getReading()), 8) + " "
                    + StringManager.rightJustify(String.valueOf(myConsumer.getInitialReading()), 9)
                    + " " + StringManager.rightJustify(kilowattUsed.format(compute.getKilowatthourRprt()), 9)                    
                    + " " + StringManager.rightJustify(formatDemand.format(reading.getDemand()), 7) + "\n");
            if (myConsumer.getCmSwitch()) {
                result.add(StringManager.centerJustify("CM :", 6)
                        + " "
                        + StringManager.rightJustify(String.valueOf(CMRead), 8)
                        + " "
                        + StringManager.rightJustify(String.valueOf(CMIntlRead), 9)
                        + " "
                        + StringManager.rightJustify(
                        kilowattUsed.format(CMDiff),
                        9) + " "
                        + StringManager.rightJustify(String.valueOf(CMDemnd), 7) + "\n");
                result.add(StringManager.rightJustify("TOTAL KWH : ", 25) + " "
                        + StringManager.rightJustify(kilowattUsed.format(totalKilowatt).toString(), 9) + " "
                        + StringManager.rightJustify(kilowattUsed.format(totalDemand).toString(), 7) + "\n");
            }
            result.add(StringManager.lineBreak());

    		
            return result;
        }
        	
        
        
       
    }

    private List<String> unpaidDues() {
        List<String> result = new ArrayList<String>();
        List<Unpaid> unpaids = dsUnpaid.getUnpaids(myConsumer.getCode());

        //========================================
    	//===================GetDevice Name=======
        String DName = "";
        if(SplashScreen.btPrinter.getDeviceN() != null)
        	DName = SplashScreen.btPrinter.getDeviceN();
        else 
        	DName ="";
        
    	if(DName.length()>=8){
           DName = DName.substring(0,8); 
        }else{
           DName="";
        }
    	//=========================================
    	
        if (DName.equals("SPP-R310")) { 
        	
	        result.add(StringManager.lineBreak());
	        result.add( "Overdue Accounts" + "\n" );
	
	        for (Unpaid up : unpaids){
	            result.add(StringManager.leftJustify(up.getBillmonth(), 36)
	                    + StringManager.rightJustify(up.getAmount().toString(), 11)
	                    + "\n");
	        }
	        
	        result.add(StringManager.leftJustify("Total Arrears : ", 36)
	                + StringManager.rightJustify(
	                DoubleManager.rRound(
	                        dsUnpaid.getTotalUnpaid(myConsumer.getCode()))
	                        .toString(), 11) + "\n");
	        result.add("\n");
	        result.add("DISCONNECTION ANYTIME" +  "\n");
	        result.add("AFTER RECEIPT OF THIS STATEMENT" + "\n");
	        result.add(StringManager.lineBreak());		
	
	        return result;
	        
        }else {
        	
        	result.add(StringManager.lineBreak());
            result.add((char) 28 + "Overdue Accounts" + "\n" + (char) 29);

            for (Unpaid up : unpaids){
                result.add(StringManager.leftJustify(up.getBillmonth(), 36)
                        + StringManager.rightJustify(up.getAmount().toString(), 11)
                        + "\n");
            }

            result.add((char) 10 + "\n");
            result.add(StringManager.leftJustify("Total Arrears : ", 33)
                    + StringManager.rightJustify(
                    DoubleManager.rRound(
                            dsUnpaid.getTotalUnpaid(myConsumer.getCode()))
                            .toString(), 11) + "\n");
            result.add((char) 28 + "DISCONNECTION ANYTIME" + (char) 29 + "\n");
            result.add("AFTER RECEIPT OF THIS STATEMENT" + "\n");
            result.add(StringManager.lineBreak());		

            return result;
        }
        	
        
       
    }
 
    public List<String> soaBody() {
    	
        List<String> result = new ArrayList<String>();
        
        //==OU varialbes declaired=============
        String OUstr="";
        String OURstr="";
        String OUstr_d="";
        String OURstr_d="";
        String DmndStr = "  Demand Rate/Kwhr ";
        String ourateStr = "";
        //=======================================

        if (compute.genSys() != 0) {        	            
            ourateStr = "Generation System";           
                       		
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getGenSys()),
                    amountFormat.format(compute.genSys()) ));
        }
        if (compute.OtherGenRateAdj() != 0) {        	            
            ourateStr = "Gen.Sys. Over/Under Rec.";           
                       		
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getOtherGenRateAdj()),
                    amountFormat.format(compute.OtherGenRateAdj()) ));
        }
        
        if(compute.gram()!=0){
        	result.add(bodyLineGenerator("GRAM & ICERA DAA",
        			rateFormat.format(rate.getGram()), 
        			amountFormat.format(compute.gram())));
        }

        if (compute.hostComm() != 0) {
            result.add(bodyLineGenerator("Host Comm",
                    rateFormat.format(rate.getHostComm()),
                    amountFormat.format(compute.hostComm())));
        }
        if (compute.forex() != 0) {
            result.add(bodyLineGenerator("FOREX",
                    rateFormat.format(rate.getForex()),
                    amountFormat.format(compute.forex())));
        }
        if (compute.evatGenCo() != 0) {
            result.add(bodyLineGenerator("Evat GenCo",
                    rateFormat.format(rate.getEvatGenCo()),
                    amountFormat.format(compute.evatGenCo())));
        }        
        if (compute.tcSystem() != 0) {
        	ourateStr = "TC Trans. Sys.";            
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getTcSystem() ),
                    amountFormat.format(compute.tcSystem()) ));
        }
        if (compute.OtherTransCostAdj() != 0) {
        	ourateStr = "Trans.Sys.Over/Under Rec.";            
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getOtherTransCostAdj() ),
                    amountFormat.format(compute.OtherTransCostAdj()) ));
        }
        if (compute.tcDemand() != 0) {
        	ourateStr = "TC Demand";           
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getTcDemand()),
                    amountFormat.format(compute.tcDemand()) ));
        }
        if (compute.OtherTransDemandCostAdj() != 0) {
        	ourateStr = "TC Dmnd. Over/Under Rec.";           
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getOtherTransDemandCostAdj()),
                    amountFormat.format(compute.OtherTransDemandCostAdj()) ));
        }
        if (compute.evatTransCo() != 0) {
            result.add(bodyLineGenerator("Evat TransCo",
                    rateFormat.format(rate.getEvatTransCo()),
                    amountFormat.format(compute.evatTransCo())));
        }
        if (compute.systemLoss() != 0) {
        	ourateStr = "System Loss";           
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getSystemLoss()),
                    amountFormat.format(compute.systemLoss()) ));
        }
        if (compute.OtherSystemLossCostAdj() != 0) {
        	ourateStr = "Sys. Loss Over/Under Rec.";           
                        
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getOtherSystemLossCostAdj()),
                    amountFormat.format(compute.OtherSystemLossCostAdj()) ));
        }
        if (compute.evatSystemLoss() != 0) {
            result.add(bodyLineGenerator("Evat System Loss",
                    rateFormat.format(rate.getEvatSystemLoss()),
                    amountFormat.format(compute.evatSystemLoss())));
        }
        if (compute.dcDemand() != 0) {
            result.add(bodyLineGenerator("DC Demand",
                    rateFormat.format(rate.getDcDemand()),
                    amountFormat.format(compute.dcDemand())));
        }
        if (compute.dcDistribution() != 0) {
            result.add(bodyLineGenerator("DC Distribution",
                    rateFormat.format(rate.getDcDistribution()),
                    amountFormat.format(compute.dcDistribution())));
        }
        if (compute.scRetailCust() != 0) {
            result.add(bodyLineGenerator("SC Retail Customer",
                    rateFormat.format(rate.getScRetailCust()),
                    amountFormat.format(compute.scRetailCust())));
        }
        if (compute.scSupplySys() != 0) {
            result.add(bodyLineGenerator("SC Supply Sys",
                    rateFormat.format(rate.getScSupplySys()),
                    amountFormat.format(compute.scSupplySys())));
        }
        if (compute.mcRetailCust() != 0) {
            result.add(bodyLineGenerator("MC Retail Customer",
                    rateFormat.format(rate.getMcRetailCust()),
                    amountFormat.format(compute.mcRetailCust())));
        }
        if (compute.mcSystem() != 0) {
            result.add(bodyLineGenerator("MC Metering System",
                    rateFormat.format(rate.getMcSys()),
                    amountFormat.format(compute.mcSystem())));
        }
        if (compute.icCrossSubsidy() != 0) {
            result.add(bodyLineGenerator("ICCS Adj.",
                    rateFormat.format(rate.getIcCrossSubsidy()),
                    amountFormat.format(compute.icCrossSubsidy())));
        }
        if (compute.lifelineDiscSubs() != 0) {
            String tmpDesc;            
            if (compute.lifelineDiscSubs() < 0) {
                tmpDesc = "Lifeline Disc.";
            } else {
                tmpDesc = "Lifeline Subs.";
            }
            
            ourateStr = tmpDesc;                        
            
            result.add(bodyLineGenerator(ourateStr,
                    rateFormat.format(rate.getLifeLineSubsidy()),
                    amountFormat.format(compute.lifelineDiscSubs()) ));
        }
        if (compute.OtherLifelineRateCostAdj() != 0) {
            result.add(bodyLineGenerator("Lifeline Over/Under Rec.",
                    rateFormat.format(rate.getOtherLifelineRateCostAdj()),
                    amountFormat.format(compute.OtherLifelineRateCostAdj())));
        }
       
        if (compute.powerActRateRed() != 0) {
            result.add(bodyLineGenerator("Power Act Rate Red.",
                    rateFormat.format(rate.getParr()),
                    amountFormat.format(compute.powerActRateRed())));
        }        
        if (compute.seniorCitizenDiscSubs() != 0) {
            String tmpDesc;
            String tmpRate;
                      
            if (compute.seniorCitizenDiscSubs() < 0) {
                tmpDesc = "Senior Citizen Disc.";  
                ourateStr = tmpDesc;
                
                tmpRate = Integer.toString((int)rate.getSeniorCitizenDiscount())+"%";              
                
            } else { 
            	tmpDesc = "Senior Citizen Subs.";
            	ourateStr = tmpDesc;
                
                //if(rate.getOtherSeniorCitizenRateAdj()!=0)
                	//ourateStr = "Senior Cit.Subs.(Excl.OU)";            	
                
                tmpRate = rateFormat.format(rate.getSeniorCitizenSubsidy() );           
               
            }
            result.add(bodyLineGenerator(ourateStr, tmpRate,
                   amountFormat.format(compute.seniorCitizenDiscSubs()) ));
        }
        
        if (compute.OtherSeniorCitizenRateAdj() != 0) {
            result.add(bodyLineGenerator("Sen.CitzOver/Under Rec.",
                    rateFormat.format(rate.getOtherSeniorCitizenRateAdj()),
                    amountFormat.format(compute.OtherSeniorCitizenRateAdj())));
        }
        
        if(compute.PARecovery() !=0){
        	result.add(bodyLineGenerator("PAR Recovery",
        			   rateFormat.format(rate.getPArecovery()),
        			   amountFormat.format(compute.PARecovery())));
        }
        
        if (compute.reinvestmentFundSustCapex() != 0) {
            result.add(bodyLineGenerator("Reinvest. Fund Sus. CAPEX",
                    rateFormat.format(rate.getReinvestmentFundSustCapex()),
                    amountFormat.format(compute.reinvestmentFundSustCapex())));
        }
        
        if (compute.evatDistribution() != 0) {
            result.add(bodyLineGenerator("Evat Distribution", (int)rate.getEvat()
                    + "%", amountFormat.format(compute.evatDistribution())));
        }
        
        if (compute.evatDiscount() != 0) {        
        result.add(bodyLineGenerator("Less : VAT Discount",
        		"",
                amountFormat.format(compute.evatDiscount())));
        }
        
        if (compute.getRptTaxFromR() != 0){
        	result.add(bodyLineGenerator("Real Property Tax ",
        			rateFormat.format(myConsumer.getRptTax()),
        			amountFormat.format(compute.getRptTaxFromR())));        	
        }    
               
        
        //Temporarily remove this statement===========
        //if (compute.ucNPCStrandedDebts() != 0) {
        //    result.add(bodyLineGenerator("UCNPCSD",
        //            rateFormat.format(rate.getUcNpcStrandedDebts()),
        //            amountFormat.format(compute.ucNPCStrandedDebts())));
        //}===========================================
        
        if (compute.ucec() != 0) {
            result.add(bodyLineGenerator("UC Environmental",
                    rateFormat.format(rate.getUcec()),
                    amountFormat.format(compute.ucec())));
        }
        if (compute.ucDUStrandedContCost() != 0) {
            result.add(bodyLineGenerator("UCDUSCC",
                    rateFormat.format(rate.getUcDuStrandedContCost()),
                    amountFormat.format(compute.ucDUStrandedContCost())));
        }
        if (compute.ucme() != 0) {
            result.add(bodyLineGenerator("UC Missionary Elect.",
                    rateFormat.format(rate.getUcme()),
                    amountFormat.format(compute.ucme())));
        }
        if (compute.ucEqTaxesAndRoyalties() != 0) {
            result.add(bodyLineGenerator("UC Eq Tax N Roy.",
                    rateFormat.format(rate.getUcEqTaxesAndRoyalties()),
                    amountFormat.format(compute.ucEqTaxesAndRoyalties())));
        }
        
        if (compute.ucNPCStrandedContCost() != 0) {
            result.add(bodyLineGenerator("UC SCC",
                    rateFormat.format(rate.getUcNpcStrandedContCost()),
                    amountFormat.format(compute.ucNPCStrandedContCost())));
        }
        
        if (compute.ucNPCStrandedDebts() != 0) {
            result.add(bodyLineGenerator("UC-SD",
                    rateFormat.format(rate.getUcNpcStrandedDebts()),
                    amountFormat.format(compute.ucNPCStrandedDebts())));
        }
        
        if (compute.ucCrossSubRemoval() != 0) {
            result.add(bodyLineGenerator("UC Cross Subs. Rem.",
                    rateFormat.format(rate.getUcCrossSubsidyRemoval()),
                    amountFormat.format(compute.ucCrossSubRemoval())));
        }


        if (myConsumer.getTransformerRental() != 0) {
            result.add(bodyLineGenerator("Transformer Rental",
                    amountFormat.format(myConsumer.getTransformerRental())));
        }
        if (compute.FitallCost() != 0) {
        	result.add(bodyLineGenerator("FIT-All",
        			rateFormat.format(rate.getFitall()), 
        			amountFormat.format(compute.FitallCost()))); 
        }
        
        /*if (compute.OUtotal() != 0) {
            result.add(bodyLineGenerator("Over/Under Recovery",
                    rateFormat.format(compute.OUrate()),
                    amountFormat.format(compute.OUtotal())));
        }
        if (compute.OtherTransDemandCostAdj() != 0) {
            result.add(bodyLineGenerator("Over/Under Dmnd Recovery",
                    rateFormat.format(compute.OUrated()),
                    amountFormat.format(compute.OtherTransDemandCostAdj())));
        }*/
        
        if (compute.flatRateWattage() != 0) {
            result.add(bodyLineGenerator("Flat Rate Wattage",
                    rateFormat.format(rate.getGram()),
                    amountFormat.format(compute.flatRateWattage())));
        }
        if (compute.prevYearAdjPowerCost() != 0) {
            result.add(bodyLineGenerator("Prev Yrs. Adj on Pow Cst",
                    rateFormat.format(rate.getPrevYearAdjPowerCost()),
                    amountFormat.format(compute.prevYearAdjPowerCost())));
        }             
            
        if (myConsumer.getOcAmount1() != 0) {        	
        	        	
            result.add(bodyLineGenerator("Other Charges - "+myConsumer.getOcCode1(),
                    amountFormat.format(myConsumer.getOcAmount1())));
        }
        if (myConsumer.getOcAmount2() != 0) {
            result.add(bodyLineGenerator("Other Charges - "+myConsumer.getOcCode2(),
                    amountFormat.format(myConsumer.getOcAmount2())));
        }
        if (myConsumer.getOcAmount3() != 0) {
            result.add(bodyLineGenerator("Other Charges - "+myConsumer.getOcCode3(),
                    amountFormat.format(myConsumer.getOcAmount3())));
        }      
            
        
        if (myConsumer.getPantawidRecovery() != 0) {
            result.add(bodyLineGenerator(
                    "PANTAWID RECOVERY "
                            + rateFormat.format(myConsumer
                            .getPantawidRecoveryRef()),
                    amountFormat.format(myConsumer.getPantawidRecovery())));
        }
        if (compute.transSysAncRefund() != 0) {
            result.add(bodyLineGeneratorWithAt("TRANS SYS PDS REFUND",
                    rateFormat.format(rate.getTransSysAncRefund()),
                    amountFormat.format(compute.transSysAncRefund())));
        }
        if (compute.transDemAncRefund() != 0) {
            result.add(bodyLineGeneratorWithAt("TRANS DEM PDS REFUND",
                    rateFormat.format(rate.getTransDemAncRefund()),
                    amountFormat.format(compute.transDemAncRefund())));
        }
        if (compute.evatTransAncRefund() != 0) {
            result.add(bodyLineGeneratorWithAt("VAT TRANS PDS REFUND",
                    rateFormat.format(rate.getEvatTransAncRefund()),
                    amountFormat.format(compute.evatTransAncRefund())));
        }
              
               
        /*//===Breakdown Over and Under Rate===================
        //===================================================           
        if ((compute.totalOU() < 0)|| (compute.totalOUd() < 0) ) {
        	OUstr = "= Php("+amountFormat.format(compute.totalOU()*-1)+")";
        	OUstr_d = "= Php("+amountFormat.format(compute.totalOUd()*-1)+")";
        }else if ((compute.totalOU() > 0)|| (compute.totalOUd() > 0)){
        	OUstr = "= Php "+amountFormat.format(compute.totalOU());
        	OUstr_d = "= Php "+amountFormat.format(compute.totalOUd());
        }
        
        if ((compute.rateOU() < 0)|| (compute.rateOUd() < 0) ) {
        	OURstr = "("+rateFormat.format(compute.rateOU()*-1)+")";
        	OURstr_d = "("+rateFormat.format(compute.rateOUd()*-1)+")";
        }else if ((compute.totalOU() > 0)|| (compute.totalOUd() > 0)){
        	OURstr = rateFormat.format(compute.rateOU());
        	OURstr_d = rateFormat.format(compute.rateOUd());
        }
        
        if(compute.rateOU() == 0){
        	OURstr = "";
        	OUstr = "";            	
        }
        
        if(compute.rateOUd()==0){
        	OUstr_d = "";
        	OURstr_d = "";
        	DmndStr="";
        }            
        result.add("Notes: Total Over/Under Recovery Rate. \n"+
                                     "  Rate/Kwhr        "+OURstr+ OUstr + "\n"+
                                        DmndStr+OURstr_d+OUstr_d+"\n");*/         
        //====End of Over and under rate breakdown==================
        //==========================================================
        
        /*if (compute.pantawidSubsidy() != 0) {
            result.add(bodyLineGenerator("PANTAWID SUBSIDY",
                    amountFormat.format(compute.pantawidSubsidy())));
            result.add(bodyLineGeneratorIndent("PANTAWID BALANCE",
                    amountFormat.format(compute.pantawidBalance())));
        }*/
       
        //========================================
    	//===================GetDevice Name=======
        String DName = "";
        if(SplashScreen.btPrinter.getDeviceN() != null)
        	DName = SplashScreen.btPrinter.getDeviceN();
        else 
        	DName ="";
        
    	if(DName.length()>=8){
           DName = DName.substring(0,8); 
        }else{
           DName="";
        }
    	//=========================================
        //=================================================================
        //=================================================================
        if (DName.equals("SPP-R310")) { 
        
	        result.add(bodyLineGenerator("CURRENT BILL",
	                amountFormat.format(compute.currentBill())));
	       /* if((myConsumer.getOcAmount1()!=0) || (myConsumer.getOcAmount2()!=0) || (myConsumer.getOcAmount3()!=0) ||
	           (myConsumer.getPantawidRecovery() != 0) || (compute.transSysAncRefund() != 0) ||
	           (compute.transDemAncRefund() != 0) || (compute.evatTransAncRefund() != 0) ||
	           (compute.pantawidSubsidy() != 0) ) {
	        	result.add((char) 28
	                + bodyLineGenerator("TOTAL CURRENT BILL", 
	                amountFormat.format(compute.currentBill())) + (char) 29);
	        }*/
	        
	        result.add(
	                 bodyLineGenerator("Total Amount Due :",
	                amountFormat.format(compute.totalBill())) );	
			
	        
	        double finalV = 0.0; 
	        double ocFinalV = 0.0;
	        double withholdingT = 0.0;
	        double ocWithholdingT = 0.0;
	        double ocAmoint1 = myConsumer.getOcAmount1();
	        double ocAmoint2 = myConsumer.getOcAmount2();
	        double ocAmoint3 = myConsumer.getOcAmount3();
	        double evat = rate.getEvat();
	        double finalVat = rate.getFinalvat(); 
	        double Withholdingtax = rate.getWithholdingtax();
	        double rentalWithholding = rate.getRentalWitholding();
	        
	        finalV = (compute.dcDemand()+
			          compute.dcDistribution()+
					  compute.scRetailCust()+
					  compute.scSupplySys()+
					  compute.mcRetailCust()+
					  compute.mcSystem()+
					  compute.icCrossSubsidy()+
					  compute.lifelineDiscSubs()+
					  compute.seniorCitizenDiscSubs()+ 				  
					  compute.OtherSeniorCitizenRateAdj()+
					  compute.OtherLifelineRateCostAdj()+
					  compute.reinvestmentFundSustCapex())*(finalVat/100);
	        withholdingT = (compute.dcDemand()+
			          compute.dcDistribution()+
					  compute.scRetailCust()+
					  compute.scSupplySys()+
					  compute.mcRetailCust()+
					  compute.mcSystem()+
					  compute.icCrossSubsidy()+
					  compute.lifelineDiscSubs()+
					  compute.seniorCitizenDiscSubs()+ 
					  compute.OtherSeniorCitizenRateAdj()+
					  compute.OtherLifelineRateCostAdj()+
					  compute.reinvestmentFundSustCapex())*(Withholdingtax/100);
	        
	        if (( ocAmoint1!= 0.0) && (myConsumer.getOcCode1().equals("XRR")) ||
	        	( ocAmoint1 != 0.0) && (myConsumer.getOcCode1().equals("P.RENTAL")))
	        	{
	        		ocFinalV = ocAmoint1*(evat/100)*finalVat/evat;
	        		ocWithholdingT = (ocAmoint1-(ocAmoint1*(evat/100)))*
	        				         (rentalWithholding/100);	       		
	        		
	        	}
	        if (( ocAmoint2!= 0.0) && (myConsumer.getOcCode2().equals("XRR")) ||
	            	( ocAmoint2 != 0.0) && (myConsumer.getOcCode2().equals("P.RENTAL")))
	            	{
	            		ocFinalV = ocAmoint2*(evat/100)*finalVat/evat;
	            		ocWithholdingT = ocWithholdingT+(ocAmoint2-(ocAmoint2*(evat/100)))*
	            				         (rentalWithholding/100);	       		
	            		
	            	}
	        
	        if (( ocAmoint3!= 0.0) && (myConsumer.getOcCode3().equals("XRR")) ||
	            	( ocAmoint3 != 0.0) && (myConsumer.getOcCode3().equals("P.RENTAL")))
	            	{
	            		ocFinalV = ocAmoint3*(evat/100)*finalVat/evat;
	            		ocWithholdingT = ocWithholdingT+(ocAmoint3-(ocAmoint3*(evat/100)))*
	            				         (rentalWithholding/100);	       		
	            		
	            	}
	        
	        
	        
            //==========================================================
	        
	        if ( (myConsumer.getRateCode().equals("H"))  ||
	             (myConsumer.getRateCode().equals("L")) )
	        {	          	
	          result.add(" \n");
	          result.add("Note:Important reminders on Withholding Tax \n"+
	                     "    upon payment, please attach the BIR Form \n"+
	        		     "    with the corresponding tax withheld. \n");
	          
	          result.add("\n"); 
	         }
	                
	        finalV = finalV + ocFinalV;
	        withholdingT = withholdingT + ocWithholdingT;
	        
	       
	       if ((myConsumer.getRateCode().equals("H")) && (myConsumer.getRcode().equals("P")) ||
	           (myConsumer.getRateCode().equals("L")) && (myConsumer.getRcode().equals("P")))
	           {
	        	result.add("  Final Vat       (BIR Form2306)5%    "+         										
	        			    formatVat.format(finalV)+"\n");        	
	           }
	        if (myConsumer.getRateCode().equals("H") || myConsumer.getRateCode().equals("L") )
	           {
	            result.add("  Withholding Tax(BIR Form2307)2%:5%  "+            			                          
	            		    formatVat.format(withholdingT)+"\n");             	
	           }           
	      
	    
	        return result;
	        
        }else {
        	
        	result.add((char) 28
                    + bodyLineGenerator("CURRENT BILL",
                    amountFormat.format(compute.currentBill())) + (char) 29);
           
            
            result.add((char) 28
                    + bodyLineGenerator("Total Amount Due :",
                    amountFormat.format(compute.totalBill())) + (char) 29
                    + (char) 10);

    		
            //================================================
            //================================================
            double finalV = 0.0; 
            double ocFinalV = 0.0;
            double withholdingT = 0.0;
            double ocWithholdingT = 0.0;
            double ocAmoint1 = myConsumer.getOcAmount1();
            double ocAmoint2 = myConsumer.getOcAmount2();
            double ocAmoint3 = myConsumer.getOcAmount3();
            double evat = rate.getEvat();
            double finalVat = rate.getFinalvat(); 
            double Withholdingtax = rate.getWithholdingtax();
            double rentalWithholding = rate.getRentalWitholding();
            
            finalV = (compute.dcDemand()+
    		          compute.dcDistribution()+
    				  compute.scRetailCust()+
    				  compute.scSupplySys()+
    				  compute.mcRetailCust()+
    				  compute.mcSystem()+
    				  compute.icCrossSubsidy()+
    				  compute.lifelineDiscSubs()+
    				  compute.seniorCitizenDiscSubs()+ 				  
    				  compute.OtherSeniorCitizenRateAdj()+
    				  compute.OtherLifelineRateCostAdj()+
    				  compute.reinvestmentFundSustCapex())*(finalVat/100);
            withholdingT = (compute.dcDemand()+
    		          compute.dcDistribution()+
    				  compute.scRetailCust()+
    				  compute.scSupplySys()+
    				  compute.mcRetailCust()+
    				  compute.mcSystem()+
    				  compute.icCrossSubsidy()+
    				  compute.lifelineDiscSubs()+
    				  compute.seniorCitizenDiscSubs()+ 
    				  compute.OtherSeniorCitizenRateAdj()+
    				  compute.OtherLifelineRateCostAdj()+
    				  compute.reinvestmentFundSustCapex())*(Withholdingtax/100);
            
            if (( ocAmoint1!= 0.0) && (myConsumer.getOcCode1().equals("XRR")) ||
            	( ocAmoint1 != 0.0) && (myConsumer.getOcCode1().equals("P.RENTAL")))
            	{
            		ocFinalV = ocAmoint1*(evat/100)*finalVat/evat;
            		ocWithholdingT = (ocAmoint1-(ocAmoint1*(evat/100)))*
            				         (rentalWithholding/100);	       		
            		
            	}
            if (( ocAmoint2!= 0.0) && (myConsumer.getOcCode2().equals("XRR")) ||
                	( ocAmoint2 != 0.0) && (myConsumer.getOcCode2().equals("P.RENTAL")))
                	{
                		ocFinalV = ocAmoint2*(evat/100)*finalVat/evat;
                		ocWithholdingT = ocWithholdingT+(ocAmoint2-(ocAmoint2*(evat/100)))*
                				         (rentalWithholding/100);	       		
                		
                	}
            
            if (( ocAmoint3!= 0.0) && (myConsumer.getOcCode3().equals("XRR")) ||
                	( ocAmoint3 != 0.0) && (myConsumer.getOcCode3().equals("P.RENTAL")))
                	{
                		ocFinalV = ocAmoint3*(evat/100)*finalVat/evat;
                		ocWithholdingT = ocWithholdingT+(ocAmoint3-(ocAmoint3*(evat/100)))*
                				         (rentalWithholding/100);	       		
                		
                	}
            
            
            result.add("");
            
            //===Breakdown Over and Under Rate===================
            //===================================================           
            /*
            if ((compute.totalOU() < 0)|| (compute.totalOUd() < 0) ) {
            	OUstr = "= Php ("+amountFormat.format(compute.totalOU()*-1)+")";
            	OUstr_d = "= Php ("+amountFormat.format(compute.totalOUd()*-1)+")";
            }else if ((compute.totalOU() > 0)|| (compute.totalOUd() > 0)){
            	OUstr = "= Php "+amountFormat.format(compute.totalOU());
            	OUstr_d = "= Php "+amountFormat.format(compute.totalOUd());
            }
            
            if ((compute.rateOU() < 0)|| (compute.rateOUd() < 0) ) {
            	OURstr = "("+rateFormat.format(compute.rateOU()*-1)+")";
            	OURstr_d = "("+rateFormat.format(compute.rateOUd()*-1)+")";
            }else if ((compute.totalOU() > 0)|| (compute.totalOUd() > 0)){
            	OURstr = rateFormat.format(compute.rateOU());
            	OURstr_d = rateFormat.format(compute.rateOUd());
            }
            
            if(compute.rateOU() == 0){
            	OURstr = "";
            	OUstr = "";            	
            }
            
            if(compute.rateOUd()==0){
            	OUstr_d = "";
            	OURstr_d = "";
            	DmndStr="";
            }
            
            result.add((char) 29+"Notes: Total Over/Under Recovery Rate. \n"+(char) 28+
                                         "   Rate/Kwhr        "+OURstr+ OUstr + "\n"+
                                         " "+DmndStr+OURstr_d+OUstr_d);                   
            
            result.add("");*/
            
            //====End of Over and under rate breakdown==================
            //==========================================================
            
            if ( (myConsumer.getRateCode().equals("H"))  ||
                 (myConsumer.getRateCode().equals("L")) )
            {
              result.add((char) 29+"Note:Important reminders on Withholding Tax \n"+
                                   "    upon payment, please attach the BIR Form with \n"+
            		               "    the corresponding tax withheld. \n");
             }
                    
            finalV = finalV + ocFinalV;
            withholdingT = withholdingT + ocWithholdingT;
            
           result.add(""); 
           if ((myConsumer.getRateCode().equals("H")) && (myConsumer.getRcode().equals("P")) ||
               (myConsumer.getRateCode().equals("L")) && (myConsumer.getRcode().equals("P")))
               {
            	result.add(bodyLineGeneratorIndent("   Final Vat      (BIR Form 2306) 5%    ",         										
            			(char) 28+ amountFormat.format(finalV))+(char) 29);        	
               }
            if (myConsumer.getRateCode().equals("H") || myConsumer.getRateCode().equals("L") )
                    {
                 	result.add(bodyLineGeneratorIndent("   Withholding Tax(BIR Form 2307) 2%:5% ",             			                          
                 			(char) 28+ amountFormat.format(withholdingT))+(char) 29);             	
                    }              
           result.add(" \n ");  
                   
           return result;        

        }
        

    }

    public List<String> soaFooter() {
        List<String> result = new ArrayList<String>();
        Time dueDate = new Time();
        Time discoDate = new Time();
        dueDate.setToNow();
        discoDate.setToNow();
        dueDate.monthDay += 9;
        discoDate.monthDay += 12;
        dueDate.normalize(true);
        discoDate.normalize(true);
        
        //====Set cuurent date and date filtered==============
        Date currDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateEnd = new Date();
        Date dateNo = new Date();
        
        try
        {
        	dateEnd = dateformat.parse("2019-05-30");
        	dateNo = dateformat.parse("2019-02-07");
        }
        catch(ParseException ex)
        {}
        //====================================================

        //========================================
    	//===================GetDevice Name=======
        String DName = "";
        if(SplashScreen.btPrinter.getDeviceN() != null)
        	DName = SplashScreen.btPrinter.getDeviceN();
        else 
        	DName ="";
        
    	if(DName.length()>=8){
           DName = DName.substring(0,8); 
        }else{
           DName="";
        }
    	//=========================================
    	
        if (DName.equals("SPP-R310")) {
        	
	        //result.add("\n"); 
	        if (!dsUnpaid.hasUnpaid(myConsumer.getCode())) {
	            result.add( "\n"+"Current Bill Due on "
	                    + dueDate.format("%D") + "\n");
	            result.add("Disconnection Date is on " + discoDate.format("%D"));
	            result.add(" \n ");  
	        }
	        result.add("");
	        if (myConsumer.getSeniorCitizenNumMon() < 30
	                && myConsumer.getSeniorCitizenNumMon() > 0) {
	            result.add(StringManager.leftJustify("  Your Senior Citizen application", 33)
	                    + "\n");
	            result.add(StringManager.leftJustify("will  only  be valid for the next", 33)
	                    + "\n");
	            result.add(StringManager.leftJustify(myConsumer.getSeniorCitizenNumMon()
	                    .toString() + " days.  Please renew your senior", 33)
	                    + "\n");
	            result.add(StringManager.leftJustify("citizen application.", 33) + "\n");
	            result.add(StringManager.leftJustify("Thank you", 33) + "\n");
	        }
	        
	        /* This statement should used during election campaigned===============
	        if(currDate.before(dateEnd))
		    {		        
		        result.add("\n------------------------------------------------");
		       	result.add("\n       Atong Kooperatiba, atong suportahan.");
		        //result.add((char) 29+"");
		       	result.add("\n   Gikan sa...");
		       	
		       	if(currDate.after(dateNo))		       		
		       		result.add("\n#152  ");
		       	else
		       		result.add("\n      ");
		       	
		       	result.add("Ako PaDayon Pilipino");
		       	result.add("\n                   Party List");
		    }		    
		    ======================================================================*/
	        
	        result.add("\n  FM-CAD-08          00           07-01-19 ");
	        result.add(" \n ");  
	        result.add(" \n ");  
	        result.add(" \n "); 
	        result.add(" \n ");
	        result.add(" \n "); 
	        result.add(" \n ");
	        
        } else {
        	
        	if (!dsUnpaid.hasUnpaid(myConsumer.getCode())) {
                result.add((char) 28 + "Current Bill Due on "
                        + dueDate.format("%D") + "\n");
                result.add("Disconnection Date is on " + discoDate.format("%D")
                        + "\n");
            }
            result.add((char) 29 + "");
            if (myConsumer.getSeniorCitizenNumMon() < 30
                    && myConsumer.getSeniorCitizenNumMon() > 0) {
                result.add(StringManager.leftJustify("  Your Senior Citizen application", 33)
                        + "\n");
                result.add(StringManager.leftJustify("will  only  be valid for the next", 33)
                        + "\n");
                result.add(StringManager.leftJustify(myConsumer.getSeniorCitizenNumMon()
                        .toString() + " days.  Please renew your senior", 33)
                        + "\n");
                result.add(StringManager.leftJustify("citizen application.", 33) + "\n");
                result.add(StringManager.leftJustify("Thank you", 33) + "\n");
            }                       
           
            /* This statement should used during election campaigned===============
		    if(currDate.before(dateEnd))
		    {		       
		        result.add("\n--------------------------------------------------------");
		       	result.add("\n"+(char) 28+"           Atong Kooperatiba, atong suportahan.");
		        result.add((char) 29+"");
		       	result.add("\n          Gikan sa...");
		       	
		       	if(currDate.after(dateNo))		       		
		       		result.add("\n#152 ");
		       	else
		       		result.add("\n     ");
		       	
		       	result.add(" Ako PaDayon Pilipino");
		       	result.add("\n          Party List"); 
		    }
	         ==========================================================*/
            
		    result.add("\n  FM-CAD-08          00           07-01-19 ");
		    result.add(" \n "); 
	        result.add(" \n ");         
	         
	        //result.add("\n"+compute.vatUB()+" Ronel");     
	        
            result.add(pageBreak());  
            //Ang Kuryente maoy paglaom, Aron kita magmauswagon. Gikan sa...
        }       
                
        return result;  
    }
    
  //===GetnCurrent Date================================
    private String getDateTime() {     	
    	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	   Date date = new Date(); 
    	   return dateFormat.format(date); 
   }

    private String bodyLineGenerator(String description, String rate,
                                     String amount) {
        String result = "";
        result += StringManager.leftJustify(description, 25) + StringManager.rightJustify(rate, 8)
                + StringManager.rightJustify(amount, 14) + "\n";
        return result;
    } 

    private String bodyLineGeneratorIndent(String description, String amount) {
        String result = "";
        result += StringManager.leftJustify(description, 20) + StringManager.rightJustify(amount, 14)
                + "\n";
        return result;
    }

    private String bodyLineGeneratorWithAt(String description, String rate,
                                           String amount) {
        String result = "";
        result += StringManager.leftJustify(description, 24) + "@" + StringManager.rightJustify(rate, 8)
                + StringManager.rightJustify(amount, 14) + "\n";
        return result;
    }

    private String bodyLineGenerator(String description, String amount) {
        String result = "";
        result += StringManager.leftJustify(description, 33) + StringManager.rightJustify(amount, 14)
                + "\n";
        return result;
    }

    private String billingPeriodStr(String myDate) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. yyyy",
                Locale.getDefault());
        SimpleDateFormat bsdf = new SimpleDateFormat("MM/dd/yyyy",
                Locale.getDefault());
        try {
            result = sdf.format(bsdf.parse(myDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String pageBreak() {
        String result = "";
        for (int i = 0; i <= 12; i++) {
            result += "\n";
        }
        return result;
    }
}
