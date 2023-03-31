package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.DateGeneration;

import org.jetbrains.annotations.NotNull;


/**
 * Date pattern editor.
 *
 * @author hsyn 16 Haziran 2022 Perşembe 13:28
 */
public interface DateGenerator {

    /**
     * Starts creating a new pattern.
     *
     * @return DateGenerator
     */
    @NotNull
    static DateGenerator start() {

        return new DateGeneration("");
    }

    /**
     * @return the date pattern
     */
    @NotNull String getPattern();

    /**
     * Adds {@code d} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator oneDigitDay();

    /**
     * Adds {@code dd} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator twoDigitDay();

    @NotNull DateGenerator twoDigitMonth();

    @NotNull DateGenerator oneDigitMonth();

    /**
     * Adds {@code yy} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator twoDigitYear();

    /**
     * Adds {@code yyyy} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator fourDigitYear();

    /**
     * Adds {@code MMM} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator shortMonthName();

    /**
     * Adds {@code MMMM} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator longMonthName();

    /**
     * Adds {@code EEE} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator shortDayName();

    /**
     * Adds {@code EEEE} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator longDayName();

    /**
     * Adds {@code HH:mm} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator shortTime();

    /**
     * Adds {@code HH:mm:ss} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator longTime();

    /**
     * Adds {@code HH} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator hour();

    /**
     * Adds {@code mm} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator minute();

    /**
     * Adds {@code ss} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator second();

    /**
     * Adds space to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator space();

    /**
     * Adds {@code :} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator colon();

    /**
     * Adds {@code -} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator hyphen();

    /**
     * Adds {@code .} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator dot();

    /**
     * Adds {@code /} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator slash();

    /**
     * Adds {@code \} to the pattern.
     *
     * @return DateGenerator
     */
    @NotNull DateGenerator backSlash();

    /**
     * Adds given separator to the pattern.
     *
     * @param separator the separator to add
     * @return DateGenerator
     */
    @NotNull DateGenerator putSeparator(@NotNull String separator);


}