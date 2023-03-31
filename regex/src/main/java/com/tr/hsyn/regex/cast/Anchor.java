package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;


public enum Anchor {
	
	//@off
	//?  Boundary Matcher                         Meaning
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \b            |  Word boundary; position between a word and a non-word character                                                                                               |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \B            |  Non-word boundary; it compliments \b and asserts true wherever \b asserts false                                                                               |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|         ^            |  Line-start anchor, which matches the start of a line                                                                                                          |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|         $            |  Line-end anchor, which matches just before the optional line break at the end of a line                                                                       |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \A            |  Permanent start of input; in a multiline input using MULTILINE mode, \A matches only at the very beginning, while ^ is matched at every line start position   |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \z            |  Permanent end of input; in a multiline input using MULTILINE mode, \z matches only at the very end while $ is matched at every line end position              |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \Z            |  Similar to \z with the only difference being that it matches just before the optional line break at the very end of the input.                                |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|        \G            |  End of the previous match; we will discuss it in advanced sections of the book in the next chapters.                                                          |
	//?|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
	//@on
	
	/**
	 * Word boundary; position between a word and a non-word character
	 */
	BOUNDARY("\\b"),
	/**
	 * Non-word boundary; it compliments <code>\b</code> and asserts true wherever <code>\b</code> asserts false
	 */
	NON_BOUNDARY("\\B"),
	/**
	 * Line-start anchor, which matches the start of a line
	 */
	START_OF_LINE("^"),
	/**
	 * Line-end anchor, which matches just before the optional line break at the end of a line
	 */
	END_OF_LINE("$"),
	/**
	 * Permanent start of input; in a multiline input using MULTILINE mode, <code>\A</code> matches only at the very beginning, while <code>^</code> is matched at every line start position
	 */
	START_OF_INPUT("\\A"),
	/**
	 * Permanent end of input; in a multiline input using MULTILINE mode, <code>\z</code> matches only at the very end while <code>$</code> is matched at every line end position
	 */
	END_OF_INPUT("\\z"),
	/**
	 * Similar to <code>\z</code> with the only difference being that it matches just before the optional line break at the very end of the input.
	 */
	END_OF_INPUT_MULTILINE("\\Z"),
	/**
	 * End of the previous match
	 */
	PREVIOUS_MATCH("\\G");
	
	private final String regex;
	
	Anchor(@NotNull String expression) {
		
		regex = expression;
	}
	
	@NotNull
	public String getRegex() {
		
		return regex;
	}
	
}
