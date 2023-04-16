package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


@SuppressWarnings("DefaultLocale")
public class Teddy implements RegexBuilder {
	
	protected final StringBuilder text = new StringBuilder();
	
	public Teddy() {
		
	}
	
	public Teddy(@NotNull String text) {
		
		this.text.append(text);
	}
	
	public <T extends Text> Teddy(@NotNull T expression) {
		
		text.append(expression.getText());
	}
	
	@NotNull
	public static RegexBuilder regex() {
		
		return new Teddy();
	}
	
	@NotNull
	public static RegexBuilder regex(@NotNull String text) {
		
		return new Teddy(text);
	}
	
	@NotNull
	public static <T extends Text> RegexBuilder regex(@NotNull T text) {
		
		return new Teddy(text);
	}
	
	@Override
	public final @NotNull String getText() {
		
		return text.toString();
	}
	
	@Override
	public String toString() {
		
		return getText();
	}
	
	@Override
	@NotNull
	public RegexBuilder with(@NotNull String regex) {
		
		text.append(regex);
		return this;
	}
	
	@Override
	public @NotNull RegexBuilder with(int i) {
		
		text.append(i);
		return this;
	}
	
	@Override
	public @NotNull RegexBuilder with(char c) {
		
		text.append(c);
		return this;
	}
	
	@Override
	public @NotNull Range toRange() {
		
		return new Ranger(text.toString());
	}
	
	@Override
	@NotNull
	public <T extends Text> RegexBuilder with(@NotNull T text) {
		
		this.text.append(text.getText());
		return this;
	}
	
	@Override
	public @NotNull <T extends Text> RegexBuilder range(@NotNull T regularExpression) {
		
		text.append("[").append(regularExpression.getText()).append("]");
		return this;
	}
	
	@Override
	public @NotNull RegexBuilder range(@NotNull String regularExpression) {
		
		text.append("[").append(regularExpression).append("]");
		return this;
	}
	
	@Override
	public @NotNull RegexBuilder rangeNumbers() {
		
		text.append("[0-9]");
		return this;
	}
	
	@Override
	public @NotNull RegexBuilder rangeLetters() {
		
		text.append("[a-zA-Z]");
		return this;
	}
}
