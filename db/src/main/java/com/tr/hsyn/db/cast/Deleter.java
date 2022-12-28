package com.tr.hsyn.db.cast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


/**
 * Silici.
 *
 * @param <T> Silinecek nesne türü
 */
public interface Deleter<T> {
	
	/**
	 * Seçilen elemanı sil.
	 *
	 * @param selection Seçim string'i
	 * @return Silme işlemi başarılıysa {@code true}
	 */
	int delete(@NonNull String selection);
	
	/**
	 * Seçilen elemanı sil.
	 *
	 * @param selection     Seçim string'i
	 * @param selectionArgs Seçim için gerekli argüman listesi
	 * @return Silme işlemi başarılıysa {@code true}
	 */
	int delete(@NonNull String selection, @Nullable String[] selectionArgs);
	
	/**
	 * Kaydı primarykey değerine göre sil.
	 *
	 * @param primaryValue primaryValue
	 * @return İşlem başarılı ise {@code true}
	 */
	boolean deleteByPrimaryKey(String primaryValue);
	
	boolean delete(T item);
	
	int delete(List<? extends T> items);
}
