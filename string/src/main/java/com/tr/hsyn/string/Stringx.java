package com.tr.hsyn.string;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Index;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.Regex;
import com.tr.hsyn.regex.dev.regex.character.Digit;
import com.tr.hsyn.regex.dev.regex.character.Letter;
import com.tr.hsyn.regex.dev.regex.character.WhiteSpace;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Provides a set of static methods and constants for working with strings.
 */
public final class Stringx {
	
	/**
	 * Regular expression that represents a white space.
	 */
	public static final WhiteSpace WHITE_SPACE = Regex.WHITE_SPACE;
	/**
	 * Regular expression that represents a letter.
	 */
	public static final Letter     LETTER      = Regex.LETTER;
	/**
	 * Regular expression that represents a digit.
	 */
	public static final Digit      DIGIT       = Regex.DIGIT;
	
	private Stringx() {
		
	}
	
	/**
	 * Combines elements into a string.
	 * The joining character is the comma.
	 *
	 * @param collection elements
	 * @param <T>        type of element
	 * @return combined string
	 */
	@NotNull
	public static <T> String join(@NotNull Iterable<? extends T> collection) {
		
		return join(collection, ",");
	}
	
	/**
	 * Combines elements into a string.
	 *
	 * @param collection elements
	 * @param delimiter  delimiter
	 * @param <T>        type of element
	 * @return combined string
	 */
	@NotNull
	public static <T> String join(@NotNull Iterable<? extends T> collection, String delimiter) {
		
		return Joiner.on(delimiter != null ? delimiter : ",").join(collection);
	}
	
	/**
	 * Split a string. The split character is a comma.
	 *
	 * @param string string to split
	 * @return the split elements
	 */
	@NotNull
	public static Iterable<String> split(@NotNull String string) {
		
		return split(string, ",");
	}
	
	/**
	 * Split a string.
	 *
	 * @param string    string to split
	 * @param delimiter delimiter
	 * @return the split elements
	 */
	@NotNull
	public static Iterable<String> split(@NotNull String string, String delimiter) {
		
		return Splitter.on(delimiter != null ? delimiter : ",")
				.omitEmptyStrings()
				.trimResults()
				.split(string);
	}
	
	/**
	 * Returns the first letter of the string.
	 *
	 * @param str string
	 * @return if the string is empty or {@code null}, then '?' is returns, or the first letter.
	 */
	public static @NotNull String getLetter(String str) {
		
		if (Stringx.isNoboe(str)) return "?";
		
		String l = String.valueOf(str.trim().charAt(0));
		
		if (Character.isAlphabetic(l.charAt(0))) return l.toUpperCase(Locale.getDefault());
		
		return l;
	}
	
	/**
	 * Removes all space characters in the string. {@code [\r\n\t\f\v]} ve {@code ' '} space.
	 *
	 * @param str string
	 * @return a contiguous string without spaces. If the given string is {@code null}, then empty string.
	 */
	@NotNull
	public static String removeAllWhiteSpaces(String str) {
		
		if (str != null) return WHITE_SPACE.removeFrom(str);
		
		return "";
	}
	
	/**
	 * Test if a string is consists of only digits.
	 *
	 * @param str the string to test
	 * @return {@code true} if the string is consists of only digits, false otherwise.
	 */
	public static boolean isNumber(@NotNull String str) {
		
		return str.matches(Regex.NUMBER);
	}
	
	/**
	 * Test if a string is not consists of only digits.
	 *
	 * @param str the string to test
	 * @return {@code true} if the string is not consists of only digits, false otherwise.
	 */
	public static boolean isNotNumber(@NotNull String str) {
		
		return !isNumber(str);
	}
	
	/**
	 * Converts a string to a title case.
	 *
	 * @param text string
	 * @return title cased string
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
	 * Formats the given string like {@link String#format(String, Object...)}.
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
	 *     .arg(1, i)
	 *     .key("name", name)
	 *     .arg(2, noted.getNumber())
	 *     .key("type", Type.getTypeStr(noted.getType()))
	 *     .arg(3, Time.getDate(noted.getDate()))
	 *     .format();</pre>
	 *
	 * @param str string
	 * @return string
	 */
	@NotNull
	public static IFormatter from(String str) {
		
		return IFormatter.create(str);
	}
	
