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
	
	/**
	 * This method finds the first occurrence of a pattern in the given text and returns its start and end indices.
	 * If no match is found, it returns {@link Index#INVALID_INDEX}.
	 *
	 * @param text the text to search for the pattern
	 * @return an Index object representing the start and end indices of the first match, or an invalid index if no match is found
	 * @throws NullPointerException if the text parameter is null
	 */
	default Index find(@NotNull CharSequence text) {
		
		var matcher = createMatcher(text);
		
		if (matcher.find()) return new Index(matcher.start(), matcher.end());
		
		return Index.ofInvalid();
	}
	
	/**
	 * This method finds the first occurrence of a pattern in the given text starting from the specified index.
	 * It returns an Index object representing the start and end indices of the match if found, otherwise it returns an invalid Index object.
	 *
	 * @param text       the text to search for the pattern
	 * @param startIndex the index to start searching from
	 * @return an Index object representing the start and end indices of the match if found, otherwise an invalid Index object
	 */
	default Index find(@NotNull CharSequence text, int startIndex) {
		
		var matcher = createMatcher(text);
		
		if (matcher.find(startIndex)) return new Index(matcher.start(), matcher.end());
		
		return Index.ofInvalid();
	}
	
	/**
	 * This method finds the index of the specified groupOrder in the given text using a regular expression matcher.
	 * If the groupOrder is found, it returns an Index object containing the start and end indices of the match.
	 * If the groupOrder is not found, it returns {@link Index#INVALID_INDEX}.
	 *
	 * @param text       The text to search for the groupOrder.
	 * @param groupOrder The order of the group to find in the regular expression.
	 * @return An Index object containing the start and end indices of the match, or an Index object with invalid indices if the groupOrder is not found.
	 */
	default Index find(@NotNull String text, int groupOrder) {
		
		var matcher = createMatcher(text);
		
		if (matcher.find()) return new Index(matcher.start(groupOrder), matcher.end(groupOrder));
		
		return Index.INVALID_INDEX;
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
	default List<Index> findGroup(@NotNull String text, @NotNull String groupName) {
		
		var         matcher = createMatcher(text);
		List<Index> list    = new ArrayList<>();
		
		while (matcher.find()) {
			
			var group = matcher.group(groupName);
			
			if (group != null) list.add(Index.of(matcher.start(groupName), matcher.end(groupName)));
		}
		
		return list;
	}
	
	default Index find(@NotNull String text, @NotNull String groupName) {
		
		var matcher = createMatcher(text);
		
		if (matcher.find())
			return Index.of(matcher.start(groupName), matcher.end(groupName));
		
		return Index.INVALID_INDEX;
	}
	
	/**
	 * This method finds all occurrences of a specified group in a given text
	 * and returns a list of Index objects representing the start and end positions of each match.
	 *
	 * @param text       The text to search for matches.
	 * @param groupOrder The index of the group to search for matches within.
	 * @return A list of Index objects representing the start and end positions of each match.
	 */
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
