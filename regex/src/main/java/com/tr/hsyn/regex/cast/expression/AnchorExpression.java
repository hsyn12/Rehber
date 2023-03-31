package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Anchor;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


public interface AnchorExpression extends RegularExpression {
	
	/**
	 * Adds the boundary anchor meta character to the match.<br>
	 * {@code \b}
	 *
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder boundary() {
		
		return with(Anchor.BOUNDARY.getRegex());
	}
	
	default @NotNull RegexBuilder boundarys() {
		
		return with(Anchor.BOUNDARY.getRegex()).oneOrMore();
	}
	
	/**
	 * Adds the boundary anchor meta character to the match.<br>
	 * {@code \b}
	 *
	 * @param times Times
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder boundary(int times) {
		
		return with(Anchor.BOUNDARY.getRegex() + Quanta.exactly(times));
	}
	
	/**
	 * Adds an anchor.
	 *
	 * @param anchor {@link Anchor}
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder anchor(@NotNull Anchor anchor) {
		
		return with(anchor.getRegex());
	}
	
	/**
	 * {@code "^"} karakteri ekler.
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder anchorStart() {
		
		return with(Anchor.START_OF_LINE.getRegex());
	}
	
	/**
	 * {@code "$"} karakteri ekler.
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder anchorEnd() {
		
		return with(Anchor.END_OF_LINE.getRegex());
	}
}
