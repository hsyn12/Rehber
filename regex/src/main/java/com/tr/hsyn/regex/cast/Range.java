package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Ranger;

import org.jetbrains.annotations.NotNull;


/**
 * Range constants and builder.
 */
public interface Range extends Regex {
	
	//! [a-zA-Z0-9_] are called word characters. All other characters are considered non-word characters
	
	String TURKISH_CHARS       = "[" + CharacterSet.TURKISH_CHARS + "]";
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
	 * Negates the range.<br>
	 * {@code [^regex]}
	 *
	 * @return New negated range
	 */
	default @NotNull Range negate() {
		
		var regex = getText();
		return new Ranger("^" + regex.substring(regex.indexOf("[") + 1, regex.lastIndexOf("]")));
	}
	
	/**
	 * Convert the range which is subtracted from the given range.<br>
	 * {@code [[this.regex]&&[^range.regex]]}
	 *
	 * @param range the range to subtract from this range
	 * @return the regex for this range which is subtracted from the given range
	 */
	default @NotNull Range except(@NotNull Range range) {
		
		return with("&&" + range.negate().getText());
	}
	
	/**
	 * Convert the range which is subtracted from the given sequence.<br>
	 * {@code [[this.regex]&&[^sequence]]}
	 *
	 * @param sequence the sequence to subtract from this range
	 * @return the range which is subtracted from the given sequence
	 */
	default @NotNull Range except(@NotNull String sequence) {
		
		return with("&&" + Ranger.rangeOf(sequence).negate().getText());
	}
	
	/**
	 * Returns a regex as a range for this range which is intersected with the given range.<br>
	 * {@code [[this.regex]&&[range.regex]]}
	 *
	 * @param range the range to intersect with this range
	 * @return the regex for this range which is intersected with the given range
	 */
	default @NotNull Range intersect(@NotNull Range range) {
		
		return with("&&" + range.getText());
	}
	
	/**
	 * Convert the range which is intersected with the given sequence.<br>
	 * {@code [[this.regex]&&[sequence]]}
	 *
	 * @param sequence the sequence to intersect with this range
	 * @return the range which is intersected with the given sequence
	 */
	default @NotNull Range intersect(@NotNull String sequence) {
		
		return with("&&" + Ranger.rangeOf(sequence).negate().getText());
	}
	
	@Override
	@NotNull <T extends Text> Range with(@NotNull T regularExpression);
	
	@Override
	@NotNull Range with(@NotNull String expression);
	
	@Override
	@NotNull Range with(int i);
	
	@Override
	@NotNull Range with(char c);
}