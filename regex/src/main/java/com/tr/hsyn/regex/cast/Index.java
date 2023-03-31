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
	public String substringOf(@NotNull String str) {
		
		return str.substring(start, end);
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
