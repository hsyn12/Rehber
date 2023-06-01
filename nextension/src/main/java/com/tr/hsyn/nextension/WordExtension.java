package com.tr.hsyn.nextension;


import com.tr.hsyn.nextension.extension.Ablative;
import com.tr.hsyn.nextension.extension.Abstract;
import com.tr.hsyn.nextension.extension.Accusative;
import com.tr.hsyn.nextension.extension.Dative;
import com.tr.hsyn.nextension.extension.Locative;
import com.tr.hsyn.nextension.extension.Plural;
import com.tr.hsyn.nextension.extension.Possessive;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


/**
 *
 */
public interface WordExtension {
	
	ResourceBundle resources = ResourceBundle.getBundle("strings");
	
	/**
	 * Bir harfin kalın ünlülerden biri olduğunu bildirir. ({@code ['a', 'ı', 'o', 'u']}'den biri)
	 */
	int             CHAR_TYPE_VOWEL_THICK    = 2000;
	/**
	 * Bir harfin ince ünlülerden biri olduğunu bildirir. ({@code ['e', 'i', 'ö', 'ü']}'den biri)
	 */
	int             CHAR_TYPE_VOWEL_THIN     = 2001;
	/**
	 * Bir harfin sert ünsüzlerden biri olduğunu bildirir. ({@code ['ç', 'f', 'h', 'k', 'p', 's', 'ş', 't', 'q']}'den biri)
	 */
	int             CHAR_TYPE_CONSONANT_HARD = 2002;
	/**
	 * Bir harfin yumuşak ünsüzlerden biri olduğunu bildirir. ({@code ['b', 'c', 'd', 'g', 'j', 'l', 'm', 'n', 'p', 'r', 'v', 'z', 'x', 'y', 'w']}'den biri)
	 */
	int             CHAR_TYPE_CONSONANT_SOFT = 2003;
	/**
	 * Bir harfin ünsüz harflerden biri olduğunu bildirir.
	 * Yani harf kalın yada ince ünlülerden biri değil demektir.
	 */
	int             CHAR_TYPE_ABC            = 2004;
	/**
	 * Bilinmeyen karakter türü
	 */
	int             CHAR_TYPE_UNKNOWN        = 2005;
	/**
	 * Kalın ünlü harfler {@code ['a', 'ı', 'o', 'u']}
	 */
	List<Character> VOWELS_THICK             = Arrays.asList('a', 'ı', 'o', 'u');
	/**
	 * İnce ünlü harfler {@code ['e', 'i', 'ö', 'ü']}
	 */
	List<Character> VOWELS_THIN              = Arrays.asList('e', 'i', 'ö', 'ü');
	/**
	 * Sert ünsüzler {@code ['ç', 'f', 'h', 'k', 'p', 's', 'ş', 't']}
	 */
	List<Character> CONSONANTS_HARD          = Arrays.asList('ç', 'f', 'h', 'k', 'p', 's', 'ş', 't');
	/**
	 * Yumuşak ünsüzler {@code ['b', 'c', 'd', 'g', 'j', 'l', 'm', 'n', 'r', 'v', 'z', 'x', 'y', 'w']}
	 */
	List<Character> CONSONANTS_SOFT          = Arrays.asList('b', 'c', 'd', 'g', 'j', 'l', 'm', 'n', 'r', 'v', 'z', 'x', 'y', 'w');
	
	static int getCharType(char c) {
		
		if (VOWELS_THICK.contains(c)) return CHAR_TYPE_VOWEL_THICK;
		if (VOWELS_THIN.contains(c)) return CHAR_TYPE_VOWEL_THIN;
		if (CONSONANTS_HARD.contains(c)) return CHAR_TYPE_CONSONANT_HARD;
		if (CONSONANTS_SOFT.contains(c)) return CHAR_TYPE_CONSONANT_SOFT;
		return CHAR_TYPE_UNKNOWN;
	}
	
