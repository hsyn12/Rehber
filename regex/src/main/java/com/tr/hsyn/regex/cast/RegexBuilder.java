package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.cast.expression.Expressions;
import com.tr.hsyn.regex.dev.Look;

import org.jetbrains.annotations.NotNull;


/**
 * Regular expression builder.
 */
public interface RegexBuilder extends Expressions {
	
	@NotNull Regex toRegex();
	
	default @NotNull RegexBuilder caseSensitive() {
		
		return with(Modifier.disable().ignoreCase());
	}
	
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
	
	/**
	 * Backreferences to a group.<br>
	 * {@code \k&lt;groupName>}
	 *
	 * @param groupName the name of the group
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder refereTo(String groupName) {
		
		return with(String.format("\\k<%s>", groupName));
	}
	
	/**
	 * Backreferences to a group.<br>
	 * {@code \k&lt;groupOrder>}
	 *
	 * @param groupOrder the order of the group
	 * @return This {@code RegexBuilder}
	 */
	@SuppressWarnings("DefaultLocale")
	default @NotNull RegexBuilder refereTo(int groupOrder) {
		
		return with(String.format("\\k<%d>", groupOrder));
	}
	
	/**
	 * Adds {@link Character#ANY}
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder any() {
		
		return with(Character.ANY);
	}
	
	/**
	 * Any character.
	 *
	 * @param times Quantifier for digit
	 * @return This {@code RegexBuilder}
	 * @see Character#ANY
	 */
	default @NotNull RegexBuilder any(int times) {
		
		return with(Character.ANY + Quanta.exactly(times));
	}
	
	default @NotNull RegexBuilder any(@NotNull Quanta quanta) {
		
		return with(Character.ANY + quanta);
	}
	
	/**
	 * Kendisinden sonra gelen düzenli ifadede büyük küçük harf ayırımı yapmaz.<br><br>
	 *
	 * <pre>
	 * var str   = "123goGo";
	 * var regex = Nina.regex().with("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 * var result = regex.withGroup("repeat", "go").oneOrMore().<u><strong>ignoreCase</strong></u>().toGroup("all").test(str);
	 * pl("Result : %s", result);//Result : false
	 * result = regex.<u><strong>ignoreCase</strong></u>().withGroup("repeat", "go").oneOrMore().toGroup("all").test(str);
	 * pl("Result : %s", result);//Result : true</pre>
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder ignoreCase() {
		
		return with(Modifier.modify().ignoreCase());
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
	
	/**
	 * Adds a double slash {@code "\\"}
	 *
	 * @return This {@code RegexBuilder}
	 */
	default @NotNull RegexBuilder escapeSlash() {
		
		return with(Character.SLASH);
	}
	
}
