package com.generic.notice48hrs;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.generic.notice48hrs.database.Consumer;
import com.generic.notice48hrs.database.ConsumerDataSource;

import java.util.Comparator;
import java.util.List;

public class SummaryList extends ListActivity{

	protected static final String TAG = "SummaryList";
	protected ConsumerDataSource dsc;
	protected ConsumerArrayAdapter adapter;
	protected List<Consumer> values;

	protected EditText search;
	protected TextView totalServed;
	protected TextView totalUnserved;
	protected TextView totalConsumer;
    protected Integer vTotalConsumer;
    protected Integer vTotalServed;

    protected int sortMode = SORT_NOTHING;

	protected static final int SORT_NOTHING = 5;
	protected static final int SORT_ACCOUNTNUMBER = 10;
	protected static final int SORT_SEQUENCE = 20;
	protected static final int SORT_NAME = 30;
	protected static final int SORT_METER_SERIAL = 40;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		this.getListView().setDividerHeight(2);

		search = (EditText) findViewById(R.id.etslSearch);
		totalServed = (TextView) findViewById(R.id.tvslTotalRead);
		totalUnserved = (TextView) findViewById(R.id.tvslTotalUnread);
		totalConsumer = (TextView) findViewById(R.id.tvslTotalRecord);
	}

    @Override
    protected void onResume() {
        super.onResume();
        initializeObjects();

        Integer totalUnserved = vTotalConsumer - vTotalServed;

        this.totalServed.setText(vTotalServed.toString());
        this.totalUnserved.setText(totalUnserved.toString());
        this.totalConsumer.setText(vTotalConsumer.toString());

        if (sortMode == SORT_NOTHING) {
            sortMode = SORT_ACCOUNTNUMBER;
            applySort();
        }
    }

    protected void initializeObjects() {
        dsc = new ConsumerDataSource(this);
        values = dsc.getAllServedConsumer();

        vTotalConsumer = dsc.getNumberOfConsumer();
        vTotalServed = values.size();

        if (adapter == null) {
            adapter = new ConsumerArrayAdapter(this, values);
            setListAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        search.addTextChangedListener(new TextWatcher() {

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
        });
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.iclSortByAccountNumber) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_ACCOUNTNUMBER;
				applySort();
			}
		} else if (item.getItemId() == R.id.iclSortBySequence) {
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_SEQUENCE;
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
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_consumer_list, menu);
		if (!menu.getItem(4).isVisible()) {
			menu.getItem(4).setVisible(true);
		}
		return true;
	}

	protected void applySort() {
		switch (sortMode) {
		case SORT_ACCOUNTNUMBER:
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
		/*case SORT_METER_SERIAL:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getMeterSerial().compareTo(rhs.getMeterSerial());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.METER_SERIAL);
			break;     */
		}
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



}
