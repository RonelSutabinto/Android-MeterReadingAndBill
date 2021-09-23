package com.zaneco.readandbill;

import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.zaneco.readandbill.database.NewConnection;
import com.zaneco.readandbill.database.NewConnectionDataSource;

public class NewConsumerList extends ListActivity{
	protected NewConnectionDataSource dsnc;
	protected List<NewConnection> values;
	protected EditText search;
	protected TextView totalNewCon;
	private NewConsumerArrayAdapter adapter;
	
	protected int sortMode;
	
	protected static final int SORT_NAME = 10;
	protected static final int SORT_SERIAL = 20;
	
	protected static final int REQUEST_ENTRY = 30;
	protected static final int ADD_ID = Menu.FIRST + 1;
	protected static final int DELETE_ID = Menu.FIRST + 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_newconsumer_list);
		this.getListView().setDividerHeight(2);
		search = (EditText) findViewById(R.id.etnceSearch);
		totalNewCon = (TextView) findViewById(R.id.tvncTotalRecord);
		dsnc = new NewConnectionDataSource(this);
		values = dsnc.getAllNewCon();
		adapter = new NewConsumerArrayAdapter(this, values);
		setListAdapter(adapter);
		sortMode = SORT_NAME;
		applySort();
		registerForContextMenu(getListView());
		totalNewCon.setText(String.valueOf(values.size())); 
		search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count < before){
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_newconsumer_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.inclSortName){
			if (!item.isChecked()){
				item.setChecked(true);
				sortMode = SORT_NAME;
				applySort();
				return true;
			}
		} else if (item.getItemId() == R.id.inclSortSerial){
			if (!item.isChecked()) {
				item.setChecked(true);
				sortMode = SORT_SERIAL;
				applySort();
				return true;
			}
		} else if (item.getItemId() == R.id.inclAddNewCon){
			Intent intent = new Intent(this, NewConsumerEntry.class);
			startActivityForResult(intent, REQUEST_ENTRY);
			return true;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, ADD_ID, 0, R.string.menu_add);
		if (values.size() != 0){
			menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, NewConsumerEntry.class);
		intent.putExtra("id", adapter.getItemId(position));
		intent.putExtra("pos", position);
		startActivityForResult(intent, REQUEST_ENTRY);
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case ADD_ID:
			Intent intent = new Intent(this, NewConsumerEntry.class);
			startActivityForResult(intent, REQUEST_ENTRY);
			return true;
		case DELETE_ID:
			NewConnection newCon = dsnc.getNewCon(adapter.getItemId(info.position));
			dsnc.deleteNewCon(newCon);
			adapter.remove(adapter.getItem(info.position));
			applySort();
			return true;
		default:
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_ENTRY){
			boolean isUpdate = false;
			NewConnection newCon;
			newCon = dsnc.getNewCon(data.getExtras().getLong("id"));
			isUpdate = data.getExtras().getInt("pos") != 0;
			if (isUpdate) {
				adapter.remove(adapter.getItem(data.getExtras().getInt("pos")));
				adapter.insert(newCon, data.getExtras().getInt("pos"));
			} else {
				adapter.add(newCon);
				totalNewCon.setText(String.valueOf(values.size()));
			}
			adapter.notifyDataSetChanged();
			applySort();
		}
	}

	private void applySort() {
		boolean notifychange = false;
		switch (sortMode) {
		case SORT_NAME:
			adapter.sort(new Comparator<NewConnection>() {
				@Override
				public int compare(NewConnection lhs, NewConnection rhs){
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			adapter.setFilterMode(NewConsumerArrayAdapter.NAME);
			notifychange = true;
			break;
		case SORT_SERIAL:
			adapter.sort(new Comparator<NewConnection>() {
				@Override
				public int compare(NewConnection lhs, NewConnection rhs){
					return lhs.getSerial().compareTo(rhs.getSerial());
				}
			});
			adapter.setFilterMode(NewConsumerArrayAdapter.SERIAL);
			notifychange = true;
			break;
		default:
			break;
		}
		if (notifychange){
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	
	
}
