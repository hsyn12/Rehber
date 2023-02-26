package com.tr.hsyn.regex.cast;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Regular expression builder.
 */
@SuppressWarnings("StringBufferMayBeStringBuilder")
public interface Regex extends Text {
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.
	 *
	 * @param regularExpression Eklenecek düzenli ifade
	 * @param <T>               {@link Text} sınıfınından herhangi bir tür
	 * @return Yeni bir {@code Regex} nesnesi
	 */
	<T extends Text> @NotNull Regex with(@NotNull T regularExpression);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.
	 *
	 * @param expression Eklenecek düzenli ifade
	 * @return Yeni bir {@code Regex} nesnesi
	 */
	@NotNull Regex with(@NotNull String expression);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.
	 *
	 * @param i int
	 * @return Yeni bir {@code Regex} nesnesi
	 */
	@NotNull Regex with(int i);
	
	/**
	 * Düzenli ifadenin sonuna ekleme yapar.
	 *
	 * @param c char
	 * @return Yeni bir {@code Regex} nesnesi
	 */
	@NotNull Regex with(char c);
	
	/**
	 * Adds quantifier to the match.
	 *
	 * @param count the count of the quantifier
	 * @return match with quantifier {@code regex{count}}
	 */
	@NotNull
	default Regex times(int count) {
		
		return with(Quantifier.EXACTLY(count));
	}
	
	/**
	 * Adds quantifier to the match.
	 *
	 * @param min minimum number of repetitions
	 * @param max maximum number of repetitions
	 * @return match with quantifier {@code regex{min,max}}
	 */
	@NotNull
	default Regex times(int min, int max) {
		
		return with(Quantifier.BETWEEN(min, max));
	}
	
	/**
	 * Positive lookahead. (assertion after the match)
	 *
	 * @param expression Düzenli ifade
	 * @param <T>        Düzenli ifade sınıflarından bir tür
	 * @return New {@code Regex} with added the expression {@code regex(?=expression)}
	 */
	default <T extends Text> @NotNull Regex lookAhead(@NotNull T expression) {
		
		return with(String.format("(?=%s)", expression.getText()));
	}
	
	/**
	 * Adds reluctant quantifiers {@code +?} (yield shortest matches).
	 *
	 * @return New {@code Regex} with added the reluctant quantifiers
	 */
	default @NotNull Regex reluctant() {
		
		return with("+?");
	}
	
	/**
	 * Adds the pipe sign {@code |}
	 *
	 * @return New {@code Regex} with added the pipe
	 */
	default @NotNull Regex or() {
		
		return with("|");
	}
	
	/**
	 * @param expression Düzenli ifade
	 * @param <T>        Düzenli ifade sınıflarından bir tür
	 * @return New {@code Regex} with added the expression with or {@code regex|expression}
	 */
	default <T extends Text> @NotNull Regex or(@NotNull T expression) {
		
		return with("|" + expression.getText());
	}
	
	/**
	 * @param expression Düzenli ifade
	 * @return New {@code Regex} with added the expression with or {@code regex|expression}
	 */
	default @NotNull Regex or(@NotNull String expression) {
		
		return with("|" + expression);
	}
	
	/**
	 * {@link #getText()} metodu ile dönecek olan ifadeyi aralık içine alır.<br><br>
	 *
	 * <pre>var regex = Teddy.regex("1-5").toRange(); // [1-5]</pre>
	 *
	 * @return Yeni bir {@link Regex} nesnesi
	 */
	@NotNull Range toRange();
	
	<T extends Text> @NotNull Regex range(@NotNull T expression);
	
	@NotNull Regex range(@NotNull String expression);
	
	default @NotNull Regex group(@NotNull String expression) {
		
		return with(String.format("(%s)", expression));
	}
	
	default <T extends Text> @NotNull Regex group(@NotNull T expression) {
		
		return with(String.format("(%s)", expression.getText()));
	}
	
	default <T extends Text> @NotNull Regex group(@NotNull String groupName, @NotNull T expression) {
		
		return with(String.format("(?<%s>%s)", groupName, expression.getText()));
	}
	
	default @NotNull Regex group(@NotNull String groupName, @NotNull String expression) {
		
		return with(String.format("(?<%s>%s)", groupName, expression));
	}
	
	@NotNull Regex toGroup();
	
	@NotNull Regex toGroup(@NotNull String groupName);
	
	/**
	 * Adds the boundary meta character to the match.<br>
	 * {@code \b}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex boundary() {
		
		return with(Anchor.BOUNDARY);
	}
	
	/**
	 * Adds the space meta character to the match
	 *
	 * @return New {@code Regex}
	 */
	
	default @NotNull Regex space() {
		
		return with(Character.WHITE_SPACE);
	}
	
	/**
	 * Adds the non-space meta character to the match
	 *
	 * @return New {@code Regex}
	 */
	
