package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)
 */
public class Accusative implements WordExtension {
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar = word.charAt(word.length() - 1);
		char charMate = word.charAt(word.length() - 2);
		
		if (WordExtension.isVowelThin(lastChar)) {
			
			if (lastChar == 'ü' || lastChar == 'ö') {
				
				return "yü";
			}
			else {
				
				return "yi";
			}
		}
		else if (WordExtension.isVowelThick(lastChar)) {
			
			if (lastChar == 'u' || lastChar == 'o') {
				
				return "yu";
			}
			else {
				
				return "yı";
			}
		}
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			return "i";
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			return "ı";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			return "u";
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate)) {
			
			if (charMate == 'ü') return "ü";
			return "i";
		}
		else return "";
		
	}
	
	public static void main(String[] args) {
		
		String[] names = {
				
				"Tahta", "Kalem", "Ev", "Otel", "Ayak", "Kale", "Pencere",
				"Dümen", "Telefon", "Çiçek", "Çarşaf", "Keşiş", "İn", "İç", "Dolu", "Gül"
		};
		
		var ext = new Accusative();
		
		for (var name : names) {
			
			System.out.printf("%s-%s\n", name, ext.getExt(name));
		}
		
	}
}
