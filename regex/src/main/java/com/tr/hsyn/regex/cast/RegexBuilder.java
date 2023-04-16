package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.cast.expression.Expressions;
import com.tr.hsyn.regex.dev.regex.Regex;

import org.jetbrains.annotations.NotNull;


/**
 * Regular expression builder.
 */
public interface RegexBuilder extends Expressions {
	
	/**
	 * Adds {@link Regex#ANY}
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder any() {
		
		return with(Regex.ANY);
	}
	
	/**
	 * Any character.
	 *
	 * @param times Quantifier for digit
	 * @return This {@code RegexBuilder}
	 * @see Regex#ANY
	 */
	default @NotNull RegexBuilder any(int times) {
		
		return with(Regex.ANY + Quanta.exactly(times));
	}
	
	default @NotNull RegexBuilder any(@NotNull Quanta quanta) {
		
		return with(Regex.ANY + quanta.getRegex());
	}
	
	/**
	 * Eşleşmenin en az sayıda olmasını sağlar.<br><br>
	 *
	 * <pre>
	 * var str     = "123456HelloTest";
	 * var lazy    = Nina.like().digits().letters().lazy();
	 * var notLazy = Nina.like().digits().letters();
	 *
	 * pl("Lazy Regex      : %s", lazy);
	 * pl("Not Lazy Regex  : %s", notLazy);
	 * pl("Lazy Result     : %s", Nina.Dev.getParts(str, lazy.findAll(str)));
	 * pl("Not Lazy Result : %s", Nina.Dev.getParts(str, notLazy.findAll(str)));
	 *
	 * // Lazy Regex      : \p{N}+\p{L}+?
	 * // Not Lazy Regex  : \p{N}+\p{L}+
	 * // Lazy Result     : [123456H]
	 * // Not Lazy Result : [123456HelloTest]
	 * // </pre>
	 *
	 * @return Kurulan {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder lazy() {
		
		return zeroOrOne();
	}
	
}
