package com.tr.hsyn.db;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.db.actor.DBOperator;
import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.warnerlabel.Remember;

import java.util.List;
import java.util.function.Function;


/**
 * <h1>DBBase</h1>
 * <p>
 * Daha az zahmetle iş yapan bir veri tabanı.
 * Genişlet ve kullan.
 *
 * @param <T> Veri tabanında tutulacak model türü
 * @author hsyn 02 Nisan 2021 Cuma 12:02:25
 */
@Remember(note = "Bu sınıf hala tam olarak olgunlaşmış değil")
public abstract class DBBase<T extends Identity> extends DBOperator<T> {
	
	/**
	 * Veri tabanı oluştur.<br>
	 * Bir veri tabanı için en önemli bilgi kolonlardır. (Elzemdir)<br>
	 * Bir kolon, altına yazılacak bilgiler için bir isim niteliğindedir.
	 * Ancak sadece isim yeterli değil, kaydedilecek bilginin türü de gerekir. (int, string vs.)<br>
	 * Kolonlar ile ilgili tüm bilgiler {@link DB} arayüzü ile alınır.
	 * Bu arayüz aynı zamanda tablo oluşturacak sql komutunu da hazırlar.
	 * Kullanıcının yapması gerekenler {@link DB} arayüzünde bildirilmektedir.
	 *
	 * @param context     context
	 * @param dbInterface veri tabanı için gerekli bilgiler
	 */
	public DBBase(@NonNull Context context, @NonNull DB dbInterface) {
		
		super(context, dbInterface);
	}
	
	/**
	 * Satırları nesneye dönüştür.<br>
	 * {@link DBBase} sınıfı soyut ve generic olduğu için kaydedilen nesne hakkında bir bilgisi yoktur.<br>
	 * Bu yüzden alt sınıflar kaydettikleri satırları yeniden nesneye dönüştürmek zorundadır.<br>
	 * {@code Cursor}, bir satıra konumlanmış ve bilgi alınmaya hazır durumda gelir.
	 * Bu nesnenin konumunu değiştirme, konum otomatik olarak ayarlanmakta.
	 *
	 * @param cursor cursor
	 * @return Nesne
	 */
	@NonNull
	protected abstract T createObject(@NonNull final Cursor cursor);
	
	@SuppressLint({"DefaultLocale", "Range"})
	
	@Nullable
	public String getString(long primaryValue, @NonNull String columnName) {
		
		var primaryKey = databaseInterface.getPrimaryKey();
		
		var cursor = getReadableDatabase().query(
				databaseInterface.getTableName(),
				null,
				String.format("%s=?", primaryKey),
				Database.createArg(primaryValue),
				null,
				null,
				null
		);
		
		
		if (cursor != null) {
			
			String value = null;
			
			if (cursor.moveToFirst()) {
				
				value = cursor.getString(cursor.getColumnIndex(columnName));
			}
			
			cursor.close();
			return value;
		}
		
		return null;
	}
	
	@Override
	@Nullable
	public T find(long primaryValue) {
		
		return find(primaryValue, this::createObject);
	}
	
	@Nullable
	public T find(long primaryValue, @NonNull Function<? super Cursor, ? extends T> func) {
		
		String key = databaseInterface.getPrimaryKey();
		
		return find(Database.createSelection(key), Database.createArg(primaryValue), func);
	}
	
	@Nullable
	public T find(@NonNull String selection, String[] args, @NonNull Function<? super Cursor, ? extends T> func) {
		
		Cursor cursor = getReadableDatabase().query(
				databaseInterface.getTableName(),
				null,
				selection,
				args,
				null, null, null
		);
		
		if (cursor == null) return null;
		
		T item = null;
		
		if (cursor.moveToFirst()) {
			
			item = func.apply(cursor);
		}
		
		cursor.close();
		return item;
	}
	
	@Override
	@NonNull
	public List<T> queryAll() {
		
		return query(this::createObject, null, null, null);
	}
	
	@Override
	public List<T> queryAll(String selection) {
		
		return query(this::createObject, selection, null, null);
	}
	
	@Override
	public List<T> queryAll(String selection, String sortOrder) {
		
		return query(this::createObject, selection, null, sortOrder);
	}
	
	@NonNull
	@Override
	public List<T> queryAll(String selection, String[] selectionArgs, String sortOrder) {
		
		return query(this::createObject, selection, selectionArgs, sortOrder);
	}
	
}
