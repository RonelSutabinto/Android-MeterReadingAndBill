package com.generic.notice48hrs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDataSource {
    public static final String TABLE_CONSUMER = "consumer";

    public static final String ID = "_id";
    public static final String ACCOUNTNUMBER = "accountnumber";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String ARREARS = "arrears";
    public static final String SERVED = "served";
    public static final String METERSERIAL = "meterserial";

	protected SQLiteDatabase db;
	protected Notice48hrsDatabaseHelper dbHelper;
	protected String[] allColumns = { ID,
			ACCOUNTNUMBER,
			NAME,
			ADDRESS,
			ARREARS,
			SERVED,
            METERSERIAL};

    public ConsumerDataSource(Context context) {
        dbHelper = new Notice48hrsDatabaseHelper(context);
    }

    protected static List<String> consumerFields(){
        List<String> consumerFields = new ArrayList<String>();
        consumerFields.add(ID + " integer primary key autoincrement, ");
        consumerFields.add(ACCOUNTNUMBER + " text not null, ");
        consumerFields.add(NAME + " text not null, ");
        consumerFields.add(ADDRESS + " text not null, ");
        consumerFields.add(ARREARS + " real not null, ");
        consumerFields.add(SERVED + " integer not null, ");
        consumerFields.add(METERSERIAL + " text not null");
        return  consumerFields;
    }

    protected ContentValues consumerValues(Consumer consumer){
        ContentValues values = new ContentValues();
        values.put(ACCOUNTNUMBER, consumer.getAccountNumber());
        values.put(NAME, consumer.getName());
        values.put(ADDRESS, consumer.getAddress());
        values.put(ARREARS, consumer.getArrears());
        values.put(SERVED, consumer.valueServed());
        values.put(METERSERIAL, consumer.getMeterSerial());
        return values;
    }

	public Consumer createConsumer(Consumer consumer) {
		db = dbHelper.getWritableDatabase();
		long insertId = db.insert(TABLE_CONSUMER,
				null, consumerValues(consumer));
		db.close();
		return getConsumer(consumer.getAccountNumber().replace("-","").trim());
	}
	
	//public UserProfile createUserProfile(UserProfile userProfile) {
	//	db = dbHelper.getWritableDatabase();
	//	long insertId = db.insert(TABLE_USER_PROFILE,  //userProfileFields
	//			null, userProfileValues(userProfile));
	//	db.close();
	//	return getUserProfile();
	//}
	
	//public UserProfile getUserProfile() {
	//	db = dbHelper.getReadableDatabase();
	//	Cursor cursor = db.query(TABLE_USER_PROFILE,
	//			allColumns, null, null, null, null, null);
	//	cursor.moveToFirst();
	//	UserProfile nuUp = cursorToUserProfile(cursor, new UserProfile());
	//	cursor.close();
	//	db.close();
	//	return nuUp;
	//}
	
    public Consumer getConsumer(long id){
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMER,
                allColumns, ID + "="
                + id, null, null, null, null);
        cursor.moveToFirst();
        Consumer con = cursorToConsumer(cursor, new Consumer());
        cursor.close();
        db.close();
        return con;
    }

    public Consumer getConsumer(String accountNumber){
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMER,
                allColumns, ACCOUNTNUMBER + "='"
                + accountNumber, null, null, null, null);
        cursor.moveToFirst();
        Consumer con = cursorToConsumer(cursor, new Consumer());
        cursor.close();
        db.close();
        return con;
    }

	public void updateConsumer(Consumer consumer) {
		db = dbHelper.getWritableDatabase();
		long id = consumer.getId();
		db.update(TABLE_CONSUMER, consumerValues(consumer),
                ID + "=" + id, null);
		db.close();
	}
	
	public void updateConsumerServed(Consumer consumer,int idS) {
		db = dbHelper.getWritableDatabase();
		long id = consumer.getId();
		db.update(TABLE_CONSUMER, consumerValues(consumer),
                ID + "=" + idS, null);
		db.close();
	}

	public List<Consumer> getAllConsumer() {
		db = dbHelper.getReadableDatabase();
		List<Consumer> consumers = new ArrayList<Consumer>();

		Cursor cursor = db.query(TABLE_CONSUMER,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Consumer consumer = cursorToConsumer(cursor, new Consumer());
			consumers.add(consumer);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return consumers;
	}

	protected Consumer cursorToConsumer(Cursor cursor, Consumer consumer) {
		if (cursor.getCount() != 0) {
			consumer.setId(cursor.getLong(cursor.getColumnIndex(ID)));
			//consumer.setCode(cursor.getLong(cursor.getColumnIndex(CODE)));
			consumer.setAccountNumber(cursor.getString(cursor.getColumnIndex(ACCOUNTNUMBER)));
			consumer.setName(cursor.getString(cursor.getColumnIndex(NAME)));
			consumer.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
			consumer.setArrears(cursor.getDouble(cursor.getColumnIndex(ARREARS)));
			consumer.setMeterSerial(cursor.getString(cursor.getColumnIndex(METERSERIAL)));
			consumer.setServed(cursor.getInt(cursor.getColumnIndex(SERVED)) == 1);
		}
		return consumer;
	}

	public Integer getNumberOfConsumer() {
		Integer result;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "
				+ TABLE_CONSUMER, null);
		cursor.moveToFirst();
		result = cursor.getInt(0);
		cursor.close();
		db.close();
		return result;
	}

	public Integer getNumberOfUnservered() {
		Integer result = 0;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "
				+ TABLE_CONSUMER + " WHERE "
				+ SERVED + " = 1", null);
		cursor.moveToFirst();
		result = cursor.getInt(0);
		cursor.close();
		db.close();
		return result;
	}

	public List<Consumer> getAllServedConsumer() {
		db = dbHelper.getReadableDatabase();
		List<Consumer> consumers = new ArrayList<Consumer>();

		Cursor cursor = db.query(TABLE_CONSUMER,
				allColumns, SERVED + " = 1",
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Consumer consumer = cursorToConsumer(cursor, new Consumer());
			consumers.add(consumer);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return consumers;
	}

	public void updateConsumersArrears() {
		db = dbHelper.getWritableDatabase();
		db.rawQuery("UPDATE " + TABLE_CONSUMER
                + " SET " + ARREARS
                + " = (SELECT SUM("
                + UnpaidDataSource.AMOUNT + ") FROM "
                + UnpaidDataSource.TABLE_UNPAID + " WHERE "
                + UnpaidDataSource.CODE + " = "
                + TABLE_CONSUMER + "."
                //+ CODE + ")"
                , null);
		db.close();
	}

    public static String consumerCreate(){
        String result = "create table "
                + ConsumerDataSource.TABLE_CONSUMER + "(";
        for (String string : consumerFields()){
            result += string;
        }
        result += ", UNIQUE(" + ACCOUNTNUMBER + ") ON CONFLICT IGNORE); ";
        return  result;
    }

    public void refresh(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ConsumerDataSource.TABLE_CONSUMER);
        db.execSQL(consumerCreate());
        db.close();
    }
}
