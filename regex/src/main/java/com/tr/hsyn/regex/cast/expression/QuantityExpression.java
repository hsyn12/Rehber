package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


/**
 * Düzenli ifadeler için çokluk bildiren metotlar tanımlar.
 */
public interface QuantityExpression extends RegularExpression {
	
	/**
	 * Düzenli ifadeye {@code +} karakterini ekler.
	 * Kendisinden önce gelen ifadenin <em>bir yada daha fazla</em> kez tekrar edeceğini bildirir.
	 *
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder oneOrMore() {
		
		return with(Quanta.ONE_OR_MORE.getRegex());
	}
	
	/**
	 * Düzenli ifadeye {@code *} karakterini ekler.
	 * Kendisinden önce gelen ifadenin <em>sıfır yada daha fazla</em> kez tekrar edeceğini bildirir.
	 *
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder zeroOrMore() {
		
		return with(Quanta.ZERO_OR_MORE.getRegex());
	}
	
	/**
	 * Düzenli ifadeye {@code ?} karakterini ekler.
	 * Kendisinden önce gelen ifadenin <em>sıfır yada bir</em> kez tekrar edeceğini bildirir.
	 *
	 * @return Kurulan {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder zeroOrOne() {
		
		return with(Quanta.ZERO_OR_ONE.getRegex());
	}
	
	/**
	 * Kendisinden önce gelen ifadenin <em>en fazla</em> kaç kez tekrar edeceğini bildirir.
	 *
	 * @param most <em>En fazla</em> tekrar sayısı
	 * @return This  {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder atMost(int most) {
		
		return with(Quanta.atMost(most));
	}
	
	/**
	 * Kendisinden önce gelen ifadenin <em>en az</em> kaç kez tekrar edeceğini bildirir.
	 *
	 * @param least <em>En az</em> tekrar sayısı
	 * @return This  {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder atLeast(int least) {
		
		return with(Quanta.atLeast(least));
	}
	
	/**
	 * Kendisinden önce gelen ifadenin <em>tam olarak</em> kaç kez tekrar edeceğini bildirir.
	 *
	 * @param times Tekrar sayısı
	 * @return This  {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder times(int times) {
		
		return with(Quanta.exactly(times));
	}
	
	/**
	 * Kendisinden önce gelen ifadenin <em>en az</em> ve <em>en çok</em> kaç kez tekrar edeceğini bildirir.
	 *
	 * @param min En az tekrar sayısı
	 * @param max En çok tekrar sayısı
	 * @return This  {@code RegexBuilder} nesnesi
	 */
	default @NotNull RegexBuilder times(int min, int max) {
		
		return with(Quanta.minMax(min, max));
	}
	
	
}
