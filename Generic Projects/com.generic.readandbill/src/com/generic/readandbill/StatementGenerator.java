package com.generic.readandbill;

import android.content.Context;

import com.generic.readandbill.database.*;
import com.generic.readandbill.database.UserProfileDataSource;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatementGenerator {

    protected static final String END_STRING = "\n";

    protected final Context context;
    protected Consumer myConsumer;
	protected ConsumerDataSource dsConsumer;
	protected Reading reading;
	protected ReadingDataSource dsReading;
	protected Rates rate;
	protected RateDataSource dsRate;
	protected UserProfile userProfile;
	protected UserProfileDataSource dsUserProfile;
    protected DecimalFormat kilowattUsed;
    protected DecimalFormat percentage;
    protected DecimalFormat amountFormat;
    protected DecimalFormat rateFormat;
   // protected DecimalFormat kwhDemandFormat;
	
	public StatementGenerator(Context context, Consumer myConsumer) {
        this.context = context;
		dsConsumer = new ConsumerDataSource(context);
		dsReading = new ReadingDataSource(context);
		dsUserProfile = new UserProfileDataSource(context);
		dsRate = new RateDataSource(context);

        kilowattUsed = new DecimalFormat("######0.0");
       // kwhDemandFormat = new DecimalFormat("#####0.0##");
        amountFormat = new DecimalFormat("###,###,##0.00");
        rateFormat = new DecimalFormat("##0.0000");

		this.myConsumer = myConsumer;
		if (myConsumer != null) {
			reading = dsReading.getReading(myConsumer.getId(), ReadingDataSource.CONSUMER_ID);
			rate = dsRate.getRate(myConsumer.getRateCode());
		}

	}

    public List<String> generateSOA() {
        List<String> result = new ArrayList<String>();
        if (myConsumer != null) {
            for (String string : soaHeader()) {
                result.add(string);
                // result += string;
            }
            for (String string : soaBody()) {
                result.add(string);
                // result += string;
            }
            for (String string : soaFooter()) {
                result.add(string);
                // result += string;
            }
			/*
			 * result += soaHeader(); result += soaBody(); result +=
			 * soaFooter();
			 */
        }
        return result;
    }

    protected List<String> soaFooter() {
        return null;
    }

    protected List<String> soaBody() {
        return null;
    }

    protected List<String> soaHeader() {
        return null;
    }

	

}
