package com.tr.hsyn.string;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Digit;
import com.tr.hsyn.regex.dev.regex.character.Letter;
import com.tr.hsyn.regex.dev.regex.character.WhiteSpace;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * String işlemleri için fonksiyonlar içerir.
 */
public final class Stringx {
	
	public static final WhiteSpace WHITE_SPACE = Regex.WHITE_SPACE;
	public static final Letter     LETTER      = Regex.LETTER;
	public static final Digit      DIGIT       = Regex.DIGIT;
	
	private Stringx() {
		
	}
	
	/**
	 * Combines array elements with the specified character.
	 *
	 * @param collection array to combine
	 * @param delimiter  delimiter
	 * @param <T>        type of array
	 * @return combined string
	 */
	@NotNull
	public static <T> String joinToString(T @NotNull [] collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(collection);
	}
	
	/**
	 * Combines list elements into a string.
	 *
	 * @param collection list
	 * @param delimiter  delimiter
	 * @param <T>        type of list
	 * @return combined string
	 */
	@NotNull
	public static <T> String joinToString(@NotNull List<? extends T> collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(collection);
	}
	
	/**
	 * Combines list elements into a string. The join character is a comma.
	 *
	 * @param collection list
	 * @param <T>        type of list
	 * @return combined string
	 */
	@NotNull
	public static <T> String joinToString(List<? extends T> collection) {
		
		if (collection == null) return "";
		
		return Joiner.on(',').join(collection);
	}
	
	/**
	 * Combines elements into a string.
	 * The join character is a comma.
	 *
	 * @param collection elements
	 * @param <T>        type of element
	 * @return combined string
	 */
	@NotNull
	public static <T> String joinToString(@NotNull Iterable<? extends T> collection) {
		
		return Joiner.on(',').join(collection);
	}
	
	/**
	 * Combines list elements into a string. The join character is a comma.
	 *
	 * @param collection list
	 * @return combined string
	 */
	@NotNull
	public static String joinToString(int[] collection) {
		
		return Joiner.on(',').join(Collections.singletonList(collection));
	}
	
	/**
	 * Combines list elements into a string. The join character is a comma.
	 *
	 * @param collection list
	 * @return combined string
	 */
	@NotNull
	public static String joinToString(long[] collection) {
		
		return Joiner.on(',').join(Collections.singletonList(collection));
	}
	
	/**
	 * Combines list elements into a string.
	 *
	 * @param collection list
	 * @return combined string
	 */
	@NotNull
	public static String joinToString(int[] collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(Collections.singletonList(collection));
	}
	
	/**
	 * Split a string. The split character is a comma.
	 *
	 * @param value string to split
	 * @return the split elements
	 */
	@NotNull
	public static Iterable<String> split(@NotNull String value) {
		
		return Splitter.on(",").omitEmptyStrings().trimResults().split(value);
	}
	
	/**
	 * Split a string.
	 *
	 * @param value string to split
	 * @return the split elements
	 */
	@NotNull
	public static Iterable<String> split(@NotNull String value, String delimiter) {
		
		return Splitter.on(delimiter).omitEmptyStrings().trimResults().split(value);
	}
	
	/**
	 * Returns the first character of the string.
	 * If string is empty or {@code null}, an empty string is returned.
	 *
	 * @param str string to get first character
	 * @return first character
	 */
	@NotNull
	public static String getFirstChar(String str) {
		
		if (str == null) {str = "";}
		
		str = trimWhiteSpaces(str);
		
		if (str.isEmpty()) {return "";}
		
		return String.valueOf(str.charAt(0));
	}
	
	/**
	 * Deletes all space characters in the string. {@code [\r\n\t\f\v]} ve {@code ' '} space.
	 *
	 * @param str String
	 * @return A contiguous string without spaces. If the given string is {@code null}, then empty string
	 */
	@NotNull
	public static String trimWhiteSpaces(String str) {
		
		if (str != null) return WHITE_SPACE.removeFrom(str);
		
		return "";
	}
	
	@NotNull
	public static Tester test(@NotNull String text) {
		
		return new Tester(text);
	}
	
	/**
	 * Verilen string'in baş harfini büyütür.
	 *
	 * @param text String
	 * @return Baş harfi büyük string
	 */
	@NotNull
	public static String toTitle(@NotNull String text) {
		
		StringBuilder builder = new StringBuilder(text);
		
		char c = text.charAt(0);
		
		if (c == 'i') c = 'İ';
		else c = Character.toUpperCase(c);
		
		builder.setCharAt(0, c);
		
		return builder.toString();
	}
	
