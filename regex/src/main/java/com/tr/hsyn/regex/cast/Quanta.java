package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Düzenli ifadelerde çokluk bildiren meta karakterleri ve bunları döndüren metotları tanımlar.
 * <p>
 * <p>
 * <pre>
 *       ?  : {@link #ZERO_OR_ONE}    - Kendisinden önce gelen karakterin yada karakter grubunun {@code 0} yada {@code 1} tane olması gerektiğini belirtir.
 *       +  : {@link #ONE_OR_MORE}    - Kendisinden önce gelen karakterin yada karakter grubunun en az {@code 1} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 *       *  : {@link #ZERO_OR_MORE}   - Kendisinden önce gelen karakterin yada karakter grubunun en az {@code 0} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 * </pre><br>
 */
@SuppressWarnings("DefaultLocale")
public enum Quanta {
	
	/**
	 * Means optional match.
	 * It is used to match zero or one occurrence of the preceding element.
	 * It is also used for lazy matching
	 */
	ZERO_OR_ONE("?"),
	/**
	 * Matches zero or more occurrences of the preceding character or group
	 */
	ZERO_OR_MORE("*"),
	/**
	 * Matches one or more occurrences of the preceding element
	 */
	ONE_OR_MORE("+");
	
	private final String regex;
	
	Quanta(@NotNull String regex) {
		
		this.regex = regex;
	}
	
	/**
	 * Bir düzenli ifadenin en az kaç kez tekrar etmesi gerektiğini bildiren düzenli ifadeyi döndürür.
	 * Varsayılan olarak üst sınır yoktur.
	 * {@code "{count,}"}
	 *
	 * @param count Düzenli ifadenin tekrar etmesi gereken en az sayı
	 * @return Adet bildiren düzenli ifade
	 */
	@NotNull
	public static String atLeast(int count) {
		
		return String.format("{%d,}", count);
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
	public static boolean isQuantifier(char s) {
		
		return s == '?' || s == '*' || s == '+';
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
	 * @param s the string to check
	 * @return true if the character is a quantifier
	 */
	public static boolean isQuantifier(@NotNull String s) {
		
		return s.equals("?") || s.equals("*") || s.equals("+");
	}
	
	/**
	 * Bir düzenli ifadenin en çok kaç kez tekrar etmesi gerektiğini bildiren düzenli ifadeyi döndürür.
	 * En az sayı varsayılan olarak sıfırdır. {@code "{0,count}"}
	 *
	 * @param count Düzenli ifadenin tekrar etmesi gereken en fazla sayı
	 * @return Adet bildiren düzenli ifade
	 */
	@NotNull
	public static String atMost(int count) {
		
		return String.format("{0,%d}", count);
	}
	
	/**
	 * Bir düzenli ifadenin en az ve en çok kaç adet olması gerektiğini bildiren düzenli ifadeyi döndürür. {@code "{min,max}"}
	 *
	 * @param min Düzenli ifadenin tekrar etmesi gereken en az sayı
	 * @param max Düzenli ifadenin tekrar etmesi gereken en fazla sayı
	 * @return Adet bildiren düzenli ifade
	 */
	@NotNull
	public static String minMax(int min, int max) {
		
		return String.format("{%d,%d}", min, max);
	}
	
	/**
	 * Bir düzenli ifadenin tam olarak kaç kez tekrar etmesi gerektiğini bildiren düzenli ifadeyi döndürür. {@code "{count}"}
	 *
	 * @param count Adet
	 * @return Adet bildiren düzenli ifade
	 */
	@NotNull
	public static String exactly(int count) {
		
		return String.format("{%d}", count);
	}
	
	public String getRegex() {
		
		return regex;
	}
	
	@Override
	public String toString() {
		
		return getRegex();
	}
	
	
}
