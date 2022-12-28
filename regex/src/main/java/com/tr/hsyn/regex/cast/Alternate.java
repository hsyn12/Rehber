package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Alternative;

import org.jetbrains.annotations.NotNull;


/**
 * Create a new regex for alternation. {@code (x|y|z)}
 *
 * @author xyz 19 Haziran 2022 Pazar 18:33
 */
public interface Alternate {

    /**
     * Creates a new match for the alternation. {@code (regex|alternate)}
     *
     * @param alternate the alternate to match
     * @return alternate with the alternate added
     */
    @NotNull
    static Alternate of(@NotNull Alternate alternate) {

        return new Alternative(alternate);
    }

    /**
     * Creates a new match for the alternation. {@code (regex|match)}
     *
     * @param regex the match to match
     * @return alternate with the match added
     */
    @NotNull
    static Alternate of(@NotNull Regex regex) {

        return new Alternative(regex);
    }

    /**
     * Creates a new match for the alternation. {@code (regex|range)}
     *
     * @param range the range to match
     * @return alternate with the range added
     */
    @NotNull
    static Alternate of(@NotNull Range range) {

        return new Alternative(range);
    }

    /**
     * Creates a new match for the alternation. {@code (regex|i)}
     *
     * @param i the integer to add
     * @return alternate with the integer added
     */
    @NotNull
    static Alternate of(int i) {

        return new Alternative(i);
    }

    /**
     * Creates a new match for the alternation. {@code (i|j)}
     *
     * @param i the integer to match
     * @param j the second integer to match
     * @return alternate with the integer added
     */
    @NotNull
    static Alternate of(int i, int j) {

        return new Alternative(i, j);
    }

    /**
     * @return the regex for alternation
     */
    @NotNull String getRegex();

    /**
     * Add a string to the alternation.
     *
     * @param s the match to add
     * @return alternate with the match added
     */
    @NotNull Alternate add(@NotNull String s);

    /**
     * Add an integer to the alternation.
     *
     * @param i the integer to add
     * @return alternate with the integer added
     */
    @NotNull Alternate add(int i);

    @NotNull Alternate add(char c);

    /**
     * Add a match to the alternation.
     *
     * @param regex the match to add
     * @return alternate with the match added
     */
    default @NotNull Alternate add(@NotNull Regex regex) {

        return add(regex.getRegex());
    }

    /**
     * Add a range to the alternation.
     *
     * @param range the range to add
     * @return alternate with the range added
     */
    default @NotNull Alternate add(@NotNull Range range) {

        return add(range.getRegex());
    }

    /**
     * Add an alternate to the alternation.
     *
     * @param alternate the alternate to add
     * @return alternate with the alternate added
     */
    default @NotNull Alternate add(@NotNull Alternate alternate) {

        return add(alternate.getRegex());
    }

    /**
     * Creates a new match for the alternation.
     *
     * @return a new match for the alternation
     */
    @NotNull
    default Alternate of() {

        return new Alternative();
    }

}