	/**
	 * Kelimeye uygun son eklentiyi döndürür.<br><br>
	 * <p>
	 * what = {@link Extension#TYPE_ACCUSATIVE} için örnekler<br>
	 *
	 *
	 *    <ul>
	 *       <li>Ağaç    --> Ağaç'ı</li>
	 *       <li>Ahmet   --> Ahmet'i</li>
	 *       <li>Hasta   --> Hasta'yı</li>
	 *       <li>Özgür   --> Özgür'ü</li>
	 *       <li>Harun   --> Harun'u</li>
	 *       <li>Eskici  --> Eskici'yi</li>
	 *       <li>Uykucu  --> Uykucu'yu</li>
	 *       <li>Ütü     --> Ütü'yü</li>
	 *    </ul>
	 *
	 * <p>what = {@link Extension#TYPE_DATIVE} için örnekler<br>
	 *
	 * <ul>
	 *    <li>Ağaç    --> Ağaç'a</li>
	 *    <li>Ahmet   --> Ahmet'e</li>
	 *    <li>Hasta   --> Hasta'ya</li>
	 *    <li>Özgür   --> Özgür'e</li>
	 *    <li>Harun   --> Harun'a</li>
	 *    <li>Eskici  --> Eskici'ye</li>
	 *    <li>Uykucu  --> Uykucu'ya</li>
	 *    <li>Ütü     --> Ütü'ye</li>
	 * </ul>
	 * <p>
	 * what = {@link Extension#TYPE_ABLATIVE} için örnekler<br>
	 *
	 * <ul>
	 *    <li>Ağaç    --> Ağaç'tan</li>
	 *    <li>Ahmet   --> Ahmet'ten</li>
	 *    <li>Hasta   --> Hasta'dan</li>
	 *    <li>Özgür   --> Özgür'den</li>
	 *    <li>Harun   --> Harun'dan</li>
	 *    <li>Eskici  --> Eskici'den</li>
	 *    <li>Uykucu  --> Uykucu'dan</li>
	 *    <li>Ütü     --> Ütü'den</li>
	 * </ul>
	 *
	 * <p>what = {@link Extension#TYPE_POSSESSIVE} için örnekler<br>
	 *
	 * <ul>
	 *    <li>Ağaç    --> Ağaç'ın</li>
	 *    <li>Ahmet   --> Ahmet'in</li>
	 *    <li>Hasta   --> Hasta'nın</li>
	 *    <li>Özgür   --> Özgür'ün</li>
	 *    <li>Harun   --> Harun'un</li>
	 *    <li>Eskici  --> Eskici'nin</li>
	 *    <li>Uykucu  --> Uykucu'nun</li>
	 *    <li>Ütü     --> Ütü'nün</li>
	 * </ul>
	 *
	 * @param word kelime
	 * @param what eklentinin türü
	 * @return eklenti
	 */
	static @NotNull String getWordExt(@NotNull String word, int what) {
		
		return WordExtension.create(what).getExt(word);
	}
	
	/**
	 * Kelimeye uygun eki döndürür.
	 *
	 * @param word Kelime
	 * @return Kelimenin eki
	 */
	@NotNull String getExt(@NotNull String word);
	
	/**
	 * Ekin türüne göre uygun eklenti nesnesi oluşturur.
	 *
	 * @param type Ek türü (mesela {@link Extension#TYPE_LOCATIVE})
	 * @return {@link WordExtension} nesnesi
	 */
	@NotNull
	static WordExtension create(int type) {
		
		switch (type) {
			
			case Extension.TYPE_ABLATIVE: return new Ablative();
			case Extension.TYPE_DATIVE: return new Dative();
			case Extension.TYPE_POSSESSIVE: return new Possessive();
			case Extension.TYPE_ACCUSATIVE: return new Accusative();
			case Extension.TYPE_LOCATIVE: return new Locative();
			case Extension.TYPE_ABSTRACT: return new Abstract();
			case Extension.TYPE_PLURAL: return new Plural();
			
			default: throw new IllegalArgumentException("There is no extension class for type : " + type);
		}
	}
	
	/**
	 * Bir karakterin kalın ünlülerden biri olup olmadığını test eder.<br>
	 * Kalın ünlü harfler {@code ['a', 'ı', 'o', 'u']}
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter kalın ünlülerden biri ise {@code true}, aksi halde {@code false}
	 */
	static boolean isVowelThick(char c) {
		
		return VOWELS_THICK.contains(c);
	}
	
	/**
	 * Bir karakterin ince ünlülerden biri olup olmadığını test eder.<br>
	 * İnce ünlü harfler  {@code ['e', 'i', 'ö', 'ü']}
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter kalın ünlülerden biri ise {@code true}, aksi halde {@code false}
	 */
	static boolean isVowelThin(char c) {
		
		return VOWELS_THIN.contains(c);
	}
	
	/**
	 * Bir karakterin sert ünsüzlerden biri olup olmadığını test eder.<br>
	 * Sert ünsüzler {@code ['ç', 'f', 'h', 'k', 'p', 's', 'ş', 't']}
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter kalın ünlülerden biri ise {@code true}, aksi halde {@code false}
	 */
	static boolean isConsonantHard(char c) {
		
		return CONSONANTS_HARD.contains(c);
	}
	
	/**
	 * Bir karakterin yumuşak ünsüzlerden biri olup olmadığını test eder.<br>
	 * Yumuşak ünsüzler {@code ['b', 'c', 'd', 'g', 'j', 'l', 'm', 'n', 'p', 'r', 'v', 'z', 'x', 'y', 'w']}
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter kalın ünlülerden biri ise {@code true}, aksi halde {@code false}
	 */
	static boolean isConsonantSoft(char c) {
		
		return CONSONANTS_SOFT.contains(c);
	}
	
}
