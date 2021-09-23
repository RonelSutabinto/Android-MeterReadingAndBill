package com.androidapp.mytools.bluetooth;

public class ExtechPrinterDriver {
    public final static String CHAR72 = String.valueOf((char) 27 + "k" + "5");  // 72 Char
    public final static String CHAR57 = String.valueOf((char) 27 + "k" + "3");  // 57 Char
    public final static String CHAR48 = String.valueOf((char) 27 + "k" + "2");  // 48 Char Default
    public final static String ENABLE_EMPHASIZED = String.valueOf((char)27 + "U" + "1");
    public final static String DISABLE_EMPHASIZED = String.valueOf((char) 27 + "U" + "0");
    public final static String ENABLE_DOUBLE_SIZE = String.valueOf((char) 28);
    public final static String DISABLE_DOUBLE_SIZE = String.valueOf((char) 29);

}
