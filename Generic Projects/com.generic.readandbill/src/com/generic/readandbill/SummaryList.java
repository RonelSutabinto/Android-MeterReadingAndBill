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

public class SummaryList extends ListActivity {

	
	protected static final String TAG = "SummaryList";
	protected ConsumerDataSource dsc;
	protected ReadingDataSource dsr;
	protected ConsumerArrayAdapter adapter;
	protected List<Consumer> values;

	protected EditText search;
	protected TextView totalRead;
	protected TextView totalUnread;
	protected TextView totalConsumer;

	protected int sortMode = SORT_NOTHING;

	protected static final int SORT_NOTHING = 5;
	protected static final int SORT_ACCOUNT_NUMBER = 10;
	protected static final int SORT_SEQUENCE = 20;
	protected static final int SORT_NAME = 30;
	protected static final int SORT_METER_SERIAL = 40;
	//protected static final int REQUEST_CODE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		this.getListView().setDividerHeight(2);

		search = (EditText) findViewById(R.id.etslSearch);
		totalRead = (TextView) findViewById(R.id.tvslTotalRead);
		totalUnread = (TextView) findViewById(R.id.tvslTotalUnread);
		totalConsumer = (TextView) findViewById(R.id.tvslTotalRecord);

		dsc = new ConsumerDataSource(this);
		dsr = new ReadingDataSource(this);

		search.addTextChangedListener(searchTextWatcher());
	}

    protected TextWatcher searchTextWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (adapter != null) {
                    if (count < before) {
                        adapter.resetData();
                    }
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
	protected void onResume() {
		super.onResume();

		Integer totalConsumer = dsc.getNumberOfConsumer();
		Integer totalRead = dsr.getTotalRead();
		Integer totalUnread = totalConsumer - totalRead;

        values = dsc.getAllUnReadConsumer();

		this.totalRead.setText(totalRead.toString());
		this.totalUnread.setText(totalUnread.toString());
		this.totalConsumer.setText(totalConsumer.toString());

		if (adapter == null) {
			adapter = new ConsumerArrayAdapter(this, values);
			setListAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}

		if (sortMode == SORT_NOTHING) {
			sortMode = SORT_ACCOUNT_NUMBER;
			if (adapter != null)
                applySort();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.iclSortByAccountNumber) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_ACCOUNT_NUMBER;
				applySort();
			}
		} else if (item.getItemId() == R.id.iclSortByName) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_NAME;
				applySort();
			}
		} else if (item.getItemId() == R.id.iclSortByMeterSerial) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_METER_SERIAL;
				applySort();
			}
		} else {
            return super.onOptionsItemSelected(item);
        }
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_consumer_list, menu);
		return true;
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	//=======================================================================
	//=======================================================================
	/*protected Intent myTransactionActivityCaller(Reading reading) {
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//Consumer consumer = adapter.getItem(position);
		//Reading reading = dsr.getReading(consumer.getId(),
		//		ReadingDataSource.CONSUMER_ID);
		//Intent intent = myTransactionActivityCaller(reading);
		//intent.putExtra("id", adapter.getItemId(position));
		//intent.putExtra("pos", position);
		Intent intent = new Intent(this, MyReadingEntry.class);
		startActivityForResult(intent, REQUEST_CODE);
	}
	*/
	
/*
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
 * */
}
