package com.tr.hsyn.perfectsort;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Sorting by string in programming is designed well for especially English alphabet.
 * This class provides a sort method to work well for the Turkish alphabet too.
 */
public class PerfectSort {
	
	/**
	 * Alphabet and numbers in ordered
	 */
	public static final  String                  ALPHABET = "abcçdefgğhıijklmnoöpqrsştuüvwxyz0123456789";
	/**
	 * Ranks of the alphabet and numbers
	 */
	private static final Map<Character, Integer> MAP      = new HashMap<>();
	
	static {
		
		for (int i = 0; i < ALPHABET.length(); i++) {
			
			MAP.put(ALPHABET.charAt(i), i);
		}
	}
	
	/**
	 * Compares two strings based on the {@link #ALPHABET}.
	 *
	 * @param s1 first string
	 * @param s2 second string
	 * @return {@code true} if the first one is smaller than the second one.
	 * 		If the strings are equal, {@code false} is returned.
	 * 		If the first string is {@code null} returns {@code false}.
	 * 		If the second string is {@code null} returns {@code true}.
	 */
	public static boolean isSmall(String s1, String s2) {
		
		return compare(s1, s2) < 0;
		
	}
	
	/**
	 * Compares two strings based on the {@link #ALPHABET}.
	 *
	 * @param s1 first string
	 * @param s2 second string
	 * @return {@code 0} if two strings are equal,
	 *      {@code 1} if the first one is bigger than the second one,
	 *      {@code -1} if the first one is smaller than the second one.
	 */
	public static int compare(String s1, String s2) {
		
		if (s1 == null) return 1;
		if (s2 == null) return -1;
		
		if (s1.equals(s2)) {return 0;}
		
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		
		int minLen = Math.min(s1.length(), s2.length());
		
		for (int i = 0; i < minLen; i++) {
			
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			
			if (c1 != c2) {
				
				if (isBig(c1, c2)) {return 1;}
				
				return -1;
			}
		}
		
		return s1.length() - s2.length();
	}
	
	/**
	 * Compares two characters based on the {@link #ALPHABET}.
	 *
	 * @param c1 first character
	 * @param c2 second character
	 * @return {@code true} if the first one is bigger than the second one.
	 */
	private static boolean isBig(char c1, char c2) {
		
		return compare(c1, c2) > 0;
	}
	
	/**
	 * Compares two characters based on the {@link #ALPHABET}.
	 *
	 * @param c1 first character
	 * @param c2 second character
	 * @return {@code 0} if two characters are equal,
	 *      {@code 1} if the first one is bigger than the second one,
	 *      {@code -1} if the first one is smaller than the second one.
	 */
	private static int compare(Character c1, Character c2) {
		
		Integer v1 = MAP.get(c1);
		
		if (v1 == null) {return 1;}
		
		Integer v2 = MAP.get(c2);
		
		if (v2 == null) {return -1;}
		
		return v1 - v2;
	}
	
	/**
	 * Sorts a list of strings based on the {@link #ALPHABET}.
	 *
	 * @param list list of strings
	 * @return sorted list or {@code null} if the list is {@code null}
	 */
	public static List<String> sort(List<String> list) {
		
		if (list == null) return null;
		list.sort(PerfectSort::compare);
		return list;
	}
	
	/**
	 * Returns a comparator that sorts on the {@link #ALPHABET}.
	 *
	 * @param keyExtractor key extractor to extract the string to compare
	 * @param <T>          type
	 * @return comparator
	 */
	public static <T> Comparator<T> stringComparator(Function<T, String> keyExtractor) {
		
		return (o1, o2) -> {
			
			String s1 = keyExtractor.apply(o1);
			String s2 = keyExtractor.apply(o2);
			
			return compare(s1, s2);
		};
	}
	
	
}
