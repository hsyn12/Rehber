package com.tr.hsyn.regex.dev.regex.quantity;


import org.jetbrains.annotations.NotNull;


public interface ZeroOrOne extends Quanta {
	
	@Override
	default @NotNull String getText() {
		
		return ZERO_OR_ONE;
	}
}
