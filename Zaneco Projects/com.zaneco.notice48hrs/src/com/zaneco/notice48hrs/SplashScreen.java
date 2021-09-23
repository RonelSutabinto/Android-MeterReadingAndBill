package com.zaneco.notice48hrs;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import com.generic.notice48hrs.R;
import com.zaneco.notice48hrs.database.*;

import java.util.List;

//import javax.swing.JOptionPane;

public class SplashScreen extends com.generic.notice48hrs.SplashScreen {

    public static final String version = "2019.07.30";
    protected UnpaidDataSource dsUnpaid;
    private ConsumerDataSource dszConsumer;
    private UserProfileDataSource dsUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        dszConsumer = new ConsumerDataSource(this);
        dsConsumer = dszConsumer;
        dsUnpaid = new UnpaidDataSource(this);
        dsUserProfile = new UserProfileDataSource(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.issDisco) {
            startActivity(new Intent(this, MyConsumerList.class));
        } else
            super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.issProcessTextFile) {
            initializeDatabase();
            List<String> dataList = retrieveResultFile(Environment
                    .getExternalStorageDirectory()
                    + "/Notice48/Infiles/Upload.txt");
            readDataLine(dataList);
        } else if (item.getItemId() == R.id.issSummary) {
            startActivity(new Intent(this, SummaryList.class));
        } else
            super.onMenuItemSelected(featureId, item);
        return true;
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
                dszConsumer.updateConsumersArrears();
                AlertDialog ad = doneDialog("Processing Text File");
                ad.show();
            } else {
                createData(strToObj(dataList.get(i), mode), mode);
            }
        }
    }

    @Override
    protected void createData(Object myData, int mode){
        switch (mode) {
            case CONSUMER_TABLE:
                dszConsumer.createConsumer((Consumer) myData);
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

    protected Object strToObj(String data, int mode) {
        switch (mode) {
            case CONSUMER_TABLE:
                Consumer consumer = new Consumer();
                consumer.setCode(Long.parseLong(data.substring(0, 8).trim()));
                consumer.setAccountNumber(data.substring(8, 23).trim());
                consumer.setName(data.substring(23, 53).trim());
                consumer.setAddress(data.substring(53, 83).trim());
                consumer.setMeterSerial(data.substring(83, 98).trim());
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
                userProfile.setAddress(data.substring(0,30).trim());
                userProfile.setAddressB(data.substring(30,30+(data.length()-30)).trim());
                                              
                return userProfile;
            default:
                return null;
        }
    }

    protected void initializeDatabase() {
        dszConsumer.refresh();
        dsUnpaid.truncate();
        dsUserProfile.refresh();
    }
}
