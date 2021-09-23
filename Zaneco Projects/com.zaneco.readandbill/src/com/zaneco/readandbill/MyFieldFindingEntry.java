package com.zaneco.readandbill;

import android.os.Bundle;
import android.widget.TextView;

import com.zaneco.readandbill.database.Consumer;
import com.zaneco.readandbill.database.ConsumerDataSource;

public class MyFieldFindingEntry extends com.generic.readandbill.MyFieldFindingEntry {
	
	private TextView mCMInitialReading;
	private TextView mCMReading;
	
	private Consumer consumer;
	private ConsumerDataSource dsConsumer;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        dsConsumer = new ConsumerDataSource(this);
        super.onCreate(savedInstanceState);
	}
	
	protected void initControls(){
		super.initControls();
        mCMInitialReading = (TextView) findViewById(R.id.tvCICMInitialReading);
        mCMReading = (TextView) findViewById(R.id.tvCICMReading);

		consumer = dsConsumer.getConsumer(extras.getLong("id"));
		
		mMultiplier.setText(String.valueOf(consumer.getMultiplier()));
		mCMInitialReading.setText(String.valueOf(consumer.getCmInitialReading()));
		mCMReading.setText(String.valueOf(consumer.getCmReading()));
	}
	
}
