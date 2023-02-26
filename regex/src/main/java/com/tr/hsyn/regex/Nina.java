package com.tr.hsyn.regex;


import com.tr.hsyn.regex.act.Ranger;
import com.tr.hsyn.regex.act.Teddy;
import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.CharacterSet;
import com.tr.hsyn.regex.cast.DateGenerator;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.Quantifier;
import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Regex;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.cast.WordGenerator;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <h2>Nina</h2>
 * <p>
 * {@code Nina}, 1997 yılının bahar aylarında Paris'te  <em>Sen Nehri</em> kıyısındaki <em>Monnaine</em> mahallesinde doğmuş.<br>
 * İyi giyimli, güzel ve nazik konuşan insanların arasında büyümüş.<br>
 * Çocukken kendisi de öyle güzel ve nazik konuşmak için çabalamış ancak bir gün<br>
 * konuşulan kelimelerin konuşan kişiye göre biraz farklılık gösterdiğini farketmiş.<br>
 * Bu konu ilgisini çekmiş ve yıllarca kelimeler üzerinde inceleme araştırma yapmış.<br>
 * Zengin bir ailenin tek kızı olan {@code Nina} üniversiteyi bitirdikten sonra
 * <em>Düzenli İfadeler Araştırma Geliştirme Vakfı</em>nı kurmuş.<br>
 * ---------------------------------------------------------------------------------------------------------------------<br>
 *
 * <h3>Önsöz</h3>
 * <p>
 * Harfler heceleri, heceler kelimeleri, kelimeler cümleleri, cümleler ifadeleri oluşturur.<br>
 * Gözlerimiz cümleler içinde kelimeleri, kelimeler içinde heceleri, heceler içinde harfleri bulur.<br>
 * Ve her biri sadece bizler için bir anlam ifade eder.<br>
 * Dijital ortamlar açısından bunların hiçibiri için bir <strong>anlam</strong>dan söz etmek mümkün değildir.<br>
 * Ancak <strong>anlam</strong> da bir kelimedir. Eğer biz bir kelimeyi sanal olarak temsil edebiliyorsak,<br>
 * <strong>anlam</strong>ı neden temsil edemeyelim.<br>
 * <p>
 * Şuanki teknolojimiz manaları temsil edemiyor ancak kelimeleri gerçeğiyle birebir temsil edebiliyor.<br>
 * Ve biz bu temsiller üzerinden araştırmalar yapabiliyoruz.<br>
 * {@code Nina}'nın ihtisas alanı bu temsillerdir.<br>
 * ---------------------------------------------------------------------------------------------------------------------<br>
 * <p>
 * <strong>Temsil etme</strong> olayı bizi, kelimeler dünyasının anlamdan soyutlanmış alt bir katmanına indirir.<br>
 * Bu katman, gerçek hayatla ilgili ilginç benzeşimler gösterir.
 * Mesela gerçekte de, içine girdiğimiz bu temsil sisteminde de en temel eleman <strong>harfler</strong>dir.<br>
 * Biz bir makineye, bir paragraf içinde aradığımız bir kelimeyi, kelimenin harflerini tarif ederek anlatabiliriz.<br>
 * Tarif ettiğimiz harfler için, harflerin herbirine diğerinden farklı veya diğeriyle ortak özellikler belirleyebiliriz.<br>
 * Mesela kelimemiz '23 Nisan' olsun ve bu kelimeyi uzun uzun paragrafları olan bir makalede arıyor olalım.<br>
 * Ancak bu kelime sadece '23 Nisan' olarak değil, '12 Nisan, 1 Nisan' vs. gibi nisan ayının farklı
 * günleriyle geçiyor olsun.<br>
 * Ve biz bu nisan ayının tüm günlerini ortaya çıkarmak istiyoruz.<br>
 * Öncelikle bu ifadeyi soyutlamamız gerek. <br>
 * En temel eleman <strong>harfler</strong>dir, o halde sistemin bize harflerin temsili için
 * bazı araçlar sunması gerek. <br>
 * Java dili bize bu temsil sistemini 'düzenli ifadeler' başlığı altında sunar.<br>
 * Harflerin temsili 'karakter sınıfı' kategorisiyle başlar.
 * Bu katagorideki en temel temsiller şöyle : <br><br>
 *
 * <pre>
 *    .    -   Yeni satır karakteri hariç herhangi bir karakteri temsil eder.
 *    \w   -   Herhangi bir harf, herhangi bir sayı veya alt tire (_) karakterini temsil eder.
 *    \d   -   Sayı karakterini temsil eder.
 *    \s   -   Boşluk karakterini temsil eder.
 * </pre><br>
 * <p>
 * Bunlar bu haliyle sadece ve sadece tek bir karakteri temsil ederler.
 * Liste elbette bundan uzun.<br>
 * Harflere nicelik özelliği ekleyebilmemiz için bir de 'niceleyiciler' kategorisi vardır.
 * Niceleyiciler, herhangi bir harften yada harf temsilinden sonra gelirler ve
 * o harfin sayısal olarak ne kadar çoklukta olması gerektiğini bildirirler.<br>
 *
 * <pre>
 *       ?        - Kendisinden önce gelen karakterin {@code 0} yada {@code 1} tane olması gerektiğini belirtir.
 *       +        - Kendisinden önce gelen karakterin en az {@code 1} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 *       *        - Kendisinden önce gelen karakterin en az {@code 0} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 *    {min,max}   - Kendisinden önce gelen karakterin minimum ve maximum sayıda olması gerektiğini bildirir.
 * </pre><br>
 *
 *
 * <p>
 * Örneğimize dönersek, arayacağımız kelime {@code 'XX Nisan'}.
 * Buradaki 'XX'ler, oraya sayı geleceğini belirtmekte ve bu sayılar en az bir en çok iki olmalı.<br>
 * Buna göre düzenli ifademiz şöyle olabilir : {@code \d{1,2}\sNisan}<br>
 * Küme parantezlerinin içine en az ve en çok geçebilecek sayıyı yazdık.<br>
 * {@code \d, \s} gibi karakterler, düzenli ifadeler konusu içerisinde {@code meta karakterler} olarak adlandırılıyor.<br>
 * Yani bunlar bizim bildiğimiz anlamdaki harflerin temsil sistemi altındaki karşıllığıdır.<br>
 * <p>
 * Örneğimizi bundan çok daha farklı şekillerde de yazabiliriz ancak
 * basitlik ve özet geçme açısından sadece bir bakış atma amacı taşımakta olduğundan daha ileri gitmiyoruz.<br>
 * Burada ana tema, harflerin bir şekilde temsil edilebilmesi için daha soyut bir düşünce benimsiyor olmamız.<br>
 * Bu soyutlama bizi gerçeklerle ilgili daha derin düşünmeye sevk edebilir.<br>
 * Neticede herşey temsillerden ibaret.
 * Belki bu çalışma bize yeni bir bakış açısı kazandırır.<br><br>
 *
 * <h2>Düzenli İfadeler</h2>
 *
 * <p> Bir düzenli ifade, bir harfi, bir heceyi yada bütün olarak bir kelimeyi yada kelime grubunu tarif edebilir.<br>
 * Daha da ileri gidip <strong>ifadeleri</strong> tarif eden bir düzenli ifade yazmak da mümkündür,<br>
 * ancak bu düzenli ifade o kadar karmaşık olacaktır ki
 * bunun için çok daha soyut düşünmeyi gerektirecektir.<br>
 * {@code Nina} uzun ve karmaşık düzenli ifadeler yazmayı kolaylaştırır.<br>
 * Ancak karmaşıklık düzenli ifadelerin temelinde var ve ifadeler ilerledikçe daha fazla karmaşıklık kaçınılmazdır.<br>
 * <p>
 * Bir yazı tarif eden düzenli ifadelere {@code Patterns} denilir.<br>
 * Türkçe olarak <i>desen, kalıp</i> diyoruz.<br>
 * Bu da bize bahsettiğimiz soyutlamayı vurgulamaktadır.<br>
 * Düzenli ifade oluşturmak için bir <b>pattern</b> bir desen tanımlamamız gerek.<br>
 * Ve {@code Nina} bize bu konuda yardımcı olacak.<br><br>
 *
 * <p>Mesela en çok yapılan iş, bir yazının istediğimiz kriterde olup olmadığını bulmak.<br>
 * Önce {@code Nina} ile bir düzenli ifade oluşturalım.<br><br>
 *
 * <pre>
 * Regex r = newRegex()
 *             .letterUpper()
 *             .atLeast(4)
 *             .digit()
 *             .atLeast(4);
 * </pre>
 *
 * <p>
 * Bu düzenli ifade, en az 4 büyük harf ve en az 4 sayıdan oluşan bir yazıyı temsil eder.<br>
 * Şimdi bunu bir örnek yazı ile eşleşme olup olmadığını test edelim.<br><br>
 *
 * <pre>
 * r.test("CUMA1342"); // true. Düzenli ifadenin minimum gereksinimlerini karşılıyor (en az 4 büyük harf, en az 4 adet sayı)
 * r.test("CUMALİ1342"); // true. Harf sayısı fazla fakat bu olabilir çünkü <strong>en az 4</strong> adet olma koşulunu karşılıyor, üst sınır yok
 * r.test("ÇİĞDEM01342"); // true.
 * r.test("ÇİğDEM01342"); // false. Eşleşme olmaz çünkü büyük harf kuralımız var</pre>
 *
 * @author hsyn 14 Haziran 2022 Salı 11:18
 */
