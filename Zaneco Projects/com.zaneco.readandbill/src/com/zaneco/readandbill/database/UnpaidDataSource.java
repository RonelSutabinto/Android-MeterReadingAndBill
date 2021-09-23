package com.zaneco.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UnpaidDataSource {

    public static final String TABLE_UNPAID = "unpaid";

    public static final String ID = "_id";
    public static final String CODE = "code";
    public static final String BILLMONTH = "billmonth";
    public static final String AMOUNT = "amount";

    private SQLiteDatabase db;
    private ReadandBillDatabaseHelper dbHelper;
    private String[] allColumns = { ID,
            CODE,
            BILLMONTH,
            AMOUNT };

    public static List<String> unpaidFields(){
        List<String> unpaidFields = new ArrayList<String>();
        unpaidFields.add(ID + " integer primary key autoincrement, ");
        unpaidFields.add(CODE + " integer not null, ");
        unpaidFields.add(BILLMONTH + " text not null, ");
        unpaidFields.add(AMOUNT + " real not null");
        return unpaidFields;
    }

    public UnpaidDataSource(Context context) {
        dbHelper = new ReadandBillDatabaseHelper(context);
    }

    private ContentValues unpaidContentValues(Unpaid unpaid){
        ContentValues values = new ContentValues();
        values.put(CODE, unpaid.getCode());
        values.put(BILLMONTH, unpaid.getBillmonth());
        values.put(AMOUNT, unpaid.getAmount());
        return values;
    }

    public Unpaid createUnpaid(Unpaid unpaid) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_UNPAID, null,
                unpaidContentValues(unpaid));
        db.close();
        return getUnpaid(insertId);
    }

    public Unpaid getUnpaid(long id){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_UNPAID, allColumns,
                ID + "=" + id, null, null,
                null, null);
        cursor.moveToFirst();
        Unpaid unpd = cursorToUnpaid(cursor);
        cursor.close();
        db.close();
        return unpd;
    }

    public boolean hasUnpaid(Long consumerRef) {
        db = dbHelper.getReadableDatabase();
        boolean result = false;
        Cursor cursor = db.query(TABLE_UNPAID, allColumns,
                CODE + "=" + consumerRef, null,
                null, null, null);
        result = cursor.getCount() > 0;
        db.close();
        return result;
    }

    public List<Unpaid> getUnpaids(Long consumerRef) {
        db = dbHelper.getReadableDatabase();
        List<Unpaid> unpaids = new ArrayList<Unpaid>();

        Cursor cursor = db.query(TABLE_UNPAID, allColumns,
                CODE + "=" + consumerRef, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Unpaid unpaid = cursorToUnpaid(cursor);
            unpaids.add(unpaid);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return unpaids;
    }

    public Double getTotalUnpaid(Long consumerRef) {
        db = dbHelper.getReadableDatabase();
        Double result;
        Cursor cursor = db.rawQuery("SELECT ifnull(sum("
                + AMOUNT
                + "),0) FROM unpaid WHERE " + CODE
                + "=" + consumerRef, null);
        cursor.moveToFirst();
        result = cursor.getDouble(0);
        cursor.close();
        db.close();
        return result;
    }

    private Unpaid cursorToUnpaid(Cursor cursor) {
        Unpaid unpaid = new Unpaid();
        if (cursor.getCount() != 0) {
            unpaid.setId(cursor.getLong(0));
            unpaid.setCode(cursor.getLong(1));
            unpaid.setBillmonth(cursor.getString(2));
            unpaid.setAmount(cursor.getDouble(3));
        }
        return unpaid;
    }

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_UNPAID + "(";
        for (String s : unpaidFields()){
            result += s;
        }
        result += "); ";
        return result;
    }

    public void refreshUnpaid(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_UNPAID);
        db.execSQL(DATABASE_CREATE());
        db.close();
    }
}
