package com.tr.hsyn.string;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.dev.RegexChar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * String işlemleri için fonksiyonlar içerir.
 */
public final class Stringx {
	
	public static final int UPPER = 0;
	public static final int LOWER = 1;
	
	/**
	 * Dizi elemanlarını belirtilen karakter ile birleştirir.
	 *
	 * @param collection Dizi
	 * @param delimiter  Birleştirme karakteri
	 * @param <T>        Dizi elemanlarının türü
	 * @return Birleştirilmiş string
	 */
	@NotNull
	public static <T> String joinToString(T @NotNull [] collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(collection);
	}
	
	/**
	 * Liste elemanlarını string olarak birleştirir.
	 *
	 * @param collection Liste
	 * @param delimiter  Birleştirme karakteri
	 * @param <T>        Liste eleman türü
	 * @return Birleştirilmiş string
	 */
	@NotNull
	public static <T> String joinToString(@NotNull List<? extends T> collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(collection);
	}
	
	/**
	 * Liste elemanlarını string olarak birleştirir.
	 * Birleştirme karakteri virgül'dür.
	 *
	 * @param collection Liste
	 * @param <T>        Liste eleman türü
	 * @return Birleştirilmiş string
	 */
	@NotNull
	public static <T> String joinToString(List<? extends T> collection) {
		
		if (collection == null) return "";
		
		return Joiner.on(',').join(collection);
	}
	
	@NotNull
	public static <T> String joinToString(@NotNull Iterable<? extends T> collection) {
		
		return Joiner.on(',').join(collection);
	}
	
	@NotNull
	public static String joinToString(int[] collection) {
		
		return Joiner.on(',').join(List.of(collection));
	}
	
	@NotNull
	public static String joinToString(long[] collection) {
		
		return Joiner.on(',').join(List.of(collection));
	}
	
	@NotNull
	public static String joinToString(int[] collection, @NotNull String delimiter) {
		
		return Joiner.on(delimiter).join(List.of(collection));
	}
	
	@NotNull
	public static Iterable<String> split(@NotNull String value) {
		
		return Splitter.on(",").omitEmptyStrings().trimResults().split(value);
	}
	
	@NotNull
	public static Iterable<String> split(@NotNull String value, String delimeter) {
		
		return Splitter.on(delimeter).omitEmptyStrings().trimResults().split(value);
	}
	
	/**
	 * String'in ilk karakterini döndürür.
	 * Eğer string boş ya da {@code null} ise boş string döner.
	 *
	 * @param str String
	 * @return String'in ilk karakteri
	 */
	@NotNull
	public static String getFirstChar(String str) {
		
		if (str == null) {str = "";}
		
		str = trimWhiteSpaces(str);
		
		if (str.isEmpty()) {return "";}
		
		return String.valueOf(str.charAt(0));
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
		
		var c = text.charAt(0);
		
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
	 * Verilen string'i formatla.
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
		
		if (Nina.Test.isNoboe(word) || Nina.Test.isNoboe(searchText)) return false;
		
		if (word.length() < searchText.length()) return false;
		
		searchText = Nina.Edit.removeWhiteSpaces(searchText);
		
		var reg = Nina.regex().whiteSpace().zeroOrMore();
		
		if (ignoreCase) reg = reg.with(Modifier.modify().ignoreCase());
		
		for (int i = 0; i < searchText.length(); i++) {
			
			reg = reg.with(searchText.charAt(i))
					.whiteSpace()
					.zeroOrMore();
		}
		
		return reg.existIn(word);
		
		//return indexOfMatches(word, searchText, ignoreCase).length != 0;
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
		
		if (Nina.Test.isNoboe(word) || Nina.Test.isNoboe(searchText)) return new Integer[0];
		
		searchText = Nina.Edit.removeWhiteSpaces(searchText);
		
		var reg = RegexChar.WHITE_SPACE.zeroOrMore();
		
		if (ignoreCase) reg = reg.ignoreCase();
		
		for (int i = 0; i < searchText.length(); i++)
		     reg = reg.with(RegexChar.WHITE_SPACE.zeroOrMore().with(searchText.charAt(i)));
		
		var indexes = reg.findAll(word);
		
		Integer[] result = new Integer[indexes.size() * 2];
		
		int x = 0;
		for (var i : indexes) {
			
			result[x++] = i.start;
			result[x++] = i.end;
		}
		
		return result;
	}
	
