package com.tr.hsyn.regex.dev.regex.cast;


import com.tr.hsyn.regex.dev.regex.quantity.Quanta;

import org.jetbrains.annotations.NotNull;


public class RegexQuantity implements Quanta {
	
	private final String regex;
	
	public RegexQuantity(@NotNull String regex) {this.regex = regex;}
	
	@Override
	public @NotNull String getText() {
		
		return regex;
	}
}
