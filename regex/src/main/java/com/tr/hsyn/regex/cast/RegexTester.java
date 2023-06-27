package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


public interface RegexTester extends RegexMatcher {
	
	/**
	 * Tests created regex against the given string (all string matching).<br><br>
	 *
	 * <pre>
	 * String str = "12 Nisan 1981";
	 * var regex = Nina.like()
	 * 	.boundary()
	 * 	.withRange("1-2")
	 * 	.withRange("0-9")
	 * 	.space(Quantifier.ONE_OR_MORE)
	 * 	.any(Quantifier.ONE_OR_MORE);
	 *
	 * pl("Regex   : %s", regex);               // Regex   : \b[1-2][0-9]\p{Z}+.+
	 * pl("Result  : %s", regex.test(str));     // Result  : true
	 * pl("Result  : %s", regex.matchesOf(str));// Result  : [12 Nisan 1981]
	 * </pre>
	 *
	 * @param text the text to test
	 * @return true if the regex matches the text
	 */
	default boolean test(@NotNull String text) {
		
		return text.matches(getText());
	}
	
	/**
	 * Tests created regex against the given string (all string matching).<br><br>
	 *
	 * <pre>
	 * String str = "12 Nisan 1981";
	 * var regex = Nina.like()
	 * 	.boundary()
	 * 	.withRange("1-2")
	 * 	.withRange("0-9")
	 * 	.space(Quantifier.ONE_OR_MORE)
	 * 	.any(Quantifier.ONE_OR_MORE);
	 *
	 * pl("Regex   : %s", regex);               // Regex   : \b[1-2][0-9]\p{Z}+.+
	 * pl("Result  : %s", regex.test(str, <strong><u>3</u></strong>));  // Result  : <strong><u>false</u></strong>
	 * pl("Result  : %s", regex.matchesOf(str));// Result  : [12 Nisan 1981]
	 * </pre>
	 *
	 * @param text       the text to test
	 * @param beginIndex the index to start the test
	 * @return if the regex matches the text {@code true}, otherwise {@code false}
	 */
	default boolean test(@NotNull CharSequence text, int beginIndex) {
		
		return text.toString().substring(beginIndex).matches(getText());
	}
	
	/**
	 * Tests created regular expression against the given text for containing (partial match).<br><br>
	 *
	 * <pre>
	 * String str = "12 Nisan 1981";
	 * var regex = Nina.like()
	 * 	.boundary()
	 * 	.withRange("1-2")
	 * 	.withRange("0-9")
	 * 	.space(Quantifier.ONE_OR_MORE)
	 * 	.any(Quantifier.ONE_OR_MORE);
	 *
	 * pl("Regex   : %s", regex);               // Regex   : \b[1-2][0-9]\p{Z}+.+
	 * pl("Result  : %s", regex.existIn(str));  // Result  : <strong><u>true</u></strong>
	 * pl("Result  : %s", regex.matchesOf(str));// Result  : [12 Nisan 1981]
	 * </pre>
	 *
	 * @param text the text to test
	 * @return true if the text has the regular expression, otherwise {@code false}
	 */
	default boolean existIn(@NotNull String text) {
		
		return createMatcher(text).find();
	}
	
	
}
