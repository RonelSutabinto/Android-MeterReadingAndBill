package com.generic.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateDataSource {

    public static final String TABLE_RATES = "rates";

    public static final String ID = "_id";
    public static final String RATECODE = "ratecode";
    public static final String GENSYS = "gensys";
    public static final String HOSTCOMM = "hostcomm";
    public static final String SYSTEMLOSS = "systemloss";
    public static final String PARR = "parr";
    public static final String TCDEMAND = "tcdemand";
    public static final String TCSYSTEM = "tcsystem";
    public static final String DCDEMAND = "dcdemand";
    public static final String DCSYSTEM = "dcsystem";
    public static final String SCRETAILCUST = "scretailcust";
    public static final String SCSUPPLYSYS = "scsupplysys";
    public static final String MCRETAILCUST = "mcretailcust";
    public static final String MCSYS = "mcsys";
    public static final String LIFELINESUBSIDY = "lifelinesubsidy";
    public static final String SCSWITCH = "scswitch";
    public static final String SENIORCITIZENSUBSIDY = "seniorcitizensubsidy";
    public static final String SCKILOWATTHOURLIMIT = "sckilowatthourlimit";
    public static final String SENIORCITIZENDISCOUNT = "seniorcitizendiscount";
    public static final String REINVESTMENTFUNDSUSTCAPEX = "reinvestmentfundsustcapex";
    public static final String PREVYEARADJPOWERCOST = "prevyearadjpowercost";
    public static final String UCME = "ucme";
    public static final String UCEC = "ucec";    
    public static final String FOREX = "forex";    
    public static final String OTHERGENRATEADJ = "OtherGenRateAdj";
    public static final String OTHERTRANSADJ = "OtherTransCostAdj";
    public static final String OTHERTRANSDEMANDADJ ="OtherTransDemandCostAdj";
    public static final String OTHERSYSTEMLOSSADJ ="OtherSystemLossCostAdj";
    public static final String OTHERLIFELINERATEADJ = "OtherLifelineRateCostAdj";
    public static final String OTHERSENIORCITIZENADJ = "OtherSeniorCitizenRateAdj";  
    public static final String PARECOVERY = "PArecovery";
    public static final String FINALVAT = "Finalvat";
    public static final String WITHHOLDINGTAX = "Withholdingtax";
    public static final String RENTALWITLHOLDING = "RentalWithholding";
    
    protected SQLiteDatabase db;
    protected ReadandBillDatabaseHelper dbHelper;
    protected String[] allColumns = { ID,
            RATECODE,
            GENSYS,
            HOSTCOMM,
            TCDEMAND,
            TCSYSTEM,
            SYSTEMLOSS,
            DCDEMAND,
            DCSYSTEM,
            SCRETAILCUST,
            SCSUPPLYSYS,
            MCRETAILCUST,
            MCSYS,
            UCME,
            UCEC,
            PARR,
            LIFELINESUBSIDY,
            PREVYEARADJPOWERCOST,
            REINVESTMENTFUNDSUSTCAPEX,
            SCSWITCH,
            SCKILOWATTHOURLIMIT,
            SENIORCITIZENSUBSIDY,
            SENIORCITIZENDISCOUNT,
            FOREX,
            OTHERGENRATEADJ,
            OTHERTRANSADJ,
            OTHERTRANSDEMANDADJ,
            OTHERSYSTEMLOSSADJ,
            OTHERLIFELINERATEADJ,
            OTHERSENIORCITIZENADJ,
            PARECOVERY,
            FINALVAT,
            WITHHOLDINGTAX,
            RENTALWITLHOLDING};

    public static List<String> ratesFields(){
        List<String> rateFields = new ArrayList<String>();
        rateFields.add(RateDataSource.ID + " integer primary key autoincrement, ");
        rateFields.add(RateDataSource.RATECODE + " text not null, ");
        rateFields.add(RateDataSource.GENSYS + " real not null, ");
        rateFields.add(RateDataSource.HOSTCOMM + " real not null, ");
        rateFields.add(RateDataSource.SYSTEMLOSS + " real not null, ");
        rateFields.add(RateDataSource.PARR + " real not null, ");
        rateFields.add(RateDataSource.TCDEMAND + " real not null, ");
        rateFields.add(RateDataSource.TCSYSTEM + " real not null, ");
        rateFields.add(RateDataSource.DCDEMAND + " real not null, ");
        rateFields.add(RateDataSource.DCSYSTEM + " real not null, ");
        rateFields.add(RateDataSource.SCRETAILCUST + " real not null, ");
        rateFields.add(RateDataSource.SCSUPPLYSYS + " real not null, ");
        rateFields.add(RateDataSource.MCRETAILCUST + " real not null, ");
        rateFields.add(RateDataSource.MCSYS + " real not null, ");
        rateFields.add(RateDataSource.LIFELINESUBSIDY + " real not null, ");
        rateFields.add(RateDataSource.SCSWITCH + " real not null, ");
        rateFields.add(RateDataSource.SENIORCITIZENSUBSIDY + " real not null, ");
        rateFields.add(RateDataSource.SCKILOWATTHOURLIMIT + " real not null, ");
        rateFields.add(RateDataSource.SENIORCITIZENDISCOUNT + " real not null, ");
        rateFields.add(RateDataSource.REINVESTMENTFUNDSUSTCAPEX + " real not null, ");
        rateFields.add(RateDataSource.PREVYEARADJPOWERCOST + " real not null, ");
        rateFields.add(RateDataSource.UCME + " real not null, ");
        rateFields.add(RateDataSource.UCEC + " real not null, ");
        rateFields.add(RateDataSource.FOREX + " real not null, ");
        rateFields.add(RateDataSource.OTHERGENRATEADJ + " real not null, ");
        rateFields.add(RateDataSource.OTHERTRANSADJ + " real not null, ");
        rateFields.add(RateDataSource.OTHERTRANSDEMANDADJ + " real not null, ");
        rateFields.add(RateDataSource.OTHERSYSTEMLOSSADJ + " real not null, ");
        rateFields.add(RateDataSource.OTHERLIFELINERATEADJ + " real not null, ");
        rateFields.add(RateDataSource.OTHERSENIORCITIZENADJ + " real not null,");
        rateFields.add(RateDataSource.PARECOVERY+ " real not null, ");
        rateFields.add(RateDataSource.FINALVAT + " real not null, ");
        rateFields.add(RateDataSource.WITHHOLDINGTAX + " real not null, ");
        rateFields.add(RateDataSource.RENTALWITLHOLDING + " real not null ");
        return rateFields;
    }

    public RateDataSource(Context context) {
        dbHelper = new ReadandBillDatabaseHelper(context);
    }

    public RateDataSource(ReadandBillDatabaseHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        if (this.dbHelper == null) new RateDataSource(context);
    }

    protected ContentValues rateContentValues(Rates rate) {
        ContentValues values = new ContentValues();
        values.put(RATECODE, rate.getRateCode());
        values.put(GENSYS, rate.getGenSys());
        values.put(HOSTCOMM, rate.getHostComm());
        values.put(TCDEMAND, rate.getTcDemand());
        values.put(TCSYSTEM, rate.getTcSystem());
        values.put(SYSTEMLOSS, rate.getSystemLoss());
        values.put(DCDEMAND, rate.getDcDemand());
        values.put(DCSYSTEM,
                rate.getDcDistribution());
        values.put(SCRETAILCUST,
                rate.getScRetailCust());
        values.put(SCSUPPLYSYS,
                rate.getScSupplySys());
        values.put(MCRETAILCUST,
                rate.getMcRetailCust());
        values.put(MCSYS, rate.getMcSys());
        values.put(UCME, rate.getUcme());
        values.put(UCEC, rate.getUcec());
        values.put(PARR, rate.getParr());
        values.put(LIFELINESUBSIDY,
                rate.getLifeLineSubsidy());
        values.put(PREVYEARADJPOWERCOST,
                rate.getPrevYearAdjPowerCost());
        values.put(REINVESTMENTFUNDSUSTCAPEX,
                rate.getReinvestmentFundSustCapex());
        values.put(SCSWITCH, rate.getScSwitch());
        values.put(SCKILOWATTHOURLIMIT,
                rate.getScKiloWattHourLimit());
        values.put(SENIORCITIZENSUBSIDY,
                rate.getSeniorCitizenSubsidy());
        values.put(SENIORCITIZENDISCOUNT,
                rate.getSeniorCitizenDiscount());
        values.put(FOREX, rate.getForex());
        values.put(OTHERGENRATEADJ, 
        		rate.getOtherGenRateAdj());
        values.put(OTHERTRANSADJ, 
        		rate.getOtherTransCostAdj());
        values.put(OTHERTRANSDEMANDADJ, 
        		rate.getOtherTransDemandCostAdj());
        values.put(OTHERSYSTEMLOSSADJ, 
        		rate.getOtherSystemLossCostAdj());
        values.put(OTHERLIFELINERATEADJ, 
        		rate.getOtherLifelineRateCostAdj());
        values.put(OTHERSENIORCITIZENADJ, 
        		rate.getOtherSeniorCitizenRateAdj());
        values.put(PARECOVERY, rate.getPArecovery());
        values.put(FINALVAT,rate.getFinalvat());
        values.put(WITHHOLDINGTAX,rate.getWithholdingtax());
        values.put(RENTALWITLHOLDING,rate.getRentalWitholding());
        return values;
    }

    public Rates createRate(Rates rate){
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_RATES,null, rateContentValues(rate));
        db.close();
        return getRate(insertId);
    }

    public Rates getRate(String rateCode) {
        db = dbHelper.getReadableDatabase();
        Rates myRate = new Rates();
        Cursor cursor = db.query(TABLE_RATES, allColumns,
                RATECODE + "='" + rateCode + "'",
                null, null, null, null);
        cursor.moveToFirst();
        myRate = cursorToRate(cursor, myRate);
        cursor.close();
        db.close();
        return myRate;
    }

    public Rates getRate(long id) {
        db = dbHelper.getReadableDatabase();
        Rates myRate = new Rates();
        Cursor cursor = db.query(TABLE_RATES, allColumns,
                ID + "=" + ID,
                null, null, null, null);
        cursor.moveToFirst();
        myRate = cursorToRate(cursor, myRate);
        cursor.close();
        db.close();
        return myRate;
    }

    protected Rates cursorToRate(Cursor cursor, Rates rate) {
        if (cursor.getCount() != 0) {
            rate.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            rate.setRateCode(cursor.getString(cursor.getColumnIndex(RATECODE)));
            rate.setGenSys(cursor.getDouble(cursor.getColumnIndex(GENSYS)));
            rate.setHostComm(cursor.getDouble(cursor.getColumnIndex(HOSTCOMM)));
            rate.setTcDemand(cursor.getDouble(cursor.getColumnIndex(TCDEMAND)));
            rate.setTcSystem(cursor.getDouble(cursor.getColumnIndex(TCSYSTEM)));
            rate.setSystemLoss(cursor.getDouble(cursor.getColumnIndex(SYSTEMLOSS)));
            rate.setDcDemand(cursor.getDouble(cursor.getColumnIndex(DCDEMAND)));
            rate.setDcDistribution(cursor.getDouble(cursor.getColumnIndex(DCSYSTEM)));
            rate.setScRetailCust(cursor.getDouble(cursor.getColumnIndex(SCRETAILCUST)));
            rate.setScSupplySys(cursor.getDouble(cursor.getColumnIndex(SCSUPPLYSYS)));
            rate.setMcRetailCust(cursor.getDouble(cursor.getColumnIndex(MCRETAILCUST)));
            rate.setMcSys(cursor.getDouble(cursor.getColumnIndex(MCSYS)));
            rate.setUcme(cursor.getDouble(cursor.getColumnIndex(UCME)));
            rate.setUcec(cursor.getDouble(cursor.getColumnIndex(UCEC)));
            rate.setParr(cursor.getDouble(cursor.getColumnIndex(PARR)));
            rate.setLifeLineSubsidy(cursor.getDouble(cursor.getColumnIndex(LIFELINESUBSIDY)));
            rate.setPrevYearAdjPowerCost(cursor.getDouble(cursor.getColumnIndex(PREVYEARADJPOWERCOST)));
            rate.setReinvestmentFundSustCapex(cursor.getDouble(cursor.getColumnIndex(REINVESTMENTFUNDSUSTCAPEX)));
            rate.setScSwitch(cursor.getLong(cursor.getColumnIndex(SCSWITCH)) > 0);
            rate.setScKiloWattHourLimit(cursor.getDouble(cursor.getColumnIndex(SCKILOWATTHOURLIMIT)));
            rate.setSeniorCitizenSubsidy(cursor.getDouble(cursor.getColumnIndex(SENIORCITIZENSUBSIDY)));
            rate.setSeniorCitizenDiscount(cursor.getDouble(cursor.getColumnIndex(SENIORCITIZENDISCOUNT)));
            rate.setForex(cursor.getDouble(cursor.getColumnIndex(FOREX)));
            rate.setOtherGenRateAdj(cursor.getDouble(cursor.getColumnIndex(OTHERGENRATEADJ)));
            rate.setOtherTransCostAdj(cursor.getDouble(cursor.getColumnIndex(OTHERTRANSADJ)));
            rate.setOtherTransDemandCostAdj(cursor.getDouble(cursor.getColumnIndex(OTHERTRANSDEMANDADJ)));
            rate.setOtherSystemLossCostAdj(cursor.getDouble(cursor.getColumnIndex(OTHERSYSTEMLOSSADJ)));
            rate.setOtherLifelineRateCostAdj(cursor.getDouble(cursor.getColumnIndex(OTHERLIFELINERATEADJ)));
            rate.setOtherSeniorCitizenRateAdj(cursor.getDouble(cursor.getColumnIndex(OTHERSENIORCITIZENADJ)));
            rate.setPArecovery(cursor.getDouble(cursor.getColumnIndex(PARECOVERY)));
            rate.setFinalvat(cursor.getDouble(cursor.getColumnIndex(FINALVAT)));
            rate.setWithholdingtax(cursor.getDouble(cursor.getColumnIndex(WITHHOLDINGTAX)));
            rate.setRentalWitholding(cursor.getDouble(cursor.getColumnIndex(RENTALWITLHOLDING)));
        } else {
            rate = null;
        }
        return rate;
    }

    public void truncate(){
        db = dbHelper.getWritableDatabase();
        dbHelper.refreshTable(db, TABLE_RATES, ratesFields());
        db.close();
    }

}
