package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Karakter Gruplarını ve bunların sayısal kodlarını tanımlar.<br>
 * Karakter kodları, bir düzenli ifade için kullanılan her bir sembolün
 * sayısal karşığıdır.<br>
 * Ancak bu semboller alfabetik, sayısal, boşluk, yada noktalama işaretleri olabilir.<br>
 * Çokluk bildiren {@code '+,*,?,{x,y}'} sembolleri dikkate alınmaz.<br>
 * Amaç, bir kelimeyi en genel haliyle tarif etmek için kullanılan sembolleri bilmek.<br>
 * Bu şekilde bir üretici kodlara bakarak uygun karakter setine ait harf üretebilir.<br>
 * Aksi halde tüm düzenli ifadenin ayrıştırılması gerekirdi, ki bu işe hiç girmek istemiyoruz.<br>
 */
public interface CharacterSet {


    int KEY_DIGIT                = 1;
    int KEY_LETTER               = 2;
    int KEY_LETTER_LOWER         = 3;
    int KEY_LETTER_UPPER         = 4;
    int KEY_PUNCT                = 5;
    int KEY_CONSONANT            = 6;
    int KEY_CONSONANT_UPPER      = 7;
    int KEY_CONSONANT_LOWER      = 8;
    int KEY_VOWEL                = 9;
    int KEY_VOWEL_LOWER          = 10;
    int KEY_VOWEL_UPPER          = 11;
    int KEY_VOWEL_THIN           = 12;
    int KEY_VOWEL_THIN_UPPER     = 13;
    int KEY_VOWEL_THICK          = 14;
    int KEY_VOWEL_THICK_UPPER    = 15;
    int KEY_CONSONANT_HARD       = 16;
    int KEY_CONSONANT_HARD_UPPER = 17;
    int KEY_CONSONANT_SOFT       = 18;
    int KEY_CONSONANT_SOFT_UPPER = 19;
    int KEY_WHITE_SPACES         = 20;
    int KEY_SPACE                = 21;


    String ENGLISH_CHARS        = "qxwQXW";
    String LETTERS              = "abcçdefgğhıijklmnoöprsştuüvyzxwABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZXW";
    String LETTERS_LOWER        = "abcçdefgğhıijklmnoöprsştuüvyzxw";
    String LETTERS_UPPER        = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZXW";
    String DIGITS               = "0123456789";
    String PUNCT                = ".,;:!?%&#@()[]{}<>*+-_=\\/|\"'`$£¥€";
    String WHITESPACE           = " \t\r\n\f\\x0B";
    String CONSONANT            = "bcçdfgğhjklmnprsştvyzBCÇDFGĞHJKLMNPRSŞTVYZ" + ENGLISH_CHARS;
    String CONSONANT_LOWER      = "bcçdfgğhjklmnprsştvyzqxw";
    String CONSONANT_UPPER      = "BCÇDFGĞHJKLMNPRSŞTVYZ";
    String CONSONANT_HARD       = "çfhkpsştq";
    String CONSONANT_HARD_UPPER = "ÇFHKPSŞTQ";
    String CONSONANT_SOFT       = "bcdgjlmnprvzxyw";
    String CONSONANT_SOFT_UPPER = "BCDGJLMNPRVZXYW";
    String VOWELS               = "aeıioöuüAEIİOÖUÜ";
    String VOWELS_UPPER         = "AEIİOÖUÜ";
    String VOWELS_LOWER         = "aeıioöuü";
    String VOWELS_THICK         = "aıou";
    String VOWELS_THICK_UPPER   = "AIOU";
    String VOWELS_THIN          = "eiöü";
    String VOWELS_THIN_UPPER    = "EİÖÜ";
    String TURKISH_CHARS        = "çğıöşüÇĞİÖŞÜ";
    String TURKISH_CHARS_LOWER  = "çğıöşü";
    String TURKISH_CHARS_UPPER  = "ÇĞİÖŞÜ";


    //! ünlü ile biten hecelere açık hece denir
    //! kalın ünlülerden sonra kalın, ince ünlülerden sonra ince ünlülerin gelmesine büyük ünlü uyumu denir
    //- düz ünlülerden sonra düz ünlülerin, yuvarlak ünlülerden
    //- sonra dar yuvarlak ya da geniş düz ünlülerin gelmesine küçük ünlü uyumu denir
    //! Türkçede iki ünlü yan yana gelmez

    /**
     * Returns character class for the given character.
     *
     * @param c the character
     * @return character class for the given character
     */
    @NotNull
    static String getCharecterSet(char c) {

        return getCharecterSet(c, false, false);
    }

