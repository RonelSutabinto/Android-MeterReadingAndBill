package com.zaneco.notice48hrs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.androidapp.mytools.objectmanager.ArrayManager;

import java.util.List;

public class UserProfileDataSource extends com.generic.notice48hrs.database.UserProfileDataSource{

    public static final String ADDRESS = "address";
    public static final String ADDRESSB = "addressB";

    private String[] zAllColumns = { ADDRESS,ADDRESSB };

    public UserProfileDataSource(Context context) {
        super(context);
        allColumns = ArrayManager.concat(allColumns, zAllColumns);
    }

    protected static List<String> userProfileFields() {
        List<String> userProfileFields = com.generic.notice48hrs.database.UserProfileDataSource.userProfileFields();
        userProfileFields.set(userProfileFields.size() - 1, userProfileFields.get(userProfileFields.size() - 1) + ", ");
        userProfileFields.add(UserProfileDataSource.ADDRESS + " text not null,");
        userProfileFields.add(UserProfileDataSource.ADDRESSB + " text not null ");
        return userProfileFields;
    }

    protected ContentValues userProfileValues(UserProfile userProfile){
        ContentValues values = super.userProfileValues(userProfile);
        values.put(ADDRESS, userProfile.getAddress());
        values.put(ADDRESSB, userProfile.getAddressB());
        return  values;
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        db = dbHelper.getWritableDatabase();
        long insertId = db.insert(TABLE_USER_PROFILE,
                null, userProfileValues(userProfile));
        db.close();
        return getUserProfile();
    }

    public UserProfile getUserProfile() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        UserProfile nuUp = cursorToUserProfile(cursor);
        cursor.close();
        db.close();
        return nuUp;
    }

    protected UserProfile cursorToUserProfile(Cursor cursor) {
        UserProfile userProfile = new UserProfile();
        if (cursor.getCount() != 0){
            userProfile.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
            userProfile.setAddressB(cursor.getString(cursor.getColumnIndex(ADDRESSB)));
        }
        return userProfile;
    }

    public UserProfile arrayToUserProfile(String[] rawData){
        UserProfile userProfile = new UserProfile();
        userProfile.setName(rawData[0]);
        userProfile.setAddress(rawData[1]);
      
        return userProfile;
    }

    protected static String userProfileCreate() {
        String result = "create table "
                + UserProfileDataSource.TABLE_USER_PROFILE + "(";
        for (String string : userProfileFields())
            result += string;
        result += "); ";
        return  result;
    }

    @Override
    public void refresh(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + UserProfileDataSource.TABLE_USER_PROFILE);
        db.execSQL(userProfileCreate());
        db.close();
    }
}
