package com.tr.hsyn.regex.dev.regex.character.cast;


import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


public class Expression implements Character {
	
	protected final String  regex;
	protected final boolean non;
	
	public Expression(@NotNull String regex) {
		
		this.regex = regex;
		non        = false;
	}
	
	public Expression(@NotNull String regex, boolean isNon) {
		
		this.regex = regex;
		non        = isNon;
	}
	
	@Override
	public @NotNull String getText() {
		
		return non ? String.format("[^%s]", regex) : regex;
	}
	
	@Override
	public @NotNull Character non() {
		
		return new Expression(regex, !non);
	}
}
