package com.tr.hsyn.regex.act;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Regex;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


@SuppressWarnings("DefaultLocale")
public class Teddy implements Regex {
	
	protected final String text;
	
	public Teddy(@NotNull String text) {
		
		this.text = text;
	}
	
	public <T extends Text> Teddy(@NotNull T expression) {
		
		text = expression.getText();
	}
	
	@NotNull
	public static Regex regex() {
		
		return new Teddy("");
	}
	
	@NotNull
	public static Regex regex(@NotNull String text) {
		
		return new Teddy(text);
	}
	
	@NotNull
	public static <T extends Text> Regex regex(@NotNull T text) {
		
		return new Teddy(text);
	}
	
	@Override
	public final @NotNull String getText() {
		
		return text;
	}
	
	@Override
	public String toString() {
		
		return text;
	}
	
	@NotNull
	public Regex with(@NotNull String regex) {
		
		return regex(text + regex);
	}
	
	@Override
	public @NotNull Regex with(int i) {
		
		return regex(this.text + i);
	}
	
	@Override
	public @NotNull Regex with(char c) {
		
		return regex(this.text + c);
	}
	
	@Override
	public @NotNull Range toRange() {
		
		return Ranger.rangeOf(getText());
	}
	
	@Override
	public @NotNull <T extends Text> Regex range(@NotNull T expression) {
		
		return with(Ranger.rangeOf(expression).getText());
	}
	
	@Override
	public @NotNull Regex range(@NotNull String expression) {
		
		return with(Ranger.rangeOf(expression).getText());
	}
	
	@Override
	public @NotNull Regex toGroup() {
		
		return regex(String.format("(%s)", text));
	}
	
	@Override
	public @NotNull Regex toGroup(@NotNull String groupName) {
		
		return regex(String.format("(?<%s>%s)", groupName, text));
	}
	
	@NotNull
	public <T extends Text> Regex with(@NotNull T text) {
		
		return regex(this.text + text.getText());
	}
}
