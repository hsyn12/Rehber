package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Punctuation;

import org.jetbrains.annotations.NotNull;


public class RegexPunctuation implements Punctuation {
	
	private final String regex;
	
	public RegexPunctuation(String regex) {this.regex = regex;}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return Regex.NON_PUNCTUATION;
	}
}