public interface Nina {
	
	/**
	 * Regular expression for digits.
	 */
	String NUMBER = "^\\p{N}+$";
	/**
	 * Regular expression for alphabetics.
	 */
	String WORD   = "^\\p{L}+$";
	
	/**
	 * Yeni ve boş bir düzenli ifade nesnesi oluşturur.
	 *
	 * @return {@link Regex}
	 */
	@NotNull
	static Regex regex() {
		
		return Teddy.regex();
	}
	
	/**
	 * Yeni bir düzenli ifade nesnesi oluşturur.
	 *
	 * @param expression Düzenli ifade
	 * @return Yeni bir {@link Regex} nesnesi
	 */
	@NotNull
	static Regex regex(@NotNull String expression) {
		
		return Teddy.regex(expression);
	}
	
	/**
	 * Yeni bir düzenli ifade nesnesi oluşturur.
	 *
	 * @param expression Düzenli ifade
	 * @return Yeni bir {@link Regex} nesnesi
	 */
	static <T extends Text> @NotNull Regex regex(@NotNull T expression) {
		
		return Teddy.regex(expression);
	}
	
	static <T extends Text> @NotNull Range range(@NotNull T expresion) {
		
		return Ranger.rangeOf(expresion);
	}
	
	static <T extends Text> @NotNull Range range(@NotNull String expresion) {
		
		return Ranger.rangeOf(expresion);
	}
	
