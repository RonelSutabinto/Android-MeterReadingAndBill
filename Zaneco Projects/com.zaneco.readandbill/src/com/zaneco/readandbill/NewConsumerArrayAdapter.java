package com.zaneco.readandbill;

import java.util.ArrayList;
import java.util.List;

import com.generic.readandbill.database.Consumer;
import com.generic.readandbill.database.Reading;
import com.generic.readandbill.database.ReadingDataSource;
import com.zaneco.readandbill.database.ComputeCharges;
import com.zaneco.readandbill.database.ConsumerDataSource;
import com.zaneco.readandbill.database.NewConnection;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class NewConsumerArrayAdapter extends ArrayAdapter<NewConnection> implements Filterable{

	private final Activity context;
	
	private List<NewConnection> newCons;
	private List<NewConnection> origList;	
	
	private Filter newConFilter;
	
	protected int filterMode;
	
	protected final static int NAME = 10;
	protected final static int SERIAL = 20;
	
	public NewConsumerArrayAdapter(Activity context, List<NewConnection> list) {
		super(context, R.layout.row_newcon_list);
		this.context = context;
		this.newCons = list;
		this.origList = list;
	}
	
	static class ViewHolder{
		protected TextView route;
		protected TextView name;
		protected TextView serial;
		protected TextView reading;		
		
	}
	
	public int getCount(){
		return newCons.size();
	}
	
	public NewConnection getItem(int position){
		return newCons.get(position);
	}
	
	public long getItemId(int position){
		return newCons.get(position).getId();
	}
	
	public int getFilterMode(){
		return filterMode;
	}
	
	public void setFilterMode(int filterMode){
		this.filterMode = filterMode;
	}
	
	public void resetData(){
		newCons = origList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		NewConnection nc = newCons.get(position);
		View view = convertView;
		
		ViewHolder holder = new ViewHolder();
		
		if (convertView == null){
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.row_newcon_list, null);
			
			TextView tvRoute = (TextView) view.findViewById(R.id.tvnclRoute);
			TextView tvName = (TextView) view.findViewById(R.id.tvnclName);
			TextView tvSerial = (TextView) view.findViewById(R.id.tvnclSerial);
			TextView tvReading = (TextView) view.findViewById(R.id.tvnclReading);			
			
			holder.route = tvRoute;
			holder.name = tvName;
			holder.serial = tvSerial;
			holder.reading = tvReading;			
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
								
		holder.route.setText(nc.getRoute());
		holder.name.setText(nc.getName());
		holder.serial.setText(nc.getSerial());
		holder.reading.setText(nc.getReading().toString());
		return view;
	}
	
	@Override
	public Filter getFilter() {
		if (newConFilter == null){
			newConFilter = new NewConsumerFilter();
		}
		return newConFilter;
	}
	
	private class NewConsumerFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults myResults = new FilterResults();
			if (constraint == null || constraint.length() == 0){
				myResults.values = origList;
				myResults.count = origList.size();
			} else {
				List<NewConnection> filteredList = new ArrayList<NewConnection>();
				boolean addIt = false;
				for (NewConnection newConnection : newCons) {
					switch (filterMode) {
					case NAME:
						addIt = newConnection.getName().startsWith(constraint.toString());
						break;
					case SERIAL:
						addIt = newConnection.getSerial().startsWith(constraint.toString());
						break;
					default:
						break;
					}
					if (addIt){
						filteredList.add(newConnection);
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
				newCons = (List<NewConnection>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
}
