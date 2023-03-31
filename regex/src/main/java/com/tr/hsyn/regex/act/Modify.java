package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Modifier;

import org.jetbrains.annotations.NotNull;


public class Modify implements Modifier {
	
	protected final StringBuilder regex = new StringBuilder();
	
	public Modify() {}
	
	public Modify(boolean disable) {
		
		if (disable) regex.append("-");
	}
	
	@Override
	public String toString() {
		
		return getText();
	}
	
	@Override
	public @NotNull String getText() {
		
		return "(?" + regex + ")";
	}
	
	private Modifier add(CharSequence sequence) {
		
		regex.append(sequence);
		return this;
	}
	
	@Override
	public @NotNull Modifier ignoreCase() {
		
		return add(IGNORE_CASE);
	}
	
	@Override
	public @NotNull Modifier dotAll() {
		
		return add(DOTALL);
	}
	
	@Override
	public @NotNull Modifier multiline() {
		
		return add(MULTILINE);
	}
	
	@Override
	public @NotNull Modifier unicodeCase() {
		
		return add(UNICODE_CASE);
	}
	
	@Override
	public @NotNull Modifier unicodeCharacter() {
		
		return add(UNICODE_CHARACTER);
	}
	
	@Override
	public @NotNull Modifier unixLines() {
		
		return add(UNIX_LINES);
	}
	
	@Override
	public @NotNull Modifier extended() {
		
		return add(EXTENDED);
	}
	
	@Override
	public @NotNull CharSequence getModifier() {
		
		return regex.toString();
	}
	
	
}
