package com.zaneco.readandbill;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.zaneco.readandbill.database.Consumer;
import com.zaneco.readandbill.database.ConsumerDataSource;

import java.util.List;

public class SummaryList extends com.generic.readandbill.SummaryList
{
    private ConsumerDataSource dsc;
    private List<Consumer> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dsc = new ConsumerDataSource(this);
        values = dsc.getReadConsumer();
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.generic.readandbill.R.menu.menu_consumer_list, menu);
        MenuItem item = menu.findItem(R.id.islPrintSummary);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.islPrintSummary) {
            SummaryDataGenerator sdg = new SummaryDataGenerator(this, values,
                    dsc.getNumberOfConsumer());
            
            //sdg.summaryHeader();
            //sdg.summaryBody();
            sdg.setSummary();
            SplashScreen.btPrinter.setCodeprintCheck(false);            
            SplashScreen.btPrinter.print(sdg.getSummary());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
