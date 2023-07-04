package com.tr.hsyn.phone_numbers;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Telefon numaraları üzerinde yapılan bazı işlemler sağlar.
 */
public class PhoneNumbers {
	
	/**
	 * Telefon numaraları için minimum uzunluk
	 */
	public static final  int             N_MIN                = 10;
	/**
	 * Telefon numaraları için maximum uzunluk
	 */
	public static final  int             N_MAX                = 15;
	public static final  String          NUMBER_TYPE_LOCAL    = "[0-9]{10,15}";// 0 543 493 7530
	public static final  String          NUMBER_TYPE_NATIONAL = "\\+[0-9]{12,15}";//+xxxxx5434937530
	private static final PhoneNumberUtil PHONE_NUMBER_UTIL    = PhoneNumberUtil.getInstance();
	
	private PhoneNumbers() {
		
	}
	
	public static PhoneNumberUtil.@Nullable PhoneNumberType getPhoneNumberType(String number) {
		
		if (Stringx.isNoboe(number)) return null;
		
		try {return PHONE_NUMBER_UTIL.getNumberType(PHONE_NUMBER_UTIL.parse(number, "ZZ"));}
		catch (NumberParseException e) {xlog.w(e);}
		
		return null;
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
			
			var code = getRegionCode(number);
			
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
		
		try {return PHONE_NUMBER_UTIL.getRegionCodeForNumber(PHONE_NUMBER_UTIL.parse(number, "ZZ"));}
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
		
		@NotNull String number = Stringx.trimWhiteSpaces(_number);
		
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
	 * For a string to be a phone number, it must fit certain models.
	 *
	 * @param number phone number
	 * @return {@code true} if the string is a phone number
	 */
	public static boolean isPhoneNumber(@NotNull String number) {
		
		return !getPhoneNumberFormatType(number).equals(PhoneNumberType.INVALID);
	}
	
	/**
	 * Eliminates all non-numeric characters except {@code '+'} sign.
	 *
	 * @param number string
	 * @return eliminated string
	 */
	@NotNull
	public static String getRealNumber(@NotNull String number) {
		
		return number.replaceAll("[^0-9+]", "");
	}
	
	/**
	 * Returns the type of the number format.
	 *
	 * @param number number
	 * @return number type or zero if invalid.
	 * @see #NUMBER_TYPE_LOCAL
	 * @see #NUMBER_TYPE_NATIONAL
	 */
	public static PhoneNumberType getPhoneNumberFormatType(@NotNull String number) {
		
		number = getRealNumber(number);
		
		if (PhoneNumberType.LOCAL.matches(number)) return PhoneNumberType.LOCAL;
		if (PhoneNumberType.NATIONAL.matches(number)) return PhoneNumberType.NATIONAL;
		return PhoneNumberType.INVALID;
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
	 * @param number Telefon numarası
	 * @param size   İstenen uzunluk
	 * @return Formatlı numara
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
	 * Minimum length must be {@link #N_MIN},
	 * and maximum length must be {@link #N_MAX} after eliminated non-numeric characters.
	 *
	 * @param number1 number
	 * @param number2 number
	 * @return {@code true} if numbers are equal
	 */
	public static boolean equals(@NotNull String number1, @NotNull String number2) {
		
		String rNum  = getRealNumber(number1);
		String rNum2 = getRealNumber(number2);
		
		if (rNum.length() < N_MIN || rNum.length() > N_MAX) return false;
		if (rNum2.length() < N_MIN || rNum2.length() > N_MAX) return false;
		
		if (rNum.equals(rNum2)) return true;
		
		String n1 = formatNumber(number1, N_MIN);
		String n2 = formatNumber(number2, N_MIN);
		
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
	
	public static String overWrite(@NotNull String number) {
		
		return overWrite(number, '*');
	}
	
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
	
	/**
	 * Types of phone numbers.
	 */
	public enum PhoneNumberType {
		/**
		 * Local number type.
		 */
		LOCAL(NUMBER_TYPE_LOCAL),
		/**
		 * National number type.
		 */
		NATIONAL(NUMBER_TYPE_NATIONAL),
		/**
		 * Invalid number type.
		 */
		INVALID("");
		
		/**
		 * Format of the phone number. This is a regular expression.
		 */
		private final String format;
		
		PhoneNumberType(String format) {
			
			this.format = format;
		}
		
		/**
		 * Returns the format of the phone number.
		 *
		 * @return format (regular expression)
		 */
		public String getFormat() {
			
			return format;
		}
		
		/**
		 * Checks if the number matches the format.
		 *
		 * @param number number
		 * @return {@code true} if the number matches
		 */
		public boolean matches(@NotNull String number) {
			
			return number.matches(format);
		}
	}
	
	
}