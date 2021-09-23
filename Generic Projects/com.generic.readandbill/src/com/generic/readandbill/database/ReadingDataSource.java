package com.generic.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReadingDataSource {

    public static final String TABLE_READINGS = "readings";

    public static final String ID = "_id";
    public static final String IDCONSUMER = "idconsumer";
    public static final String READING = "reading";
    public static final String TRANSACTIONDATE = "transactiondate";
    public static final String ISPRINTED = "isprinted";
    public static final String FEEDBACKCODE = "feedbackcode";
    public static final String DEMAND = "demand";
    public static final String FIELDFINDING = "fieldFinding";
    public static final String SENIORCITIZENDISCOUNT = "seniorcitizendiscount";
    public static final String KILOWATTHOUR = "kilowatthour";
    public static final String REMARKS = "remarks";
    public static final String TOTALAMOUNT = "totalamount";
    public static final String ISAM = "isam";
    public static final String READINGDATE = "readingdate";
    public static final String ATM_REF = "atm_ref";
    public static final String DEMANDRDNG = "demandRdng";

    public static final int READING_ID = 15;
    public static final int CONSUMER_ID = 20;

    //Database fields
    protected SQLiteDatabase db;
    private ReadandBillDatabaseHelper dbHelper;
    protected String[] allColumns = { ID,
            IDCONSUMER,
            READING,
            TRANSACTIONDATE,
            ISPRINTED,
            FEEDBACKCODE,
            DEMAND,
            FIELDFINDING,
            SENIORCITIZENDISCOUNT,
            KILOWATTHOUR,
            REMARKS,
            TOTALAMOUNT,
            ISAM,
            READINGDATE,
            ATM_REF,
            DEMANDRDNG};

    public static List<String> readingFields(){
        List<String> readingFields = new ArrayList<String>();
        readingFields.add(ReadingDataSource.ID + " integer primary key autoincrement, ");
        readingFields.add(ReadingDataSource.IDCONSUMER + " integer not null, ");
        readingFields.add(ReadingDataSource.READING + " real not null, ");
        readingFields.add(ReadingDataSource.TRANSACTIONDATE + " integer not null, ");
        readingFields.add(ReadingDataSource.ISPRINTED + " integer not null, ");
        readingFields.add(ReadingDataSource.FEEDBACKCODE + " text not null, ");
        readingFields.add(ReadingDataSource.DEMAND + " real(8,1) not null, ");
        readingFields.add(ReadingDataSource.FIELDFINDING + " integer not null, ");
        readingFields.add(ReadingDataSource.SENIORCITIZENDISCOUNT + " real not null, ");
        readingFields.add(ReadingDataSource.KILOWATTHOUR + " real not null, ");
        readingFields.add(ReadingDataSource.REMARKS + " text not null, ");
        readingFields.add(ReadingDataSource.TOTALAMOUNT + " real not null, ");
        readingFields.add(ReadingDataSource.ISAM + " integer not null, ");
        readingFields.add(ReadingDataSource.READINGDATE + " text not null, ");      
        readingFields.add(ReadingDataSource.ATM_REF + " text not null, ");   
        readingFields.add(ReadingDataSource.DEMANDRDNG + " real not null ");       
        	
        return readingFields;
    }

    public ReadingDataSource(Context context) {
        dbHelper = new ReadandBillDatabaseHelper(context);
    }
    public ReadingDataSource(ReadandBillDatabaseHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        if (this.dbHelper == null) new ReadingDataSource(context);
    }

    protected ContentValues readingContentValues(Reading reading){
        ContentValues values = new ContentValues();
        Integer isPrinted = 0;
        Integer isAM = 0;
        if (reading.isPrinted) {
            isPrinted = 1;
        }
        if (reading.isAM){
            isAM = 1;
        }
        values.put(IDCONSUMER, reading.getIdConsumer());
        values.put(READING, reading.getReading());
        values.put(TRANSACTIONDATE,
                reading.getTransactionDate());
        values.put(ISPRINTED,
                isPrinted);
        values.put(FEEDBACKCODE,
                reading.getFeedBackCode());
        values.put(DEMAND, reading.getDemand());
        values.put(FIELDFINDING,
                reading.getFieldFinding());
        values.put(SENIORCITIZENDISCOUNT,
                reading.getSeniorCitizenDiscount());
        values.put(KILOWATTHOUR,
                reading.getKilowatthour());
        values.put(REMARKS, reading.getRemarks());
        values.put(TOTALAMOUNT,
                reading.getTotalbill());
        values.put(ISAM, isAM);
        values.put(READINGDATE, reading.getReadingDate());
        values.put(ATM_REF,reading.getAtmref());
        values.put(DEMANDRDNG, reading.getDemandRdng());
        return values;
    }

    public Reading createReading(Reading reading) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_READINGS, null,
                readingContentValues(reading));
        db.close();
        return getReading(insertId, READING_ID);
    }

    public void deleteReading(Reading reading) {
        db = dbHelper.getWritableDatabase();
        long id = reading.getId();
        db.delete(TABLE_READINGS,
                ID + "=" + id, null);
        db.close();
    }

    public void updateReading(Reading reading) {
        db = dbHelper.getWritableDatabase();
        long id = reading.getId();
        db.update(TABLE_READINGS, readingContentValues(reading),
                ID + "=" + id, null);
        db.close();
    }

    public List<Reading> getAllReadings() {
        db = dbHelper.getReadableDatabase();
        List<Reading> readings = new ArrayList<Reading>();

        Cursor cursor = db.query(TABLE_READINGS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reading reading = cursorToReading(cursor, new Reading());
            readings.add(reading);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return readings;
    }

    public Reading getReading(long id, int mode) {
        db = dbHelper.getReadableDatabase();
        Reading myReading = new Reading();
        Cursor cursor;
        switch (mode) {
            case READING_ID:
                cursor = db.query(TABLE_READINGS, allColumns,
                        ID + "=" + id, null, null,
                        null, null);
                break;
            case CONSUMER_ID:
                cursor = db.query(TABLE_READINGS, allColumns,
                        IDCONSUMER + "=" + id, null,
                        null, null, null);
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null){
            cursor.moveToFirst();
            myReading = cursorToReading(cursor, myReading);
            cursor.close();
        }
        db.close();
        return myReading;
    }

    public Integer getTotalRead() {
        db = dbHelper.getReadableDatabase();
        Integer result = 0;
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM readings", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getInt(0);
        } else {
            result = 0;
        }
        db.close();
        return result;
    }

    public List<ZoneReport> getZoneReport(){
        db = dbHelper.getReadableDatabase();
        List<ZoneReport> zones = new ArrayList<ZoneReport>();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) as QtyToBeRead, " +
                TRANSACTIONDATE + ", " +
                "MIN(" + TRANSACTIONDATE + ") as minTime, " +
                "MAX(" + TRANSACTIONDATE + ") as maxTime, " +
                "MIN(" + TRANSACTIONDATE + ") - " +
                "MAX(" + TRANSACTIONDATE + ") as totalTime " +
                "FROM " + TABLE_READINGS + " GROUP BY " +
                ISAM + ", " +
                READINGDATE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            zones.add(cursorToZone(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return zones;
    }

    protected Reading cursorToReading(Cursor cursor, Reading reading) {
        if (cursor.getCount() != 0) {
            reading.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            reading.setidConsumer(cursor.getLong(cursor.getColumnIndex(IDCONSUMER)));
            reading.setReading(cursor.getDouble(cursor.getColumnIndex(READING)));
            reading.setTransactionDate(cursor.getLong(cursor.getColumnIndex(TRANSACTIONDATE)));
            reading.setIsPrinted(cursor.getLong(cursor.getColumnIndex(ISPRINTED)) > 0);
            reading.setFeedBackCode(cursor.getString(cursor.getColumnIndex(FEEDBACKCODE)));
            reading.setDemand(cursor.getDouble(cursor.getColumnIndex(DEMAND)));
            reading.setFieldFinding(cursor.getLong(cursor.getColumnIndex(FIELDFINDING)));
            reading.setSeniorCitizenDiscount(cursor.getDouble(cursor.getColumnIndex(SENIORCITIZENDISCOUNT)));
            reading.setKilowatthour(cursor.getDouble(cursor.getColumnIndex(KILOWATTHOUR)));
            reading.setRemarks(cursor.getString(cursor.getColumnIndex(REMARKS)));
            reading.setTotalbill(cursor.getDouble(cursor.getColumnIndex(TOTALAMOUNT)));
            reading.setIsAM(cursor.getInt(cursor.getColumnIndex(ISAM)) > 0);
            reading.setReadingDate(cursor.getString(cursor.getColumnIndex(READINGDATE)));
            reading.setAtmref(cursor.getString(cursor.getColumnIndex(ATM_REF)));  
            reading.setDemandRdng(cursor.getDouble(cursor.getColumnIndex(DEMANDRDNG)));
           
        } else {
            reading.setId((long) -1);
        }
        return reading;
    }

    private ZoneReport cursorToZone(Cursor cursor){
        ZoneReport zone = new ZoneReport();
        if (cursor.getCount() != 0){
            zone.setQtyToBeRead(cursor.getLong(0));
            zone.setReadingDate(cursor.getLong(1));
            zone.setMinTime(cursor.getLong(2));
            zone.setMaxTime(cursor.getLong(3));
            zone.setTotalTime(cursor.getLong(4));
        }
        return zone;
    }

    public void truncate(){
        db = dbHelper.getWritableDatabase();
        dbHelper.refreshTable(db, TABLE_READINGS, readingFields());
        db.close();
    }

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_READINGS + "(";
        for (String s : readingFields()) {
            result += s;
        }
        result += ");";
        return result;
    }
}
