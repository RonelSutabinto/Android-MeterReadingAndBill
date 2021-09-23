package com.zaneco.readandbill.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NewConnectionDataSource {

	public static final String TABLE_NEWCON = "newconnection";
	
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String SERIAL = "serial";
	public static final String READING = "reading";
	public static final String REMARKS = "remarks";
	public static final String TRANSACTIONDATE = "transactiondate";
	public static final String ROUTE = "route";
	public static final String STATE = "state";
	
	private SQLiteDatabase db;
	private ReadandBillDatabaseHelper dbHelper;

	private String[] allColumns = { ID,NAME,SERIAL,READING,REMARKS,TRANSACTIONDATE,ROUTE,STATE };

    public static List<String> newConnectionFields(){
        List<String> newConnectionFields = new ArrayList<String>();
        newConnectionFields.add(ID + " integer primary key autoincrement, ");
        newConnectionFields.add(NAME + " text not null, ");
        newConnectionFields.add(SERIAL + " text not null, ");
        newConnectionFields.add(READING + " real not null, ");
        newConnectionFields.add(REMARKS + " text not null, ");
        newConnectionFields.add(TRANSACTIONDATE + " integer not null, ");
        newConnectionFields.add(ROUTE + " text not null, ");
        newConnectionFields.add(STATE + " integer not null");
        return newConnectionFields;
    }

	public NewConnectionDataSource(Context context) {
		dbHelper = new ReadandBillDatabaseHelper(context);
	}

    protected ContentValues newConnectionValues(NewConnection newConnection){
        ContentValues values = new ContentValues();
        values.put(NAME, newConnection.getName());
        values.put(SERIAL, newConnection.getSerial());
        values.put(READING, newConnection.getReading().toString());
        values.put(REMARKS, newConnection.getRemarks());
        values.put(TRANSACTIONDATE, newConnection.getTransactionDate().toString());
        values.put(ROUTE, newConnection.getRoute());
        values.put(STATE, newConnection.getState().toString());
        return values;
    }

	public NewConnection createNewCon(NewConnection newCon){
		db = dbHelper.getWritableDatabase();
		Long insertId = db.insert(TABLE_NEWCON, null, newConnectionValues(newCon));
		db.close();
		return getNewCon(insertId);
	}

    public NewConnection getNewCon(Long id){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NEWCON, allColumns, ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        NewConnection newCon = cursorToNewCon(cursor);
        cursor.close();
        db.close();
        return newCon;
    }

	public void deleteNewCon(NewConnection newCon){
		db = dbHelper.getWritableDatabase();
		Long id = newCon.getId();
		db.delete(TABLE_NEWCON, ID + "=" + id, null);
		db.close();
	}

	public void updateNewCon(NewConnection newCon){
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, newCon.getName());
		values.put(SERIAL, newCon.getSerial());
		values.put(READING, newCon.getReading().toString());
		values.put(REMARKS, newCon.getRemarks());
		values.put(TRANSACTIONDATE, newCon.getTransactionDate().toString());
		values.put(ROUTE, newCon.getRoute());
		values.put(STATE, newCon.getState().toString());
		db.update(TABLE_NEWCON, values, ID + "=" + newCon.getId(), null);
		db.close();
	}

	public List<NewConnection> getAllNewCon(){
		db = dbHelper.getReadableDatabase();
		List<NewConnection> newCons = new ArrayList<NewConnection>();
		Cursor cursor = db.query(TABLE_NEWCON, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			newCons.add(cursorToNewCon(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return newCons;
	}

	private NewConnection cursorToNewCon(Cursor cursor) {
		NewConnection newCon = new NewConnection();
		if (cursor.getCount() != 0){
			newCon.setId(cursor.getLong(0));
			newCon.setName(cursor.getString(1));
			newCon.setSerial(cursor.getString(2));
			newCon.setReading(cursor.getDouble(3));
			newCon.setRemarks(cursor.getString(4));
			newCon.setTransactionDate(cursor.getLong(5));
			newCon.setRoute(cursor.getString(6));
			newCon.setState(cursor.getInt(7));
		}
		return newCon;
	}

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_NEWCON + "(";
        for (String s : newConnectionFields()){
            result += s;
        }
        result += "); ";
        return result;
    }

	public void refreshNewConnection(){
		db = dbHelper.getWritableDatabase();
		db.execSQL("drop table if exists " + TABLE_NEWCON);
        db.execSQL(DATABASE_CREATE());
		db.close();
	}
}
