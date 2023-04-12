package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.jetbrains.annotations.NotNull;


/**
 * Noktalama işareti için düzenli ifadeler.<br>
 */
public interface PuncExpression extends RegularExpression {
	
	/**
	 * Bir noktalama işareti.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder punc() {
		
		return with(Character.PUNCTUATION);
	}
	
	/**
	 * Noktalama işareti dışında bir karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonPunc() {
		
		return with(Character.NON_PUNCTUATION);
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama işareti.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder puncs() {
		
		return punc().oneOrMore();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama harici karakter.
	 *
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder nonPuncs() {
		
		return nonPunc().oneOrMore();
	}
	
	/**
	 * Bir noktalama işareti.
	 *
	 * @param quanta Çokluk
	 * @return This {@link RegexBuilder}
	 */
	default @NotNull RegexBuilder punc(@NotNull Quanta quanta) {
		
		return with(Character.PUNCTUATION + quanta);
	}
	
}