	default @NotNull Regex nonSpace() {
		
		return with(Character.NON_WHITE_SPACE);
	}
	
	/**
	 * Backreferences to a group.<br>
	 * {@code \k&lt;groupName>}
	 *
	 * @param groupName the name of the group
	 * @return New {@code Regex}
	 */
	default @NotNull Regex refereTo(String groupName) {
		
		return with(String.format("\\k<%s>", groupName));
	}
	
	/**
	 * Backreferences to a group.<br>
	 * {@code \k&lt;groupOrder>}
	 *
	 * @param groupOrder the order of the group
	 * @return New {@code Regex}
	 */
	@SuppressWarnings("DefaultLocale")
	default @NotNull Regex refereTo(int groupOrder) {
		
		return with(String.format("\\k<%d>", groupOrder));
	}
	
	/**
	 * Adds a meta character {@code "."}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex any() {
		
		return with(Character.ANY);
	}
	
	default @NotNull Regex ignoreCase() {
		
		return with(Modifier.of().ignoreCase());
	}
	
	/**
	 * Adds lowercase letter meta character.
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex letterLower() {
		
		return with(Character.LETTER_LOWER);
	}
	
	/**
	 * Adds uppercase letter meta character.
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex letterUpper() {
		
		return with(Character.LETTER_UPPER);
	}
	
	/**
	 * Adds the meta character {@code ?}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex zeroOrOne() {
		
		return with(Quantifier.ZERO_OR_ONE);
	}
	
	/**
	 * Adds the meta character {@code *}
	 *
	 * @return New {@code Regex}
	 */
	@NotNull
	default Regex zeroOrMore() {
		
		return with(Quantifier.ZERO_OR_MORE);
	}
	
	/**
	 * Adds the meta character {@code +}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex oneOrMore() {
		
		return with(Quantifier.ONE_OR_MORE);
	}
	
	/**
	 * Adds quantifier to the match.
	 *
	 * @param count the minimum number of times the match must occur
	 * @return New {@code Regex}
	 */
	default @NotNull Regex atLeast(int count) {
		
		return with(Quantifier.AT_LEAST(count));
	}
	
	/**
	 * Adds quantifier to the match.
	 *
	 * @param count the maximum number of times the match must occur
	 * @return New {@code Regex}
	 */
	default @NotNull Regex atMost(int count) {
		
		return with(Quantifier.AT_MOST(count));
	}
	
	/**
	 * Adds a digit character to the match.<br>
	 * {@code \p{N}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex digit() {
		
		return with(Character.DIGIT);
	}
	
	/**
	 * Adds a non-digit meta character to the match.<br>
	 * {@code \P{N}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex nonDigit() {
		
		return with(Character.NON_DIGIT);
	}
	
	/**
	 * Adds a letter meta character.<br>
	 * {@code \p{L}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex letter() {
		
		return with(Character.LETTER);
	}
	
	/**
	 * Adds a non-letter meta character.<br>
	 * {@code \P{L}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex nonLetter() {
		
		return with(Character.NON_LETTER);
	}
	
	/**
	 * Adds a control meta character.<br>
	 * {@code \p{C}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex control() {
		
		return with(Character.CONTROL);
	}
	
	/**
	 * Adds a non-control meta character.<br>
	 * {@code \P{C}}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex nonControl() {
		
		return with(Character.NON_CONTROL);
	}
	
	/**
	 * Adds a double slash {@code "\\"}
	 *
	 * @return New {@code Regex}
	 */
	default @NotNull Regex escapeSlash() {
		
		return with(Character.SLASH);
	}
	
	/**
	 * Tests created regex against the given string (all string matching).
	 *
	 * @param text the text to test
	 * @return true if the regex matches the text
	 */
	default boolean test(@NotNull CharSequence text) {
		
		return text.toString().matches(getText());
	}
	
	/**
	 * Tests created regex against the given string.
	 *
	 * @param text       the text to test
	 * @param beginIndex the index to start the test
	 * @return if the regex matches the text until the limit is reached or the end of the text returns {@code true}, otherwise {@code false}
	 */
	default boolean test(@NotNull CharSequence text, int beginIndex) {
		
		return createMatcher(text.toString()).find(beginIndex);
	}
	
	/**
	 * Tests text contains the regex or not (partial match).
	 *
	 * @param text the text to test
	 * @return true if the text contains the regex
	 */
	default boolean containsIn(@NotNull CharSequence text) {
		
		return test(text, 0);
	}
	
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
		
		while (matcher.find()) list.add(Index.of(matcher.start(), matcher.end()));
		
