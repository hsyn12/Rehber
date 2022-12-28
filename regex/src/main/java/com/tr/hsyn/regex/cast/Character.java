package com.tr.hsyn.regex.cast;


/**
 * Meta characters.<br>
 *
 * @author hsyn 14 Haziran 2022 Sal? 12:21
 */
public interface Character {


    /**
     * Delimiters.
     */
    String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
    /**
     * Any character other than newline
     */
    String ANY                  = ".";
    /**
     * A whitespace character, including line break. {@code [ \t\r\n\f\x0B]}
     */
    String WHITE_SPACE          = "\\p{Z}";

    /**
     * A non-whitespace chacracter {@code [^\s]}
     */
    String NON_WHITE_SPACE  = "\\P{Z}";
    String SPACE            = "\\s";
    String NON_SPACE        = "\\S";
    /**
     * A digit 0-9 {@code [0-9]}
     */
    String DIGIT            = "\\p{N}";
    /**
     * A non-digit chacracter {@code [^0-9]}
     */
    String NON_DIGIT        = "\\P{N}";
    /**
     * An alphabetic character {@code [a-zA-Z]}
     */
    String LETTER           = "\\p{L}";
    /**
     * A lowercase alphabetic character {@code [a-z]}
     */
    String LETTER_LOWER     = "\\p{Ll}";
    String NON_LETTER_LOWER = "\\P{Ll}";
    /**
     * An uppercase alphabetic character {@code [A-Z]}
     */
    String LETTER_UPPER     = "\\p{Lu}";
    String NON_LETTER_UPPER = "\\P{Lu}";
    /**
     * A non-alphabetic character {@code [^a-zA-Z]}
     */
    String NON_LETTER       = "\\P{L}";
    /**
     * A control character {@code [\p{Cntrl}]}
     */
    String CONTROL          = "\\p{C}";

    /**
     * Punctuation characters {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
     */
    String PUNC        = "\\p{P}";
    String NON_PUNC    = "\\P{P}";
    /**
     * A non-control character {@code [^\p{C}]}
     */
    String NON_CONTROL = "\\P{C}";
    String SYMBOL      = "\\p{S}";
    String SLASH       = "\\";


    static String getCharacterClass(char c) {

        if (java.lang.Character.isSpaceChar(c)) return SPACE;
        if (java.lang.Character.isDigit(c)) return DIGIT;
        if (java.lang.Character.isLetter(c)) return LETTER;

        return PUNC;
    }


}