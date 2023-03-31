package com.tr.hsyn.db.actor;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * SQLite veri tabanı üzerinde çalışan en temel metotları tanımlar.
 */
public interface SimpleDBOperator {
	
	/**
	 * Veri tabanına yeni bir bilgi ekler.
	 *
	 * @param db        Veri tabanı
	 * @param tableName Tablo ismi
	 * @param values    Eklenecek bilgiler
	 * @return Ekleme işlemi başarılı ise {@code true}
	 */
	static boolean add(@NonNull SQLiteDatabase db, @NonNull String tableName, @NonNull ContentValues values) {
		
		return db.insert(tableName, null, values) > 0;
	}
	
	/**
	 * Veri tabanında kayıtlı olan bir bilgiyi günceller.
	 *
	 * @param db        Veri tabanı
	 * @param tableName Tablo ismi
	 * @param selection Güncellemenin yapılacağı satırı işaret eden seçim string'i
	 * @param args      Seçim için gerekli argümanlar
	 * @param values    Yazılacak yeni bilgi
	 * @return Güncelleme işlemi başarılı ise {@code true}
	 */
	static boolean update(@NonNull SQLiteDatabase db, @NonNull String tableName, @NonNull String selection, @Nullable String[] args, @NonNull ContentValues values) {
		
		return db.update(tableName, values, selection, args) > 0;
	}
	
	/**
	 * Veri tabanında kayıtlı bir bilgiyi siler.
	 *
	 * @param db            Veri tabanı
	 * @param tableName     Tablo ismi
	 * @param selection     Silinecek satırı işaret eden seçim string'i
	 * @param selectionArgs Seçim için gerekli argümanlar
	 * @return Silinen satır sayısı
	 */
	static int delete(@NonNull SQLiteDatabase db, @NonNull String tableName, @NonNull String selection, @Nullable String[] selectionArgs) {
		
		return db.delete(tableName, selection, selectionArgs);
	}
	
}
