package com.generic.readandbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.generic.readandbill.database.*;

//import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import sun.org.mozilla.javascript.internal.regexp.SubString;

public class MyReadingEntry extends Activity implements OnClickListener {

    protected boolean updateList = false;      
       
    private TextView mAccountNumber;
    private TextView mRateCode;
    private TextView mMultiplier;
    private TextView mName;
    private TextView mAddress;

    protected EditText mFeedBackCode;
    protected EditText mReading;
    protected EditText mDemandRdng;
    protected TextView mDemand;
    protected TextView mCurrBill;
    protected TextView mKilowatthour;
    protected TextView mTotalBill;
    protected TextView mRemarks;
    private Button mConfirm;
    private Button mConfirmAndPrint;
    private Button mCancel;

    protected Context context;
    protected Consumer consumer;
    protected Reading reading;
    protected ComputeCharges compute;
    protected ConsumerDataSource dsConsumer;
    protected ReadingDataSource dsReading;
    private int listPosition;

    protected static final int INSERT_ACTIVITY = 10;
    protected static final int UPDATE_ACTIVITY = 20;
    protected int activityMode;

    protected DecimalFormat kilowattFormatter = new DecimalFormat("######0.0");
    protected DecimalFormat formatDemand = new DecimalFormat("######0.0##");
    protected DecimalFormat amountFormatter = new DecimalFormat("##,###,###,##0.00");

    protected Bundle extras;

    protected static final String TAG = "ReadingEntry";
    private TextView mInitialReading;
   
  //  private TextView tv_tmpCode;
   // private EditText et_tmpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_entry);

        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else {
            extras = getIntent().getExtras();
        }

