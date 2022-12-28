package com.tr.hsyn.content.handler;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;


/**
 * SQLite veri tabanı satır okuyucusu.
 * Hangi satırların nasıl okunacağını ve nasıl nesneye dönüştürüleceğini bildirir.
 *
 * @param <T>
 */
public interface SQLContentHandler<T> {
	
	/**
	 * Nesneyi oluştur.
	 * Bu aşamada {@code cursor} nesnesi bir satıra konumlanmış ve okumaya hazır durumdadır.
	 * Burada sadece satır okuma işlemi yapılır, {@code cursor} nesnesi otomatik olarak ilerletilmektedir.
	 *
	 * @param cursor Cursor
	 * @return T türünde nesne
	 */
	@NonNull
	T handle(@NonNull Cursor cursor);
	
	/**
	 * Nesne oluşturma aşaması başlamadan önceki hazırlık durumu.
	 * Bu aşamada {@code cursor} hazır olmakla birlikte her hangi bir satıra konumlanmış değildir.
	 *
	 * @param cursor Cursor
	 */
	default void onCreateCursor(@NonNull Cursor cursor) {}
	
	/**
	 * Nesnenin test aşaması.
	 *
	 * @param content  Test edilecek olan nesne
	 * @param contents Şimdiye kadar toplanan nesnelerin bir listesi
	 * @return Eğer {@code false} dönerse nesne gözardı edilmiş olur.
	 */
	default boolean predicate(@NonNull T content, @NonNull List<T> contents) {
		
		return true;
	}
	
	/**
	 * Veri tabanından alınmak istenen bilgiler.
	 *
	 * @return Projection
	 */
	@Nullable
	default String[] getProjection() {
		
		return null;
	}
	
	/**
	 * Veri tabanından bilgiler alınırken kullanılacak sıralama kriteri.
	 *
	 * @return Sıralama kriteri
	 */
	@Nullable
	default String getSortOrder() {
		
		return null;
	}
	
	/**
	 * Veri tabanındaki tüm satırların nesneleştirilmesi sonucunda elde edilen listeyi sıralar.
	 *
	 * @return Sıralayıcı nesne
	 */
	@Nullable
	default Comparator<T> getComparator() {return null;}
}
