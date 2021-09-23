package com.zaneco.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.androidapp.mytools.objectmanager.ArrayManager;
import com.generic.readandbill.database.UnreadAccounts;

import java.util.ArrayList;
import java.util.List;

public class ConsumerDataSource extends
        com.generic.readandbill.database.ConsumerDataSource {

    
    public static final String EVATDISCOUNT = "evatdiscount";   
    public static final String OCCODE1 = "occode1";
    public static final String OCAMOUNT1 = "ocamount1";
    public static final String OCCODE2 = "occode2";
    public static final String OCAMOUNT2 = "ocamount2";
    public static final String OCCODE3 = "occode3";
    public static final String OCAMOUNT3 = "ocamount3";    
    public static final String PANTAWIDSUBSIDY = "pantawidsubsidy";
    public static final String PANTAWIDRECOVERYREF = "pantawidrecoveryref";
    public static final String PANTAWIDRECOVERY = "pantawidrecovery";
    public static final String SENIORCITIZENNUMMON = "seniorcitizennummon";
    public static final String RPTTAX = "rpttax";
  

    private ReadandBillDatabaseHelper dbHelper;
    private String[] zAllColumns = { EVATDISCOUNT,
            OCCODE1, OCAMOUNT1, OCCODE2, OCAMOUNT2, OCCODE3, OCAMOUNT3, DEMAND, PANTAWIDSUBSIDY, 
            PANTAWIDRECOVERYREF, PANTAWIDRECOVERY, SENIORCITIZENNUMMON,RPTTAX};
    
    
      public static List<String> consumerFields() {
        List<String> consumerFields = com.generic.readandbill.database.ConsumerDataSource.consumerFields();
        consumerFields.set(consumerFields.size() - 1,
                consumerFields.get(consumerFields.size() - 1) + ", ");        
        consumerFields.add(EVATDISCOUNT + " real not null, ");        
        consumerFields.add(OCCODE1 + " text not null, ");
        consumerFields.add(OCAMOUNT1 + " real not null, ");
        consumerFields.add(OCCODE2 + " text not null, ");
        consumerFields.add(OCAMOUNT2 + " real not null, ");
        consumerFields.add(OCCODE3 + " text not null, ");
        consumerFields.add(OCAMOUNT3 + " real not null, ");       
        consumerFields.add(PANTAWIDSUBSIDY + " real not null, ");
        consumerFields.add(PANTAWIDRECOVERYREF + " text not null, ");
        consumerFields.add(PANTAWIDRECOVERY + " real not null, ");
        consumerFields.add(SENIORCITIZENNUMMON + " integer not null, ");   
        consumerFields.add(RPTTAX + " real not null ");
      
        return consumerFields;
    }

    public ConsumerDataSource(Context context) {
        super(new ReadandBillDatabaseHelper(context), context);
        dbHelper = new ReadandBillDatabaseHelper(context);
        allColumns = ArrayManager.concat(allColumns, zAllColumns);
    }

    protected ContentValues consumerContentValues(Consumer consumer) {
        ContentValues values = super.consumerContentValues(consumer);
        
        values.put(EVATDISCOUNT, consumer.getEvatDiscount());        
        // values.put(EVATDISCOUNT, consumer.getOcAmount1());
        values.put(OCCODE1, consumer.getOcCode1());
        values.put(OCAMOUNT1, consumer.getOcAmount1());
        values.put(OCCODE2, consumer.getOcCode2());
        values.put(OCAMOUNT2, consumer.getOcAmount2());
        values.put(OCCODE3, consumer.getOcCode3());
        values.put(OCAMOUNT3, consumer.getOcAmount3());        
        values.put(PANTAWIDSUBSIDY, consumer.getPantawidSubsidy());
        values.put(PANTAWIDRECOVERYREF, consumer.getPantawidRecoveryRef());
        values.put(PANTAWIDRECOVERY, consumer.getPantawidRecovery());
        values.put(SENIORCITIZENNUMMON, consumer.getSeniorCitizenNumMon());
        values.put(RPTTAX, consumer.getRptTax());
     
        return values;
    }

    public Consumer createConsumer(Consumer consumer){
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_CONSUMERS, null,
                consumerContentValues(consumer));
        db.close();
        return getConsumer(insertId);
    }

    @Override
    public Consumer getConsumer(Long id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMERS, allColumns, ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        db.close();
        return cursorToConsumer(cursor);
    }

    public List<UnreadAccounts> getAllUnread() {
        db = dbHelper.getReadableDatabase();
        List<UnreadAccounts> unReads = new ArrayList<UnreadAccounts>();
        Cursor cursor = db.rawQuery("SELECT " + ACCOUNTNUMBER + ", " + NAME
                + " FROM " + TABLE_CONSUMERS + " WHERE "
                + "NOT EXISTS (SELECT * FROM "
                + ReadingDataSource.TABLE_READINGS + " WHERE "
                + TABLE_CONSUMERS + "." + CODE + " = "
                + ReadingDataSource.TABLE_READINGS + "."
                + ReadingDataSource.IDCONSUMER + ")", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            unReads.add(cursorToUnread(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return unReads;
    }

    public List<Consumer> getReadConsumer(String acctFrom, String acctTo) {
        db = dbHelper.getReadableDatabase();
        List<Consumer> consumers = new ArrayList<Consumer>();
        Cursor cursor = db.rawQuery("SELECT c." + ID + ", " + "c." + CODE + ", " + "c." + ACCOUNTNUMBER + ", " + "c."
                + NAME + ", c." + ADDRESS + ", c." + RATECODE + ", c." + MULTIPLIER + ", c." + METERSERIAL + ", c."
                + EVATDISCOUNT + ", c." + READINGDATE + ", c." + INITIALREADING + ", c." + CMSWITCH + ", c."
                + CMMULTIPLIER + ", c." + CMREADING + ", c." + CMINITIALREADING + ", c." + CMDEMAND + ", c."
                + TRANSFORMER_RENTAL + ", c." + OCCODE1 + ", c." + OCAMOUNT1 + ", c." + OCCODE2 + ", c." + OCAMOUNT2
                + ", c." + OCCODE3 + ", c." + OCAMOUNT3 + ", c." + CONNECTIONCODE + ", c." + DEMAND + ", c."
                + PANTAWIDSUBSIDY + ", c." + PANTAWIDRECOVERYREF + ", c." + PANTAWIDRECOVERY + ", c."
                + SENIORCITIZENNUMMON + ", c." + RPTTAX
                + " FROM " + TABLE_CONSUMERS + " c INNER JOIN " + ReadingDataSource.
                TABLE_READINGS + " as r ON r." + ReadingDataSource.IDCONSUMER + " = c." + CODE + " WHERE "
                + ACCOUNTNUMBER + " BETWEEN " + acctFrom + " and " + acctTo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            consumers.add(cursorToConsumer(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return consumers;
    }
    
    public Consumer getConsumerRefReading(Long code) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONSUMERS, allColumns,
                CODE + "=" + code, null, null, null, null);
        cursor.moveToFirst();
        db.close();
        return cursorToConsumer(cursor);
    }

    public List<Consumer> getZanecoAllUnReadConsumer() {
        db = dbHelper.getReadableDatabase();
        List<Consumer> result = new ArrayList<Consumer>();
        Cursor cursor = db
                .rawQuery("SELECT c.* " + "FROM " + TABLE_CONSUMERS  + " c LEFT JOIN " +
                        ReadingDataSource.TABLE_READINGS + " r on r." + ReadingDataSource.IDCONSUMER + " = c." + ID +
                        " WHERE r." + ReadingDataSource.ID + " is null;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToConsumer(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Consumer> getReadConsumer() {
        db = dbHelper.getReadableDatabase();
        List<Consumer> consumers = new
                ArrayList<Consumer>();
        Cursor cursor = db.rawQuery("SELECT c.* FROM " + TABLE_CONSUMERS + " c inner join " + ReadingDataSource.
                TABLE_READINGS + " r on r.idconsumer = c._id" +
                " group by c."+ ConsumerDataSource.ACCOUNTNUMBER +
                " ORDER BY c." + ConsumerDataSource.ACCOUNTNUMBER + ";",
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            consumers.add(cursorToConsumer(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return consumers;
    }


    public String getZone() {
        db = dbHelper.getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT SUBSTR(" + ACCOUNTNUMBER
                + ",1,2) FROM " + TABLE_CONSUMERS, null);
        cursor.moveToFirst();
        result = cursor.getString(0);
        cursor.close();
        db.close();
        return result;
    }

    protected Consumer cursorToConsumer(Cursor cursor) {
        Consumer consumer = new Consumer();
        if (cursor.getCount() != 0) {
            consumer = (Consumer) super.cursorToConsumer(cursor, consumer);            
            consumer.setEvatDiscount(cursor.getDouble(cursor.getColumnIndex(EVATDISCOUNT)));           
            consumer.setOcCode1(cursor.getString(cursor.getColumnIndex(OCCODE1)));
            consumer.setOcAmount1(cursor.getDouble(cursor.getColumnIndex(OCAMOUNT1)));
            consumer.setOcCode2(cursor.getString(cursor.getColumnIndex(OCCODE2)));
            consumer.setOcAmount2(cursor.getDouble(cursor.getColumnIndex(OCAMOUNT2)));
            consumer.setOcCode3(cursor.getString(cursor.getColumnIndex(OCCODE3)));
            consumer.setOcAmount3(cursor.getDouble(cursor.getColumnIndex(OCAMOUNT3)));            
            consumer.setDemand(cursor.getDouble(cursor.getColumnIndex(DEMAND)));
            consumer.setPantawidSubsidy(cursor.getDouble(cursor.getColumnIndex(PANTAWIDSUBSIDY)));
            consumer.setPantawidRecoveryRef(cursor.getString(cursor.getColumnIndex(PANTAWIDRECOVERYREF)));
            consumer.setPantawidRecovery(cursor.getDouble(cursor.getColumnIndex(PANTAWIDRECOVERY)));
            consumer.setSeniorCitizenNumMon(cursor.getInt(cursor.getColumnIndex(SENIORCITIZENNUMMON)));
            consumer.setRptTax(cursor.getDouble(cursor.getColumnIndex(RPTTAX)));
         
        }
        return consumer;
    }

    
    /*public Consumer getConsumerR(String Accnt) {
        db = dbHelper.getReadableDatabase();
        Consumer MyConsumer;
        Cursor cursor = db.query(TABLE_CONSUMERS,
                allColumns, "accountnumber=" + Accnt,
                null, null, null,null);
        cursor.moveToFirst();
        MyConsumer =cursorToConsumer(cursor);
        cursor.close();
        db.close();
        return MyConsumer;
    }*/
    
   
     /*public Rates getRate(String rateCode) {
        db = dbHelper.getReadableDatabase();
        Rates myRate;
        Cursor cursor = db.query(TABLE_RATES, allColumns,
                RATECODE + "='" + rateCode + "'",
                null, null, null, null);
        cursor.moveToFirst();
        myRate = cursorToRate(cursor);
        cursor.close();
        db.close();
        return myRate;
    }
    */
    
    private UnreadAccounts cursorToUnread(Cursor cursor) {
        UnreadAccounts unRead = new UnreadAccounts();
        if (cursor.getCount() != 0) {
            unRead.setAccountNumber(cursor.getString(0));
            unRead.setName(cursor.getString(1));
        }
        return unRead;
    }

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_CONSUMERS + "(";
        for (String s : consumerFields()) {
            result += s;
        }
        result += ");";
        return result;
    }

    public void refreshConsumer(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_CONSUMERS);
        db.execSQL(DATABASE_CREATE());
        db.close();

    }
}
