package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Bir string içinde bir yer bildirir.
 */
public class Index {
	
	public static final Index INVALID_INDEX = new Index(-1, -1);
	
	/**
	 * Başlangıç index'i (dahil)
	 */
	public final int start;
	/**
	 * Bitiş index'i (hariç)
	 */
	public final int end;
	
	/**
	 * Yeni bir index nesnesi oluturur.
	 *
	 * @param start Başlangıç index'i  (dahil)
	 * @param end   Bitiş index'i (hariç)
	 */
	public Index(int start, int end) {
		
		this.start = start;
		this.end   = end;
	}
	
	/**
	 * Yeni bir index nesnesi oluturur.
	 *
	 * @param start Başlangıç index'i  (dahil)
	 * @param end   Bitiş index'i (hariç)
	 */
	@NotNull
	public static Index of(int start, int end) {
		
		return new Index(start, end);
	}
	
	/**
	 * Yeni bir index nesnesi oluturur.
	 */
	@NotNull
	public static Index ofInvalid() {
		
		return INVALID_INDEX;
	}
	
	/**
	 * String içinden index'lerin bildirdiği yeri döndürür.
	 *
	 * @param str String
	 * @return İndex'lerin bildirdiği yerdeki string
	 */
	public String stringOf(@NotNull String str) {
		
		if (isInvalid()) return "";
		return str.substring(start, end);
	}
	
	/**
	 * Returns a substring of the input string starting from the specified index.
	 *
	 * @param str the input string to extract the substring from
	 * @return the substring of the input string starting from the specified index
	 */
	public String stringAfterStart(@NotNull String str) {
		
		return str.substring(start);
	}
	
	/**
	 * Returns a substring of the input string after the specified end index.
	 *
	 * @param str the input string to extract the substring from
	 * @return the substring of the input string after the specified end index
	 */
	public String stringAfterEnd(@NotNull String str) {
		
		return str.substring(end);
	}
	
	/**
	 * Returns a boolean value indicating whether the start and end indices of a string are valid.
	 * The method checks if either the start or end index is not equal to -1, which indicates that the indices are valid.
	 *
	 * @return true if the start and end indices are valid, false otherwise.
	 */
	public boolean isValid() {
		
		return start != -1 && end != -1;
	}
	
	public boolean isValidStart() {
		
		return start != -1;
	}
	
	public boolean isValidEnd() {
		
		return end != -1;
	}
	
	/**
	 * Returns a boolean value indicating whether the start and end values are both -1.
	 * If both values are -1, it means that the object is invalid.
	 *
	 * @return true if the start and end values are both -1, false otherwise.
	 */
	public boolean isInvalid() {
		
		return start == -1 && end == -1;
	}
	
	/**
	 * Başlangıç ve bitiş index'lerini istenen formatta dmndürür.<br>
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
	
	@NotNull
	@Override
	public String toString() {
		
		return "Index{" + "start=" + start + ", end=" + end + '}';
	}
}
