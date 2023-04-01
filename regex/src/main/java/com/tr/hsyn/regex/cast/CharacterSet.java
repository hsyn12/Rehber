package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Karakter Gruplarını ve bunların sayısal kodlarını tanımlar.<br>
 */
public interface CharacterSet {
	
	//region Karakter Kodları
	
	//!///////// Karakter Kodları //////////////////////////////
	
	/**
	 * Herhangi bir rakam.<br>
	 * {@code "0123456789"}
	 *
	 * @see #DIGITS
	 */
	int KEY_DIGIT                = 1;
	/**
	 * Herhangi bir harf.<br>
	 * {@code "abcçdefgğhıijklmnoöprsştuüvyzxwABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZXW"}
	 *
	 * @see #LETTERS
	 */
	int KEY_LETTER               = 2;
	/**
	 * Herhangi bir küçük harf.<br>
	 * {@code "abcçdefgğhıijklmnoöprsştuüvyzxw"}
	 *
	 * @see #LETTERS_LOWER
	 */
	int KEY_LETTER_LOWER         = 3;
	/**
	 * Herhangi bir büyük harf.<br>
	 * {@code "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZXW"}
	 *
	 * @see #LETTERS_UPPER
	 */
	int KEY_LETTER_UPPER         = 4;
	/**
	 * Noktalama karakterlerinden herhangi biri.<br>
	 * {@code ".,;:!?%&#@()[]{}<>*+-_=\\/|\"'`$£¥€"}
	 *
	 * @see #PUNCT
	 */
	int KEY_PUNCT                = 5;
	/**
	 * Herhangi bir sessiz harf.<br>
	 * {@code "bcçdfgğhjklmnprsştvyzBCÇDFGĞHJKLMNPRSŞTVYZqxwQXW"}
	 *
	 * @see #CONSONANTS
	 */
	int KEY_CONSONANT            = 6;
	/**
	 * Herhangi büyük bir sessiz harf.<br>
	 * {@code "BCÇDFGĞHJKLMNPRSŞTVYZQXW"}
	 *
	 * @see #CONSONANTS_UPPER
	 */
	int KEY_CONSONANT_UPPER      = 7;
	/**
	 * Herhangi küçük bir sessiz harf.<br>
	 * {@code "bcçdfgğhjklmnprsştvyzqxw"}
	 *
	 * @see #CONSONANTS_LOWER
	 */
	int KEY_CONSONANT_LOWER      = 8;
	/**
	 * Herhangi bir sesli harf.<br>
	 * {@code "aeıioöuüAEIİOÖUÜ"}
	 *
	 * @see #VOWELS
	 */
	int KEY_VOWEL                = 9;
	/**
	 * Herhangi küçük bir sesli harf.<br>
	 * {@code "aeıioöuü"}
	 *
	 * @see #VOWELS_LOWER
	 */
	int KEY_VOWEL_LOWER          = 10;
	/**
	 * Herhangi büyük bir sesli harf.<br>
	 * {@code "AEIİOÖUÜ"}
	 *
	 * @see #VOWELS_UPPER
	 */
	int KEY_VOWEL_UPPER          = 11;
	/**
	 * Herhangi ince bir sesli harf.<br>
	 * {@code "eiöü"}
	 *
	 * @see #VOWELS_THIN
	 */
	int KEY_VOWEL_THIN           = 12;
	/**
	 * Herhangi ince büyük bir sesli harf.<br>
	 * {@code "EİÖÜ"}
	 *
	 * @see #VOWELS_THIN_UPPER
	 */
	int KEY_VOWEL_THIN_UPPER     = 13;
	/**
	 * Herhangi kalın küçük bir sesli harf.<br>
	 * {@code "aıou"}
	 *
	 * @see #VOWELS_THICK
	 */
	int KEY_VOWEL_THICK          = 14;
	/**
	 * Herhangi kalın bir sesli harf.<br>
	 * {@code "AIOU"}
	 *
	 * @see #VOWELS_THICK_UPPER
	 */
	int KEY_VOWEL_THICK_UPPER    = 15;
	/**
	 * Herhangi sert sessiz küçük bir harf.<br>
	 * {@code "çfhkpsştq"}
	 *
	 * @see #CONSONANTS_HARD
	 */
	int KEY_CONSONANT_HARD       = 16;
	/**
	 * Herhangi sert sessiz büyük bir harf.<br>
	 * {@code "ÇFHKPSŞTQ"}
	 *
	 * @see #CONSONANTS_HARD
	 */
	int KEY_CONSONANT_HARD_UPPER = 17;
	/**
	 * Herhangi yumuşak sessiz küçük bir harf.<br>
	 * {@code "bcdgjlmnprvzxyw"}
	 *
	 * @see #CONSONANTS_SOFT
	 */
	int KEY_CONSONANT_SOFT       = 18;
	/**
	 * Herhangi yumuşak sessiz büyük bir harf.<br>
	 * {@code "BCDGJLMNPRVZXYW"}
	 *
	 * @see #CONSONANTS_SOFT_UPPER
	 */
	int KEY_CONSONANT_SOFT_UPPER = 19;
	/**
	 * Herhangi bir boşluk karakteri.<br>
	 * {@code " \t\r\n\f\\x0B"}
	 *
	 * @see #WHITESPACE
	 */
	int KEY_WHITE_SPACES         = 20;
	/**
	 * Boşluk karakteri
	 */
	int KEY_SPACE                = 21;
	// endregion Karakter Kodları
	