	static <T extends Text> @NotNull Regex group(@NotNull T expresion) {
		
		return regex().group(expresion.getText());
	}
	
	@SafeVarargs
	static <T extends Text> @NotNull Regex group(@NotNull T @NotNull ... expresions) {
		
		StringBuilder regex = new StringBuilder();
		
		for (var exp : expresions) regex.append(exp.getText());
		
		return regex().group(regex.toString());
	}
	
	static <T extends Text> @NotNull Regex group(@NotNull String expresion) {
		
		return regex().group(expresion);
	}
	
	static <T extends Text> @NotNull Regex group(@NotNull String groupName, @NotNull T expresion) {
		
		return regex().group(groupName, expresion.getText());
	}
	
	static <T extends Text> @NotNull Regex group(@NotNull String groupName, @NotNull String expresion) {
		
		return regex().group(groupName, expresion);
	}
	
	static void main(String[] args) {
		
		test10();
	}
	
	static void test11() {
		//(?<name>\p{L}+) harf 1 veya daha fazla
		//(?=\P{L}+)?  harf olmayan 1 veya daha fazla karakter (positive lookahead - yakalamayacak)
		String pattern = "(?<name>\\p{L}+)(?=\\P{L}+)?";
		String text    = "ali, ()?   _veli_,,,,deli,,,, keli.?";
		
		var l = regex(pattern).matchesOf(text);
		
		pl("Result : %s", l);
	}
	
