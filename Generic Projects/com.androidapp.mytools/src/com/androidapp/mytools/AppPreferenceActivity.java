package com.androidapp.mytools;

import android.content.Context;
import android.preference.*;
import com.androidapp.mytools.bluetooth.BluetoothSharedPrefs;

import android.os.Bundle;

public class AppPreferenceActivity extends PreferenceActivity {

    private Context context;
    private ListPreference listPreference;
    private PreferenceCategory pref2;
    private CheckBoxPreference checkBoxPreference;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*getPreferenceManager().setSharedPreferencesName(BluetoothSharedPrefs.PREFS_NAME);
		addPreferencesFromResource(R.xml.preference);                                    */
        context = this;
        addPreferencesFromResource(R.xml.preference);

        listPreference = (ListPreference) findPreference("bluetooth_mac");
        pref2 = (PreferenceCategory) findPreference("pref2");
        checkBoxPreference = (CheckBoxPreference) findPreference("bluetooth_always_on");

        if (listPreference.getValue()!=null)
            if (listPreference.getEntry() == null)
                listPreference.setSummary("");
            else
                listPreference.setSummary(listPreference.getEntry().toString());

        checkBoxPreference.setChecked(BluetoothSharedPrefs.getBluetoothAlwaysOn(getBaseContext()));
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listPreference.setValue(newValue.toString());
                BluetoothSharedPrefs.setMacAddress(getBaseContext(),newValue.toString());
                preference.setSummary(listPreference.getEntry());
                return true;
            }
        });

        checkBoxPreference.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                checkBoxPreference.setChecked((Boolean) newValue);
                BluetoothSharedPrefs.setBluetoothAlwaysOn(getBaseContext(),checkBoxPreference.isChecked());
                return true;
            }
        });

	}

	@Override
	public void finish() {
		if (BluetoothSharedPrefs.getMacAddress(this) != null){
            BluetoothSharedPrefs.setDeviceName(context, listPreference.getEntry().toString());
			setResult(RESULT_OK);
		} else
			setResult(RESULT_CANCELED);
		super.finish();
	}

	
}
