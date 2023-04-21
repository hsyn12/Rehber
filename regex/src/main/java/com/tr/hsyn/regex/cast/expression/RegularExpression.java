package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.RegexEditor;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public interface RegularExpression extends RegexEditor {
	
	/**
	 * Appends the given regular expression to the current regex builder.
	 *
	 * @param expression the regular expression to be appended to the current regex builder. Must not be null.
	 * @return the current regex builder with the given regular expression appended to it.
	 */
	@NotNull
	RegexBuilder with(@NotNull String expression);
	
	/**
	 * Appends the given regular expression to the current regex builder.
	 *
	 * @param expression the regular expression to be appended to the current regex builder. Must not be null.
	 * @return the current regex builder with the given regular expression appended to it.
	 */
	@Override
	<T extends Text> @NotNull RegexBuilder with(@NotNull T expression);
	
	/**
	 * Appends the given integer to the current regex builder.
	 *
	 * @param i the integer to be appended to the current regex builder.
	 * @return the current regex builder with the given integer appended to it.
	 */
	@NotNull
	RegexBuilder with(int i);
	
	/**
	 * Appends the given character to the current regex builder.
	 *
	 * @param c the character to be appended to the current regex builder.
	 * @return the current regex builder with the given character appended to it.
	 */
	@NotNull
	RegexBuilder with(char c);
}