    /**
     * Verilen karakterin ait olduğu karakter setini döndürür.<br>
     *
     * @param c            karakter
     * @param moreSpecific {@code true} ise ünlü harfler için ince-kalın, ünsüz harfler için sert-yumuşak harf ayrımı yapar
     * @return karakter seti
     */
    static String getCharecterSet(char c, boolean specific, boolean moreSpecific) {

        //@off
		switch (c) {

			//- Boşluk karakterleri
			case ' ':
			case '\n':
			case '\t':
			case '\r':
			case '\f': return Character.SPACE;//- hepsi için sadece bir boşluk

			//- Alfabetik karakterler

			case 'a':
			case 'A':
				if (moreSpecific)   return VOWELS_THICK;
				if(specific) return VOWELS_LOWER;
			case 'b':
			case 'B':
			case 'c':
			case 'C': if (moreSpecific) return CONSONANT_SOFT;
			case 'ç':
			case 'Ç': if (moreSpecific) return CONSONANT_HARD;
			case 'd':
			case 'D':
				if (moreSpecific) return CONSONANT_SOFT;
				if(specific) return CONSONANT_LOWER;
			case 'e':
			case 'E':
				if (moreSpecific) return VOWELS_THIN;
				if(specific) return VOWELS_LOWER;
			case 'f':
			case 'F': if (moreSpecific) return CONSONANT_HARD;
			case 'g':
			case 'G': if (moreSpecific) return CONSONANT_SOFT;
			case 'ğ':
			case 'Ğ':
			case 'h':
			case 'H':
				if (moreSpecific) return CONSONANT_HARD;
				if(specific) return CONSONANT_LOWER;
			case 'i':
			case 'İ': if (moreSpecific) return VOWELS_THIN;
			case 'ı':
			case 'I':
				if (moreSpecific) return VOWELS_THICK;
				if(specific) return VOWELS_LOWER;
			case 'j':
			case 'J': if (moreSpecific) return CONSONANT_SOFT;
			case 'k':
			case 'K': if (moreSpecific) return CONSONANT_HARD;
			case 'l':
			case 'L':
			case 'm':
			case 'M':
			case 'n':
			case 'N':
				if (moreSpecific) return CONSONANT_SOFT;
				if(specific) return CONSONANT_LOWER;
			case 'o':
			case 'O': if (moreSpecific) return VOWELS_THICK;
			case 'ö':
			case 'Ö':
				if (moreSpecific) return VOWELS_THIN;
				if(specific) return VOWELS_LOWER;
			case 'p':
			case 'P': if (moreSpecific) return CONSONANT_HARD;
			case 'q':
			case 'Q':
			case 'r':
			case 'R': if (moreSpecific) return CONSONANT_SOFT;
			case 's':
			case 'S':
			case 'ş':
			case 'Ş':
			case 't':
			case 'T':
				if (moreSpecific) return CONSONANT_HARD;
				if(specific) return CONSONANT_LOWER;
			case 'u':
			case 'U': if (moreSpecific) return VOWELS_THICK;
			case 'ü':
			case 'Ü': if (moreSpecific) return VOWELS_THIN;
			case 'v':
			case 'V':
			case 'w':
			case 'W':
			case 'x':
			case 'X':
			case 'y':
			case 'Y':
			case 'z':
			case 'Z':
				if (moreSpecific) return CONSONANT_SOFT;
				return LETTERS_LOWER;

			//@on
        }

        if (java.lang.Character.isDigit(c)) return DIGITS;

        return Posix.PUNCT;
    }

    /**
     * Verilen karakter sınıfını uygun karakter aralığa çevirir.
     *
     * @param characterSet karakter seti
     * @return {@code [characterSet]}
     */
    static String convertToRange(@NotNull String characterSet) {

        switch (characterSet) {

            //@off
			case           Character.SPACE: return "[\\s]";
			case                WHITESPACE: return Range.WHITE_SPACE;
			case              VOWELS_THICK: return "[" + VOWELS_THICK + "]";
			case               VOWELS_THIN: return "[" + VOWELS_THIN + "]";
			case            CONSONANT_HARD: return "[" + CONSONANT_HARD + "]";
			case            CONSONANT_SOFT: return "[" + CONSONANT_SOFT + "]";
			case                 CONSONANT: return "[" + CONSONANT + "]";
			case           CONSONANT_LOWER: return "[" + CONSONANT_LOWER + "]";
			case           CONSONANT_UPPER: return "[" + CONSONANT_UPPER + "]";
			case              VOWELS_LOWER: return "[" + VOWELS_LOWER + "]";
			case              VOWELS_UPPER: return "[" + VOWELS_UPPER + "]";
			case                    VOWELS: return "[" + VOWELS + "]";
			case             LETTERS_UPPER: return Range.ALPHA_UPPER;
			case             LETTERS_LOWER: return Range.ALPHA_LOWER;
			case                   LETTERS: return Range.ALPHA;
			case                    DIGITS: return Range.NUMERIC;
			                       default: return Range.PUNCT;
			//@on
        }
    }

