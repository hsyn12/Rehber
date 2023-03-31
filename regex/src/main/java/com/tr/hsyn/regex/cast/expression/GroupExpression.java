package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public interface GroupExpression extends RegularExpression {
	
	/**
	 * Bir ifadeyi grup içine alarak düzenli ifadenin sonuna ekler.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 *
	 * regex = regex.withGroup("go").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+(go)+</pre>
	 *
	 * @param expression İfade
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder group(@NotNull String expression) {
		
		return with(String.format("(%s)", expression));
	}
	
	default @NotNull RegexBuilder groupNonCaptured(@NotNull String expression) {
		
		return with(String.format("(?:%s)", expression));
	}
	
	/**
	 * Bir ifadeyi grup içine alarak düzenli ifadenin sonuna ekler.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 *
	 * regex = regex.withGroup(Nina.regex().digit().or().withPunc()).oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+(\p{N}|\p{P})+</pre>
	 *
	 * @param expression İfade
	 * @param <T>        {@link Text} sınıfından bir tür
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	default <T extends Text> @NotNull RegexBuilder group(@NotNull T expression) {
		
		return with(String.format("(%s)", expression.getText()));
	}
	
	default <T extends Text> @NotNull RegexBuilder groupNonCaptured(@NotNull T expression) {
		
		return with(String.format("(?:%s)", expression.getText()));
	}
	
	/**
	 * Bir ifadeyi grup içine alarak düzenli ifadenin sonuna ekler.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 *
	 * regex = regex.withGroup("dp", Nina.regex().digit().or().withPunc()).oneOrMore();
	 * pl("Regex : %s", regex);/Regex : [0-9]+(?&lt;dp>\p{N}|\p{P})+</pre>
	 *
	 * @param groupName  Grup ismi
	 * @param expression İfade
	 * @param <T>        {@link Text} sınıfından bir tür
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	default <T extends Text> @NotNull RegexBuilder group(@NotNull String groupName, @NotNull T expression) {
		
		return with(String.format("(?<%s>%s)", groupName, expression.getText()));
	}
	
	/**
	 * Bir ifadeyi grup içine alarak düzenli ifadenin sonuna ekler.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 *
	 * regex = regex.withGroup("repeat", "go").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+(?&lt;repeat>go)+</pre>
	 *
	 * @param groupName  Grubun adı
	 * @param expression İfade
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder group(@NotNull String groupName, @NotNull String expression) {
		
		return with(String.format("(?<%s>%s)", groupName, expression));
	}
	
	/**
	 * Düzenli ifadenin tamamını grup olarak döndürür.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 * regex = regex.withGroup("repeat", "go").oneOrMore().toGroup();
	 * pl("Regex : %s", regex);//Regex : ([0-9]+(?&lt;repeat>go)+)</pre>
	 *
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	@NotNull RegexBuilder toGroup();
	
	@NotNull RegexBuilder toGroupAtomic();
	
	@NotNull RegexBuilder toGroupNonCaptured();
	
	/**
	 * Düzenli ifadenin tamamını grup olarak döndürür.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex("[0-9]").oneOrMore();
	 * pl("Regex : %s", regex);//Regex : [0-9]+
	 * regex = regex.withGroup("repeat", "go").oneOrMore().toGroup("all");
	 * pl("Regex : %s", regex);//Regex : (?&lt;all>[0-9]+(?&lt;repeat>go)+)</pre>
	 *
	 * @param groupName Grubun adı
	 * @return Kurulan {@link RegexBuilder} nesnesi
	 */
	@NotNull RegexBuilder toGroup(@NotNull String groupName);
}
