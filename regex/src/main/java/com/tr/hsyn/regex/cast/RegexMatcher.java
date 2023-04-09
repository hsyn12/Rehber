package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface RegexMatcher extends Text {
	
	static @NotNull Matcher createMatcher(@NotNull String regex, @NotNull String text) {
		
		return Pattern.compile(regex).matcher(text);
	}
	
	/**
	 * Returns matcher by specified text
	 *
	 * @param text the text to create from
	 * @return matcher
	 */
	@NotNull
	default Matcher createMatcher(@NotNull CharSequence text) {
		
		return Pattern.compile(getText()).matcher(text);
	}
	
	/**
	 * Bir yazının başından sonuna kadar
	 * tüm eşleşen bölümleri almayı sağlar.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore();
	 * pl(regex.matchesOf(str));// [go, come, look]</pre>
	 *
	 * @param text the text to extract from
	 * @return extracted matches list
	 */
	@NotNull
	default List<String> matchesOf(@NotNull CharSequence text) {
		
		return matchesOf(text, 0);
	}
	
	/**
	 * Bir yazının başından sonuna kadar
	 * tüm eşleşen bölümleri almayı sağlar.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore();
	 * pl(regex.matchesOf(str, 2));// [go, come]</pre>
	 *
	 * @param text  the text to extract from
	 * @param limit the maximum number of match to extract. {@code limit < 1} means no limit.
	 * @return extracted text
	 */
	default List<String> matchesOf(@NotNull CharSequence text, int limit) {
		
		int          count   = 0;
		var          matcher = createMatcher(text);
		List<String> list    = new ArrayList<>();
		
		while (matcher.find()) {
			
			var group = matcher.group();
			
			if (group != null) {
				
				list.add(group);
				count++;
			}
			
			if (limit > 0 && count >= limit) break;
		}
		
		return list;
	}
	
	/**
	 * Düzenli ifadede belirtilen grubu yazının başından sonuna kadar arar.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.matchesOfGroup(str, "word", 0));// [go, come, look]</pre>
	 *
	 * @param text      the text to test
	 * @param groupName the name of the group for matches
	 * @param limit     the maximum number of matches. {@code limit < 1} means no limit
	 * @return list of matches
	 */
	default List<String> matchesOfGroup(@NotNull CharSequence text, @NotNull String groupName, int limit) {
		
		int          count   = 0;
		Matcher      matcher = createMatcher(text);
		List<String> list    = new ArrayList<>();
		
		try {
			while (matcher.find()) {
				
				var group = matcher.group(groupName);
				
				if (group != null) {
					
					list.add(group);
					count++;
				}
				
				if (limit > 0 && count >= limit) break;
			}
		}
		catch (Exception ignored) {}
		
		return list;
	}
	
}
