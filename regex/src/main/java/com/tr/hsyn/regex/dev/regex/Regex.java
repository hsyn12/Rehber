package com.tr.hsyn.regex.dev.regex;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.act.Teddy;
import com.tr.hsyn.regex.cast.CharacterSet;
import com.tr.hsyn.regex.cast.Index;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.RegexMatcher;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.dev.regex.character.Character;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


/**
 * This interface provides a set of static methods for working with regular expressions in Java 8.
 * Regular expressions are patterns used to match character combinations in strings.
 */
public interface Regex {
	
	/**
	 * Regular expression for digits.
	 */
	@RegExp          String NUMBER           = "^\\p{N}+$";
	/**
	 * Regular expression for alphabetics.
	 */
	@RegExp          String WORD             = "^\\p{L}+$";
	/**
	 * An alphabetic character. {@code \p{L}}
	 */
	@RegExp          String LETTER           = "\\p{L}";
	/**
	 * Any character except letter. {@code \P{L}}
	 */
	@RegExp          String NON_LETTER       = "\\P{L}";
	/**
	 * A digit.  {@code \p{N}}
	 */
	@RegExp          String DIGIT            = "\\p{N}";
	/**
	 * Any character except digit. {@code \P{N}}
	 */
	@RegExp          String NON_DIGIT        = "\\P{N}";
	/**
	 * A whitespace character, including line break. {@code [ \t\r\n\f\x0B]}
	 */
	@RegExp          String WHITE_SPACE      = "\\p{Z}";
	/**
	 * Any character except white space.
	 */
	@RegExp          String NON_WHITE_SPACE  = "\\P{Z}";
	/**
	 * Punctuation character {@code [!"#$%&'()*+,\-./:;<=>?@\[\\\]^_`{|}~]}
	 */
	@RegExp          String PUNCTUATION      = "\\p{P}";
	/**
	 * Any character except punctuation.
	 */
	@RegExp          String NON_PUNCTUATION  = "\\P{P}";
	/**
	 * Any character from {@link #LETTER}, {@link #DIGIT}, {@link #PUNCTUATION}, {@link #WHITE_SPACE}
	 */
	@RegExp
	@NotNull         String ANY              = String.format("[%s%s%s%s]", LETTER, DIGIT, PUNCTUATION, WHITE_SPACE);
	/**
	 * A lowercase alphabetic character {@code [a-z]}
	 */
	@RegExp @NotNull String LETTER_LOWER     = "\\p{Ll}";
	/**
	 * Any character except lowercase alphabetic. {@code [^\p{Ll}]}
	 */
	@RegExp @NotNull String NON_LETTER_LOWER = "\\P{Ll}";
	/**
	 * An uppercase alphabetic character {@code [A-Z]}
	 */
	@RegExp @NotNull String LETTER_UPPER     = "\\p{Lu}";
	/**
	 * Any character except uppercase alphabetic. {@code [^\p{Lu}]}
	 */
	@RegExp @NotNull String NON_LETTER_UPPER = "\\P{Lu}";
	/**
	 * Delimiters {@code '.$^{[()|*+?\'}
	 */
	String DELIMITER_CHARACTERS = ".$^{[()|*+?\\";
	/**
	 * A control character {@code [\p{Cntrl}]}
	 */
	@RegExp @NotNull String CONTROL     = "\\p{C}";
	/**
	 * A non-control character {@code [^\p{C}]}
	 */
	@RegExp @NotNull String NON_CONTROL = "\\P{C}";
	/**
	 * A symbol character {@code [\p{S}]}
	 */
	@RegExp @NotNull String SYMBOL      = "\\p{S}";
	/**
	 * A backslash character {@code [\\]}
	 */
	@NotNull         String BACK_SLASH  = "\\\\";
	
	/**
	 * Yazının içinden, verilen karakter türüne ait karakterleri siler.
	 *
	 * @param text      Yazı
	 * @param regexChar Karakter türü
	 * @return Yeni bir string
	 */
	@NotNull
	static @Unmodifiable String removeAll(@NotNull String text, @NotNull Character regexChar) {
		
		return regexChar.removeFrom(text);
	}
	
