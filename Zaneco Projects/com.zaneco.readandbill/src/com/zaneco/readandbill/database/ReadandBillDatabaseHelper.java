package com.zaneco.readandbill.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ReadandBillDatabaseHelper extends com.generic.readandbill.database.ReadandBillDatabaseHelper{

    private static final String DATABASE_NAME = "readandbill.db";
    private static final int DATABASE_VERSION = 4;

    public ReadandBillDatabaseHelper(Context context) {
        super(context);
    }
    
    //public ReadandBillDatabaseHelper(Context context) {
    // 	 super(context, DATABASE_NAME, null,DATABASE_VERSION);
    // }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConsumerDataSource.DATABASE_CREATE());
        db.execSQL(NewConnectionDataSource.DATABASE_CREATE());
        db.execSQL(RateDataSource.DATABASE_CREATE());
        db.execSQL(ReadingDataSource.DATABASE_CREATE());
        db.execSQL(UnpaidDataSource.DATABASE_CREATE());
        db.execSQL(UserProfileDataSource.DATABASE_CREATE());
        db.execSQL(BillhistoryDataSource.DATABASE_CREATE());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropSQL = "drop table if exists ";

        db.execSQL(dropSQL + ConsumerDataSource.TABLE_CONSUMERS);
        db.execSQL(dropSQL + NewConnectionDataSource.TABLE_NEWCON);
        db.execSQL(dropSQL + RateDataSource.TABLE_RATES);
        db.execSQL(dropSQL + ReadingDataSource.TABLE_READINGS);
        db.execSQL(dropSQL + UnpaidDataSource.TABLE_UNPAID);
        db.execSQL(dropSQL + UserProfileDataSource.TABLE_USER_PROFILE);
        db.execSQL(dropSQL + BillhistoryDataSource.TABLE_BILLHISTORY);

        db.execSQL(ConsumerDataSource.DATABASE_CREATE());
        db.execSQL(NewConnectionDataSource.DATABASE_CREATE());
        db.execSQL(RateDataSource.DATABASE_CREATE());
        db.execSQL(ReadingDataSource.DATABASE_CREATE());
        db.execSQL(UnpaidDataSource.DATABASE_CREATE());
        db.execSQL(UserProfileDataSource.DATABASE_CREATE());
        db.execSQL(BillhistoryDataSource.DATABASE_CREATE());
    }

   

   /*
	@Override
	protected String databaseDrop() {
		String sql = "DROP TABLE IF EXISTS ";
		String result = "";
		result += sql + UnpaidDataSource.TABLE_UNPAID + "; ";
		result += sql + NewConnectionDataSource.TABLE_NEWCON + "; ";
		return super.databaseDrop() + result;
	}

	public ReadandBillDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}
	
	public void refreshUB(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + UnpaidDataSource.TABLE_UNPAID);
		db.execSQL(unpaidCreate(unpaidFields()));
	}

	public void refreshNC(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + NewConnectionDataSource.TABLE_NEWCON);
		db.execSQL(newConnectionCreate(newConnectionFields()));
	}
	
	//@Override

 



	
	protected String unpaidCreate(List<String> unpaidFields){
		String result = "create table "
			+ UnpaidDataSource.TABLE_UNPAID + "(";
		for (String string : unpaidFields) {
			result += string;
		}
		result += "); ";
		return result;
	}
	
	protected String newConnectionCreate(List<String> newConnectionFields){
		String result = "create table "
			+ NewConnectionDataSource.TABLE_NEWCON + "(";
		for (String string : newConnectionFields) {
			result += string;
		}
		result += "); ";
		return result;
	}
	
	//@Override

	}*/


}
