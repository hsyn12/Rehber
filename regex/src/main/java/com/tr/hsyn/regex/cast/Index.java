package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Represents a place in a string, (or something like)
 * that pointing to the start and end indexes.
 * The object points to the part of something.
 * This 'something' is a string mostly.
 * Moreover, there are methods to work directly with the strings.<br><br>
 * <p>
 * As like be {@link String}, {@code -1} represents an invalid index.
 */
public class Index {
	
	/**
	 * Invalid index.
	 */
	public static final Index INVALID_INDEX = new Index(-1, -1);
	
	/**
	 * Start index. (inclusive)
	 */
	public final int start;
	/**
	 * End index. (exclusive)
	 */
	public final int end;
	
	/**
	 * Creates a new index.
	 *
	 * @param start start index
	 * @param end   end index
	 */
	public Index(int start, int end) {
		
		this.start = start;
		this.end   = end;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Index{" + "start=" + start + ", end=" + end + '}';
	}
	
	/**
	 * Returns a substring of the input.
	 *
	 * @param str the input string
	 * @return the substring or empty string if the index is invalid
	 */
	public String substringOf(@NotNull String str) {
		
		if (isInvalid()) return "";
		return str.substring(start, end);
	}
	
	/**
	 * Returns a substring of the input string starting from the specified index to the end of the string.
	 * Like {@link String#substring(int, int)}.
	 *
	 * @param str the input string to extract the substring from
	 * @return the substring of the input string starting from the specified index to the end of the string.
	 */
	public String stringAfterStart(@NotNull String str) {
		
		return str.substring(start);
	}
	
	/**
	 * Returns a substring of the input string after the end index to the end of the string.
	 * Like {@link String#substring(int)}
	 *
	 * @param str the input string to extract the substring from
	 * @return the substring of the input string after the specified end index to the end of the string.
	 */
	public String stringAfterEnd(@NotNull String str) {
		
		return str.substring(end);
	}
	
	/**
	 * Tests if the start index and end index are valid.
	 *
	 * @return {@code true} if the start index and end index are valid, false otherwise.
	 */
	public boolean isValid() {
		
		return start != -1 && end != -1;
	}
	
	/**
	 * Tests if the start index is valid.
	 *
	 * @return {@code true} if the start index is valid, false otherwise.
	 */
	public boolean isValidStart() {
		
		return start != -1;
	}
	
	/**
	 * Tests if the end index is valid.
	 *
	 * @return {@code true} if the end index is valid, false otherwise.
	 */
	public boolean isValidEnd() {
		
		return end != -1;
	}
	
	/**
	 * Tests if the index is invalid.
	 *
	 * @return {@code true} if the index is invalid, false otherwise.
	 */
	public boolean isInvalid() {
		
		return start == -1 && end == -1;
	}
	
	/**
	 * Returns a formatted string.
	 *
	 * <pre>
	 *    var index = Index.of(1,3);
	 *    index.toString("%s:%s"); // 1:3
	 *    index.toString("%s-%s"); // 1-3
	 *    index.toString("%2$s --> %1$s"); // 3 --> 1
	 * </pre>
	 *
	 * @param format Format
	 * @return String
	 */
	public String toString(@NotNull String format) {
		
		return String.format(format, start, end);
	}
	
	/**
	 * Creates a new index.
	 *
	 * @param start start index
	 * @param end   end index
	 * @return new index object
	 */
	@NotNull
	public static Index of(int start, int end) {
		
		return new Index(start, end);
	}
	
	/**
	 * Returns the invalid index object.
	 */
	@NotNull
	public static Index ofInvalid() {
		
		return INVALID_INDEX;
	}
}
