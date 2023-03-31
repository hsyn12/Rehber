package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


/**
 * Harf karakterleri üzerine düzenli ifade kalıpları.
 */
public interface LetterExpression extends RegularExpression {
	
	/**
	 * Bir harf karakteri.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letter() {
		
		return with(Character.LETTER);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> harf karakteri
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letters() {
		
		return letter().oneOrMore();
	}
	
	/**
	 * Bir harf karakteri.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letter(@NotNull Quanta quanta) {
		
		return with(Character.LETTER + quanta);
	}
	
	/**
	 * Harf olmayan bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonLetter() {
		
		return with(Character.NON_LETTER);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> harf olmayan karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonLetters() {
		
		return nonLetter().oneOrMore();
	}
	
	/**
	 * Harf olmayan bir karakter.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonLetter(@NotNull Quanta quanta) {
		
		return with(Character.NON_LETTER + quanta);
	}
	
	/**
	 * Küçük bir harf.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letterLow() {
		
		return with(Character.LETTER_LOWER);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> küçük harf.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder lettersLow() {
		
		return letterLow().oneOrMore();
	}
	
	/**
	 * Küçük bir harf.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letterLow(@NotNull Quanta quanta) {
		
		return with(Character.LETTER_LOWER + quanta);
	}
	
	/**
	 * Büyük bir harf.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letterUp() {
		
		return with(Character.LETTER_UPPER);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> büyük harf.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder lettersUp() {
		
		return letterUp().oneOrMore();
	}
	
	/**
	 * Büyük bir harf.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letterUp(@NotNull Quanta quanta) {
		
		return with(Character.LETTER_UPPER + quanta);
	}
	
	
}
