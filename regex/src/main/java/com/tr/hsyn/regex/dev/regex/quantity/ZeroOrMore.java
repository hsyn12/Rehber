package com.tr.hsyn.regex.dev.regex.quantity;


import org.jetbrains.annotations.NotNull;


public interface ZeroOrMore extends Quanta {
	
	@Override
	default @NotNull String getText() {
		
		return ZERO_OR_MORE;
	}
}