	/**
	 * Remove all characters that match the given regular expression.
	 *
	 * @param sequence the string to remove characters from
	 * @param regex    the regular expression to match characters to remove
	 * @return New string with all matching characters removed
	 */
	@NotNull
	static @Unmodifiable String removeAll(@NotNull String sequence, @NotNull String regex) {
		
		return sequence.replaceAll(regex, "");
	}
	
	/**
	 * Remove all characters that match the given regular expression until the limit is reached.
	 *
	 * @param str   the string to remove characters from
	 * @param regex the regular expression to match characters to remove
	 * @param limit the maximum number of characters to remove. If {@code limit} less than one, all characters will be removed.
	 * @return New string with all matching characters removed
	 */
	@NotNull
	static @Unmodifiable String removeAll(String str, String regex, int limit) {
		
		return replace(str, "", regex, limit);
	}
	
	/**
	 * Replace all characters that match the given regular expression with the given replacement until the given limit is reached.
	 *
	 * @param str         the string to replace characters in
	 * @param replacement the string to replace matching characters with
	 * @param regex       the regular expression to match characters to replace
	 * @param limit       the maximum number of replacements to perform. If {@code limit} less than one, then all matches are replaced.
	 * @return New string with all matching characters replaced
	 */
	@NotNull
	static String replace(String str, String replacement, String regex, int limit) {
		
		if (str == null || str.isEmpty()) return "";
		if (regex == null) return str;
		if (replacement == null) return str;
		
		return builder(Text.of(regex)).replaceFrom(str, replacement, limit);
	}
	
	/**
	 * Replace all characters that match the given regular expression with the given replacement.
	 *
	 * @param str         the string to replace characters in
	 * @param replacement the string to replace matching characters with
	 * @param regex       the regular expression to match characters to replace
	 * @return New string with all matching characters replaced
	 */
	@NotNull
	static String replace(@NotNull String str, @NotNull String replacement, @NotNull String regex) {
		
		return str.replaceAll(regex, replacement);
	}
	
	/**
	 * Removes all white spaces.
	 *
	 * @param str the string to remove white spaces from
	 * @return New string without white spaces
	 */
	@NotNull
	static String removeWhiteSpaces(@NotNull String str) {
		
		return Character.WHITE_SPACE.removeFrom(str);
	}
	
	/**
	 * Removes all digit characters.
	 *
	 * @param str the string to remove digit characters from
	 * @return New string without digit characters
	 */
	@NotNull
	static String removeDigits(@NotNull String str) {
		
		return Character.DIGIT.removeFrom(str);
	}
	
	/**
	 * This method removes all the letters from the given string and returns the resulting string.
	 *
	 * @param str the input string from which letters are to be removed
	 * @return the resulting string after removing all the letters
	 * @throws NullPointerException if the input string is null
	 */
	@NotNull
	static String removeLetters(@NotNull String str) {
		
		return Character.LETTER.removeFrom(str);
	}
	
	/**
	 * Removes all non digit characters and returns the remains
	 *
	 * @param str String
	 * @return An object consisting only of numbers
	 */
	@NotNull
	static String retainDigits(String str) {
		
		if (str != null) return Character.NON_DIGIT.removeFrom(str);
		
		return "";
	}
	
	/**
	 * This method takes a string as input and returns a new string that contains only the letters from the original string.
	 * If the input string is null, an empty string is returned.
	 *
	 * @param str the input string to be processed
	 * @return a new string that contains only the letters from the original string, or an empty string if the input is null
	 */
	@NotNull
	static String retainLetters(String str) {
		
		if (str != null) return Character.NON_LETTER.removeFrom(str);
		
		return "";
	}
	
	/**
	 * Returns a new instance of RegexBuilder.
	 *
	 * @return a new instance of RegexBuilder
	 */
	static @NotNull RegexBuilder builder() {
		
		return new Teddy();
	}
	
	/**
	 * Creates a regular expression builder.
	 *
	 * @param expression Expression
	 * @param <T>        Type of the expression
	 * @return Regular expression builder {@link RegexBuilder}
	 */
	static <T extends Text> @NotNull RegexBuilder builder(@NotNull T expression) {
		
		return new Teddy(expression);
	}
	
