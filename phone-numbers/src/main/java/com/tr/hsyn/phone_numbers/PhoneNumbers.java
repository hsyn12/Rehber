package com.tr.hsyn.phone_numbers;


import com.tr.hsyn.string.Stringx;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Telefon numaraları üzerinde yapılan bazı işlemler sağlar.
 */
public class PhoneNumbers {
	
	/**
	 * Telefon numaraları için minimum uzunluk
	 */
	public static final int N_MIN = 10;
	/**
	 * Telefon numaraları için maximum uzunluk
	 */
	public static final int N_MAX = 14;
	
	/**
	 * Verilen telefon numarasını belirtilen uzunlukta formatlar.
	 * String içindeki sayı harici tüm karakterler (boşluk dahil) silinir.
	 * Sonuçta sadece sayılardan oluşan boşluksuz bitişik bir string kalır.
	 * Telefon numarası eğer belirtilen uzunluğu aşmıyorsa sonuç string döner,
	 * aşıyorsa uzunluğu eşitlemek için baştaki karakterler kırpılır.<br><br>
	 * <p>
	 * Örnek olarak <br>
	 * {@code format("+90 543 493 7530", 10)} = "5434937530"<br>
	 * {@code format("+90 543 493 7530", 7)} = "4937530"<br>
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
	 * Rakamların arasına boşluk koyarak daha okunur bir numara döndürür.<br>
	 * <p>
	 * {@code beautifyNumber("5434937530")} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  = 543 493 7530<br>
	 * {@code beautifyNumber("05434937530")} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  = 0543 493 7530<br>
	 * {@code beautifyNumber("905434937530")} &nbsp;&nbsp;&nbsp; = 90 543 493 7530<br>
	 * {@code beautifyNumber("+905434937530")} &nbsp; = +90 543 493 7530<br><br>
	 * <p>
	 * Bu kombinasyon dışındakiler geldiği gibi geri döner.
	 *
	 * @param _number Telefon numarası
	 * @return Formatlı numara
	 */
	public static String beautifyNumber(@NotNull String _number) {
		
		if (!isPhoneNumber(_number)) return _number;
		
		var number = Stringx.trimWhiteSpaces(_number);
		
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
	 * Verilen string'in telefon numarası olup olmadığını kontrol eder.<br><br>
	 *
	 * <strong>Kurallar</strong><br>
	 * <strong>----------</strong><br>
	 * <ol>
	 *    <li>Rakam harici bir karakteri olan string elbette bir telefon numarası olamaz. Ancak aşağıda tek tırnak içindeki karakterleri görmezden geliyorum<br>
	 * 		<pre>	'+', '(', ')', ' ' boşluk karakteri</pre></li>
	 *    <li>Yukarıdaki karakterler hariç bir string'in telefon numarası olabilmesi için en az on (10) en fazla ondört (14) haneli
	 * 	 olması gerektiğini varsayıyorum.</li>
	 * </ol>
	 * <br>
	 * <strong>Sonuçlar</strong><br>
	 * <strong>----------</strong><br>
	 * <ol>
	 *    <li>Yukarıdaki görmezden gelinen karakterler haricinde
	 * 	  rakam olmayan bir karakterle karşılaşılırsa {@code false} döner.</li>
	 *    <li>Yukarıdaki görmezden gelinen karakterler string'ten çıkarıldıktan sonra
	 * 	  geriye kalan ve sadece rakamlardan oluşan string'ın uzunluğu
	 * 	  10'dan küçükse yada 14'ten büyükse {@code false} döner.</li>
	 * </ol>
	 *
	 * @param number String
	 * @return Telefon numarası ise {@code true}
	 * @see #N_MIN
	 * @see #N_MAX
	 */
	public static boolean isPhoneNumber(@NotNull String number) {
		
		return isPhoneNumber(number, false);
	}
	
	/**
	 * {@link #isPhoneNumber(String)} fonksiyonu ile tek farkı uzunluğu kontrol etmeyebilir.
	 *
	 * @param number       Str
	 * @param ignoreLength Uzunluk göz ardı edilsin mi?
	 * @return Numara ise {@code true}
	 */
	public static boolean isPhoneNumber(@NotNull String number, boolean ignoreLength) {
		
		number = Stringx.trimWhiteSpaces(number);
		
		if (number.isEmpty()) return false;
		
		for (char c : number.toCharArray()) {
			
			//- Bu karakterleri görmezden gelelim
			if (c == '+' || c == '(' || c == ')') continue;
			
			if (!Character.isDigit(c)) return false;
		}
		
		if (ignoreLength) return true;
		
		number = Stringx.trimNonDigits(number);
		
		return number.length() >= N_MIN && number.length() <= N_MAX;
	}
	
	public static boolean containsNumber(List<String> numbers, String number) {
		
		if (numbers == null || number == null) return false;
		
		for (var n : numbers) {
			
			if (equals(n, number)) return true;
		}
		
		return false;
	}
	
	/**
	 * İki numarayı eşitlik için karşılaştırır.<br>
	 * Aradaki boşluk ve sayısal olmayan karakterler önemli değil,
	 * sadece sayısal bölümler karşılaştırılır.<br><br>
	 *
	 *
	 * <pre>equalsNumbers("+90543 4937530", "5 4 3 4 9 3 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("+90(543) 4937530", "5 4 3 4 9 3 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("90(543) 4937530", "05 4 3 4 (9 3) 7 5 3 0");// true</pre>
	 * <pre>equalsNumbers("90(543)s 4937530", "05 4 3 4 (9 3) 7 5 3 0";// true</pre>
	 * <pre>equalsNumbers("5 ", " 5");// true</pre><br>
	 *
	 * @param number1 Number1
	 * @param number2 Number2
	 * @return İki numara eşitse {@code true}
	 * @see #N_MIN
	 * @see #N_MAX
	 */
	public static boolean equals(@NotNull String number1, @NotNull String number2) {
		
		if (number1.equals(number2)) return true;
		
		var n1 = formatNumber(number1, 10);
		var n2 = formatNumber(number2, 10);
		
		return n1.equals(n2);
	}
	
	
	/**
	 * İki telefon numarası listesini eşitlik için kontrol eder.
	 * Telefon numaralarının liste içindeki yeri önemli değildir.
	 * Eğer iki liste de aynı telefon numaralarını tutuyorsa {@code true} döner.<br><br>
	 *
	 * <pre>
	 * String n1 = "5434937530";
	 * String n2 = "5434937530";
	 *
	 * List<String> l1 = Lists.newArrayList(n1, "5");
	 * List<String> l2 = Lists.newArrayList("5", n2);
	 * equals(l1, l2); // true</pre>
	 *
	 * @param numbers1 Numbers
	 * @param numbers2 Numbers
	 * @return İki liste eşitse {@code true}
	 */
	public static boolean equals(@NotNull List<String> numbers1, @NotNull List<String> numbers2) {
		
		if (numbers1.size() == numbers2.size()) {
			
			int match = 0;
			
			for (String n1 : numbers1) {
				
				for (String n2 : numbers2) {
					
					if (equals(n1, n2)) match++;
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
	
	
}