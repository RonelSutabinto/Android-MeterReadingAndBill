package com.generic.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDataSource {

    public static final String TABLE_CONSUMERS = "consumers";

    public static final String ID = "_id";
    public static final String ACCOUNTNUMBER = "accountnumber";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String RATECODE = "ratecode";
    public static final String METERSERIAL = "meterserial";
    public static final String READINGDATE = "readingdate";
    public static final String INITIALREADING = "initialreading";
    public static final String MULTIPLIER = "multiplier";
    public static final String TRANSFORMER_RENTAL = "transformerrental";
    public static final String CONNECTIONCODE = "connectioncode";
    public static final String CMSWITCH = "cmswitch";
    public static final String CMMULTIPLIER = "cmmultiplier";
    public static final String CMREADING = "cmreading";
    public static final String CMINITIALREADING = "cminitialreading";
    public static final String CMDEMAND = "cmdemand";
    public static final String CODE = "code";
    public static final String DEMAND = "demand";
    //public static final String DEMANDRDNG = "demandrdng";
    public static final String ISFIXDEMAND = "isfixdemand";
    public static final String POLENUMBER = "polenumber";
    public static final String XFORMERQTY = "xformerqty";
    public static final String XFORMERKVA = "xformerkva";
    public static final String RCODE = "rcode";
    public static final String ISGRAM = "isgram";
    

    protected SQLiteDatabase db;
    protected ReadandBillDatabaseHelper dbHelper;
    protected String[] allColumns = { ID,CODE, ACCOUNTNUMBER, NAME, ADDRESS, RATECODE, METERSERIAL, READINGDATE,
            INITIALREADING, MULTIPLIER, TRANSFORMER_RENTAL,CONNECTIONCODE,
            CMSWITCH, CMMULTIPLIER, CMREADING, CMINITIALREADING, CMDEMAND,
            DEMAND,ISFIXDEMAND,POLENUMBER,XFORMERQTY,XFORMERKVA,RCODE,ISGRAM};//,DEMANDRDNG};

    public static List<String> consumerFields(){
        List<String> consumerFields = new ArrayList<String>();
        consumerFields.add(ID + " integer primary key autoincrement, ");
        consumerFields.add(CODE + " integer not null, ");
        consumerFields.add(ACCOUNTNUMBER + " text not null, ");
        consumerFields.add(NAME + " text not null, ");
        consumerFields.add(ADDRESS + " text not null, ");
        consumerFields.add(RATECODE + " text not null, ");
        consumerFields.add(METERSERIAL + " text not null, ");
        consumerFields.add(READINGDATE + " text not null, ");
        consumerFields.add(INITIALREADING + " real not null, ");
        consumerFields.add(TRANSFORMER_RENTAL + " real not null, ");
        consumerFields.add(MULTIPLIER + " real not null, ");
        consumerFields.add(CONNECTIONCODE + " integer not null, ");
        consumerFields.add(CMSWITCH + " integer not null, ");
        consumerFields.add(CMMULTIPLIER + " real not null, ");
        consumerFields.add(CMREADING + " real not null, ");
        consumerFields.add(CMINITIALREADING + " real not null, ");
        consumerFields.add(CMDEMAND + " real not null,");
        consumerFields.add(DEMAND+" real not null,");
        consumerFields.add(ISFIXDEMAND+ " real not null, ");
        consumerFields.add(POLENUMBER+ " text not null, ");
        consumerFields.add(XFORMERQTY+ " integer not null,");
        consumerFields.add(XFORMERKVA+ " text not null, ");
        consumerFields.add(RCODE + " text not null, ");
        consumerFields.add(ISGRAM + " integer not null ");

        
        return consumerFields;
    }

    public ConsumerDataSource(Context context) {
        dbHelper = new ReadandBillDatabaseHelper(context);
    }

    public ConsumerDataSource(ReadandBillDatabaseHelper dbHelper, Context context){
        this.dbHelper = dbHelper;
        if (this.dbHelper == null) new ConsumerDataSource(context);
    }

    public boolean isOpen() {
        if (db != null) {
            return db.isOpen();
        } else {
            return false;
        }
    }

    protected ContentValues consumerContentValues(Consumer consumer){
        ContentValues values = new ContentValues();
        values.put(CODE, consumer.getCode());
        values.put(ACCOUNTNUMBER, consumer.getAccountNumber());
        values.put(NAME, consumer.getName());
        values.put(ADDRESS, consumer.getAddress());
        values.put(RATECODE, consumer.getRateCode());
        values.put(MULTIPLIER, consumer.getMultiplier());
        values.put(METERSERIAL, consumer.getMeterSerial());
        values.put(READINGDATE, consumer.getInitialReadingDate());
        values.put(INITIALREADING, consumer.getInitialReading());
        values.put(TRANSFORMER_RENTAL, consumer.getTransformerRental()); 
        values.put(CONNECTIONCODE, consumer.getConnectionCode());
        values.put(CMSWITCH, consumer.getCmSwitch());
        values.put(CMMULTIPLIER, consumer.getCmMultiplier());
        values.put(CMREADING, consumer.getCmReading());
        values.put(CMINITIALREADING, consumer.getCmInitialReading());
        values.put(CMDEMAND, consumer.getCmDemand());
        values.put(DEMAND,consumer.getDemand());
        values.put(ISFIXDEMAND,consumer.getIsfixdemand());
        values.put(POLENUMBER, consumer.getPoleNumber());
        values.put(XFORMERQTY, consumer.getXFormerQty());
        values.put(XFORMERKVA, consumer.getXFormerKVA());
        values.put(RCODE, consumer.getRcode());
        values.put(ISGRAM,consumer.getisGram());
        return values;
    }

    public Consumer createConsumer(Consumer consumer) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_CONSUMERS,
                null, consumerContentValues(consumer));
        db.close();
        return getConsumer(insertId);
    }

    public Consumer getConsumer(Long id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMERS,
                allColumns, ID + "=" + id,
                null, null, null,null);
        cursor.moveToFirst();
        db.close();
        return cursorToConsumer(cursor, new Consumer());
    }

    public List<Consumer> getAllConsumer() {
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONSUMERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        List<Consumer> result = new ArrayList<Consumer>();
        while (!cursor.isAfterLast()) {
            result.add(cursorToConsumer(cursor,new Consumer()));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Consumer> getAllUnReadConsumer() {
        db = dbHelper.getReadableDatabase();
        List<Consumer> result = new ArrayList<Consumer>();
        Cursor cursor = db
                .rawQuery("SELECT c.* " + "FROM " + TABLE_CONSUMERS  + " c LEFT JOIN " +
                        ReadingDataSource.TABLE_READINGS + " r on r." + ReadingDataSource.IDCONSUMER + " = c." + ID +
                        " WHERE r." + ReadingDataSource.ID + " is null;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToConsumer(cursor, new Consumer()));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    protected Consumer cursorToConsumer(Cursor cursor, Consumer consumer) {
        consumer.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        consumer.setCode(cursor.getLong(cursor.getColumnIndex(CODE)));
        consumer.setAccountNumber(cursor.getString(cursor.getColumnIndex(ACCOUNTNUMBER)));
        consumer.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        consumer.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
        consumer.setRateCode(cursor.getString(cursor.getColumnIndex(RATECODE)));
        consumer.setMeterSerial(cursor.getString(cursor.getColumnIndex(METERSERIAL)));
        consumer.setInitialReadingDate(cursor.getString(cursor.getColumnIndex(READINGDATE)));
        consumer.setInitialReading(cursor.getDouble(cursor.getColumnIndex(INITIALREADING)));
        consumer.setMultiplier(cursor.getDouble(cursor.getColumnIndex(MULTIPLIER)));
        consumer.setTransformerRental(cursor.getDouble(cursor.getColumnIndex(TRANSFORMER_RENTAL)));
        consumer.setConnectionCode(cursor.getInt(cursor.getColumnIndex(CONNECTIONCODE)));
        consumer.setCmSwitch(cursor.getLong(cursor.getColumnIndex(CMSWITCH)) > 0);
        consumer.setCmMultiplier(cursor.getDouble(cursor.getColumnIndex(CMMULTIPLIER)));
        consumer.setCmReading(cursor.getDouble(cursor.getColumnIndex(CMREADING)));
        consumer.setCmInitialReading(cursor.getDouble(cursor.getColumnIndex(CMINITIALREADING)));
        consumer.setCmDemand(cursor.getDouble(cursor.getColumnIndex(CMDEMAND)));
        consumer.setDemand(cursor.getDouble(cursor.getColumnIndex(DEMAND)));
        consumer.setIsfixdemand(cursor.getInt(cursor.getColumnIndex(ISFIXDEMAND)));
        consumer.setPoleNumber(cursor.getString(cursor.getColumnIndex(POLENUMBER)));
        consumer.setXFormerQty(cursor.getInt(cursor.getColumnIndex(XFORMERQTY)));
        consumer.setXFormerKVA(cursor.getString(cursor.getColumnIndex(XFORMERKVA)));
        consumer.setRcode(cursor.getString(cursor.getColumnIndex(RCODE)));
        consumer.setisGram(cursor.getInt(cursor.getColumnIndex(ISGRAM)));
        return consumer;
    }

    public Integer getNumberOfConsumer() {
        db = dbHelper.getReadableDatabase();
        Integer result = 0;
        Cursor cursor = db.rawQuery("select count(*) recCount from "
                + TABLE_CONSUMERS, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getInt(0);
        } else {
            result = 0;
        }
        db.close();
        return result;
    }

    public void truncate() {
        db = dbHelper.getWritableDatabase();
        dbHelper.refreshTable(db, TABLE_CONSUMERS, consumerFields());
        db.close();
    }

}
