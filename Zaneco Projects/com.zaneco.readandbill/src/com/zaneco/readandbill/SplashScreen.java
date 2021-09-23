package com.zaneco.readandbill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidapp.mytools.objectmanager.StringManager;
import com.generic.readandbill.R;
import com.generic.readandbill.database.FieldFindingDataSource;
import com.generic.readandbill.database.Reading;
import com.generic.readandbill.database.UnreadAccounts;
import com.generic.readandbill.database.ZoneReport;
import com.zaneco.readandbill.database.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends com.generic.readandbill.SplashScreen {

    private UnpaidDataSource dsUnpaid;
    private NewConnectionDataSource dsnc;
    private ConsumerDataSource dsConsumer; 
    private RateDataSource dsRates;
    private UserProfileDataSource dsUserProfile;
    private BillhistoryDataSource dsBillhistory;

    private static final int RATES = 30;
    private static final int UNPAID = 40;
    private static final int PREVIOUSR = 99;    

    public static final String version = "2020.11.16"; 

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.iProcessTextFile) {
            if (readFile()){

            }
            //dsBillhistory.refreshBillhistory(); 
            return true;
        } else if (item.getItemId() == R.id.iGenerateResult) {
            generateResultFile(initializeResultFields(),Environment.getExternalStorageDirectory()
                    + "/ReadAndBill/OutFiles/result.txt");
            return true;
        } else if (item.getItemId() == R.id.iConsumerList) {
            startActivity(new Intent(this, MyConsumerList.class));
            return true;
        } else if (item.getItemId() == com.zaneco.readandbill.R.id.iNewCon) {
            startActivity(new Intent(this, NewConsumerList.class));
            return true;
        } else if (item.getItemId() == R.id.iBatchPrinting) {
            startActivity(new Intent(this, BatchPrintingSOA.class));
            return true;
        } else if (item.getItemId() == R.id.iSummaryList) {
            startActivity(new Intent(this, SummaryList.class));
            return true;
        } else if (item.getItemId() == R.id.iZoneReport) {
            generateZoneReport();
            return true;
        }else if (item.getItemId() == R.id.iPreviousRecord) {        	
       	            	
        	/*
        	 Class ourClass = Class.forName("com.example.androidsample."+choiceClick);
			Intent ourIntent = new Intent(Menu.this,ourClass);
			startActivity(ourIntent);
        	 */
        	/*        	
        	Class ourClass;
			try {
				
				ourClass = Class.forName("com.zaneco.readandbill.BillhistoryList");
				startActivity(new Intent(SplashScreen.this,ourClass));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        	
       	    return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

   
    protected boolean readFile() {
        boolean result = false;
        File myFile = new File(filePath);
        if (myFile.exists()){
            initializeDatabase();
            List<String> dataList;
            dataList = retrieveStringFromFile(filePath);
            readDataList(dataList);
            result = true;
        } else {
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("File not found!");
            ad.show();
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dsConsumer = new ConsumerDataSource(this);
        dsUserProfile = new UserProfileDataSource(this);
        dsUnpaid = new UnpaidDataSource(this);
        dsnc = new NewConnectionDataSource(this);
        dsFF = new FieldFindingDataSource();
        dsRates = new RateDataSource(this);
        dsBillhistory = new BillhistoryDataSource(this);
        splashScreenImage = (ImageView) findViewById(R.id.imageView1);
        splashScreenImage.setImageResource(com.zaneco.readandbill.R.drawable.zaneco);
    }



    public void readDataList(List<String> dataList) {
        int mode = NOTHING;
        
        File myFile = new File(filePath);
                
        for (int i = 0; i <= dataList.size() - 1; i++) {
            if (dataList.get(i).equals("DOWNLOAD")) {
                mode = USERPROFILE;
            } else if (dataList.get(i).equals("END TRANS")) {
                mode = CONSUMER;
            } else if (dataList.get(i).equals("END MASTER")) {
                mode = UNPAID;
            } else if (dataList.get(i).equals("END UNPAID")) {
                mode = RATES;          
               
            } else if (dataList.get(i).endsWith("END PREVIOUS READING")){ 
                 mode = PREVIOUSR;            	
            } else if (dataList.get(i).equals(".....")) {
                AlertDialog ad = doneDialog("Processing Text File");
                ad.show();
            } else {               	
            	if (mode==PREVIOUSR) {            		
            		insertBillhistory(dataList.get(i));
            	}else{
            		createData(listToObject(dataList.get(i), mode), mode);
            	}           	
            	
            }
        }
        if (myFile.exists()) {
            myFile.delete();
        }
    }
    private void insertBillhistory(String dataValues){
    	String[] data;        
        data = dataValues.split("~");
        
        try {   
        	dsBillhistory.open();
    		dsBillhistory.createBillhistory(Long.parseLong(data[0]),data[1],data[2],Double.parseDouble(data[3]),Double.parseDouble(data[4]),Double.parseDouble(data[5]),data[6],Double.parseDouble(data[7]));
    		dsBillhistory.close();
    	}catch (Exception e) {
			// TODO: handle exception
    		Log.e("Code error: ",e.getMessage());
		}		
    }
    private Object listToObject(String dataValues, int mode) {
        String[] data;
        
        data = dataValues.split("~");
        switch (mode) {
            case CONSUMER:
                Consumer consumer = new Consumer();
                consumer.setCode(Long.parseLong(data[0]));
                consumer.setAccountNumber(data[1]);
                consumer.setName(data[2].trim());
                consumer.setAddress(data[3].trim());
                consumer.setRateCode(data[4]);
                consumer.setMultiplier(Double.parseDouble(data[5]));
                consumer.setMeterSerial(data[6]);
                consumer.setEvatDiscount(Double.parseDouble(data[7]));            
                consumer.setInitialReadingDate(data[8]);
                consumer.setInitialReading(Double.parseDouble(data[9]));
                consumer.setCmSwitch(Integer.parseInt(data[10]) == 1);
                consumer.setCmMultiplier(Double.parseDouble(data[11]));
                consumer.setCmReading(Double.parseDouble(data[12]));
                consumer.setCmInitialReading(Double.parseDouble(data[13]));
                consumer.setCmDemand(Double.parseDouble(data[14]));   
                consumer.setisGram(Integer.parseInt(data[15]));
                consumer.setOcCode1(data[16]);
                consumer.setOcAmount1(Double.parseDouble(data[17]));
                consumer.setOcCode2(data[18]);
                consumer.setOcAmount2(Double.parseDouble(data[19]));
                consumer.setOcCode3(data[20]);
                consumer.setOcAmount3(Double.parseDouble(data[21]));
                consumer.setConnectionCode(Integer.parseInt(data[22]));                
                consumer.setRptTax(Double.parseDouble(data[23]));
                consumer.setDemand(Double.parseDouble(data[24]));   
                consumer.setPantawidSubsidy(Double.parseDouble(data[25])); 
                consumer.setPantawidRecoveryRef(data[26]);
                consumer.setPantawidRecovery(Double.parseDouble(data[27]));
                consumer.setSeniorCitizenNumMon(Integer.parseInt(data[28].trim()));
                consumer.setIsfixdemand(Integer.parseInt(data[29].trim()));
                consumer.setPoleNumber(data[30].trim());
                consumer.setXFormerQty(Integer.parseInt(data[31]));
                consumer.setXFormerKVA(data[32].trim());                
                consumer.setRcode(data[33]);
                return consumer;               
            case RATES:            	
                Rates rate = new Rates();
                rate.setRateCode(data[0]);
                rate.setGenSys(Double.parseDouble(data[1]));
                rate.setHostComm(Double.parseDouble(data[2]));
                rate.setForex(Double.parseDouble(data[3]));
                rate.setTcDemand(Double.parseDouble(data[4]));
                rate.setTcSystem(Double.parseDouble(data[5]));
                rate.setSystemLoss(Double.parseDouble(data[6]));
                rate.setDcDemand(Double.parseDouble(data[7]));
                rate.setDcDistribution(Double.parseDouble(data[8]));
                rate.setScRetailCust(Double.parseDouble(data[9]));
                rate.setScSupplySys(Double.parseDouble(data[10]));
                rate.setMcRetailCust(Double.parseDouble(data[11]));
                rate.setMcSys(Double.parseDouble(data[12]));
                rate.setUcNpcStrandedDebts(Double.parseDouble(data[13]));
                rate.setUcNpcStrandedContCost(Double.parseDouble(data[14]));
                rate.setUcDuStrandedContCost(Double.parseDouble(data[15]));
                rate.setUcme(Double.parseDouble(data[16]));
                rate.setUcEqTaxesAndRoyalties(Double.parseDouble(data[17]));
                rate.setUcec(Double.parseDouble(data[18]));
                rate.setUcCrossSubsidyRemoval(Double.parseDouble(data[19]));
                rate.setIcCrossSubsidy(Double.parseDouble(data[20]));
                rate.setParr(Double.parseDouble(data[21]));
                rate.setLifeLineSubsidy(Double.parseDouble(data[22]));
                rate.setTransSysAncRefund(Double.parseDouble(data[23]));
                rate.setGram(Double.parseDouble(data[24]));
                rate.setTransDemAncRefund(Double.parseDouble(data[25]));
                rate.setPrevYearAdjPowerCost(Double.parseDouble(data[26]));
                rate.setEvatTransAncRefund(Double.parseDouble(data[27]));
                rate.setEvat(Double.parseDouble(data[28]));
                rate.setEvatGenCo(Double.parseDouble(data[29]));
                rate.setEvatTransCo(Double.parseDouble(data[30]));
                rate.setEvatSystemLoss(Double.parseDouble(data[31]));
                rate.setReinvestmentFundSustCapex(Double.parseDouble(data[32]));               
                rate.setScSwitch(Integer.parseInt(data[34]) == 1);
                rate.setScKiloWattHourLimit(Double.parseDouble(data[35]));
                rate.setSeniorCitizenDiscount(Double.parseDouble(data[36]));
                rate.setSeniorCitizenSubsidy(Double.parseDouble(data[37]));
                rate.setOtherGenRateAdj(Double.parseDouble(data[38]));
                rate.setOtherTransCostAdj(Double.parseDouble(data[39]));
                rate.setOtherTransDemandCostAdj(Double.parseDouble(data[40]));
                rate.setOtherSystemLossCostAdj(Double.parseDouble(data[41]));
                rate.setOtherLifelineRateCostAdj(Double.parseDouble(data[42]));
                rate.setOtherSeniorCitizenRateAdj(Double.parseDouble(data[43]));
                rate.setFitall(Double.parseDouble(data[44]));
                rate.setPArecovery(Double.parseDouble(data[45]));
                rate.setFinalvat(Double.parseDouble(data[46]));
                rate.setWithholdingtax(Double.parseDouble(data[47]));    
                return rate;
            case UNPAID:
                Unpaid unpaid = new Unpaid();
                unpaid.setCode(Long.parseLong(data[0]));
                unpaid.setBillmonth(data[1]);
                unpaid.setAmount(Double.parseDouble(data[2]));
                return unpaid;
            case USERPROFILE:
                UserProfile userProfile = new UserProfile();
                userProfile.setName(data[0]);
                userProfile.setBillmonth(data[1]);
                userProfile.setTelno(data[2]);
                return userProfile;
            case PREVIOUSR:           	
            	//AlertDialog ad3 = doneDialog("Ronel Process Data");
                //ad3.show();  
            	
            	Billhistory billhistory = new Billhistory();
            	billhistory.setCode(Long.parseLong(data[0]));
            	billhistory.setAccountnumber(data[1]);
            	billhistory.setName(data[2]);
            	billhistory.setPresentreading(Double.parseDouble(data[3]));
            	billhistory.setPreviousreading(Double.parseDouble(data[4]));
            	billhistory.setDiff(Double.parseDouble(data[5]));
            	billhistory.setBillmonth(data[6]);
            	billhistory.setTotalbill(Double.parseDouble(data[7]));
            	
            	return null;            	
            default:
                return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    public void createData(Object myData, int mode) {
        switch (mode) {
            case CONSUMER:
                dsConsumer.createConsumer((Consumer) myData);
                break;
            case RATES:
                dsRates.createRates((Rates) myData);
                break;
            case UNPAID:
                dsUnpaid.createUnpaid((Unpaid) myData);
                break;
            case USERPROFILE:
                dsUserProfile.createUserProfile((UserProfile) myData);
                break;
            //case PREVIOUSR:
            //	dsBillhistory.createBillhistory((Billhistory) myData);
            //	break;
            default:
                break;
        }
    }

    @Override
    protected List<String> initializeResultFields() {
        List<String> result = new ArrayList<String>();

        List<Reading> readings = dsReading.getAllReadings();
        Time myReadingDate = new Time();
        Integer isPrinted;
        String ffCode = "0";
        String ffDescription;

        if (readings.size() > 0) {

            for (Reading reading : readings) {
                myReadingDate.set(reading.getTransactionDate());
                if (reading.getIsPrinted()) {
                    isPrinted = 1;
                } else {
                    isPrinted = 0;
                }
                ffCode = "0";
                if (reading.getFieldFinding() != -1) {
                    ffCode = dsFF.getCode(reading.getFieldFinding());
                    ffDescription = dsFF.getDescription(ffCode);
                } else {
                    ffDescription = reading.getRemarks();
                }

                result.add(StringManager.leftJustify(
                        String.valueOf(dsConsumer.getConsumer(reading.getIdConsumer()).getCode()), 6)
                        + StringManager.leftJustify("", 18)
                        + StringManager.rightJustify(
                        String.valueOf(reading.getReading()), 8)
                        + StringManager.rightJustify(
                        String.valueOf(reading.getDemand()), 4)
                        + StringManager.leftJustify(isPrinted.toString(), 1)
                        + StringManager.leftJustify(reading.getFeedBackCode(), 15)
                        + StringManager.leftJustify(ffCode, 2)
                        + StringManager.leftJustify(ffDescription, 15)
                        + StringManager.leftJustify(
                        myReadingDate.format("%m%d%H%M%S"), 10) + " "
                        + StringManager.leftJustify(
                        String.valueOf(reading.getKilowatthour()), 8)
                        + StringManager.leftJustify(reading.getAtmref(), 18)
                        + (char) 13 + "" + (char) 10);

            }
        }

        Time transactionDate = new Time();

        List<NewConnection> newCons = dsnc.getAllNewCon();

        result.add("NEW CONN");
        for (NewConnection newConnection : newCons) {
            transactionDate.set(newConnection.getTransactionDate());
            result.add(StringManager.leftJustify(newConnection
                    .getName().toString(), 30)
                    + StringManager.leftJustify(
                    newConnection.getSerial(), 15)
                    + StringManager.rightJustify(newConnection
                    .getReading().toString(), 6)
                    + StringManager.leftJustify(
                    newConnection.getRemarks(), 30)
                    + StringManager.leftJustify(
                    transactionDate.format("%m%d%H%M%S"), 10)
                    + StringManager.leftJustify(
                    newConnection.getRoute(), 4) + (char) 13 + "" + (char) 10);
        }

        return result;
    }

    private boolean generateZoneReport() {
        Integer totalConsumer = dsConsumer.getNumberOfConsumer();
        List<ZoneReport> zrs = dsReading.getZoneReport();
        List<UnreadAccounts> ura = dsConsumer.getAllUnread();
        if (zrs.size() != 0) {
            try {

                BufferedWriter bw = new BufferedWriter(new FileWriter(
                        Environment.getExternalStorageDirectory()
                                + "/ReadAndBill/OutFiles/ZoneReport.txt"));

                bw.write("Zone " + dsConsumer.getZone() + " Report \n");
                bw.write("\n");
                bw.write("--------------------------------------------------------------------------------\n");
                bw.write("|QTY 2|     |          |         |     DURATION    |  TOT TIME   |   MIN PER   |\n");
                bw.write("|B RED| QTY |   DATE   |   DAY   |  START | FINISH |    MIN      |   CONSUMER  |\n");
                bw.write("--------------------------------------------------------------------------------\n");
                for (int i = 0; i < zrs.size(); i++) {
                    bw.write("|"
                            + StringManager.rightJustify(
                            totalConsumer.toString(), 5)
                            + "|"
                            + StringManager.rightJustify(zrs.get(i)
                            .getQtyToBeRead().toString(), 5)
                            + "|"
                            + StringManager.centerJustify(zrs.get(i)
                            .getReadingDate(), 10)
                            + "|"
                            + StringManager.leftJustify(zrs.get(i)
                            .getDay(), 9)
                            + "|"
                            + StringManager.leftJustify(zrs.get(i)
                            .getMinTime(), 8)
                            + "|"
                            + StringManager.leftJustify(zrs.get(i)
                            .getMaxTime(), 8)
                            + "|"
                            + StringManager.leftJustify(zrs.get(i)
                            .getTotalTime(), 8)
                            + "|"
                            + StringManager.leftJustify(zrs.get(i)
                            .getAvgTime(), 8) + "\n");
                }
                bw.write("--------------------------------------------------------------------------------\n");
                bw.write("\n");
                bw.write("Missed Read\n");
                bw.write("-------------------------------------------\n");
                bw.write("|Account # | NAME                         |\n");
                bw.write("-------------------------------------------\n");
                for (int i = 0; i < ura.size(); i++) {
                    bw.write("|"
                            + StringManager.leftJustify(ura.get(i)
                            .getAccountNumber(), 10)
                            + "|"
                            + StringManager.leftJustify(ura.get(i)
                            .getName(), 30) + "|\n");
                }
                bw.write("-------------------------------------------\n");
                bw.close();
                AlertDialog ad = doneDialog("Zone Report");
                ad.show();
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        } else {
            AlertDialog ad = nothingDialog();
            ad.show();
            return false;
        }
    }

    @Override
    protected void initializeDatabase() {
        dsConsumer.refreshConsumer();
        dsUnpaid.refreshUnpaid();
        dsRates.refreshRates();
        dsUserProfile.refreshUserProfile();
        dsReading.truncate();
        dsnc.refreshNewConnection();
        
        dsBillhistory.open();
        dsBillhistory.refreshBillhistory();
        dsBillhistory.close();
    }
}
