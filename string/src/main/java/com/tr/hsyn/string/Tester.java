package com.tr.hsyn.string;


import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


public class Tester {
	
	private final String text;
	
	public Tester(@NotNull String text) {
		
		this.text = text;
	}
	
	public boolean is(@NotNull Character c) {
		
		return c.all(text);
	}
	
	public boolean isNot(@NotNull Character c) {
		
		return !is(c);
	}
	
}
