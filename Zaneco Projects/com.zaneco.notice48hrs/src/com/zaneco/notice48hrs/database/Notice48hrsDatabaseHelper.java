package com.zaneco.notice48hrs.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Erwin
 * Date: 9/29/13
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Notice48hrsDatabaseHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "Notice.db";
    protected static final int DATABASE_VERSION = 1;

    public Notice48hrsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
