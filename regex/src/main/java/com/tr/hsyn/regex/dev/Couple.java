package com.tr.hsyn.regex.dev;


import org.jetbrains.annotations.NotNull;


/**
 * Birbirine bir bağ ile bağlanmış iki sayının yapısını tanımlar.
 * Varsayılan bağ {@code :} işaretidir.
 */
public class Couple {
	
	/**
	 * Bağlantının sol tarafındaki sayı
	 */
	public final String left;
	/**
	 * Bağlantının sağ tarafındaki sayı
	 */
	public final String right;
	/**
	 * Sayıları birbirine bağlayan bağ
	 */
	public final String link;
	
	/**
	 * Yeni bir nesnesi oluşturur.
	 *
	 * @param left  Çiftin sol taraf değeri
	 * @param right Çiftin sağ taraf değeri
	 */
	public Couple(@NotNull String left, @NotNull String right) {
		
		this.left  = left;
		this.right = right;
		link       = ":";
	}
	
	/**
	 * Yeni bir nesnesi oluşturur.
	 *
	 * @param left  Çiftin sol taraf değeri
	 * @param right Çiftin sağ taraf değeri
	 * @param link  Çiftleri birbirine bağlayan bağ
	 */
	public Couple(@NotNull String left, @NotNull String right, @NotNull String link) {
		
		this.left  = left;
		this.right = right;
		this.link  = link;
	}
	
	public boolean isEmpty() {
		
		return left.isEmpty() || right.isEmpty();
	}
	
	@SuppressWarnings("DefaultLocale")
	@Override
	@NotNull
	public String toString() {
		
		return String.format("%s%s%s", left, link, right);
	}
	
	
}
