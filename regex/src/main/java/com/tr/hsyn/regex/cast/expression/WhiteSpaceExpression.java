package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


/**
 * Boşluk karakteri üzerine düzenli ifade kalıpları.
 */
public interface WhiteSpaceExpression extends RegularExpression {
	
	/**
	 * Bir boşluk.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#WHITE_SPACE
	 */
	default @NotNull RegexBuilder whiteSpace() {
		
		return with(Character.WHITE_SPACE);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> boşluk.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 * @see Character#WHITE_SPACE
	 */
	default @NotNull RegexBuilder whiteSpace(@NotNull Quanta quanta) {
		
		return with(Character.WHITE_SPACE + quanta);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> boşluk.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#WHITE_SPACE
	 */
	default @NotNull RegexBuilder whiteSpaces() {
		
		return with(Character.WHITE_SPACE).oneOrMore();
	}
	
	/**
	 * Boşluk olmayan bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	default @NotNull RegexBuilder nonWhiteSpace() {
		
		return with(Character.NON_WHITE_SPACE);
	}
	
	/**
	 * Boşluk olmayan <em>bir yada daha fazla</em> karakter.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	default @NotNull RegexBuilder nonWhiteSpaces() {
		
		return nonWhiteSpace().oneOrMore();
	}
	
	/**
	 * Boşluk olmayan bir karakter.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	default @NotNull RegexBuilder nonWhiteSpace(@NotNull Quanta quanta) {
		
		return with(Character.NON_WHITE_SPACE + quanta);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> boşluk harici karakter.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	default @NotNull RegexBuilder nonSpaces() {
		
		return with(Character.NON_WHITE_SPACE).oneOrMore();
	}
	
}
