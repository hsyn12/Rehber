package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.WhiteSpace;

import org.jetbrains.annotations.NotNull;


public class RegexWhiteSpace implements WhiteSpace {
	
	private final String regex;
	
	public RegexWhiteSpace(String regex) {this.regex = regex;}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return Regex.NON_WHITE_SPACE;
	}
}
