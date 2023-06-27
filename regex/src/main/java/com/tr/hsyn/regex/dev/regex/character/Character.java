package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.character.cast.Expression;

import org.jetbrains.annotations.NotNull;


/**
 * This interface provides a set of static methods and constants for working with regular expressions.
 * Regular expressions are patterns used to match character combinations in strings.
 */
public interface Character extends Text {
	
	/**
	 * An alphabetical character. {@code \p{L}}
	 */
	String LETTER          = "\\p{L}";
	/**
	 * Any character except letter. {@code \P{L}}
	 */
	String NON_LETTER      = "\\P{L}";
	/**
	 * A digit.  {@code \p{N}}
	 */
	String DIGIT           = "\\p{N}";
	String DIGITS          = "\\p{N}+";
	/**
	 * Any character except digit. {@code \P{N}}
	 */
	String NON_DIGIT       = "\\P{N}";
	/**
	 * A space character, including line break. {@code [ \t\r\n\f\x0B]}
	 */
	String WHITE_SPACE     = "\\p{Z}";
	/**
	 * Any character except space.
	 */
	String NON_WHITE_SPACE = "\\P{Z}";
	/**
	 * Punctuation character {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 */
	String PUNCTUATION     = "\\p{P}";
	/**
	 * Any character except punctuation.
	 */
	String NON_PUNCTUATION = "\\P{P}";
	/**
	 * Any character from {@link #LETTER}, {@link #DIGIT}, {@link #PUNCTUATION}, {@link #WHITE_SPACE}
	 */
	
	@NotNull String ANY              = String.format("[%s%s%s%s]", LETTER, DIGIT, PUNCTUATION, WHITE_SPACE);
	@NotNull String NON_ANY          = String.format("[^%s%s%s%s]", LETTER, DIGIT, PUNCTUATION, WHITE_SPACE);
	/**
	 * A lowercase alphabetical character {@code [a-z]}
	 */
	@NotNull String LETTER_LOWER     = "\\p{Ll}";
	/**
	 * Any character except lowercase alphabetical. {@code [^\p{Ll}]}
	 */
	@NotNull String NON_LETTER_LOWER = "\\P{Ll}";
	/**
	 * An uppercase alphabetical character {@code [A-Z]}
	 */
	@NotNull String LETTER_UPPER     = "\\p{Lu}";
	/**
	 * Any character except uppercase alphabetical. {@code [^\p{Lu}]}
	 */
	@NotNull String NON_LETTER_UPPER = "\\P{Lu}";
	/**
	 * Delimiters {@code '.$^{[()|*+?\'}
	 */
	String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
	/**
	 * A control character {@code [\p{Cntrl}]}
	 */
	@NotNull String CONTROL     = "\\p{C}";
	/**
	 * A non-control character {@code [^\p{C}]}
	 */
	@NotNull String NON_CONTROL = "\\P{C}";
	/**
	 * A symbol character {@code [\p{S}]}
	 */
	@NotNull String SYMBOL      = "\\p{S}";
	/**
	 * A backslash character {@code [\\]}
	 */
	@NotNull String BACK_SLASH  = "\\\\";
	
	/**
	 * Returns a non-null character that is guaranteed to be different from any other character.
	 * This method is useful for initializing variables that need to have a default value.
	 *
	 * @return a non-null character that is guaranteed to be different from any other character
	 */
	@NotNull Character non();
	
	/**
	 * Returns a new {@link Text} object that represents the current text concatenated
	 * with the {@link Quanta#ZERO_OR_ONE} constant.
	 * The returned {@link Text} object is guaranteed to be not null.
	 *
	 * @return a new {@link Text} object that represents the current text concatenated with the {@link Quanta#ZERO_OR_ONE} constant.
	 */
	default @NotNull Text zeroOrOne() {
		
		return Text.of(getText() + Quanta.ZERO_OR_ONE);
	}
	
	/**
	 * Returns a new {@link Text} object that represents the current text concatenated
	 * with the {@link Quanta#ZERO_OR_MORE} constant.
	 * The returned {@link Text} object is guaranteed to be not null.
	 *
	 * @return a new {@link Text} object that represents the current text concatenated with the {@link Quanta#ZERO_OR_MORE} constant.
	 */
	default @NotNull Text zeroOrMore() {
		
		return Text.of(getText() + Quanta.ZERO_OR_MORE);
	}
	
	/**
	 * Returns a new {@link Text} object that represents the current text concatenated
	 * with the {@link Quanta#ONE_OR_MORE} constant.
	 * The returned {@link Text} object is guaranteed to be not null.
	 *
	 * @return a new {@link Text} object that represents the current text concatenated with the {@link Quanta#ONE_OR_MORE} constant.
	 */
	default @NotNull Text oneOrMore() {
		
		return Text.of(getText() + Quanta.ONE_OR_MORE);
	}
	
	/**
	 * This method removes all occurrences of a specific character from a given string.
	 *
	 * @param text the input string from which the text needs to be removed
	 * @return a new string with all occurrences of the specified text removed
	 * @throws NullPointerException if the input string is null
	 */
	default String removeFrom(@NotNull String text) {
		
		return text.replaceAll(getText(), "");
	}
	
	/**
	 * Replaces all occurrences of the text in the input string with the specified replacement string.
	 *
	 * @param text        the input string to be processed
	 * @param replacement the replacement string to be used
	 * @return the resulting string after all occurrences of the text have been replaced with the replacement string
	 */
	default String replaceFrom(@NotNull String text, String replacement) {
		
		return text.replaceAll(getText(), replacement);
	}
	
	/**
	 * Returns a new string that retains only the characters from
	 * the input string that match the pattern specified by the non-null {@link #non()} object.
	 *
	 * @param text the input string to be filtered
	 * @return a new string that retains only the characters from the input string that match the pattern specified by the non-null non() object
	 * @implNote This method uses the replaceAll() method of the String class to remove all characters that do not match the pattern specified by the non() object.
	 * @implSpec This method is a default method of the interface and can be overridden by implementing classes.
	 */
	default String retainFrom(@NotNull String text) {
		
		return text.replaceAll(non().getText(), "");
	}
	
	/**
	 * Checks if the given text matches the current text concatenated with one or more Quanta.
	 *
	 * @param text the text to be checked for a match
	 * @return true if the given text matches the current text concatenated with one or more Quanta, false otherwise
	 */
	default boolean all(@NotNull String text) {
		
		return text.matches(getText() + Quanta.ONE_OR_MORE);
	}
	
	/**
	 * Returns a <code>Character</code> object representing the specified text.
	 *
	 * @param text the string to be converted to a Character object
	 * @return a Character object representing the specified text
	 */
	@NotNull
	static Character of(@NotNull String text) {
		
		return new Expression(text);
	}
	
	
}
