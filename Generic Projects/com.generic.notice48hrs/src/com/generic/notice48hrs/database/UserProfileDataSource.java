package com.generic.notice48hrs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDataSource {

    public static final String TABLE_USER_PROFILE = "userprofile";

    public static final String NAME = "name";
    
	protected SQLiteDatabase db;
	protected Notice48hrsDatabaseHelper dbHelper;

	protected String[] allColumns = { NAME };

	public UserProfileDataSource(Context context) {
		dbHelper = new Notice48hrsDatabaseHelper(context);
	}

    protected static List<String> userProfileFields() {
        List<String> userProfileFields = new ArrayList<String>();
        userProfileFields.add(UserProfileDataSource.NAME + " text not null");
        return userProfileFields;
    }

    protected ContentValues userProfileValues(UserProfile userProfile){
        ContentValues values = new ContentValues();
        values.put(NAME, userProfile.getName());
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
		UserProfile nuUp = cursorToUserProfile(cursor, new UserProfile());
		cursor.close();
		db.close();
		return nuUp;
	}

	protected UserProfile cursorToUserProfile(Cursor cursor, UserProfile userProfile) {
		if (cursor.getCount() != 0) {
			userProfile.setName(cursor.getString(cursor.getColumnIndex(NAME)));
		}
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

    public void refresh(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + UserProfileDataSource.TABLE_USER_PROFILE);
        db.execSQL(userProfileCreate());
        db.close();
    }
}
