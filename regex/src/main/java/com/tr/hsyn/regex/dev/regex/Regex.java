package com.tr.hsyn.regex.dev.regex;


import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.character.Character;
import com.tr.hsyn.regex.dev.regex.character.Digit;
import com.tr.hsyn.regex.dev.regex.character.Letter;
import com.tr.hsyn.regex.dev.regex.character.Punctuation;
import com.tr.hsyn.regex.dev.regex.character.WhiteSpace;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexDigit;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexLetter;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexPunctuation;
import com.tr.hsyn.regex.dev.regex.character.cast.RegexWhiteSpace;


public interface Regex extends Text {
	
	
	Letter LETTER     = new RegexLetter(Character.LETTER);
	Letter NON_LETTER = new RegexLetter(Character.NON_LETTER);
	
	Digit DIGIT     = new RegexDigit(Character.DIGIT);
	Digit NON_DIGIT = new RegexDigit(Character.NON_DIGIT);
	
	WhiteSpace  WHITE_SPACE     = new RegexWhiteSpace(Character.WHITE_SPACE);
	WhiteSpace  NON_WHITE_SPACE = new RegexWhiteSpace(Character.NON_WHITE_SPACE);
	Punctuation PUNCTUATION     = new RegexPunctuation(Character.PUNCTUATION);
	Punctuation NON_PUNCTUATION = new RegexPunctuation(Character.NON_PUNCTUATION);
}
