package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.Regex;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


/**
 * Noktalama işareti için düzenli ifadeler.<br>
 */
public interface PuncExpression extends RegularExpression {
	
	/**
	 * Bir noktalama işareti.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Regex#PUNC
	 */
	default @NotNull RegexBuilder punc() {
		
		return with(Regex.PUNC);
	}
	
	/**
	 * Noktalama işareti dışında bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Regex#NON_PUNC
	 */
	default @NotNull RegexBuilder nonPunc() {
		
		return with(Regex.NON_PUNC);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama işareti.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Regex#PUNCS
	 */
	default @NotNull RegexBuilder puncs() {
		
		return with(Regex.PUNCS);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama harici karakter.
	 *
	 * @return This {@link RegexBuilder}
	 * @see Regex#NON_PUNCS
	 */
	default @NotNull RegexBuilder nonPuncs() {
		
		return with(Regex.NON_PUNCS);
	}
	
	/**
	 * Bir noktalama işareti.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 * @see Regex#PUNC
	 */
	default @NotNull RegexBuilder punc(@NotNull Quanta quanta) {
		
		return with(Regex.PUNC + quanta);
	}
	
}
