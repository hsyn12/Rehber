package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.character.cast.Expression;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;


public interface Character extends Text {
	
	/**
	 * An alphabetic character. {@code \p{L}}
	 */
	@RegExp          String LETTER           = "\\p{L}";
	/**
	 * Any character except letter. {@code \P{L}}
	 */
	@RegExp          String NON_LETTER       = "\\P{L}";
	/**
	 * A digit.  {@code \p{N}}
	 */
	@RegExp          String DIGIT            = "\\p{N}";
	/**
	 * Any character except digit. {@code \P{N}}
	 */
	@RegExp          String NON_DIGIT        = "\\P{N}";
	/**
	 * A whitespace character, including line break. {@code [ \t\r\n\f\x0B]}
	 */
	@RegExp          String WHITE_SPACE      = "\\p{Z}";
	/**
	 * Any character except white space.
	 */
	@RegExp          String NON_WHITE_SPACE  = "\\P{Z}";
	/**
	 * Punctuation character {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 */
	@RegExp          String PUNCTUATION      = "\\p{P}";
	/**
	 * Any character except punctuation.
	 */
	@RegExp          String NON_PUNCTUATION  = "\\P{P}";
	/**
	 * Any character from {@link #LETTER}, {@link #DIGIT}, {@link #PUNCTUATION}, {@link #WHITE_SPACE}
	 */
	@RegExp
	@NotNull         String ANY              = String.format("[%s%s%s%s]", LETTER, DIGIT, PUNCTUATION, WHITE_SPACE);
	/**
	 * A lowercase alphabetic character {@code [a-z]}
	 */
	@RegExp @NotNull String LETTER_LOWER     = "\\p{Ll}";
	/**
	 * Any character except lowercase alphabetic. {@code [^\p{Ll}]}
	 */
	@RegExp @NotNull String NON_LETTER_LOWER = "\\P{Ll}";
	/**
	 * An uppercase alphabetic character {@code [A-Z]}
	 */
	@RegExp @NotNull String LETTER_UPPER     = "\\p{Lu}";
	/**
	 * Any character except uppercase alphabetic. {@code [^\p{Lu}]}
	 */
	@RegExp @NotNull String NON_LETTER_UPPER = "\\P{Lu}";
	/**
	 * Delimiters {@code '.$^{[()|*+?\'}
	 */
	String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
	/**
	 * A control character {@code [\p{Cntrl}]}
	 */
	@RegExp @NotNull String CONTROL     = "\\p{C}";
	/**
	 * A non-control character {@code [^\p{C}]}
	 */
	@RegExp @NotNull String NON_CONTROL = "\\P{C}";
	/**
	 * A symbol character {@code [\p{S}]}
	 */
	@RegExp @NotNull String SYMBOL      = "\\p{S}";
	/**
	 * A backslash character {@code [\\]}
	 */
	@NotNull         String BACK_SLASH  = "\\\\";
	
	@NotNull
	static Character of(@NotNull String text) {
		
		return new Expression(text);
	}
	
	default @NotNull Text zeroOrOne() {
		
		return Text.of(getText() + Quanta.ZERO_OR_ONE);
	}
	
	default @NotNull Text zeroOrMore() {
		
		return Text.of(getText() + Quanta.ZERO_OR_MORE);
	}
	
	default @NotNull Text oneOrMore() {
		
		return Text.of(getText() + Quanta.ONE_OR_MORE);
	}
	
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
		
		return text.replaceAll(getText(), "");
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
	 * Character.LETTER.all("seni sensiz yaşamak en kötü kader olsa gerek");//false
	 * Character.LETTER.all("senisensizyaşamakenkötükaderolsagerek");//true
	 * Character.DIGIT.all("12041981");//true
	 * Character.DIGIT.all("12.04.1981");//false
	 * </pre>
	 *
	 * @param text Test edilecek yazı
	 * @return Yazının tüm karakterleri, çağrının yapıldığı karakter türünden oluşuyorsa {@code true}
	 */
	default boolean all(@NotNull String text) {
		
		return text.matches(getText() + Quanta.ONE_OR_MORE);
	}
	
	/**
	 * Karakter türünün tümleyenini döndürür. <br>
	 *
	 * <pre>
	 * var nonLetter = Character.LETTER.non();
	 * var nonDigit  = Character.DIGIT.non();
	 *
	 * pl("Letter : %s", nonLetter.getText()); //Letter : \P{L}
	 * pl("Digit  : %s", nonDigit.getText()); // Digit  : \P{N}
	 * </pre>
	 *
	 * @return Karakter türünün tümleyenini döndürür.
	 */
	@NotNull Character non();
}