	/**
	 * Verilen string'i formatla. Aynen {@link String#format(String, Object...)} fonksiyonu gibi.
	 * Tek farkı, dil olarak türkçe kullanması.
	 *
	 * @param text text
	 * @param args args
	 * @return text
	 */
	public static @NotNull String format(CharSequence text, Object... args) {
		
		return String.format(Locale.getDefault(), text != null ? text.toString() : "null", args);
	}
	
	/**
	 * Formats the given string.
	 *
	 * <pre>val value = Stringx.from("%d. name='$name', number='%s', type='$type', date='%s'%n")
	 *                                     .arg(1, i)
	 *                                     .key("name", name)
	 *                                     .arg(2, noted.getNumber())
	 *                                     .key("type", Type.getTypeStr(noted.getType()))
	 *                                     .arg(3, Time.getDate(noted.getDate()))
	 *                                     .format();</pre>
	 *
	 * @param str string
	 * @return string
	 */
	public static IFormatter from(String str) {
		
		return IFormatter.create(str);
	}
	
	public static boolean _isMatch(String word, String searchText) {
		
		if (word == null || searchText == null) return false;
		
		if (word.length() < searchText.length()) return false;
		
		return indexOfMatches(word, searchText).length != 0;
	}
	
	/**
	 * Bir kelimenin içinde başka bir kelimenin geçtiği yerlerin başlangıç ve bitiş index'lerini verir.<br>
	 * Eşleşme yapılırken boşluklar da hesaplanır.<br>
	 * Mesela '543493' içinde ' 5 4 3 ' aranıyorsa eşleşecek ve [0,3] dizisi dönecek.<br>
	 * Mesela '5 4 3 493' içinde '543' aranıyorsa eşleşecek ve [0,6] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'ua' aranıyorsa eşleşecek ve [4, 7] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'u   a' aranıyorsa eşleşecek ve yine [4, 7] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'U' aranıyorsa eşleşecek ve [1, 2, 4, 5] dizisi dönecek.<br><br>
	 * <p>
	 * Arama, eşleşme bulunca sonlanmaz, ne kadar eşleşme varsa bulmaya devam eder.
	 * Dönen eşleşme dizisi, ikili eşler şeklinde düşünülmeli.
	 * Her bir ikili, eşleşmenin başlama (dahil) ve bitiş (hariç) index'ini bildirir.
	 *
	 * @param word       Aramanın yapılacağı kelime
	 * @param searchText Aranacak kelime
	 * @return Eşleşme olmazsa boş dizi
	 */
	@NotNull
	public static Integer[] indexOfMatches(String word, String searchText) {
		
		return indexOfMatches(word, searchText, true);
	}
	
	/**
	 * Bir kelimenin içinde başka bir kelimenin geçtiği yerlerin başlangıç ve bitiş index'lerini verir.<br>
	 * Eşleşme yapılırken boşluklar da hesaplanır.<br>
	 * Mesela '543493' içinde ' 5 4 3 ' aranıyorsa eşleşecek ve [0,3] dizisi dönecek.<br>
	 * Mesela '5 4 3 493' içinde '543' aranıyorsa eşleşecek ve [0,6] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'ua' aranıyorsa eşleşecek ve [4, 7] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'u   a' aranıyorsa eşleşecek ve yine [4, 7] dizisi dönecek.<br>
	 * Mesela 'Mulu Acar' içinde 'U' aranıyorsa eşleşecek ve [1, 2, 4, 5] dizisi dönecek.<br><br>
	 * <p>
	 * Arama, eşleşme bulunca sonlanmaz, ne kadar eşleşme varsa bulmaya devam eder.
	 * Dönen eşleşme dizisi, ikili eşler şeklinde düşünülmeli.
	 * Her bir ikili, eşleşmenin başlama (dahil) ve bitiş (hariç) index'ini bildirir.
	 *
	 * @param word       Aramanın yapılacağı kelime
	 * @param searchText Aranacak kelime
	 * @param ignoreCase Karakter küçük/büyük duyarlılığı
	 * @return Eşleşme olmazsa boş dizi
	 */
	public static Integer @NotNull [] indexOfMatches(String word, String searchText, boolean ignoreCase) {
		
		if (Regex.isNoboe(word) || Regex.isNoboe(searchText)) return new Integer[0];
		
		searchText = Regex.removeWhiteSpaces(searchText);
		
		com.tr.hsyn.regex.cast.@NotNull RegexBuilder reg = Nina.whiteSpace().zeroOrMore();
		
		if (ignoreCase) reg = reg.ignoreCase();
		
		for (int i = 0; i < searchText.length(); i++)
		     reg = reg.with(Nina.whiteSpace().zeroOrMore().with(searchText.charAt(i)));
		
		List<com.tr.hsyn.regex.cast.Index> indexes = reg.findAll(word);
		
		Integer[] result = new Integer[indexes.size() * 2];
		
		int x = 0;
		for (com.tr.hsyn.regex.cast.Index i : indexes) {
			
			result[x++] = i.start;
			result[x++] = i.end;
		}
		
		return result;
	}
	