	/**
	 * Verilen string'i, ilk ve son iki karakteri hariç yıldız (*) karakteri ile maskeler.<br>
	 * Mesela 'ahmet' kelimesi 'ah*et' olur.<br>
	 * Mesela 'ahmet bey' kelimesi 'ah*** *ey' olur.<br><br>
	 * <p>
	 * Verilen string {@code null} ise 'null' string'i döner.<br>
	 * Verilen string boş ise tek bir yıldız '*' string'i döner.<br>
	 * Verilen string 3 karakterden az ise karakter sayısı kadar yıldız '*' string'i döner.<br>
	 * Verilen string 3 karakter ise, mesela 'ali', sadece orta karakteri yıldız olur, 'a*i'<br>
	 * Verilen string 4 karakter ise, mesela 'abla', orta karakterler yıldız olur, 'a**a'<br>
	 * Geri kalan durumlarda ilk ve son iki karakter haricindekiler yıldız olur.
	 *
	 * @param str String
	 * @return Maskelenmiş string
	 */
	public static String overWrite(String str) {
		
		return overWrite(str, '*');
	}
	
	/**
	 * String'i belirtilen karakter ile maskeler.
	 *
	 * @param str String
	 * @param c   Maskeleme karakteri
	 * @return Maskelenmiş string
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
	
	/**
	 * Verilen iki string'i karşılaştırarak birinin diğerinin
	 * içinde geçip geçmediğini tespit edecek.<br></br>
	 * <p>
	 * Mesela 'EL' ile 'hello' kelimesi eşleşir.
	 * Eşleşmelerde büyük küçük harf ayrımı yapılmayacak.
	 *
	 * @param s1 s1
	 * @param s2 s2
	 * @return eşeleşme var ise {@code true}
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
	
	public static boolean isLowerCase(@NotNull String string) {
		
		for (char c : string.toCharArray()) {
			
			if (Character.isUpperCase(c)) return false;
		}
		
		return true;
	}
	
	public static boolean isBlank(@NotNull String str) {
		
		return trimWhiteSpaces(str).isEmpty();
	}
	
	public static boolean isNullOrBlank(String str) {
		
		return str == null || isBlank(str);
	}
	
	@NotNull
	public static String toUpper(@NotNull String str) {
		
		var chars = str.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			
			char c = chars[i];
			
			if (c != 'i' && c != 'ı') chars[i] = Character.toUpperCase(c);
			else if (c == 'i') chars[i] = 'İ';
			else chars[i] = 'I';
		}
		
		return new String(chars);
	}
	
	/**
	 * Verilen string'i küçük harflere çevirir.
	 * {@link String#toLowerCase()} metodundan tek farkı {@code I} ve {@code İ} harflerini
	 * {@code ı} ve {@code i} olarak çevirir.
	 *
	 * @param str Küçük harflere çevrilecek olan string
	 * @return Küçük harflere çevrilmiş olan string
	 */
	@NotNull
	public static String toLower(@NotNull String str) {
		
		var chars = str.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			
			char c = chars[i];
			
			if (c != 'I' && c != 'İ') chars[i] = Character.toLowerCase(c);
			else if (c == 'İ') chars[i] = 'i';
			else chars[i] = 'ı';
		}
		
