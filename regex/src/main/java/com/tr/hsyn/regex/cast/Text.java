package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.SimpleText;

import org.jetbrains.annotations.NotNull;


/**
 * Text
 */
public interface Text {
	
	@NotNull
	static Text of(@NotNull String text) {
		
		return new SimpleText(text);
	}
	
	/**
	 * @return Text
	 */
	@NotNull String getText();
}