	/**
	 * Returns the start and end indexes of places in a text where another text occurs.<br>
	 * When matching, blanks are calculated.<br>
	 * <p>
	 * For example, if ' 5 4 3' is searched for in '543493'
	 * it will match, and the sequence {@code [0, 3]} will return.<br>
	 * For example, if '543' is searched for in '5 4 3 493',
	 * it will match and the sequence [0, 6] will be returned.<br>
	 * For example, if 'ua' is searched in 'Mutlu Acar',
	 * it will match and the sequence [4, 7] will return.<br>
	 * For example, if 'u a' is searched in 'Mutlu Acar',
	 * it will match and the sequence [4, 7] will return.<br>
	 * For example, if 'U' is searched in 'Mutlu Acar',
	 * it will match and the sequence [1, 2, 4, 5] will return.<br><br>
	 * <p>
	 * The search doesn't end when it finds a match, it continues to find as many matches as there are.
	 *
	 * @param word       the text that searched in
	 * @param searchText search text
	 * @return the start and end indexes if matches
	 */
	public static List<Index> findIndexes(String word, String searchText) {
		
		return findIndexes(word, searchText, true);
	}
	
	/**
	 * Returns the start and end indexes of places in a text where another text occurs.<br>
	 * When matching, blanks are calculated.<br>
	 * For example, if ' 5 4 3' is searched for in '543493'
	 * it will match, and the sequence [0, 3] will return.<br>
	 * For example, if '543' is searched for in '5 4 3 493',
	 * it will match and the sequence [0,6] will be returned.<br>
	 * For example, if 'ua' is searched in 'Mutlu Acar',
	 * it will match and the sequence [4, 7] will return.<br>
	 * For example, if 'u a' is searched in 'Mutlu Acar',
	 * it will match and the sequence [4, 7] will return.<br>
	 * For example, if 'U' is searched in 'Mutlu Acar',
	 * it will match and the sequence [1, 2, 4, 5] will return.<br><br>
	 * <p>
	 * The search doesn't end when it finds a match, it continues to find as many matches as there are.
	 *
	 * @param word       the text that searched in
	 * @param searchText search text
	 * @param ignoreCase ignore case
	 * @return the start and end indexes if matches
	 */
	public static List<Index> findIndexes(String word, String searchText, boolean ignoreCase) {
		
		if (Regex.isNoboe(word) || Regex.isNoboe(searchText)) return new ArrayList<>(0);
		
		searchText = Regex.removeWhiteSpaces(searchText);
		
		RegexBuilder reg = Nina.whiteSpace().zeroOrMore();
		
		if (ignoreCase) reg = reg.ignoreCase();
		
		for (int i = 0; i < searchText.length(); i++)
		     reg = reg.with(Nina.whiteSpace().zeroOrMore().with(searchText.charAt(i)));
		
		List<com.tr.hsyn.regex.cast.Index> indexes = reg.findAll(word);
		
		List<Index> result = new ArrayList<>(indexes.size());
		//result[x++] = i.start;
		//result[x++] = i.end;
		result.addAll(indexes);
		
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
	 * Tests if a string has another string.
	 * The spaces don't break the match.
	 * <p>
	 * For example, {@code '543'} compared to {@code ' 5 4 3 '} yields {@code true}.
	 *
	 * @param word       the text that searched in
	 * @param searchText search text
	 * @param ignoreCase ignore case
	 * @return {@code true} if matches
	 */
	public static boolean isMatch(String word, String searchText, boolean ignoreCase) {
		
		if (isNoboe(word) || isNoboe(searchText)) return false;
		
		if (word.length() < searchText.length()) return false;
		
		searchText = Regex.removeWhiteSpaces(searchText);
		
		com.tr.hsyn.regex.cast.@NotNull RegexBuilder reg = Nina.regex().whiteSpace().zeroOrMore().lazy();
		
		if (ignoreCase) reg.with(Modifier.modify().ignoreCase());
		
		for (int i = 0; i < searchText.length(); i++) {
			
			reg.with(searchText.charAt(i))
					.whiteSpace(Quanta.ZERO_OR_MORE)
					.lazy();
		}
		
		return reg.existIn(word);
		
		//return indexOfMatches(word, searchText, ignoreCase).length != 0;
	}
	
	/**
	 * Tests for the absence of a string.
	 *
	 * @param str string to test for the absence
	 * @return {@code true} if the string is {@code null} or blank or empty.
	 * 		(<strong>N</strong>ull <strong>O</strong>r <strong>B</strong>lank <strong>O</strong>r <strong>E</strong>mpty)
	 */
	public static boolean isNoboe(String str) {
		
		return str == null || WHITE_SPACE.removeFrom(str).isEmpty();
	}
	
	@Deprecated(forRemoval = true)
	@NotNull
	public static Integer @NotNull [] _indexOfMatches(String word, String searchText) {
		
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

