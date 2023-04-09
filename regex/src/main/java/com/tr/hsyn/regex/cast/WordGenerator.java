package com.tr.hsyn.regex.cast;


import com.tr.hsyn.regex.Regex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Rastgele kelime üreticisi.<br>
 */
public class WordGenerator {
	
	public static final Random                   random             = new Random(System.currentTimeMillis());
	private final       Set<java.lang.Character> excludedCharacters = new HashSet<>();
	private final       Integer[]                regexCodes;
	private final       int                      length;
	
	/**
	 * Yeni bir nesne oluşturur.
	 *
	 * @param like          Üretilecek kelimeler için bir ipucu, üretici bu kelimenin harf türlerine bakarak benzer kelime üretir.
	 *                      {@code null} ise {@code laleli} kelimesini örnek olarak kullanır.
	 * @param length        Üretilecek kelimenin uzunluğu. {@code x <= 0} ise {@code 6} olarak belirlenir.
	 * @param excludedChars Üretilecek kelimede geçmesi istenmeyen harfler
	 */
	public WordGenerator(@Nullable String like, int length, @Nullable String excludedChars) {
		
		regexCodes  = Regex.Dev.toRegexCodes(like != null ? like : "laleli", true, false);
		this.length = (length > 0) ? length : (like != null) ? like.length() : 6;
		
		if (excludedChars != null && excludedChars.length() > 0)
			exclude(excludedChars);
	}
	
	/**
	 * @return Yeni bir kelime
	 */
	@NotNull
	public String getWord() {
		
		StringBuilder sb = new StringBuilder();
		
		int index = 0;
		
		while (sb.length() < length) {
			
			int    code    = regexCodes[index];
			String charSet = CharacterSet.getCharacterSet(code);
			char   c       = charSet.charAt(getNextIndex(charSet.length()));
			
			/* if (CharacterSet.isVowel(c)) {
				
				c = CharacterSet.getBigVowelHarmony(c);
			} */
			
			if (shouldExclude(c)) continue;
			
			sb.append(c);
			
			index++;
			
			if (index >= regexCodes.length) index = 0;
		}
		
		return sb.toString();
	}
	
	/**
	 * Yeni harf seçimi için karakter setinden bir index.
	 *
	 * @param bound Karakter setinin uzunluğu
	 * @return Index
	 */
	protected int getNextIndex(int bound) {
		
		return random.nextInt(bound);
	}
	
	/**
	 * Seçilen bir harfin hariç tutulup tutulmayacağını bildirir.
	 *
	 * @param c Harf
	 * @return {@code true} ise seçilen harf üretim dışı tutulur
	 */
	public boolean shouldExclude(char c) {
		
		return excludedCharacters.contains(c);
	}
	
	/**
	 * Üretimde örnek olarak kullanılan kelimenin regex kod karşılığı.
	 *
	 * @return Düzenli ifade kodları
	 */
	public Integer[] getRegexCodes() {
		
		return regexCodes;
	}
	
	/**
	 * @return Üretilecek kelimenin uzunluğu
	 */
	public int getLength() {
		
		return length;
	}
	
	/**
	 * Harflerin üretimde hariç tutulmasını sağlar.
	 *
	 * @param chars Üretimde hariç tutulacak harfler
	 */
	public void exclude(@NotNull String chars) {
		
		for (var c : chars.toCharArray())
			excludedCharacters.add(c);
		
	}
	
	
}
