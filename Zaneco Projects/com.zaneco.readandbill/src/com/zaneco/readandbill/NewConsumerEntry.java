package com.zaneco.readandbill;

import com.zaneco.readandbill.database.NewConnection;
import com.zaneco.readandbill.database.NewConnectionDataSource;

import android.app.Activity;
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

public class NewConsumerEntry extends Activity implements OnClickListener, OnFocusChangeListener, TextWatcher{

	protected EditText name;	
	protected EditText meterSerial;
	protected EditText reading;
	protected EditText route;
	protected EditText remarks;
	protected Button confirm;
	protected Button cancel;
	protected boolean updateList = false;
	
	private NewConnectionDataSource dsnc;
	private NewConnection newCon;
	
	protected int listPosition;
	
	protected final int INSERT_ACTIVITY = 10;
	protected final int UPDATE_ACTIVITY = 20;
	protected int activityMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extra;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newconsumer_entry);
		if (savedInstanceState != null){
			extra = savedInstanceState;
			activityMode = extra.getInt("am");
			newCon = dsnc.getNewCon(extra.getLong("id"));
			if (activityMode == UPDATE_ACTIVITY){
				listPosition = extra.getInt("pos");
			}
		} else {
			extra = getIntent().getExtras();
			if (extra != null){
				activityMode = UPDATE_ACTIVITY;
				newCon = dsnc.getNewCon(extra.getLong("id"));
				listPosition = extra.getInt("pos");
			} else {
				activityMode = INSERT_ACTIVITY;
				newCon = new NewConnection();
			}
		}
		setUI();
	}

	protected void setUI(){
		dsnc = new NewConnectionDataSource(this);
		name = (EditText) findViewById(R.id.etnceName);
		meterSerial = (EditText) findViewById(R.id.etnceSerial);
		reading = (EditText) findViewById(R.id.etnceReading);
		route = (EditText) findViewById(R.id.etnceRoute);
		remarks = (EditText) findViewById(R.id.etnceRemarks);
		confirm = (Button) findViewById(R.id.btnnceConfirm);
		cancel = (Button) findViewById(R.id.btnnceCancel);
		
		name.addTextChangedListener(this);
		meterSerial.addTextChangedListener(this);
		reading.addTextChangedListener(this);
		route.addTextChangedListener(this);		
		
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		name.setOnFocusChangeListener(this);
		meterSerial.setOnFocusChangeListener(this);
		reading.setOnFocusChangeListener(this);
		route.setOnFocusChangeListener(this);
		remarks.setOnFocusChangeListener(this);
		
		if (activityMode == UPDATE_ACTIVITY){
			name.setText(newCon.getName());
			meterSerial.setText(newCon.getSerial());
			reading.setText(newCon.getReading().toString());
			route.setText(newCon.getRoute());
			remarks.setText(newCon.getRemarks());			
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!name.getText().toString().equals("") && 
			!meterSerial.getText().toString().equals("") &&
			!reading.getText().toString().equals("") &&
			!route.getText().toString().equals("")){
			assignNewCon();
			if (activityMode == INSERT_ACTIVITY) {
				newCon = dsnc.createNewCon(newCon);
			} else {
				dsnc.updateNewCon(newCon);
				outState.putInt("pos", listPosition);
			}
			outState.putLong("id", newCon.getId());
			outState.putInt("am", activityMode);
		}
	}

	@Override
	public void finish() {
		if (updateList){
			Intent data = new Intent();
			data.putExtra("id", newCon.getId());
			data.putExtra("pos", listPosition);
			setResult(RESULT_OK, data);
		} else {
			setResult(RESULT_CANCELED);
		}
		super.finish();
	}

	protected void assignNewCon(){
		Time transDate = new Time();
		transDate.setToNow();
		newCon.setTransactionDate(transDate.toMillis(true));
		newCon.setName(name.getText().toString());
		newCon.setSerial(meterSerial.getText().toString());
		newCon.setReading(Double.parseDouble(reading.getText().toString()));
		newCon.setRoute(route.getText().toString());
		newCon.setRemarks(remarks.getText().toString());
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnnceConfirm){
			updateList = true;
			assignNewCon();
			switch (activityMode) {
			case INSERT_ACTIVITY:
				dsnc.createNewCon(newCon);
				break;
			case UPDATE_ACTIVITY:
				dsnc.updateNewCon(newCon);
				break;
			default:
				break;
			}
		} else if (v.getId() == R.id.btnnceCancel){
			updateList = false;
		}
		finish();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		confirm.setEnabled(!name.getText().equals("") && 
				           !meterSerial.getText().equals("") &&
				           !reading.getText().equals("") &&
				           !route.getText().equals("") &&
				           !remarks.getText().equals(""));
	}
	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		confirm.setEnabled(!name.getText().toString().equals("") &&
				  	  	   !meterSerial.getText().toString().equals("") &&
				  	  	   !reading.getText().toString().equals("") &&
				  	  	   !route.getText().toString().equals(""));
	}


}