	/**
	 * Kelimenin ({@code word}) içinde başka bir kelime ({@code searchText}) geçiyor mu?
	 * Yapılacak olan karşılaştırmanın özelliği string içindeki boşlukları da
	 * hesaba katması. Yani boşluklar eşitliği bozmaz.<br>
	 * <p>
	 * Mesela {@code '543'}  ile {@code ' 5 4 3 '} karşılaştırması {@code true} sonucu verir.
	 *
	 * @param word       Aramanın yapılacağı string
	 * @param searchText Aranacak string
	 * @return {@code word} içinde {@code searchText} geçiyorsa {@code true}
	 */
	public static boolean isMatch(String word, String searchText) {
		
		return isMatch(word, searchText, true);
	}
	
	/**
	 * Kelimenin ({@code word}) içinde başka bir kelime ({@code searchText}) geçiyor mu?
	 * Yapılacak olan karşılaştırmanın özelliği string içindeki boşlukları da
	 * hesaba katması.  Yani boşluklar eşitliği bozmaz.<br>
	 * <p>
	 * Mesela {@code '543'}  ile {@code ' 5 4 3 '} karşılaştırması {@code true} sonucu verir.
	 *
	 * @param word       Aramanın yapılacağı string
	 * @param searchText Aranacak string
	 * @param ignoreCase Karakterlerin büyük/küçük olması önemsizse {@code true}
	 * @return {@code word} içinde {@code searchText} geçiyorsa {@code true}
	 */
	public static boolean isMatch(String word, String searchText, boolean ignoreCase) {
		
		if (isNoboe(word) || isNoboe(searchText)) return false;
		
		if (word.length() < searchText.length()) return false;
		
		searchText = Regex.removeWhiteSpaces(searchText);
		
		com.tr.hsyn.regex.cast.@NotNull RegexBuilder reg = Nina.regex().whiteSpace().zeroOrMore().lazy();
		
		if (ignoreCase) reg.with(Modifier.modify().ignoreCase());
		
		for (int i = 0; i < searchText.length(); i++) {
			
			reg.with(searchText.charAt(i))
					.whiteSpace()
					.zeroOrMore()
					.lazy();
		}
		
		return reg.existIn(word);
		
		//return indexOfMatches(word, searchText, ignoreCase).length != 0;
	}
	
	/**
	 * Bir yazının varlığını test eder.
	 *
	 * @param str Yazı
	 * @return Eğer yazı <code>null</code> veya boşluk veya boş ise <code>true</code>
	 * 		(<strong>N</strong>ull <strong>O</strong>r <strong>B</strong>lank <strong>O</strong>r <strong>E</strong>mpty)
	 */
	public static boolean isNoboe(String str) {
		
		return str == null || WHITE_SPACE.removeFrom(str).isEmpty();
	}
	
