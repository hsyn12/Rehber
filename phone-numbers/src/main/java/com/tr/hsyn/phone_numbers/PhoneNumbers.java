package com.tr.hsyn.phone_numbers;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Provides some helper methods for phone numbers to format, validate and compare.
 * According to the <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> standard,<br>
 *
 * <table style="padding:15px; border-collapse:collapse;">
 *    <tr style="border-bottom:1px solid #ccc;border-collapse:collapse;">
 *       <th style="text-align:center;padding:10px">Country Code</th>
 *       <th style="text-align:center;padding:10px">Subscriber Number</th>
 *    </tr>
 *    <tr style="border-right:0 solid #ccc;">
 *       <td style="text-align:center;">1 to 3 digits</td>
 *       <td style="text-align:center;">12 to 14 digits</td>
 *    </tr>
 * </table>
 * <br>
 * <p>
 * So, the maximum phone number length is 15 digits with country code,
 * and the minimum length is 12 digits without country code.
 * A string with 12 digits can be a phone number.
 * And we do not care about the other options,
 * we are not an operator.<br><br>
 * <p>
 * This class was considered for phone numbers that are in <strong>Turkey</strong> especially.
 * For example, in Turkey, the subscriber number length is 10 digits.
 * Caution, the above table shows only country code and subscriber number length,
 * there is another code number between these two numbers.
 * It is area code.
 * The above table combines the area code and subscriber number.
 * So the subscriber number includes the area code.
 * If we remove the area code from the subscriber number,
 * only the subscriber number left that consists of 10 digits.
 * So, the {@link #MINIMUM_NUMBER_LENGTH} constant have been set to 10.
 * In this way, we can create a unique value from a phone number with 10 digits.
 * The last 10 digits from a number in Turkey are unique.
 * Maybe other countries are proper for this too, or not.<br><br>
 *
 * <p>
 * Phone numbers are numbers.
 * This is enough for a lot of situations.
 */
public class PhoneNumbers {
	
	/**
	 * The minimum phone number length.
	 */
	public static final int    MINIMUM_NUMBER_LENGTH = 10;
	/**
	 * The maximum phone number length.
	 */
	public static final int    MAXIMUM_NUMBER_LENGTH = 15;
	/**
	 * The regular expression for phone number.
	 */
	public static final String PHONE_NUMBER_REGEX    = "\\+[0-9]{10,15}";//+xxxxx5434937530;
	
	private PhoneNumbers() {
		
	}
	
	public static void main(String[] args) {
		
		
		String[] numbers = {
				"+9235434937530",
				"+905434937530",
				"905434937530",
				"05434937530",
				"5434937530",
				"434937530",
		};
		
		for (String number : numbers) {
			
			String code = getRegionCode(number);
			
			if (code != null) {
				
				System.out.printf("%s -> %s\n", number, getRegionCode(number));
			}
		}
	}
	
	/**
	 * Returns the region code that consists of two upper case letters for the given number.
	 * The given phone number must be having the region number.<br><br>
	 *
	 * <pre>
	 *    {@code getRegionCode("05434937530");} // null
	 *    {@code getRegionCode("+905434937530");} // 'TR'
	 * </pre>
	 *
	 * @param number number
	 * @return region code
	 */
	@Nullable
	public static String getRegionCode(String number) {
		
		if (Stringx.isNoboe(number)) return null;
		
		PhoneNumberUtil u = PhoneNumberUtil.getInstance();
		
		try {return u.getRegionCodeForNumber(u.parse(number, "ZZ"));}
		catch (NumberParseException e) {xlog.w(e);}
		
		return null;
	}
	
	/**
	 * Returns a more legible number by inserting a space between numbers.<br>
	 * <pre>
	 * {@code beautifyNumber("5434937530")}     // 543 493 7530
	 * {@code beautifyNumber("05434937530")}    // 0543 493 7530
	 * {@code beautifyNumber("905434937530")}   // 90 543 493 7530
	 * {@code beautifyNumber("+905434937530")}  // +90 543 493 7530
	 * <pre>
	 *
	 * @param _number number
	 * @return formatted number
	 */
	public static String beautifyNumber(@NotNull String _number) {
		
		if (!isPhoneNumber(_number)) return _number;
		
		@NotNull String number = Stringx.removeAllWhiteSpaces(_number);
		
		if (number.length() == 11) {
			
			return String.format("%s %s %s", number.substring(0, 4), number.substring(4, 7), number.substring(7));
		}
		
		if (number.length() == 12) {
			
			return String.format("%s %s %s %s", number.substring(0, 2), number.substring(2, 5), number.substring(5, 8), number.substring(8));
		}
		
		if (number.length() == 13) {
			
			return String.format("%s %s %s %s", number.substring(0, 3), number.substring(3, 6), number.substring(6, 9), number.substring(9));
		}
		if (number.length() == 10) {
			
			return String.format("%s %s %s", number.substring(0, 3), number.substring(3, 6), number.substring(6));
		}
		
		return _number;
	}
	
	/**
	 * Checks if the string is a phone number.
	 *
	 * @param number phone number
	 * @return {@code true} if the string is a phone number
	 * @see #PHONE_NUMBER_REGEX
	 */
	public static boolean isPhoneNumber(@NotNull String number) {
		
		return normalize(number).matches(PHONE_NUMBER_REGEX);
	}
	
	/**
	 * Removes all non-numeric characters except {@code '+'} sign.
	 *
	 * @param number string
	 * @return eliminated string
	 */
	@NotNull
	public static String normalize(@NotNull String number) {
		
		return number.replaceAll("[^0-9+]", "");
	}
	
	/**
	 * Checks if the list of numbers has the given number.
	 * Equality is done with {@link #equalsOrContains(String, String)}.
	 *
	 * @param numbers list of numbers
	 * @param number  number
	 * @return {@code true} if the list has the number
	 */
	public static boolean containsNumber(List<String> numbers, String number) {
		
		if (numbers == null || number == null) return false;
		
		for (String n : numbers) {
			
			if (equalsOrContains(n, number)) return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the list of numbers has the given number.
	 * Equality is done with {@link #equals(String, String)}.
	 *
	 * @param numbers list of numbers
	 * @param number  number
	 * @return {@code true} if the list has the number
	 */
	public static boolean existsNumber(List<String> numbers, String number) {
		
		if (numbers == null || number == null) return false;
		
		for (String n : numbers) {
			
			if (equals(n, number)) return true;
		}
		
		return false;
	}
	
	/**
	 * Compares two numbers for equality or inclusion.<br>
	 * Spaces and non-numeric characters are not important,
	 * only numerical sections are compared.<br><br>
	 *
	 *
	 * <pre>equalsNumbers("+90543 4937530", "5 4 3 4 9 3 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("+90(543) 4937530", "5 4 3 4 9 3 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("90(543) 4937530", "05 4 3 4 (9 3) 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("90(543)s 4937530", "05 4 3 4 (9 3) 7 5 3 0";// true</pre>
	 * <pre>equalsNumbers("5 ", " 5");// true</pre><br>
	 *
	 * @param number1 number1
	 * @param number2 number2
	 * @return {@code true} if numbers are equal or contained in each other
	 */
	public static boolean equalsOrContains(@NotNull String number1, @NotNull String number2) {
		
		if (number1.contains(number2) || number2.contains(number1))
			return true;
		
		@NotNull String n1 = formatNumber(number1, 10);
		@NotNull String n2 = formatNumber(number2, 10);
		
		if (n1.equals(n2)) return true;
		
		if (n1.length() >= n2.length()) return n1.contains(n2);
		
		return n2.contains(n1);
	}
	
	/**
	 * Formats the number to the given size.
	 * Eliminates all non-numeric characters and
	 * trims the number from the left to the given size.<br><br>
	 * <p>
	 * {@code format("+90 543 493 7530", 10);} // "5434937530"<br>
	 * {@code format("+90 543 493 7530", 7);} //  "4937530"<br>
	 *
	 * @param number phone number
	 * @param size   number size
	 * @return formatted number
	 */
	@NotNull
	public static String formatNumber(@NotNull String number, int size) {
		
		number = Stringx.trimNonDigits(number);
		
		if (number.length() <= size) return number;
		
		return number.substring(number.length() - size);
	}
	
	/**
	 * Checks if two numbers are equal.
	 * Spaces and non-numeric characters are not important,
	 * only numerical sections are compared.
	 * Minimum length must be {@link #MINIMUM_NUMBER_LENGTH},
	 * and maximum length must be {@link #MAXIMUM_NUMBER_LENGTH} after eliminated non-numeric characters.
	 *
	 * @param number1 number
	 * @param number2 number
	 * @return {@code true} if numbers are equal
	 */
	public static boolean equals(@NotNull String number1, @NotNull String number2) {
		
		String rNum  = normalize(number1);
		String rNum2 = normalize(number2);
		
		if (rNum.length() < MINIMUM_NUMBER_LENGTH || rNum.length() > MAXIMUM_NUMBER_LENGTH)
			return false;
		if (rNum2.length() < MINIMUM_NUMBER_LENGTH || rNum2.length() > MAXIMUM_NUMBER_LENGTH)
			return false;
		
		if (rNum.equals(rNum2)) return true;
		
		String n1 = formatNumber(number1, MINIMUM_NUMBER_LENGTH);
		String n2 = formatNumber(number2, MINIMUM_NUMBER_LENGTH);
		
		if (n1.length() != n2.length()) return false;
		
		return n1.equals(n2);
	}
	
	/**
	 * Checks two phone number lists for equality.
	 * The location of phone numbers in the list does not matter.<br><br>
	 *
	 * <pre>
	 * String n1 = "05434937530";
	 * String n2 = "5434937530";
	 *
	 * List<String> l1 = Lists.newArrayList(n1, "5");
	 * List<String> l2 = Lists.newArrayList("5", n2);
	 * equals(l1, l2); // true</pre>
	 *
	 * @param numbers1 numbers
	 * @param numbers2 numbers
	 * @return {@code true} if lists are equal
	 */
	public static boolean equalsOrContains(@NotNull List<String> numbers1, @NotNull List<String> numbers2) {
		
		if (numbers1.size() == numbers2.size()) {
			
			int match = 0;
			
			for (String n1 : numbers1) {
				
				for (String n2 : numbers2) {
					
					if (equalsOrContains(n1, n2)) match++;
				}
			}
			
			return match == numbers1.size();
		}
		
		return false;
	}
	
	/**
	 * Returns the phone number with the masked until the last four digits.
	 *
	 * @param number phone number
	 * @return masked phone number
	 */
	public static String overWrite(@NotNull String number) {
		
		return overWrite(number, '*');
	}
	
	/**
	 * Returns the phone number with the masked until the last four digits.
	 *
	 * @param number phone number
	 * @param over   character to mask
	 * @return masked phone number
	 */
	public static String overWrite(@NotNull String number, char over) {
		
		if (isPhoneNumber(number)) {
			
			number = Stringx.trimNonDigits(number);
			StringBuilder builder = new StringBuilder();
			
			for (int i = 0; i < number.length() - 4; i++) {
				
				builder.setCharAt(i, over);
			}
			
			return builder.toString();
		}
		
		return Stringx.overWrite(number, over);
	}
	
	
}