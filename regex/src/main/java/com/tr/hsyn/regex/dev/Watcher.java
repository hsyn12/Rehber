package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public class Watcher implements Look {
	
	private final String  text;
	private final boolean ahead;
	private final boolean negative;
	
	public Watcher(@NotNull String text) {
		
		this.text = text;
		ahead     = true;
		negative  = false;
	}
	
	public <T extends Text> Watcher(@NotNull T text) {
		
		this(text.getText());
	}
	
	public Watcher(@NotNull String text, boolean ahead, boolean negative) {
		
		this.text     = text;
		this.ahead    = ahead;
		this.negative = negative;
	}
	
	public <T extends Text> Watcher(@NotNull T text, boolean ahead, boolean negative) {
		
		this(text.getText(), ahead, negative);
	}
	
	@Override
	public @NotNull String getText() {
		
		return String.format("(?%s%s)", ahead ? (negative ? "!" : "=") : negative ? "<!" : "<=", text);
	}
	
	@Override
	public @NotNull Look positive() {
		
		return new Watcher(text, ahead, false);
	}
	
	@Override
	public @NotNull Look negative() {
		
		return new Watcher(text, ahead, true);
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return getText();
	}
}