	static void test12() {
		
		//! (([0-2]?[0-9])|(3[0-1])) (N|n)isan
		String str = "143 nisan";
		
		var range1 = group(range("0-2").zeroOrOne().range("0-9"));
		var range2 = group(regex().with(3).range("0-1"));
		var date   = group(range1.or(range2));
		
		var month = regex().space().oneOrMore().with(regex("N").or("n").toGroup()).with("isan");
		
		var regex = regex().with(date).with(month);
		
		pl(regex);
		pl(regex.test(str));
		
	}
	
	static void test10() {
		
		//Regex r = regex().letterUpper().atLeast(4).digit().atLeast(4);
		
		var regex = Teddy.regex("1-5").toRange();
		pl("Regex : %s", regex);
		
		//String text = "ÇİğDEM01342";
		//pl("Regex : %s", r);
		//pl("%s : %s", text, r.test(text));
		
	}
	
	static void test9() {
		
		String pattern = "(?<name>\\p{L}+)(?=\\P{L}+)?";
		String text    = "ali, ()?    veli,,,, deli,,,, keli";
		
		var l = regex(pattern).matchesOf(text);
		
		pl("Result : %s", l);
	}
	
	static void test7() {
		
		String mySearch = "merhaba dünya";
		
		var m1 = Dev.toRegexCodes(mySearch, false, false);
		var m2 = Dev.toRegexCodes(mySearch, false, true);
		var m3 = Dev.toRegexCodes(mySearch, true, false);
		var m4 = Dev.toRegexCodes(mySearch, true, true);
		
		pl("%s : %s", mySearch, Arrays.toString(m1));
		pl("%s : %s", mySearch, Arrays.toString(m2));
		pl("%s : %s", mySearch, Arrays.toString(m3));
		pl("%s : %s", mySearch, Arrays.toString(m4));
		
		/*
		merhaba dünya : [3, 3, 3, 3, 3, 3, 3, 5, 3, 3, 3, 3, 3]
		merhaba dünya : [18, 12, 18, 16, 14, 18, 14, 5, 18, 12, 18, 18, 14]
		merhaba dünya : [8, 10, 8, 8, 10, 8, 10, 5, 8, 3, 8, 3, 10]
		merhaba dünya : [18, 12, 18, 16, 14, 18, 14, 5, 18, 12, 18, 18, 14]
		*/
		
	}
	
	static void test6() {
		
		System.out.println(System.getProperty("file.encoding"));
		String myWord = "hello girls";
		
		var generator = new WordGenerator(myWord, 0, CharacterSet.ENGLISH_CHARS + CharacterSet.TURKISH_CHARS);
		
		pl("Word       : %s", myWord);
		pl("RegexCodes : %s", Arrays.toString(generator.getRegexCodes()));
		
		for (int i = 0; i < 10; i++) {
			
			var word = generator.getWord();
			
			if (word.equals(myWord)) {
				pl("Bingo!! %s [%d]", word, i);
				break;
			}
			
			pl("Generated : %s", word);
		}
		
		pl("The End");
		
		
	}
	
