package com.zaneco.readandbill.database;

import com.androidapp.mytools.objectmanager.ArrayManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class UserProfileDataSource extends com.generic.readandbill.database.UserProfileDataSource {

    public static final String BILLMONTH = "billmonth";
    public static final String TELNO = "telno";

    private ReadandBillDatabaseHelper dbHelper;
    private String[] zAllColumns = { BILLMONTH,TELNO };

    public static List<String> userProfileFields() {
        List<String> userProfileFields = com.generic.readandbill.database.
                UserProfileDataSource.userProfileFields();
        userProfileFields.set(userProfileFields.size() - 1, userProfileFields.get(
                userProfileFields.size() - 1) + ", ");
        userProfileFields.add(BILLMONTH + " text not null,");
        userProfileFields.add(TELNO + " text not null ");
        return userProfileFields;
    }

    public UserProfileDataSource(Context context) {
        super(new ReadandBillDatabaseHelper(context), context);
        dbHelper = new ReadandBillDatabaseHelper(context);
        allColumns = ArrayManager.concat(allColumns, zAllColumns);
    }

    public ContentValues upValues(UserProfile userProfile){
        ContentValues values = super.upValues(userProfile);
        values.put(BILLMONTH, userProfile.getBillmonth());
        values.put(TELNO, userProfile.getTelno());
        return values;
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        db = dbHelper.getWritableDatabase();
        db.insert(TABLE_USER_PROFILE,null, upValues(userProfile));
        db.close();
        return getUserProfile();
    }

    public UserProfile getUserProfile() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        UserProfile userProfile = cursorToUserProfile(cursor);
        db.close();
        return userProfile;
    }

    protected UserProfile cursorToUserProfile(Cursor cursor) {
        UserProfile userProfile = new UserProfile();
        if (cursor.getCount() > 0){
            userProfile = (UserProfile) super.cursorToUserProfile(cursor, userProfile);
            userProfile.setBillmonth(cursor.getString(cursor.getColumnIndex(BILLMONTH)));
            userProfile.setTelno(cursor.getString(cursor.getColumnIndex(TELNO)));
        }
        return userProfile;
    }

    public static final String DATABASE_CREATE(){
        String result = "create table " + TABLE_USER_PROFILE + "(";
        for (String s : userProfileFields()){
            result += s;
        }
        result += ");";
        return result;
    }

    public void refreshUserProfile(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_USER_PROFILE);
        db.execSQL(DATABASE_CREATE());
        db.close();
    }

}
