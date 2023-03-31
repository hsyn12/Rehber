package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public interface LogicalExpression extends RegularExpression {
	
	/**
	 * Adds the pipe sign {@code |}
	 *
	 * @return Kurulan {@code RegexBuilder} with added the pipe
	 */
	default @NotNull RegexBuilder or() {
		
		return with("|");
	}
	
	/**
	 * @param expression Düzenli ifade
	 * @param <T>        Düzenli ifade sınıflarından bir tür
	 * @return Kurulan {@code RegexBuilder} with added the expression with or {@code regex|expression}
	 */
	default <T extends Text> @NotNull RegexBuilder or(@NotNull T expression) {
		
		return with("|" + expression.getText());
	}
	
	/**
	 * @param expression Düzenli ifade
	 * @return Kurulan {@code RegexBuilder} with added the expression with or {@code regex|expression}
	 */
	default @NotNull RegexBuilder or(@NotNull String expression) {
		
		return with("|" + expression);
	}
}
