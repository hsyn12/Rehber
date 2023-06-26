package com.tr.hsyn.phone_numbers;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tr.hsyn.xlog.xlog;


public class _PhoneNumbers {
	
	private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
	
	public static void main(String[] args) {
		
		String[] numbers = {
				"5434937530",
				"05434937530",
				"905434937530",
				"+905434937530",
				"+19842407851"
		};
		
		for (String number : numbers) {
			System.out.println(number + " => " + format(number));
		}
	}
	
	public static String format(String number) {
		
		Phonenumber.PhoneNumber _number = null;
		
		try {
			_number = PHONE_NUMBER_UTIL.parse(number, null);
			
			log(PHONE_NUMBER_UTIL.getRegionCodeForNumber(_number));
			log(PHONE_NUMBER_UTIL.getNumberType(_number));
			return _number.getCountryCode() + "";
		}
		catch (NumberParseException e) {
			xlog.w(e);
		}
		
		
		return null;
	}
	
	public static void log(Object msg, Object... args) {
		
		System.out.printf((msg != null ? msg.toString() : "null") + "%n", args);
	}
	
}
