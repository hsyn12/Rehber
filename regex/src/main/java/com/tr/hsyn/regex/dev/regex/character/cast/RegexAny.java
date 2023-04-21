package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Any;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


public class RegexAny extends Expression implements Any {
	
	public RegexAny(@NotNull String regex, boolean isNon) {
		
		super(regex, isNon);
	}
	
	public RegexAny(@NotNull String regex) {
		
		super(regex);
	}
	
	@Override
	public @NotNull String getText() {
		
		return non ? Character.ANY : Character.NON_ANY;
	}
	
	@Override
	public @NotNull Character non() {
		
		return non ? Regex.ANY : Regex.NON_ANY;
	}
}