	// region Karakter Setleri
	
	//////////! Karakter Setleri ///////////////////////////////
	
	/**
	 * İngiliz alfabesine özgü harfler.<br>
	 * {@code "qxwQXW"}
	 */
	String ENGLISH_CHARS         = "qxwQXW";
	/**
	 * Alfabe. {@code "abcçdefgğhıijklmnoöprsştuüvyzqxwABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZQXW"}
	 */
	String LETTERS               = "abcçdefgğhıijklmnoöprsştuüvyzqxwABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZQXW";
	/**
	 * Küçük harfler. {@code "abcçdefgğhıijklmnoöprsştuüvyzqxw"}
	 */
	String LETTERS_LOWER         = "abcçdefgğhıijklmnoöprsştuüvyzqxw";
	/**
	 * Büyük harfler. {@code "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZQXW"}
	 */
	String LETTERS_UPPER         = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZQXW";
	/**
	 * Rakamlar. {@code "0123456789"}
	 */
	String DIGITS                = "0123456789";
	/**
	 * Noktalama karakterleri. {@code ".,;:!?%&#@()[]{}<>*+-_=\\/|\"'`$£¥€"}
	 */
	String PUNCT                 = ".,;:!?%&#@()[]{}<>*+-_=\\/|\"'`$£¥€";
	/**
	 * Boşluk karakterleri. {@code " \t\r\n\f\\x0B"}
	 */
	String WHITESPACE            = " \t\r\n\f\\x0B";
	/**
	 * Sessiz harfler. {@code "bcçdfgğhjklmnprsştvyzBCÇDFGĞHJKLMNPRSŞTVYZqxwQXW"}
	 */
	String CONSONANTS            = "bcçdfgğhjklmnprsştvyzBCÇDFGĞHJKLMNPRSŞTVYZ" + ENGLISH_CHARS;
	/**
	 * Küçük sessiz harfler. {@code "bcçdfgğhjklmnprsştvyzqxw"}
	 */
	String CONSONANTS_LOWER      = "bcçdfgğhjklmnprsştvyzqxw";
	/**
	 * Büyük sessiz harfler. {@code "BCÇDFGĞHJKLMNPRSŞTVYZQXW"}
	 */
	String CONSONANTS_UPPER      = "BCÇDFGĞHJKLMNPRSŞTVYZQXW";
	/**
	 * Sert sessizler. {@code "çfhkpsştq"}
	 */
	String CONSONANTS_HARD       = "çfhkpsştq";
	/**
	 * Sert sessizler. {@code "ÇFHKPSŞTQ"}
	 */
	String CONSONANTS_HARD_UPPER = "ÇFHKPSŞTQ";
	/**
	 * Yumuşak sessizler. {@code "bcdgjlmnprvzxyw"}
	 */
	String CONSONANTS_SOFT       = "bcdgjlmnprvzxyw";
	/**
	 * Yumuşak sessizler. {@code "BCDGJLMNPRVZXYW"}
	 */
	String CONSONANTS_SOFT_UPPER = "BCDGJLMNPRVZXYW";
	/**
	 * Sesli harfler. {@code "aeıioöuüAEIİOÖUÜ"}
	 */
	String VOWELS                = "aeıioöuüAEIİOÖUÜ";
	/**
	 * Sesli harfler. {@code "AEIİOÖUÜ"}
	 */
	String VOWELS_UPPER          = "AEIİOÖUÜ";
	/**
	 * Sesli harfler. {@code "aeıioöuü"}
	 */
	String VOWELS_LOWER          = "aeıioöuü";
	/**
	 * Kalın sesliler. {@code "aıou"}
	 */
	String VOWELS_THICK          = "aıou";
	/**
	 * Kalın sesliler. {@code "AIOU"}
	 */
	String VOWELS_THICK_UPPER    = "AIOU";
	/**
	 * İnce sesliler. {@code "eiöü"}
	 */
	String VOWELS_THIN           = "eiöü";
	/**
	 * İnce sesliler. {@code "EİÖÜ"}
	 */
	String VOWELS_THIN_UPPER     = "EİÖÜ";
	/**
	 * Türk alfabesine özgü karakterler. {@code "çğıöşüÇĞİÖŞÜ"}
	 */
	String TURKISH_CHARS         = "çğıöşüÇĞİÖŞÜ";
	/**
	 * Türk alfabesine özgü karakterler. {@code "çğıöşü"}
	 */
	String TURKISH_CHARS_LOWER   = "çğıöşü";
	/**
	 * Türk alfabesine özgü karakterler. {@code "ÇĞİÖŞÜ"}
	 */
	String TURKISH_CHARS_UPPER   = "ÇĞİÖŞÜ";
	// endregion Karakter Setleri
	
