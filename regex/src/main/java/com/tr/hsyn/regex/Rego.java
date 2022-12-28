package com.tr.hsyn.regex;


import com.tr.hsyn.regex.cast.Alternate;
import com.tr.hsyn.regex.cast.Character;
import com.tr.hsyn.regex.cast.CharacterSet;
import com.tr.hsyn.regex.cast.DateGenerator;
import com.tr.hsyn.regex.cast.Modifier;
import com.tr.hsyn.regex.cast.Quantifier;
import com.tr.hsyn.regex.cast.Range;
import com.tr.hsyn.regex.cast.Regex;
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
 * <h1>Rego</h1>
 *
 * <p>
 * Rego; düzenli ifadeler araştırma geliştirme yatırım AŞ. LDT. ŞTİ. genel müdürlüğü yöneticisi.<br>
 * ---------------------------------------------------------------------------------------------------------------------<br>
 * <p>
 * Harfler heceleri, heceler kelimeleri, kelimeler cümleleri, cümleler ifadeleri oluşturur.<br>
 * Gözlerimiz cümleler içinde kelimeleri, kelimeler içinde heceleri, heceler içinde harfleri bulur.<br>
 * Ve her biri sadece bizler için bir anlam ifade eder.<br>
 * Dijital ortamlar açısından bunların hiçibiri için bir <strong>anlam</strong>dan söz etmek mümkün değildir.<br>
 * Bir makine bir kelimeye <strong>anlam</strong> yükleyemez. Ancak onu temsil edebilir.<br>
 * Ve biz bu temsiller üzerinde arama yapabiliriz. Ve arama kriterini de temsillerle belirleyebiliriz.<br>
 * {@code Rego} sınıfının ihtisas alanı işte bu temsillerdir.<br>
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
 * Dedikki en temel eleman <strong>harfler</strong>dir, o halde sistemin bize harflerin temsili için
 * bazı araçlar sunması gerek. <br>
 * İşte tam bu noktada <strong>Düzenli ifadelere hoşgeldiniz</strong> tabelasnıı görmüş olman gerekiyor.<br>
 * Java dili bize bu temsil sistemini 'düzenli ifadeler' başlığı altında sunar.<br>
 * Harflerin temsili 'karakter sınıfı' kategorisiyle başlar.
 * Bu katagorideki en temel temsiller şöyle : <br><br>
 *
 * <pre><code>
 *    .    -   Yeni satır karakteri hariç herhangi bir karakteri temsil eder.
 *    \w   -   Harf ve sayı ve alt tire (_) karakterini temsil eder.
 *    \d   -   Sayı karakterini temsil eder.
 *    \s   -   Boşluk karakterini temsil eder.
 * </code></pre><br>
 * <p>
 * Bunlar bu haliyle sadece ve sadece tek bir karakteri temsil ederler.
 * Liste elbette bundan uzun.<br>
 * Harflere nicelik özelliği ekleyebilmemiz için bir de 'niceleyiciler' kategorisi vardır.
 * Niceleyiciler, hergangi bir harften yada harf temsilinden sonra gelirler ve
 * o harfin sayısal olarak ne kadar çoklukta olması gerektiğini bildirirler.<br>
 *
 * <pre><code>
 *    ?    -   Kendisinden önce gelen karakterin {@code 0} yada {@code 1} tane olması gerektiğini belirtir.
 *    +    -   Kendisinden önce gelen karakterin en az {@code 1} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 *    *    -   Kendisinden önce gelen karakterin en az {@code 0} en çok ise sınırsız sayıda olması gerektiğini belirtir.
 * </code></pre><br>
 * <p>
 * Buna ek olarak, tam olarak kaç adet harf gerektiğini belirtmek istersek, {@code {adet}} kullanabiliriz.<br>
 * Örneğimize dönersek, arayacağımız kelime {@code 'XX Nisan'}.
 * Buradaki 'XX'ler, oraya sayı geleceğini belirtmekte ve bu sayılar en az bir en çok iki olmalı.<br>
 * Buna göre düzenli ifademiz şöyle olabilir : {@code \d{1,2}\sNisan}<br>
 * Küme parantezlerinin içine en az ve en çok geçebilecek sayıyı yazdık.<br>
 * {@code \d, \s} gibi karakterler, düzenli ifadeler konusu içesinde {@code meta karakterler} olarak adlandırılıyor.<br>
 * Yani bunlar bizim bildiğimiz anlamdaki harflerin temsil sistemi altındaki karşıllığdır.<br>
 * Örneğimizi bundan çok daha farklı şekillerde de yazabiliriz ancak
 * basitlik ve özet geçme açısından sadece bir bakış atma amacı taşımakta olduğundan daha ileri gitmiyoruz.<br>
 * Burada ana tema, harflerin bir şekilde temsil edilebilmesi için daha soyut bir düşünce benimsiyor olmamız.<br>
 * Bu soyutlama bizi gerçeklerle ilgili daha derin düşünmeye sevk edebilir.<br>
 * Neticede herşey temsillerden ibaret.
 * Fakat biz bunları doğduğumuz günden beri kullanıyor olduğumuzdan olayın derinini farketmiyoruz.<br>
 * Belki bu çalışma bize yeni bir bakış açısı kazandırır.<br><br>
 *
 * <h2>Düzenli İfadeler</h2>
 *
 * <p> Bir düzenli ifade, bir harfi, bir heceyi yada bütün olarak bir kelimeyi yada kelime grubunu tarif edebilir.<br>
 * Daha da ileri gidip <strong>ifadeleri</strong> tarif eden bir düzenli ifade yazmak da mümkündür,<br>
 * ancak bu düzenli ifade o kadar karmaşık olacaktır ki
 * bunun için çok daha soyut düşünmeyi gerektirecektir.<br>
 * {@code Rego} sınıfı uzun ve karmaşık düzenli ifadeler yazmayı kolaylaştırır.<br>
 * Ancak karmaşıklık düzenli ifadelerin temelinde var ve ifadeler ilerledikçe daha fazla karmaşıklık kaçınılmazdır.<br>
 * <br>
 * Bir yazı tarif eden düzenli ifadelere {@code Patterns} denilir.<br>
 * Türkçe olarak <i>desen, kalıp</i> diyoruz.<br>
 * Bu da bize bahsettiğimiz soyutlamayı vurgulamaktadır.<br>
 * Düzenli ifade oluşturmak için bir <b>pattern</b> bir desen tanımlamamız gerek.<br>
 *
 * @author hsyn 14 Haziran 2022 Salı 11:18
 */
