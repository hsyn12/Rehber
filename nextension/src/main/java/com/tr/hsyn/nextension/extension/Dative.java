package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -ye -ya- -e -a (Yönelme Hali) (Datif - Kime, Nereye, Neye)
 */
public class Dative implements WordExtension {
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar     = word.charAt(word.length() - 1);
		char charMate     = word.charAt(word.length() - 2);
		int  lastCharType = WordExtension.getCharType(lastChar);
		
		switch (lastCharType) {
			
			case CHAR_TYPE_VOWEL_THIN: return "ye";
			case CHAR_TYPE_VOWEL_THICK: return "ya";
			case CHAR_TYPE_CONSONANT_HARD:
				if (WordExtension.isVowelThick(charMate)) return "a";
				else if (WordExtension.isVowelThin(charMate)) return "e";
				break;
			case CHAR_TYPE_CONSONANT_SOFT:
				if (WordExtension.isVowelThin(charMate)) return "e";
				else if (WordExtension.isVowelThick(charMate)) return "a";
				break;
		}
		
		return "";
	}
}