	@NotNull
	public static Integer[] _indexOfMatches(String word, String searchText) {
		
		//? Gelen string'lerden herhangi biri null ya da boş ise boş dizi dönecek
		if (word == null || searchText == null ||
		    word.trim().isEmpty() || searchText.trim().isEmpty()) return new Integer[0];
		
		//! Aranan string içinde boş karakter olmayacak
		searchText = CharMatcher.whitespace().removeFrom(searchText).toLowerCase();
		word       = word.toLowerCase();
		
		//- Aranan kelime bitişik, aramanın yapıldığı kelime ise boşluklu.
		//- Aramanın yapıldığı kelimenin boşluklu (varsa tabi) kalması index'lerin
		//- doğru olmasını sağlıyor.
		//- Yani arama yaparken girilen kelime boşluklu da girilse bitişik de girilse
		//- diğer kelimede yine aynı yeri bulacak (eşleşme varsa yani).
		//- Ve biz ilk kelime içinde arama yaptığımız için ikinci kelimenin boşlukları
		//- hiç sorun teşkil etmiyor index'leri bulurken, aksine esneklik sağlıyor.
		
		//Aramanın yapıldığı string doğal olarak daha uzun olmak zorunda
		if (word.length() < searchText.length()) return new Integer[0];
		
		//Devam
		int  sIndex = 0, wIndex = 0;//? sIndex searchText için, wIndex ise word için index görevi yapacak
		char searchChar, wordChar;//? searchChar, searchText'ten ilerleyecek, wordChar ise word'dan ilerleyecek
		
		//- sEnd ve wEnd string'lerin sonunu gösterecek
		final int           sEnd       = searchText.length();
		final int           wEnd       = word.length();
		final List<Integer> indexes    = new ArrayList<>();//eşleşmenin olduğu index'ler
		int                 matchCount = 0;
		
		while (true) {
			
			//? Eğer iki string'in de sonuna gelmişsek iş bitmiş demektir.
			if (sIndex == sEnd && wIndex == wEnd) {
				
				//? Ama bitirmeden önce eşleşme başlamışsa bitiş index'ini ekle
				if (matchCount == sEnd) {//- Tümü eşleşmiş ise
					
					indexes.add(wIndex);
				}
				else {//- Tümü eşleşmemiş
					
					if (matchCount == 0 || indexes.isEmpty()) break;
					
					//- Eğer eşleşme başlamış ama sonuçlanmamışsa listeden çıkarmalıyız
					indexes.remove(indexes.size() - 1);
				}
				
				break; // İş biter bayım
			}
			
			// searchText'in sonuna gelmişsek başlamış eşleşme varsa bitecek
			// yoksa ve hala word'un sonuna gelmemişsek searchText'in başına dönüp
			// varsa diğer eşleşmeleri arayacak
			if (sIndex == sEnd) {
				
				if (matchCount == sEnd) {//Tam eşleşme var
					
					indexes.add(wIndex);
				}
				else {
					
					if (matchCount > 0) {
						
						if (!indexes.isEmpty())
							indexes.remove(indexes.size() - 1);
					}
					
				}
				
				//Aranan string'in sonuna geldik, başa dönüp diğer eşleşmeler kontrol edilecek
				matchCount = sIndex = 0;
				wIndex++;//Ama aramanın yapıldığı string tabiki bir ileri gidecek
				continue;//Buradan dönmemiz gerek
			}
			
			// eğer word'un sonuna gelmişsek iş bitmiştir
			if (wIndex == wEnd) {
				
				if (matchCount == sEnd) {
					
					indexes.add(wIndex);
				}
				else {
					
					if (matchCount == 0) break;
					if (indexes.isEmpty()) break;
					
					indexes.remove(indexes.size() - 1);
				}
				
				break;//Bitir
			}
			
			//Kontrol burada başlıyor
			
			searchChar = searchText.charAt(sIndex);
			wordChar   = word.charAt(wIndex);
			
			
			if (!Character.isSpaceChar(wordChar)) {
				
				if (searchChar == wordChar) {
					
					if (sIndex == 0) {
						
						indexes.add(wIndex);
					}
					
					// eşleşme olduğu sürece ilerleyerek devam et
					sIndex++;
					wIndex++;
					matchCount++;
				}
				else {//Eşleşme yok
					
					if (matchCount != 0) {
						
						if (!indexes.isEmpty())
							indexes.remove(indexes.size() - 1);
						
					}
					else wIndex++;
					
					sIndex = matchCount = 0;
				}
			}
			else wIndex++;
		}
		
		return indexes.toArray(new Integer[0]);
	}
	
	/**
	 * Masks the given string with the asterisk (*) character, except for the first and last two characters.<br>
	 * For example, the word 'ahmet' becomes 'ah*et'.<br>
	 * For example, the word 'ahmet bey' becomes 'ah*** *ey'.<br><br>
	 * <p>
	 * If the given string is {@code null}, it returns a 'null' string.
	 * If the given string is empty, a single asterisk returns the string '*'.
	 * If the given string is less than 3 characters, the asterisk '*' string returns as many characters as the number of characters.<br>
	 * If the given string is 3 characters, for example, 'ali' only the middle character becomes an asterisk, 'a*i'<br>
	 * If the given string is 4 characters, for example, the middle characters become asterisks, 'a**a'<br>
	 * In the remaining cases, all but the first and last two characters become stars.
	 *
	 * @param str string to mask
	 * @return masked string
	 */
	public static String overWrite(String str) {
		
		return overWrite(str, '*');
	}
	
