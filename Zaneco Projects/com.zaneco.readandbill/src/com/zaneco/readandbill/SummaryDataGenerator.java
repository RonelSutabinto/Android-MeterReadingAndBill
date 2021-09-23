package com.zaneco.readandbill;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import com.generic.readandbill.database.Reading;
import com.zaneco.readandbill.database.BillhistoryDataSource;
import com.zaneco.readandbill.database.ReadingDataSource;
import com.zaneco.readandbill.database.Consumer;
import com.zaneco.readandbill.database.UserProfile;
import com.zaneco.readandbill.database.UserProfileDataSource;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SummaryDataGenerator {

	 private ReadingDataSource dsReading;
	    private UserProfileDataSource dsUP;
	    private String periodCover;
	    private Integer totalRead;
	    private Integer totalUnread;
	    private Integer totalConsumer;
	    private Double totalKilowatthour;
	    private DecimalFormat kwhFormatter;
	    private BillhistoryDataSource dsbillhistory;
	    private AlertDialog.Builder alertDialogBuilder;

	    private List<Consumer> summary;
	    private List<String> summaryBodyStr;
	    private List<String> getBodyStr = new ArrayList<String>();
	    private List<String> getDRpost = new ArrayList<String>();
	    private String[] stockArr;
	    private Integer countDpost;
	    private Integer countCheckpost;
	    private Double kwhDpost;
	    private Double kwhCheckpost;
	    protected DecimalFormat formatDemand = new DecimalFormat("######0.0##");
	   

	    public SummaryDataGenerator(Context context, List<Consumer> summary, Integer totalConsumer) {
	        dsReading = new ReadingDataSource(context);
	        dsUP = new UserProfileDataSource(context);
	        dsbillhistory = new BillhistoryDataSource(context);
	        alertDialogBuilder = new AlertDialog.Builder(context);
	        summaryBodyStr = new ArrayList<String>();
	        //getBodyStr = new ArrayList<String>();
	        
	        kwhFormatter = new DecimalFormat("#####0.0");	        
	        
	        this.periodCover = "";
	        this.summary = summary;
	        this.totalRead = summary.size();
	        this.totalConsumer = totalConsumer;
	        this.totalUnread = totalConsumer - totalRead;
	    }

	    public List<String> getSummary(){   	      
	        return summaryBodyStr;
	    }
	    
	    public void setSummary(){
	    	List<String> bodyStr = new ArrayList<String>();
	    	DecimalFormat df=new DecimalFormat("0.0");
	    	String[] data;
	    	//bodyStr = summaryBody();
	    	summaryBodyStr.clear();	    	
	    	int a=0;   	
	    	    	
	    	 List<String> result = new ArrayList<String>();
		        for (String string : summaryHeader()) {
		        	summaryBodyStr.add(string);
		        }
		        
		        summaryBody();		        
		        for(String string : getBodyStr){
		        	summaryBodyStr.add(string);
		        }
		        summaryBodyStr.add(lineBreak());  		       
		        summaryBodyStr.add("\n"+centerJustify(leftJustify("Record Read : ", 21) + 
		        		       rightJustify(df.format(kwhCheckpost), 6)+ 
		        		       rightJustify(countCheckpost.toString(), 15), 42));
		        summaryBodyStr.add(lineBreak());
		        
		        summaryBodyStr.add("\n");
		        summaryBodyStr.add("\n _");
		        summaryBodyStr.add("\n CONSISTENT READING");
		        summaryBodyStr.add(lineBreak());
		        summaryBodyStr.add("\n" + leftJustify("Acct#", 10)
		                + " " + leftJustify("FB", 2)
		                + " " + leftJustify("P.Read", 6)
		                + " " + leftJustify("KWHr", 6)
		                + " " + leftJustify("T", 1));
		        summaryBodyStr.add(lineBreak());
		        
		        
		        for(String string : getDRpost){
		        	summaryBodyStr.add(string);
		        }
		        summaryBodyStr.add(lineBreak());		        
		        summaryBodyStr.add("\n"+centerJustify(leftJustify("Record Read : ", 21) + 
	        		       rightJustify(df.format(kwhDpost), 6)+ 
	        		       rightJustify(countDpost.toString(), 15), 42));
		        summaryBodyStr.add(lineBreak());      
		        
		        
		        for (String string : summaryFooter()) {
		        	summaryBodyStr.add(string);
		        }
	    }

	    private List<String> summaryHeader(){
	    	Calendar c = Calendar.getInstance();
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	    	String strDate = sdf.format(c.getTime());
	    	
	        UserProfile userProfile;
	        userProfile = dsUP.getUserProfile();
	        List<String> result = new ArrayList<String>();
	        Reading reading = dsReading.getReading(summary.get(0).getId(), ReadingDataSource.CONSUMER_ID);
	        Time readingDate = new Time();
	        readingDate.set(reading.getTransactionDate());
	        totalKilowatthour = 0.0;
	        result.add("Ver " + SplashScreen.version+" Android");
	        result.add("\n"+strDate);
	        result.add("\n" + (char) 28 + centerJustify("Reading Summary", 47) + (char) 29);
	       // result.add("\n" + centerJustify(userProfile.getBillmonth(), 4));	        
	        result.add("\n _");
	        result.add("\n VERIFY READING");
	        result.add(lineBreak());
	        result.add("\n" + leftJustify("Acct#", 10)
	                + " " + leftJustify("FB", 2)
	                + " " + leftJustify("P.Read", 6)
	                + " " + leftJustify("KWHr", 6)
	                + " " + leftJustify("T", 1));
	        result.add(lineBreak());				
					
			/*result += "Ver " + SplashScreen.version;
			result += "\n" + (char) 28 + centerJustify("Reading Summary", 47) + (char) 29;
			result += "\n";
			result += "\n" + readingDate.format("%D");
			result += "\n" + centerJustify("|" + leftJustify("Acct#", 10) 
					                     + "|" + leftJustify("FB", 2)
					                     + "|" + leftJustify("P.Read", 6)
					                     + "|" + leftJustify("KWHr", 6) + "|", 47);
			result += lineBreak();*/
	        return result;
	    }

	    private void summaryBody(){
	    	 List<String> result = new ArrayList<String>();  
	    	 List<String> selectResult = new ArrayList<String>();
	    	 String[] data;
	    	 String[] dataAve;
	    	 String tmpStr=null;	    	 
	    	
	         Reading reading = null;
	         int dataSize;
	         double rdngKwh = 0.0;
	         double readingcur = 0.0;
	         double rdngAve =0.0;
	         boolean forCheck = false;
	         String accountnumberAve;
	         String accountnumber;
	         String ffCode;
	         String ratecode;
	         
	       
	         if (!(getBodyStr.isEmpty())) {
	        	 this.getBodyStr.clear();
	         }
	         
	         if (!(getDRpost.isEmpty())){
	        	 this.getDRpost.clear();
	         }
	         
	         countDpost = 0;
	         countCheckpost = 0;
	         kwhDpost = 0.0;
	         kwhCheckpost =0.0;
	         dataSize = summary.size();
	         stockArr=new String[dataSize];
	         getBodyStr.clear();
	         for (int i = 0; i < dataSize; i++){
	             reading = dsReading.getReading(summary.get(i).getId(), ReadingDataSource.CONSUMER_ID);
	         
	             accountnumber = summary.get(i).getAccountNumber();
	             ffCode = reading.getFeedBackCode();
	             readingcur = reading.getReading();
	             rdngKwh = reading.getKilowatthour();
	             ratecode = summary.get(i).getRateCode();	             
	             	           
	             rdngAve = 0.0;
	             forCheck = false;
	             dsbillhistory.open();
	             try{
		             Cursor cursorAve = dsbillhistory.getAverageRdng(accountnumber);	             
		             if(cursorAve!=null){
		            	 rdngAve = cursorAve.getDouble(cursorAve.getColumnIndex(dsbillhistory.DIFF));
		             }
		         }catch(Exception e){
		        	 
		         }
	             dsbillhistory.close();	            
		        		        
	             
	            if( (rdngAve >=10) && (rdngAve <=20)){
	            	 if(!((rdngKwh<=40) && (rdngKwh>=10))){	            		
	            		 countCheckpost +=1;
	            		 kwhCheckpost += reading.getKilowatthour(); 
	            		 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1)); 
			        	 
	            	 }
	             } else if ( (rdngAve >=20.1) && (rdngAve <=100) ) {
	            	 if(!( ((rdngAve-(rdngAve*0.5))<=rdngKwh) && ((rdngAve+(rdngAve*0.5))>=rdngKwh)) ){	            		 
	            		 countCheckpost +=1;
	            		 kwhCheckpost += reading.getKilowatthour(); 
	            		 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
		            	 
	            	 }
	             } else if ( (rdngAve >=100.1) && (rdngAve <=1000)){
	            	 if (!(((rdngAve-(rdngAve*0.25))<=rdngKwh) && ((rdngAve+(rdngAve*0.25))>=rdngKwh)) ){
	            		 countCheckpost +=1;
	            		 kwhCheckpost += reading.getKilowatthour(); 
		            	 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
	            	 }	 
	             } else if((rdngAve >=1000.1) && (rdngAve <=5000)){
	            	 if (!(((rdngAve-(rdngAve*0.2))<=rdngKwh) && ((rdngAve+(rdngAve*0.2))>=rdngKwh))){	            		 
	            		 countCheckpost +=1;
	            		 kwhCheckpost += reading.getKilowatthour(); 
	            		 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
	            	 }
		            	 
	             } else if (rdngAve >=5000.1){
	            	 if (!(((rdngAve-1000)<=rdngAve) && ((rdngAve+1000)>=rdngAve))){	            		
	            		 countCheckpost +=1;
	            		 kwhCheckpost += reading.getKilowatthour(); 
	            		 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
		            	 
	            	 }
	             }else if ((rdngAve<10.0) || (rdngKwh < 10.0)) {	            	
	            	 countCheckpost +=1;
	            	 kwhCheckpost += reading.getKilowatthour(); 
	            	 getBodyStr.add("\n" + leftJustify(accountnumber, 10)
	                         + " " + leftJustify(ffCode, 2)
	                         + " " + rightJustify(Double.toString(readingcur), 7)
	                         + " " + rightJustify(Double.toString(rdngKwh), 6)
	                         + " " + leftJustify(ratecode, 1));
	            	 
	            	 /*alertDialogBuilder.setTitle("Error:");
	            	 alertDialogBuilder.setMessage(accountnumber+ "="+
		                       String.valueOf(reading.getKilowatthour())+ "  : STEP 6");
		        	 AlertDialog alertDialog6 = alertDialogBuilder.create();
		        	 alertDialog6.show();*/
	             }
	            
	            if((rdngAve >=10) && (rdngAve <=20) && (rdngKwh<=40) && (rdngKwh>=10)){	            	 
	            		 countDpost +=1;
	            		 kwhDpost += reading.getKilowatthour(); 
	            		 getDRpost.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1)); 
			        	 
	            	 
	             } else if ((rdngAve >=20.1) && (rdngAve <=100) && ((rdngAve-(rdngAve*0.5))<=rdngKwh) && ((rdngAve+(rdngAve*0.5))>=rdngKwh)) {
	            	
	            	 	 countDpost +=1;
	            	 	 kwhDpost += reading.getKilowatthour(); 
	            		 getDRpost.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
		            	 
	            	
	             } else if ((rdngAve >=100.1) && (rdngAve <=1000) && ((rdngAve-(rdngAve*0.25))<=rdngKwh) && ((rdngAve+(rdngAve*0.25))>=rdngKwh)){
	            	 	 countDpost +=1;
	            	 	 kwhDpost += reading.getKilowatthour(); 
		            	 getDRpost.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
	            	 	 
	             } else if((rdngAve >=1000.1) && (rdngAve <=5000) && ((rdngAve-(rdngAve*0.2))<=rdngKwh) && ((rdngAve+(rdngAve*0.2))>=rdngKwh)){
	            	     countDpost +=1;
	            	     kwhDpost += reading.getKilowatthour(); 
	            		 getDRpost.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
	                	 
	             } else if (rdngAve >=5000.1 && ((rdngAve-1000)<=rdngKwh) && ((rdngAve+1000)>=rdngKwh)){
	            	     countDpost +=1;
	            	     kwhDpost += reading.getKilowatthour(); 
	            		 getDRpost.add("\n" + leftJustify(accountnumber, 10)
		                         + " " + leftJustify(ffCode, 2)
		                         + " " + rightJustify(Double.toString(readingcur), 7)
		                         + " " + rightJustify(Double.toString(rdngKwh), 6)
		                         + " " + leftJustify(ratecode, 1));
		            	 
	            }
           	 
	                         	            	           
	            totalKilowatthour += reading.getKilowatthour();  	           	            
	          
	         }           	     

	    }

	    private List<String> summaryFooter(){
	        Integer size = summary.size();
	        DecimalFormat df=new DecimalFormat("0.0");
	        UserProfile userProfile;
	        userProfile = dsUP.getUserProfile();
	        List<String> result = new ArrayList<String>();
	        //result.add(lineBreak());
	        result.add("\n"+centerJustify(rightJustify("TOTAL KWHr:",19)+rightJustify(df.format(totalKilowatthour),8)+rightJustify(" ",16), 42));
	        result.add("\n");
	        result.add("\n");
	        result.add("\nBilling Period: " + userProfile.getBillmonth());
	        result.add("\nMeter Reader  : " + userProfile.getName());                          
	        //result.add("\n" + centerJustify(rightJustify(size.toString(), 21) + "   " + rightJustify(df.format(totalKilowatthour), 6),47));
	        //result.add("\n" + centerJustify(leftJustify("Total Record Read : ", 21) + rightJustify(df.format(totalKilowatthour), 6)+ rightJustify(totalRead.toString(), 15), 42));
	        result.add("\n" + centerJustify(leftJustify("Total Record Read : ", 27)+ rightJustify(totalRead.toString(), 15), 42));
	        result.add("\n" + centerJustify(leftJustify("Total Record Unread : ", 32) + rightJustify(totalUnread.toString(), 10), 42));
	        result.add("\n" + centerJustify(leftJustify("Total Record : ", 32) + rightJustify(totalConsumer.toString(), 10), 42));	        
	        result.add("\n");
	        
	        result.add("\n  FM-CAD-03          00           07-01-19 ");	        
	        result.add(pageBreak());		
	
	        return result;
	    }

	    private String leftJustify(String stringToJustify, int length) {
	        String result = stringToJustify;
	        for (int i = 1; i <= length - stringToJustify.length(); i++){
	            result += " ";
	        }
	        return result;
	    }

	    private String rightJustify(String stringToJustify, int length) {
	        String result = "";
	        for (int i = 1; i <= length - stringToJustify.length(); i++){
	            result += " ";
	        }
	        result += stringToJustify;
	        return result;
	    }

	    private String centerJustify(String stringToJustify, int length) {
	        String result = "";
	        int left = (length - stringToJustify.length())/ 2;
	        int right = left;
	        if (length % 2 != 0) {
	            left += 1;
	        }
	        result += leftJustify("", left) + stringToJustify + rightJustify("", right);
	        return result;
	    }
	    private String lineBreak() {
	        String result = "";
	        result += "\n" + leftJustify("", 47).replace(" ", "-");
	        return result;
	    }

	    private String pageBreak() {
	        String result = "";
	        for (int i = 0; i <= 14; i++){
	            result += "\n";
	        }
	        return result;
	    }
}
