package com.tr.hsyn.string;


import org.jetbrains.annotations.NotNull;


public class Tester {
	
	private final String text;
	
	public Tester(@NotNull String text) {
		
		this.text = text;
	}
	
	public boolean is(@NotNull String regex) {
		
		return text.matches(regex);
	}
	
	public boolean isNot(@NotNull String c) {
		
		return !is(c);
	}
	
}
