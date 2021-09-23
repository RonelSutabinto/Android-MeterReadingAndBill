package com.androidapp.mytools.objectmanager;

import com.androidapp.mytools.bluetooth.PrinterControls;

public class StringManager {
	public static String Left(String text, int length){
		return text.substring(0, length);		
	}
	
	public static String Right(String text, int length){
		return text.substring(text.length() - length, length);		
	}
	
	public static String Mid(String text, int start, int end){
		return text.substring(start, start + end);		
	}
	
	public static String Mid(String text, int start){
		return text.substring(start, text.length() - start);		
	}

    public static String leftJustify(String stringToJustify, int length) {
        String result = stringToJustify;
        for (int i = 1; i <= length - stringToJustify.length(); i++){
            result += " ";
        }
        return result;
    }

    public static String rightJustify(String stringToJustify, int length) {
        String result = "";
        for (int i = 1; i <= length - stringToJustify.length(); i++){
            result += " ";
        }
        result += stringToJustify;
        return result;
    }

    public static String centerJustify(String stringToJustify, int length) {
        String result = "";
        int left = (length - stringToJustify.length())/ 2;
        int right = left;
        if (length % 2 != 0) {
            left += 1;
        }
        result += leftJustify("", left) + stringToJustify + rightJustify("", right);
        return result;
    }

    public static String lineBreak() {
        String result = "";
        result += leftJustify("", 48).replace(" ", "-") + "\n";
        return result;
    }

    public static String[] listTrimmer(String[] forTrimming){
        String[] trimmedString = new String[forTrimming.length];
        for (int i = 0; i <= forTrimming.length - 1; i++)
            trimmedString[i] = forTrimming[i].trim();
        return trimmedString;
    }

    public static String pageBreak() {
        String result = "";
        int limit;
        if (PrinterControls.btPrinter.getDeviceName().equals("SPP-R300"))
            limit = 3;
        else
            limit = 11;
        for (int i = 0; i <= limit; i++) {
            result += "\n";
        }
        return result;
    }

    public static String lineFeed() {
        String result = "";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        return result;
    }
    public static String formFeed() {
        String result = "";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        result += "\n";
        return result;
    }

    public static String SigLine() {
        String result = "";
        result += leftJustify("", 47).replace(" ", "_") + "\n";
        return result;
    }
}
