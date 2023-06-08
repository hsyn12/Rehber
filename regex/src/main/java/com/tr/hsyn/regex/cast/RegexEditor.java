package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;


public interface RegexEditor extends RegexFinder {
	
	
	/**
	 * Removes the match from the given string.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.removeFrom(str)); // 123567789?</pre>
	 *
	 * @param text the text to remove from
	 * @return text without the match
	 */
	default String removeFrom(@NotNull CharSequence text) {
		
		return createMatcher(text).replaceAll("");
	}
	
	/**
	 * Removes the match from the given string.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.removeFrom(str, 2)); // 123567789look</pre>
	 *
	 * @param text  the text to remove from
	 * @param limit the maximum number of matches to remove. {@code limit < 1} means no limit.
	 * @return text without the match
	 */
	default String removeFrom(@NotNull CharSequence text, int limit) {
		
		if (true) throw new IllegalArgumentException("Not implemented yet");
		int     count = 0;
		Pattern regex = Pattern.compile(getText());
		
		java.util.regex.Matcher matcher = regex.matcher(text);
		
		StringBuilder sb = new StringBuilder();
		
		while (matcher.find()) {
			
			//matcher.appendReplacement(sb, "");
			count++;
			
			if (limit > 0 && count >= limit) break;
		}
		
		//matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * Replaces the match with the given string.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.replaceFrom(str, "#")); // 123#567#789#?</pre>
	 *
	 * @param text        the text to replace in
	 * @param replacement the replacement string
	 * @return text with the replacement
	 */
	default String replaceFrom(@NotNull String text, @NotNull String replacement) {
		
		return Pattern.compile(getText()).matcher(text).replaceAll(replacement);
	}
	
	/**
	 * Replaces the match with the given string.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.replaceFrom(str, "#", 2)); // 123#567#789look?</pre>
	 *
	 * @param text        the text to replace in
	 * @param replacement the replacement string
	 * @param limit       the maximum number of replacements to make. {@code limit < 1} means no limit.
	 * @return text with the replacement
	 */
	default String replaceFrom(@NotNull String text, @NotNull String replacement, int limit) {
		
		int                              count   = 0;
		java.util.regex.@NotNull Matcher matcher = createMatcher(text);
		
		//!  StringBuilder android için yok görünüyor
		//noinspection StringBufferMayBeStringBuilder
		StringBuffer sb = new StringBuffer();
		
		while (matcher.find()) {
			
			matcher.appendReplacement(sb, replacement);
			
			count++;
			
			if (limit > 0 && count >= limit) break;
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}
}
