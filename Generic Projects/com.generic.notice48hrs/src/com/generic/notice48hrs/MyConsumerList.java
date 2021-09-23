package com.generic.notice48hrs;

import android.app.ListActivity;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.androidapp.mytools.bluetooth.BluetoothSharedPrefs;
import com.generic.notice48hrs.database.Consumer;
import com.generic.notice48hrs.database.ConsumerDataSource;
import com.generic.notice48hrs.database.UnpaidDataSource;
import com.generic.notice48hrs.database.UserProfileDataSource;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class MyConsumerList extends ListActivity{

	protected ConsumerDataSource dsc;
	protected UnpaidDataSource dsub;
	protected UserProfileDataSource dsup;
	protected ConsumerArrayAdapter adapter;

	protected List<Consumer> values;
	protected EditText search;

	protected int sortMode;

	protected static final int SORT_ACCOUNT_NUMBER = 10;
	protected static final int SORT_NAME = 20;

	protected static final int REQUEST_CODE = 10;
	protected static final int FIELDFINDING_ID = Menu.FIRST + 1;
	protected static final int DELETE_ID = Menu.FIRST + 2;

	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_PREFERENCE = 2;

	protected int listPos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumer_list);
		this.getListView().setDividerHeight(2);

		search = (EditText) findViewById(R.id.etclSearch);

		initializeObject();

        sortMode = SORT_ACCOUNT_NUMBER;
        applySort();
        registerForContextMenu(getListView());

	}

    protected void initializeObject(){
        dsc = new ConsumerDataSource(this);
        dsub = new UnpaidDataSource(this);
        dsup = new UserProfileDataSource(this);

        values = dsc.getAllConsumer();

        adapter = new ConsumerArrayAdapter(this, values);
        setListAdapter(adapter);


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
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.iclSortByAccountNumber) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_ACCOUNT_NUMBER;
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
		} /*else if (item.getItemId() == R.id.iclSortByMeterSerial) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_METER_SERIAL;
				applySort();
				return true;
			}
		}    */
		return false;
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
		case SORT_NAME:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.NAME);
			break;
/*        case SORT_METER_SERIAL:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getMeterSerial().compareTo(rhs.getMeterSerial());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.METER_SERIAL);
			break;*/
		}
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	protected String billmonthStr(String billmonth) {
		return billmonth.substring(0, 2) + " 20" + billmonth.substring(2, 4);
	}


	protected String getDefaultMacAddress() {
		String result = "";
		result = BluetoothSharedPrefs.getMacAddress(this);
		return result;
	}

	private void closeSocket(BluetoothSocket bts) {
		try {
			bts.close();
			Log.d(this.getClass().getName(), "Socket closed");
		} catch (IOException e) {
			Log.d(this.getClass().getName(), "Could not close socket!");
		}
	}

}
