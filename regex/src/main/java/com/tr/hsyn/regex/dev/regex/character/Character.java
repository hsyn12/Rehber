package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.quantity.Quanta;

import org.jetbrains.annotations.NotNull;


public interface Character extends Text {
	
	/**
	 * An alphabetic character {@code [a-zA-Z]}
	 */
	String LETTER          = "\\p{L}";
	/**
	 * Any character except letter. {@code [^\p{L}]}
	 */
	String NON_LETTER      = "\\P{L}";
	/**
	 * A digit 0-9 {@code [0-9]}
	 */
	String DIGIT           = "\\p{N}";
	/**
	 * Any character except digit. {@code [^\d]}
	 */
	String NON_DIGIT       = "\\P{N}";
	/**
	 * A whitespace character, including line break. {@code [ \t\r\n\f\x0B]}
	 */
	String WHITE_SPACE     = "\\p{Z}";
	/**
	 * Any character except white space. {@code [^\s]}
	 */
	String NON_WHITE_SPACE = "\\P{Z}";
	/**
	 * Punctuation characters {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 */
	String PUNCTUATION     = "\\p{P}";
	/**
	 * Any character except punctuation. {@code [^\p{P}]}
	 */
	String NON_PUNCTUATION = "\\P{P}";
	/**
	 * Any character from {@link #LETTER}, {@link #DIGIT}, {@link #PUNCTUATION}, {@link #WHITE_SPACE}
	 */
	@NotNull String ANY              = String.format("[%s%s%s%s]", Letter.LETTER, Digit.DIGIT, Punctuation.PUNCTUATION, WhiteSpace.WHITE_SPACE);
	/**
	 * A lowercase alphabetic character {@code [a-z]}
	 */
	@NotNull String LETTER_LOWER     = "\\p{Ll}";
	/**
	 * Any character except lowercase alphabetic. {@code [^\p{Ll}]}
	 */
	@NotNull String NON_LETTER_LOWER = "\\P{Ll}";
	/**
	 * An uppercase alphabetic character {@code [A-Z]}
	 */
	@NotNull String LETTER_UPPER     = "\\p{Lu}";
	/**
	 * Any character except uppercase alphabetic. {@code [^\p{Lu}]}
	 */
	@NotNull String NON_LETTER_UPPER = "\\P{Lu}";
	/**
	 * Delimiters {@code '.$^{[()|*+?\'}
	 */
	String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
	/**
	 * A control character {@code [\p{Cntrl}]}
	 */
	@NotNull String CONTROL     = "\\p{C}";
	/**
	 * A non-control character {@code [^\p{C}]}
	 */
	@NotNull String NON_CONTROL = "\\P{C}";
	/**
	 * A symbol character {@code [\p{S}]}
	 */
	@NotNull String SYMBOL      = "\\p{S}";
	/**
	 * A slash character {@code [\]}
	 */
	@NotNull String SLASH       = "\\";
	
	/**
	 * Yazı içindeki belirli bir karakter türüne ait tüm karakterleri siler.<br><br>
	 *
	 * <pre>
	 * // 12041981
	 * Regex.WHITE_SPACE.removeFrom("12 04 1981");</pre>
	 *
	 * @param text Yazı
	 * @return Metodun çağrıldığı karakter türüne ait tüm karakterler silinmiş yeni bir string
	 */
	default String removeFrom(@NotNull String text) {
		
		return replaceFrom(text, "");
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
	default String replaceFrom(@NotNull String text, String replacement) {
		
		return text.replaceAll(getText(), replacement);
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
	default String retainFrom(@NotNull String text) {
		
		return text.replaceAll(non().getText(), "");
	}
	
	/**
	 * Bir yazının tamamının aynı tür karakterden olup olmadığını test eder.<br><br>
	 *
	 * <pre>
	 * Regex.LETTER.all("seni sensiz yaşamak en kötü kader olsa gerek");//false
	 * Regex.LETTER.all("senisensizyaşamakenkötükaderolsagerek");//true
	 * Regex.DIGIT.all("12041981");//true
	 * Regex.DIGIT.all("12.04.1981");//false
	 * </pre>
	 *
	 * @param text Test edilecek yazı
	 * @return Yazının tüm karakterleri, çağrının yapıldığı karakter türünden oluşuyorsa {@code true}
	 */
	default boolean all(@NotNull String text) {
		
		return text.matches(getText() + Quanta.ONE_OR_MORE);
	}
	
	@NotNull Character non();
}
