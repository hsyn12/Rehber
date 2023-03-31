package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.RegexEditor;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public interface RegularExpression extends RegexEditor {
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex(".+").with('[').with(0).with("-9]").build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex.withPunc();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex = regex.withPunc().build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]\p{P}</pre>
	 *
	 * @param expression Eklenecek düzenli ifade
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	@NotNull RegexBuilder with(@NotNull String expression);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex(".+").with('[').with(0).with("-9]").build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex.withPunc();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex = regex.withPunc().build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]\p{P}</pre>
	 *
	 * @param regularExpression Eklenecek düzenli ifade
	 * @param <T>               {@link Text} sınıfınından herhangi bir tür
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	<T extends Text> @NotNull RegexBuilder with(@NotNull T regularExpression);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex(".+").with('[').with(0).with("-9]").build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex.withPunc();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex = regex.withPunc().build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]\p{P}</pre>
	 *
	 * @param i int
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	@NotNull RegexBuilder with(int i);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.<br><br>
	 *
	 * <pre>
	 * var regex = Nina.regex(".+").with('[').with(0).with("-9]").build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex.withPunc();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]
	 *
	 * regex = regex.withPunc().build();
	 * pl("Regex : %s", regex);//Regex : .+[0-9]\p{P}</pre>
	 *
	 * @param c char
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	@NotNull RegexBuilder with(char c);
}
