package com.zaneco.readandbill.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.generic.readandbill.database.ReadandBillDatabaseHelper;

public class HighPrevDataSource {
	public static final String ACCOUNTNUMBER = "accountnumber";
	public static final String NAME = "name";
	public static final String PRESENTREADING_KWH = "presentreadingKWH";
	public static final String PREVIOUSREADING_KWH = "previousreadingKWH";
	public static final String PRESENTREADING_DATE = "presentreadingDate";
	public static final String PREVIOUSREADING_DATE = "previousreadingDate";
	public static final String DIFF = "diff";	
	public static final String MULTIPLIER = "multiplier";
	public static final String KWH = "kwh";
	public static final String DEMAND = "demand";
	public static final String TOTALBILL = "totalbill";
	public static final String BILLMONTH = "billmonth";
	public static final String WRATECODE = "wratecode";
	public static final String READINGTYPE = "readingtype";
	public static final String ID = "_id";
	public static final String TABLE_BILLHISTORY = "hv_billhistory";
	
	private Context ourcontext;
	protected SQLiteDatabase db;
	private ReadandBillDatabaseHelper dbhelper;
	private String[] AllColumns = {ID,ACCOUNTNUMBER,NAME,PRESENTREADING_KWH,PREVIOUSREADING_KWH,
			                       PRESENTREADING_DATE,PREVIOUSREADING_DATE,DIFF,MULTIPLIER,
			                       KWH,DEMAND,TOTALBILL,BILLMONTH,WRATECODE,READINGTYPE};
	public String[] selectColumns = {ID,ACCOUNTNUMBER,NAME,PRESENTREADING_KWH,PREVIOUSREADING_KWH,
                                     PRESENTREADING_DATE,PREVIOUSREADING_DATE,DIFF,MULTIPLIER,
                                     KWH,DEMAND,TOTALBILL,BILLMONTH,WRATECODE};
	public HighPrevDataSource(Context context){
		this.ourcontext = context;
	}
	
	public HighPrevDataSource open() throws SQLException{
		dbhelper = new ReadandBillDatabaseHelper(ourcontext);
		db = dbhelper.getWritableDatabase();
		return this;
	}
	public void close(){
		dbhelper.close();
	}
	
	public static List<String> HVbillhistoryFields(){
		List<String> billhistoryFields = new ArrayList<String>();
		billhistoryFields.add(ID+"integer primary key autoincrement,");
		billhistoryFields.add(ACCOUNTNUMBER + " text not null, ");
		billhistoryFields.add(NAME+" text not null, ");
		billhistoryFields.add(PRESENTREADING_KWH+" real not null,");
		billhistoryFields.add(PREVIOUSREADING_KWH+" real not null,");
		billhistoryFields.add(PRESENTREADING_DATE+ "text not null,");
		billhistoryFields.add(PREVIOUSREADING_DATE+"text not null,");
		billhistoryFields.add(DIFF+" real not null,");
		billhistoryFields.add(MULTIPLIER+" real not null,");
		billhistoryFields.add(KWH+" real not null,");
		billhistoryFields.add(DEMAND+" real not null,");
		billhistoryFields.add(TOTALBILL+" real not null,");
		billhistoryFields.add(BILLMONTH+" text not null,");
		billhistoryFields.add(WRATECODE+" text not real,");
		billhistoryFields.add(READINGTYPE+" text not null ");
		return billhistoryFields;
	}
	
	public static final String DATABASE_CREATE(){
		String result ="create table "+TABLE_BILLHISTORY+"(";
		
		for(String s : HVbillhistoryFields()){
			result +=s;
		}
		
		result +=");";
		return result;
		
	}
	
	public void refreshBillhistory(){
		db = dbhelper.getWritableDatabase();
		db.execSQL(DATABASE_CREATE());
		db.close();
	}
	
	public Cursor getAllrows(){
		Cursor c = db.rawQuery("select * from hv_billhistory", null);
		
		if(c!=null)
			c.moveToFirst();
		return c;
	}
	
	public long createHVbillhistory(String accountnumber,String name,double presentreading_kwh,double previousreading_kwh,String presentreading_date,String previousreading_date,double diff,double multiplier,double kwh,double demand,double totalbill,String billmonth,String wratecode,String readingtype){
		ContentValues value = new ContentValues();
		value.put(ACCOUNTNUMBER, accountnumber);
		value.put(NAME, name);
		value.put(PRESENTREADING_KWH, presentreading_kwh);
		value.put(PREVIOUSREADING_KWH, previousreading_kwh);
		value.put(PRESENTREADING_DATE,presentreading_date);
		value.put(PREVIOUSREADING_DATE, previousreading_date);
		value.put(DIFF, diff);
		value.put(MULTIPLIER, multiplier);
		value.put(KWH,kwh);
		value.put(DEMAND, demand);
		value.put(TOTALBILL, totalbill);
		value.put(BILLMONTH, billmonth);
		value.put(WRATECODE, wratecode);
		value.put(READINGTYPE, readingtype);
		return db.insert(TABLE_BILLHISTORY, null, value);
	}
	/*
	 
	
	public string getData(){
		Cursor c = db.query(TABLE_BILLHISTORY, selectColumns, null, null, null, null, null);
		String result = "";
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			result = result 
		}
	}
		    
			
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
	

	*/
}
