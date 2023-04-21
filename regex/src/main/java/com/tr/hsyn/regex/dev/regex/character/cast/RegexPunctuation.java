package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Punctuation;

import org.jetbrains.annotations.NotNull;


public class RegexPunctuation extends Expression implements Punctuation {
	
	public RegexPunctuation(String regex) {
		
		super(regex, false);
	}
	
	public RegexPunctuation(String regex, boolean isNon) {
		
		super(regex, isNon);
	}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return non ? Regex.PUNCTUATION : Regex.NON_PUNCTUATION;
	}
}
