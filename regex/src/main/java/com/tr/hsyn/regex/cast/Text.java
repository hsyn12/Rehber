package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.SimpleText;

import org.jetbrains.annotations.NotNull;


/**
 * Text
 */
public interface Text {
	
	@NotNull
	static Text of(@NotNull String expression) {
		
		return new SimpleText(expression);
	}
	
	/**
	 * @return Text
	 */
	@NotNull
	String getText();
	
	default <T extends Text> @NotNull Text with(@NotNull T expression) {
		
		return of(getText() + expression.getText());
	}
}