public interface Rego {

    /**
     * Regular expression for digits.
     */
    String NUMBER = "^\\p{N}+$";
    /**
     * Regular expression for alphabetics.
     */
    String WORD   = "^\\p{L}+$";

    static void main(String[] args) {

        test6();
    }

    static void test10() {

        var r = newRegex().letterUpper().atLeast(4).digit().atLeast(4);

        pl("Regex : %s", r);
        //var generation = new WordGeneration();

    }

    static void test9() {

        String pattern = "(?<name>\\p{L}+)(?=\\P{L}+)?";
        String text    = "ali, ()?    veli,,,, deli,,,, keli";

        var l = newRegex(pattern).matchesOf(text);

        pl("Result : %s", l);
    }

    static void test7() {

        String mySearch = "merhaba dünya";

        var m = Dev.toRegex(mySearch, true);

        pl("m : %s", m);
    }

    static void test6() {


        System.out.println(System.getProperty("file.encoding"));
        String myWord = "@sa{?=)/";

        var generator = WordGenerator.ofLike(myWord).setCharacterSet("+");

        pl("Word       : %s", myWord);
        pl("RegexCodes : %s", Arrays.toString(generator.getRegexCodes()));

        for (int i = 0; i < 10; i++) {
            var word = generator.newWord();

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

        var m = newRegex().add("nisa\\s").addGroup(newRegex().add(Alternate.of('c', 'B')), "bebek").any().oneOrMore();

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
        var    m        = newRegex().add("a").space().zeroOrMore().add('b');

        pl("Match : '%s'", m.test(mySearch));
        pl("Remove : '%s'", m.replaceFrom(mySearch, "*"));

        var indexes = m.indexesOf(mySearch);
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

        var m = Regex.of().letter().control().space().asRangeNegated();
        pl(m.replaceFrom("+ 9 0 5 4 3 4 9 3 7 5 3 0", "*"));

        pl(m);
    }

    static void pl(Object message, Object... args) {

        System.out.printf((message != null ? message.toString() : "null") + "%n", args);
    }

    @NotNull
    static Regex newRegex() {

        return Regex.of();
    }

    @NotNull
    static Regex newRegex(@NotNull String str) {

        return Regex.of(str);
    }

    interface Test {

        /**
         * Test a string for a regular expression.
         *
         * @param str   the string to test
         * @param regex the regular expression to test the string against
         * @return true if the string matches the regular expression, false otherwise
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
         * @return true if the text is {@code null} or blank or empty, false otherwise
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

            Regex regex = newRegex();

            if (ignoreCase) regex.add(Modifier.of().ignoreCase());

            if (ignoreSpaces) {
                for (var c : what.toString().toCharArray()) regex.space().oneOrMore().add(c);
            }

            return regex.containsIn(text);
        }
    }

    interface Edit {

        /**
         * Remove all characters that match the given regular expression.
         *
         * @param sequence the string to remove characters from
         * @param regex    the regular expression to match characters to remove
         * @return the string with all matching characters removed
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
         * @return the string with all matching characters removed
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
         * @return the string with all matching characters replaced
         */
        @NotNull
        static String replace(String str, String replacement, String regex, int limit) {

            if (str == null || str.isEmpty()) return "";
            if (regex == null) return str;
            if (replacement == null) return str;

            return Regex.of(regex).replaceFrom(str, replacement, limit);
        }

        /**
         * Replace all characters that match the given regular expression with the given replacement.
         *
         * @param str         the string to replace characters in
         * @param replacement the string to replace matching characters with
         * @param regex       the regular expression to match characters to replace
         * @return the string with all matching characters replaced
         */
        @NotNull
        static String replace(String str, String replacement, String regex) {

            return replace(str, replacement, regex, 0);
        }

        /**
         * Removes all white spaces.
         *
         * @param str the string to remove white spaces from
         * @return the string without white spaces
         */
        @NotNull
        static String removeWhiteSpaces(String str) {

            return replace(str, "", Character.WHITE_SPACE);
        }

        /**
         * Removes all digit characters.
         *
         * @param str the string to remove digit characters from
         * @return the string without digit characters
         */
        @NotNull
        static String removeNumerics(String str) {

            return replace(str, "", Character.DIGIT);
        }

        /**
         * Removes all alpha characters from the string.
         *
         * @param str the string to remove alpha characters from
         * @return the string without alpha characters
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
         * <pre><code>
         * overwrite("12345", "*");           // returns "12*45"
         * overwrite("1234567", "*");         // returns "12***67"
         * overwrite("1234567897654", "*");   // returns "12*********54"
         * overwrite("1234", "*");            // returns "1**4"
         * overwrite("123", "*");             // returns "1*3"
         * overwrite("12", "*");              // returns "12"
         * overwrite("1", "*");               // returns "1"
         * </code></pre>
         *
         * @param source      the string to overwrite
         * @param replacement the character to use to overwrite
         * @return the overwritten string
         */
        @NotNull
        static String overWrite(String source, String replacement) {

            if (replacement == null || source == null || source.toString().isEmpty()) return "";

            Regex regex = newRegex();

            if (source.length() <= 4) {
                regex.any().times(1).add(Regex.of().any().oneOrMore().asGroup("rp")).any().times(1);
            }
            else {
                regex.any().times(2).add(Regex.of().any().oneOrMore().asGroup("rp")).any().times(2);
            }

            Matcher m = Pattern.compile(regex.getRegex()).matcher(source);

            if (m.find())
                return new StringBuilder(source).replace(m.start("rp"), m.end("rp"), replacement.toString().repeat(m.group("rp").length())).toString();

            return source;
        }
    }

    interface Dev {

        /**
         * Returns regular expression which equvalent to the given string.
         *
         * @param str the string to convert to regular expression
         * @return the regular expression
         */
        static @NotNull Regex toRegex(String str) {

            return toRegex(str, false);
        }

        static @NotNull Regex toRegex(String str, boolean exactQuantifiers) {

            Regex regex = newRegex();

            if (str == null) return regex;

            char[] chars              = str.toCharArray();
            String lastCharacterClass = "";
            int    lastCharacterCount = 1;

            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];

                var clazz = Character.getCharacterClass(c);

                if (lastCharacterClass.equals(clazz)) {
                    if (exactQuantifiers) {
                        //- E?er karakter i?in kesin say? isteniyorsa
                        //- buradan lastCharacterCount de?erini bir artt?r?p
                        //- d?ng?n?n ba??na d?nmeliyiz.
                        //- Bu ko?ulda i?lem lastCharacterClass != clazz
                        //- oldu?unda yap?lmal?d?r.
                        //- D?zenli ifadeye burada bir ?ey eklenmedi?ine dikkat et.
                        //- lastCharacterClass da de?i?miyor ??nk? zaten ayn?.

                        lastCharacterCount++; //! Sadece burada arttırılıyor.

                        //- Sona kalan dona kalmasın
                        if (i == chars.length - 1) {
                            regex.add(clazz).times(lastCharacterCount);
                        }

                        continue;
                    }

                    var r = regex.getRegex();

                    if (!Quantifier.isQuantifier(r.charAt(r.length() - 1))) regex.add(Quantifier.ONE_OR_MORE);
                }
                else {
                    //- Uyu?mazl?k var.

                    if (exactQuantifiers) {
                        //- Karakter i?in kesin say? isteniyor.
                        //- Bu durumda lastCharacterClass de?erini d?zenli ifadeye eklemeliyiz.

                        //- Bu ilk d?ng? m??
                        if (!lastCharacterClass.isEmpty()) {
                            //- ?lk d?ng? de?il.
                            //- Gerekli bilgileri ekliyoruz.

                            regex.add(lastCharacterClass).times(lastCharacterCount);

                            lastCharacterCount = 1;
                        }
                    }
                    else {
                        regex.add(clazz);
                    }
                }

                lastCharacterClass = clazz;
            }

            return regex;
        }

        static Integer @NotNull [] toRegexCodes(CharSequence sequence) {

            return toRegexCodes(sequence, false, false);
        }

        static Integer @NotNull [] toRegexCodes(CharSequence sequence, boolean specific, boolean moreSpecific) {

            if (sequence == null) return new Integer[0];

            List<Integer> codes = new ArrayList<>();

            char[] chars = sequence.toString().toCharArray();

            for (char c : chars) {
                var clazz = CharacterSet.getCharecterSet(c, specific, moreSpecific);

                int code = CharacterSet.getCharacterCode(clazz);

                Rego.pl("Class : %66s - %-2c - %-2d", clazz, c, code);

                codes.add(code);
            }

            return codes.toArray(new Integer[0]);
        }
    }
}
