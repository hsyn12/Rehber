package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.Regex;
import com.tr.hsyn.regex.cast.Index;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * <strong>H</strong>ead - <strong>M</strong>iddle - <strong>T</strong>ail.<br>
 * Düzenli bir ifade modelinin başını ortasını ve sonunu tarif etmek için kullanılır.<br>
 * <p>
 * Sınıfı kullanmadan önce bilinmesi gereken hususlar şunlardır :
 *
 * <ul>
 *    <li>Hazırlanan modelin bir başı bir ortası ve bir sonu olmak zorunda.
 *    Ancak sınıfı kurarken belirtilmek zorunda değil.</li>
 *
 *    <li>
 *       Sınıfı kurarken belirtilen bölümler olduğu gibi kabul edilir,
 *       belirtilmeyen bölümler için {@link Regex#ANY} {@code + "+"} düzenli ifadesi kullanılır.
 *       Bu düzenli ifadenin herhangi bir harf, sayı, noktalama, boşluk veya {@code _} alt tire karakterlerinden
 *       kabul ettiğine ve bu karakterlerden herhangi birinden en az bir tane olması gerektiğine dikkat edilmeli.
 *       Yani bir bölüm belirtilmese bile o bölümde herhangi bir karakter geçmek zorunda.
 *    </li>
 * </ul>
 * <p>
 * <p>
 *    <pre>
 * var hmt = HMT.builder()
 *     .head(Nina.like().boundary().digit().oneOrMore().boundary())
 *     .tail(Nina.like().letter().oneOrMore().with("?").boundary())
 *     .build();
 *
 * var str    = "hello i am 42 years old. This is the 3point for me from 1981 in spring";
 * var result = hmt.checkOn(str);
 *
 * pl("Regex  : %s", hmt.getRegex());
 * pl("Index  : %s", result);
 * pl("Result : %s", Nina.Dev.getParts(str, result));
 *
 * //Regex   : (\b\p{N}+\b)([\p{L}\p{N}\p{P}\p{Z}_]+?)(\p{L}+?\b)
 * //Index   : [Index{start=11, end=19}, Index{start=56, end=63}]
 * //Result  : [42 years, 1981 in]
 * </pre>
 */
public class HMT {
	
	private final String head;
	private final String middle;
	private final String tail;
	
	private HMT(String head, String middle, String tail) {
		
		this.head   = head;
		this.middle = middle;
		this.tail   = tail;
	}
	
	/**
	 * Yeni bir {@code HMT} nesnesi oluşturmak için bir builder nesnesi döndürür.<br>
	 *
	 * @return {@link HMTBuilder}
	 */
	@NotNull
	public static HMTBuilder builder() {
		
		return new HMTBuilder();
	}
	
	/**
	 * @return Düzenli ifade
	 */
	public String getRegex() {
		
		return createRegex().getText();
	}
	
	@NotNull
	private RegexBuilder createRegex() {
		
		return Regex.like()
				.group(head)
				.group(middle)
				.group(tail);
	}
	
	/**
	 * Modelin sadece başlangıç kısmıyla eşleşen yerlerin index'lerini döndürür.<br>
	 * Öncelikle modelin tamamı eşleşmiş olmalı, eşleşen modelden başlangıç kısımlarını döndürür.<br>
	 *
	 * <pre>
	 * var hmt = HMT.builder()
	 *     .head(Nina.like().boundary().digit().oneOrMore().boundary())
	 *     .tail(Nina.like().letter().oneOrMore().with("?").boundary())
	 *     .build();
	 *
	 * var str    = "hello i am 42 years old. This is the 3point for me from 1981 in spring";
	 * var result = hmt.findHeads(str);
	 *
	 * pl("Regex  : %s", hmt.getRegex());
	 * pl("Index  : %s", result);
	 * pl("Result : %s", Nina.Dev.getParts(str, result));
	 *
	 * //Regex   : (\b\p{N}+\b)([\p{L}\p{N}\p{P}\p{Z}_]+?)(\p{L}+?\b)
	 * //Index   : [Index{start=11, end=13}, Index{start=56, end=60}]
	 * //Result  : [42, 1981]
	 * </pre>
	 *
	 * @param str Test edilecek string
	 * @return Bulunan eşleşmeler
	 */
	public List<Index> findHeads(@NotNull String str) {
		
		return createRegex().findGroup(str, 1);
	}
	
	/**
	 * Modelin sadece bitiş kısmıyla eşleşen yerlerin index'lerini döndürür.<br>
	 * Öncelikle modelin tamamı eşleşmiş olmalı, eşleşen modelden başlangıç kısımlarını döndürür.<br>
	 *
	 * <pre>
	 * var hmt = HMT.builder()
	 *     .head(Nina.like().boundary().digit().oneOrMore().boundary())
	 *     .tail(Nina.like().letter().oneOrMore().with("?").boundary())
	 *     .build();
	 *
	 * var str    = "hello i am 42 years old. This is the 3point for me from 1981 in spring";
	 * var result = hmt.findTails(str);
	 *
	 * pl("Regex  : %s", hmt.getRegex());
	 * pl("Index  : %s", result);
	 * pl("Result : %s", Nina.Dev.getParts(str, result));
	 *
	 * //Regex   : (\b\p{N}+\b)([\p{L}\p{N}\p{P}\p{Z}_]+?)(\p{L}+?\b)
	 * //Index   : [Index{start=14, end=19}, Index{start=61, end=63}]
	 * //Result  : [years, in]
	 * </pre>
	 *
	 * @param str Test edilecek string
	 * @return Bulunan eşleşmeler
	 */
	public List<Index> findTails(@NotNull String str) {
		
		return createRegex().findGroup(str, 3);
	}
	
	/**
	 * Modelin sadece orta kısmıyla eşleşen yerlerin index'lerini döndürür.<br>
	 * Öncelikle modelin tamamı eşleşmiş olmalı, eşleşen modelden başlangıç kısımlarını döndürür.<br>
	 *
	 * <pre>
	 * var hmt = HMT.builder()
	 *     .head(Nina.like().boundary().digit().oneOrMore().boundary())
	 *     .tail(Nina.like().letter().oneOrMore().with("?").boundary())
	 *     .build();
	 *
	 * var str    = "hello i am 42 years old. This is the 3point for me from 1981 in spring";
	 * var result = hmt.findMiddles(str);
	 *
	 * pl("Regex  : %s", hmt.getRegex());
	 * pl("Index  : %s", result);
	 * pl("Result : %s", Nina.Dev.getParts(str, result));
	 *
	 * //Regex   : (\b\p{N}+\b)([\p{L}\p{N}\p{P}\p{Z}_]+?)(\p{L}+?\b)
	 * //Index   : [Index{start=13, end=14}, Index{start=60, end=61}]
	 * //Result : [ ,  ]
	 * </pre>
	 *
	 * @param str Test edilecek string
	 * @return Bulunan eşleşmeler
	 */
	public List<Index> findMiddles(@NotNull String str) {
		
		return createRegex().findGroup(str, 2);
	}
	
	/**
	 * Düzenli ifadeyi string üzerinde test eder ve bulduğu eşleşmelerin yerlerini döndürür.
	 *
	 * @param str Test edilecek string
	 * @return Eşleşmelerin yerleri
	 */
	@NotNull
	public List<Index> findAll(@NotNull String str) {
		
		return Regex.regex(getRegex()).findAll(str);
	}
	
	/**
	 * {@code HMT} builder class
	 */
	public static final class HMTBuilder {
		
		private String head   = Regex.ANY + "+?";
		private String middle = Regex.ANY + "+?";
		private String tail   = Regex.ANY + "+?";
		
		private HMTBuilder() {}
		
		/**
		 * Yazının başlangıcını tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder head(String regex) {
			
			if (!Regex.Test.isNoboe(regex)) this.head = regex;
			return this;
		}
		
		/**
		 * Yazının orta kısmını (başlangıç kısmından hemen sonrasını) tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder middle(@NotNull Text regex) {
			
			if (!Regex.Test.isNoboe(regex.getText())) this.middle = regex.getText();
			return this;
		}
		
		/**
		 * Yazının sonunu tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder tail(@NotNull Text regex) {
			
			if (!Regex.Test.isNoboe(regex.getText())) this.tail = regex.getText();
			return this;
		}
		
		/**
		 * Yazının başlangıcını tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder head(@NotNull Text regex) {
			
			if (!Regex.Test.isNoboe(regex.getText())) this.head = regex.getText();
			return this;
		}
		
		/**
		 * Yazının orta kısmını (başlangıç kısmından hemen sonrasını) tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder middle(String regex) {
			
			if (!Regex.Test.isNoboe(regex)) this.middle = regex;
			return this;
		}
		
		/**
		 * Yazının sonunu tarif eden düzenli ifadeyi set eder.
		 *
		 * @param regex Düzenli ifade
		 * @return {@code HMTBuilder}
		 */
		public HMTBuilder tail(String regex) {
			
			if (!Regex.Test.isNoboe(regex)) this.tail = regex;
			return this;
		}
		
		/**
		 * @return Yeni bir {@link HMT} nesnesi
		 */
		@NotNull
		public HMT build() {
			
			return new HMT(head, middle, tail);
		}
	}
}
