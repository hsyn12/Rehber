package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class Abstract implements WordExtension {
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar = word.charAt(word.length() - 1);
		char charMate = word.charAt(word.length() - 2);
		
		if (WordExtension.isVowelThin(lastChar)) {
			
			if (lastChar == 'ü' || lastChar == 'ö') {
				
				return resources.getString("abs_thin_uo");
			}
			else {
				
				return resources.getString("abs_thin");
			}
		}
		else if (WordExtension.isVowelThick(lastChar)) {
			
			if (lastChar == 'u' || lastChar == 'o') {
				
				return resources.getString("abs_thick_uo");
			}
			else {
				
				return resources.getString("abs_thick");
			}
		}
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			if (charMate == 'ü' || charMate == 'ö') return resources.getString("abs_thin_ou");
			else return resources.getString("abs_thin");
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			if (charMate == 'u' || charMate == 'o') return resources.getString("abs_thick_ou");
			else return resources.getString("abs_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			if (charMate == 'u' || charMate == 'o') return resources.getString("abs_thick_ou");
			else return resources.getString("abs_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate)) {
			if (charMate == 'ü' || charMate == 'ö') return resources.getString("abs_thin_ou");
			return resources.getString("abs_thin");
		}
		else return "";
	}
}
