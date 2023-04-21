package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


public interface ControlExpression extends RegularExpression {
	
	/**
	 * Adds a control meta character.<br>
	 * {@code \p{C}}
	 *
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder control() {
		
		return with(Character.CONTROL);
	}
	
	/**
	 * Adds a control meta character.<br>
	 * {@code \p{C}}
	 *
	 * @param quanta Çokluk
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder control(@NotNull Quanta quanta) {
		
		return with(Character.CONTROL + quanta);
	}
	
	/**
	 * Adds a non-control meta character.<br>
	 * {@code \P{C}}
	 *
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder nonControl() {
		
		return with(Character.NON_CONTROL);
	}
	
	/**
	 * Adds a non-control meta character.<br>
	 * {@code \P{C}}
	 *
	 * @param quanta Çokluk
	 * @return Kurulan {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder nonControl(@NotNull Quanta quanta) {
		
		return with(Character.NON_CONTROL + quanta);
	}
}
