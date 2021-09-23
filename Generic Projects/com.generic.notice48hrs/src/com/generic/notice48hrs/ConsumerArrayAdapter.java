package com.generic.notice48hrs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.generic.notice48hrs.database.Consumer;

import java.util.ArrayList;
import java.util.List;

//import android.content.Intent;
//import android.view.View.OnClickListener;

public class ConsumerArrayAdapter extends ArrayAdapter<Consumer> implements
		Filterable {

	private final Activity context;
	
	private List<Consumer> consumers;
	private List<Consumer> origList;

	private Filter consumerFilter;

	private int filterMode;

	public final static int ACCOUNT_NUMBER = 10;
	public final static int NAME = 20;
	public final static int METER_SERIAL = 30;

	// private LayoutInflater mInflater;

	public ConsumerArrayAdapter(Activity context, List<Consumer> list) {
		super(context, R.layout.row_consumer_list, list);
		this.context = context;
		this.consumers = list;
		this.origList = list;
		// mInflater = LayoutInflater.from(context);
	}

	static class ViewHolder {
		protected TextView acctNumber;
		protected TextView name;
		protected TextView address;
		protected TextView meterSerial;
		protected TextView isProcess;
	}

	public int getCount() {
		return consumers.size();
	}

	public Consumer getItem(int position) {
		return consumers.get(position);
	}

	public long getItemId(int position) {
		return consumers.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Consumer c = consumers.get(position);
		View view = convertView;
		
		ViewHolder holder  = new ViewHolder();
		
		if (convertView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.row_consumer_list, null);
			
			TextView tvAcctNum = (TextView) view.findViewById(R.id.tvAccountNumber);
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
			TextView tvMetSer = (TextView) view.findViewById(R.id.tvMeterSerial);
			TextView tvIsProcess = (TextView) view.findViewById(R.id.tvIsServed);

			holder.acctNumber = tvAcctNum;
			holder.name = tvName;
			holder.address = tvAddress;
			holder.meterSerial = tvMetSer;
			holder.isProcess = tvIsProcess;
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.acctNumber.setText(c.getAccountNumber());
		holder.name.setText(c.getName());
		holder.address.setText(c.getAddress());
		
		//holder.meterSerial.setText(c.getMeterSerial());
		
		if (c.isServed()) {
			holder.isProcess.setText("Y");
		} else {
			holder.isProcess.setText("N");
		}
		return view;
	}

	public int getFilterMode() {
		return filterMode;
	}

	public void setFilterMode(int filterMode) {
		this.filterMode = filterMode;
	}

	public void resetData() {
		consumers = origList;
	}

	@Override
	public Filter getFilter() {
		if (consumerFilter == null) {
			consumerFilter = new ConsumerFilter();
		}
		return consumerFilter;
	}

	private class ConsumerFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults myResults = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				myResults.values = origList;
				myResults.count = origList.size();
			} else {
				List<Consumer> filteredList = new ArrayList<Consumer>();
				boolean addIt = false;
				for (Consumer consumer : consumers) {
					switch (filterMode) {
					case ACCOUNT_NUMBER:
						addIt = consumer.getAccountNumber().startsWith(
								constraint.toString());
						break;
					case NAME:
						addIt = consumer.getName().toUpperCase()
								.indexOf(constraint.toString().toUpperCase()) > -1;
						break;
					/*case METER_SERIAL:
						addIt = consumer.getMeterSerial().startsWith(
								constraint.toString());
						break;        */
					}
					if (addIt) {
						filteredList.add(consumer);
					}
				}
				myResults.values = filteredList;
				myResults.count = filteredList.size();
			}

			return myResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count == 0) {
				notifyDataSetInvalidated();
			} else {
				consumers = (List<Consumer>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	/*
	 * @Override public Filter getFilter() { return new Filter() {
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override protected void publishResults(CharSequence constraint,
	 * FilterResults results) { if (results.count == 0){
	 * notifyDataSetInvalidated(); } else { list = (List<Consumer>)
	 * results.values; notifyDataSetChanged(); } }
	 * 
	 * @Override protected FilterResults performFiltering(CharSequence
	 * constraint) {
	 * 
	 * } }; }
	 */

}
