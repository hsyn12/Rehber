package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.Group;

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
		
		return with(Group.group(expression));
	}
	
	/**
	 * Adds as a non-capturing group the given expression.
	 *
	 * @param expression the regular expression pattern to be added as a non-capturing group
	 * @return a new RegexBuilder object with the non-capturing group added to the regular expression pattern
	 */
	default @NotNull RegexBuilder groupNonCaptured(@NotNull String expression) {
		
		return with(Group.nonCaptured(expression));
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
		
		return group(expression.getText());
	}
	
	/**
	 * Returns a RegexBuilder object with a non-capturing group that matches the specified text.
	 *
	 * @param expression the Text object containing the text to match
	 * @return a RegexBuilder object with a non-capturing group that matches the specified text
	 */
	default <T extends Text> @NotNull RegexBuilder groupNonCaptured(@NotNull T expression) {
		
		return groupNonCaptured(expression.getText());
	}
	
	/**
	 * This method allows the creation of a named capturing group in a regular expression pattern.
	 *
	 * @param groupName  - a String representing the name of the capturing group
	 * @param expression - a Text object representing the regular expression pattern for the capturing group
	 * @return a RegexBuilder object with the named capturing group added to the regular expression pattern
	 */
	default <T extends Text> @NotNull RegexBuilder group(@NotNull String groupName, @NotNull T expression) {
		
		return group(groupName, expression.getText());
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
		
		return with(Group.builder().name(groupName).group(expression).build());
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
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	default RegexBuilder toGroup() {
		
		return Group.group(getText()).toRegex();
	}
	
	/**
	 * Returns a RegexBuilder object that creates an atomic group in the regular expression pattern.
	 * An atomic group is a non-capturing group that,
	 * once the regex engine enters it, it will not backtrack out of it.
	 * This method returns a <code>RegexBuilder</code> object that creates an atomic group
	 * with the text of the current <code>RegexBuilder</code> object.
	 *
	 * @return New <code>RegexBuilder</code> object that creates an atomic group in the regular expression pattern
	 */
	@NotNull
	default RegexBuilder toGroupAtomic() {
		
		return Group.atomic(getText()).toRegex();
	}
	
	/**
	 * Returns a RegexBuilder object that creates a non-capturing group regex pattern.
	 * This method is annotated with @NotNull to indicate that it will never return null.
	 * <p>
	 * This method calls the {@link Group#nonCaptured(String, Object...)} method of the {@link Group} class
	 * to create a non-capturing group regex pattern.
	 *
	 * @return a RegexBuilder object that creates a non-capturing group regex pattern.
	 */
	@NotNull
	default RegexBuilder toGroupNonCaptured() {
		
		return Group.nonCaptured(getText()).toRegex();
	}
	
	/**
	 * Returns a new <code>RegexBuilder</code> with a named group added to the current text.
	 *
	 * @param groupName the name of the group to be added
	 * @return a new RegexBuilder with the named group added
	 */
	@NotNull
	default RegexBuilder toGroup(@NotNull String groupName) {
		
		return Group.builder().name(groupName).group(getText()).build().toRegex();
	}
}
