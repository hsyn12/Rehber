package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.WhiteSpace;

import org.jetbrains.annotations.NotNull;


public class RegexWhiteSpace extends Expression implements WhiteSpace {
	
	public RegexWhiteSpace(@NotNull String regex) {
		
		super(regex);
	}
	
	public RegexWhiteSpace(@NotNull String regex, boolean isNon) {
		
		super(regex, isNon);
	}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return non ? Character.WHITE_SPACE : Character.NON_WHITE_SPACE;
	}
}
