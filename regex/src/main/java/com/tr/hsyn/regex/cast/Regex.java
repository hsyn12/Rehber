package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Matchy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A regular expression editor.
 */
@SuppressWarnings("StringBufferMayBeStringBuilder")
public interface Regex {

    /**
     * Creates a new match with the given regex.
     *
     * @param regex the regex to use
     * @return match with the given regex
     */
    @NotNull
    static Regex of(@NotNull String regex) {

        return new Matchy(regex);
    }

    /**
     * Creates a new match with empty regex.
     *
     * @return match with empty regex
     */
    @NotNull
    static Regex of() {

        return new Matchy("");
    }

    /**
     * @return regex pattern for matching.
     */
    @NotNull
    String getRegex();

    /**
     * Adds a CharSequence to the end of the regex.
     *
     * @param s the CharSequence to add.
     * @return match object with the added CharSequence.
     */
    @NotNull Regex add(@NotNull CharSequence s);

    /**
     * Adds a character to the end of the regex.
     *
     * @param c the character to add.
     * @return match object with the added character.
     */
    @NotNull Regex add(char c);

    /**
     * Inserts an integer at the specified index.
     *
     * @param i the index at which to insert the integer.
     * @return match object with the inserted integer.
     */
    @NotNull Regex add(int i);

    /**
     * Puts the match in a group and captures the group.
     *
     * @param name the name of the group
     * @return match in a group {@code (?<name>this)}
     */
    @NotNull Regex asGroup(String name);

    @NotNull Regex addFirst(@NotNull CharSequence s);

    /**
     * Adds modifier
     *
     * @param modifier modifier
     * @return regex object with the added modifier.
     */
    default @NotNull Regex add(@NotNull Modifier modifier) {

        return add(modifier.getRegex());
    }

    /**
     * Adds given match.
     *
     * @param regex the match to add.
     * @return match object with the added match.
     */
    default @NotNull Regex add(@NotNull Regex regex) {

        return add(regex.getRegex());
    }

    /**
     * Adss the given range.
     *
     * @param range range
     * @return regex object with the added range.
     */
    default @NotNull Regex add(@NotNull Range range) {

        return add(range.getRegex());
    }

    /**
     * Adds the given alternate.
     *
     * @param alternate alternate
     * @return regex object with the added alternate.
     */
    default @NotNull Regex add(@NotNull Alternate alternate) {

        return add(alternate.getRegex());
    }

    /**
     * Adds the given regex as a group.
     *
     * @param regex the match to add.
     * @param name  the name of the group.
     * @return match object with the added match.
     */
    default @NotNull Regex addGroup(@NotNull Regex regex, @NotNull String name) {

        return add(String.format("(?<%s>%s)", name, regex.getRegex()));
    }

    /**
     * Adds quantifier to the match.
     *
     * @param min minimum number of repetitions
     * @param max maximum number of repetitions
     * @return match with quantifier {@code regex{min,max}}
     */
    default @NotNull Regex times(int min, int max) {

        return add(Quantifier.BETWEEN(min, max));
    }

    /**
     * Adds quantifier to the match.
     *
     * @param count the count of the quantifier
     * @return match with quantifier {@code regex{count}}
     */
    default @NotNull Regex times(int count) {

        return add(Quantifier.EXACTLY(count));
    }

    /**
     * Adds the boundary meta character to the match.
     *
     * @return match with boundary meta character {@code \b}
     */
    default @NotNull Regex boundary() {

        return add(Anchor.BOUNDARY);
    }

    /**
     * Adds the space meta character to the match
     *
     * @return match with space meta character
     */
    default @NotNull Regex space() {

        return add(Character.WHITE_SPACE);
    }

    /**
     * Adds the non-space meta character to the match
     *
     * @return match with non-space meta character
     */
    default @NotNull Regex nonSpace() {

        return add(Character.NON_WHITE_SPACE);
    }

    /**
     * Puts the match in a group.
     *
     * @return match in a group {@code (this)}
     */
    default @NotNull Regex asGroup() {

        return addFirst("(").add(")");
    }

    /**
     * Puts the regex in a range and negates it.
     *
     * @return match in a range {@code [^this]}
     */
    default @NotNull Regex asRangeNegated() {

        return addFirst("[^").add(']');
    }

    /**
     * Backreferences to a group.
     *
     * @param groupName the name of the group
     * @return match with the group backreference {@code \k<groupName>}
     */
    default @NotNull Regex refereTo(String groupName) {

        return add(String.format("\\k<%s>", groupName));
    }

    /**
     * Backreferences  to a group.
     *
     * @param groupOrder the order of the group
     * @return match with the group backreference {@code \k<groupOrder>}
     */
    @SuppressWarnings("DefaultLocale")
    default @NotNull Regex refereTo(int groupOrder) {

        return add(String.format("\\k<%d>", groupOrder));
    }

