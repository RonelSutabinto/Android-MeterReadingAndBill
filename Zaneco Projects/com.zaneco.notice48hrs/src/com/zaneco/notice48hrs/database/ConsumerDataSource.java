package com.zaneco.notice48hrs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.androidapp.mytools.objectmanager.ArrayManager;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDataSource extends com.generic.notice48hrs.database.ConsumerDataSource{

    public static final String CODE = "code";
    public static final String SERVEDR = "servedr";

    private String[] zAllColumns = { CODE, METERSERIAL,SERVEDR };

    public ConsumerDataSource(Context context) {
        super(context);
        allColumns = ArrayManager.concat(allColumns,zAllColumns);
    }

    protected static List<String> consumerFields() {
        List<String> consumerFields = com.generic.notice48hrs.database.ConsumerDataSource.consumerFields();
        consumerFields.set(consumerFields.size() - 1, consumerFields.get(consumerFields.size() - 1) + ", ");
        consumerFields.add(ConsumerDataSource.CODE + " text not null,");
        consumerFields.add(ConsumerDataSource.SERVEDR +" integer DEFAULT 0");
        return consumerFields;
    }

    protected ContentValues consumerValues(Consumer consumer) {
        ContentValues consumerValues = super.consumerValues(consumer);
        consumerValues.put(CODE, consumer.getCode());
        consumerValues.put(SERVEDR,consumer.getServedr());
        return consumerValues;
    }

    public Consumer createConsumer(Consumer consumer) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_CONSUMER, null, consumerValues(consumer));
        db.close();
        return getConsumer(insertId);
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
                + CODE + ")"
                , null);
        db.close();
    }

    @Override
    public Consumer getConsumer(long id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMER, allColumns, ID + "=" + id,null,null,null,null);
        cursor.moveToFirst();
        Consumer consumer = cursorToConsumer(cursor);
        db.close();
        return consumer;
    }

    public List<Consumer> getAllZanecoConsumer() {
        db = dbHelper.getReadableDatabase();
        List<Consumer> consumers = new ArrayList<Consumer>();

        Cursor cursor = db.query(TABLE_CONSUMER,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Consumer consumer = cursorToConsumer(cursor);
            consumers.add(consumer);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return consumers;
    }

    protected Consumer cursorToConsumer(Cursor cursor) {
        Consumer consumer = new Consumer();
       // if (cursor.getCount() != 0){
            consumer = (Consumer) super.cursorToConsumer(cursor,consumer);
            consumer.setCode(cursor.getLong(cursor.getColumnIndex(CODE)));
            consumer.setMeterSerial(cursor.getString(cursor.getColumnIndex(METERSERIAL)));
            consumer.setServedr(cursor.getInt(cursor.getColumnIndex(SERVEDR)));
       // }
        return consumer;
    }

    public List<Consumer> getAllServedZanecoConsumer() {
        db = dbHelper.getReadableDatabase();
        List<Consumer> consumers = new ArrayList<Consumer>();

        Cursor cursor = db.query(TABLE_CONSUMER,
                allColumns, SERVED + " = 1",
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Consumer consumer = cursorToConsumer(cursor);
            consumers.add(consumer);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return consumers;
    }

    protected Consumer arrayToConsumer(String[] rawData){
        Consumer consumer = new Consumer();
        consumer.setCode(Long.parseLong(rawData[0]));
        consumer.setAccountNumber(rawData[1]);
        consumer.setName(rawData[2]);
        consumer.setAddress(rawData[3]);
        consumer.setMeterSerial(rawData[4]);
        consumer.setArrears(0);
        return consumer;
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

    @Override
    public void refresh(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ConsumerDataSource.TABLE_CONSUMER);
        db.execSQL(consumerCreate());
        db.close();
    }
}