		return new String(chars);
	}
	
	@NotNull
	public static String toCapital(@NotNull String str) {
		
		if (str.trim().isEmpty()) return str;
		
		char first = str.charAt(0);
		
		if (first == 'i') first = 'I';
		else first = Character.toUpperCase(first);
		
		return first + str.substring(1);
	}
	
	@NotNull
	public static String setCharAt(String str, int index, char c) {
		
		StringBuilder stringBuilder = new StringBuilder(str);
		
		stringBuilder.setCharAt(index, c);
		
		return stringBuilder.toString();
	}
	
	@NotNull
	public static String setCharAt(String str, int index, int charType) {
		
		StringBuilder stringBuilder = new StringBuilder(str);
		
		char c = stringBuilder.charAt(index);
		
		if (charType == UPPER) {
			
			if (c == 'i') c = 'I';
			
			c = Character.toUpperCase(c);
		}
		else if (charType == LOWER) {
			
			if (c == 'I') c = 'i';
			c = Character.toLowerCase(c);
		}
		
		stringBuilder.setCharAt(index, c);
		return stringBuilder.toString();
	}
	
	/**
	 * Verilen string'deki ilk büyük ya da ilk küçük harfin index'ini döndürür
	 *
	 * @param str      Bir string
	 * @param charType Büyük harf için {@link #UPPER} veya küçük harf için {@link #LOWER}
	 * @return İndex yada -1 (bulunamazsa)
	 */
	public static int indexOfCase(String str, int charType) {
		
		switch (charType) {
			
			case UPPER:
				
				for (int i = 0; i < str.length(); i++)
					if (Character.isUpperCase(str.charAt(i))) return i;
				
				return -1;
			
			case LOWER:
				
				for (int i = 0; i < str.length(); i++)
					if (Character.isLowerCase(str.charAt(i))) return i;
				
				return -1;
			
			default:
				return -1;
		}
	}
	
	@NotNull
	public static String repeat(char c, int count) {
		
		return String.valueOf(c).repeat(Math.max(0, count));
	}
	
	@NotNull
	public static String repeat(@NotNull String s, int count) {
		
		StringBuilder sb = new StringBuilder();
		
		//noinspection StringRepeatCanBeUsed
		for (int i = 0; i < count; i++) sb.append(s);
		
		return sb.toString();
	}
	
	/**
	 * String içindeki tüm boşluk karakterlerini siler. {@code [\r\n\t\f\v]} ve {@code ' '} boşluk.
	 *
	 * @param str String
	 * @return Boşluksuz bitişik bir string. Eğer verilen string {@code null} ise boş string
	 */
	@NotNull
	public static String trimWhiteSpaces(String str) {
		
		if (str != null) return str.replaceAll("\\s", "");
		
		return "";
	}
	
	/**
	 * String içindeki sayı harici tüm karakterleri siler.
	 *
	 * @param str String
	 * @return Sadece sayılardan oluşan bitişik boşluksuz bir string. Sayı yoksa boş string.
	 */
	@NotNull
	public static String trimNonDigits(@NotNull String str) {
		
		return str.replaceAll("[^0-9]", "");
	}
	
	/**
	 * İki string'i eşitlik için karşılaştırır.
	 *
	 * @param s1 String
	 * @param s2 String
	 * @return İki string eşitse {@code true}. Eğer iki string de {@code null} ise {@code true}
	 */
	public static boolean equals(@Nullable String s1, @Nullable String s2) {
		
		if (s1 == null && s2 == null) return true;
		if (s1 == null || s2 == null) return false;
		
		s1 = trimWhiteSpaces(s1);
		s2 = trimWhiteSpaces(s2);
		
		return s1.equals(s2);
	}
	
	/**
	 * İki string nesnenin sayısal içeriklerini karşılaştırır.
	 * Karşılaştırma, nesnelerin sayısal olmayan tüm karakterleri çıkarılarak yapılır.
	 * Yani karşılaştırılacak string nesnelerin içinde sayısal olmayan karakterler bulunabilir.<br>
	 * İki {@code null} nesne birbirine eşit değildir.<br>
	 * İki boş string birbirine eşit değildir.
	 * Karşılaştırmanın yapılması için
	 * nesnelerin içinde en az bir tane sayısal karakterin olması gerek.<br>
	 *
	 *
	 * <pre>
	 * equalsNumbers(null, null); // false
	 * equalsNumbers(null, ""); // false
	 * equalsNumbers("", ""); // false
	 * equalsNumbers("hsyn1204", "h1s2y0n4"); // true
	 * </pre>
	 *
	 * @param s1 Sayı
	 * @param s2 Sayı
	 * @return İki sayı birbirine eşitse {@code true}, değilse {@code false}
	 */
	public static boolean equalsNumbers(String s1, String s2) {
		
		return equalsNumbers(s1, s2, 0);
	}
	
	/**
	 * İki sayısal değer içeren string nesneyi eşitlik için karşılaştırır.
	 * Nesneler sayısal olmayan karakterler içerebilir,
	 * karşılaştırma bu karakterler çıkarılarak yapılır.
	 * Bu bir sayısal karşılaştırma olduğu için iki {@code null} nesne birbirine eşit olamaz.
	 * Ve iki boş string de birbirine eşit olmaz.
	 *
	 * <pre>
	 * equalsNumbers(null, null, 0); // false
	 * equalsNumbers(null, "", 0); // false
	 * equalsNumbers("", "", 0); // false
	 * equalsNumbers("", "", 0); // false
	 * equalsNumbers("hsyn1204", "h1s2y0n4", 0); // true
	 * equalsNumbers("123", "2", 0); // false
	 * equalsNumbers("123", "2", 2); // true
	 * // tolerans değeri, nesnelerden biri diğerini içeriyorsa,
	 * // iki nesnenin karakter sayıları arasında en fazla kaç karakter fark olması gerektiğini bildirir.
	 *
	 * </pre>
	 *
	 * @param s1        String
	 * @param s2        String
	 * @param tolerance Tolerans
	 * @return İki string sayısal olarak eşitse {@code true}, değilse {@code false}
	 */
	public static boolean equalsNumbers(String s1, String s2, int tolerance) {
		
		if (s1 == null || s2 == null) return false;
		
		s1 = Stringx.trimNonDigits(s1);
		s2 = Stringx.trimNonDigits(s2);
		
		if (s1.isBlank() || s2.isBlank()) return false;
		
		if (s1.equals(s2)) return true;
		
		int i1 = s1.length();
		int i2 = s2.length();
		
		if (i1 == i2) return false;
		
		if (s2.contains(s1) || s1.contains(s2))
			return Math.abs(i1 - i2) <= tolerance;
		else return false;
	}
	
}

