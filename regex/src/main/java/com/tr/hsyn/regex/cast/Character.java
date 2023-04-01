package com.tr.hsyn.regex.cast;


/**
 * Meta characters.<br>
 *
 * @author hsyn 14 Haziran 2022 Sal? 12:21
 */
public interface Character {
	
	/**
	 * An alphabetic character {@code [a-zA-Z]}
	 */
	String LETTER               = "\\p{L}";
	/**
	 * A non-alphabetic character {@code [^a-zA-Z]}
	 */
	String NON_LETTER           = "\\P{L}";
	/**
	 * A digit 0-9 {@code [0-9]}
	 */
	String DIGIT                = "\\p{N}";
	/**
	 * A non-digit chacracter {@code [^0-9]}
	 */
	String NON_DIGIT            = "\\P{N}";
	/**
	 * A whitespace character, including line break. {@code [ \t\r\n\f\x0B]}
	 */
	String WHITE_SPACE          = "\\p{Z}";
	/**
	 * A non-whitespace chacracter {@code [^\s]}
	 */
	String NON_WHITE_SPACE      = "\\P{Z}";
	/**
	 * Punctuation characters {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 */
	String PUNC                 = "\\p{P}";
	String NON_PUNC             = "\\P{P}";
	/**
	 * Any character from {@link #LETTER}, {@link #DIGIT}, {@link #PUNC}, {@link #WHITE_SPACE}, {@code "_"}
	 */
	String ANY                  = String.format("[%s%s%s%s_]", LETTER, DIGIT, PUNC, WHITE_SPACE);
	/**
	 * A lowercase alphabetic character {@code [a-z]}
	 */
	String LETTER_LOWER         = "\\p{Ll}";
	String NON_LETTER_LOWER     = "\\P{Ll}";
	/**
	 * An uppercase alphabetic character {@code [A-Z]}
	 */
	String LETTER_UPPER         = "\\p{Lu}";
	String NON_LETTER_UPPER     = "\\P{Lu}";
	/**
	 * Delimiters {@code '.$^{[()|*+?\'}
	 */
	String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
	/**
	 * A control character {@code [\p{Cntrl}]}
	 */
	String CONTROL              = "\\p{C}";
	/**
	 * A non-control character {@code [^\p{C}]}
	 */
	String NON_CONTROL          = "\\P{C}";
	String SYMBOL               = "\\p{S}";
	String SLASH                = "\\";
	
	
	/**
	 * Bir karakterin ait olduğu karakter sınıfını döndürür.<br>
	 *
	 * <ul>
	 *    <li>{@link #WHITE_SPACE}  : boşluk sınıfı</li>
	 *    <li>{@link #DIGIT}  : sayı sınıfı</li>
	 *    <li>{@link #LETTER} : harf sınıfı</li>
	 *    <li>{@link #PUNC}   : noktalama sınıfı</li>
	 * </ul>
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter grubu
	 */
	static String getCharacterClass(char c) {
		
		if (java.lang.Character.isLetter(c)) return LETTER;
		if (java.lang.Character.isDigit(c)) return DIGIT;
		if (java.lang.Character.isWhitespace(c)) return WHITE_SPACE;
		
		return PUNC;
	}
	
	
}