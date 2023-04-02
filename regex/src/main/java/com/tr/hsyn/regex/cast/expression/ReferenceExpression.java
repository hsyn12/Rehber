package com.tr.hsyn.regex.cast.expression;


import com.tr.hsyn.regex.cast.RegexBuilder;

import org.jetbrains.annotations.NotNull;


public interface ReferenceExpression extends RegularExpression {
	
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
}
