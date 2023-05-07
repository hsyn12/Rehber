package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -in -ün -nin -nün -un -ın (İyelik Eki)
 */
public class Possessive implements WordExtension {
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar = word.charAt(word.length() - 1);
		char charMate = word.charAt(word.length() - 2);
		
		if (WordExtension.isVowelThin(lastChar)) {
			
			if (lastChar == 'ü' || lastChar == 'ö') {
				
				return resources.getString("poss_thin_uo");
			}
			else {
				
				return resources.getString("poss_thin");
			}
		}
		else if (WordExtension.isVowelThick(lastChar)) {
			
			if (lastChar == 'u' || lastChar == 'o') {
				
				return resources.getString("poss_thick_uo");
			}
			else {
				
				return resources.getString("poss_thick");
			}
		}
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			return resources.getString("poss_hard_thin");
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			return resources.getString("poss_hard_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			return resources.getString("poss_soft_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate)) {
			
			if (charMate == 'ü' || charMate == 'ö') return resources.getString("poss_soft_thin_uo");
			return resources.getString("poss_soft_thin");
		}
		else return "";
	}
	
}
