package com.tr.hsyn.db.cast;


import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Function;


/**
 * Ekleyici.<br>
 * Veri tabanına ekleme yapacak metotları bildirir.
 *
 * @param <T> Eklenecek nesne türü
 */
public interface Adder<T> {
	
	
	/**
	 * Verilen nesneyi veri tabanına kaydet.
	 *
	 * @param item Kaydedilecek nesne
	 * @return Kayıt başarılı ise {@code true}
	 */
	boolean add(T item);
	
	/**
	 * Verilen listedeki nesneleri veri tabanına kaydet.
	 *
	 * @param items Kaydedilecek elemanlar
	 * @return Başarılı bir şekilde kaydedilen eleman sayısı
	 */
	int add(@NonNull List<? extends T> items);
	
	/**
	 * Verilen listedeki nesneleri veri tabanına kaydet.
	 *
	 * @param items          Kaydedilecek elemanlar
	 * @param valuesFunction Elemanları uygun nesneye dönüştüren fonksiyon
	 */
	int add(@NonNull List<? extends T> items, @NonNull Function<? super T, ContentValues> valuesFunction);
	
	/**
	 * Verilen bilgileri veri tabanına kaydet.
	 *
	 * @param values Kaydedilecek bilgiler
	 * @return Kayıt başarılı ise {@code true}
	 */
	boolean add(ContentValues values);
	
}