	/**
	 * Overwrite the given string with the given character, except first and last two characters of the source.<br>
	 * if the given string less than three characters, then the source returns.<br>
	 * if the given string three characters, then the middle character of the source replaced with the given character.<br>
	 * <p>
	 * For example :<br>
	 *
	 * <pre>
	 * overwrite("12345", "*");           // returns "12*45"
	 * overwrite("1234567", "*");         // returns "12***67"
	 * overwrite("1234567897654", "*");   // returns "12*********54"
	 * overwrite("1234", "*");            // returns "1**4"
	 * overwrite("123", "*");             // returns "1*3"
	 * overwrite("12", "*");              // returns "12"
	 * overwrite("1", "*");               // returns "1"
	 * </pre>
	 *
	 * @param source      the string to overwrite
	 * @param replacement the character to use to overwrite
	 * @return New overwritten string
	 */
	@NotNull
	static String overWrite(String source, String replacement) {
		
		if (replacement == null || source == null || source.isEmpty()) return "";
		
		RegexBuilder regex = builder();
		RegexBuilder gr    = builder();
		
		
		if (source.length() <= 4) {
			
			regex.any(1).group("rp", gr.any().oneOrMore()).any(1);
			//regex.any().times(1).with(group("rp").any().oneOrMore()).any().times(1);
		}
		else {
			regex.any(2).group("rp", gr.any().oneOrMore()).any(2);
		}
		
		Matcher m = RegexMatcher.createMatcher(regex.getText(), source);
		
		if (m.find())
			return new StringBuilder(source).replace(m.start("rp"), m.end("rp"), replacement.repeat(m.group("rp").length())).toString();
		
		return source;
	}
	
	/**
	 * Test a string for a regular expression.
	 *
	 * @param str   the string to test
	 * @param regex the regular expression to test the string against
	 * @return {@code true} if the string matches the regular expression, {@code false} otherwise
	 */
	static boolean test(@NotNull String str, @NotNull String regex) {
		
		return str.matches(regex);
	}
	
	/**
	 * Noboe : {@code null} <strong>o</strong>r  <strong>b</strong>lank <strong>o</strong>r  <strong>e</strong>mpty.
	 *
	 * @param text the text to test
	 * @return {@code true} if the text is {@code null} or blank or empty, {@code false} otherwise
	 */
	static boolean isNoboe(String text) {
		
		return text == null || Regex.removeWhiteSpaces(text).isEmpty();
	}
	
	/**
	 * Test regex contains.
	 * This is the regex search.<br>
	 * Maybe {@code text} contains spaces, maybe case sensitive or not.<br><br>
	 * <p>
	 * For example :<br>
	 * {@code contains("N i   s a bebek", "nisa", true, true); // returns true}<br>
	 *
	 * @param text         the text to search in
	 * @param what         the text to search for
	 * @param ignoreSpaces {@code true} if spaces should be ignored, {@code false} otherwise
	 * @param ignoreCase   {@code true} if case should be ignored, {@code false} otherwise
	 * @return {@code true} if the text contains the {@code what}, {@code false} otherwise
	 */
	static boolean contains(String text, String what, boolean ignoreSpaces, boolean ignoreCase) {
		
		if (text == null || what == null) return false;
		
		RegexBuilder regex = builder();
		
		if (ignoreCase) regex.with(Modifier.modify().ignoreCase());
		
		if (ignoreSpaces) {
			for (var c : what.toCharArray()) regex.whiteSpace().with("*?").with(c);
		}
		
		return regex.existIn(text);
	}
	
	/**
	 * Returns the string found after the last occurance of 'after' inside 'text'.
	 * If no string is found, returns an empty string.
	 *
	 * @param text  the text in which to search for the string
	 * @param after the string to search for in 'text'
	 * @return the string found after the last occurance of 'after' inside 'text'
	 */
	static @NotNull String stringAfterLast(@NotNull String text, @NotNull String after) {
		
		var index = text.lastIndexOf(after);
		
		if (index != -1) return text.substring(index + 1);
		
		return "";
	}
	
	/**
	 * Returns the string after the first occurrence of a given substring.
	 *
	 * @param text  Specify the text that will be searched for the after parameter
	 * @param after Specify the string that is being searched for
	 * @return The text after the first occurrence of the parameter &quot;after&quot; in the parameter &quot;text&quot;
	 */
	static @NotNull String stringAfterFirst(@NotNull String text, @NotNull String after) {
		
		var index = text.indexOf(after);
		
		if (index != -1) return text.substring(index + 1);
		
		return "";
	}
	
