package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public class SimpleText implements Text {
	
	private final String expression;
	
	public SimpleText(@NotNull String expression) {
		
		this.expression = expression;
	}
	
	public <T extends Text> SimpleText(@NotNull T expression) {
		
		this.expression = expression.getText();
	}
	
	@Override
	public @NotNull String getText() {
		
		return expression;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return getText();
	}
}