		return list;
	}
	
	/**
	 * Gruplama yöntemi kullanarak bir yazının başından sonuna kadar
	 * tüm eşleşen bölümleri almayı sağlar.<br><br>
	 *
	 * <pre>
	 * String pattern = "(?&lt;name&gt;\\p{L}+)(?=\\P{L}+)?";
	 * String text    = "ali, ()?    veli,,,, deli,,,, keli";
	 *
	 * var l = newRegex(pattern).matchesOf(text); // [ali, veli, deli, keli]
	 * </pre>
	 *
	 * @param text the text to extract from
	 * @return extracted matches list
	 */
	@NotNull
	default List<String> matchesOf(@NotNull CharSequence text) {
		
		return matchesOf(text, 0);
	}
	
	/**
	 * Gruplama yöntemi kullanarak bir yazının başından sonuna kadar
	 * tüm eşleşen bölümleri almayı sağlar.<br><br>
	 *
	 * <pre>
	 * String pattern = "(?&lt;name&gt;\\p{L}+)(?=\\P{L}+)?";
	 * String text    = "ali, ()?    veli,,,, deli,,,, keli";
	 *
	 * var l     = newRegex(pattern).matchesOf(text, 0); // [ali, veli, deli, keli]
	 * var one   = newRegex(pattern).matchesOf(text, 1); // [ali]
	 * var two   = newRegex(pattern).matchesOf(text, 2); // [ali, veli]
	 * var three = newRegex(pattern).matchesOf(text, 3); // [ali, veli, deli]
	 * var four  = newRegex(pattern).matchesOf(text, 4); // [ali, veli, deli, keli]
	 * var four2 = newRegex(pattern).matchesOf(text, 5); // [ali, veli, deli, keli]
	 * </pre>
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
	 * {@link #matchesOf(CharSequence, int)} metodu gibi gruplama
	 * yöntemi kullanılır, ancak burada grubun ismi verilerek daha spesifik eşleşmeler bulmayı sağlar.
	 *
	 * @param text      the text to test
	 * @param groupName the name of the group for matches
	 * @param limit     the maximum number of matches
	 * @return list of matches
	 */
	default List<String> matchesGroupOf(@NotNull CharSequence text, @NotNull String groupName, int limit) {
		
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
	
	/**
	 * Returns all matched indexes of the given group name
	 *
	 * @param text      the text to get from
	 * @param groupName the name of the group
	 * @return list of matches indexes
	 */
	default List<Index> findGroups(@NotNull CharSequence text, @NotNull String groupName) {
		
		var         matcher = createMatcher(text);
		List<Index> list    = new ArrayList<>();
		
		while (matcher.find()) {
			
			var group = matcher.group(groupName);
			
			if (group != null) list.add(Index.of(matcher.start(groupName), matcher.end(groupName)));
			
		}
		
		return list;
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
	 * Removes the match from the given string.
	 *
	 * @param text the text to remove from
	 * @return text without the match
	 */
	default String removeFrom(@NotNull CharSequence text) {
		
		return createMatcher(text).replaceAll("");
	}
	
	/**
	 * Removes the match from the given string.
	 *
	 * @param text  the text to remove from
	 * @param limit the maximum number of matches to remove. {@code limit < 1} means no limit.
	 * @return text without the match
	 */
	default String removeFrom(@NotNull CharSequence text, int limit) {
		
		int count = 0;
		var regex = Pattern.compile(getText());
		
		var matcher = regex.matcher(text);
		
		StringBuilder sb = new StringBuilder();
		
		while (matcher.find()) {
			
			matcher.appendReplacement(sb, "");
			count++;
			
			if (limit > 0 && count >= limit) break;
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * Replaces the match with the given string.
	 *
	 * @param text        the text to replace in
	 * @param replacement the replacement string
	 * @return text with the replacement
	 */
	default String replaceFrom(@NotNull CharSequence text, @NotNull String replacement) {
		
		return Pattern.compile(getText()).matcher(text).replaceAll(replacement);
	}
	
	/**
	 * Replaces the match with the given string.
	 *
	 * @param text        the text to replace in
	 * @param replacement the replacement string
	 * @param limit       the maximum number of replacements to make. {@code limit < 1} means no limit.
	 * @return text with the replacement
	 */
	default String replaceFrom(@NotNull String text, @NotNull String replacement, int limit) {
		
		int count   = 0;
		var matcher = createMatcher(text);
		
		//!  StringBuilder android için yok görünüyor
		StringBuffer sb = new StringBuffer();
		
		while (matcher.find()) {
			
			matcher.appendReplacement(sb, replacement);
			
			count++;
			
			if (limit > 0 && count >= limit) break;
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * Index data
	 */
	class Index {
		
		public final int start;
		public final int end;
		
		public Index(int start, int end) {
			
			this.start = start;
			this.end   = end;
		}
		
		@NotNull
		static Index of(int start, int end) {
			
			return new Index(start, end);
		}
		
		@NotNull
		@Override
		public String toString() {
			
			return "Index{" + "start=" + start + ", end=" + end + '}';
		}
	}
}
