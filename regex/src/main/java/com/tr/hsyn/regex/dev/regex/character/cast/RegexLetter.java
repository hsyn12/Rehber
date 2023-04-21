package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Letter;

import org.jetbrains.annotations.NotNull;


public class RegexLetter extends Expression implements Letter {
	
	public RegexLetter(String regex) {
		
		super(regex, false);
	}
	
	public RegexLetter(String regex, boolean isNon) {
		
		super(regex, isNon);
	}
	
	@Override
	public @NotNull Character non() {
		
		return non ? Regex.LETTER : Regex.NON_LETTER;
	}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
}
