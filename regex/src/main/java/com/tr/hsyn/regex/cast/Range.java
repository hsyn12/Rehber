package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


/**
 * Range constants and builder.
 */
public interface Range extends Text {
	
	//! [a-zA-Z0-9_] are called word characters. All other characters are considered non-word characters
	
	String TURKISH_CHARS       = "[" + CharacterSet.TURKISH_CHARS + "]";
	String TURKISH_CHARS_LOWER = "[" + CharacterSet.TURKISH_CHARS_LOWER + "]";
	String TURKISH_CHARS_UPPER = "[" + CharacterSet.TURKISH_CHARS_UPPER + "]";
	/**
	 * Charactes in the range a-z or A-Z
	 */
	String ALPHA               = "[" + Character.LETTER + "]";
	/**
	 * Charactes in the range a-z or A-Z or 0-9
	 */
	String ALPHA_NUMERIC       = "[" + Character.LETTER + Character.DIGIT + "]";
	/**
	 * Character in the range A-Z
	 */
	String ALPHA_UPPER         = "[" + Character.LETTER_UPPER + "]";
	
	
	/**
	 * Negates the range.<br><br>
	 *
	 * <pre>
	 * var regex = Ranger.rangeOfNumbers();
	 * pl("Regex : %s", regex); // Regex : [0-9]
	 * pl("Regex : %s", regex.negate()); // Regex : [^0-9]</pre><br>
	 *
	 * @return New {@link Range} with negated {@code [^regex]}
	 */
	@NotNull Range negate();
	
	/**
	 * Convert the range which is subtracted from the given range.<br>
	 * {@code [[this.regex]&&[^range.regex]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeOfNumbers().except(Nina.rangeOf("3-5")));
	 * pl("Regex  : %s", regex); // Regex  : [[0-9]&&[^3-5]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param range the range to subtract from this range
	 * @return the regex for this range which is subtracted from the given range
	 */
	@NotNull Range except(@NotNull Range range);
	
	/**
	 * Convert the range which is subtracted from the given sequence.<br>
	 * {@code [[this.regex]&&[^sequence]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeOfNumbers().except("345"));
	 * pl("Regex  : %s", regex); // Regex  : [[0-9]&&[^345]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param sequence the sequence to subtract from this range
	 * @return the range which is subtracted from the given sequence
	 */
	@NotNull Range except(@NotNull String sequence);
	
	/**
	 * Returns a regex as a range for this range which is intersected with the given range.<br>
	 * {@code [[this.regex]&&[range.regex]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeOf("345").intersect(Nina.rangeOfNumbers()));
	 * pl("Regex  : %s", regex); // Regex  : [[345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [3, 4, 5]
	 * //-----------------------------------------------------------------
	 * regex = Nina.regex(Nina.rangeOf("345").<strong><u>negate</u></strong>().intersect(Nina.rangeOfNumbers()));
	 * pl("Regex  : %s", regex); // Regex  : [[^345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param range the range to intersect with this range
	 * @return the regex for this range which is intersected with the given range
	 */
	@NotNull Range intersect(@NotNull Range range);
	
	/**
	 * Convert the range which is intersected with the given sequence.<br>
	 * {@code [[this.regex]&&[sequence]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeOf("345").intersect("0-9")));
	 * pl("Regex  : %s", regex); // Regex  : [[345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [3, 4, 5]</pre><br>
	 *
	 * @param sequence the sequence to intersect with this range
	 * @return the range which is intersected with the given sequence
	 */
	@NotNull Range intersect(@NotNull String sequence);
	
	
	@NotNull Regex toRegex();
	
}