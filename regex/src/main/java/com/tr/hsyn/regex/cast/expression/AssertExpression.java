package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.Look;

import org.jetbrains.annotations.NotNull;


/**
 * Kontrol ifadesi.
 */
public interface AssertExpression extends RegularExpression {
	
	/**
	 * Positive lookahead. (assertion after the match)
	 *
	 * @param expression Düzenli ifade
	 * @param <T>        Düzenli ifade sınıflarından bir tür
	 * @return This {@code RegexBuilder} with added the expression {@code regex(?=expression)}
	 */
	default <T extends Text> @NotNull RegexBuilder lookAhead(@NotNull T expression) {
		
		return with(Look.ahead(expression));
	}
	
	/**
	 * Positive lookbehind. (assertion before the match)
	 *
	 * @param expression Düzenli ifade
	 * @param <T>        Düzenli ifade sınıflarından bir tür
	 * @return This {@code RegexBuilder} <code>regex(?&lt;=expression)</code>
	 */
	default <T extends Text> @NotNull RegexBuilder lookBehind(@NotNull T expression) {
		
		return with(Look.behind(expression));
	}
}
