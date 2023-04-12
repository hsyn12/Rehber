package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Letter;

import org.jetbrains.annotations.NotNull;


public class RegexLetter implements Letter {
	
	private final String regex;
	
	public RegexLetter(String regex) {this.regex = regex;}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return Regex.NON_LETTER;
	}
}
