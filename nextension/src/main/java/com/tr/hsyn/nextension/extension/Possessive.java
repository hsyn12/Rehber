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
				
				return "nün";
			}
			else {
				
				return "nin";
			}
		}
		else if (WordExtension.isVowelThick(lastChar)) {
			
			if (lastChar == 'u' || lastChar == 'o') {
				
				return "nun";
			}
			else {
				
				return "nın";
			}
		}
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			return "in";
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			return "ın";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			return "un";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate)) {
			
			if (charMate == 'ü' || charMate == 'ö') return "ün";
			return "in";
		}
		else return "";
	}
	
}
