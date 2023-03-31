package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Regex;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public class Ranger implements Range {
	
	private final String text;
	
	public Ranger(@NotNull String sequence) {
		
		text = String.format("[%s]", sequence);
	}
	
	public <T extends Text> Ranger(@NotNull T sequence) {
		
		text = String.format("[%s]", sequence.getText());
	}
	
	@Override
	public @NotNull Range negate() {
		
		var regex = getText();
		return new Ranger("^" + regex.substring(regex.indexOf("[") + 1, regex.lastIndexOf("]")));
	}
	
	@Override
	public @NotNull Range except(@NotNull Range range) {
		
		return new Ranger(getText() + "&&" + range.negate().getText());
	}
	
	@Override
	public @NotNull Range except(@NotNull String sequence) {
		
		return new Ranger(getText() + "&&" + new Ranger(sequence).negate().getText());
	}
	
	@Override
	public @NotNull Range intersect(@NotNull Range range) {
		
		return new Ranger(getText() + "&&" + range.getText());
	}
	
	@Override
	public @NotNull Range intersect(@NotNull String sequence) {
		
		return new Ranger(getText() + "&&" + new Ranger(sequence).getText());
	}
	
	@Override
	public @NotNull Regex toRegex() {
		
		return new Teddy(text);
	}
	
	@Override
	public @NotNull String getText() {
		
		return text;
	}
	
	@Override
	public String toString() {
		
		return getText();
	}
}
