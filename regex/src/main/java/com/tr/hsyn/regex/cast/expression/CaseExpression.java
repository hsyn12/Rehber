package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


public interface CaseExpression extends RegularExpression {
	
	default @NotNull RegexBuilder caseSensitive() {
		
		return with(Modifier.disable().ignoreCase());
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
}
