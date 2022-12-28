package com.tr.hsyn.news;


import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


/**
 * <h2>Haber Merkezi</h2>
 * <p>
 * Bu sınıfın görevi, tüm uygulama genelinde gerçekleşen olayların
 * haberini yapmaktır.<br>
 */
public final class News extends NewsShot implements NewsLabel {
	
	/**
	 * Yeni haber yayınla.
	 *
	 * @param sheet Haber
	 */
	public static void sheet(@NotNull Sheet sheet) {
		
		xlog.d(sheet);
	}
	
}