	//! ünlü ile biten hecelere açık hece denir
	//! kalın ünlülerden sonra kalın, ince ünlülerden sonra ince ünlülerin gelmesine büyük ünlü uyumu denir
	//- düz ünlülerden sonra düz ünlülerin, yuvarlak ünlülerden
	//- sonra dar yuvarlak ya da geniş düz ünlülerin gelmesine küçük ünlü uyumu denir
	//? Türkçede iki ünlü yan yana gelmez
	
	/**
	 * Bir karakterin ait olduğu karakter setini döndürür.<br>
	 *
	 * @param c            Karakter
	 * @param specific     {@code true} ise büyük-küçük ünlü-ünsüz harf ayrımı yapılır
	 * @param moreSpecific {@code true} ise ünlü harfler için ince-kalın, ünsüz harfler için sert-yumuşak harf ayrımı yapılır
	 * @return Karakter seti
	 */
	static String getCharecterSet(char c, boolean specific, boolean moreSpecific) {
		
		//@off
		switch (c) {

			//- Boşluk karakterleri
			case ' ':
			case '\n':
			case '\t':
			case '\r':
			case '\f': return " ";//- hepsi için sadece bir boşluk

			//- Alfabetik karakterler

			case 'a':
			case 'ı':
			case 'o':
			case 'u':
				if (moreSpecific)   return VOWELS_THICK;
				if   (specific)     return VOWELS_LOWER;
				return LETTERS;
			case 'e':
			case 'i':
			case 'ö':
			case 'ü':
				if (moreSpecific)   return VOWELS_THIN;
				if   (specific)     return VOWELS_LOWER;
				return LETTERS;
			case 'b':
			case 'c':
			case 'd':
			case 'g':
			case 'j':
			case 'l':
			case 'm':
			case 'n':
			case 'r':
			case 'v':
			case 'z':
			case 'w':
			case 'x':
			case 'y':
				if (moreSpecific)   return CONSONANTS_SOFT;
				if   (specific)     return CONSONANTS_LOWER;
				return LETTERS;
			case 'ç':
			case 'f':
			case 'h':
			case 'k':
			case 'p':
			case 's':
			case 'ş':
			case 't':
			case 'q':
				if (moreSpecific)   return CONSONANTS_HARD;
				if   (specific)     return CONSONANTS_LOWER;
				return LETTERS;
			case 'ğ':
				if   (specific) 	return CONSONANTS_LOWER;
				return LETTERS;
			case 'A':
			case 'I':
			case 'O':
			case 'U':
				if (moreSpecific)   return VOWELS_THICK;
				if   (specific)     return VOWELS_UPPER;
				return LETTERS;
			case 'E':
			case 'İ':
			case 'Ö':
			case 'Ü':
				if (moreSpecific)   return VOWELS_THIN;
				if   (specific)     return VOWELS_UPPER;
				return LETTERS;
			case 'B':
			case 'C':
			case 'D':
			case 'G':
			case 'J':
			case 'L':
			case 'M':
			case 'N':
			case 'R':
			case 'V':
			case 'Z':
			case 'W':
			case 'X':
			case 'Y':
				if (moreSpecific)   return CONSONANTS_SOFT;
				if   (specific)     return VOWELS_UPPER;
				return LETTERS;
			case 'Ç':
			case 'F':
			case 'H':
			case 'K':
			case 'P':
			case 'S':
			case 'Ş':
			case 'T':
			case 'Q':
				if (moreSpecific)   return CONSONANTS_HARD;
				if   (specific)     return VOWELS_UPPER;
				return LETTERS;
			case 'Ğ':
				
				if   (specific)  return CONSONANTS_UPPER;
				return LETTERS;
			//@on
		}
		
		if (java.lang.Character.isDigit(c)) return DIGITS;
		
		return Posix.PUNCT;
	}
	
