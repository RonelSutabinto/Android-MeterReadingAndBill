package com.zaneco.readandbill;

import java.util.List;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import com.zaneco.readandbill.database.ComputeCharges;
import com.zaneco.readandbill.database.Consumer;
import com.zaneco.readandbill.database.ConsumerDataSource;

public class BatchPrintingSOA extends com.generic.readandbill.BatchPrintingSOA {

	private List<Consumer> consumers;
	private int position = 0;
	private ConsumerDataSource dsConsumer;
	
	protected boolean isFin;
	protected boolean pause = false;
	protected ProgressDialog btProgressDialog;

	@Override
	protected void uiInit() {
		super.uiInit();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnbpContinous || v.getId() == R.id.btnbpPause) {
			position = 0;
			pause = v.getId() == R.id.btnbpPause;
			consumers = dsConsumer.getReadConsumer(etAccountFrom.getText()
					.toString(), etAccountTo.getText().toString());
			printBatch();
		}
	}

	private void printBatch() {
		if (position < consumers.size()) {
			AlertDialog ad = printEvent();
			if (!pause) {
                ComputeCharges compute = new ComputeCharges(context, consumers.get(position));
				printSoa(compute);
			}
			if (position != 0 && pause && position < consumers.size()) {
				ad.show();
			}
			position += 1;
		}
	}
	
	private void printSoa(ComputeCharges compute){
		StatementGenerator soa = new StatementGenerator(this, compute);
		SplashScreen.btPrinter.print(soa.generateSOA());
	}

	private AlertDialog printEvent() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this)
				.setTitle("Done printing! Do you wish to proceed")
				.setMessage("Tap OK to continue printing").setCancelable(false)
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
                        ComputeCharges compute = new ComputeCharges(context, consumers.get(position));
						printSoa(compute);
					}
				}).setNegativeButton("NO", null);
		return adb.create();
	}

}