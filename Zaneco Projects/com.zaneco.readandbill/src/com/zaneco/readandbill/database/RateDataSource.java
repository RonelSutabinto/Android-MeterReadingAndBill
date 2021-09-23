package com.zaneco.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.androidapp.mytools.objectmanager.ArrayManager;

import java.util.List;

public class RateDataSource extends com.generic.readandbill.database.RateDataSource{
    
    public static final String UCNPCSTRANDEDCONTCOST = "ucnpcstrandedcontcost";
    public static final String UCNPCSTRANDEDDEBTS = "ucnpcstrandeddebts";
    public static final String UCDUSTRANDEDCONTCOST = "ucdustrandedcontcost";
    public static final String UCEQTAXESANDROYALTIES = "uceqtaxesandroyalties";
    public static final String UCCROSSSUBSIDYREMOVAL = "uccrosssubsidyremoval";
    public static final String ICCROSSSUBSIDY = "iccrosssubsidy";
    public static final String GRAM = "gram";
    public static final String TRANSSYSANCREFUND = "transsysancrefund";
    public static final String TRANSDEMANCREFUND = "transdemancrefund";
    public static final String EVATTRANSANCREFUND = "evattransancrefund";
    public static final String EVAT = "evat";
    public static final String EVATGENCO = "evatgenco";
    public static final String EVATTRANSCO = "evattransco";
    public static final String EVATSYSTEMLOSS = "evatsystemloss";
    public static final String FITALL = "fitall";
    
   

    private ReadandBillDatabaseHelper dbHelper;
    private String[] zAllColumns = {UCNPCSTRANDEDDEBTS,UCNPCSTRANDEDCONTCOST,
            UCDUSTRANDEDCONTCOST,UCEQTAXESANDROYALTIES,UCCROSSSUBSIDYREMOVAL,
            ICCROSSSUBSIDY,TRANSSYSANCREFUND,GRAM,TRANSDEMANCREFUND,EVATTRANSANCREFUND,
            EVAT,EVATGENCO,EVATTRANSCO,EVATSYSTEMLOSS,FITALL};

    public static List<String> ratesFields() {
        List<String> rateFields = com.generic.readandbill.database.RateDataSource.ratesFields();
        rateFields.set(rateFields.size()-1, rateFields.get(rateFields.size() - 1) + ", ");
        
        rateFields.add(RateDataSource.UCNPCSTRANDEDCONTCOST + " real not null, ");
        rateFields.add(RateDataSource.UCNPCSTRANDEDDEBTS + " real not null, ");
        rateFields.add(RateDataSource.UCDUSTRANDEDCONTCOST + " real not null, ");
        rateFields.add(RateDataSource.UCEQTAXESANDROYALTIES + " real not null, ");
        rateFields.add(RateDataSource.UCCROSSSUBSIDYREMOVAL + " real not null, ");
        rateFields.add(RateDataSource.ICCROSSSUBSIDY + " real not null, ");
        rateFields.add(RateDataSource.GRAM + " real not null, ");
        rateFields.add(RateDataSource.TRANSSYSANCREFUND + " real not null, ");
        rateFields.add(RateDataSource.TRANSDEMANCREFUND + " real not null, ");
        rateFields.add(RateDataSource.EVATTRANSANCREFUND + " real not null, ");
        rateFields.add(RateDataSource.EVAT + " real not null, ");
        rateFields.add(RateDataSource.EVATGENCO + " real not null, ");
        rateFields.add(RateDataSource.EVATTRANSCO + " real not null, ");
        rateFields.add(RateDataSource.EVATSYSTEMLOSS + " real not null, "); 
        rateFields.add(RateDataSource.FITALL +" real not null");
        return rateFields;
    }

    public RateDataSource(Context context) {
        super(new ReadandBillDatabaseHelper(context), context);
        dbHelper = new ReadandBillDatabaseHelper(context);
        allColumns = ArrayManager.concat(allColumns, zAllColumns);
    }

