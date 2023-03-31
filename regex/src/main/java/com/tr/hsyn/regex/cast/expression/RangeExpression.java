package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


public interface RangeExpression extends RegularExpression {
	
	
	/**
	 * {@link #getText()} metodu ile dönecek olan ifadeyi aralık içine alır.<br><br>
	 *
	 * <pre>var regex = Nina.regex("1-5").toRange(); // [1-5]</pre>
	 *
	 * @return Yeni bir {@link Range} nesnesi
	 */
	@NotNull Range toRange();
	
	<T extends Text> @NotNull RegexBuilder range(@NotNull T regularExpression);
	
	@NotNull RegexBuilder range(@NotNull String regularExpression);
	
	@NotNull RegexBuilder rangeNumbers();
	
	@NotNull RegexBuilder rangeLetters();
}
