package com.androidapp.mytools.objectmanager;

public class ArrayManager {
	public static String[] concat(String[] first, String[] second){
		int firstLen = first.length;
		int secLen = second.length;
		String[] all = new String[firstLen + secLen];
		System.arraycopy(first, 0, all, 0, firstLen);
		System.arraycopy(second, 0, all, firstLen, secLen);
		return all;
	}
}
