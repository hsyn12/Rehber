package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


@SuppressWarnings("DefaultLocale")
public interface Quantifier {
	
	int KEY_OPTINAL = 0;
	int KEY_PLUS    = 1;
	int KEY_STAR    = 2;
	int KEY_SIZE    = 3;
	
	/**
	 * Means optional match.
	 * It is used to match zero or one occurrence of the preceding element.
	 * It is also used for lazy matching
	 */
	String ZERO_OR_ONE  = "?";
	/**
	 * Matches zero or more occurrences of the preceding character or group
	 */
	String ZERO_OR_MORE = "*";
	/**
	 * Matches one or more occurrences of the preceding element
	 */
	String ONE_OR_MORE  = "+";
	
	/**
	 * Checks quantifier.
	 *
	 * @param s the string to check
	 * @return true if the string is a quantifier
	 */
	static boolean isQuantifier(@NotNull String s) {
		
		return s.equals(ZERO_OR_ONE) || s.equals(ZERO_OR_MORE) || s.equals(ONE_OR_MORE);
	}
	
	/**
	 * Bir karakterin, çokluk bildiren bir düzenli ifade karakteri olup olmadığını bildirir.<br>
	 *
	 * <ul>
	 *    <li>{@code '?'} sıfır yada bir tane</li>
	 *    <li>{@code '*'}  sıfır yada daha fazla</li>
	 *    <li>{@code '+'}  bir yada daha fazla</li>
	 * </ul>
	 *
	 * @param s the character to check
	 * @return true if the character is a quantifier
	 */
	static boolean isQuantifier(char s) {
		
		return s == '?' || s == '*' || s == '+';
	}
	
	static String AT_LEAST(int count) {
		
		return String.format("{%d,}", count);
	}
	
	static String AT_MOST(int count) {
		
		return String.format("{0,%d}", count);
	}
	
	static String BETWEEN(int min, int max) {
		
		return String.format("{%d,%d}", min, max);
	}
	
	static String EXACTLY(int count) {
		
		return String.format("{%d}", count);
	}
	
}