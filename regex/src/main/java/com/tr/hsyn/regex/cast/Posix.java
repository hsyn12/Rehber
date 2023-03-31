package com.tr.hsyn.regex.cast;


public interface Posix {

    //?      POSIX Character Classes                 Meaning                               Character Class
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{ASCII}               All ASCII characters                              [\x00-\x7F]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Digit}               Any digit                                         [0-9]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Lower}               Lowercase alphabets                               [a-z]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Upper}               Uppercase alphabets                               [A-Z]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Alpha}               Alphabets                                         [a-zA-Z]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Alnum}               Alphanumeric characters                           [a-zA-Z0-9]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Punct}               Punctuation characters               [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Blank}               Space and tab characters                          [ \t]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Space}               Space characters                                  [ \t\r\n\f]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Graph}               A visible ASCII character                         [\p{Alnum}\p{Punct}]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Print}               A printable character                             [\p{Graph}\x20]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{Cntrl}               Control characters                                [\x00-\x1F\x7F]
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{XDigit}              Hexadecimal digits                                [0-9A-Fa-f]
    //?|-----------------------------------------------------------------------------------------------------------------|


    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{javaLowerCase}     Equivalent to java.lang.Character.isLowerCase()
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{javaUpperCase}     Equivalent to java.lang.Character.isUpperCase()
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{javaWhitespace}    Equivalent to java.lang.Character.isWhitespace()
    //?|-----------------------------------------------------------------------------------------------------------------|
    //!             \p{javaMirrored}      Equivalent to java.lang.Character.isMirrored()
    //?|-----------------------------------------------------------------------------------------------------------------|

    /**
     * All ASCII characters {@code  [\x00-\x7F]}
     */
    String ASCII           = "\\p{ASCII}";
    /**
     * Any digit {@code [0-9]}
     */
    String DIGIT           = "\\p{Digit}";
    /**
     * Lowercase alphabets {@code [a-z]}
     */
    String LOWER           = "\\p{Lower}";
    /**
     * Uppercase alphabets {@code [A-Z]}
     */
    String UPPER           = "\\p{Upper}";
    /**
     * Alphabets {@code [a-zA-Z]}
     */
    String ALPHA           = "\\p{Alpha}";
    /**
     * Alphanumeric characters {@code [a-zA-Z0-9]}
     */
    String ALNUM           = "\\p{Alnum}";
    /**
     * Punctuation characters {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
     */
    String PUNCT           = "\\p{Punct}";
    /**
     * Space and tab characters {@code [ \t]}
     */
    String BLANK           = "\\p{Blank}";
    /**
     * Space characters {@code [ \t\r\n\f]}
     */
    String SPACE           = "\\p{Space}";
    /**
     * A visible ASCII character {@code [\p{Alnum}\p{Punct}]}
     */
    String GRAPH           = "\\p{Graph}";
    /**
     * A printable character {@code [\p{Graph}\x20]}
     */
    String PRINT           = "\\p{Print}";
    /**
     * Control characters {@code [\x00-\x1F\x7F]}
     */
    String CNTRL           = "\\p{Cntrl}";
    /**
     * Hexadecimal digits {@code [0-9A-Fa-f]}
     */
    String XDIGIT          = "\\p{XDigit}";
    /**
     * Equivalent to java.lang.Character.isLowerCase()
     */
    String JAVA_LOWER_CASE = "\\p{javaLowerCase}";
    /**
     * Equivalent to java.lang.Character.isUpperCase()
     */
    String JAVA_UPPER_CASE = "\\p{javaUpperCase}";
    /**
     * Equivalent to java.lang.Character.isWhitespace()
     */
    String JAVA_WHITESPACE = "\\p{javaWhitespace}";
    /**
     * Equivalent to java.lang.Character.isMirrored()
     */
    String JAVA_MIRRORED   = "\\p{javaMirrored}";


}
