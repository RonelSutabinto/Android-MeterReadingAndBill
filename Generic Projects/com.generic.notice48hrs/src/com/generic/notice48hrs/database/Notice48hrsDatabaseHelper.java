package com.generic.notice48hrs.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Notice48hrsDatabaseHelper extends SQLiteOpenHelper{
			
	protected static final String DATABASE_NAME = "Notice.db";
	protected static final int DATABASE_VERSION = 2;
    protected UserProfileDataSource dsUp;

	public Notice48hrsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	protected String databaseCreate() {
		String result = "";
		result += unpaidCreate(unpaidFields());
		return result;
	}
	
	protected String databaseDrop() {
		String drpStatement = "DROP TABLE IF EXISTS ";
		String result = "";
		result += drpStatement + ConsumerDataSource.TABLE_CONSUMER + "; ";
		result += drpStatement + UnpaidDataSource.TABLE_UNPAID + "; " ;
		result += drpStatement + UserProfileDataSource.TABLE_USER_PROFILE + "; ";
		return result;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(databaseCreate());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Notice48hrsDatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		refreshDatabase(db);
	}

	private void refreshDatabase(SQLiteDatabase db) {
		db.execSQL(databaseDrop());
		onCreate(db);
	}
	
	public void refreshUnpaid(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + UnpaidDataSource.TABLE_UNPAID);
		db.execSQL(unpaidCreate(unpaidFields()));
	}

    protected List<String> unpaidFields(){
        List<String> unpaidFields = new ArrayList<String>();
        unpaidFields.add(UnpaidDataSource.ID + " integer primary key autoincrement, ");
        unpaidFields.add(UnpaidDataSource.CODE + " integer not null, ");
        unpaidFields.add(UnpaidDataSource.BILLMONTH + " text not null, ");
        unpaidFields.add(UnpaidDataSource.AMOUNT + " real not null");
        return  unpaidFields;
    }
    
    protected String unpaidCreate(List<String> unpaidFields){
        String result = "create table "
            + UnpaidDataSource.TABLE_UNPAID + "(";
        for (String string : unpaidFields){
            result += string;
        }
        result += "); ";
        return result;
    }


    


}
