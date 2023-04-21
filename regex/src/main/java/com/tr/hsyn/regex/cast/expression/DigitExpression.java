package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Character;

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
		
		return with(Regex.NON_DIGIT);
	}
	
	/**
	 * Sayısal olmayan <em>bir yada daha fazla</em> karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonDigits() {
		
		return nonDigit().oneOrMore();
	}
}
