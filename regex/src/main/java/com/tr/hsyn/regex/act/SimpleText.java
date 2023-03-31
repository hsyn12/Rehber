package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public class SimpleText implements Text {
	
	private final String text;
	
	public SimpleText(@NotNull String text) {
		
		this.text = text;
	}
	
	@Override
	public @NotNull String getText() {
		
		return text;
	}
}
