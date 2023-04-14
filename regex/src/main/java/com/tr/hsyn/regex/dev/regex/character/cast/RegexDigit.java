package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Digit;

import org.jetbrains.annotations.NotNull;


public class RegexDigit implements Digit {
	
	private final String regex;
	
	public RegexDigit(String regex) {this.regex = regex;}
	
	@Override
	public @NotNull Digit non() {
		
		return Character.NON_DIGIT;
	}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
}
