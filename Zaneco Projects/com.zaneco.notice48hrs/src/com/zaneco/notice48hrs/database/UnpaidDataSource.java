package com.zaneco.notice48hrs.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UnpaidDataSource {

    public static final String TABLE_UNPAID = "unpaid";

    public static final String ID = "_id";
    public static final String CODE = "code";
    public static final String BILLMONTH = "billmonth";
    public static final String AMOUNT = "amount";
    
	protected SQLiteDatabase db;
	protected com.generic.notice48hrs.database.Notice48hrsDatabaseHelper dbHelper;
	private String[] allColumns = { ID,
			CODE,
			BILLMONTH,
			AMOUNT };

	public UnpaidDataSource(Context context) {
		dbHelper = new com.generic.notice48hrs.database.Notice48hrsDatabaseHelper(context);
	}

    private ContentValues unpaidValues(Unpaid unpaid){
        ContentValues values = new ContentValues();
        values.put(CODE, unpaid.getCode());
        values.put(BILLMONTH,
                unpaid.getBillMonth());
        values.put(AMOUNT, unpaid.getAmount());
        return  values;
    }

	public Unpaid createUnpaid(Unpaid unpaid) {
		db = dbHelper.getWritableDatabase();

		long insertedId = db.insert(TABLE_UNPAID,
				null, unpaidValues(unpaid));
		Cursor cursor = db.query(TABLE_UNPAID,
				allColumns, ID + "=" + insertedId,
				null, null, null, null);
		cursor.moveToFirst();
		Unpaid unpb = cursorToUnpaid(cursor);
		cursor.close();
		db.close();
		return unpb;
	}

	public List<Unpaid> getConsumersUnpaid(Consumer con) {
		List<Unpaid> unpbs = new ArrayList<Unpaid>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_UNPAID,
				allColumns,
				CODE + " = " + con.getCode(),
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			unpbs.add(cursorToUnpaid(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return unpbs;
	}

	public Double getArrear(Long consumerId) {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT SUM(" + AMOUNT +  ") FROM "
				+ TABLE_UNPAID + " WHERE "
				+ CODE + " = " + consumerId, null);
		cursor.moveToFirst();
		db.close();
		if (cursor.getCount() == 0){
			return 0.0;
		} else {
			return cursor.getDouble(0);
		}		
	}

	private Unpaid cursorToUnpaid(Cursor cursor) {
		Unpaid unpb = new Unpaid();
		if (cursor.getCount() != 0) {
			unpb.setId(cursor.getLong(0));
			unpb.setCode(cursor.getLong(1));
			unpb.setBillMonth(cursor.getString(2));
			unpb.setAmount(cursor.getDouble(3));
		}
		return unpb;
	}

	public void truncate() {
		db = dbHelper.getWritableDatabase();
		dbHelper.refreshUnpaid(db);
		db.close();
	}
}
