package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public class Ranger implements Range {
	
	private final String  text;
	private final boolean negated;
	
	public Ranger(@NotNull String sequence) {
		
		text    = sequence;
		negated = false;
	}
	
	public <T extends Text> Ranger(@NotNull T sequence) {
		
		text    = sequence.getText();
		negated = false;
	}
	
	public Ranger(@NotNull String sequence, boolean negated) {
		
		text         = sequence;
		this.negated = negated;
	}
	
	public <T extends Text> Ranger(@NotNull T sequence, boolean negated) {
		
		text         = sequence.getText();
		this.negated = negated;
	}
	
	@Override
	public @NotNull Range with(@NotNull Range range) {
		//String.format("%s%s%s", negated ? "^" : "", text, range.getRange());
		return new Ranger(text + range.getRange(), negated);
	}
	
	@Override
	public @NotNull String getRange() {
		
		return text;
	}
	
	@Override
	public @NotNull Range negate() {
		
		return new Ranger(text, !negated);
	}
	
	@Override
	public @NotNull Range except(@NotNull Range range) {
		
		return new Ranger(getText() + "&&" + range.negate());
	}
	
	@Override
	public @NotNull Range except(@NotNull String sequence) {
		
		return new Ranger(getText() + "&&" + new Ranger(sequence).negate());
	}
	
	@Override
	public @NotNull Range intersect(@NotNull Range range) {
		
		return new Ranger(getText() + "&&" + range);
	}
	
	@Override
	public @NotNull Range intersect(@NotNull String sequence) {
		
		return new Ranger(getText() + "&&" + new Ranger(sequence));
	}
	
	@Override
	public @NotNull RegexBuilder toRegex() {
		
		return new Teddy(getText());
	}
	
	@Override
	public @NotNull String getText() {
		
		return String.format("[%s%s]", negated ? "^" : "", text);
	}
	
	@Override
	public String toString() {
		
		return getText();
	}
}
