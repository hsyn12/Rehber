package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


/**
 * Zero width assertions.<br>
 * <p>
 * Düzenli ifadelere ayrıca bir kontrol ifadesi eklemeyi sağlar.
 * Bu eklenen kontrol ifadeleri <em>Regex Engine</em> tarafından yakalanmaz,
 * sadece ifadenin doğruluğu veya yanlışlığını kontrol eder.<br>
 * Bu şekilde bir düzenli ifadenin sağında yada solunda arzu edilen kontrol yapılarak daha
 * spesifik eşleşmeler bulunur.<br><br>
 *
 * <pre>
 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
 * var regex = Nina.like(Look.behind(":")).digits();
 * // Düzenli ifade, string içindeki sayıları istiyor.
 * // Ancak sayının hemen gerisinde {@code :} iki nokta üstüste işareti olsun istiyor.
 * // Eğer gerisinde {@code :} iki nokta üstüste işareti olsun <u>istemiyorsak</u>
 * // {@code Look.ahead(":").negative()} şeklinde kontrol ifadesini olumsuz da yapabiliriz
 *
 * pl("Regex  : %s", regex);//Regex  : (?<=:)\p{N}+
 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [45]
 * // {@code :} iki nokta üstüste ifadesi yakalanmadığı için sonuca dahil edilmiyor
 * </pre>
 */
public interface Look extends Text {
	
	/**
	 * Eşleşmenin gerisine bakar.<br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like(Look.behind(":")).digits();
	 *
	 * pl("Regex  : %s", regex);//Regex  : (?<=:)\p{N}+
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [45]
	 * </pre>
	 *
	 * @param regex Kontrol edilecek düzenli ifade
	 * @return Yeni bir {@link Look} nesnesi
	 */
	static @NotNull Look behind(@NotNull String regex) {
		
		return new Watcher(regex, false, false);
	}
	
	/**
	 * Eşleşmenin gerisine bakar.<br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like(Nina.like().puncs()).digits();
	 *
	 * pl("Regex  : %s", regex);//Regex  : (?<=\p{P}+)\p{N}+
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [45]
	 * </pre>
	 *
	 * @param regex Kontrol edilecek düzenli ifade
	 * @param <T>   {@link Text}
	 * @return Yeni bir {@link Look} nesnesi
	 */
	static <T extends Text> @NotNull Look behind(@NotNull T regex) {
		
		return behind(regex.getText());
	}
	
	/**
	 * Eşleşmenin ilerine bakar.<br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like().digits().with(Look.ahead(":"));
	 *
	 * pl("Regex  : %s", regex);//Regex  : \p{N}+(?=:)
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [15]
	 * </pre>
	 *
	 * @param regex Kontrol edilecek düzenli ifade
	 * @return Yeni bir {@link Look} nesnesi
	 */
	static @NotNull Look ahead(@NotNull String regex) {
		
		return new Watcher(regex, true, false);
	}
	
	/**
	 * Eşleşmenin ilerine bakar.<br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like().digits().with(Look.ahead(Nina.like().puncs()));
	 *
	 * pl("Regex  : %s", regex);//Regex  : \p{N}+(?=\p{P}+)
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [15]
	 * </pre>
	 *
	 * @param regex Kontrol edilecek düzenli ifade
	 * @param <T>   {@link Text}
	 * @return Yeni bir {@link Look} nesnesi
	 */
	static <T extends Text> @NotNull Look ahead(@NotNull T regex) {
		
		return ahead(regex.getText());
	}
	
	/**
	 * Kontrol ifadesini pozitif (olumlu) yapar.
	 * Oluşturulan kontrol ifadeleri varsayılan olarak pozitiftir.
	 * {@link #negative()} metodu uygulanmadığı sürece olumlu bir kontrol yapılır.<br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like().digits().with(Look.ahead(Nina.like().puncs()));
	 * //Düzenli ifade, string içindeki sayıları istiyor.
	 * //Ancak sayının hemen sonrasında <em>bir veya daha fazla</em> noktalama işareti olsun istiyor.
	 *
	 * pl("Regex  : %s", regex);//Regex  : \p{N}+(?=\p{P}+)
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [15]
	 * </pre>
	 *
	 * @return Yeni bir {@link Look} nesnesi
	 */
	@NotNull Look positive();
	
	/**
	 * Kontrol ifadesini negatif (olumsuz) yapar.
	 * Oluşturulan kontrol ifadeleri varsayılan olarak pozitiftir.
	 * <br><br>
	 *
	 * <pre>
	 * var str   = "12 Nisan 1981 Çarşamba öğleden sonra 15:45 suları";
	 * var regex = Nina.like().digits().with(Look.ahead(":").negative());
	 * //Düzenli ifade, string içindeki sayıları istiyor.
	 * //Ancak sayının hemen sonrasında {@code :} iki nokta üstüste işareti olsun istemiyor.
	 *
	 * pl("Regex  : %s", regex);//Regex  : \p{N}+(?!:)
	 * pl("Result : %s", Nina.Dev.getParts(str, regex.findAll(str)));//Result : [12, 1981, 1, 45]
	 * </pre>
	 *
	 * @return Yeni bir {@link Look} nesnesi
	 */
	@NotNull Look negative();
}
