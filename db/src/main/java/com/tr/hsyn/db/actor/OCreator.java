package com.tr.hsyn.db.actor;


import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * Nesne oluşturucu.<br>
 * Veri tabanındaki satırları okuyup nesnelere çevirir ve bu nesneleri liste olarak sunar.
 * Yani sağlanan cursor ({@link #getCursor()}) ile tüm satırları okur ve
 * sağlanan nesne fonksiyonu ({@link #getObjectFunction()}) ile nesneleri oluşturup listeye koyar.
 * <br>
 * Veri tabanından bilgi almak için, sorgulama işlemleri ile bilgileri çekme işlemlerini birbirinden ayırır.
 * Bu arayüz sadece bilgileri çeker ve bir listesini sunar.
 *
 * @param <R>
 */
public interface OCreator<R> {
	
	/**
	 * Veri tabanından okunan satırları nesneye dönüştürecek olan nesneyi döndür.
	 *
	 * @return Dönüştürücü fonksiyon
	 */
	@NonNull
	Function<Cursor, ? extends R> getObjectFunction();
	
	/**
	 * Bilgilerin alınacağı cursor.
	 *
	 * @return Cursor
	 */
	@NonNull
	Cursor getCursor();
	
	/**
	 * Bilgileri dönüştürme işlemini başlatır.
	 *
	 * @return Veri tabanından alınan bilgilerin listesi
	 */
	@NonNull
	default List<R> execute() {
		
		final List<R> list = new ArrayList<>();
		
		var function = getObjectFunction();
		var cursor   = getCursor();
		
		while (cursor.moveToNext()) list.add(function.apply(cursor));
		
		cursor.close();
		return list;
	}
}
