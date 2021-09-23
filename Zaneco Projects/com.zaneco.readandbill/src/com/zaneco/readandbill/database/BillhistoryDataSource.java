package com.zaneco.readandbill.database;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.androidapp.mytools.objectmanager.ArrayManager;
import com.generic.readandbill.database.ReadandBillDatabaseHelper;
import com.generic.readandbill.database.UnreadAccounts;
//import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.ArrayList;
import java.util.List;
public class BillhistoryDataSource {
	
	public static final String CODE = "code1";
	public static final String ACCOUNTNUMBER = "accountnumber";
	public static final String NAME = "name";
	public static final String PRESENTREADING = "presentreading";
	public static final String PREVIOUSREADING = "previousreading";
	public static final String DIFF = "diff";
	public static final String BILLMONTH = "billmonth";
	public static final String TOTALBILL = "totalbill";
	public static final String TABLE_BILLHISTORY="billhistory";
	public static final String ID = "_id";
	
	
	private Context ourcontext;
	protected SQLiteDatabase db;
	private ReadandBillDatabaseHelper dbhelper;
	private String[] AllColumns = {ID,CODE,ACCOUNTNUMBER,NAME,PRESENTREADING,PREVIOUSREADING,DIFF,BILLMONTH,TOTALBILL};
	public String[] selectColumns= {ID,ACCOUNTNUMBER,
			                         NAME,
			                         PRESENTREADING,
			                         PREVIOUSREADING,
			                         DIFF,			                         
			                         BILLMONTH,
			                         TOTALBILL};
	
	public BillhistoryDataSource(Context context){
		this.ourcontext = context;
		
		//dbhelper = new ReadandBillDatabaseHelper(ourcontext);
		//AllColumns = ArrayManager.concat(AllColumns, second)
	}
	
	public BillhistoryDataSource open() throws SQLException{
		 dbhelper = new ReadandBillDatabaseHelper(ourcontext);
		 db = dbhelper.getWritableDatabase();		  	  
		 return this;
	}
	public void close() {
	     dbhelper.close();
	}  
	

	public static List<String> BillhistoryFields(){
		// List<String> consumerFields = new ArrayList<String>();
		List<String> billhistory_fields = new ArrayList<String>();
		billhistory_fields.add(ID + " integer primary key autoincrement, ");
		billhistory_fields.add(CODE + " integer not null, ");
		billhistory_fields.add(ACCOUNTNUMBER+" text not null, ");
		billhistory_fields.add(NAME+" text not null, ");
		billhistory_fields.add(PRESENTREADING+" real not null, ");
		billhistory_fields.add(PREVIOUSREADING+" real not null, ");
		billhistory_fields.add(DIFF+" real not null, ");		
		billhistory_fields.add(BILLMONTH+" text not null, ");
		billhistory_fields.add(TOTALBILL+" real not null");
		
		return billhistory_fields;		
	}	
	
	public static final String DATABASE_CREATE(){
		String result ="create table "+TABLE_BILLHISTORY +"(";
		
		for(String s : BillhistoryFields()){
			result += s;
		}	
		result += ");";
		return result;
	}
	
	public void refreshBillhistory(){
		db = dbhelper.getWritableDatabase();
		db.execSQL("drop table if exists "+TABLE_BILLHISTORY); 
		db.execSQL(DATABASE_CREATE());
		db.close();
	}	
	
	
	public Cursor getAllrows(){
		String where = null;
		//db = dbhelper.getWritableDatabase();
		/*Cursor c = db.rawQuery("Select * from (Select _id," +
				                               "accountnumber,name," +
				                               "count(*) as counts," +
				                               "round(sum(ifnull(diff,0))/3,1) " +
				                               "as diff from " +
		                                        TABLE_BILLHISTORY +" group by code1" +
		                                       ") b where b.counts =" + 3,null );*/
		Cursor c = db.rawQuery("Select _id,"+
		                              "accountnumber,"+
				                      "name,"+
		                              "0 as counts,round(diff,1) as diff from "+TABLE_BILLHISTORY,null);
		if (c!=null){
			c.moveToFirst();			
		}		
		return c;
	}
	