    /**
     * Puts the regex into the range brackets {@code [regex]}
     *
     * @return match with the regex in the brackets {@code [regex]}
     */
    default @NotNull Regex asRange() {

        return addFirst("[").add("]");
    }

    /**
     * Adds a meta character {@code "."}
     *
     * @return match with the meta character added {@code "."}
     */
    default @NotNull Regex any() {

        return add(Character.ANY);
    }

    default @NotNull Regex ignoreCase() {

        return add(Modifier.of().ignoreCase());
    }

    /**
     * Adds lowercase letter meta character.
     *
     * @return regex object with added meta character
     */
    default @NotNull Regex letterLower() {

        return add(Character.LETTER_LOWER);
    }

    /**
     * Adds uppercase letter meta character.
     *
     * @return regex object with added meta character
     */
    default @NotNull Regex letterUpper() {

        return add(Character.LETTER_UPPER);
    }

    /**
     * Adds the meta character {@code ?}
     *
     * @return match with the meta character {@code regex?}
     */
    default @NotNull Regex zeroOrOne() {

        return add(Quantifier.ZERO_OR_ONE);
    }

    /**
     * Adds the meta character {@code *}
     *
     * @return match with the meta character {@code regex*}
     */
    @NotNull
    default Regex zeroOrMore() {

        return add(Quantifier.ZERO_OR_MORE);
    }

    /**
     * Adds the meta character {@code +}
     *
     * @return match with the meta character {@code regex+}
     */
    default @NotNull Regex oneOrMore() {

        return add(Quantifier.ONE_OR_MORE);
    }

    /**
     * Adds quantifier to the match.
     *
     * @param count the minimum number of times the match must occur
     * @return match with quantifier {@code regex{count,}
     */
    default @NotNull Regex atLeast(int count) {

        return add(Quantifier.AT_LEAST(count));
    }

    /**
     * Adds quantifier to the match.
     *
     * @param count the maximum number of times the match must occur
     * @return match with quantifier {@code regex{0,count}}
     */
    default @NotNull Regex atMost(int count) {

        return add(Quantifier.AT_MOST(count));
    }

    /**
     * Adds a digit character to the match.
     *
     * @return match {@code \p{N}}
     */
    default @NotNull Regex digit() {

        return add(Character.DIGIT);
    }

    /**
     * Adds a non-digit meta character to the match.
     *
     * @return match {@code \P{N}}
     */
    default @NotNull Regex nonDigit() {

        return add(Character.NON_DIGIT);
    }

    /**
     * Adds a letter meta character.
     *
     * @return match with the letter meta character added {@code \p{L}}
     */
    default @NotNull Regex letter() {

        return add(Character.LETTER);
    }

    /**
     * Adds a non-letter meta character.
     *
     * @return match with the non-letter meta character added {@code \P{L}}
     */
    default @NotNull Regex nonLetter() {

        return add(Character.NON_LETTER);
    }

    /**
     * Adds a control meta character.
     *
     * @return match with the control meta character added {@code \p{C}}
     */
    default @NotNull Regex control() {

        return add(Character.CONTROL);
    }

    /**
     * Adds a non-control meta character.
     *
     * @return match with the non-control meta character added {@code \P{C}}
     */
    default @NotNull Regex nonControl() {

        return add(Character.NON_CONTROL);
    }

    /**
     * Adds a double slash {@code "\\"}
     *
     * @return match with the double slash added {@code "\\"}
     */
    default @NotNull Regex addEscapeSlash() {

        return add(Character.SLASH);
    }

    /**
     * Tests created regex against the given string (all string matching).
     *
     * @param text the text to test
     * @return true if the regex matches the text
     */
    default boolean test(@NotNull CharSequence text) {

        return text.toString().matches(getRegex());
    }

    /**
     * Tests created regex against the given string.
     *
     * @param text       the text to test
     * @param beginIndex the index to start the match
     * @return if the regex matches the text until the limit is reached or the end of the text returns {@code true}, otherwise {@code false}
     */
    default boolean test(@NotNull CharSequence text, int beginIndex) {

        return Pattern.compile(getRegex()).matcher(text.toString()).find(beginIndex);
    }

    /**
     * Tests text contains the regex or not (partial match).
     *
     * @param text the text to test
     * @return true if the text contains the regex
     */
    default boolean containsIn(@NotNull CharSequence text) {

        return test(text, 0);
    }

    /**
     * Returns all matches of the regex in the text.
     *
     * @param text the text to test
     * @return all matches of the regex in the text
     */
    default List<Index> indexesOf(@NotNull CharSequence text) {

        return indexesOf(text, 0);
    }

