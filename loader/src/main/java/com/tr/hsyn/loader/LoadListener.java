package com.tr.hsyn.loader;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Yükleme işlemi için dinleyici.
 *
 * @param <T> Yüklenen nesne türü
 */
@FunctionalInterface
public interface LoadListener<T> {
	
	/**
	 * Yükleme işleminin her adımında durumu bildirmek için çağrılır.
	 *
	 * @param progress    geçerli adım
	 * @param total       toplam adım
	 * @param currentItem bu yükleme adımında alınan nesne
	 */
	void onProgress(int progress, int total, @NotNull T currentItem);
	
	/**
	 * Yükleme işleminin tamamlandığını bildirir.
	 *
	 * @param items yüklenen tüm nesnelerinin listesi
	 */
	default void onCompleted(@NotNull List<T> items) {}
	
	/**
	 * Yükleme işleminde bir hata olduğunu bildirir.
	 * Bu hata yüklemenin durduğu anlamına gelir.
	 *
	 * @param throwable hata nesnesi
	 */
	default void onError(@NotNull Throwable throwable) {}
}
