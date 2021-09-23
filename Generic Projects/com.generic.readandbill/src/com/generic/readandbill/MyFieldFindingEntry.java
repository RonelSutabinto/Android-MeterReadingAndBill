package com.generic.readandbill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.generic.readandbill.database.*;

import java.util.ArrayList;
import java.util.List;

public class MyFieldFindingEntry extends Activity implements OnClickListener, OnFocusChangeListener {
	protected boolean updateList = false;
	protected int activityMode;
	
	private TextView mAccountNumber;
	private TextView mRateCode;
	protected TextView mMultiplier;
	private TextView mName;
	private TextView mAddress;
	private TextView mInitialReading;
	
	protected EditText ffCode;
	protected Spinner mFieldFinding;
	protected TextView mRemarks;
	protected Button mConfirm;
	protected Button mCancel;
	
	protected final int INSERT_ACTIVITY = 10;
	protected final int UPDATE_ACTIVITY = 20;	
	
	protected FieldFindingDataSource dsfieldfinding;
	protected List<FieldFindings> fieldFindings;
	
	protected Bundle extras;
	protected Consumer consumer;
	protected Reading reading;
	protected ConsumerDataSource dsConsumer;
	
	protected ReadingDataSource dsReading;	
	
	private int listPosition;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fieldfinding_entry);
		if (savedInstanceState != null) {
			extras = savedInstanceState;
		} else {
			extras = getIntent().getExtras();
		}
		
		dsfieldfinding = new FieldFindingDataSource();
		dsReading = new ReadingDataSource(this);
		dsConsumer = new ConsumerDataSource(this);

        fieldFindings = dsfieldfinding.getAllFieldFindings();
		listPosition = extras.getInt("pos");
		initControls();
	}

	protected void initControls(){
		consumer = dsConsumer.getConsumer(extras.getLong("id"));
		reading = dsReading.getReading(consumer.getId(), ReadingDataSource.CONSUMER_ID);
		mAccountNumber = (TextView) findViewById(R.id.tvCIAccountNumber);
		mRateCode = (TextView) findViewById(R.id.tvCIRateCode);
		mMultiplier = (TextView) findViewById(R.id.tvCIMultiplier);
		mName = (TextView) findViewById(R.id.tvCIName);
		mAddress = (TextView) findViewById(R.id.tvCIAddress);
		mInitialReading = (TextView) findViewById(R.id.tvCIInitialReading);
				
		mRemarks = (TextView) findViewById(R.id.etFFERemarks);
		
		mAccountNumber.setText(consumer.getAccountNumber());
		mRateCode.setText(consumer.getRateCode());
		
		mName.setText(consumer.getName());
		mAddress.setText(consumer.getAddress());
		
		mInitialReading.setText(String.valueOf(consumer.getInitialReading()));
		
		List<String> spinnerArray = setList();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>
			(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
							
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mFieldFinding = (Spinner) findViewById(R.id.spnFieldFindings);
		mFieldFinding.setAdapter(adapter);		
		mFieldFinding.setSelection(0);		
		mFieldFinding.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mConfirm.setEnabled(mFieldFinding.getSelectedItemPosition() != 0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				mConfirm.setEnabled(false);
			}
		});
		
		
		mConfirm = (Button) findViewById(R.id.bfConfirm);
		mCancel = (Button) findViewById(R.id.bfCancel);
		
		mRemarks.setText("");
		mConfirm.setEnabled(false);
		
		mConfirm.setOnClickListener(this);		
		mCancel.setOnClickListener(this);
		
		if (reading != null && reading.getId() != -1){
			activityMode = UPDATE_ACTIVITY;
			String fieldFinding = dsfieldfinding.getDescription(dsfieldfinding.getCode(reading.getFieldFinding()));
			for (int i = 0; i < fieldFindings.size(); i++){
				String s = (String) mFieldFinding.getItemAtPosition(i);
				if (s.equalsIgnoreCase(fieldFinding)){
					mFieldFinding.setSelection(i);
					break;
				}
			}
			mRemarks.setText(reading.getRemarks());
		} else {
			reading.setidConsumer(consumer.getId());
			activityMode = INSERT_ACTIVITY;
		}
				
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(reading != null && mFieldFinding.getSelectedItemPosition() != 0){
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
		outState.putLong("id", consumer.getId());
	}

	protected List<String> setList(){
		List<FieldFindings> ffs = dsfieldfinding.getFfs();
		List<String> spinnerArray = new ArrayList<String>();
		
		for (int i = 0; i < ffs.size(); i++){
			spinnerArray.add(ffs.get(i).getfDescription());
		}
		
		ffCode = (EditText) findViewById(R.id.etFFEFieldFindingsCode);
		ffCode.setOnFocusChangeListener(this);
		
		return spinnerArray;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bfConfirm){			
			updateList = false;
			passData();
			switch (activityMode) {
			case INSERT_ACTIVITY:
				dsReading.createReading(reading);
				updateList = true;
				break;
			case UPDATE_ACTIVITY:
				dsReading.updateReading(reading);
				updateList = true;
				break;
			default:
				break;
			}
			finish();
		} else if (v.getId() == R.id.bfCancel){
			finish();
		}
	}
	
	protected void passData(){
		Time readingDate = new Time();
		readingDate.setToNow();
		reading.setidConsumer(consumer.getId());
		reading.setRemarks(mRemarks.getText().toString());
		reading.setFieldFinding((long) mFieldFinding.getSelectedItemPosition());
		reading.setIsAM(readingDate.format("%p").equals("AM"));
		reading.setReadingDate(readingDate.format("%m%d"));
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.etFFEFieldFindingsCode){
			if (!hasFocus && !ffCode.getText().toString().trim().equals("")){
				for (int i = 0; i < mFieldFinding.getCount(); i++){
					String s = (String) mFieldFinding.getItemAtPosition(i);
					if (s.equals(dsfieldfinding.getDescription(ffCode.getText().toString()))){
						mFieldFinding.setSelection(i);
					}
				}
			}
		} else if(v.getId() == R.id.spnFieldFindings) {
			if (!hasFocus){
				ffCode.setText(dsfieldfinding.getCode(mFieldFinding.getItemAtPosition(mFieldFinding.getSelectedItemPosition()).toString()));
			}
		}
	}
}