	/**
	 * The stringBeforeLast function returns the text before the last occurrence of a given substring.
	 *
	 * @param text   Specify the text that will be searched
	 * @param before Specify the substring that will be used to find the last occurrence of
	 * @return The text before the last occurrence of a substring
	 */
	static @NotNull String stringBeforeLast(@NotNull String text, @NotNull String before) {
		
		var index = text.lastIndexOf(before);
		
		if (index != -1) return text.substring(0, index);
		
		return "";
	}
	
	/**
	 * Returns the part of a string before the first occurrence of another string.
	 *
	 * @param text   Specify the text that will be searched
	 * @param before Specify the string to be searched for in the text parameter
	 * @return The string before the first occurrence of a given substring
	 */
	static @NotNull String stringBeforeFirst(@NotNull String text, @NotNull String before) {
		
		var index = text.indexOf(before);
		
		if (index != -1) return text.substring(0, index);
		
		return "";
	}
	
	/**
	 * Bir string içinde belirli index'lerdeki parçaları döndürür.
	 *
	 * @param str     String
	 * @param indices Parçaların yerleri
	 * @return Parçaların listesi
	 */
	@NotNull
	static List<String> getStringParts(String str, @NotNull List<Index> indices) {
		
		List<String> list = new ArrayList<>();
		
		if (isNoboe(str)) return list;
		
		for (var index : indices) list.add(str.substring(index.start, index.end));
		
		return list;
	}
	
	/**
	 * Bir yazının düzenli ifade karşılığını döndürür.
	 *
	 * @param str Yazı
	 * @return the regular expression
	 */
	static @NotNull RegexBuilder toRegex(String str) {
		
		return toRegex(str, false);
	}
	
	/**
	 * Bir yazının düzenli ifade karşılığını döndürür.
	 *
	 * @param str              Yazı
	 * @param exactQuantifiers Tam adet modu. {@code true} ise, aynı düzenli ifade grubuna giren karakterlerin tam adedi belirtilerek düzenli ifade oluşturulur.
	 *                         Eğer {@code false} ise tam adet yerine birden fazla anlamına gelen {@code +} işareti kullanılarak düzenli ifade oluşturulur.
	 * @return the regular expression
	 */
	static @NotNull RegexBuilder toRegex(String str, boolean exactQuantifiers) {
		
		RegexBuilder regex = builder();
		
		if (str == null) return regex;
		
		char[] chars              = str.toCharArray();
		String lastCharacterClass = "";
		int    lastCharacterCount = 1;
		
		for (int i = 0; i < chars.length; i++) {
			
			char c = chars[i];
			
			//- Karakterin sınıfı
			var clazz = getCharacterClass(c);
			
			//- İlk döngü burayı daima atlayacak çünkü lastCharacterClass boş
			if (lastCharacterClass.equals(clazz)) {
				if (exactQuantifiers) {
					//- Eğer karakter için kesin sayı isteniyorsa
					//- buradan lastCharacterCount değerini bir arttırıp
					//- döngünün başına dönmeliyiz.
					//- Bu koşulda işlem lastCharacterClass != clazz
					//- olduğunda yapılmalıdır.
					//- Düzenli ifadeye burada bir şey eklenmediğine dikkat et.
					//- lastCharacterClass da değişmiyor çünkü zaten aynı.
					
					lastCharacterCount++; //! Sadece burada arttırılıyor.
					
					//- Sona kalan dona kalmasın
					if (i == chars.length - 1) {
						regex.with(clazz).times(lastCharacterCount);
					}
					
					continue;
				}
				
				var r = regex.getText();
				
				if (!Quanta.isQuantifier(r.charAt(r.length() - 1))) regex.oneOrMore();
			}
			else {
				
				//- Burası ya ilk döngü yada uyuşmazlık var.
				
				if (exactQuantifiers) {
					//- Karakter için kesin sayı isteniyor.
					//- Bu durumda lastCharacterClass değerini düzenli ifadeye eklemeliyiz.
					
					//- Bu ilk döngü mü?
					if (!lastCharacterClass.isEmpty()) {
						//- ilk döngü değil.
						//- Gerekli bilgileri ekliyoruz.
						
						regex.with(lastCharacterClass).times(lastCharacterCount);
						
						lastCharacterCount = 1;
					}
				}
				else {
					//- Kesin sayı istenmiyor, direk ekleyebiliriz
					regex.with(clazz);
				}
			}
			
			lastCharacterClass = clazz;
		}
		
		return regex;
	}
	
