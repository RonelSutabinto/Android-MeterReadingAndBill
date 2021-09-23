package com.generic.readandbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.androidapp.mytools.AppPreferenceActivity;
import com.androidapp.mytools.bluetooth.BluetoothSharedPrefs;
import com.androidapp.mytools.bluetooth.MyPrinter;
import com.androidapp.mytools.bluetooth.PrinterControls;
import com.androidapp.mytools.objectmanager.StringManager;
import com.generic.readandbill.database.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends Activity {

    protected static final int PREFCODE = 30;
    protected ConsumerDataSource dsConsumer;   
	protected ReadingDataSource dsReading;
	protected FieldFindingDataSource dsFF;
	protected RateDataSource dsRates;
	protected UserProfileDataSource dsUserProfile;
	protected String btrAddress = null;
	protected ImageView splashScreenImage;
	protected static final String TAG = "Splash Screen";
	protected int mode = NOTHING;
    private Activity context;
	public static MyPrinter btPrinter;

    protected String filePath = Environment.getExternalStorageDirectory()
            + "/ReadAndBill/InFiles/Upload.txt";

	protected static final int NOTHING = 0;
	protected static final int CONSUMER = 10;
	protected static final int USERPROFILE = 20;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        this.context = this;

		setContentView(R.layout.activity_splash_screen);
		createDirIfNotExists(Environment.getExternalStorageDirectory()
				+ "/ReadAndBill/OutFiles/");
		createDirIfNotExists(Environment.getExternalStorageDirectory()
				+ "/ReadAndBill/InFiles/");

		btPrinter = new MyPrinter(this);
        if (BluetoothSharedPrefs.getMacAddress(this) != null) {
            btPrinter.setMacAddress(BluetoothSharedPrefs.getMacAddress(this));
            btPrinter.setDeviceName(BluetoothSharedPrefs.getDeviceName(this));
            btPrinter.setAlwaysOn(BluetoothSharedPrefs.getBluetoothAlwaysOn(this));
            PrinterControls.btPrinter = btPrinter;
        }

		dsConsumer = new ConsumerDataSource(this);
		dsRates = new RateDataSource(this);
		dsReading = new ReadingDataSource(this);
		dsUserProfile = new UserProfileDataSource(this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_splash, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.iConsumerList) {
			startActivity(new Intent(this, MyConsumerList.class));
			return true;
		} else if (item.getItemId() == R.id.iProcessTextFile) {
			return true;
		} else if (item.getItemId() == R.id.iGenerateResult) {
			generateResultFile(initializeResultFields(), Environment.getExternalStorageDirectory()
                    + "/ReadAndBill/OutFiles/result.txt");
			return true;
		} else if (item.getItemId() == R.id.iSummaryList) {
			startActivity(new Intent(this, SummaryList.class));
			return true;
		} else if (item.getItemId() == R.id.iBluetoothSetting) {
			btPrinter.turnOnBT();
            while (!btPrinter.isTurnedOn()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
			startActivityForResult(new Intent(this, AppPreferenceActivity.class), PREFCODE);
			return true;
		}
		return false;
	}
	
	public void readDataList(List<String> dataList) {		
		File myFile = new File(Environment.getExternalStorageDirectory()
				+ "/ReadAndBill/InFiles/Upload.txt");
		for (int i = 0; i < dataList.size(); i++) {
			if (i == 0) {
				mode = USERPROFILE;
			} else if (dataList.get(i).equals("END TRANS")) {
				mode = CONSUMER;
			} else {
				createData(dataList.get(i));
			}
			
		}
		AlertDialog ad = doneDialog("Processing Text File");
		ad.show();
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	private void createData(String dataValues) {
		switch (mode) {
		case USERPROFILE:
			//dsUserProfile.createUserProfile(listToUserProfile(dataValues));
			break;
		case CONSUMER:
			//dsConsumer.createConsumer(listToConsumer(dataValues));
			break;
		default:
			break;
		}
	}

	private boolean createDirIfNotExists(String path) {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				Log.e("Read And Bill Log :: ",
						"Problem creating folder or SD Card not present");
				return false;
			}
		}
		return true;
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

	protected AlertDialog doneDialog(String dataKind) {
		AlertDialog.Builder result = new AlertDialog.Builder(this)
				.setTitle("Done processing")
				.setMessage(dataKind + " please tap OK to resume")
				.setPositiveButton("OK", null).setCancelable(false);
		return result.create();
	}

	protected AlertDialog nothingDialog() {
		AlertDialog.Builder result = new AlertDialog.Builder(this)
				.setTitle("Nothing to process")
				.setMessage("Tap OK to continue").setPositiveButton("OK", null)
				.setCancelable(false);
		return result.create();
	}

	protected List<String> initializeResultFields() {
		List<String> result = new ArrayList<String>();

		List<Reading> readings = dsReading.getAllReadings();
		Time myReadingDate = new Time();
		Integer isPrinted;
		String ffCode = "0";
		String ffDescription;

		if (readings.size() > 0) {

			for (Reading reading : readings) {
				myReadingDate.set(reading.getTransactionDate());
				if (reading.getIsPrinted()) {
					isPrinted = 1;
				} else {
					isPrinted = 0;
				}
                ffCode = "0";
				if (reading.getFieldFinding() != -1) {
					ffCode = dsFF.getCode(reading.getFieldFinding());
					ffDescription = dsFF.getDescription(ffCode);
				} else {
					ffDescription = reading.getRemarks();
				}

				result.add(StringManager.leftJustify(
						String.valueOf(reading.getIdConsumer()), 8)
						+ StringManager.leftJustify("", 16)
						+ StringManager.rightJustify(
								String.valueOf(reading.getReading()), 8)
						+ StringManager.rightJustify(
								String.valueOf(reading.getDemand()), 4)
						+ StringManager.leftJustify(isPrinted.toString(), 1)
						+ StringManager.leftJustify(reading.getFeedBackCode(), 15)
						+ StringManager.leftJustify(ffCode, 2)
						+ StringManager.leftJustify(ffDescription, 15)
						+ StringManager.leftJustify(
								myReadingDate.format("%m%d%H%M%S"), 10) + " "
						+ StringManager.leftJustify(
								String.valueOf(reading.getKilowatthour()), 8)						
						+ (char) 13 + "" + (char) 10);

			}
		}
		return result;
	}

	protected boolean generateResultFile(final List<String> result, final String path) {
		if (result.size() != 0) {
			Thread genResult = new Thread() {
				public void run() {
					try {
						FileWriter fw = new FileWriter(new File(path));
						for (int i = 0; i < result.size() - 1; i++) {
							fw.append(result.get(i));
							if (i % 4 == 0) {
								Thread.sleep(500);
							}
						}
						fw.flush();
						fw.close();
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog ad = doneDialog("Result");
                                ad.show();
                            }
                        });

					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			genResult.start();
            return true;
		} else {
			AlertDialog ad = nothingDialog();
			ad.show();
            return false;
		}
	}


	protected void initializeDatabase() {		
		dsConsumer.truncate();
		dsRates.truncate();
		dsReading.truncate();
		dsUserProfile.truncate();		
	}
}
