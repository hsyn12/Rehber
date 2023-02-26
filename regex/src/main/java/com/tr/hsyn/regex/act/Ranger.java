package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class Ranger extends Teddy implements Range {
	
	public Ranger(char first, char last) {
		
		super(String.format("[%c-%c]", first, last));
	}
	
	public Ranger(int first, int last) {
		
		super(String.format(Locale.getDefault(), "[%d-%d]", first, last));
	}
	
	public Ranger(@NotNull String sequence) {
		
		super(String.format("[%s]", sequence));
	}
	
	public <T extends Text> Ranger(@NotNull T sequence) {
		
		super(String.format("[%s]", sequence.getText()));
	}
	
	@NotNull
	public static Range rangeOf(@NotNull String expression) {
		
		return new Ranger(expression);
	}
	
	@NotNull
	public static <T extends Text> Range rangeOf(@NotNull T expression) {
		
		return new Ranger(expression);
	}
	
	@NotNull
	public static Range rangeOf(char first, char last) {
		
		return new Ranger(first, last);
	}
	
	@NotNull
	public static Range rangeOf(int first, int last) {
		
		return new Ranger(first, last);
	}
	
	@Override
	public @NotNull Range with(@NotNull String regex) {
		
		return rangeOf(text + regex);
	}
	
	@Override
	public @NotNull Range with(int i) {
		
		return rangeOf(text + i);
	}
	
	@Override
	public @NotNull Range with(char c) {
		
		return rangeOf(text + c);
	}
	
	@Override
	public @NotNull <T extends Text> Range with(@NotNull T text) {
		
		return rangeOf(this.text + text);
	}
}
