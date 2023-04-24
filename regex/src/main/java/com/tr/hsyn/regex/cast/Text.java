package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.SimpleText;

import org.jetbrains.annotations.NotNull;


/**
 * Text
 */
public interface Text {
	
	@NotNull
	static Text of(@NotNull String expression, Object... args) {
		
		return new SimpleText(String.format(expression, args));
	}
	
	@NotNull
	static <T extends Text> Text of(@NotNull T expression) {
		
		return new SimpleText(expression);
	}
	
	/**
	 * Returns the text associated with this object.
	 *
	 * @return a non-null String representing the text associated with this object.
	 */
	@NotNull
	String getText();
	
	/**
	 * Concatenates the text of the current object with the text of the provided expression object.
	 *
	 * @param expression the expression object whose text will be concatenated with the current object's text
	 * @return a new <code>Text</code> object with the concatenated text
	 */
	default <T extends Text> @NotNull Text with(@NotNull T expression) {
		
		return of(getText() + expression.getText());
	}
}
