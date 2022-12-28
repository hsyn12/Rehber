package com.tr.hsyn.db.cast;


import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.util.List;


/**
 * Güncelleyici.<br>
 * Veri tabanında kayıtlı bilgileri güncelleyecek metotları bildirir.
 *
 * @param <T> Güncellenen nesne türü
 */
public interface Updater<T> {
	
	/**
	 * Verilen nesneyi, {@code primary key} ile belirtilen yere yaz.<br>
	 *
	 * @param item         Güncel eleman
	 * @param primaryValue Güncellenecek elemanın id değeri
	 * @return Güncelleme başarılı ise {@code true}
	 */
	boolean update(@NonNull T item, @NonNull String primaryValue);
	
	boolean update(@NonNull T item);
	
	boolean update(@NonNull T item, long primaryValue);
	
	int update(@NonNull List<? extends T> items);
	
	
	/**
	 * Verilen bilgileri, {@code primaryKey} ile belirtilen yere yaz.
	 *
	 * @param values     Yazılacak bilgiler
	 * @param primaryKey primaryKey
	 * @return Güncelleme başarılı ise {@code true}
	 */
	boolean update(@NonNull ContentValues values, @NonNull String primaryKey);
	
	/**
	 * Verilen bilgiyi seçilen yere yaz.
	 *
	 * @param values        Yazılacak bilgiler
	 * @param selection     Sqlite selection
	 * @param selectionArgs selectionArgs
	 * @return Güncelleme başarılı ise {@code true}
	 */
	boolean update(@NonNull ContentValues values, @NonNull String selection, String[] selectionArgs);
	
}
