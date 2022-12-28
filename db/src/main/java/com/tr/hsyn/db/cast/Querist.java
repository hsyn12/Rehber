package com.tr.hsyn.db.cast;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Function;


/**
 * Sorgulayıcı.
 *
 * @param <T> Sorgulama işlemlerinin dönüş değeri türü
 */
public interface Querist<T> {
	
	/**
	 * @return Tüm satırlar
	 */
	@NonNull
	List<T> queryAll();
	
	/**
	 * Seçilen satırları döndürür.
	 *
	 * @param selection Seçim
	 * @param sortOrder Sıralama ölçütü
	 * @return Seçilen satırlardaki nesneler
	 */
	List<T> queryAll(String selection, String sortOrder);
	
	
	/**
	 * @param selection Seçim
	 * @return Seçilen elemanların listesi
	 */
	List<T> queryAll(String selection);
	
	/**
	 * Seçilen elemanları döndürür.
	 *
	 * @param selection     Seçim
	 * @param selectionArgs Seçim argümanları
	 * @param sortOrder     Sılama ölçütü
	 * @return Eleman listesi
	 */
	@NonNull
	List<T> queryAll(String selection, String[] selectionArgs, String sortOrder);
	
	/**
	 * Veri tabanında bir kolona ait bilgiyi alır.
	 *
	 * @param primaryValue Bilginin alınacağı satır
	 * @param columnName   Bilginin alınacağı kolon
	 * @return string
	 */
	@Nullable
	String getString(long primaryValue, @NonNull String columnName);
	
	/**
	 * Veri tabanından {@code long} değerleri alır.
	 *
	 * @param primaryValue Bilginin alınacağı satır
	 * @param columnNames  Bilginin alınacağı kolonlar
	 * @return long dizi
	 */
	@Nullable
	long[] getLongs(long primaryValue, @NonNull String[] columnNames);
	
	/**
	 * Seçilen satırları döndürür.
	 *
	 * @param function      Satırları nesneye dönüştürecek olan fonksiyon
	 * @param selection     Seçim
	 * @param selectionArgs Seçim argümanları
	 * @param sortOrder     Sılama ölçütü
	 * @return Eleman listesi
	 */
	@NonNull
	List<T> query(@NonNull Function<Cursor, ? extends T> function, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder);
}
