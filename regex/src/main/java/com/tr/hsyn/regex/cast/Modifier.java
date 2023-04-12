package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.act.Modify;

import org.jetbrains.annotations.NotNull;


public interface Modifier extends Text {
	
	//?                            Mode Modifiers
	
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//?   Mode         Name                                  Meaning
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?i)    Ignore case mode              Enables case-insensitive matching for US-ASCII text
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?s)    DOTALL mode                   Makes DOT match all the characters, including line breaks
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?m)    MULTILINE mode                Makes ^ and $ match the beginning and end of any line
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?u)    UNICODE_CASE mode             Enables Unicode case folding
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?U)    UNICODE_CHARACTER mode        Enables the Unicode version of predefined character classes and POSIX character classes
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?d)    UNIX_LINES mode               Enables line-endings to be “\n”, “\r”, or “\r\n”
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	//!|    (?x)    EXTENDED mode                 Enables whitespace and comments to be ignored
	//?|-----------------------------------------------------------------------------------------------------------------------------------------------------|
	
	
	/**
	 * (?i)    Ignore case mode              Enables case-insensitive matching for US-ASCII text
	 */
	String IGNORE_CASE       = "i";
	/**
	 * (?s)    DOTALL mode                   Makes DOT match all the characters, including line breaks
	 */
	String DOTALL            = "s";
	/**
	 * (?m)    MULTILINE mode                Makes ^ and $ match the beginning and end of any line
	 */
	String MULTILINE         = "m";
	/**
	 * (?u)    UNICODE_CASE mode             Enables Unicode case folding
	 */
	String UNICODE_CASE      = "u";
	/**
	 * (?U)    UNICODE_CHARACTER mode        Enables the Unicode version of predefined character classes and POSIX character classes
	 */
	String UNICODE_CHARACTER = "U";
	/**
	 * (?d)    UNIX_LINES mode               Enables line-endings to be “\n”, “\r”, or “\r\n”
	 */
	String UNIX_LINES        = "d";
	/**
	 * (?x)    EXTENDED mode                 Enables whitespace and comments to be ignored
	 */
	String EXTENDED          = "x";
	
	/**
	 * @return new modifier
	 */
	@NotNull
	static Modifier modify() {
		
		return new Modify();
	}
	
	@NotNull
	static Modifier disable() {
		
		return new Modify(true);
	}
	
	/**
	 * @return the regex string
	 */
	@Override
	@NotNull
	String getText();
	
	/**
	 * @return modifier object with added the ignoreCase symbol
	 */
	@NotNull
	Modifier ignoreCase();
	
	/**
	 * @return modifier object with added the dotAll symbol
	 */
	@NotNull
	Modifier dotAll();
	
	/**
	 * @return modifier object with added the multiline symbol
	 */
	@NotNull
	Modifier multiline();
	
	/**
	 * @return modifier object with added the unicodeCase symbol
	 */
	@NotNull
	Modifier unicodeCase();
	
	/**
	 * @return modifier object with added the unicodeCharacter symbol
	 */
	@NotNull
	Modifier unicodeCharacter();
	
	/**
	 * @return modifier object with added the unixLines symbol
	 */
	@NotNull
	Modifier unixLines();
	
	/**
	 * @return modifier object with added the extended symbol
	 */
	@NotNull
	Modifier extended();
	
	/**
	 * @return only modifier string (iux vs.)
	 */
	@NotNull
	CharSequence getModifier();
	
	
}
