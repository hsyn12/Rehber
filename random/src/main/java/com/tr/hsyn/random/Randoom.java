package com.tr.hsyn.random;


import java.security.SecureRandom;


/**
 * It provides methods that generate random numbers or boolean values.
 */
public class Randoom {
	
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * @return random {@code true} or {@code false}
	 */
	public static boolean getBool() {
		
		return RANDOM.nextBoolean();
	}
	
	/**
	 * Give it a shot with a percentage.
	 *
	 * @param percent If it is 100 or greater than 100 it will definitely return {@code true}.
	 *                If it is 0 or less than 0 it will definitely return {@code false}
	 * @return {@code true} or {@code false} depending on the percentage.
	 */
	public static boolean getBool(int percent) {
		
		if (percent >= 100) return true;
		if (percent <= 0) return false;
		
		return getInt(100 / percent) == 0;
	}
	
	/**
	 * Random number.
	 *
	 * @param end End limit. (Not included)
	 * @return [0, end) a number in the range
	 */
	public static int getInt(int end) {
		
		return RANDOM.nextInt(end);
	}
	
	/**
	 * @return Random number
	 */
	public static int getInt() {
		
		return RANDOM.nextInt();
	}
	
	/**
	 * Generates a number in the range of two numbers.
	 *
	 * @param start The first number is the starting limit. It is included in the number to be produced.
	 * @param end   Second number ending limit. It is not included in the number to be produced.
	 * @return A random number in the range of two numbers
	 */
	public static int getInt(int start, int end) {
		
		return getInt(end - start) + start;
	}
	
	/**
	 * @return Random long
	 */
	public static long getLong() {
		
		return RANDOM.nextLong();
	}
	
	/**
	 * Generates a number in the range of two numbers.
	 *
	 * @param start The first number is the starting limit. It is included in the number to be produced.
	 * @param end   Second number ending limit. It is not included in the number to be produced.
	 * @return A random number in the range of two numbers
	 */
	public static long getLong(long start, long end) {
		
		return getLong(end - start) + start;
	}
	
	/**
	 * Random number.
	 *
	 * @param end End limit. (Not included)
	 * @return [0, end) a number in the range
	 */
	public static long getLong(long end) {
		
		return Math.abs(RANDOM.nextLong()) % end;
	}
	
}
