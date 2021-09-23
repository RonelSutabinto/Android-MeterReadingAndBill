package com.generic.readandbill;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.generic.readandbill.database.Consumer;
import com.generic.readandbill.database.ConsumerDataSource;
import com.generic.readandbill.database.Reading;
import com.generic.readandbill.database.ReadingDataSource;

import java.util.Comparator;
import java.util.List;

public class MyConsumerList extends ListActivity {

	protected ReadingDataSource dsr;
	protected ConsumerArrayAdapter adapter;
	protected List<Consumer> values;
	protected ConsumerDataSource dsc;
    protected TextView totalConsumer;
	private EditText search;

	private int sortMode;

	private static final int SORT_ACCOUNT_NUMBER = 10;
	private static final int SORT_SEQUENCE = 20;
	private static final int SORT_NAME = 30;
	private static final int SORT_METER_SERIAL = 40;

	protected static final int REQUEST_CODE = 10;
	protected static final int FIELDFINDING_ID = Menu.FIRST + 1;
	protected static final int DELETE_ID = Menu.FIRST + 2;
	protected static final int VIEWALL = 90;
	protected static final int VIEWUNREAD = 95;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumer_list);
		this.getListView().setDividerHeight(2);
		dsc = new ConsumerDataSource(this);

        values = dsc.getAllConsumer();       
		
        totalConsumer = (TextView) findViewById(R.id.cltvTotalConsumer);
		search = (EditText) findViewById(R.id.etclSearch);

        totalConsumer.setText(dsc.getNumberOfConsumer().toString());
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count < before) {
					adapter.resetData();
				}
				adapter.getFilter().filter(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        initialization();
	}

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetInvalidated();
    }

    public void initialization() {
		dsr = new ReadingDataSource(this);

		adapter = new ConsumerArrayAdapter(this, values);
		setListAdapter(adapter);
		sortMode = SORT_ACCOUNT_NUMBER;
		applySort();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.iclSortByAccountNumber) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_ACCOUNT_NUMBER;				
				//values = dsc.getAllUnReadConsumer();
				//adapter = new ConsumerArrayAdapter(this, values);
				applySort();
				return true;
			}
		} else if (item.getItemId() == R.id.iclSortBySequence) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_SEQUENCE;
				applySort();
				return true;
			}
		} else if (item.getItemId() == R.id.iclSortByName) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_NAME;
				applySort();
				return true;
			}
		} else if (item.getItemId() == R.id.iclSortByMeterSerial) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_METER_SERIAL;
				applySort();
				return true;
			}
		}else if(item.getItemId() == R.id.iclViewUnread){
			if(!item.isChecked()){
				item.setChecked(true);								
				
				sortMode = SORT_ACCOUNT_NUMBER;
				
				values = dsc.getAllUnReadConsumer();
				adapter = new ConsumerArrayAdapter(this, values);
				applySort();
				return true;
			}		
		}else if(item.getItemId() == R.id.iclViewAll){
			if(!item.isChecked()){
				item.setChecked(true);
				sortMode = SORT_ACCOUNT_NUMBER;
				
				values = dsc.getAllConsumer();
				adapter = new ConsumerArrayAdapter(this, values);
				applySort();				
				return true;
			}
		}
		
			
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_consumer_list, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Consumer consumer = adapter.getItem(position);
		Reading reading = dsr.getReading(consumer.getId(),
				ReadingDataSource.CONSUMER_ID);
		Intent intent = myTransactionActivityCaller(reading);
		intent.putExtra("id", adapter.getItemId(position));
		intent.putExtra("pos", position);
		startActivityForResult(intent, REQUEST_CODE);
	}

	protected Intent myTransactionActivityCaller(Reading reading) {
		Intent intent = null;
		if (reading.getId() != -1) {
			if (reading.getFieldFinding() != 0) {
				intent = new Intent(this, MyFieldFindingEntry.class);
			} else {
				intent = new Intent(this, MyReadingEntry.class);
			}
		} else {
			intent = new Intent(this, MyReadingEntry.class);
		}
		return intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			/*Consumer consumer;
			consumer = adapter.getItem(data.getExtras().getInt("pos"));
			adapter.remove(adapter.getItem(data.getExtras().getInt("pos")));
			adapter.insert(consumer, data.getExtras().getInt("pos"));*/
			applySort();
            getListView().setSelection(data.getExtras().getInt("pos"));
		}
	}

	protected void applySort() {
		switch (sortMode) {
		case SORT_ACCOUNT_NUMBER:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getAccountNumber().compareTo(
							rhs.getAccountNumber());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.ACCOUNT_NUMBER);
			break;
		case SORT_SEQUENCE:
			break;
		case SORT_NAME:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.NAME);
			break;
		case SORT_METER_SERIAL:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getMeterSerial().compareTo(rhs.getMeterSerial());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.METER_SERIAL);
			break;
		}
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();		
		
	}
}