        this.context = this;
        dsConsumer = new ConsumerDataSource(this);
        dsReading = new ReadingDataSource(this);
        consumer = dsConsumer.getConsumer(extras.getLong("id"));
        reading = dsReading.getReading(consumer.getId(),
                ReadingDataSource.CONSUMER_ID);
        if (reading.getIdConsumer() == -1){
            reading.setidConsumer(consumer.getId());
        }
        listPosition = extras.getInt("pos");
        initControls();
    }

    protected void initControls() {
    	//String tmpCode;
        compute = new ComputeCharges(this, consumer);

        mAccountNumber = (TextView) findViewById(R.id.tvCIAccountNumber);
        mRateCode = (TextView) findViewById(R.id.tvCIRateCode);
        mMultiplier = (TextView) findViewById(R.id.tvCIMultiplier);
        mName = (TextView) findViewById(R.id.tvCIName);
        mAddress = (TextView) findViewById(R.id.tvCIAddress);
        mInitialReading = (TextView) findViewById(R.id.tvCIInitialReading);

        mFeedBackCode = (EditText) findViewById(R.id.etREFeedbackcode);
        mReading = (EditText) findViewById(R.id.etREReading);
        mDemand = (TextView) findViewById(R.id.tvDemand);
        mDemandRdng = (EditText) findViewById(R.id.etDemandRdng);
        mCurrBill = (TextView)findViewById(R.id.tvCurrBill);
        mKilowatthour = (TextView) findViewById(R.id.tvREKilowatthour);
        mTotalBill = (TextView) findViewById(R.id.tvRETotalBill);
        mRemarks = (TextView) findViewById(R.id.tvRERemarks);
        mConfirm = (Button) findViewById(R.id.brConfirm);
        mConfirmAndPrint = (Button) findViewById(R.id.brConfirmAndPrint);
        mCancel = (Button) findViewById(R.id.brCancel);       

        mAccountNumber.setText(consumer.getAccountNumber());       
                                 
        mRateCode.setText(consumer.getRateCode());
        mMultiplier.setText(String.valueOf(consumer.getMultiplier()));
        mName.setText(consumer.getName());
        mAddress.setText(consumer.getAddress()); 
         
        //filter Industrial consumer with fixed demand=============
        //=========================================================
        mDemandRdng.setEnabled(false);
        if((consumer.getIsfixdemand()==1) && (consumer.getRateCode().toString().equals("H"))){  
        	//mDemandRdng.setText(formatDemand.format(consumer.getDemandRdng()));
        	mDemand.setText(formatDemand.format(consumer.getDemand()));
        	//mDemandRdng.setText(formatDemand.format(consumer.getDemand()));
        } else if((consumer.getIsfixdemand()==0) && (consumer.getRateCode().toString().equals("H")) ){ 
        	mDemandRdng.setEnabled(true);
        }
        //=========================================================
        //=========================================================
         	                  
        if (mInitialReading != null)
            mInitialReading.setText(localizedFormat(consumer.getInitialReading(),Locale.getDefault()));

        mConfirm.setEnabled(false);        
        mReading.setText("");
        mKilowatthour.setText("");
        mTotalBill.setText("");
        mRemarks.setText("");      
        mDemandRdng.setText("");

        mReading.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus
                        && !mReading.getText().toString().trim().equals("")) {
                    assignToReading();
                } else if (hasFocus) {
                    mReading.selectAll();
                }
                mConfirm.setEnabled(mReading.getText().toString() != "");
            }
        });

        mDemandRdng.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus
                        && !mReading.getText().toString().trim().equals("")) {
                    assignToReading();
                } else if (hasFocus) {
                    mReading.selectAll();
                }
                mConfirm.setEnabled(mReading.getText().toString() != "");
				
			}
		});
        
      /*  mDemandRdng.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				//if (count < before) {		
                       
                    assignToReading();
                 //} else if (hasFocus) {
				//}else{
                    mReading.selectAll();
               // }
                mConfirm.setEnabled(mReading.getText().toString() != "");
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
        	
        	
        });*/
        mFeedBackCode.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus
                        && !mFeedBackCode.getText().toString().trim()
                        .equals("")) {
                    if (!hasFocus
                            && !mReading.getText().toString().trim().equals("")) {
                        assignToReading();
                    } else {
                        reading.setFeedBackCode(mFeedBackCode.getText()
                                .toString());
                    }
                }
                mConfirm.setEnabled(mReading.getText().toString() != "");
            }
        });



        mReading.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mConfirm.setEnabled(mReading.getText().toString() != "");
            }
        });

        mConfirm.setOnClickListener(this);
        mConfirmAndPrint.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        if (reading != null && reading.getId() != -1) {
            activityMode = UPDATE_ACTIVITY;
            assignUI();
        } else {
			/*reading.setCode(consumer.getCode());*/
            activityMode = INSERT_ACTIVITY;
        }
    }

    protected void assignUI() {
    	//AlertDialog ad = new AlertDialog.Builder(this).create(); 
    	
        mFeedBackCode.setText(reading.getFeedBackCode().toString());
        mFeedBackCode.setText(reading.getFeedBackCode());
        
        if (reading.getId() != -1 && reading.getDemand() != 0)     
        	 mDemand.setText(localizedFormat(reading.getDemand(),
                     Locale.getDefault()));          	
        
        if (reading.getId() != -1 && reading.getDemandRdng() != 0) 	        
          mDemandRdng.setText(formatDemand.format(reading.getDemandRdng())); //.setText("0.0");        
          
     
        if (reading.getId() != -1 && reading.getReading() != 0)
            mReading.setText(localizedFormat(reading.getReading(),
                    Locale.getDefault()));
        
        compute.setReading(reading);
        mKilowatthour.setText(localizedFormat(compute.getKilowatthourRprt(),
                Locale.getDefault()));
        mTotalBill.setText(localizedFormat(compute.currentBill(),
                Locale.getDefault()));
        mRemarks.setText(reading.getRemarks());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (reading != null && mReading.getText().toString() != "") {
            dsReading.updateReading(reading);
        }
        outState.putLong("id", consumer.getId());
    }

    @Override
    public void finish() {
        if (updateList) {
            Intent data = new Intent();
            data.putExtra("id", consumer.getId());
            data.putExtra("pos", listPosition);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        
        
        super.finish();
    }

    protected String localizedFormat(Double value, Locale loc) {
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("######0.0");
        return df.format(value);
    }

    protected void assignToReading() { 
    	//AlertDialog ad = new AlertDialog.Builder(this).create(); 
        Time readingDate = new Time();        
                 
        readingDate.setToNow();
        reading.setFeedBackCode(reading.getFeedBackCode());
        reading.setRemarks(mRemarks.getText().toString());       
               	
        reading.setTransactionDate(readingDate.toMillis(true));
        if (!mReading.getText().toString().trim().equals("")){
            reading.setReading(Double.parseDouble(mReading.getText().toString()
                    .replaceAll(",", "")));
            
            if(! mDemandRdng.getText().toString().trim().equals(""))           
              	reading.setDemandRdng(Double.valueOf(mDemandRdng.getText().toString()));
          	           	
                        
             if((!mDemand.getText().toString().trim().equals(""))&& (mDemandRdng.getText().toString().trim().equals("")))  
                 reading.setDemand(Double.valueOf(mDemand.getText().toString())); 	
                       
            if(!mDemandRdng.getText().toString().trim().equals("")) 
            {           	
            	
            	reading.setDemand(Double.valueOf(formatDemand.format(Double.valueOf(mDemandRdng.getText().toString())*
            			          Double.valueOf(mMultiplier.getText().toString()))));            	
            	            	            	            	
            	mDemand.setText(formatDemand.format(Double.valueOf(mDemandRdng.getText().toString())*
                          Double.valueOf(mMultiplier.getText().toString())
                          ));           	
            	
            }                  
                      	            	
            compute.setReading(reading);
            reading.setFeedBackCode(mFeedBackCode.getText().toString());
            reading.setKilowatthour(compute.getKilowatthourRprt());
            reading.setTotalbill(compute.currentBill());
            reading.setSeniorCitizenDiscount(compute.seniorCitizenDiscRate());
            
            
            mKilowatthour.setText(kilowattFormatter.format(compute.getKilowatthourRprt()));
            mTotalBill.setText(amountFormatter.format(compute.currentBill()));
            mCurrBill.setText(amountFormatter.format(compute.currentBill()));          
        
        }
        reading.setIsAM(readingDate.format("%p").equals("AM"));
        reading.setReadingDate(readingDate.format("%m%d"));       
       // ad.setMessage(String.valueOf(compute.currentBill()));
      //  ad.show();       
                
        
    }

    @Override
    public void onClick(View v) {
    	AlertDialog ad = new AlertDialog.Builder(this).create();
    	//AlertDialog ad= new AlertDialog.Builder(context).create();
		
    	//ad.setMessage("Ronel Sutabinto");
		//ad.show();  
    	try{
	        if (v.getId() == R.id.brConfirm || v.getId() == R.id.brConfirmAndPrint) {         	
	        	updateList = false;              	       		       	
	        	
	            assignToReading();          	            
	            updateList = (activityMode == INSERT_ACTIVITY || activityMode == UPDATE_ACTIVITY);
	            updateReading();
	            
	            
	        }
	        
	        finishActivity(v);
        
    	}catch(Exception e){  
    		
    		ad.setMessage(e.getMessage());
    		ad.show();    		
    	}
    	
        
    }

    protected void updateReading() {
        switch (activityMode) {
            case INSERT_ACTIVITY:
                dsReading.createReading(reading);
                break;
            case UPDATE_ACTIVITY:
                dsReading.updateReading(reading);
                break;
            default:
                break;
        }
    }

    protected void finishActivity(View v) {
        finish();
    }

    protected AlertDialog setAlertdialogBox(int mode){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (mode) {
            case 0:
                adb.setTitle("bluetooth not yet configured");
                adb.setMessage("Could not proceed to printing");
                break;
            case 1:
                adb.setTitle("Bluetooth is still printing");
                adb.setMessage("Wait till i'm done");
                break;
            default:
                break;
        }
        adb.setCancelable(false);
        adb.setPositiveButton("OK", null);
        return adb.create();
    }

    protected void printSOA(List<String> soaDetail){
    	
    	if (SplashScreen.btPrinter.print(soaDetail)) {
            finish();
        } else {
			/*if (SplashScreen.btPrinter.getBtd() == null) {
				AlertDialog ad = setAlertdialogBox(0);
				ad.show();
				SplashScreen.btPrinter.turnOffBT();
			} else if (SplashScreen.btPrinter.getBta().isEnabled()) {			
				AlertDialog ad = setAlertdialogBox(1);
				ad.show();
			} */
        }
    }



}
