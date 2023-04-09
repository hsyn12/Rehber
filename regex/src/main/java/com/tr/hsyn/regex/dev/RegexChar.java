package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.Regex;
import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


/**
 * <h3>Regex Characters</h3>
 * <p>
 * Yazılar 4 karakter türünden oluşur.
 *
 * <ol>
 * <li>Harf      - {@link #LETTER}</li>
 * <li>Rakam     - {@link #DIGIT}</li>
 * <li>Boşluk    - {@link #WHITE_SPACE}</li>
 * <li>Noktalama - {@link #PUNC}</li>
 * </ol>
 *
 * <p>
 * Bu sınıf bu 4 karakter türünü (ve zıtlarını) bildirir.
 * Karakter türleri, düzenli ifade karşılıkları ile tutulur, ki
 * bu sınıfın {@code RegexChar} olduğunu hatırlatırım,
 * {@link Text} arayüzünü uygular ve {@link Text#getText()} metodu türün düzenli ifade karşılığını döndürür.
 * Tanımlanan türler üzerinde temel işlemler gerçekleştirecek metotlar da tanımlıdır.
 */
public enum RegexChar implements Text {
	
	/**
	 * Harf.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#LETTER
	 */
	LETTER(Character.LETTER),
	/**
	 * Harf olmayan.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#NON_LETTER
	 */
	NON_LETTER(Character.NON_LETTER),
	/**
	 * Rakam.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#DIGIT
	 */
	DIGIT(Character.DIGIT),
	/**
	 * Rakam olmayan.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#NON_DIGIT
	 */
	NON_DIGIT(Character.NON_DIGIT),
	/**
	 * Boşluk.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#WHITE_SPACE
	 */
	WHITE_SPACE(Character.WHITE_SPACE),
	/**
	 * Boşluk olmayan.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#NON_WHITE_SPACE
	 */
	NON_WHITE_SPACE(Character.NON_WHITE_SPACE),
	/**
	 * Noktalama.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#PUNC
	 */
	PUNC(Character.PUNC),
	/**
	 * Noktalama olmayan.
	 *
	 * @see com.tr.hsyn.regex.cast.Character#NON_PUNC
	 */
	NON_PUNC(Character.NON_PUNC);
	
	private final String regex;
	
	RegexChar(@NotNull String regex) {
		
		this.regex = regex;
	}
	
	/**
	 * @return Karakter türünü temsil eden düzenli ifade
	 */
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public String toString() {
		
		return regex;
	}
	
	/**
	 * Bir yazının herhangi bir yerinde karakter türüne ait bir karakter geçip geçmediğini test eder.<br><br>
	 *
	 * <pre>
	 * RegexChar.DIGIT.any("Bugün 23 Nisan");//true
	 * RegexChar.DIGIT.any("Hello everyone");//false
	 * </pre>
	 *
	 * @param text Test edilecek yazı
	 * @return Yazının herhangi bir yerinde, çağrının yapıldığı karakter türünden bir karakter geçiyorsa {@code true}
	 */
	public boolean any(@NotNull String text) {
		
		return Regex.like(regex).existIn(text);
	}
	
	/**
	 * Bir yazının tamamının aynı tür karakterden olup olmadığını test eder.<br><br>
	 *
	 * <pre>
	 * RegexChar.LETTER.all("seni sensiz yaşamak en kötü kader olsa gerek");//false
	 * RegexChar.LETTER.all("senisensizyaşamakenkötükaderolsagerek");//true
	 * RegexChar.DIGIT.all("12041981");//true
	 * RegexChar.DIGIT.all("12.04.1981");//false
	 * </pre>
	 *
	 * @param text Test edilecek yazı
	 * @return Yazının tüm karakterleri, çağrının yapıldığı karakter türünden oluşuyorsa {@code true}
	 */
	public boolean all(@NotNull String text) {
		
		return Regex.like(regex).oneOrMore().test(text);
	}
	
	/**
	 * Yazı içindeki belirli bir türe ait tüm karakterleri siler.<br><br>
	 *
	 * <pre>
	 * // 12041981
	 * RegexChar.WHITE_SPACE.removeFrom("12 04 1981");</pre>
	 *
	 * @param text Yazı
	 * @return Metodun çağrıldığı karakter türüne ait tüm karakterler silinmiş yeni bir string
	 */
	public String removeFrom(@NotNull String text) {
		
		return Regex.like(regex).removeFrom(text);
	}
	
	/**
	 * Yazı içindeki belirli bir türe ait tüm karakterleri başka bir sring ile değişrir.<br><br>
	 *
	 * <pre>
	 * RegexChar.WHITE_SPACE.replaceFrom("12 Nisan 1981", ".");
	 * // 12.Nisan.1981
	 * </pre>
	 *
	 * @param text        Yazı
	 * @param replacement Karakterin yerine geçecek string
	 * @return Metodun çağrıldığı karakter türüne ait tüm karakterleri değiştirilmiş yeni bir string
	 */
	public String replaceFrom(@NotNull String text, String replacement) {
		
		return Regex.like(regex).replaceFrom(text, replacement);
	}
	
	/**
	 * Bir yazıdan, karakter türüne ait olmayan karakterleri siler.
	 * Başka bir değişle, yazıda sadece kendi türüne ait karakterleri bırakır.<br><br>
	 *
	 * <pre>
	 * var str = "12 Nisan 1981";
	 * RegexChar.LETTER.retainFrom(str);// Nisan
	 * RegexChar.DIGIT.retainFrom(str);// 121981
	 * </pre>
	 *
	 * @param text Yazı
	 * @return Çağrının yapıldığı türe ait olmayan tüm karakterler silinmiş yeni bir string.
	 * 		Sadece çağrının yapıldığı türe ait karakterlerden oluşan yeni bir string.
	 */
	public String retainFrom(@NotNull String text) {
		
		return Regex.like(regex).toRange().negate().toRegex().removeFrom(text);
	}
	
}
