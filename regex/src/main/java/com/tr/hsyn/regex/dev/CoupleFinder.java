package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Index;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Yazının içinde geçen bağlı çiftleri bulmayı sağlar.
 */
public class CoupleFinder {
	
	/**
	 * Sol tarafın grup ismi
	 */
	private static final String LEFT        = "left";
	/**
	 * Sağ tarafın grup ismi
	 */
	private static final String RIGHT       = "right";
	/**
	 * Sol tarafın düzenli ifadesi
	 */
	private static final String LEFT_REGEX  = "[a-zA-Z0-9]";
	/**
	 * Sağ tarafın düzenli ifadesi
	 */
	private static final String RIGHT_REGEX = "[a-zA-Z0-9]";
	
	/**
	 * Çiftleri birbirine bağlayan bağ
	 */
	public final String  link;
	/**
	 * Bağın sol taraf uzunluğu
	 */
	public final int     distanceLeft;
	/**
	 * Bağın sağ taraf uzunluğu
	 */
	public final int     distanceRight;
	/**
	 * Arama modeli
	 */
	public final Pattern pattern;
	
	/**
	 * Yeni bir {@code CoupleFinder} nesnesi oluşturur.<br><br>
	 *
	 * <pre>
	 * var     str1         = "01:0dfd45546d54f121124:49df55:9dfiü";
	 * CoupleFinder parser  = new CoupleFinder(":", 2, 2);
	 *
	 * pl("Regex  : %s", parser.pattern.pattern());//Regex  : (?<left>[a-zA-Z0-9]{1,2}+):(?<right>[a-zA-Z0-9]{1,2}+)
	 * pl("Result : %s", parser.parseAll(str1));//Result : [01:0d, 24:49, 55:9d]
	 * pl("Result : %s", parser.findAll(str1));//Result : [Index{start=0, end=5}, Index{start=20, end=25}, Index{start=27, end=32}]
	 * </pre>
	 *
	 * <p>
	 * <p>
	 * Bu kurucu ile oluşturulan nesneler varsayılan olarak çiftleri harf ve sayı olarak seçer
	 * ve çiftlerin her birinden en az 1 tane olmasını ister. En fazla ise verilen uzunluklarda olacaktır.
	 * Yani her bir çift en az 1 karakter en fazla belirtilen uzunlukta olacak ve
	 * daima en uzun olan parça alınacaktır.<br><br>
	 *
	 * <pre>
	 * var          str1    = "01:0dfd45546d54f121124:49df55:9dfiü";
	 * CoupleFinder parser  = new CoupleFinder(":", 3, 4);
	 *
	 * pl("Regex  : %s", parser.pattern.pattern());//Regex  : (?<left>[a-zA-Z0-9]{1,3}+):(?<right>[a-zA-Z0-9]{1,4}+)
	 * pl("Result : %s", parser.parseAll(str1));//Result : [01:0dfd, 124:49df, 55:9dfi]
	 * pl("Result : %s", parser.findAll(str1));//Result : [Index{start=0, end=7}, Index{start=19, end=27}, Index{start=27, end=34}]
	 * </pre>
	 *
	 * <p>
	 * <p>
	 * Görüldüğü gibi, varsa en uzun parçayı, yoksa daha kısa olanı seçiyor.
	 * Taaki bağ ile bağlanmış en az 1 karakter kalana kadar {@code x:x}.
	 * Burada karakter seçimi değişmez bir değerdir,
	 * harf veya sayı seçer. Eğer bu seçimin farklı olması isteniyorsa,
	 * sınıfın {@link CoupleFinder#CoupleFinder(Text, Text, Text)}
	 * kurucusu kullanılmalıdır.
	 *
	 * @param link          Bağ
	 * @param distanceLeft  Sol taraf uzunluğu
	 * @param distanceRight Sağ taraf uzunluğu
	 */
	public CoupleFinder(String link, int distanceLeft, int distanceRight) {
		
		this.link          = link != null && !link.isEmpty() ? link : ":";
		this.distanceLeft  = distanceLeft;
		this.distanceRight = distanceRight;
		pattern            = createPattern(this.link, distanceLeft, distanceRight);
	}
	
