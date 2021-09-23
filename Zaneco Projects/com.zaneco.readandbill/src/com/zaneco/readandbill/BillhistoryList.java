package com.zaneco.readandbill;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

//import com.sun.org.apache.xerces.internal.jaxp.validation.ErrorHandlerAdaptor;
import com.zaneco.readandbill.database.BillhistoryDataSource;
import com.zaneco.readandbill.database.Billhistory;
import com.zaneco.readandbill.database.ReadandBillDatabaseHelper;

//public class BillhistoryList extends ListActivity {

public class BillhistoryList extends Activity {
	TextView TVname_bh;
    ListView myList;
    TextView totalRec;
    Cursor cursor;    
    private BillhistoryDataSource dsBillhistory;   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_history);
		
		myList = (ListView) findViewById(R.id.listView_bh);
		TVname_bh =(TextView) findViewById(R.id.textView_PrevRecord);
		dsBillhistory = new BillhistoryDataSource(this);
		
		dsBillhistory.open();		
		
		populateBillhistoryListView();	
	}
    		
	private void populateBillhistoryListView(){ 
		totalRec = (TextView) findViewById(R.id.tvTotalRecord);
		cursor = dsBillhistory.getAllrows();  	    	 
	     
		int countRecords = cursor.getCount();
		totalRec.setText(" :"+Integer.toString(countRecords));
		
	    String[] fromFieldName = new String[]{dsBillhistory.ACCOUNTNUMBER,
	    			                          dsBillhistory.NAME,	    			                              
	    			                          "counts",
	    			                          dsBillhistory.DIFF};
	   
	    int[] toViewsID = new int[] {R.id.tvaccountnumber_bh,
	    			           R.id.tvname_bh,
	    			           R.id.tvcount_bh,
	    			           R.id.tvavrg_bh};
		try{    		   
		    SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,
		    			                                 R.layout.row_billhistory,
		    			                                 cursor,
		    			                                 fromFieldName,
		    			                                 toViewsID);    
		    //myCursorAdapter.notifyDataSetChanged();
		    myList.setAdapter(myCursorAdapter);	
		}catch(Exception e){	
			//Log.v("Error:",e.getMessage());
			//AlertDialog Msg = new AlertDialog.Builder(this).create();
			//Msg.setTitle(e.getMessage());
			//Msg.show();
			
		}
	}	
	
}
