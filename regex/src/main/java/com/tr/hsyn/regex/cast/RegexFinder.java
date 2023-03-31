package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public interface RegexFinder extends RegexTester {
	
	/**
	 * Returns all matches of the regex in the text.
	 *
	 * @param text the text to test
	 * @return all matches of the regex in the text
	 */
	default List<Index> findAll(@NotNull CharSequence text) {
		
		return findAll(text, 0);
	}
	
	default Index find(@NotNull CharSequence text) {
		
		var matcher = createMatcher(text);
		
		if (matcher.find()) return new Index(matcher.start(), matcher.end());
		
		return Index.ofInvalid();
	}
	
	/**
	 * Returns all matches of the regex in the text.
	 *
	 * @param text       the text to test
	 * @param beginIndex the index to start from
	 * @return all matches of the regex in the text
	 */
	@NotNull
	default List<Index> findAll(@NotNull CharSequence text, int beginIndex) {
		
		var list = new ArrayList<Index>();
		
		if (beginIndex < 0) beginIndex = 0;
		if (text.length() <= beginIndex) return list;
		
		var matcher = createMatcher(text.toString().substring(beginIndex));
		
		while (matcher.find()) {
			
			if (matcher.start() != matcher.end())
				list.add(Index.of(matcher.start(), matcher.end()));
		}
		
		return list;
	}
	
	/**
	 * Returns all matched indexes of the given group name.<br><br>
	 *
	 * <pre>
	 * var str   = "123go567come789look?";
	 * var regex = Nina.like().letter().oneOrMore().toGroup("word");
	 *
	 * pl(regex.findGroups(str, "word")); // [Index{start=3, end=5}, Index{start=8, end=12}, Index{start=15, end=19}]
	 * </pre>
	 *
	 * @param text      the text to get from
	 * @param groupName the name of the group
	 * @return list of matches indexes
	 */
	default List<Index> findGroup(@NotNull CharSequence text, @NotNull String groupName) {
		
		var         matcher = createMatcher(text);
		List<Index> list    = new ArrayList<>();
		
		while (matcher.find()) {
			
			var group = matcher.group(groupName);
			
			if (group != null) list.add(Index.of(matcher.start(groupName), matcher.end(groupName)));
		}
		
		return list;
	}
	
	default List<Index> findGroup(@NotNull CharSequence text, int groupOrder) {
		
		var         matcher = createMatcher(text);
		List<Index> list    = new ArrayList<>();
		
		while (matcher.find()) {
			
			var group = matcher.group(groupOrder);
			
			if (group != null) list.add(Index.of(matcher.start(groupOrder), matcher.end(groupOrder)));
		}
		
		return list;
	}
	
}