    /**
     * Verilen karakter setinin karakter kodunu döndürür.<br>
     *
     * @param clazz karakter seti
     * @return karakter kodu
     */
    static int getCharacterCode(@NotNull String clazz) {

        //@off
		switch (clazz) {

			case          VOWELS_THICK: return KEY_VOWEL_THICK;
			case    VOWELS_THICK_UPPER: return KEY_VOWEL_THICK_UPPER;
			case           VOWELS_THIN: return KEY_VOWEL_THIN;
			case     VOWELS_THIN_UPPER: return KEY_VOWEL_THIN_UPPER;
			case                VOWELS: return KEY_VOWEL;
			case          VOWELS_UPPER: return KEY_VOWEL_UPPER;
			case          VOWELS_LOWER: return KEY_VOWEL_LOWER;
			case             CONSONANT: return KEY_CONSONANT;
			case       CONSONANT_UPPER: return KEY_CONSONANT_UPPER;
			case       CONSONANT_LOWER: return KEY_CONSONANT_LOWER;
			case        CONSONANT_HARD: return KEY_CONSONANT_HARD;
			case  CONSONANT_HARD_UPPER: return KEY_CONSONANT_HARD_UPPER;
			case        CONSONANT_SOFT: return KEY_CONSONANT_SOFT;
			case  CONSONANT_SOFT_UPPER: return KEY_CONSONANT_SOFT_UPPER;
			case               LETTERS: return KEY_LETTER;
			case         LETTERS_UPPER: return KEY_LETTER_UPPER;
			case         LETTERS_LOWER: return KEY_LETTER_LOWER;
			case                DIGITS: return KEY_DIGIT;
		}
		//@on

        return KEY_PUNCT;
    }

    /**
     * Verilen karakter kodunun karakter setini döndürür.<br>
     * Mesela {@link #KEY_DIGIT} koduna ait karakter seti {@link #LETTERS}'dir.<br>
     * Karakter setleri düzenli ifade sembolleri olabileceği gibi, direk karakterler de olabilir.<br>
     * Mesela {@link #VOWELS} seti direk karakterlerden oluşurken {@code 'aeıioöuüAEIİOÖUÜ'},<br>
     * {@link Character#PUNC} seti ise {@code \\p{P}} sembolünden oluşmaktadır.<br>
     * Metodun amacı, verilen kodun hangi karakter grubuna ait olduğunu bildirmak.<br>
     * Bunun tam tersini ise {@link #getCharacterCode(String)} metodu yapmaktadır.
     *
     * @param characterCode character code
     * @return character set
     */
    static String getChracterSet(int characterCode) {

        switch (characterCode) {
            //@off
			case KEY_LETTER_UPPER:           return LETTERS_UPPER;
			case KEY_LETTER_LOWER:           return LETTERS_LOWER;
			case KEY_LETTER:                 return LETTERS;
			case KEY_VOWEL_THICK:            return VOWELS_THICK;
			case KEY_VOWEL_THICK_UPPER:      return VOWELS_THICK_UPPER;
			case KEY_VOWEL_THIN:             return VOWELS_THIN;
			case KEY_VOWEL_THIN_UPPER:       return VOWELS_THIN_UPPER;
			case KEY_VOWEL:                  return VOWELS;
			case KEY_VOWEL_UPPER:            return VOWELS_UPPER;
			case KEY_VOWEL_LOWER:            return VOWELS_LOWER;
			case KEY_CONSONANT_HARD:         return CONSONANT_HARD;
			case KEY_CONSONANT_HARD_UPPER:   return CONSONANT_HARD_UPPER;
			case KEY_CONSONANT_SOFT:         return CONSONANT_SOFT;
			case KEY_CONSONANT_SOFT_UPPER:   return CONSONANT_SOFT_UPPER;
			case KEY_CONSONANT:              return CONSONANT;
			case KEY_CONSONANT_UPPER:        return CONSONANT_UPPER;
			case KEY_CONSONANT_LOWER:        return CONSONANT_LOWER;
			case KEY_DIGIT:                  return DIGITS;
			default:                         return PUNCT;
		}
		//@on
    }

    /**
     * Verilen karakter setinin kodunu döndürür.<br>
     *
     * @param characterSet karakter seti
     * @return karakter kodu
     */
    static int getRegexCode(@NotNull String characterSet) {

        switch (characterSet) {
            //@off
			case       Character.SPACE:  return KEY_SPACE;
			case            WHITESPACE:  return KEY_WHITE_SPACES;
			case               LETTERS:  return KEY_LETTER;
			case         LETTERS_LOWER:  return KEY_LETTER_LOWER;
			case         LETTERS_UPPER:  return KEY_LETTER_UPPER;
			case                VOWELS:  return KEY_VOWEL;
			case          VOWELS_LOWER:  return KEY_VOWEL_LOWER;
			case          VOWELS_UPPER:  return KEY_VOWEL_UPPER;
			case             CONSONANT:  return KEY_CONSONANT;
			case       CONSONANT_LOWER:  return KEY_CONSONANT_LOWER;
			case       CONSONANT_UPPER:  return KEY_CONSONANT_UPPER;
			case        CONSONANT_SOFT:  return KEY_CONSONANT_SOFT;
			case  CONSONANT_SOFT_UPPER:  return KEY_CONSONANT_SOFT_UPPER;
			case        CONSONANT_HARD:  return KEY_CONSONANT_HARD;
			case  CONSONANT_HARD_UPPER:  return KEY_CONSONANT_HARD_UPPER;
			case                DIGITS:  return KEY_DIGIT;
			                  default :  return KEY_PUNCT;
		}
		//@on
    }
}
