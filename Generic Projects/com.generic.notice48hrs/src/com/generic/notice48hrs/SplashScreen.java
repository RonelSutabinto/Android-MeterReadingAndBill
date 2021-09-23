package com.generic.notice48hrs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.androidapp.mytools.AppPreferenceActivity;
import com.androidapp.mytools.bluetooth.BluetoothSharedPrefs;
import com.androidapp.mytools.bluetooth.MyPrinter;
import com.androidapp.mytools.bluetooth.PrinterControls;
import com.generic.notice48hrs.database.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends Activity {

	protected ConsumerDataSource dsConsumer;
	protected UnpaidDataSource dsUnpaid;
	protected UserProfileDataSource dsUserProfile;

	public static MyPrinter btPrinter;

	protected static final int NOTHING = 0;
	protected static final int CONSUMER_TABLE = 10;
	protected static final int UNPAID_TABLE = 20;
	protected static final int USER_PROFILE_TABLE = 30;
	protected static final int PREFCODE = 40;

    protected final String filePathInFile = Environment.getExternalStorageDirectory()
            + "/Notice48/InFiles/";
    protected final String filePathOutFile = Environment.getExternalStorageDirectory()
            + "/Notice48/OutFiles/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
        createDirIfNotExists(filePathInFile);
		createDirIfNotExists(filePathOutFile);
		
		btPrinter = new MyPrinter(this);
		if (BluetoothSharedPrefs.getMacAddress(this) != null) {
            btPrinter.setMacAddress(BluetoothSharedPrefs.getMacAddress(this));
            btPrinter.setDeviceName(BluetoothSharedPrefs.getDeviceName(this));
            btPrinter.setAlwaysOn(BluetoothSharedPrefs.getBluetoothAlwaysOn(this));
            PrinterControls.btPrinter = btPrinter;
		}
		
		dsConsumer = new ConsumerDataSource(this);
		dsUnpaid = new UnpaidDataSource(this);
		dsUserProfile = new UserProfileDataSource(this);
	}

	private boolean createDirIfNotExists(String path) {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				Log.e(this.getClass().getName(),
						"Problem creating folder or SD Card not present");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = true;
		if (item.getItemId() == R.id.issProcessTextFile) {
			List<String> dataList = new ArrayList<String>();
			dataList = retrieveResultFile(Environment
					.getExternalStorageDirectory()
					+ "/Notice48/Infiles/Upload.txt");
			
			readDataLine(dataList);
		} else if (item.getItemId() == R.id.issSummary) {
			startActivity(new Intent(this, SummaryList.class));
		} else if (item.getItemId() == R.id.issBluetoothSetting) {
			btPrinter.turnOnBT();
            while (!btPrinter.isTurnedOn()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
			startActivityForResult(new Intent(this, AppPreferenceActivity.class), PREFCODE);
		} else {
			result = false;
		}
		return result;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PREFCODE && resultCode == RESULT_OK) {
			btPrinter.setMacAddress(BluetoothSharedPrefs.getMacAddress(this));
            btPrinter.setDeviceName(BluetoothSharedPrefs.getDeviceName(this));
            btPrinter.setAlwaysOn(BluetoothSharedPrefs.getBluetoothAlwaysOn(this));
            PrinterControls.btPrinter = btPrinter;
        }
        btPrinter.turnOffBT();
    }
	
	protected void readDataLine(List<String> dataList){
		int mode = NOTHING;
		for (int i = 0; i < dataList.size(); i++) {
			if (dataList.get(i).equals("DOWNLOAD")){
				mode = USER_PROFILE_TABLE;
			} else if (dataList.get(i).equals("END TRANS")) {
				mode = CONSUMER_TABLE;
			} else if (dataList.get(i).equals("END MASTER")) {
				mode = UNPAID_TABLE;
			} else if (dataList.get(i).equals(".....")) {
				dsConsumer.updateConsumersArrears();
				AlertDialog ad = doneDialog("Processing Text File");
				ad.show();
			} else {
				createData(strToObj(dataList.get(i), mode), mode);
			}
		}
	}

	protected List<String> retrieveResultFile(String path) {
		List<String> textData = new ArrayList<String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				textData.add(line);
			}
			br.close();
		} catch (IOException e) {
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle("File not found");
			ad.show();
		}
		return textData;
	}

	protected Object strToObj(String data, int mode) {
		switch (mode) {
		case CONSUMER_TABLE:
			Consumer consumer = new Consumer();
			//consumer.setCode(Long.parseLong(data.substring(0, 8).trim()));
			consumer.setAccountNumber(data.substring(8, 23).trim());
			consumer.setName(data.substring(23, 53).trim());
			consumer.setAddress(data.substring(53, 83).trim());
			//consumer.setMeterSerial(data.substring(83, 98).trim());
			consumer.setArrears(0.0);
			return consumer;
		case UNPAID_TABLE:
			Unpaid unpb = new Unpaid();
			unpb.setCode(Long.parseLong(data.substring(0, 8).trim()));
			unpb.setBillMonth(data.substring(8, 12).trim());
			unpb.setAmount(Double.parseDouble(data.substring(12, 20).trim()));
			return unpb;
		case USER_PROFILE_TABLE:
			UserProfile userProfile = new UserProfile();
			userProfile.setName(data.substring(0,30).trim());
			return userProfile;
		default:
			return null;
		}
	}
	
	protected void createData(Object myData, int mode){
		switch (mode) {
		case CONSUMER_TABLE:
			dsConsumer.createConsumer((Consumer) myData);
			break;
		case UNPAID_TABLE:
			dsUnpaid.createUnpaid((Unpaid) myData);
			break;
		case USER_PROFILE_TABLE:
			dsUserProfile.createUserProfile((UserProfile) myData);
			break;
		default:
			break;
		}
	}
	
	protected AlertDialog doneDialog(String dataKind) {
		AlertDialog.Builder result = new AlertDialog.Builder(this)
				.setTitle("Done processing")
				.setMessage(dataKind + " please tap OK to resume")
				.setPositiveButton("OK", null).setCancelable(false);
		return result.create();
	}

    protected boolean retrieveData(String fileName){
        boolean result = false;
        File myFile = new File(fileName);
        List<String> rawData;
        if (myFile.exists()){
            initializeDatabase();
            rawData = retrieveStringFromFile(fileName);
            processRawData(rawData);
            //myFile.delete();
        } else {
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("File not found!");
            ad.show();
        }
        return result;
    }

    public List<String> retrieveStringFromFile(String path) {
        List<String> textData = new ArrayList<String>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                textData.add(line);
            }
            br.close();
        } catch (IOException e) {
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("File not found!");
            ad.show();
        }
        return textData;
    }

    protected void processRawData(List<String> rawData) {
        for (int i = 0; i <= rawData.size() - 1; i++){
            /*if (i == 0){
                UserProfile userProfileValues = listToUserProfile(rawData.get(i));
                dsUserProfile.createUserProfile(userProfileValues);
            } else {
                Consumer consumer = listToConsumer(rawData.get(i));
                dsConsumer.createConsumer(consumer);
            }*/
        }
    }

    protected void initializeDatabase() {
        dsConsumer.refresh();
        dsUnpaid.truncate();
        dsUserProfile.refresh();
    }
}
