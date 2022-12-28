package com.tr.hsyn.content.handler;


import android.content.ContentResolver;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;


/**
 * Veri tabanından hangi bilgilerin alınacağını,
 * alınan bilgilerin nasıl nesneleştirileceğini ve nasıl sıralanacağını tanımlar.
 *
 * @param <T> Nesne türü
 */
public interface ContentHandler<T> {
	
	/**
	 * Nesneyi oluştur.
	 *
	 * @param cursor Cursor
	 * @return T türünde nesne
	 */
	@NonNull
	T handle(@NonNull Cursor cursor);
	
	/**
	 * Nesne oluşturma aşaması başlamadan önceki hazırlık durumu.
	 *
	 * @param resolver ContentResolver
	 * @param cursor   Cursor
	 */
	default void onCreateCursor(@NonNull ContentResolver resolver, @NonNull Cursor cursor) {}
	
	/**
	 * Nesnenin test aşaması.
	 *
	 * @param content  Test edilecek olan nesne
	 * @param contents Şimdiye kadar toplanan nesnelerin bir listesi
	 * @return Eğer {@code false} dönerse nesne gözardı edilmiş olur.
	 */
	default boolean predicate(T content, List<T> contents) {
		
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
