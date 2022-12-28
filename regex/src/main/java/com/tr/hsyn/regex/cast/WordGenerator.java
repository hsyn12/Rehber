package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Random word generator.
 */
public interface WordGenerator {

    static @NotNull WordGenerator ofLike(@NotNull CharSequence sequence) {

        return new WordGeneration(sequence);
    }

    /**
     * @param end limit
     * @return next random integer
     */
    int getNextIndex(int end);

    /**
     * @param c char
     * @return {@code true} if char shouldn't be in the word
     */
    boolean shouldExclude(char c);

    /**
     * @return codes for generating characters that describe characters
     */
    Integer[] getRegexCodes();

    /**
     * @return word length to generate
     */
    int getLength();

    @Nullable
    String getCharacterSet();

    WordGenerator setCharacterSet(@Nullable String set);

    WordGenerator exclude(char... chars);

    /**
     * @return random word
     */
    default String newWord() {

        char[] word  = new char[getLength()];
        String chars = getCharacterSet();

        if (chars != null)
            for (int i = 0; i < getLength(); i++)
                 word[i] = chars.charAt(getNextIndex(chars.length()));
        else
            for (int i = 0; i < getLength(); ) {

                int code = getRegexCodes()[i];

                String charSet = CharacterSet.getChracterSet(code);
                char   c       = charSet.charAt(getNextIndex(charSet.length()));

                if (shouldExclude(c)) continue;

                word[i++] = c;
            }

        return new String(word);
    }


}
