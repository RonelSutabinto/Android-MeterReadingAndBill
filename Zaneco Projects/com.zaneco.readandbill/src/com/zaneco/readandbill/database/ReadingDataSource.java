package com.zaneco.readandbill.database;

import android.content.Context;

/**
 * Created by Erwin on 2/22/14.
 */
public class ReadingDataSource extends com.generic.readandbill.database.ReadingDataSource {
    private ReadandBillDatabaseHelper dbHelper;
    public ReadingDataSource(Context context) {
        super(new ReadandBillDatabaseHelper(context), context);
        dbHelper = new ReadandBillDatabaseHelper(context);
    }
}
