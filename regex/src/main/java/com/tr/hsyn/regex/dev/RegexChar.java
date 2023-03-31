package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
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
 * Bu sınıf bu 4 karakter türünü bildirir.
 * Karakter türleri, düzenli ifade karşılıkları ile tutulur, ki
 * bu sınıfın {@code RegexChar} olduğunu hatırlatırım,
 * {@link Text} arayüzünü uygular ve {@link Text#getText()} metodu karakterin düzenli ifade karşılığını döndürür.
 * Ssınıfın birkaç yardımcı metodu da bulunur çok basit işler için.
 * Mesela {@link #oneOrMore()} metodu, çağrıldığı karakter türü için <em>bir yada daha fazla tekrar</em>
 * etme ifadesini döndürür.<br><br>
 *
 * <pre>
 * var regex = RegexChar.DIGIT.oneMore();// "\p{N}+"
 * </pre>
 *
 * <p>
 * Bu metotlar {@link RegexBuilder} nesnesi döndürürler.
 * Bu da pratik bir şekilde ifade kurmayı sağlar.<br><br>
 *
 * <pre>
 * var str    = "12 04 1981";
 * var digits = RegexChar.DIGIT.oneMore().boundary();
 * pl("Result : %s", Nina.Dev.getParts(str, digits.findAll(str)));
 * //Result : [12, 04, 1981]
 * </pre>
 */
public enum RegexChar implements Text {
	
	/**
	 * Harf<br>
	 * {@code [a-zA-Z]}
	 *
	 * @see com.tr.hsyn.regex.cast.Character#LETTER
	 */
	LETTER,
	/**
	 * Rakam<br>
	 * {@code [0-9]}
	 *
	 * @see com.tr.hsyn.regex.cast.Character#DIGIT
	 */
	DIGIT,
	/**
	 * Boşluk<br>
	 * {@code [ \t\r\n\f\x0B]}
	 *
	 * @see com.tr.hsyn.regex.cast.Character#WHITE_SPACE
	 */
	WHITE_SPACE,
	/**
	 * Noktalama<br>
	 * {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 *
	 * @see com.tr.hsyn.regex.cast.Character#PUNC
	 */
	PUNC;
	
	/**
	 * @return Karakter türünü temsil eden düzenli ifade
	 */
	@Override
	public @NotNull String getText() {
		
		switch (this) {
			
			case LETTER: return Character.LETTER;
			case DIGIT: return Character.DIGIT;
			case WHITE_SPACE: return Character.WHITE_SPACE;
			case PUNC: return Character.PUNC;
		}
		
		throw new IllegalArgumentException("No Type");
	}
	
	/**
	 * @return Karakter türünün bir yada daha fazla olması gerektiğini bildiren düzenli ifade
	 */
	@NotNull
	public RegexBuilder oneOrMore() {
		
		return Nina.like(getText() + Quanta.ONE_OR_MORE);
	}
	
	/**
	 * @return Karakter türünün sıfır yada bir tane olması gerektiğini bildiren düzenli ifade
	 */
	@NotNull
	public RegexBuilder zeroOrOne() {
		
		return Nina.like(getText() + Quanta.ZERO_OR_ONE);
	}
	
	/**
	 * @return Karakter türünün sıfır yada daha fazla olması gerektiğini bildiren düzenli ifade
	 */
	@NotNull
	public RegexBuilder zeroOrMore() {
		
		return Nina.like(getText() + Quanta.ZERO_OR_MORE);
	}
	
	@NotNull
	public RegexBuilder times(int min, int max) {
		
		return Nina.like(getText()).times(min, max);
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
		
		return Nina.like(getText()).existIn(text);
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
		
		return oneOrMore().test(text);
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
		
		return Nina.like(getText()).removeFrom(text);
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
		
		return Nina.like(getText()).replaceFrom(text, replacement);
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
		
		return Nina.like(getText()).toRange().negate().toRegex().removeFrom(text);
	}
	
}
