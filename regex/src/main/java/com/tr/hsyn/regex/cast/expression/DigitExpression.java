package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


/**
 * Sayısal karakter üzerine düzenli ifade kalıpları.
 */
public interface DigitExpression extends RegularExpression {
	
	/**
	 * Sayısal bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder digit() {
		
		return with(Character.DIGIT);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> sayısal karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder digits() {
		
		return digit().oneOrMore();
	}
	
	/**
	 * Sayısal bir karakter.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder digit(@NotNull Quanta quanta) {
		
		return with(Character.DIGIT + quanta);
	}
	
	/**
	 * Sayısal olmayan bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonDigit() {
		
		return with(Character.NON_DIGIT);
	}
	
}