	/**
	 * Bir karakter setinin karakter kodunu döndürür.<br>
	 *
	 * @param characterSet Karakter seti
	 * @return Karakter kodu
	 */
	static int getCharacterCode(@NotNull String characterSet) {
		
		//@off
		switch (characterSet) {
			
			case             WHITESPACE: return KEY_SPACE;
			case           VOWELS_THICK: return KEY_VOWEL_THICK;
			case     VOWELS_THICK_UPPER: return KEY_VOWEL_THICK_UPPER;
			case            VOWELS_THIN: return KEY_VOWEL_THIN;
			case      VOWELS_THIN_UPPER: return KEY_VOWEL_THIN_UPPER;
			case                 VOWELS: return KEY_VOWEL;
			case           VOWELS_UPPER: return KEY_VOWEL_UPPER;
			case           VOWELS_LOWER: return KEY_VOWEL_LOWER;
			case             CONSONANTS: return KEY_CONSONANT;
			case       CONSONANTS_UPPER: return KEY_CONSONANT_UPPER;
			case       CONSONANTS_LOWER: return KEY_CONSONANT_LOWER;
			case        CONSONANTS_HARD: return KEY_CONSONANT_HARD;
			case  CONSONANTS_HARD_UPPER: return KEY_CONSONANT_HARD_UPPER;
			case        CONSONANTS_SOFT: return KEY_CONSONANT_SOFT;
			case  CONSONANTS_SOFT_UPPER: return KEY_CONSONANT_SOFT_UPPER;
			case                LETTERS: return KEY_LETTER;
			case          LETTERS_UPPER: return KEY_LETTER_UPPER;
			case          LETTERS_LOWER: return KEY_LETTER_LOWER;
			case                 DIGITS: return KEY_DIGIT;
			default:return KEY_PUNCT;
		}
		//@on
	}
	
