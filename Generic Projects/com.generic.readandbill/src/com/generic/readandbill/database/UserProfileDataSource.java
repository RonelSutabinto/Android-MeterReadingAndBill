package com.generic.readandbill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDataSource {

    public static final String TABLE_USER_PROFILE = "userprofile";

    public static final String ID = "_id";
    public static final String NAME = "name";

    protected SQLiteDatabase db;
    protected ReadandBillDatabaseHelper dbHelper;
    protected String[] allColumns = { ID,
            NAME};

    public static List<String> userProfileFields(){
        List<String> userProfileFields = new ArrayList<String>();
        userProfileFields.add(UserProfileDataSource.ID + " integer primary key autoincrement, ");
        userProfileFields.add(UserProfileDataSource.NAME + " text not null");
        return userProfileFields;
    }

    public UserProfileDataSource(Context context) {
        dbHelper = new ReadandBillDatabaseHelper(context);
    }
    public UserProfileDataSource(ReadandBillDatabaseHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        if (this.dbHelper == null)dbHelper = new ReadandBillDatabaseHelper(context);
    }

    protected ContentValues upValues(UserProfile userProfile){
        ContentValues values = new ContentValues();
        values.put(NAME, userProfile.getName());
        return values;
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_USER_PROFILE,
                null, upValues(userProfile));
        Cursor cursor = db.query(TABLE_USER_PROFILE,
                allColumns, ID + "="
                + insertId, null, null, null, null);
        cursor.moveToFirst();
        UserProfile newUserProfile = cursorToUserProfile(cursor, new UserProfile());
        cursor.close();
        db.close();
        return newUserProfile;
    }

    public UserProfile getUserProfile() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        UserProfile userProfile = cursorToUserProfile(cursor, new UserProfile());
        db.close();
        return userProfile;
    }

    protected UserProfile cursorToUserProfile(Cursor cursor, UserProfile userProfile) {
        if (cursor.getCount() > 0){
            userProfile.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            userProfile.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        }
        return userProfile;
    }

    public void truncate(){
        db = dbHelper.getWritableDatabase();
        dbHelper.refreshTable(db, TABLE_USER_PROFILE, userProfileFields());
        db.close();
    }
}
