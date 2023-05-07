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
			
			case CHAR_TYPE_VOWEL_THIN: return resources.getString("to_thin");
			case CHAR_TYPE_VOWEL_THICK: return resources.getString("to_thick");
			case CHAR_TYPE_CONSONANT_HARD:
			case CHAR_TYPE_CONSONANT_SOFT:
				if (WordExtension.isVowelThick(charMate)) return resources.getString("to_hard_thick");
				else if (WordExtension.isVowelThin(charMate))
					return resources.getString("to_hard_thin");
				break;
		}
		
		return "";
	}
}