	public Cursor getAverageRdng(String accnt){	
		/*Cursor c = db.rawQuery("Select _id,accountnumber,name,counts,diff from (Select _id," +
                "accountnumber,name," +
                "count(*) as counts," +
                "round(sum(ifnull(diff,0))/3,1) " +
                "as diff from " +
                 TABLE_BILLHISTORY +" group by code1" +
                ") b where b.accountnumber = ? and b.counts =" + 3,new String[]{accnt} );*/
		Cursor c = db.rawQuery("Select _id,accountnumber,name,diff from " +
                 TABLE_BILLHISTORY +
                 " b where b.accountnumber = ?",new String[]{accnt} );
		if (c!=null){
		c.moveToFirst();			
		}		
		return c;
	}
	/*
	 public Cursor getAllrows(){
		String where = null;
		//db = dbhelper.getWritableDatabase();
		Cursor c = db.query(TABLE_BILLHISTORY, selectColumns, where,null,null, null, null);
		if (c!=null){
			c.moveToFirst();			
		}		
		return c;
	}
	 */
	public String getData(){
		Cursor c = db.query(TABLE_BILLHISTORY, selectColumns, null,null,null, null, null);
		
		String result ="";
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			result = result+ c.getString(c.getColumnIndex(ACCOUNTNUMBER))+
					c.getString(c.getColumnIndex(NAME))+
					Double.toString(c.getDouble(c.getColumnIndex(PRESENTREADING)))+
					Double.toString(c.getDouble(c.getColumnIndex(PREVIOUSREADING)))+
					Double.toString(c.getDouble(c.getColumnIndex(DIFF)))+
					c.getString(c.getColumnIndex(BILLMONTH))+
					Double.toString(c.getDouble(c.getColumnIndex(TOTALBILL)))+
					"\n";
		}
		return result;
	}
	/*
	 public String geData() {
		// TODO Auto-generated method stub
		String columns[] = new String[]{ROWID,NAME,NUMBER,RELATIVE};
		Cursor c =ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		String result = "";
		int iROW = c.getColumnIndex(ROWID);
		int iNAME = c.getColumnIndex(NAME);
		int iNUMBER = c.getColumnIndex(NUMBER);
		int iRELATIVE = c.getColumnIndex(RELATIVE);
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			result =  result + leftJustify(c.getString(iROW),4) + leftJustify(c.getString(iNAME),25) +"  "+ c.getString(iNUMBER)+"  "+c.getString(iRELATIVE)+"\n";			
		}
		return result;
	}
	 */
	public long createBillhistory(long codeBH,String accountnumberBH,String nameBH, double presentreadingBH,double previousreadingBH,double diffBH,String billmonthBH,double totalBH){
		ContentValues value = new ContentValues();
		value.put(CODE, codeBH);
		value.put(ACCOUNTNUMBER, accountnumberBH);
		value.put(NAME,nameBH);
		value.put(PRESENTREADING, presentreadingBH);
		value.put(PREVIOUSREADING, previousreadingBH);
		value.put(DIFF, diffBH);
		value.put(BILLMONTH, billmonthBH);
		value.put(TOTALBILL, totalBH);
		
		return db.insert(TABLE_BILLHISTORY, null, value);
	}
	  
	
/*
	public Billhistory createBillhistory(Billhistory billhistory){
		db = dbhelper.getWritableDatabase();
		long insertID = db.insert(TABLE_BILLHISTORY, null,
				        BillhistoryContentValues(billhistory));
		
		Cursor cursor = db.query(TABLE_BILLHISTORY, AllColumns, ID+"="+insertID,
			        	null,null,null,null);
		cursor.moveToFirst();
		Billhistory newbillhistory = cursorToBillhistory(cursor);
		cursor.close();
		db.close();
		return newbillhistory;
	}*/
	/*
	protected Billhistory cursorToBillhistory(Cursor cursor){
		Billhistory billhistory = new Billhistory();
		
		//if(cursor.getCount() != 0){
			billhistory.setCode(cursor.getLong(cursor.getColumnIndex(CODE)));
			billhistory.setAccountnumber(cursor.getString(cursor.getColumnIndex(ACCOUNTNUMBER)));
			billhistory.setName(cursor.getString(cursor.getColumnIndex(NAME)));
			billhistory.setPresentreading(cursor.getDouble(cursor.getColumnIndex(PRESENTREADING)));
			billhistory.setPreviousreading(cursor.getDouble(cursor.getColumnIndex(PREVIOUSREADING)));
			billhistory.setDiff(cursor.getDouble(cursor.getColumnIndex(DIFF)));
			billhistory.setBillmonth(cursor.getString(cursor.getColumnIndex(BILLMONTH)));
			billhistory.setTotalbill(cursor.getDouble(cursor.getColumnIndex(TOTALBILL))); 
		//}
		return billhistory;
	}*/

}
