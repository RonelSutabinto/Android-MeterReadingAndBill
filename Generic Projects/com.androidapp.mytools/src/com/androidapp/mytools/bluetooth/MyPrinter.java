package com.androidapp.mytools.bluetooth;


import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
//import android.bluetooth.BluetoothSharedPrefs;
//com.androidapp.mytools.bluetooth.BluetoothSharedPrefs;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;

public class MyPrinter implements Runnable{

    protected static final String TAG = "Bluetooth Printing Class";

    protected Activity context;
    protected volatile BluetoothAdapter bta;
    protected volatile BluetoothDevice btd;
    protected volatile BluetoothSocket bts;
    protected Thread printThread;
    protected String codeentry;
    private BluetoothSharedPrefs prefsBluetooth;

    protected Bitmap logo;
    protected int logoWidth;
    protected int logoHeight;
    protected BitSet dots;
    protected boolean alwaysOn = false;

    protected String defaultMacAddress;
    protected String deviceName;
    protected OutputStream mmOutputStream;
    protected boolean codeprintCheck=false;
    protected boolean isPrint = false;
               
    private String deviceN="";
   
    protected UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805f9b34fb");//"00001101-0000-1000-8000-00805F9B34FB");

    protected volatile List<List<String>> printQue;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        this.deviceN = deviceName;
    }
            
    public void setMacAddress(String macAddress){
        this.defaultMacAddress = macAddress;
    }
    public boolean getCodeprintCheck(){
    	return codeprintCheck;
    }
    public void setCodeprintCheck(boolean printcode){
    	this.codeprintCheck = printcode;
    }
    public String getDeviceName(){
        return this.deviceName;
    }

    public String getDeviceN(){
    	return deviceN;
    }
        
    public MyPrinter(Activity context) {
        this.context = context;
        if (bta == null) {
            bta = BluetoothAdapter.getDefaultAdapter();
            if (bta == null) {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("No bluetooth hardware found in this unit");
                adb.setMessage("Could not proceed to printing");
                adb.setCancelable(false);
                adb.setPositiveButton("OK", null);
                AlertDialog ad = adb.create();
                ad.show();
            }
        }
        printQue = new ArrayList<List<String>>();
    }

    private void setPrintThread(){
        if (printThread == null)
            printThread = new Thread(this);
        else if (printThread.getState() == Thread.State.TERMINATED)
            printThread = new Thread(this);
        else if (printThread.getState() == Thread.State.RUNNABLE)
            printThread.run();
        if (printThread.getState() == Thread.State.NEW)
            printThread.start();
    }

    public void setAlwaysOn(boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
        if (alwaysOn){
            initBluetooth();
            connectSocket();
        }
    }

    public BluetoothDevice getBtd() {
        return btd;
    }

    public void setBluetoothDevice() {
        if (btd == null){
            new Thread(){
                public void run(){
                    while (bta.getState() != BluetoothAdapter.STATE_ON){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    btd = bta.getRemoteDevice(defaultMacAddress);
                }
            }.start();
        }
    }

    protected void closeSocket(BluetoothSocket bs) {
        if (!alwaysOn)
            try {
                if (bs != null)
                    bs.close();
                Log.d(TAG, "Socket closed");
            } catch (IOException e) {
                Log.d(TAG, "Could not close socket!");
            }
    }

    protected Boolean connectSocket() {
        try {
            while (btd == null)
                Thread.sleep(500);
            bta.cancelDiscovery();
            bts = btd.createRfcommSocketToServiceRecord(applicationUUID);
            bts.connect();
            return true;
        } catch (IOException e) {
            Log.e(TAG + ": Socket Connection", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void appendToQue(List<String> forPrinting){
        printQue.add(forPrinting);
      
    }

    public boolean print(List<String> forPrinting) {
        boolean result = false;
        setPrintThread();
        appendToQue(forPrinting);
        return result;
    }
    
    public void setisPrint(boolean isP){
    	this.isPrint = isP;
    }
    
    public boolean getisPrint(){
    	return this.isPrint;
    }
    public void print48H(List<String> forPrinting) {
    	// boolean result = false;
        setPrintThread();
        appendToQue(forPrinting);
        //return result;
    }

    public BluetoothAdapter getBta(){
        return bta;
    }

    public void turnOnBT(){
        if (bta == null) bta = BluetoothAdapter.getDefaultAdapter();
        if (bta.getState() == BluetoothAdapter.STATE_TURNING_OFF)
            new Thread(){
                public void run(){
                    bta = BluetoothAdapter.getDefaultAdapter();
                    while (bta.getState() != BluetoothAdapter.STATE_OFF)
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    bta.enable();
                }
            }.start();
        else if (bta.getState() == BluetoothAdapter.STATE_OFF) bta.enable();
    }

    public void turnOffBT(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!alwaysOn){
                    while (bta.getState() == BluetoothAdapter.STATE_TURNING_ON)
                        try {
                            Thread.sleep(500);//500
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    if (bta.isEnabled())
                        bta.disable();
                    bta = null;
                    if (bts != null)
                        try {
                            bts.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }
            }
        }).start();

    }
          
    //=============================
    //=============================
    public void openDialog(String str) {
        final Dialog dialog = new Dialog(context); // Context, this, etc.
        //  dialog.setContentView("Ronel");
        dialog.setTitle(str);
        dialog.show();
        //Log.e(TAG,"I shouldn't be here");
    }
    
    public void openBox(String str){       
        
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("No bluetooth hardware found in this unit");
                adb.setMessage(str);
                adb.setCancelable(false);
                adb.setPositiveButton("OK", null);
                AlertDialog ad = adb.create();
                ad.show();
        
        
    }
    //=============================
    //=============================
    public void runAPEX(){    	
    	byte fontL[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x10};    	
    	byte fontS[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x00};   
    	byte fontT_h[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x04}; //Horizontal
    	byte fontT_v[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x01};//vertical
    	Boolean printFlag = false;
    	int countPrint = 0;   	 
    	
        while (true){
            if (printQue.isEmpty()){
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (bta == null) bta = BluetoothAdapter.getDefaultAdapter();
                if (!alwaysOn && (bta.getState() == BluetoothAdapter.STATE_OFF || bta.getState() == BluetoothAdapter.
                        STATE_TURNING_OFF)){
                    initBluetooth();
                }
                while (!bta.isEnabled())
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                while (btd == null)
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                if (!alwaysOn)
                    connectSocket();
                /*if (!bts.isConnected()){
                    printQue.clear();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Please turnOn bluetooth printer..", Toast.LENGTH_LONG).show();
                            turnOffBT();
                        }
                    });
                    continue;
                } ---------------*/
                Integer iterator = 1;
                OutputStream os;
                try {
                    os = bts.getOutputStream();
                    countPrint = 0;                                               
                                       
                    while (!printQue.isEmpty()){
                    	                   	
                        for (String string : printQue.get(0)) {
                        	
                        	countPrint++;  
                        	
                        	//========Reading=====================
                        	//====================================
                        	if(( (countPrint==13) && (string != "N/A") && (codeprintCheck==true)) || (countPrint==9)){
                        		os.write((byte)0x0E);
                        	}else{
                        		os.write((byte)0x0F);
                        	}     
                        	
                        	//if ((countPrint == 6) && (codeprintCheck==true)) {
                        	if ((countPrint == 6) && (codeprintCheck==true)) {
                        		os.write((byte)0x0E);                        		
                        		os.write(getcodePrintAPEX());    
                        		os.write((byte)0x0F);
                        	}      
                        	//===================================
                        	//===================================
                        	
                        	//========Notice=====================
                        	/*if (countPrint == 8) {
                        		//os.write((byte)0x0E);
                        		//os.write(getcodePrintAPEX());    
                        		//os.write((byte)0x0F);
                        		
                        	}  */
                        	//===================================
                        	
                        	if((string==" Ako PaDayon Pilipino")||(string=="\n#152 "))
                        	{                       		
                        		os.write((byte)0x0E); //double wide
                        		os.write((byte)0x1C); //double high
                        		os.write(string.getBytes());                        		
                        		os.write((byte)0x1D); 
                        		os.write((byte)0x0F);
                        	}
                        	else if((string=="\n          Gikan sa...") || (string=="\n          Party List"))
                        	{
                        		os.write((byte)0x0E);                         		
                        		os.write(string.getBytes());                           		
                        		os.write((byte)0x0F);
                        	}
                        	else
                        	{
                        		os.write(string.getBytes());    
                        	} 
                        	 
	                        iterator += 1;
	                        if (iterator >= 6 && codeprintCheck==true) {
	                              Thread.sleep(500);	                        	  
	                              iterator = 0;
	                        }else if (iterator >= 11 && codeprintCheck==false) {
	                              Thread.sleep(800);	                        	  
	                              iterator = 0;
	                        }                            
	                         
                        	
                        }
                        printQue.remove(0);
                    }
                } catch (IOException e) {
                	this.isPrint = false;
                    Log.e(TAG + ": Printing", e.getMessage());
                    printQue.clear();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Please turnOn bluetooth printer..", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (InterruptedException e) {
                	this.isPrint = false;
                    Log.e(TAG + ": Interruption", e.getMessage());
                } finally {
                	
                    if (printQue.size() == 0){
                        closeSocket(bts);
                        if (!alwaysOn)
                            turnOffBT();
                    }
                }
            }
        }
                        	
                    
    }
    
    
    @Override
    public void run() { 
    	
    this.isPrint = true;
    
    String DName = "";     
    if(deviceName != null)
    	DName = deviceName;
    else 
    	DName ="";
    
	if(DName.length()>=8){
       DName = DName.substring(0,8); 
    }else{
       DName="";
    }
	
    //if (deviceName.substring(0,8).equals("SPP-R310")){    	
    if (DName.equals("SPP-R310")){ 	
    
    	Boolean printFlag = false;
    	int countPrint = 0;
    	byte printCenter[] = {(byte) 0x1B,(byte) 0x61,(byte) 0x01};
    	byte printLeft[] = {(byte) 0x1B,(byte) 0x61,(byte) 0x00};
    	byte fontL[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x10};    
    	byte fontLb[] = {(byte) 0x1B,(byte) 0x21,(byte) 0x32};    
    	byte fontS[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x00};    	
    	byte fontT_v[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x01}; //verticalcal
    	byte fontT_h[] = {(byte) 0x1d,(byte) 0x21,(byte) 0x10}; //Horizontal
    	byte[] endP  = {(byte) 0x0D,(byte) 0x0A,+
				        (byte) 0x1D,(byte) 0x42,(byte) 0x00 };  
    	byte boldFont[] = {(byte) 0x1B,(byte) 0x45,(byte) 0x01};
    	byte boldoff[]={(byte) 0x1B,(byte) 0x45,(byte) 0x00};
    	byte labelFont[] = {(byte) 0x08,(byte) 0x4C,(byte) 0x4C};
    	String subStr = "";
    	String[] separated;
    	
    	
        while (true){
            if (printQue.isEmpty()){
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (bta == null) bta = BluetoothAdapter.getDefaultAdapter();
                if (!alwaysOn && (bta.getState() == BluetoothAdapter.STATE_OFF || bta.getState() == BluetoothAdapter.
                        STATE_TURNING_OFF)){
                    initBluetooth();
                }
                while (!bta.isEnabled())
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                while (btd == null)
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                if (!alwaysOn)
                
                    connectSocket();
                /*if (!bts.isConnected()){
                    printQue.clear();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Please turnOn bluetooth printer..", Toast.LENGTH_LONG).show();
                            turnOffBT();
                        }
                    });
                    continue;
                } */
                Integer iterator = 1;
                OutputStream os;
               // String subStr = 
                try {
                    os = bts.getOutputStream();
                    countPrint = 0;
                                                           
                    while (!printQue.isEmpty()){
                    	                   	
                        for (String string : printQue.get(0)) {
                        	
                        	countPrint++;
                        	separated = string.split(" :"); 
                        	
                        	
                        	//===Reading==========================
                        	//os.write(endP)----not included; 
                        	if ((countPrint==6) && (codeprintCheck==true) || (countPrint==13) && (string != "N/A") && (codeprintCheck==true) || (countPrint==9)){                        		
                        		os.write(fontL);                        	
                        	}else if(separated[0].equals("Total Amount Due") || countPrint==11) {
                        		os.write(fontT_v);   
                        		
                        	}
                        	else{                        		
                        		os.write(fontS);                        		
                        	}//=================================== 
                        	
                        	
                        	//==========Notice ===================
                        	//os.write(fontS);
                        	//====================================
                            
                        	
                        	//=========Reading====================5
                        	if ((countPrint == 6) && (codeprintCheck==true)) {
                        		os.write(printCenter);//testing====
                        		os.write((byte)0x0E);                        		
                        		os.write(getcodePrint());    
                        		os.write((byte)0x0F);
                        		os.write(printLeft);//testing======                        	
                        	}//==================================
                        	
                        	/*
                        	//========Notice=====================
                        	if (countPrint == 6) {
                        		os.write((byte)0x0E);
                        		os.write(getcodePrint());    
                        		os.write((byte)0x0F);
                        		
                        	}  
                        	//==============================              	                    		
                        	*/
                        	//=============For Testing=========
                        	//if (countPrint == 6){
                        	//	os.write(deviceName.getBytes());       
                        	//}
                        	//=================================
                        	if((string=="\n       Atong Kooperatiba, atong suportahan.") || 
                        	   (string=="\n                   Party List"))
                        	{
                        		os.write(fontT_v);
                        		os.write(string.getBytes());
                        		//os.write(fontS);
                        	}                        		
                        	else if(string=="Ako PaDayon Pilipino")
                        	{                       		
                        		//os.write((byte)0x0E); //double wide
                        		//os.write((byte)0x1C); //double high
                        		//os.write(fontT_v);
                        		os.write(fontLb);
                        		os.write(string.getBytes());   
                        		//os.write(fontS);
                        		//os.write((byte)0x1D); 
                        		//os.write((byte)0x0F);
                        	}
                        	else if(string=="\n   Gikan sa...") 
                        	{
                        		//os.write((byte)0x0E);
                        		os.write(fontT_h);
                        		os.write(string.getBytes()); 
                        		//os.write(fontS);
                        		//os.write((byte)0x0F);
                        	}
                        	else if (string=="\n#152  ")
                        	{
                        		//os.write(boldFont);
                        		os.write(fontT_v);
                        		os.write(string.getBytes()); 
                        		//os.write(boldoff);
                        	}
                        	else
                        	{
                        		os.write(string.getBytes());    
                        	}
                        	//os.write(string.getBytes());                        	  
                        	
	                        iterator += 1;
	                        if (iterator >= 6 && codeprintCheck==true) {
	                              //Thread.sleep(500);
	                        	  Thread.sleep(100);
	                              iterator = 0;
	                        }else if (iterator >= 11 && codeprintCheck==false) {
	                              //Thread.sleep(800);
	                              Thread.sleep(200);
	                              iterator = 0;
	                        }                                                       
                        	
                        }
                        
                        printQue.remove(0);
                    }
                } catch (IOException e) {
                	this.isPrint = false;
                    Log.e(TAG + ": Printing", e.getMessage());
                    printQue.clear();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Please turnOn bluetooth printer..", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (InterruptedException e) {
                	this.isPrint = false;
                    Log.e(TAG + ": Interruption", e.getMessage());
                } finally {
                    if (printQue.size() == 0){
                        closeSocket(bts);
                        if (!alwaysOn)
                            turnOffBT();
                    }
                }
            }
        }
        
      }else{
    		runAPEX();
      }
    } 

    protected void initBluetooth(){
        turnOnBT();
        setBluetoothDevice();
    }

    public boolean isTurnedOn(){
        if (bta.getState() == BluetoothAdapter.STATE_ON) return true;
        else return false;
    }
    
    //===================================
    //===================================
    public void setcodeStrPrint(String tmp){     	
    	//mmOutputStream.write(APEXcode(tmp));   
    	codeentry = tmp;
    } 
    
    public byte[] getcodePrintAPEX(){ 
       	byte[] formats  = {(byte) 0x1B,(byte) 0x5A,(byte) 0x32,(byte) 0x0F,(byte) 0x64,(byte)0x88};    	
    	byte[] end      = {(byte) 0x0D,(byte) 0x0A};  	
    	byte[] tmpByte = new byte[1];
    	byte[] bytes = new byte[formats.length + codeentry.length() + end.length];   	
    	
    	switch(codeentry.length()){
    		case 1: formats[3]  = (byte)0x02; break;
    		case 2: formats[3]  = (byte)0x03; break;
    		case 3: formats[3]  = (byte)0x04; break;
    		case 4: formats[3]  = (byte)0x05; break;
    		case 5: formats[3]  = (byte)0x06; break;
    		case 6: formats[3]  = (byte)0x07; break;
    		case 7: formats[3]  = (byte)0x08; break;
    		case 8: formats[3]  = (byte)0x09; break;
    		case 9: formats[3]  = (byte)0x0A; break;
    		case 10: formats[3]  = (byte)0x0B; break;
    		case 11: formats[3]  = (byte)0x0C; break;
    		case 12: formats[3]  = (byte)0x0D; break;
    		case 13: formats[3]  = (byte)0x0E; break;
    		case 14: formats[3]  = (byte)0x0F; break;
		
    	}
    	
    	
    	System.arraycopy(formats, 0,bytes, 0, formats.length);
    	
    	for (int i=0; i < codeentry.length(); i++){ 
    		 
    		 switch(codeentry.charAt(i)){
    		 	case 'A': tmpByte[0] = (byte)0x41;
    		 		      System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 	          break;
    		 	case 'B': tmpByte[0] = (byte)0x42; 
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'C': tmpByte[0] = (byte)0x43; 
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case 'D': tmpByte[0] = (byte)0x44;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case 'E': tmpByte[0] = (byte)0x45;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'F': tmpByte[0] = (byte)0x46;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'G': tmpByte[0] = (byte)0x47;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'H': tmpByte[0] = (byte)0x48;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'I': tmpByte[0] = (byte)0x49;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case 'J': tmpByte[0] = (byte)0x4A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'K': tmpByte[0] = (byte)0x4B;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'L': tmpByte[0] = (byte)0x4C;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'M': tmpByte[0] = (byte)0x4D;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'N': tmpByte[0] = (byte)0x4E;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'O': tmpByte[0] = (byte)0x4F;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'P': tmpByte[0] = (byte)0x50;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Q': tmpByte[0] = (byte)0x51;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'R': tmpByte[0] = (byte)0x52;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'S': tmpByte[0] = (byte)0x53;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'T': tmpByte[0] = (byte)0x554;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'U': tmpByte[0] = (byte)0x55;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'V': tmpByte[0] = (byte)0x56;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'W': tmpByte[0] = (byte)0x57;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'X': tmpByte[0] = (byte)0x58;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Y': tmpByte[0] = (byte)0x59;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Z': tmpByte[0] = (byte)0x5A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'a': tmpByte[0] = (byte)0x61;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'b': tmpByte[0] = (byte)0x62;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'c': tmpByte[0] = (byte)0x63;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'd': tmpByte[0] = (byte)0x64;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'e': tmpByte[0] = (byte)0x65;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'f': tmpByte[0] = (byte)0x66;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'g': tmpByte[0] = (byte)0x67;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'h': tmpByte[0] = (byte)0x68;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'i': tmpByte[0] = (byte)0x69;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'j': tmpByte[0] = (byte)0x6A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'k': tmpByte[0] = (byte)0x6B;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'l': tmpByte[0] = (byte)0x6C;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'm': tmpByte[0] = (byte)0x6D;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'n': tmpByte[0] = (byte)0x6E;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'o': tmpByte[0] = (byte)0x6F;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'p': tmpByte[0] = (byte)0x70;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'q': tmpByte[0] = (byte)0x71;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'r': tmpByte[0] = (byte)0x72;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 's': tmpByte[0] = (byte)0x73;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 't': tmpByte[0] = (byte)0x74;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'u': tmpByte[0] = (byte)0x75;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'v': tmpByte[0] = (byte)0x76;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'w': tmpByte[0] = (byte)0x77;
	 			  		  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
	 			  		  break;
    		 	case 'x': tmpByte[0] = (byte)0x78;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'y': tmpByte[0] = (byte)0x79;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'z': tmpByte[0] = (byte)0x7A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case '1': tmpByte[0] = (byte)0x31;
    		 		      System.arraycopy(tmpByte, 0,bytes, formats.length+i,1); 
    		 		      break;
    		 	case '2': tmpByte[0] = (byte)0x32;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case '3': tmpByte[0] = (byte)0x33;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 		      break;
    		 	case '4': tmpByte[0] = (byte)0x34;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 			  break;
    		 	case '5': tmpByte[0] = (byte)0x35;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 			  break;
    		 	case '6': tmpByte[0] = (byte)0x36;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case '7': tmpByte[0] = (byte)0x37;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '8': tmpByte[0] = (byte)0x38;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '9': tmpByte[0] = (byte)0x39;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '0': tmpByte[0] = (byte)0x30;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '*': tmpByte[0] = (byte)0x2A;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    	 }  }
    	
    	System.arraycopy(end, 0, bytes, formats.length+codeentry.length(),end.length);
    	return bytes;	 
    }
    public byte[] getcodePrint(){      	
     	    	
    	//==========================
    	//==========================    	  	
    	
    	byte[] formats = {(byte) 0x1d,(byte) 0x68,(byte) 0x60,+
    			          (byte) 0x1d,(byte) 0x48,(byte) 0x02,+
    			          (byte) 0x1d,(byte) 0x66,(byte) 0x00,+    			        
    	    	          (byte) 0x1d,(byte) 0x6b,(byte) 0x49,(byte) 0x08  };   	
    	/*
    	//QR Code
    	byte[] formats = {(byte) 0x1d,(byte) 0x28,(byte) 0x6b,(byte) 0x04,(byte) 0x00,(byte) 0x31,(byte) 0x41,(byte) 0x31,(byte) 0x00,
    			            (byte) 0x1d,(byte) 0x28,(byte) 0x6b,(byte) 0x03,(byte) 0x00,(byte) 0x31,(byte) 0x43,(byte) 0x07,//size adjustment
    			            (byte) 0x1d,(byte) 0x28,(byte) 0x6b,(byte) 0x03,(byte) 0x00,(byte) 0x31,(byte) 0x45,(byte) 0x30,
    			            (byte) 0x1d,(byte) 0x28,(byte) 0x6b,(byte) 0x03,(byte) 0x00,(byte) 0x31,(byte) 0x51,(byte) 0x30};*/
    	         	
    	
    	byte[] end      = {(byte) 0x0D,(byte) 0x0A,+
    					   (byte) 0x1D,(byte) 0x42,(byte) 0x00 };  
    	
    	byte[] tmpByte = new byte[1];
    	byte[] bytes = new byte[formats.length + codeentry.length() + end.length];   	
    	
    	switch(codeentry.length()){
    		case 1: formats[12]  = (byte)0x01; break;
    		case 2: formats[12]  = (byte)0x02; break;
    		case 3: formats[12]  = (byte)0x03; break;
    		case 4: formats[12]  = (byte)0x04; break;
    		case 5: formats[12]  = (byte)0x05; break;
    		case 6: formats[12]  = (byte)0x06; break;
    		case 7: formats[12]  = (byte)0x07; break;
    		case 8: formats[12]  = (byte)0x08; break;
    		case 9: formats[12]  = (byte)0x09; break;
    		case 10: formats[12]  = (byte)0x0A; break;
    		case 11: formats[12]  = (byte)0x0B; break;
    		case 12: formats[12]  = (byte)0x0C; break;
    		case 13: formats[12]  = (byte)0x0D; break;
    		case 14: formats[12]  = (byte)0x0E; break;		
    	}
    	
    	
    	System.arraycopy(formats, 0,bytes, 0, formats.length);
    	
    	for (int i=0; i < codeentry.length(); i++){ 
    		 
    		 switch(codeentry.charAt(i)){
    		 	case 'A': tmpByte[0] = (byte)0x41;
    		 		      System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 	          break;
    		 	case 'B': tmpByte[0] = (byte)0x42; 
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'C': tmpByte[0] = (byte)0x43; 
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case 'D': tmpByte[0] = (byte)0x44;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case 'E': tmpByte[0] = (byte)0x45;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'F': tmpByte[0] = (byte)0x46;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'G': tmpByte[0] = (byte)0x47;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'H': tmpByte[0] = (byte)0x48;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'I': tmpByte[0] = (byte)0x49;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case 'J': tmpByte[0] = (byte)0x4A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'K': tmpByte[0] = (byte)0x4B;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'L': tmpByte[0] = (byte)0x4C;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'M': tmpByte[0] = (byte)0x4D;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'N': tmpByte[0] = (byte)0x4E;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'O': tmpByte[0] = (byte)0x4F;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'P': tmpByte[0] = (byte)0x50;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Q': tmpByte[0] = (byte)0x51;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'R': tmpByte[0] = (byte)0x52;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'S': tmpByte[0] = (byte)0x53;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'T': tmpByte[0] = (byte)0x554;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'U': tmpByte[0] = (byte)0x55;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'V': tmpByte[0] = (byte)0x56;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'W': tmpByte[0] = (byte)0x57;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'X': tmpByte[0] = (byte)0x58;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Y': tmpByte[0] = (byte)0x59;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'Z': tmpByte[0] = (byte)0x5A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'a': tmpByte[0] = (byte)0x61;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'b': tmpByte[0] = (byte)0x62;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'c': tmpByte[0] = (byte)0x63;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'd': tmpByte[0] = (byte)0x64;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'e': tmpByte[0] = (byte)0x65;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'f': tmpByte[0] = (byte)0x66;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'g': tmpByte[0] = (byte)0x67;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'h': tmpByte[0] = (byte)0x68;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'i': tmpByte[0] = (byte)0x69;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'j': tmpByte[0] = (byte)0x6A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'k': tmpByte[0] = (byte)0x6B;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'l': tmpByte[0] = (byte)0x6C;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'm': tmpByte[0] = (byte)0x6D;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'n': tmpByte[0] = (byte)0x6E;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'o': tmpByte[0] = (byte)0x6F;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'p': tmpByte[0] = (byte)0x70;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'q': tmpByte[0] = (byte)0x71;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'r': tmpByte[0] = (byte)0x72;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 's': tmpByte[0] = (byte)0x73;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 't': tmpByte[0] = (byte)0x74;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'u': tmpByte[0] = (byte)0x75;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'v': tmpByte[0] = (byte)0x76;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'w': tmpByte[0] = (byte)0x77;
	 			  		  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
	 			  		  break;
    		 	case 'x': tmpByte[0] = (byte)0x78;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'y': tmpByte[0] = (byte)0x79;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case 'z': tmpByte[0] = (byte)0x7A;
    		 			  System.arraycopy(tmpByte, 0,bytes, formats.length+i, 1); 
    		 			  break;
    		 	case '1': tmpByte[0] = (byte)0x31;
    		 		      System.arraycopy(tmpByte, 0,bytes, formats.length+i,1); 
    		 		      break;
    		 	case '2': tmpByte[0] = (byte)0x32;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case '3': tmpByte[0] = (byte)0x33;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 		      break;
    		 	case '4': tmpByte[0] = (byte)0x34;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 			  break;
    		 	case '5': tmpByte[0] = (byte)0x35;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i,1); 
    		 			  break;
    		 	case '6': tmpByte[0] = (byte)0x36;
    		 		      System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 		      break;
    		 	case '7': tmpByte[0] = (byte)0x37;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '8': tmpByte[0] = (byte)0x38;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '9': tmpByte[0] = (byte)0x39;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '0': tmpByte[0] = (byte)0x30;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 	case '*': tmpByte[0] = (byte)0x2A;
    		 			  System.arraycopy(tmpByte, 0,bytes,formats.length+i, 1); 
    		 			  break;
    		 }  
    		 
    	 }   	
    	
     	//System.arraycopy(contents, 0, bytes, formats.length, contents.length);
     	System.arraycopy(end, 0, bytes, formats.length+codeentry.length(),end.length);
    	//System.arraycopy(end, 0, bytes, formats.length,end.length);
     	
    	return bytes;
    	
    }

}
