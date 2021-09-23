package com.zaneco.notice48hrs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.androidapp.mytools.objectmanager.StringManager;
//import com.generic.readandbill.SplashScreen;
import com.zaneco.notice48hrs.database.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//import javax.swing.JOptionPane;

public class MyConsumerList extends com.generic.notice48hrs.MyConsumerList {
    
    private ConsumerDataSource dsCon;
    private UserProfileDataSource dsup;
    private UnpaidDataSource dsub;

    private List<Consumer> values;

    private ConsumerArrayAdapter adapter;

    private static final int SORT_METER_SERIAL = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void initializeObject() {
        dsCon = new ConsumerDataSource(this);
        dsup = new UserProfileDataSource(this);
        dsub = new UnpaidDataSource(this);

        values = dsCon.getAllZanecoConsumer();

        adapter = new ConsumerArrayAdapter(this, values);
        setListAdapter(adapter);


        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (count < before) {
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listPos = position;
        
        //if (SplashScreen.btPrinter.print48H(printDiscoNotice(position))) {        	
                  
        //if (SplashScreen.btPrinter.print(printDiscoNotice(position))) {
         
            
            SplashScreen.btPrinter.print48H(printDiscoNotice(position));
            Consumer consumer = dsCon.getConsumer(adapter.getItemId(listPos));       
            consumer.setServed(true);
            //consumer.setServedr(1);
            
            if (SplashScreen.btPrinter.getisPrint()){
            	adapter.remove(adapter.getItem(listPos));
            	dsCon.updateConsumer(consumer);         
            	adapter.notifyDataSetChanged();           
            	adapter.insert(consumer, position);       
            	SplashScreen.btPrinter.setisPrint(true);
            }            
      
            /*} else {
            if (SplashScreen.btPrinter.getBtd() == null) {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("bluetooth not yet configured");
                adb.setMessage("Could not proceed to printing");
                adb.setCancelable(false);
                adb.setPositiveButton("OK", null);
                AlertDialog ad = adb.create();
                ad.show();
                SplashScreen.btPrinter.turnOffBT();
            } else if (SplashScreen.btPrinter.getBta().isEnabled()) {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Bluetooth is still printing");
                adb.setMessage("Wait till i'm done");
                adb.setCancelable(false);
                adb.setPositiveButton("OK", null);
                AlertDialog ad = adb.create();
                ad.show();
            }
        }*/
    }



    protected List<String> printDiscoNotice(int position) {
    	List<String> resultF = new ArrayList<String>();
    	
    	if (SplashScreen.btPrinter.getDeviceName().equals("SPP-R310")){    		
    	
	        Consumer con = dsCon.getConsumer(adapter.getItemId(position));
	        UserProfile up = dsup.getUserProfile();
	        List<Unpaid> unpbs = dsub.getConsumersUnpaid(con);
	        Time transDate = new Time();
	        transDate.setToNow();
	        List<String> result = new ArrayList<String>();     
	        
	        SplashScreen.btPrinter.setcodeStrPrint(con.getAccountNumber());
	        result.add("Ver " + SplashScreen.version + "\n");
	        result.add(transDate.format("%D") + "\n");
	        result.add("\n");
	        result.add(StringManager.centerJustify(
	                "ZAMBOANGA DEL NORTE ELECTRIC COOPERATIVE", 47) + "\n");
	        result.add(StringManager.centerJustify(
	                "Dipolog City, Zamboanga del Norte", 47) + "\n");        
	       
	        result.add(StringManager.leftJustify(con.getAccountNumber(), 47) + "\n");
	        result.add(StringManager.leftJustify(con.getName(), 47) + "\n");
	        result.add(StringManager.leftJustify(con.getAddress(), 47) + "\n");
			result.add(StringManager.leftJustify(con.getMeterSerial(), 47) + "\n");       
	        result.add("\n");
	        result.add(StringManager.centerJustify(
	                "KATAPUSANG  PANINGIL  UG  48  KA ORAS NGA", 47) + "\n");
	        result.add(StringManager.centerJustify(
	                "LUGWAY   NGA   PAHIBALO  SA  PAGPUTOL  SA", 47) + "\n");
	        result.add(StringManager.centerJustify("SERBISYO SA ELEKTRISIDAD", 47)
	                + "\n");
	        result.add( "\n");
	        result.add("Sir/Madame");
	        result.add("\n");
	        result.add("   Kami nagatahud paghatag kanimo niining ulahi\n");
	        result.add("ug  katapusang hangyo sa pagbayad sa kinatibuk-\n");
	        result.add("ang  bayronon  sa kuryente sulod sa duha (2) ka\n");
	        result.add("adlaw  kon  48  hrs  gikan  sa  imong  pagdawat\n");
	        result.add("niining  pahibalo  (walay  labot  ang adlaw nga\n");
			result.add("walay  trabaho) ug kantidad nga: "
					+ StringManager.rightJustify(dsub.getArrear(con.getCode())
							.toString(), 14) + "\n");
	        result.add("\n");
	        for (int i = 0; i < unpbs.size(); i++) {
	            result.add(StringManager.leftJustify(billmonthStr(unpbs.get(i)
	                    .getBillMonth()), 33)
	                    + StringManager.rightJustify(
	                    String.valueOf(unpbs.get(i).getAmount()), 14)
	                    + "\n"); 
	        }
	        result.add("\n");
	        result.add("   Ang pagbayad pagahimoon sa buhatan sa ZANECO\n");
			result.add("\"CASHIERS OFFICE \" sa " + up.getAddressB());
	        result.add("\n");
	        result.add("   Kung ugaling mapakyas ka pagbayad ning maong\n");
	        result.add("balayronon  sa  kuryente  sa gilatid nga lugway\n");
	        result.add("kami  moputol sa serbisyo sa elektrisidad bisan\n");
	        result.add("unsang  orasa  sa pag-abot sa ika tulo ka adlaw\n");
	        result.add("gikan sa imong pagdawat niining pahibalo.\n");
	        result.add("\n");
	        result.add("   Palihog  ayaw  tagda  kini nga pahibalo kung\n");
	        result.add("ugaling ang pagbayad nahuman na.\n");
	        result.add("\n");
	        result.add("   Among  gidayeg  ang  imong  pagsabot niining\n");
	        result.add("bahina.\n");
	        result.add("\n");
	        result.add(StringManager.rightJustify("Kanimo matinahuron,", 47) + "\n");
	        result.add("\n");
	        result.add(StringManager.rightJustify("(SGD) ADELMO P. LAPUT", 47)
	                + "\n");
	        result.add(StringManager.rightJustify("General Manager - CEO", 47)
	                + "\n");
	        result.add("\n");
	        result.add("\n");
	        
	        result.add(StringManager.lineBreak());
	        //result.add(StringManager.lineFeed());
	        result.add("\n");
	        result.add(StringManager.leftJustify("ACCOUNT NO.", 14)
	                + ": " + con.getAccountNumber() + "\n");
	        result.add(StringManager.leftJustify("Consumer Name", 14) + ": "
	                + con.getName() + "\n");
	        result.add("\n");
	        result.add("GIDAWAT : "
	                + "_____________________________________" + "\n");
	        result.add(StringManager.rightJustify("MIEMBRO - KONSUMANTE / TAG-IYA",
	                47) + "\n");
	        result.add("\n");
	        result.add("PETSA SA PAGDAWAT : " + transDate.format("%D")
	                + "\n");
	        result.add("\n");
	        result.add("[ ] NAGDUMILI SA PAGDAWAT _____________________\n");
	        result.add("\n");
	        result.add(StringManager.lineBreak());
	        result.add( "\n");
	        result.add( "\n");
	        result.add( "NAGHATAG SA DISCONNECTION NOTICE :\n");
	        result.add("\n");
	        result.add(StringManager.SigLine());
	        result.add(up.getAddress());
	        result.add( "\n");
	        result.add( "\n");
	        result.add("PETSA : " + transDate.format("%D") + "\n");
	        result.add( "\n");
	        result.add( "\n");       
	        result.add(StringManager.lineBreak());
	        result.add("\n");
	        result.add("\n  FM-CAD-04          00           07-01-19 ");
	        result.add( "\n");
	        result.add( "\n");   
	        result.add( "\n");   
	        result.add( "\n");   
	
	        resultF = result;
    	}else{
    		//=========================================
    		//resultF = printDiscoNoticeAPEX(position);
    		//=========================================
    		
            Consumer con = dsCon.getConsumer(adapter.getItemId(position));
            UserProfile up = dsup.getUserProfile();
            List<Unpaid> unpbs = dsub.getConsumersUnpaid(con);
            Time transDate = new Time();
            transDate.setToNow();
            List<String> result = new ArrayList<String>(); 
             
            result.add("\n");
            result.add("Ver " + SplashScreen.version + "\n");
            result.add(transDate.format("%D") + "\n");
            //result.add((char) 10 + "\n");
            result.add((char) 28
                    + StringManager.centerJustify(
                    "ZAMBOANGA DEL NORTE ELECTRIC COOPERATIVE", 47) + "\n");
            result.add((char) 29
                    + StringManager.centerJustify(
                    "Dipolog City, Zamboanga del NorteRonel", 47) + "\n");
            //.add((char) 10 + "\n");
            //result.add((char) 10 + "\n");
            result.add(StringManager.leftJustify(con.getAccountNumber(), 47) + "\n");
            result.add(StringManager.leftJustify(con.getName(), 47) + "\n");
            result.add(StringManager.leftJustify(con.getAddress(), 47) + "\n");
    		result.add(StringManager.leftJustify(con.getMeterSerial(), 47) + "\n");
           //.add((char) 10 + "\n");
          //  result.add((char) 10 + "\n");
            result.add(StringManager.centerJustify(
                    "KATAPUSANG  PANINGIL  UG  48  KA ORAS NGA", 47) + "\n");
            result.add(StringManager.centerJustify(
                    "LUGWAY   NGA   PAHIBALO  SA  PAGPUTOL  SA", 47) + "\n");
            result.add(StringManager.centerJustify("SERBISYO SA ELEKTRISIDAD", 47)
                    + "\n");
            //result.add((char) 10 + "\n");
            result.add("Sir/Madame");
            //result.add((char) 10 + "\n");
            result.add("   Kami nagatahud paghatag kanimo niining ulahi\n");
            result.add("ug  katapusang hangyo sa pagbayad sa kinatibuk-\n");
            result.add("ang  bayronon  sa kuryente sulod sa duha (2) ka\n");
            result.add("adlaw  kon  48  hrs  gikan  sa  imong  pagdawat\n");
            result.add("niining  pahibalo  (walay  labot  ang adlaw nga\n");
    		result.add("walay  trabaho) ug kantidad nga: "
    				+ StringManager.rightJustify(dsub.getArrear(con.getCode())
    						.toString(), 14) + "\n");
            result.add((char) 10 + "\n");
            for (int i = 0; i < unpbs.size(); i++) {
                result.add(StringManager.leftJustify(billmonthStr(unpbs.get(i)
                        .getBillMonth()), 33)
                        + StringManager.rightJustify(
                        String.valueOf(unpbs.get(i).getAmount()), 14)
                        + "\n");
            }
            result.add((char) 10 + "\n");
            result.add("   Ang pagbayad pagahimoon sa buhatan sa ZANECO\n");
    		result.add("\"CASHIERS OFFICE \" sa " +up.getAddressB());
            result.add((char) 10 + "\n");
            result.add("   Kung ugaling mapakyas ka pagbayad ning maong\n");
            result.add("balayronon  sa  kuryente  sa gilatid nga lugway\n");
            result.add("kami  moputol sa serbisyo sa elektrisidad bisan\n");
            result.add("unsang  orasa  sa pag-abot sa ika tulo ka adlaw\n");
            result.add("gikan sa imong pagdawat niining pahibalo.\n");
            result.add((char) 10 + "\n");
            result.add("   Palihog  ayaw  tagda  kini nga pahibalo kung\n");
            result.add("ugaling ang pagbayad nahuman na.\n");
            result.add((char) 10 + "\n");
            result.add("   Among  gidayeg  ang  imong  pagsabot niining\n");
            result.add("bahina.\n");
            result.add((char) 10 + "\n");
            result.add(StringManager.rightJustify("Kanimo matinahuron,", 47) + "\n");
            result.add((char) 10 + "\n");
            result.add(StringManager.rightJustify("(SGD) ADELMO P. LAPUT", 47)
                    + "\n");
            result.add(StringManager.rightJustify("General Manager - CEO", 47)
                    + "\n");
            result.add((char) 10 + "\n");
            result.add((char) 10 + "\n");
            result.add((char) 10 + "\n");
            result.add(StringManager.lineBreak());
            result.add(StringManager.lineFeed());
            result.add((char) 28 + StringManager.leftJustify("ACCOUNT NO.", 14)
                    + ": " + con.getAccountNumber() + "\n");
            result.add(StringManager.leftJustify("Consumer Name", 14) + ": "
                    + con.getName() + "\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add((char) 28 + "GIDAWAT : "
                    + "_____________________________________" + "\n");
            result.add(StringManager.rightJustify("MIEMBRO - KONSUMANTE / TAG-IYA",
                    47) + "\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add((char) 28 + "PETSA SA PAGDAWAT : " + transDate.format("%D")
                    + "\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add((char) 28
                    + "[ ] NAGDUMILI SA PAGDAWAT _____________________\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add(StringManager.lineBreak());
            result.add((char) 10 + "\n");
            result.add((char) 10 + "\n");
            result.add((char) 28 + "NAGHATAG SA DISCONNECTION NOTICE :\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add(StringManager.SigLine());
            result.add(up.getAddress());
            //result.add(up.getName()); 
            //result.add((char) 28 + up.getAddress() + "\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add((char) 28 + "PETSA : " + transDate.format("%D") + "\n");
            result.add((char) 29 + "" + (char) 10 + "\n");
            result.add(StringManager.lineFeed());
            result.add(StringManager.lineBreak());
            result.add("\n ");
            result.add("\n  FM-CAD-04          00           07-01-19 ");
            result.add( "\n ");   
	        result.add( "\n ");   
	        result.add( "\n ");
	        result.add( "\n ");   
	        result.add( "\n ");   
	        result.add( "\n ");
	        result.add( "\n ");
	        result.add( "\n ");   
	        result.add( "\n ");   
	        result.add( "\n ");
	        result.add( "\n ");
	        result.add( "\n ");   
	        result.add( "\n ");   
	        result.add( "\n ");
            //result.add(StringManager.formFeed());	

            resultF = result;
    	}
    	
    	return resultF;
    }

    protected List<String> printDiscoNoticeAPEX(int position) {
        Consumer con = dsCon.getConsumer(adapter.getItemId(position));
        UserProfile up = dsup.getUserProfile();
        List<Unpaid> unpbs = dsub.getConsumersUnpaid(con);
        Time transDate = new Time();
        transDate.setToNow();
        List<String> result = new ArrayList<String>();     
              
        
        result.add("Ver " + SplashScreen.version + "\n");
        result.add(transDate.format("%D") + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 28
                + StringManager.centerJustify(
                "ZAMBOANGA DEL NORTE ELECTRIC COOPERATIVE", 47) + "\n");
        result.add((char) 29
                + StringManager.centerJustify(
                "Dipolog City, Zamboanga del Norte", 47) + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 10 + "\n");
        result.add(StringManager.leftJustify(con.getAccountNumber(), 47) + "\n");
        result.add(StringManager.leftJustify(con.getName(), 47) + "\n");
        result.add(StringManager.leftJustify(con.getAddress(), 47) + "\n");
		result.add(StringManager.leftJustify(con.getMeterSerial(), 47) + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 10 + "\n");
        result.add(StringManager.centerJustify(
                "KATAPUSANG  PANINGIL  UG  48  KA ORAS NGA", 47) + "\n");
        result.add(StringManager.centerJustify(
                "LUGWAY   NGA   PAHIBALO  SA  PAGPUTOL  SA", 47) + "\n");
        result.add(StringManager.centerJustify("SERBISYO SA ELEKTRISIDAD", 47)
                + "\n");
        result.add((char) 10 + "\n");
        result.add("Sir/Madame");
        result.add((char) 10 + "\n");
        result.add("   Kami nagatahud paghatag kanimo niining ulahi\n");
        result.add("ug  katapusang hangyo sa pagbayad sa kinatibuk-\n");
        result.add("ang  bayronon  sa kuryente sulod sa duha (2) ka\n");
        result.add("adlaw  kon  48  hrs  gikan  sa  imong  pagdawat\n");
        result.add("niining  pahibalo  (walay  labot  ang adlaw nga\n");
		result.add("walay  trabaho) ug kantidad nga: "
				+ StringManager.rightJustify(dsub.getArrear(con.getCode())
						.toString(), 14) + "\n");
        result.add((char) 10 + "\n");
        for (int i = 0; i < unpbs.size(); i++) {
            result.add(StringManager.leftJustify(billmonthStr(unpbs.get(i)
                    .getBillMonth()), 33)
                    + StringManager.rightJustify(
                    String.valueOf(unpbs.get(i).getAmount()), 14)
                    + "\n");
        }
        result.add((char) 10 + "\n");
        result.add("   Ang pagbayad pagahimoon sa buhatan sa ZANECO\n");
		result.add("\"CASHIERS OFFICE \" sa " +up.getAddressB());
        result.add((char) 10 + "\n");
        result.add("   Kung ugaling mapakyas ka pagbayad ning maong\n");
        result.add("balayronon  sa  kuryente  sa gilatid nga lugway\n");
        result.add("kami  moputol sa serbisyo sa elektrisidad bisan\n");
        result.add("unsang  orasa  sa pag-abot sa ika tulo ka adlaw\n");
        result.add("gikan sa imong pagdawat niining pahibalo.\n");
        result.add((char) 10 + "\n");
        result.add("   Palihog  ayaw  tagda  kini nga pahibalo kung\n");
        result.add("ugaling ang pagbayad nahuman na.\n");
        result.add((char) 10 + "\n");
        result.add("   Among  gidayeg  ang  imong  pagsabot niining\n");
        result.add("bahina.\n");
        result.add((char) 10 + "\n");
        result.add(StringManager.rightJustify("Kanimo matinahuron,", 47) + "\n");
        result.add((char) 10 + "\n");
        result.add(StringManager.rightJustify("(SGD) ADELMO P. LAPUT", 47)
                + "\n");
        result.add(StringManager.rightJustify("General Manager - CEO", 47)
                + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 10 + "\n");
        result.add(StringManager.lineBreak());
        result.add(StringManager.lineFeed());
        result.add((char) 28 + StringManager.leftJustify("ACCOUNT NO.", 14)
                + ": " + con.getAccountNumber() + "\n");
        result.add(StringManager.leftJustify("Consumer Name", 14) + ": "
                + con.getName() + "\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add((char) 28 + "GIDAWAT : "
                + "_____________________________________" + "\n");
        result.add(StringManager.rightJustify("MIEMBRO - KONSUMANTE / TAG-IYA",
                47) + "\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add((char) 28 + "PETSA SA PAGDAWAT : " + transDate.format("%D")
                + "\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add((char) 28
                + "[ ] NAGDUMILI SA PAGDAWAT _____________________\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add(StringManager.lineBreak());
        result.add((char) 10 + "\n");
        result.add((char) 10 + "\n");
        result.add((char) 28 + "NAGHATAG SA DISCONNECTION NOTICE :\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add(StringManager.SigLine());
        result.add(up.getAddress());
        //result.add(up.getName()); 
        //result.add((char) 28 + up.getAddress() + "\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add((char) 28 + "PETSA : " + transDate.format("%D") + "\n");
        result.add((char) 29 + "" + (char) 10 + "\n");
        result.add(StringManager.lineFeed());
        result.add(StringManager.lineBreak());
        result.add("\n");
        result.add("\n  FM-CAD-04          00           07-01-19 ");
        result.add("\n ");
        result.add("\n ");
        result.add("\n ");
        result.add(StringManager.formFeed());	

        return result;
    }
    
    @Override
    protected void applySort() {
        switch (sortMode) {
            case SORT_METER_SERIAL:
			    adapter.sort(new Comparator<Consumer>() {

	    			@Override
		    		public int compare(Consumer lhs, Consumer rhs) {
			    		return lhs.getMeterSerial().compareTo(rhs.getMeterSerial());
				    }

			    });
			    adapter.setFilterMode(ConsumerArrayAdapter.METER_SERIAL);
			    break;
            case SORT_ACCOUNT_NUMBER:
                adapter.sort(new Comparator<Consumer>() {

                    @Override
                    public int compare(Consumer lhs, Consumer rhs) {
                        return lhs.getAccountNumber().compareTo(
                                rhs.getAccountNumber());
                    }

                });
                adapter.setFilterMode(ConsumerArrayAdapter.ACCOUNT_NUMBER);
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
        }
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.iclSortByMeterSerial) {
           if (!item.isChecked()) {
                item.setChecked(true);
                sortMode = SORT_METER_SERIAL;
                applySort();
                return true;
            } else {
                return false;
            }        	
                   	
        } else
            super.onOptionsItemSelected(item);
        return false;
    }
}