	/**
	 * Masks the string with the specified character.
	 *
	 * @param str string
	 * @param c   mask character
	 * @return masked string
	 * @see #overWrite(String)
	 */
	public static String overWrite(String str, char c) {
		
		if (str == null) return "null";
		
		if (str.isEmpty()) return String.valueOf(c);
		
		if (str.length() < 3) return repeat(c, str.length());
		
		StringBuilder stringBuilder = new StringBuilder(str);
		
		if (str.length() == 3) {
			
			stringBuilder.setCharAt(1, c);
			
			return stringBuilder.toString();
		}
		
		if (str.length() == 4) {
			
			stringBuilder.setCharAt(1, c);
			stringBuilder.setCharAt(2, c);
			
			return stringBuilder.toString();
		}
		
		for (int i = 2; i < stringBuilder.length() - 2; i++) {
			
			if (!Character.isSpaceChar(stringBuilder.charAt(i))) {
				
				stringBuilder.setCharAt(i, c);
			}
		}
		
		return stringBuilder.toString();
	}
	
	@NotNull
	public static String repeat(char c, int count) {
		
		StringBuilder sb = new StringBuilder();
		
		//noinspection StringRepeatCanBeUsed
		for (int i = 0; i < count; i++)
		     sb.append(c);
		
		return sb.toString();
	}
	
	/**
	 * Tests whether one passes inside the other by comparing two given strings.
	 * <p>
	 * For example, 'EL' matches the word 'hello'.
	 * There is no case of discrimination in matches.
	 *
	 * @param s1 s1
	 * @param s2 s2
	 * @return {@code true} if matches
	 */
	public static boolean matchContains(String s1, String s2) {
		
		if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) return false;
		
		s1 = s1.replaceAll("I", "i");
		s2 = s2.replaceAll("I", "i");
		
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		
		final int l1 = s1.length();
		final int l2 = s2.length();
		
		if (l1 == l2) return s1.equals(s2);
		
		if (l1 < l2) return s2.contains(s1);
		
		return s1.contains(s2);
	}
	
	/**
	 * Checks whether the given string is a lower case letter.
	 *
	 * @param string the string
	 * @return {@code true} if the string is a lower case letter
	 */
	public static boolean isLowerCase(@NotNull String string) {
		
		for (char c : string.toCharArray()) {
			
			if (Character.isUpperCase(c)) return false;
		}
		
		return true;
	}
	
	/**
	 * Checks whether the given string is null or blank.
	 *
	 * @param str the string
	 * @return {@code true} if the string is null or blank
	 */
	public static boolean isNullOrBlank(String str) {
		
		return str == null || isBlank(str);
	}
	
	/**
	 * Checks whether the given string is blank.
	 *
	 * @param str the string
	 * @return {@code true} if the string is blank
	 */
	public static boolean isBlank(@NotNull String str) {
		
		return str.trim().isEmpty();
	}
	
	/**
	 * Returns the upper case version of the given string.
	 *
	 * @param str the string
	 * @return the upper case version of the given string
	 */
	@NotNull
	public static String toUpper(@NotNull String str) {
		
		char[] chars = str.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			
			char c = chars[i];
			
			if (c != 'i' && c != 'ı') chars[i] = Character.toUpperCase(c);
			else if (c == 'i') chars[i] = 'İ';
			else chars[i] = 'I';
		}
		
		return new String(chars);
	}
	
	/**
	 * Returns the lower case version of the given string.
	 *
	 * @param str the string
	 * @return the lower case version of the given string
	 */
	@NotNull
	public static String toLower(@NotNull String str) {
		
		char[] chars = str.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			
			char c = chars[i];
			
			if (c != 'I' && c != 'İ') chars[i] = Character.toLowerCase(c);
			else if (c == 'İ') chars[i] = 'i';
			else chars[i] = 'ı';
		}
		
		return new String(chars);
	}
	
	/**
	 * Returns the capitalized version of the given string.
	 *
	 * @param str the string
	 * @return the capitalized version of the given string
	 */
	@NotNull
	public static String toCapital(@NotNull String str) {
		
		if (str.trim().isEmpty()) return str;
		
		char first = str.charAt(0);
		
		if (first == 'i') first = 'I';
		else first = Character.toUpperCase(first);
		
		return first + str.substring(1);
	}
	
	/**
	 * Returns a string with the given number of repetitions for the given string.
	 *
	 * @param s     the string
	 * @param count the number of repetitions
	 * @return a string with the given number of repetitions for the given string.
	 */
	@NotNull
	public static String repeat(@NotNull String s, int count) {
		
		StringBuilder sb = new StringBuilder();
		
		//noinspection StringRepeatCanBeUsed
		for (int i = 0; i < count; i++) sb.append(s);
		
		return sb.toString();
	}
	
	/**
	 * Deletes all characters in the string except the number.
	 *
	 * @param str string
	 * @return a contiguous no spaces string consisting only of numbers. Empty string if no number exists.
	 */
	@NotNull
	public static String trimNonDigits(@NotNull String str) {
		
		return str.replaceAll("[^0-9]", "");
	}
	
}

