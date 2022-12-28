package com.tr.hsyn.db.cast;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Function;


/**
 * Bulucu.
 *
 * @param <T> Bulunacak nesne türü
 */
public interface Finder<T> {
	
	/**
	 * Verilen id değerine sahip satırı bulur.
	 *
	 * @param primaryValue id değeri
	 * @return Eleman
	 */
	@Nullable
	T find(long primaryValue);
	
	/**
	 * Verilen id değerine sahip satırı bulur.
	 *
	 * @param primaryValue id değeri
	 * @param func         Bulunan satırı nesneye dönüştürecek fonksiyon
	 * @return Verilen fonksiyonunun döndürdüğü nesne
	 */
	@Nullable
	T find(long primaryValue, @NonNull Function<? super Cursor, ? extends T> func);
	
	/**
	 * Seçilen satırı bulur.
	 *
	 * @param selection Seçim string'i
	 * @param args      Seçim için gerekli argümanlar(yoksa {@code null} )
	 * @param func      Bulunan satırı nesneye dönüştürecek olan fonksiyon
	 * @return Verilen fonksiyonun döndürdüğü nesne
	 */
	@Nullable
	T find(@NonNull String selection, String[] args, @NonNull Function<? super Cursor, ? extends T> func);
}
