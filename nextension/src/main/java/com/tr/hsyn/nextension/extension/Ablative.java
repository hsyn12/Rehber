package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)
 */
public class Ablative implements WordExtension {
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar     = word.charAt(word.length() - 1);
		char charMate     = word.charAt(word.length() - 2);
		int  lastCharType = WordExtension.getCharType(lastChar);
		
		switch (lastCharType) {
			
			case CHAR_TYPE_VOWEL_THIN: return "den";
			case CHAR_TYPE_VOWEL_THICK: return "dan";
			case CHAR_TYPE_CONSONANT_HARD:
				if (WordExtension.isVowelThick(charMate)) return "tan";
				else if (WordExtension.isVowelThin(charMate)) return "ten";
				break;
			case CHAR_TYPE_CONSONANT_SOFT:
				if (WordExtension.isVowelThin(charMate)) return "den";
				else if (WordExtension.isVowelThick(charMate)) return "dan";
				break;
		}
		
		return "";
	}
}
