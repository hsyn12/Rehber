package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -ler -lar (Çoğul eki)
 */
public class Plural implements WordExtension {
	
	public static void main(String[] args) {
		
		String[] words = {
				
				"kapı", "pencere", "kırmızı", "mavi", "siyah", "sarı"
		};
		
		var plural = new Plural();
		
		for (String word : words) {
			System.out.println(word + ": " + plural.getExt(word));
		}
	}
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar     = word.charAt(word.length() - 1);
		char charMate     = word.charAt(word.length() - 2);
		int  lastCharType = WordExtension.getCharType(lastChar);
		
		switch (lastCharType) {
			
			//den
			case CHAR_TYPE_VOWEL_THIN: return resources.getString("plu_thin");
			case CHAR_TYPE_VOWEL_THICK: return resources.getString("plu_thick");
			default:
				if (WordExtension.isVowelThick(charMate)) return resources.getString("plu_thick");
				else if (WordExtension.isVowelThin(charMate))
					return resources.getString("plu_thin");
		}
		
		return resources.getString("plu_thick");
	}
}
