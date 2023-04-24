package com.tr.xyz.countrycodes;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class PhoneNumbers {
	
	/**
	 * Returns the country code of a given phone number.
	 *
	 * @param phoneNumber A non-null string representing a phone number.
	 * @return An integer representing the country code of the given phone number.
	 */
	public static int getCountryCode(@NotNull String phoneNumber) {
		
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "");
			return parsedNumber.getCountryCode();
		}
		catch (Exception ignore) {}
		return 0;
	}
	
	public static int getCountryCode(@NotNull String phoneNumber, String country) {
		
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, country);
			return parsedNumber.getCountryCode();
		}
		catch (Exception ignore) {}
		return 0;
	}
	
	/**
	 * This function formats a given phone number to a standardized format.
	 * It removes any non-numeric characters from the input string and adds a country code if necessary.
	 * If the input string is already in the correct format, it is returned as is.
	 *
	 * @param phoneNumber The phone number to be formatted.
	 * @return The formatted phone number as a string.
	 */
	public static @NotNull String format(@NotNull String phoneNumber) {
		
		if (phoneNumber.startsWith("+")) return phoneNumber;
		
		String number = phoneNumber.replaceAll("[^0-9]", "");
		
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(number, Locale.getDefault().getCountry());
			return ("+" + parsedNumber.getCountryCode()) + parsedNumber.getNationalNumber();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return phoneNumber;
	}
	
	
}