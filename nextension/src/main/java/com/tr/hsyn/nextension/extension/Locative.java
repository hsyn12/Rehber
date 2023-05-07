package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)
 */
public class Locative implements WordExtension {
	
	public static void main(String[] args) {
		
		String[] names = {
				
				"Tahta", "Kalem", "Ev", "Otel", "Ayak", "Kale", "Pencere", "Dümen", "Telefon", "Çiçek", "Çarşaf", "Keşiş", "İn", "İç"
		};
		
		var ext = new Locative();
		
		for (var name : names) {
			
			System.out.printf("%s-%s\n", name, ext.getExt(name));
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
			
			case CHAR_TYPE_VOWEL_THIN: return resources.getString("at_thin");
			case CHAR_TYPE_VOWEL_THICK: return resources.getString("at_thick");
			case CHAR_TYPE_CONSONANT_HARD:
				if (WordExtension.isVowelThick(charMate)) return resources.getString("at_hard_thick");
				else if (WordExtension.isVowelThin(charMate))
					return resources.getString("at_hard_thin");
				break;
			case CHAR_TYPE_CONSONANT_SOFT:
				if (WordExtension.isVowelThin(charMate)) return resources.getString("at_soft_thin");
				else if (WordExtension.isVowelThick(charMate))
					return resources.getString("at_soft_thick");
				break;
		}
		
		return "";
		/* if (WordExtension.isVowelThin(lastChar)) return "de";
		else if (WordExtension.isVowelThick(lastChar)) return "da";
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			return "ta";
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			return "te";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate))
			return "de";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			return "da";
		else return ""; */
	}
	
	
}
