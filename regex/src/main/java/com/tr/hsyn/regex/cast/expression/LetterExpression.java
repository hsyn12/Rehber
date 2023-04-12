package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;


/**
 * Harf karakterleri üzerine düzenli ifade kalıpları.
 */
public interface LetterExpression extends RegularExpression {
	
	/**
	 * Bir harf karakteri.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Character#LETTER
	 */
	default @NotNull RegexBuilder letter() {
		
		return with(Character.LETTER);
	}
	
	/**
	 * Verilen harfleri düzenli ifadeye ekler.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder letter(@Pattern("\\p{L}+") @NotNull String letter) {
		
		return with(letter);
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
