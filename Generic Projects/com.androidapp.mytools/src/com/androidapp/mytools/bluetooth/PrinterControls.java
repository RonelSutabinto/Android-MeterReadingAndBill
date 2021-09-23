package com.androidapp.mytools.bluetooth;

public class PrinterControls {
    
    public static MyPrinter btPrinter;
    
    public static String emphasized(boolean toggleEmphasized){
        if (toggleEmphasized){
            if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                    equals("APEX3"))
                return ExtechPrinterDriver.ENABLE_EMPHASIZED;
            else
                return Bixolon_SRP_R300_Driver.ENABLE_EMPHASIZED;
        } else {
            if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                    equals("APEX3"))
                return ExtechPrinterDriver.DISABLE_EMPHASIZED;
            else
                return Bixolon_SRP_R300_Driver.DISABLE_EMPHASIZED;
        }
    }

    public static String char48(){
        if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                equals("APEX3"))
            return ExtechPrinterDriver.CHAR48;
        else
            return Bixolon_SRP_R300_Driver.CHAR48;
    }


    public static String doubleHeight(boolean enable){
        if (enable)
            if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                    equals("APEX3"))
                return String.valueOf((char) 28);
            else
                return String.valueOf((char) 27 + "!9");
        else
            if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                    equals("APEX3"))
                return String.valueOf((char) 29);
            else
                return String.valueOf((char) 27 + "@");
    }
 /*   public static String char57(){
        if (btPrinter.getDeviceName().equals("EXTECH PRINTER") || btPrinter.getDeviceName().
                equals("APEX3"))
            return ExtechPrinterDriver.CHAR57;
        else
            return Bixolon_SRP_R300_Driver.CHAR57;
    }*/
    
}