	static void test5() {
		
		String mySearch = "nisa 66ebek";
		
		var m = regex().with("nisa\\s").group(regex().with("c").or("B")).with("bebek").any().oneOrMore();
		
		pl(m.findGroups(mySearch, "bebek"));
		
		pl("m : %s", m);
	}
	
	static void test4() {
		
		String mySearch = "nisa bebek";
		var    s        = Edit.overWrite(mySearch, "*");
		
		pl("overWrite : %s", s);
	}
	
	static void test3() {
		
		String mySearch = "nisa bebek";
		var    m        = regex().with("a").space().zeroOrMore().with('b');
		
		pl("Match : '%s'", m.test(mySearch));
		pl("Remove : '%s'", m.replaceFrom(mySearch, "*"));
		
		var indexes = m.findAll(mySearch);
		pl("Result : '%s'", indexes);
		
		pl("Exactly : %s", mySearch.substring(indexes.get(0).start, indexes.get(0).end));
	}
	
	static void test2() {
		
		var date = DateGenerator.start().twoDigitDay().dot().twoDigitMonth().dot().fourDigitYear().space().longTime().getPattern();
		
		pl("Date pattern is : %s", date);
		
		var d = LocalDateTime.now();
		
		var dateStr = d.format(DateTimeFormatter.ofPattern(date));
		pl("Date string is : %s", dateStr);
	}
	
	static void test1() {
		
		var m = regex().with(range(regex().letter().control().space()).negate());
		pl(m.replaceFrom("+ 9 0 5 4 3 4 9 3 7 5 3 0", "*"));
		
		pl(m);
	}
	
	public static void pl(Object message, Object... args) {
		
		System.out.printf((message != null ? message.toString() : "null") + "%n", args);
	}
	
	
	/**
	 * Kendi içinde {@link Regex} sınıfını kullanarak bazı test metotları tanımlar.<br><br>
	 *
	 * <pre>
	 *    isNumber("542 5f7 842"); //false
	 *    isNumber("54 25 78 42"); //true
	 *    isAlpha("54 25 78 42"); //false
	 *    isAlpha("abc def"); //true
	 * </pre>
	 */
	interface Test {
		
		/**
		 * Test a string for a regular expression.
		 *
		 * @param str   the string to test
		 * @param regex the regular expression to test the string against
		 * @return {@code true} if the string matches the regular expression, {@code false} otherwise
		 */
		static boolean test(CharSequence str, CharSequence regex) {
			
			if (str == null || regex == null) return false;
			
			return str.toString().matches(regex.toString());
		}
		
		/**
		 * Test if the given string is consists of only digits.
		 *
		 * @param str the string to test
		 * @return true if the string is consists of only digits, false otherwise
		 */
		static boolean isNumber(String str) {
			
			return test(Edit.removeWhiteSpaces(str), NUMBER);
		}
		
		/**
		 * Test if the given string is consists of only alphabetic characters.
		 *
		 * @param str the string to test
		 * @return true if the string is consists of only alphabetic characters, false otherwise
		 */
		static boolean isAlpha(String str) {
			
			return test(Edit.removeWhiteSpaces(str), WORD);
		}
		
		/**
		 * Noboe : {@code null} or blank or empty.
		 *
		 * @param text the text to test
		 * @return {@code true} if the text is {@code null} or blank or empty, {@code false} otherwise
		 */
		static boolean isNoboe(String text) {
			
			return text == null || Edit.removeWhiteSpaces(text).isEmpty();
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
		static boolean contains(CharSequence text, CharSequence what, boolean ignoreSpaces, boolean ignoreCase) {
			
			if (text == null || what == null) return false;
			
			Regex regex = regex();
			
			if (ignoreCase) regex.with(Modifier.of().ignoreCase());
			
			if (ignoreSpaces) {
				for (var c : what.toString().toCharArray()) regex.space().oneOrMore().with(c);
			}
			
			return regex.containsIn(text);
		}
	}
	
