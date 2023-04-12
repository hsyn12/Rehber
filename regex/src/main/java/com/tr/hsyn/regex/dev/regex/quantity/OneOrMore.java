package com.tr.hsyn.regex.dev.regex.quantity;


import org.jetbrains.annotations.NotNull;


public interface OneOrMore extends Quanta {
	
	@Override
	default @NotNull String getText() {
		
		return ONE_OR_MORE;
	}
}
