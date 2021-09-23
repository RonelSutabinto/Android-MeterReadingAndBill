package com.generic.readandbill;

import android.content.Context;
import com.generic.readandbill.database.ConsumerDataSource;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BatchPrintingSOA extends Activity implements OnClickListener{

	protected ConsumerDataSource dsConsumer;
	protected EditText etAccountFrom;
	protected EditText etAccountTo;
	protected Button btnContinous;
	protected Button btnPause;
    protected Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_batch_printing);
		dsConsumer = new ConsumerDataSource(this);
		etAccountFrom = (EditText) findViewById(R.id.etbpAccountFrom);
		etAccountTo = (EditText) findViewById(R.id.etbpAccountTo);
		btnContinous = (Button) findViewById(R.id.btnbpContinous);
		btnPause = (Button) findViewById(R.id.btnbpPause);
		
		btnContinous.setOnClickListener(this);
		btnPause.setOnClickListener(this);

        this.context = this;
		uiInit();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnbpContinous) {
			
		} else if (v.getId() == R.id.btnbpPause) {
			
		}
	}
	
	protected void uiInit(){
		
	}

	
	
}
