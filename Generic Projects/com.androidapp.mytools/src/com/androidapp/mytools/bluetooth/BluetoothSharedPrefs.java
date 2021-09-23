package com.androidapp.mytools.bluetooth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BluetoothSharedPrefs {
	public final static String PREFS_NAME = "readandbill_prefs";
    private String deviceName;
    
    public static  String getDeviceName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return  prefs.getString("bluetooth_name", null);
    }

    public static void setDeviceName(Context context, String newValue){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Editor prefEditor = prefs.edit();
        prefEditor.putString("bluetooth_name", newValue);
        prefEditor.commit();
    }

	public static String getMacAddress(Context context){
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString("bluetooth_mac", null);
	}
	
	public static void setMacAddress(Context context, String newValue){
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditior = prefs.edit();
		prefsEditior.putString("bluetooth_mac", newValue);
		prefsEditior.commit();
	}

    public static boolean getBluetoothAlwaysOn(Context context){
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getBoolean("bluetooth_always_on", false);
	}

	public static void setBluetoothAlwaysOn(Context context, boolean newValue){
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditior = prefs.edit();
		prefsEditior.putBoolean("bluetooth_always_on", newValue);
		prefsEditior.commit();
	}
}
