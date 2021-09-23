package com.androidapp.mytools.objectmanager;

import java.text.DecimalFormat;

public class DoubleManager {
	public static Double rRound(Double amount) {
		/*String AmountStr="0.0";
		DecimalFormat formatDemand = new DecimalFormat("########0.00");
		AmountStr = formatDemand.format(amount);
		
		return  Double.parseDouble(AmountStr);*/
		String result = String.format("%.4f", amount);
		
		if (Integer.parseInt(result.substring(result.indexOf(".") + 3, result.indexOf(".") + 4)) >= 5) {
			if (amount < 0) {
				return Double.parseDouble(result.substring(0, result.indexOf(".") + 3)) + -0.01;
			} else {
				return Double.parseDouble(result.substring(0, result.indexOf(".") + 3)) + 0.01;
			}
		} else {
			return Double.parseDouble(result.substring(0, result.indexOf(".") + 3));
		}
	} 
}