    private ContentValues rateContentValues(Rates rate){
        ContentValues values = super.rateContentValues((com.generic.readandbill.database.Rates) rate);
        
        values.put(UCNPCSTRANDEDDEBTS,
                rate.getUcNpcStrandedDebts());
        values.put(UCNPCSTRANDEDCONTCOST,
                rate.getUcNpcStrandedContCost());
        values.put(UCDUSTRANDEDCONTCOST,
                rate.getUcDuStrandedContCost());
        values.put(UCEQTAXESANDROYALTIES,
                rate.getUcEqTaxesAndRoyalties());
        values.put(UCCROSSSUBSIDYREMOVAL,
                rate.getUcCrossSubsidyRemoval());
        values.put(ICCROSSSUBSIDY,
                rate.getIcCrossSubsidy());
        values.put(TRANSSYSANCREFUND,
                rate.getTransSysAncRefund());
        values.put(GRAM, rate.getGram());
        values.put(TRANSDEMANCREFUND,
                rate.getTransDemAncRefund());
        values.put(EVATTRANSANCREFUND,
                rate.getEvatTransAncRefund());
        values.put(EVAT, rate.getEvat());
        values.put(EVATGENCO, rate.getEvatGenCo());
        values.put(EVATTRANSCO,
                rate.getEvatTransCo());
        values.put(EVATSYSTEMLOSS,
                rate.getEvatSystemLoss()); 
        values.put(FITALL, rate.getFitall());
        return values;
    }

    public Rates createRates(Rates rate) {
        db = dbHelper.getWritableDatabase();
        long insertId = db
                .insert(TABLE_RATES, null, rateContentValues(rate));
        Cursor cursor = db.query(TABLE_RATES, allColumns,
                ID + "=" + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Rates newRate = cursorToRate(cursor);
        cursor.close();
        db.close();
        return newRate;
    }

    public Rates getRate(String rateCode) {
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

    protected Rates cursorToRate(Cursor cursor){
        Rates rate = new Rates();
        if (cursor.getCount() != 0){
            rate = (Rates) super.cursorToRate(cursor, rate);            
            rate.setUcNpcStrandedDebts(cursor.getDouble(cursor.getColumnIndex(UCNPCSTRANDEDDEBTS)));
            rate.setUcNpcStrandedContCost(cursor.getDouble(cursor.getColumnIndex(UCNPCSTRANDEDCONTCOST)));
            rate.setUcDuStrandedContCost(cursor.getDouble(cursor.getColumnIndex(UCDUSTRANDEDCONTCOST)));
            rate.setUcEqTaxesAndRoyalties(cursor.getDouble(cursor.getColumnIndex(UCEQTAXESANDROYALTIES)));
            rate.setUcCrossSubsidyRemoval(cursor.getDouble(cursor.getColumnIndex(UCCROSSSUBSIDYREMOVAL)));
            rate.setIcCrossSubsidy(cursor.getDouble(cursor.getColumnIndex(ICCROSSSUBSIDY)));
            rate.setTransSysAncRefund(cursor.getDouble(cursor.getColumnIndex(TRANSSYSANCREFUND)));
            rate.setGram(cursor.getDouble(cursor.getColumnIndex(GRAM)));
            rate.setTransDemAncRefund(cursor.getDouble(cursor.getColumnIndex(TRANSDEMANCREFUND)));
            rate.setEvatTransAncRefund(cursor.getDouble(cursor.getColumnIndex(EVATTRANSANCREFUND)));
            rate.setEvat(cursor.getDouble(cursor.getColumnIndex(EVAT)));
            rate.setEvatGenCo(cursor.getDouble(cursor.getColumnIndex(EVATGENCO)));
            rate.setEvatTransCo(cursor.getDouble(cursor.getColumnIndex(EVATTRANSCO)));
            rate.setEvatSystemLoss(cursor.getDouble(cursor.getColumnIndex(EVATSYSTEMLOSS)));
            rate.setFitall(cursor.getDouble(cursor.getColumnIndex(FITALL)));
        }
        return rate;
    }

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_RATES + "(";
        for (String s : ratesFields()){
            result += s;
        }
        result += ");";
        return result;
    }

    public void refreshRates(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_RATES);
        db.execSQL(DATABASE_CREATE());
        db.close();

    }
}
