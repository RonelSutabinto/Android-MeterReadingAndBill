package com.zaneco.notice48hrs;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import com.androidapp.mytools.objectmanager.StringManager;
import com.zaneco.notice48hrs.database.Consumer;
import com.zaneco.notice48hrs.database.ConsumerDataSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erwin
 * Date: 10/31/13
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummaryList extends com.generic.notice48hrs.SummaryList {

    private ConsumerArrayAdapter adapter;
    private ConsumerDataSource dsc;
    protected List<Consumer> values;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.islPrintSummary) {
            SplashScreen.btPrinter.print(summaryReport());
        } else
            super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void initializeObjects() {
        dsc = new ConsumerDataSource(this);
        values = dsc.getAllServedZanecoConsumer();

        vTotalConsumer = dsc.getNumberOfConsumer();
        vTotalServed = values.size();

        if(adapter == null){
            adapter = new ConsumerArrayAdapter(this, values);
        }

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (adapter != null) {
                    if (count < before) {
                        adapter.resetData();
                    }
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void applySort() {
        switch (sortMode) {
            case SORT_ACCOUNTNUMBER:
                adapter.sort(new Comparator<Consumer>() {

                    @Override
                    public int compare(Consumer lhs, Consumer rhs) {
                        return lhs.getAccountNumber().compareTo(
                                rhs.getAccountNumber());
                    }

                });
                adapter.setFilterMode(ConsumerArrayAdapter.ACCOUNT_NUMBER);
                break;
            case SORT_SEQUENCE:
                break;
            case SORT_NAME:
                adapter.sort(new Comparator<Consumer>() {

                    @Override
                    public int compare(Consumer lhs, Consumer rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }

                });
                adapter.setFilterMode(ConsumerArrayAdapter.NAME);
                break;
		case SORT_METER_SERIAL:
			adapter.sort(new Comparator<Consumer>() {

				@Override
				public int compare(Consumer lhs, Consumer rhs) {
					return lhs.getMeterSerial().compareTo(rhs.getMeterSerial());
				}

			});
			adapter.setFilterMode(ConsumerArrayAdapter.METER_SERIAL);
			break;
        }
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<String> summaryReport() {
        List<Consumer> served = dsc.getAllServedZanecoConsumer();
        List<String> result = new ArrayList<String>();

        result.add("Ver " + SplashScreen.version + "\n");
        result.add(StringManager.centerJustify("Summary Served", 47) + "\n");
        result.add("\n");
        for (int i = 0; i < served.size(); i++) {
            result.add(StringManager.leftJustify(
                    served.get(i).getAccountNumber(), 10)
                    + StringManager.leftJustify(served.get(i).getName(), 30)
                    + "\n");
        }
        result.add(StringManager.lineBreak());
        result.add(StringManager.leftJustify("Total Served: ", 27) + StringManager.rightJustify(totalServed.getText().toString(), 20) + "\n");
        result.add(StringManager.leftJustify("Total Unserved: ", 27) + StringManager.rightJustify(totalUnserved.getText().toString(), 20) + "\n");
        result.add("\n ");
        result.add("\n  FM-CAD-05          00           07-01-19 ");
        result.add(StringManager.formFeed());

		/*result += "Ver " + SplashScreen.version + "\n";
		result += StringHelper.centerJustify("Summary Served",47) + "\n";
		result += "\n";
		for (int i = 0; i < served.size(); i++) {
			result += StringHelper.leftJustify(
					served.get(i).getAccountNumber(), 10)
					+ StringHelper.leftJustify(served.get(i).getName(), 30)
					+ "\n";
		}
		result += StringHelper.lineBreak();
		result += StringHelper.leftJustify("Total Served: ", 27) + StringHelper.rightJustify(totalServed.getText().toString(), 20) + "\n";
		result += StringHelper.leftJustify("Total Unserved: ", 27) + StringHelper.rightJustify(totalUnserved.getText().toString(), 20) + "\n";
		result += StringHelper.formFeed();*/
        return result;
    }

}
