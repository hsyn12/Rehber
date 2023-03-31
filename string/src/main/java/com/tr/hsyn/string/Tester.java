package com.tr.hsyn.string;


import com.tr.hsyn.regex.dev.RegexChar;

import org.jetbrains.annotations.NotNull;


public class Tester {
	
	private final String text;
	
	public Tester(@NotNull String text) {
		
		this.text = text;
	}
	
	public boolean is(@NotNull RegexChar c) {
		
		return c.all(text);
	}
	
	public boolean isNot(@NotNull RegexChar c) {
		
		return !is(c);
	}
	
	public boolean containsAny(@NotNull RegexChar regexChar) {
		
		return regexChar.any(text);
	}
	
}
