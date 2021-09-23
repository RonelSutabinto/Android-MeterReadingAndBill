package com.zaneco.readandbill;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.generic.readandbill.MyConsumerList;
import com.generic.readandbill.R;
import com.generic.readandbill.SplashScreen;
import com.zaneco.readandbill.database.ComputeCharges;
import com.zaneco.readandbill.database.Consumer;
import com.zaneco.readandbill.database.ConsumerDataSource;

public class MyReadingEntry extends com.generic.readandbill.MyReadingEntry {	
	protected Integer consumerCode =0;
    protected String ATMref="";
    protected Double billedAmount = 0.0;
    protected String billedDate="";    
    protected String tmpCode;
    
	private ComputeCharges zCompute;
	private Consumer consumer;
	private ConsumerDataSource dsConsumer;
	
	private TextView mCMInitialReading;
	private TextView mCMReading;
	private TextView mCMKilowatthour;
	private TextView mInitialReading;
    private TextView mConnectionStatus;
    private TextView mXFormerQty;
    private TextView mXFormerKVA;
    private String aa;
    
    protected DecimalFormat kilowattUsed;
    protected DecimalFormat amntFormat;
    private MyConsumerList consumerList;
    
    public MyReadingEntry(){
    	kilowattUsed = new DecimalFormat("######0.0");
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        dsConsumer = new ConsumerDataSource(this);
        super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initControls() {
		consumer = dsConsumer.getConsumer(extras.getLong("id"));
		compute = new ComputeCharges(this, consumer);

        compute.setReading(reading);

        zCompute = (ComputeCharges) compute;

		mCMInitialReading = (TextView) findViewById(R.id.tvCICMInitialReading);
		mCMReading = (TextView) findViewById(R.id.tvCICMReading);
		mCMKilowatthour = (TextView) findViewById(R.id.tvCICMKilowatthour);
		mInitialReading = (TextView) findViewById(R.id.tvCIInitialReading);
        mConnectionStatus = (TextView) findViewById(R.id.tvCIConStatus);
        mXFormerQty = (TextView) findViewById(R.id.tvXFormerQty);
        mXFormerKVA = (TextView) findViewById(R.id.tvXFormerKVA);
		
		mCMInitialReading.setText(String.valueOf(consumer.getCmInitialReading()));
		mCMReading.setText(String.valueOf(consumer.getCmReading()));
		//mCMKilowatthour.setText(String.valueOf(consumer.getCMKilowatthourRead()));DecimalFormat
		mCMKilowatthour.setText(kilowattUsed.format(consumer.getCMKilowatthourRead()));
		mInitialReading.setText(String.valueOf(consumer.getInitialReading()));	
		mXFormerQty.setText(String.valueOf(consumer.getXFormerQty()));
		mXFormerKVA.setText(String.valueOf(consumer.getXFormerKVA()));
		
        if (consumer.getConnectionCode() == 4)
            mConnectionStatus.setText("DISCO");
        else
            mConnectionStatus.setText("CONN");
		
		super.initControls();
		
		// billedDate.substring(2, 6)+
		tmpCode =  consumer.getAccountNumber().toString().substring(0,2)+ String.format("%7s",String.valueOf(consumer.getCode())).replace(' ','0');
	}

	@Override
	protected void assignUI() {
		super.assignUI();
		mKilowatthour.setText(kilowattFormatter.format(this.zCompute.getKilowatthourRprt()));
		mCurrBill.setText(amountFormatter.format(this.zCompute.currentBill()));
		mTotalBill.setText(amountFormatter.format(this.zCompute.totalBill()));
	}
	
	protected AlertDialog nothingDialog(String a) {
		AlertDialog.Builder result = new AlertDialog.Builder(this)
				.setTitle("Nothing to process")
				.setMessage(a).setPositiveButton("OK", null)
				.setCancelable(false);
		return result.create();
	}
	
    @Override
    protected void assignToReading() {
    	 Calendar cal = Calendar.getInstance();
    	 
      	 AlertDialog ad2 = new AlertDialog.Builder(this).create();
    	 SimpleDateFormat formatDateReading = new SimpleDateFormat("yyMMdd");  
    	 amntFormat = new DecimalFormat("###,###,##0.00");
    	 DecimalFormat formatToInt = new DecimalFormat("#########");
    	 
         long checkDigit1 = 0;
         long checkDigit2 = 0;
         long AMTref_total = 0;
         long forCheckD2total = 0;  
         long AccountNo_ATM = 0;
         int billedAmountInt=0;
         String CheckD2Str;
         
        super.assignToReading();
        if (!mReading.getText().toString().trim().equals("")){
            mKilowatthour.setText(kilowattFormatter.format(this.zCompute.getKilowatthourRprt()));            
            mTotalBill.setText(amountFormatter.format(this.zCompute.totalBill()));
            mCurrBill.setText(amountFormatter.format(this.zCompute.currentBill()));
        }
        
        //==============================================================
        //====ATM Reference Number======================================
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -4);
        billedDate = formatDateReading.format(cal.getTime());
        billedAmount = Double.valueOf(amountFormatter.format(this.zCompute.currentBill()).replace(",", ""));    
        
        AMTref_total = Integer.parseInt(tmpCode.substring(0, 1))*10;        
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(1,2))*8;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(2, 3))*7;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(3, 4))*6;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(4, 5))*5;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(5, 6))*4;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(6, 7))*3;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(7, 8))*2;
        AMTref_total = AMTref_total + Integer.parseInt(tmpCode.substring(8, 9))*1;
        
        checkDigit1 = AMTref_total % 9;    
        
        if (checkDigit1==0){ 
        	checkDigit1 = 9;
        }	       
               
        AccountNo_ATM = Long.valueOf("2"+ tmpCode + String.valueOf(checkDigit1));
        
        billedAmount = billedAmount * 100;        
        billedAmountInt =Integer.parseInt(formatToInt.format(billedAmount));     
     
       
        forCheckD2total = AccountNo_ATM + Long.valueOf(billedDate) + billedAmountInt;
              
        AMTref_total = 0;
        CheckD2Str = String.valueOf(forCheckD2total);
        AMTref_total = Integer.parseInt(CheckD2Str.substring(0, 1))*4;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(1, 2))*3;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(2, 3))*2;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(3, 4))*9;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(4, 5))*8;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(5, 6))*7;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(6, 7))*6;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(7, 8))*5;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(8, 9))*4;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(9, 10))*3;
        AMTref_total = AMTref_total+Integer.parseInt(CheckD2Str.substring(10, 11))*2;
        
        checkDigit2 = AMTref_total % 11;         
        
        if (checkDigit2 == 10){
        	checkDigit2 = 0;
        }
        
        reading.setAtmref("2"+
        		          tmpCode+
        		          String.valueOf(checkDigit1)+
        		          billedDate.substring(2, 6)+
        		          String.valueOf(checkDigit2));   
               
        
    }

    @Override
	protected void finishActivity(View v) {
    	
		if (v.getId() == R.id.brConfirmAndPrint) {			
						
			if (compute.getKilowatthour()>=0){
				StatementGenerator soa = new StatementGenerator(this, zCompute);
				SplashScreen.btPrinter.setCodeprintCheck(true);
				SplashScreen.btPrinter.setcodeStrPrint(consumer.getAccountNumber());//consumer.getAccountNumber().toString()
				printSOA(soa.generateSOA());
				
				finish();
			}
			
		} else {			
			finish();
		}
		
		//AlertDialog ad = nothingDialog(aa);
        //ad.show();	
		
	}
	
}
