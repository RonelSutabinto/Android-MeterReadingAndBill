package com.generic.notice48hrs;

public class StringHelper {
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
		result += leftJustify("", 47).replace(" ", "-") + "\n";
		return result;
	}
	
	public static String SigLine() {
		String result = "";
		result += leftJustify("", 47).replace(" ", "_") + "\n";
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
}
