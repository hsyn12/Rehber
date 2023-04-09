package com.tr.hsyn.regex;


import com.tr.hsyn.regex.act.Teddy;
import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.CharacterSet;
import com.tr.hsyn.regex.cast.DateGenerator;
import com.tr.hsyn.regex.cast.Index;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;
import com.tr.hsyn.regex.cast.WordGenerator;
import com.tr.hsyn.regex.dev.CoupleFinder;
import com.tr.hsyn.regex.dev.HMT;
import com.tr.hsyn.regex.dev.Look;
import com.tr.hsyn.regex.dev.RegexChar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

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
 * {@code Nina}, 1997 yılının bahar aylarında Paris'te  <em>Sen Nehri</em> kıyısındaki <em>Monnaine</em> mahallesinde doğmuş,
 * iyi giyimli, güzel ve nazik konuşan insanların arasında büyümüş akıllı kadındır.<br>
 * Çocukken kendisi de güzel ve nazik konuşmak için çok çabalarmış, ve bir gün
 * konuşulan kelimelerin konuşan kişiye göre biraz farklılık gösterdiğini farketmiş.<br>
 * Bu konu çok ilgisini çekmiş ve yıllarca kelimeler üzerinde inceleme araştırma yapmış.
 * Ve bir noktada kelimelerin soyut dünyasını keşfetmiş.<br>
 * Zengin bir ailenin tek kızı olan {@code Nina} üniversiteyi bitirdikten sonra
 * <em>Düzenli İfadeler Araştırma Geliştirme Vakfı</em>nı kurmuş.<br>
 * <p>
 * <hr>
 * <p>
 * <p>
 * <h3>Önsöz</h3>
 * <p>
 * Harfler heceleri, heceler kelimeleri, kelimeler cümleleri, cümleler ifadeleri oluşturur.<br>
 * Gözlerimiz cümleler içinde kelimeleri, kelimeler içinde heceleri, heceler içinde harfleri bulur.<br>
 * Ve her biri sadece bizler için bir anlam ifade eder.<br>
 * Dijital ortamlar açısından bunların hiçibiri için bir <strong>anlam</strong>dan söz etmek mümkün değildir.<br>
 * Ancak <strong>anlam</strong> da bir kelimedir. Eğer biz bir kelimeyi sanal olarak temsil edebiliyorsak,<br>
 * <strong>anlam</strong>ı neden temsil edemiyoruz.<br>
 * <p>
 * Şuanki teknolojimiz manaları temsil edemiyor ancak kelimeleri gerçeğiyle birebir temsil edebiliyor.<br>
 * Ve biz bu temsiller üzerinden araştırmalar yapabiliyoruz.<br>
 * {@code Nina}'nın ihtisas alanı bu temsillerdir.<br>
 * <p>
 * <hr>
 * <p>
 * <p>
 * <p>
 * <strong>Temsil etme</strong> olayı bizi, kelimeler dünyasının anlamdan soyutlanmış alt bir katmanına indirir.<br>
 * Bu katman, gerçek hayatla ilgili ilginç benzeşimler gösterir.
 * Mesela gerçekte de, içine girdiğimiz bu temsil sisteminde de en temel eleman <strong>harfler</strong>dir.<br>
 * Biz bir makineye, bir metin içinde aradığımız bir kelimeyi, kelimenin harflerini tarif ederek anlatabiliriz.<br>
 * Yani makineye, kelimeyi oluşturan harfleri veririz.
 * Tarif ettiğimiz harfler için, harflerin herbirine diğerinden farklı veya diğeriyle ortak özellikler belirleyebiliriz.<br>
 * Mesela bir yazının içinde <em>Nisan</em> tarihli günleri arıyorsak, {@code XX Nisan} şeklinde
 * geçen ifadeleri tespit etmemiz gerek ({@code XX} buraya sayı geleceğini belirtir).
 * Gördüğün gibi sadece harfler değil, işin içine sayılar da giriyor.<br>
 * Hatta arada bir boşluk karakteri olduğuna da dikkat çekmek isterim.<br>
 * Harfler, sayılar, boşluk, bunlar birbirinden farklı karakterlerdir ve farklı şekilde temsil edilmek zorundadır.<br>
 * Bunların dışında bir de <em>noktalama karakterleri</em> vardır ({@link CharacterSet#PUNCT}).<br>
 * Temsil sisteminde karakter türlerini kabaca 4 sınıfa ayırabiliriz.<br>
 * <p>
 * <ol>
 *    <li>Harf</li>
 *    <li>Sayı</li>
 *    <li>Boşluk</li>
 *    <li>Noktalama</li>
 * </ol>
 *
 * <p>
 * <p>
 * <p>
 * Java dili bize bu temsil sistemini <em>'Düzenli İfadeler</em>' başlığı altında sunar.<br>
 * Ve bu karakter türlerini temsil etmek için bize <em>meta karakterler</em> sağlar.<br><br>
 *
 * <pre>
 *    .    -   Yeni satır karakteri hariç herhangi bir karakteri temsil eder.
 *    \w   -   Herhangi bir harf, herhangi bir sayı veya alt tire (_) karakterini temsil eder.
 *    \d   -   Sayı karakterini temsil eder.
 *    \s   -   Boşluk karakterini temsil eder.
 * </pre><br>
 * <p>
 * Liste elbette bundan uzun. Ayrıca <em>Nina</em> bu meta karakterlerin unicode versiyonunu kullanır.
 * Mesela sayı karakteri için {@code \d} yerine {@code \p{N}} kullanır.
 * Diğer tüm karakterler için {@link Character} arayüzüne bakabilirsin.
 * <p>
 * <p>
 * <p>
 *    Meta karakterler tek başına sadece tek bir karakteri temsil eder.
 *    Bir karaktere nicelik özelliği ekleyebilmemiz için bir de '<em>niceleyiciler</em>' vardır.
 *    Niceleyiciler, herhangi bir karakterden yada meta karakterden sonra gelirler ve
 *    o karakterin sayısal olarak ne kadar çoklukta olması gerektiğini bildirirler.<br>
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
 *    {@code 'XX Nisan'} örneğine dönersek,
 *    buradaki 'XX'ler, oraya sayı geleceğini belirtmekte ve bu sayılar en az bir en çok iki tane olmalı.<br>
 *    Yukarıdaki bilgiler ışığında düzenli ifadeyi şöyle yazabiliriz : {@code \d{1,2}\sNisan}<br>
 *
 * <ul>
 *    <li>{@code \d{1,2}} - en az 1 en çok iki sayısal karakter</li>
 *    <li>{@code \s}      - bir boşluk</li>
 *    <li>{@code Nisan}   - yazının kendisi</li>
 * </ul>
 * <p>
 *    Görüldüğü üzere hem normal karakter hem de meta karakter birlikte kullanılıyor.<br>
 *    Kullanılan normal bir karakter direk kendisini temsil eder,
 *    meta karakter ise yukarıda bahsedildiği üzere farklı karakter türlerini
 *    temsil edebilir. Meta karakterler daima ters bölü işareti ile başlar {@code \}.
 *    Ve en genel bakış açışıyla bir düzenl ifadei esasında iki tür karakterden oluşur:
 * <p>
 *
 *    <ol>
 *       <li>Meta karakter</li>
 *       <li>Değişmez Karakter</li>
 *    </ol>
 *
 * <p>
 *    Yukarıda normal karakter dediğimiz şey değişmez (<strong>literal</strong>) karakter türüdür.
 *
 * <p>
 *    Burada ana tema, karakterlerin bir şekilde temsil edilebilmesi için
 *    daha soyut bir düşünce benimsiyor olmamız (<em>meta karakterler</em>).<br>
 *    Bu soyutlama bizi gerçeklerle ilgili daha derin düşünmeye sevk edebilir.<br>
 *    Neticede herşey temsillerden ibaret.
 *    Belki bu çalışma bize yeni bir bakış açısı kazandırır.<br><br>
 *
 * <h2>Düzenli İfadeler</h2>
 *
 * <p> Bir düzenli ifade, bir harfi, bir heceyi yada bütün olarak bir kelimeyi yada kelime grubunu tarif edebilir.
 * Daha da ileri gidip <strong>ifadeleri</strong> tarif eden bir düzenli ifade yazmak da mümkündür,
 * ancak bu düzenli ifade o kadar karmaşık olacaktır ki
 * bunu yazmak ve anlamak çok daha soyut düşünmeyi gerektirecektir.
 * {@code Nina} uzun ve karmaşık düzenli ifadeler yazmayı kolaylaştırır.
 * Ancak karmaşıklık düzenli ifadelerin temelinde var ve ifadeler ilerledikçe daha fazla karmaşıklık kaçınılmazdır.<br>
 * <p>
 * Bir yazı tarif eden düzenli ifadelere {@code Patterns} denilir.<br>
 * Bu yazı tek bir kelime de olabilir, birden fazla kelimeden oluşan bir kelime grubu da olabilir.
 * <em>Pattern</em> Türkçe olarak <i>desen, kalıp, model</i> anlamına gelir.<br>
 * Bu da bize bahsettiğimiz soyutlamayı vurgulamaktadır.<br>
 * Düzenli ifade oluşturmak için bir <b>pattern</b> bir model tanımlamamız gerek.<br>
 * Ve {@code Nina} bize bu konuda yardımcı olacak.<br><br>
 *
 * <pre>
 * Regex r = Nina.regex()
 *             .letterUpper()
 *             .atLeast(4)
 *             .digit()
 *             .atLeast(4)
 *             .build(); // \p{Lu}{4,}\p{N}{4,}</pre>
 *
 * <p>
 * Bu düzenli ifade, en az 4 büyük harf ve en az 4 sayıdan oluşan tek bir kelimeyi tarif etmektedir.<br>
 * Şimdi bunu bir örnek yazı ile eşleşme olup olmadığını test edelim.<br><br>
 *
 * <pre>
 * r.test("CUMA1342"); // true. Düzenli ifadenin minimum gereksinimlerini karşılıyor (en az 4 büyük harf, en az 4 adet sayı)
 * r.test("CUMALİ1342"); // true. Harf sayısı fazla fakat bu olabilir çünkü <strong>en az 4</strong> adet olma koşulunu karşılıyor, üst sınır yok
 * r.test("ÇİĞDEM01342"); // true.
 * r.test("ÇİğDEM01342"); // false. Eşleşme olmaz çünkü büyük harf kuralımız var</pre>
 * <p>
 * <p>
 * <p>
 *    {@link com.tr.hsyn.regex.cast.Regex#test(String)} metodu, verilen string'in tamamının, oluşturulan düzenli ifadeye
 *    uyup uymadığını test eder. Yani düzenli ifade ile modellediğimiz kelime her bir karakteri ile birlikte
 *    tam olarak tarif edildiği gibi bir bütün olarak test edilir. Bu bir uygunluk kontrolüdür.<br>
 *    Oluşturduğumuz modeli eğer daha büyük bir yazının içinde aramak istiyorsak,
 *    yani bir yazı içinde modelimizin eşleştiği yerleri bulmak istiyorsak,<br><br>
 *
 * <pre>
 * String str   = "123abc456def";
 * Regex  regex = Nina.regex().digit().letter().build();// <strong>\p{N}\p{L}</strong>
 *
 * pl("Result  : %s", regex.findAll(str));// Result  : [Index{start=2, end=4}, Index{start=8, end=10}]
 * pl("Result  : %s", regex.matchesOf(str));// Result  : [3a, 6d]</pre>
 *
 * <p>
 * <p>
 * <p>Bu düzenli ifade modeli, herhangi bir sayı (<strong>{@code \p{N}}</strong>) ve
 * herhangi bir harf (<strong>{@code \p{L}}</strong>).<br>
 * Yani bir sayı bir de harf olacak ve bitişik olacak (aynı sırada).<br>
 * {@link com.tr.hsyn.regex.cast.Regex#matchesOf(CharSequence)} metodu yazının içinde modelin eşleştiği parçaları döndürür.<br>
 * {@link com.tr.hsyn.regex.cast.Regex#findAll(CharSequence)} metodu ise eşleşmelerin yazı içindeki başlangıç ve bitiş index'lerini döndürür.<br><br>
 *
 * <p>
 *    Düzenli ifadeler, bir metin üzerinde değişiklik yapmak için de kullanılabilir.<br><br>
 *
 * <pre>
 * String str = "12 Nisan 1981";
 *
 * pl("Result  : '%s'", Nina.Edit.retainNumerics(str));//Result  : '121981'
 * pl("Result  : '%s'", Nina.Edit.retainLeters(str));//Result  : 'Nisan'
 * pl("Result  : '%s'", Nina.Edit.removeNumerics(str));//Result  : ' Nisan '
 * pl("Result  : '%s'", Nina.Edit.removeLetters(str));//Result  : '12  1981'
 * </pre>
 *
 * <p>
 * <p>
 * <hr>
 * <p>
 *    <h3>Regex</h3>
 * <p>
 *    Yukarıdaki paragraflardan anlaşılacağı üzere düzenli ifadeleri şu üç amaçla kullanmaktayız:
 *
 * <ol>
 *    <li>Doğrulama</li>
 *    <li>Arama</li>
 *    <li>Değişiklik Yapma</li>
 * </ol>
 *
 * <p>
 *    Ancak ikinci ve üçüncü işlemler esasında <strong>doğrulama</strong> yoluyla gerçekleştirilir.
 *    Düzenli ifade ile bir kalıp oluşturulur ve eşleşme sorgulanır. İşin özü budur.
 *
 * <p>
 *    Düzenli ifade, <strong><u>kurallı</u></strong> ifadedir.
 *    Bir düzenli ifade oluştururken kurallar belirleriz ve
 *    yukarıdaki işlemler bu kurallara (kurallı ifadeye) göre çalışır.<br>
 *
 * <p>
 *    Bir düzenli ifade oluştururken spesifik bir yapı modellenir.
 *    Mesela bir metin içinde arama yapacaksak, ne arayacağımızı bilmemiz gerekir.
 *    Veya bir metin içinde birşey değiştireceksek, neyi değiştireceğimizi bilmemiz gerekir.
 *    Yani düzenli ifade, spesifik bir ifadenin varlığına ihtiyaç duyar.
 *    Bu spesifik ifadeyi kurallı ifade olarak modellerken ise, üstü kapalı muğlak bir model hazırlayabiliriz ve
 *    düzenli ifadenin makbul olanı da budur.
 *    Eğer çok net ve birebir bir uygunluk arıyorsak basit bir eşitlik ({@link String#equals(Object)})
 *    veya içerme ({@link String#contains(CharSequence)}) kontrolü yaparak düzenli ifadelerden uzak durmamız daha doğrudur.
 *    Çünkü bunlar düzenli ifadelerden çok daha hızlı çalışır.
 *
 * <p>
 *    Yinede şunu itiraf etmeliyimki, düzenli ifadeleri anlamaya başladıktan sonra vazgeçemiyorsun.
 *
 * @author hsyn 14 Haziran 2022 Salı 11:18
 */
public interface Regex {
	
	/**
	 * Regular expression for digits.
	 */
	String NUMBER = "^\\p{N}+$";
	/**
	 * Regular expression for alphabetics.
	 */
	String WORD   = "^\\p{L}+$";
	
	/**
	 * Bir harf karakteri.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder letter() {
		
		return like().letter();
	}
	
	/**
	 * Bir harf karakteri belirtir.
	 *
	 * @param letter Harf
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder letter(@NotNull @org.intellij.lang.annotations.Pattern("[a-zA-Z]") String letter) {
		
		return like().letter();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> harf karakteri.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder letters() {
		
		return like().letters();
	}
	
	/**
	 * Harf olmayan bir karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonLetter() {
		
		return like().nonLetter();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> harf olmayan karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonLetters() {
		
		return like().nonLetters();
	}
	
	/**
	 * Sayısal bir karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder digit() {
		
		return like().digit();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> sayısal karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder digits() {
		
		return like().digit();
	}
	
	/**
	 * Sayısal olmayan bir karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonDigit() {
		
		return like().nonDigit();
	}
	
	/**
	 * Sayısal olmayan <em>bir yada daha fazla</em> karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonDigits() {
		
		return like().nonDigits();
	}
	
	/**
	 * Bir boşluk.
	 *
	 * @return New {@link RegexBuilder}
	 * @see Character#WHITE_SPACE
	 */
	static @NotNull RegexBuilder whiteSpace() {
		
		return like().whiteSpace();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> boşluk.
	 *
	 * @return New {@link RegexBuilder}
	 * @see Character#WHITE_SPACE
	 */
	static @NotNull RegexBuilder whiteSpaces() {
		
		return like().whiteSpaces();
	}
	
	/**
	 * Boşluk olmayan bir karakter.
	 *
	 * @return New {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	static @NotNull RegexBuilder nonWhiteSpace() {
		
		return like().nonWhiteSpace();
	}
	
	/**
	 * Boşluk olmayan <em>bir yada daha fazla</em> karakter.
	 *
	 * @return New {@link RegexBuilder}
	 * @see Character#NON_WHITE_SPACE
	 */
	static @NotNull RegexBuilder nonWhiteSpaces() {
		
		return like().nonWhiteSpaces();
	}
	
	/**
	 * Bir noktalama işareti.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder punc() {
		
		return like().punc();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama işareti.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder puncs() {
		
		return like().puncs();
	}
	
	/**
	 * Noktalama işareti dışında bir karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonPunc() {
		
		return like().nonPunc();
	}
	
	/**
	 * <em>Bir yada daha fazla</em> noktalama harici karakter.
	 *
	 * @return New {@link RegexBuilder}
	 */
	static @NotNull RegexBuilder nonPuncs() {
		
		return like().nonPuncs();
	}
	
	/**
	 * Verilen ifade, düzenli ifadenin başlangıcını tarif eder.<br>
	 * {@code ^(expression)}<br><br>
	 *
	 * @param expression İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder startsWith(@NotNull String expression) {
		
		return Regex.like().anchorStart().group(expression);
	}
	
	/**
	 * Verilen ifade, düzenli ifadenin sonunu tarif eder.<br>
	 * {@code (expression)$}<br><br>
	 *
	 * @param expression İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder endsWith(@NotNull String expression) {
		
		return Regex.like().anchorEnd().group(expression);
	}
	
	/**
	 * Bir ifadenin <em>bir yada daha fazla</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder oneOrMore(@NotNull String regex) {
		
		return like(String.format("%s+", regex));
	}
	
	/**
	 * Bir ifadenin <em>sıfır yada daha fazla</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder zeroOrMore(@NotNull String regex) {
		
		return like(String.format("%s*", regex));
	}
	
	/**
	 * Bir ifadenin <em>sıfır yada bir kez</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder zeroOrOne(@NotNull String regex) {
		
		return like(String.format("%s?", regex));
	}
	
	/**
	 * Bir ifadenin <em>bir yada daha fazla</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @param <T>   {@link Text}
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static <T extends Text> RegexBuilder oneOrMore(@NotNull T regex) {
		
		return like(String.format("%s+", regex));
	}
	
	/**
	 * Bir ifadenin <em>sıfır yada daha fazla</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @param <T>   {@link Text}
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static <T extends Text> RegexBuilder zeroOrMore(@NotNull T regex) {
		
		return like(String.format("%s*", regex));
	}
	
	/**
	 * Bir ifadenin <em>sıfır yadabir kez</em> tekrar etmesi gerektiğini belirten
	 * düzenli ifadeyi döndürür.
	 *
	 * @param regex İfade
	 * @param <T>   {@link Text}
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static <T extends Text> RegexBuilder zeroOrOne(@NotNull T regex) {
		
		return like(String.format("%s?", regex));
	}
	
	/**
	 * Yeni ve boş bir düzenli ifade nesnesi oluşturur.
	 *
	 * @return {@link com.tr.hsyn.regex.cast.Regex}
	 */
	@NotNull
	static RegexBuilder regex() {
		
		return Teddy.regex();
	}
	
	/**
	 * Yeni bir düzenli ifade nesnesi oluşturur.
	 *
	 * @param expression Düzenli ifade
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi
	 */
	@NotNull
	static RegexBuilder regex(@NotNull String expression) {
		
		return Teddy.regex(expression);
	}
	
	/**
	 * Yeni bir {@link Range} nesnesi döndürür.
	 *
	 * @param expression İfade
	 * @return Yeni bir {@link Range}
	 */
	@NotNull
	static Range range(@NotNull String expression) {
		
		return Range.of(expression);
	}
	
	/**
	 * Yeni bir düzenli ifade nesnesi oluşturur.
	 *
	 * @param expression Düzenli ifade
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi
	 */
	static <T extends Text> @NotNull RegexBuilder regex(@NotNull T expression) {
		
		return Teddy.regex(expression);
	}
	
	/**
	 * Yeni bir {@link Range} nesnesi döndürür.
	 *
	 * @param expression İfade
	 * @param <T>        {@link Text}
	 * @return Yeni bir {@link Range}
	 */
	static <T extends Text> @NotNull Range range(@NotNull T expression) {
		
		return Range.of(expression);
	}
	
	/**
	 * @return Yeni bir aralık {@code [0-9]}
	 */
	static @NotNull Range rangeNumbers() {
		
		return Range.digits();
	}
	
	/**
	 * @return Yeni bir aralık {@code [a-zA-Z]}
	 */
	static @NotNull Range rangeLetters() {
		
		return Range.letters();
	}
	
	/**
	 * Verilen ifadeyi grup içine alarak yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi döndürür.
	 *
	 * @param expresion İfade
	 * @param <T>       {@link Text} türünden bir tür
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi {@code (expresion)}
	 */
	static <T extends Text> @NotNull RegexBuilder group(@NotNull T expresion) {
		
		return regex().group(expresion.getText()).toRegex();
	}
	
	/**
	 * Verilen ifadeyi grup içine alarak yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi döndürür.
	 *
	 * @param expresion İfade
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi {@code (expresion)}
	 */
	static @NotNull RegexBuilder group(@NotNull String expresion) {
		
		return regex().group(expresion).toRegex();
	}
	
	/**
	 * Verilen ifadeyi grup içine alarak yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi döndürür.
	 *
	 * @param groupName Grubun adı
	 * @param expresion İfade
	 * @param <T>       {@link Text} türünden bir tür
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi {@code (?<groupName>expresion)}
	 */
	static <T extends Text> @NotNull RegexBuilder group(@NotNull String groupName, @NotNull T expresion) {
		
		return regex().group(groupName, expresion.getText()).toRegex();
	}
	
	/**
	 * Verilen ifadeyi grup içine alarak yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi döndürür.
	 *
	 * @param groupName Grubun adı
	 * @param expresion İfade
	 * @return Yeni bir {@link com.tr.hsyn.regex.cast.Regex} nesnesi {@code (?<groupName>expresion)}
	 */
	static @NotNull RegexBuilder group(@NotNull String groupName, @NotNull String expresion) {
		
		return regex().group(groupName, expresion).toRegex();
	}
	
	/**
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder like() {
		
		return new Teddy("");
	}
	
	/**
	 * @param expression İfade
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static RegexBuilder like(@NotNull String expression) {
		
		return new Teddy(expression);
	}
	
	/**
	 * @param expression İfade
	 * @param <T>        {@link Text} türünden bir tür
	 * @return Yeni bir {@link RegexBuilder} nesnesi
	 */
	@NotNull
	static <T extends Text> RegexBuilder like(@NotNull T expression) {
		
		return new Teddy(expression);
	}
	
	static void main(String[] args) {
		
		test24();
	}
	
	static void test(@org.intellij.lang.annotations.Pattern("[a-z]") String str) {}
	
	static void test24() {
		
		var str = "Seni bu köyde 5 kez 8 yerde aradım";
		
		var regex = Range.letters().with(Range.of(Character.WHITE_SPACE)).negate();
		pl("Regex : %s", regex);
		pl("Result : %s", Regex.Dev.getParts(str, regex.toRegex().findAll(str)));
	}
	
	static void test23() {
		
		var str = "Seni bu bu köyde";
		test("12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları");
	}
	
	static void test21() {
		
		var str     = "123456HelloTest";
		var lazy    = Regex.like().digits().letters().lazy();
		var notLazy = Regex.like().digits().letters();
		
		pl("Lazy Regex      : %s", lazy);
		pl("Not Lazy Regex  : %s", notLazy);
		pl("Lazy Result     : %s", Regex.Dev.getParts(str, lazy.findAll(str)));
		pl("Not Lazy Result : %s", Regex.Dev.getParts(str, notLazy.findAll(str)));
	}
	
	static void test22() {
		
		var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
		var regex = Regex.like().digit(Quanta.ONE_OR_MORE).with(Look.ahead(":").negative());
		
		pl("Regex  : %s", regex);
		pl("Result : %s", Regex.Dev.getParts(str, regex.findAll(str)));
	}
	
	static void test20() {
		
		var str = "12 Nisan 1981";
		pl("Result : %s", RegexChar.DIGIT.retainFrom(str));
	}
	
	static void test19() {
		
		var hmt = HMT.builder()
				.head(Regex.like().boundary())
				.middle(Regex.like().punc().whiteSpace().toRange().negate().toRegex().oneOrMore().lazy())
				.tail(Regex.like().boundary())
				.build();
		
		var str    = "hello i am 42 years old. This is the 3point for me from 1981 in spring";
		var result = hmt.findAll(str);
		
		pl("Regex : %s", hmt.getRegex());
		pl("Index : %s", result);
		pl("Result : %s", Regex.Dev.getParts(str, result));
		
	}
	
	static void test18() {
		
		
		CoupleFinder _parser = new CoupleFinder(":", 3, 4);
		var          str1    = "01:0dfd45546d54f121124:49df55:9dfiü";
		CoupleFinder parser  = new CoupleFinder(Regex.like(":"), Regex.like("[0-9]").times(1, 2), Regex.like("[0-9]").times(1, 2));
		
		pl("Regex  : %s", parser.pattern.pattern());
		pl("Result : %s", parser.parseAll(str1));
		pl("Result : %s", parser.findAll(str1));
		
	}
	
	static void test17() {
		
		var str   = "123456789";
		var regex = Regex.regex(Regex.rangeNumbers().except(Regex.range("3-5")));
		pl("Regex  : %s", regex);
		pl("Result : %s", regex.matchesOf(str));
	}
	
	static void test16() {
		
		var str   = "123456789";
		var regex = Regex.regex(Regex.range("345").intersect("0-9"));
		pl("Regex  : %s", regex);
		pl("Result : %s", regex.matchesOf(str));
	}
	
	static void test15() {
		
		var str = "123go567come789looküğwpeor3948934711&/%+?&^%)?=^+?";
		
		pl(Edit.retainDigits(str));
	}
	
	static void test14() {
		
		var str   = "123go567come789look?";
		var regex = Teddy.regex().letter().oneOrMore().toGroup("word");
		
		pl(regex.replaceFrom(str, "#", 2));
	}
	
	static void test13() {
		
		var str   = "123goGo";
		var regex = Teddy.regex().with("[0-9]").oneOrMore();
		pl("Regex : %s", regex);
		var result = regex.group("repeat", "go").oneOrMore().ignoreCase().toGroup("all").test(str);
		pl("Result : %s", result);
		result = regex.ignoreCase().group("repeat", "go").oneOrMore().toGroup("all").test(str);
		pl("Result : %s", result);
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
		
		var range1 = group(range("0-2").toRegex().zeroOrMore().range("0-9"));
		var range2 = group(regex().with(3).range("0-1"));
		var date   = group(range1.or(range2));
		
		var month = regex().whiteSpaces().with(regex("N").or("n").toGroup()).with("isan");
		
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
		
		pl(m.findGroup(mySearch, "bebek"));
		
		pl("m : %s", m);
	}
	
	static void test4() {
		
		String mySearch = "nisa bebek";
		var    s        = Edit.overWrite(mySearch, "*");
		
		pl("overWrite : %s", s);
	}
	
	static void test3() {
		
		String mySearch = "nisa bebek";
		var    m        = regex().with("a").whiteSpace().zeroOrMore().with('b');
		
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
		
		var m = regex().with(range(regex().letter().control().whiteSpace()).negate());
		pl(m.replaceFrom("+ 9 0 5 4 3 4 9 3 7 5 3 0", "*"));
		
		pl(m);
	}
	
	static void pl(Object message, Object... args) {
		
		System.out.printf((message != null ? message.toString() : "null") + "%n", args);
	}
	
	
	/**
	 * Kendi içinde {@link com.tr.hsyn.regex.cast.Regex} sınıfını kullanarak bazı test metotları tanımlar.<br><br>
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
		 * Noboe : {@code null} <strong>o</strong>r  <strong>b</strong>lank <strong>o</strong>r  <strong>e</strong>mpty.
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
			
			RegexBuilder regex = regex();
			
			if (ignoreCase) regex.with(Modifier.modify().ignoreCase());
			
			if (ignoreSpaces) {
				for (var c : what.toString().toCharArray()) regex.whiteSpaces().with(c);
			}
			
			return regex.existIn(text);
		}
	}
	
	/**
	 * Kendi içinde {@link com.tr.hsyn.regex.cast.Regex} sınıfını kullanarak bazı değiştirme metotları tanımlar.<br>
	 */
	interface Edit {
		
		/**
		 * Yazının içinden, verilen karakter türüne ait karakterleri siler.
		 *
		 * @param sequence  Yazı
		 * @param regexChar Karakter türü
		 * @return Yeni bir string
		 */
		@NotNull
		static @Unmodifiable CharSequence remove(@NotNull String sequence, @NotNull RegexChar regexChar) {
			
			return regexChar.removeFrom(sequence);
		}
		
		/**
		 * Remove all characters that match the given regular expression.
		 *
		 * @param sequence the string to remove characters from
		 * @param regex    the regular expression to match characters to remove
		 * @return New string with all matching characters removed
		 */
		@NotNull
		static @Unmodifiable CharSequence remove(@NotNull String sequence, @NotNull String regex) {
			
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
		static @Unmodifiable String remove(String str, String regex, int limit) {
			
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
		static String removeDigits(String str) {
			
			return replace(str, "", Character.DIGIT);
		}
		
		@NotNull
		static String removeLetters(String str) {
			
			return replace(str, "", Character.LETTER);
		}
		
		/**
		 * Removes all non digit characters and returns the remains
		 *
		 * @param str String
		 * @return An object consisting only of numbers
		 */
		@NotNull
		static String retainDigits(String str) {
			
			if (str != null) return str.replaceAll(Character.NON_DIGIT, "");
			
			return "";
		}
		
		@NotNull
		static String retainLetters(String str) {
			
			if (str != null) return str.replaceAll(Character.NON_LETTER, "");
			
			return "";
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
			
			RegexBuilder regex = regex();
			RegexBuilder gr    = regex();
			
			
			if (source.length() <= 4) {
				
				regex.any(1).group("rp", gr.any().oneOrMore()).any(1).toRegex();
				//regex.any().times(1).with(group("rp").any().oneOrMore()).any().times(1);
			}
			else {
				regex.any(2).group("rp", gr.any().oneOrMore()).any(2).toRegex();
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
		
		static @NotNull String afterLast(@NotNull String text, @NotNull String after) {
			
			var index = text.lastIndexOf(after);
			
			if (index != -1) return text.substring(index + 1);
			
			return "";
		}
		
		static @NotNull String afterFirst(@NotNull String text, @NotNull String after) {
			
			var index = text.indexOf(after);
			
			if (index != -1) return text.substring(index + 1);
			
			return "";
		}
		
		static @NotNull String beforeLast(@NotNull String text, @NotNull String after) {
			
			var index = text.lastIndexOf(after);
			
			if (index != -1) return text.substring(0, index);
			
			return "";
		}
		
		static @NotNull String beforeFirst(@NotNull String text, @NotNull String after) {
			
			var index = text.indexOf(after);
			
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
		static List<String> getParts(@NotNull String str, @NotNull List<Index> indices) {
			
			List<String> list = new ArrayList<>();
			
			if (str.isBlank()) return list;
			
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
			
			RegexBuilder regex = regex();
			
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
							regex.with(clazz).times(lastCharacterCount).toRegex();
						}
						
						continue;
					}
					
					var r = regex.getText();
					
					if (!Quanta.isQuantifier(r.charAt(r.length() - 1))) regex.oneOrMore().toRegex();
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
							
							regex.with(lastCharacterClass).times(lastCharacterCount).toRegex();
							
							lastCharacterCount = 1;
						}
					}
					else {
						//- Kesin sayı istenmiyor, direk ekleyebiliriz
						regex.with(clazz).toRegex();
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
				
				Regex.pl("Class : %66s - %-2c - %-2d", clazz, c, code);
				
				codes.add(code);
			}
			
			return codes.toArray(new Integer[0]);
		}
	}
	
	
}
