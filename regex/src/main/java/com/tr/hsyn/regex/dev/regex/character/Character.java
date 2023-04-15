package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.cast.Expression;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexDigit;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexLetter;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexPunctuation;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexWhiteSpace;

import org.jetbrains.annotations.NotNull;


public interface Character extends Text {
	
	Letter      LETTER          = new RegexLetter(Regex.LETTER);
	Letter      NON_LETTER      = new RegexLetter(Regex.NON_LETTER, true);
	Digit       DIGIT           = new RegexDigit(Regex.DIGIT);
	Digit       NON_DIGIT       = new RegexDigit(Regex.NON_DIGIT);
	WhiteSpace  WHITE_SPACE     = new RegexWhiteSpace(Regex.WHITE_SPACE);
	WhiteSpace  NON_WHITE_SPACE = new RegexWhiteSpace(Regex.NON_WHITE_SPACE);
	Punctuation PUNCTUATION     = new RegexPunctuation(Regex.PUNCTUATION, true);
	Punctuation NON_PUNCTUATION = new RegexPunctuation(Regex.NON_PUNCTUATION);
	
	@NotNull
	static Character of(@NotNull String text) {
		
		return new Expression(text);
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