	/**
	 * Kendi içinde {@link Regex} sınıfını kullanarak bazı değiştirme metotları tanımlar.<br>
	 */
	interface Edit {
		
		/**
		 * Remove all characters that match the given regular expression.
		 *
		 * @param sequence the string to remove characters from
		 * @param regex    the regular expression to match characters to remove
		 * @return New string with all matching characters removed
		 */
		@NotNull
		static CharSequence remove(String sequence, String regex) {
			
			return replace(sequence, "", regex, 0);
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
		static CharSequence remove(String str, String regex, int limit) {
			
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
			
			return regex(regex).replaceFrom(str, replacement, limit);
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
		static String replace(String str, String replacement, String regex) {
			
			return replace(str, replacement, regex, 0);
		}
		
		/**
		 * Removes all white spaces.
		 *
		 * @param str the string to remove white spaces from
		 * @return New string without white spaces
		 */
		@NotNull
		static String removeWhiteSpaces(String str) {
			
			return replace(str, "", Character.WHITE_SPACE);
		}
		
		/**
		 * Removes all digit characters.
		 *
		 * @param str the string to remove digit characters from
		 * @return New string without digit characters
		 */
		@NotNull
		static String removeNumerics(String str) {
			
			return replace(str, "", Character.DIGIT);
		}
		
		/**
		 * Removes all non digit characters and returns the remains
		 *
		 * @param str String
		 * @return An object consisting only of numbers
		 */
		@NotNull
		static String retainNumerics(String str) {
			
			return removeWhiteSpaces(removeAlpha(str));
		}
		
		/**
		 * Removes all alpha characters from the string.
		 *
		 * @param str the string to remove alpha characters from
		 * @return New string without alpha characters
		 */
		@NotNull
		static String removeAlpha(String str) {
			
			return replace(str, "", Range.ALPHA);
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
			
			Regex regex = regex();
			Regex gr    = regex();
			
			
			if (source.length() <= 4) {
				
				regex = regex.any().times(1).group("rp", gr.any().oneOrMore()).any().times(1);
				//regex.any().times(1).with(group("rp").any().oneOrMore()).any().times(1);
			}
			else {
				regex = regex.any().times(2).group("rp", gr.any().oneOrMore()).any().times(2);
			}
			
			Matcher m = Pattern.compile(regex.getText()).matcher(source);
			
			if (m.find())
				return new StringBuilder(source).replace(m.start("rp"), m.end("rp"), replacement.repeat(m.group("rp").length())).toString();
			
			return source;
		}
	}
	
	/**
	 * Düzenli ifadelerle ilgili bazı deneysel metotlar tanımlar.
	 */
	interface Dev {
		
		/**
		 * Bir yazının düzenli ifade karşılığını döndürür.
		 *
		 * @param str Yazı
		 * @return the regular expression
		 */
		static @NotNull Regex toRegex(String str) {
			
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
		static @NotNull Regex toRegex(String str, boolean exactQuantifiers) {
			
			Regex regex = regex();
			
			if (str == null) return regex;
			
			char[] chars              = str.toCharArray();
			String lastCharacterClass = "";
			int    lastCharacterCount = 1;
			
			for (int i = 0; i < chars.length; i++) {
				
				char c = chars[i];
				
				//- Karakterin sınıfı
				var clazz = Character.getCharacterClass(c);
				
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
							regex = regex.with(clazz).times(lastCharacterCount);
						}
						
						continue;
					}
					
					var r = regex.getText();
					
					if (!Quantifier.isQuantifier(r.charAt(r.length() - 1))) regex = regex.with(Quantifier.ONE_OR_MORE);
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
							
							regex = regex.with(lastCharacterClass).times(lastCharacterCount);
							
							lastCharacterCount = 1;
						}
					}
					else {
						//- Kesin sayı istenmiyor, direk ekleyebiliriz
						regex = regex.with(clazz);
					}
				}
				
				lastCharacterClass = clazz;
			}
			
			return regex;
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
	}
	
	
}