	/**
	 * Yeni bir {@code CoupleFinder} nesne oluşturur.<br><br>
	 *
	 * <pre>
	 * var          str1    = "01:0dfd45546d54f121124:49df55:9dfiü";
	 * CoupleFinder parser  = new CoupleFinder(Nina.like(":"), Nina.like("[0-9]").times(1, 2), Nina.like("[0-9]").times(1, 2));
	 *
	 * pl("Regex  : %s", parser.pattern.pattern());//Regex  : (?<left>[0-9]{1,2}):(?<right>[0-9]{1,2})
	 * pl("Result : %s", parser.parseAll(str1));//Result : [01:0, 24:49, 55:9]
	 * pl("Result : %s", parser.findAll(str1));//Result : [Index{start=0, end=4}, Index{start=20, end=25}, Index{start=27, end=31}]
	 * </pre>
	 *
	 * @param linkRegex  Bağ düzenli ifadesi
	 * @param leftRegex  Sol taraf düzenli ifadesi
	 * @param rightRegex Sağ taraf düzenli ifadesi
	 */
	public CoupleFinder(@NotNull Text linkRegex, @NotNull Text leftRegex, @NotNull Text rightRegex) {
		
		this.link          = "";
		this.distanceLeft  = 0;
		this.distanceRight = 0;
		pattern            = createPattern(linkRegex, leftRegex, rightRegex);
	}
	
	/**
	 * Yazının içindeki {@link #link} ile bağlı çifti döndürür.
	 *
	 * @param str Yazı
	 * @return {@link Couple}
	 */
	@NotNull
	public Couple parse(@NotNull String str) {
		
		var match = pattern.matcher(str);
		
		if (match.find()) {
			
			var left  = match.group("left");
			var right = match.group("right");
			
			return new Couple(left, right);
		}
		
		return new Couple("", "");
	}
	
	/**
	 * Yazının içindeki {@link #link} ile bağlı geçen tüm çifleri döndürür.
	 *
	 * @param str Yazı
	 * @return {@link Couple} listesi
	 */
	@NotNull
	public List<Couple> parseAll(@NotNull String str) {
		
		var          match   = pattern.matcher(str);
		List<Couple> couples = new ArrayList<>();
		
		while (match.find()) {
			
			var left  = match.group("left");
			var right = match.group("right");
			
			couples.add(new Couple(left, right));
		}
		
		return couples;
	}
	
	/**
	 * Yazı içindeki {@link #link} ile bağlı ilk çiftin index'ini döndürür.
	 *
	 * @param str Yazı
	 * @return {@link Index}
	 */
	@Nullable
	public Index find(@NotNull String str) {
		
		return find(str, 0);
	}
	
	/**
	 * Yazı içindeki {@link #link} ile bağlı ilk çiftin index'ini döndürür.
	 *
	 * @param str        Yazı
	 * @param startIndex Aramanın başlayacağı index
	 * @return {@link Index}
	 */
	@Nullable
	public Index find(@NotNull String str, int startIndex) {
		
		var match = pattern.matcher(str);
		
		if (match.find(startIndex)) {
			
			return new Index(match.start(), match.end());
		}
		
		return null;
	}
	
	/**
	 * Yazı içindeki {@link #link} ile bağlı tüm çiftlerin yerlerini döndürür.
	 *
	 * @param str Yazı
	 * @return {@link Index} listesi
	 */
	@NotNull
	public List<Index> findAll(@NotNull String str) {
		
		var         match = pattern.matcher(str);
		List<Index> list  = new ArrayList<>();
		
		while (match.find()) list.add(new Index(match.start(), match.end()));
		
		return list;
	}
	
	/**
	 * Çiftleri bulmak için gerekli olan modeli döndürür.
	 * Modelin oluşturulabilmesi için çiftlerin ne ile bağlı olduğunu
	 * ve bu bağın sol ve sağ taraf uzunluğunun bilinmesi gerekir.
	 *
	 * @param link          Bağ
	 * @param distanceLeft  Sol taraf uzunluğu
	 * @param distanceRight Sağ taraf uzunluğu
	 * @return Eşleşme modeli
	 */
	@NotNull
	@SuppressWarnings("DefaultLocale")
	private Pattern createPattern(@NotNull String link, int distanceLeft, int distanceRight) {
		//? ((LEFT [0-9]{x,y}):(RIGHT [0-9]{x,y}))
		
		String leftDistance  = String.format("{1,%d}+", distanceLeft);
		String rightDistance = String.format("{1,%d}+", distanceRight);
		
		RegexBuilder regex =
				Nina.like()
						.group(LEFT, String.format("%s%s", LEFT_REGEX, leftDistance))
						.with(link)
						.group(RIGHT, String.format("%s%s", RIGHT_REGEX, rightDistance));
		
		return Pattern.compile(regex.getText());
	}
	
	@NotNull
	@SuppressWarnings("DefaultLocale")
	private Pattern createPattern(@NotNull Text linkRegex, @NotNull Text leftRegex, @NotNull Text rightRegex) {
		
		RegexBuilder regex =
				Nina.like()
						.group(LEFT, leftRegex)
						.with(linkRegex)
						.group(RIGHT, rightRegex);
		
		return Pattern.compile(regex.getText());
	}
}
