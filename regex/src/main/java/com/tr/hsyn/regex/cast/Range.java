package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Ranger;

import org.jetbrains.annotations.NotNull;


/**
 * Range constants.
 */
public interface Range {

    //! [a-zA-Z0-9_] are called word characters. All other characters are considered non-word characters

    String TURKISH_CHARS = "[" + CharacterSet.TURKISH_CHARS + "]";

    String TURKISH_CHARS_LOWER = "[" + CharacterSet.TURKISH_CHARS_LOWER + "]";
    String TURKISH_CHARS_UPPER = "[" + CharacterSet.TURKISH_CHARS_UPPER + "]";
    /**
     * Charactes in the range a-z or A-Z
     */
    String ALPHA               = "[" + Character.LETTER + CharacterSet.TURKISH_CHARS + "]";
    /**
     * Charactes in the range a-z or A-Z or 0-9
     */
    String ALPHA_NUMERIC       = "[" + Character.LETTER + CharacterSet.TURKISH_CHARS + Character.DIGIT + "]";
    /**
     * Character in the range A-Z
     */
    String ALPHA_UPPER         = "[" + Character.LETTER_UPPER + CharacterSet.TURKISH_CHARS_UPPER + "]";

    /**
     * Character in the range a-z
     */
    String ALPHA_LOWER = "[" + Character.LETTER_LOWER + CharacterSet.TURKISH_CHARS_LOWER + "]";

    /**
     * Character in the range 0-9
     */
    String NUMERIC = "[" + Character.DIGIT + "]";

    /**
     * White spaces range
     */
    String WHITE_SPACE = "[" + Character.WHITE_SPACE + "]";

    /**
     * Punctuation range
     */
    String PUNCT = "[" + Character.PUNC + "]";

    /**
     * @return new empty range
     */
    @NotNull
    static Range Of() {

        return new Ranger();
    }

    /**
     * @param sequence sequence
     * @return new range with given sequence
     */
    @NotNull
    static Range Of(@NotNull CharSequence sequence) {

        return new Ranger(sequence);
    }

    /**
     * @param first first
     * @param last  last
     * @return new range {@code [first-last]}
     */
    @NotNull
    static Range Of(char first, char last) {

        return new Ranger(first, last);
    }

    /**
     * @param first first
     * @param last  last
     * @return new range {@code [first-last]}
     */
    @NotNull
    static Range Of(int first, int last) {

        return new Ranger(first, last);
    }

    /**
     * @return hazırlanan düzenli ifadeyi köşeli parantezler içine alınmış şekilde döndürür {@code [regex]}
     */
    @NotNull
    String getRegex();

    /**
     * Düzenli ifadeye ekleme yapar.
     *
     * @param sequence sequence
     * @return range nesnesi
     */
    @NotNull Range add(@NotNull CharSequence sequence);

    /**
     * @return negated range for this range {@code [^regex]}
     */
    @NotNull Range negate();

    /**
     * Düzenli ifadeye ekleme yapar.
     *
     * @param regex regex
     * @return range nesnesi
     */
    default @NotNull Range add(@NotNull Regex regex) {

        return add(regex.getRegex());
    }

    /**
     * Düzenli ifadeye ekleme yapar.
     *
     * @param range range
     * @return range nesnesi
     */
    default @NotNull Range add(@NotNull Range range) {

        return add(range.getRegex());
    }

    /**
     * Returns a regex as a range for this range which is subtracted from the given range.
     * {@code [[this.regex]&&[^range.regex]]}
     *
     * @param range the range to subtract from this range
     * @return the regex for this range which is subtracted from the given range
     */
    default @NotNull Regex except(@NotNull Range range) {

        return Regex.of(getRegex())
                .add("&&")
                .add(range.negate().getRegex())
                .asRange();
    }

    /**
     * Returns a regex as a range for this range which is subtracted from the given sequence.
     *
     * @param sequence the sequence to subtract from this range
     * @return the regex for this range which is subtracted from the given sequence
     */
    default @NotNull Regex except(@NotNull String sequence) {

        return Regex.of(getRegex())
                .add("&&")
                .add(Regex.of(sequence).asRangeNegated())
                .asRange();
    }

    /**
     * Returns a regex as a range for this range which is intersected with the given range.
     * {@code [[this.regex]&&[range.regex]]}
     *
     * @param range the range to intersect with this range
     * @return the regex for this range which is intersected with the given range
     */
    default @NotNull Regex intersect(@NotNull Range range) {

        return Regex.of(getRegex())
                .add("&&")
                .add(range.getRegex())
                .asRange();
    }

    /**
     * Returns a regex as a range for this range which is intersected with the given sequence.
     *
     * @param sequence the sequence to intersect with this range
     * @return the regex for this range which is intersected with the given sequence
     */
    default @NotNull Regex intersect(@NotNull String sequence) {

        return Regex.of(getRegex())
                .add("&&")
                .add(Regex.of(sequence).asRange())
                .asRange();
    }

}