	/**
	 * Bir karakterin ait olduğu karakter sınıfını döndürür.<br>
	 *
	 * <ul>
	 *    <li>{@link #WHITE_SPACE}  : boşluk sınıfı</li>
	 *    <li>{@link #DIGIT}  : sayı sınıfı</li>
	 *    <li>{@link #LETTER} : harf sınıfı</li>
	 *    <li>{@link #PUNCTUATION}   : noktalama sınıfı</li>
	 * </ul>
	 *
	 * @param c Test edilecek karakter
	 * @return Karakter grubu
	 */
	static String getCharacterClass(char c) {
		
		if (java.lang.Character.isLetter(c)) return LETTER;
		if (java.lang.Character.isDigit(c)) return DIGIT;
		if (java.lang.Character.isWhitespace(c)) return WHITE_SPACE;
		
		return PUNCTUATION;
	}
	
	/**
	 * Bir yazının karaker kod karşılığını döndürür.
	 *
	 * @param sequence Yazı
	 * @return Karakter kodları
	 */
	static Integer @NotNull [] toRegexCodes(String sequence) {
		
		return toRegexCodes(sequence, false, false);
	}
	
	/**
	 * Bir yazının karaker kod karşılığını döndürür.<br><br>
	 *
	 * <pre>
	 * String mySearch = "merhaba dünya";
	 *
	 * var m1 = Dev.toRegexCodes(mySearch, false, false);
	 * var m2 = Dev.toRegexCodes(mySearch, false, true);
	 * var m3 = Dev.toRegexCodes(mySearch, true, false);
	 * var m4 = Dev.toRegexCodes(mySearch, true, true);
	 *
	 * pl("%s : %s", mySearch, Arrays.toString(m1));
	 * pl("%s : %s", mySearch, Arrays.toString(m2));
	 * pl("%s : %s", mySearch, Arrays.toString(m3));
	 * pl("%s : %s", mySearch, Arrays.toString(m4));
	 *
	 * //merhaba dünya : [3, 3, 3, 3, 3, 3, 3, 5, 3, 3, 3, 3, 3]
	 * //merhaba dünya : [18, 12, 18, 16, 14, 18, 14, 5, 18, 12, 18, 18, 14]
	 * //merhaba dünya : [8, 10, 8, 8, 10, 8, 10, 5, 8, 3, 8, 3, 10]
	 * //merhaba dünya : [18, 12, 18, 16, 14, 18, 14, 5, 18, 12, 18, 18, 14]
	 * </pre>
	 *
	 * @param sequence     Yazı
	 * @param specific     Karakter kodları için ünlü-ünsüz ayırımı yapar
	 * @param moreSpecific Karakter kodlarında sesli harfler için ince-kalın, sesiz harflar için sert-yumuşak ayırımı yapar
	 * @return Karakter kodları
	 * @see CharacterSet
	 */
	static Integer @NotNull [] toRegexCodes(String sequence, boolean specific, boolean moreSpecific) {
		
		if (sequence == null) return new Integer[0];
		
		List<Integer> codes = new ArrayList<>();
		
		char[] chars = sequence.toCharArray();
		
		for (char c : chars) {
			var clazz = CharacterSet.getCharecterSet(c, specific, moreSpecific);
			
			int code = CharacterSet.getCharacterCode(clazz);
			
			Nina.pl("Class : %66s - %-2c - %-2d", clazz, c, code);
			
			codes.add(code);
		}
		
		return codes.toArray(new Integer[0]);
	}
	
	/**
	 * Bir yazının tamamının harf karakterlerinden oluşup oluşmadığını test eder.
	 *
	 * @param str Yazı
	 * @return {@code true} ise, tüm yazı harf karakterlerinden oluşuyor,
	 *      {@code false} ise yazının içinde harf karakteri dışında bir karakter var yada
	 * 		yazı boş demektir.
	 * @see Regex#LETTER
	 */
	static boolean isLetter(@NotNull String str) {
		
		return str.matches(WORD);
	}
	
	/**
	 * Test if the given string is consists of only digits.
	 *
	 * @param str the string to test
	 * @return true if the string is consists of only digits, false otherwise
	 */
	static boolean isNumber(@NotNull String str) {
		
		return str.matches(NUMBER);
	}
}
