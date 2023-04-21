package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Digit;

import org.jetbrains.annotations.NotNull;


public class RegexDigit extends Expression implements Digit {
	
	public RegexDigit(@NotNull String regex) {
		
		super(regex);
	}
	
	public RegexDigit(@NotNull String regex, boolean isNon) {
		
		super(regex, isNon);
	}
	
	@Override
	public @NotNull Character non() {
		
		return non ? Regex.DIGIT : Regex.NON_DIGIT;
	}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
}
