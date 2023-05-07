package com.tr.hsyn.nextension.extension;


import com.tr.hsyn.nextension.WordExtension;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)
 */
public class Accusative implements WordExtension {
	
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
	
	@Override
	public @NotNull String getExt(@NotNull String word) {
		
		word = word.toLowerCase(Locale.getDefault());
		
		if (word.length() < 2) return "";
		
		char lastChar = word.charAt(word.length() - 1);
		char charMate = word.charAt(word.length() - 2);
		
		if (WordExtension.isVowelThin(lastChar)) {
			
			if (lastChar == 'ü' || lastChar == 'ö') {
				
				return resources.getString("into_thin_uo");
			}
			else {
				
				return resources.getString("into_thin");
			}
		}
		else if (WordExtension.isVowelThick(lastChar)) {
			
			if (lastChar == 'u' || lastChar == 'o') {
				
				return resources.getString("into_thick_uo");
			}
			else {
				
				return resources.getString("into_thick");
			}
		}
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThin(charMate))
			return resources.getString("into_hard_thin");
		else if (WordExtension.isConsonantHard(lastChar) && WordExtension.isVowelThick(charMate))
			return resources.getString("into_hard_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThick(charMate))
			return resources.getString("into_soft_thick");
		else if (WordExtension.isConsonantSoft(lastChar) && WordExtension.isVowelThin(charMate)) {
			
			if (charMate == 'ü') return resources.getString("into_soft_thin_u");
			return resources.getString("into_soft_thin");
		}
		else return "";
		
	}
}