	/**
	 * Bir karakterin karakter kodunu döndürür.
	 *
	 * @param c            Karakter
	 * @param specific     {@code true} ise büyük-küçük ünlü-ünsüz harf ayrımı yapılır
	 * @param moreSpecific {@code true} ise ünlü harfler için ince-kalın, ünsüz harfler için sert-yumuşak harf ayrımı yapılır
	 * @return Karakter kodu
	 */
	static int getCharacterCode(char c, boolean specific, boolean moreSpecific) {
		
		//@off
		switch (c) {
			
			//- Boşluk karakterleri
			case ' ':
			case '\n':
			case '\t':
			case '\r':
			case '\f': return KEY_SPACE;//- hepsi için sadece bir boşluk
			
			//- Alfabetik karakterler
			
			case 'a':
			case 'ı':
			case 'o':
			case 'u':
				if (moreSpecific)   return KEY_VOWEL_THICK;
				if   (specific)     return KEY_VOWEL_LOWER;
				return KEY_VOWEL;
			case 'e':
			case 'i':
			case 'ö':
			case 'ü':
				if (moreSpecific)   return KEY_VOWEL_THIN;
				if   (specific)     return KEY_VOWEL_LOWER;
				return KEY_VOWEL;
			case 'b':
			case 'c':
			case 'd':
			case 'g':
			case 'j':
			case 'l':
			case 'm':
			case 'n':
			case 'r':
			case 'v':
			case 'z':
			case 'w':
			case 'x':
			case 'y':
				if (moreSpecific)   return KEY_CONSONANT_SOFT;
				if   (specific)     return KEY_CONSONANT_LOWER;
				return KEY_CONSONANT;
			case 'ç':
			case 'f':
			case 'h':
			case 'k':
			case 'p':
			case 's':
			case 'ş':
			case 't':
			case 'q':
				if (moreSpecific)   return KEY_CONSONANT_HARD;
				if   (specific)     return KEY_CONSONANT_LOWER;
				return KEY_CONSONANT;
			case 'ğ':
				if   (specific) 	return KEY_CONSONANT_LOWER;
				return KEY_CONSONANT;
			
			case 'A':
			case 'I':
			case 'O':
			case 'U':
				if (moreSpecific)   return KEY_VOWEL_THICK;
				if   (specific)     return KEY_VOWEL_UPPER;
				return KEY_VOWEL;
			case 'E':
			case 'İ':
			case 'Ö':
			case 'Ü':
				if (moreSpecific)   return KEY_VOWEL_THIN;
				if   (specific)     return KEY_VOWEL_UPPER;
				return KEY_VOWEL;
			case 'B':
			case 'C':
			case 'D':
			case 'G':
			case 'J':
			case 'L':
			case 'M':
			case 'N':
			case 'R':
			case 'V':
			case 'Z':
			case 'W':
			case 'X':
			case 'Y':
				if (moreSpecific)   return KEY_CONSONANT_SOFT;
				if   (specific)     return KEY_VOWEL_UPPER;
				return KEY_CONSONANT;
			case 'Ç':
			case 'F':
			case 'H':
			case 'K':
			case 'P':
			case 'S':
			case 'Ş':
			case 'T':
			case 'Q':
				if (moreSpecific)   return KEY_CONSONANT_HARD;
				if   (specific)     return KEY_VOWEL_UPPER;
				return KEY_CONSONANT;
			case 'Ğ':
				
				if   (specific)  return KEY_CONSONANT_UPPER;
				return KEY_CONSONANT;
			//@on
		}
		
		if (java.lang.Character.isDigit(c)) return KEY_DIGIT;
		
		return KEY_PUNCT;
		//@on
	}
	
	/**
	 * Verilen karakter kodunun karakter setini döndürür.<br>
	 * Mesela {@link #KEY_DIGIT} koduna ait karakter seti {@link #DIGITS}'dir.<br>
	 * Metodun amacı, verilen kodun hangi karakter grubuna ait olduğunu bildirmek.<br>
	 * Bunun tam tersini ise {@link #getCharacterCode(String)} metodu yapmaktadır.
	 *
	 * @param characterCode character code
	 * @return character set
	 */
	static String getCharacterSet(int characterCode) {
		
		switch (characterCode) {
			//@off
			case KEY_LETTER_UPPER:           return LETTERS_UPPER;
			case KEY_LETTER_LOWER:           return LETTERS_LOWER;
			case KEY_LETTER:                 return LETTERS;
			case KEY_SPACE:                  
			case KEY_WHITE_SPACES:           return " ";
			case KEY_VOWEL_THICK:            return VOWELS_THICK;
			case KEY_VOWEL_THICK_UPPER:      return VOWELS_THICK_UPPER;
			case KEY_VOWEL_THIN:             return VOWELS_THIN;
			case KEY_VOWEL_THIN_UPPER:       return VOWELS_THIN_UPPER;
			case KEY_VOWEL:                  return VOWELS;
			case KEY_VOWEL_UPPER:            return VOWELS_UPPER;
			case KEY_VOWEL_LOWER:            return VOWELS_LOWER;
			case KEY_CONSONANT_HARD:         return CONSONANTS_HARD;
			case KEY_CONSONANT_HARD_UPPER:   return CONSONANTS_HARD_UPPER;
			case KEY_CONSONANT_SOFT:         return CONSONANTS_SOFT;
			case KEY_CONSONANT_SOFT_UPPER:   return CONSONANTS_SOFT_UPPER;
			case KEY_CONSONANT:              return CONSONANTS;
			case KEY_CONSONANT_UPPER:        return CONSONANTS_UPPER;
			case KEY_CONSONANT_LOWER:        return CONSONANTS_LOWER;
			case KEY_DIGIT:                  return DIGITS;
			default:                         return PUNCT;
		}
		//@on
	}
	
	
}
