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
			
			//den
			case CHAR_TYPE_VOWEL_THIN: return resources.getString("from_thin");
			case CHAR_TYPE_VOWEL_THICK: return resources.getString("from_thick");
			case CHAR_TYPE_CONSONANT_HARD:
				if (WordExtension.isVowelThick(charMate)) return resources.getString("from_hard_thick");
				else if (WordExtension.isVowelThin(charMate))
					return resources.getString("from_hard_thin");
				break;
			case CHAR_TYPE_CONSONANT_SOFT:
				if (WordExtension.isVowelThin(charMate)) return resources.getString("from_soft_thin");
				else if (WordExtension.isVowelThick(charMate))
					return resources.getString("from_soft_thick");
				break;
		}
		
		return "";
	}
}
