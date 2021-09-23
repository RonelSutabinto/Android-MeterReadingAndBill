package com.generic.readandbill;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.generic.readandbill.database.Consumer;
import com.generic.readandbill.database.ConsumerDataSource;
import com.generic.readandbill.database.Reading;
import com.generic.readandbill.database.ReadingDataSource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import android.content.Intent;
//import android.view.View.OnClickListener;

public class ConsumerArrayAdapter extends ArrayAdapter<Consumer> implements
		Filterable {

	protected final Activity context;

	protected ReadingDataSource dsReading;
	protected ConsumerDataSource dsConsumer;

	private List<Consumer> consumers;
	private List<Consumer> origList;

	private Filter consumerFilter;

	private int filterMode;
	//protected Bundle extras;
	

    protected DecimalFormat kilowattFormatter = new DecimalFormat("##,###,###,##0.0");

	public final static int ACCOUNT_NUMBER = 10;
	public final static int SEQUENCE = 20;
	public final static int NAME = 30;
	public final static int METER_SERIAL = 40;

	// private LayoutInflater mInflater;

	public ConsumerArrayAdapter(Activity context, List<Consumer> list) {
		super(context, R.layout.row_consumer_list, list);
		this.context = context;
		this.consumers = list;
		this.origList = list;
		dsReading = new ReadingDataSource(context);
        dsConsumer = new ConsumerDataSource(context);
		// mInflater = LayoutInflater.from(context);
	}

	static class ViewHolder {
		protected TextView acctNumber;
		protected TextView name;
		protected TextView address;
		protected TextView polenumber;
		protected TextView meterSerial;
		protected TextView kilowatthour;
		protected TextView Status;
		protected TextView refAtm;	
	
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
		View view = null;		

		if (convertView == null) {
			
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.row_consumer_list, null);

			final ViewHolder holder = new ViewHolder();
			
			TextView tvAcctNum = (TextView) view
					.findViewById(R.id.tvAccountNumber);
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
			TextView tvMetSer = (TextView) view
					.findViewById(R.id.tvMeterSerial);
			TextView tvKWH = (TextView) view.findViewById(R.id.tvKilowatthour);
			TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
			//LinearLayout llRowLayout = (LinearLayout) view.findViewById(R.id.clLinerLayout);
			TextView tvPolenumber = (TextView) view.findViewById(R.id.tvPoleNumber);			

			holder.acctNumber = tvAcctNum;
			holder.name = tvName;
			holder.address = tvAddress;
			holder.polenumber = tvPolenumber;
			holder.meterSerial = tvMetSer;
			holder.kilowatthour = tvKWH;
			holder.Status = tvStatus;	
			//holder.llRowLayout = llRowLayout;
			
			view.setTag(holder);
		} else {
			view = convertView;
		}

		Consumer c = consumers.get(position);
		Reading r = dsReading.getReading(c.getId(),
				ReadingDataSource.CONSUMER_ID);
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		holder.acctNumber.setText(c.getAccountNumber());
		holder.name.setText(c.getName());
		holder.address.setText(c.getAddress());
		holder.polenumber.setText(c.getPoleNumber());
		holder.meterSerial.setText(c.getMeterSerial());
		
		if (r != null) {
			if (r.getId() != -1) {
				if (r.getFieldFinding() == 0 || r.getFieldFinding() == -1) {
					holder.kilowatthour.setText(kilowattFormatter.format(r.getKilowatthour()));
				} else {
					holder.kilowatthour.setText("FF");
				}
			} else {
				holder.kilowatthour.setText("0");
			}
		} else {
			holder.kilowatthour.setText("0");
		}
		
		int getConcode = c.getConnectionCode();
		if (getConcode == 1) {
			holder.Status.setText("CONN");
		}else if (getConcode == 4){
			holder.Status.setText("DISCO");
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
				List<Consumer> filteredList;
                filteredList = new ArrayList<Consumer>();
                boolean addIt = false;
				for (Consumer c : consumers) {
					switch (filterMode) {
					case ACCOUNT_NUMBER:
						addIt = c.getAccountNumber().startsWith(
								constraint.toString());
						break;
					case SEQUENCE:
						break;
					case NAME:
						addIt = c
								.getName()
								.toUpperCase(Locale.US)
								.indexOf(
										constraint.toString().trim().toUpperCase(
												Locale.US), 0) > -1;
						break;
					case METER_SERIAL:
						addIt = c.getMeterSerial().startsWith(
								constraint.toString());
						break;
					}
					if (addIt) {
						filteredList.add(c);
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
