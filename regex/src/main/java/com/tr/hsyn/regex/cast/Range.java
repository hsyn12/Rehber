package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Ranger;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


/**
 * Range constants and builder.
 */
public interface Range extends Text {
	
	//! [a-zA-Z0-9_] are called word characters. All other characters are considered non-word characters
	
	/**
	 * Returns a range for letters.<br><br>
	 *
	 * @return New <code>Range</code> with letters
	 * @see Character#LETTER
	 */
	static @NotNull Range letters() {
		
		return new Ranger(Character.LETTER);
	}
	
	/**
	 * Returns a range for digits.<br><br>
	 *
	 * @return New <code>Range</code> with digits
	 * @see Character#DIGIT
	 */
	static @NotNull Range digits() {
		
		return new Ranger(Character.DIGIT);
	}
	
	/**
	 * Returns a range for the given expression.<br><br>
	 *
	 * @param expression The expression to be converted
	 * @return New <code>Range</code> with the given expression
	 */
	static @NotNull Range of(@NotNull String expression, Object... args) {
		
		return new Ranger(String.format(expression, args));
	}
	
	/**
	 * Verilen ifadeyi olumsuz bir aralık olarak döndürür.<br><br>
	 *
	 * <pre>var range = Range.noneOf("123"); // [^123]</pre>
	 *
	 * @param expression İfade
	 * @return Yeni bir <code>Range</code> nesnesi
	 */
	static @NotNull Range noneOf(@NotNull String expression, Object... args) {
		
		return new Ranger(String.format(expression, args), true);
	}
	
	
	/**
	 * Returns a range for the given {@link com.tr.hsyn.regex.dev.regex.character.Character}.<br><br>
	 *
	 * @param regexChar The <code>com.tr.hsyn.regex.dev.regex.character.Character</code> for the range
	 * @return New <code>Range</code> with the given expression
	 */
	static @NotNull Range of(@NotNull Character regexChar) {
		
		return new Ranger(regexChar);
	}
	
	/**
	 * Returns a negated range for the given {@link Character}.<br><br>
	 *
	 * <pre>var range = Range.noneOf(Character.DIGIT);// [^0-9]</pre>
	 *
	 * @param regexChar The <code>Character</code> for the range
	 * @return New <code>Range</code> with the given expression
	 */
	static @NotNull Range noneOf(@NotNull Character regexChar) {
		
		return new Ranger(regexChar, true);
	}
	
	/**
	 * Returns a range for the given {@link Text}.<br><br>
	 *
	 * @param expression The {@link Text} for the range
	 * @param <T>        The type of the expression
	 * @return New <code>Range</code> with the given expression
	 */
	static <T extends Text> @NotNull Range of(@NotNull T expression) {
		
		return new Ranger(expression);
	}
	
	/**
	 * Returns a negated range for the given {@link Text}.<br><br>
	 *
	 * @param expression The {@link Text} for the range
	 * @param <T>        The type of the expression
	 * @return New <code>Range</code> with the given expression
	 */
	static <T extends Text> @NotNull Range noneOf(@NotNull T expression) {
		
		return new Ranger(expression, true);
	}
	
	/**
	 * Returns a range for the given range.<br><br>
	 *
	 * @param range The range for the range
	 * @return New <code>Range</code> with the given range
	 */
	@NotNull
	Range with(@NotNull Range range);
	
	/**
	 * Returns the non brackets range as a string.<br><br>
	 *
	 * <pre>var range = Range.of("123456789").getRange(); // "123456789"</pre>
	 *
	 * @return New <code>String</code> with the range
	 */
	@NotNull
	String getRange();
	
	/**
	 * Negates the range.<br><br>
	 *
	 * <pre>
	 * var regex = {@link Range#digits()};
	 * pl("Regex : %s", regex); // Regex : [0-9]
	 * pl("Regex : %s", regex.negate()); // Regex : [^0-9]</pre><br>
	 *
	 * @return New {@link Range} with negated {@code [^regex]}
	 */
	@NotNull
	Range negate();
	
	/**
	 * Convert the range which is subtracted from the given range.<br>
	 * {@code [[this.regex]&&[^range.regex]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeNumbers().except(Nina.range("3-5")));
	 * pl("Regex  : %s", regex); // Regex  : [[0-9]&&[^3-5]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param range the range to subtract from this range
	 * @return the regex for this range which is subtracted from the given range
	 */
	@NotNull
	Range except(@NotNull Range range);
	
	/**
	 * Convert the range which is subtracted from the given sequence.<br>
	 * {@code [[this.regex]&&[^sequence]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.rangeNumbers().except("345"));
	 * pl("Regex  : %s", regex); // Regex  : [[0-9]&&[^345]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param sequence the sequence to subtract from this range
	 * @return the range which is subtracted from the given sequence
	 */
	@NotNull
	Range except(@NotNull String sequence);
	
	/**
	 * Returns a regex as a range for this range which is intersected with the given range.<br>
	 * {@code [[this.regex]&&[range.regex]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.range("345").intersect(Nina.rangeNumbers()));
	 * pl("Regex  : %s", regex); // Regex  : [[345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [3, 4, 5]
	 * // -----------------------------------------------------------------
	 * regex = Nina.regex(Nina.range("345").<strong><u>negate</u></strong>().intersect(Nina.rangeNumbers()));
	 * pl("Regex  : %s", regex); // Regex  : [[^345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [1, 2, 6, 7, 8, 9]</pre><br>
	 *
	 * @param range the range to intersect with this range
	 * @return the regex for this range which is intersected with the given range
	 */
	@NotNull
	Range intersect(@NotNull Range range);
	
	/**
	 * Convert the range which is intersected with the given sequence.<br>
	 * {@code [[this.regex]&&[sequence]]}<br><br>
	 *
	 * <pre>
	 * var str   = "123456789";
	 * var regex = Nina.regex(Nina.range("345").intersect("0-9")));
	 * pl("Regex  : %s", regex); // Regex  : [[345]&&[0-9]]
	 * pl("Result : %s", regex.matchesOf(str)); // Result : [3, 4, 5]</pre><br>
	 *
	 * @param sequence the sequence to intersect with this range
	 * @return the range which is intersected with the given sequence
	 */
	@NotNull
	Range intersect(@NotNull String sequence);
	
	/**
	 * Returns a regex for this range.<br>
	 *
	 * @return The regex for this range
	 */
	@NotNull
	RegexBuilder toRegex();
	
}