    /**
     * Returns all matches of the regex in the text.
     *
     * @param text       the text to test
     * @param beginIndex the index to start from
     * @return all matches of the regex in the text
     */
    @NotNull
    default List<Index> indexesOf(@NotNull CharSequence text, int beginIndex) {

        var list = new ArrayList<Index>();

        if (beginIndex < 0) beginIndex = 0;
        if (text.length() <= beginIndex) return list;

        var pattern = Pattern.compile(getRegex());
        var matcher = pattern.matcher(text.toString().substring(beginIndex));

        while (matcher.find())
            list.add(Index.of(matcher.start(), matcher.end()));

        return list;
    }

    /**
     * Returns the matches from the given string.
     *
     * @param text the text to extract from
     * @return extracted matches list
     */
    @NotNull
    default List<String> matchesOf(@NotNull CharSequence text) {

        return matchesOf(text, 0);
    }

    /**
     * Extracts the match from the given string.
     *
     * @param text  the text to extract from
     * @param limit the maximum number of match to extract. {@code limit < 1} means no limit.
     * @return extracted text
     */
    default List<String> matchesOf(@NotNull CharSequence text, int limit) {

        int          count   = 0;
        var          matcher = createMatcher(text);
        List<String> list    = new ArrayList<>();

        while (matcher.find()) {

            var group = matcher.group();

            if (group != null) {

                list.add(group);
                count++;
            }

            if (limit > 0 && count >= limit) break;
        }

        return list;
    }

    /**
     * Returns all matches.
     *
     * @param text      the text to test
     * @param groupName the name of the group for matches
     * @param limit     the maximum number of matches
     * @return list of matches
     */
    default List<String> matchesGroupOf(@NotNull CharSequence text, @NotNull String groupName, int limit) {

        int          count   = 0;
        Matcher      matcher = createMatcher(text);
        List<String> list    = new ArrayList<>();

        while (matcher.find()) {

            var group = matcher.group(groupName);

            if (group != null) {

                list.add(group);
                count++;
            }

            if (limit > 0 && count >= limit) break;
        }

        return list;
    }

    /**
     * Returns all matched indexes of the given group name
     *
     * @param text      the text to get from
     * @param groupName the name of the group
     * @return list of matches indexes
     */
    default List<Index> findGroups(@NotNull CharSequence text, @NotNull String groupName) {

        var         matcher = createMatcher(text);
        List<Index> list    = new ArrayList<>();

        while (matcher.find()) {

            var group = matcher.group(groupName);

            if (group != null)
                list.add(Index.of(matcher.start(groupName), matcher.end(groupName)));

        }

        return list;
    }

    /**
     * Returns matcher by specified text
     *
     * @param text the text to create from
     * @return matcher
     */
    @NotNull
    default Matcher createMatcher(@NotNull CharSequence text) {

        return Pattern.compile(getRegex()).matcher(text);
    }

    /**
     * Removes the match from the given string.
     *
     * @param text the text to remove from
     * @return text without the match
     */
    default String removeFrom(@NotNull CharSequence text) {

        return Pattern.compile(getRegex()).matcher(text).replaceAll("");
        //return removeFrom(text, 0);
    }

    /**
     * Removes the match from the given string.
     *
     * @param text  the text to remove from
     * @param limit the maximum number of matches to remove. {@code limit < 1} means no limit.
     * @return text without the match
     */
    default String removeFrom(@NotNull CharSequence text, int limit) {

        int count = 0;
        var regex = Pattern.compile(getRegex());

        var matcher = regex.matcher(text);

        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {

            matcher.appendReplacement(sb, "");
            count++;

            if (limit > 0 && count >= limit) break;
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Replaces the match with the given string.
     *
     * @param text        the text to replace in
     * @param replacement the replacement string
     * @return text with the replacement
     */
    default String replaceFrom(@NotNull CharSequence text, @NotNull String replacement) {

        return Pattern.compile(getRegex()).matcher(text).replaceAll(replacement);
    }

    /**
     * Replaces the match with the given string.
     *
     * @param text        the text to replace in
     * @param replacement the replacement string
     * @param limit       the maximum number of replacements to make. {@code limit < 1} means no limit.
     * @return text with the replacement
     */
    default String replaceFrom(@NotNull String text, @NotNull String replacement, int limit) {

        int count   = 0;
        var matcher = createMatcher(text);

        //!  StringBuilder android için yok görünüyor
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {

            matcher.appendReplacement(sb, replacement);

            count++;

            if (limit > 0 && count >= limit) break;
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Index data
     */
    class Index {

        public final int start;
        public final int end;

        public Index(int start, int end) {

            this.start = start;
            this.end   = end;
        }

        @NotNull
        static Index of(int start, int end) {

            return new Index(start, end);
        }

        @NotNull
        @Override
        public String toString() {

            return "Index{" +
                   "start=" + start +
                   ", end=" + end +
                   '}';
        }
    }
}
