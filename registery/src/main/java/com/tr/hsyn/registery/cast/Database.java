package com.tr.hsyn.registery.cast;


import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.registery.SimpleDatabase;
import com.tr.hsyn.registery.Values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Veri tabanı arayüzü.
 *
 * @param <T> Veri türü
 */
public interface Database<T extends Identity> {
	
	@NotNull
	DB getDBInterface();
	
	@NotNull
	SimpleDatabase getSimpleDatabase();
	
	/**
	 * Verilen nesne için bilgileri oluştur.<br>
	 *
	 * @param item Nesne
	 * @return Nesne bilgileri
	 */
	@NotNull
	Values contentValuesOf(@NotNull T item);
	
	void close();
	
	long getSizeInBytes();
	
	long getRawCount();
	
	boolean update(@NotNull T item);
	
	/**
	 * Verilen id değerine sahip satırı bulur.
	 *
	 * @param primaryValue id değeri
	 * @return Eleman
	 */
	@Nullable
	T find(long primaryValue);
	
	/**
	 * @return Tüm satırlar
	 */
	@NotNull
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
	@NotNull
	List<T> queryAll(String selection, String[] selectionArgs, String sortOrder);
	
	/**
	 * Veri tabanında bir kolona ait bilgiyi alır.
	 *
	 * @param primaryValue Bilginin alınacağı satır
	 * @param columnName   Bilginin alınacağı kolon
	 * @return string
	 */
	@Nullable
	String getString(long primaryValue, @NotNull String columnName);
	
	default int delete(@NotNull List<? extends T> items) {
		
		var ids        = items.stream().map(T::getId).map(String::valueOf).collect(Collectors.toList());
		var primaryKey = getDBInterface().getPrimaryKey();
		return getSimpleDatabase().delete(getDBInterface().getTableName(), createSelection(primaryKey, ids), null);
	}
	
	default boolean delete(@NotNull T item) {
		
		return deleteByPrimaryKey(String.valueOf(item.getId()));
	}
	
	/**
	 * Kaydı primarykey değerine göre sil.
	 *
	 * @param primaryValue primaryValue
	 * @return İşlem başarılı ise {@code true}
	 */
	default boolean deleteByPrimaryKey(String primaryValue) {
		
		String key = getDBInterface().getPrimaryKey();
		
		return delete(createSelection(key), createArg(primaryValue)) > 0;
	}
	
	/**
	 * Verilen bilgileri, {@code primaryKey} ile belirtilen yere yaz.
	 *
	 * @param values     Yazılacak bilgiler
	 * @param primaryKey primaryKey
	 * @return Güncelleme başarılı ise {@code true}
	 */
	default boolean update(@NotNull Values values, @NotNull String primaryKey) {
		
		String primaryColumn = getDBInterface().getPrimaryKey();
		
		return update(values, createSelection(primaryColumn), createArg(primaryKey));
	}
	
	/**
	 * Seçilen elemanı sil.
	 *
	 * @param selection Seçim string'i
	 * @return Silme işlemi başarılıysa {@code true}
	 */
	default int delete(@NotNull String selection) {
		
		return delete(selection, null);
	}
	
	/**
	 * Seçilen elemanı sil.
	 *
	 * @param selection     Seçim string'i
	 * @param selectionArgs Seçim için gerekli argüman listesi
	 * @return Silme işlemi başarılıysa {@code true}
	 */
	default int delete(@NotNull String selection, @Nullable String[] selectionArgs) {
		
		return getSimpleDatabase().delete(getDBInterface().getTableName(), selection, selectionArgs);
	}
	
	default int update(@NotNull List<? extends T> items) {
		
		return (int) items.stream().filter(this::update).count();
	}
	
	default boolean update(@NotNull T item, long primaryValue) {
		
		return update(item, String.valueOf(primaryValue));
	}
	
	/**
	 * Verilen nesneyi, {@code primary key} ile belirtilen yere yaz.<br>
	 *
	 * @param item         Güncel eleman
	 * @param primaryValue Güncellenecek elemanın id değeri
	 * @return Güncelleme başarılı ise {@code true}
	 */
	default boolean update(@NotNull T item, @NotNull String primaryValue) {
		
		var key = getDBInterface().getPrimaryKey();
		
		return update(contentValuesOf(item), key + "=?", new String[]{primaryValue});
	}
	
	/**
	 * Verilen bilgiyi seçilen yere yaz.
	 *
	 * @param values        Yazılacak bilgiler
	 * @param selection     Sqlite selection
	 * @param selectionArgs selectionArgs
	 * @return Güncelleme başarılı ise {@code true}
	 */
	default boolean update(@NotNull Values values, @NotNull String selection, String[] selectionArgs) {
		
		return getSimpleDatabase().update(getDBInterface().getTableName(), values, selection, selectionArgs) > 0;
	}
	
	/**
	 * Verilen nesneyi veri tabanına kaydet.
	 *
	 * @param item Kaydedilecek nesne
	 * @return Kayıt başarılı ise {@code true}
	 */
	default boolean add(T item) {
		
		return add(contentValuesOf(item));
	}
	
	/**
	 * Verilen listedeki nesneleri veri tabanına kaydet.
	 *
	 * @param items Kaydedilecek elemanlar
	 * @return Başarılı bir şekilde kaydedilen eleman sayısı
	 */
	default int add(@NotNull List<? extends T> items) {
		
		return add(items, this::contentValuesOf);
	}
	
	/**
	 * Verilen listedeki nesneleri veri tabanına kaydet.
	 *
	 * @param items          Kaydedilecek elemanlar
	 * @param valuesFunction Elemanları uygun nesneye dönüştüren fonksiyon
	 */
	default int add(@NotNull List<? extends T> items, @NotNull Function<? super T, Values> valuesFunction) {
		
		int count = 0;
		var db    = getSimpleDatabase();
		
		try {
			
			db.beginTransaction();
			
			for (var item : items) {
				
				var i = db.insert(getDBInterface().getTableName(), null, valuesFunction.apply(item));
				
				if (i != -1) count++;
			}
			
			db.setTransactionSuccessful();
		}
		finally {
			
			db.endTransaction();
		}
		
		return count;
	}
	
	/**
	 * Verilen bilgileri veri tabanına kaydet.
	 *
	 * @param values Kaydedilecek bilgiler
	 * @return Kayıt başarılı ise {@code true}
	 */
	default boolean add(Values values) {
		
		return getSimpleDatabase().insert(getDBInterface().getTableName(), null, values) != -1;
	}
	
	@NotNull
	static String[] createArg(@NotNull String arg) {
		
		return new String[]{arg};
	}
	
	@NotNull
	static String[] createArg(long arg) {
		
		return new String[]{String.valueOf(arg)};
	}
	
	/**
	 * {@code x in (a,b,c,d)} şeklinde bir seçim sorgusu oluşturur.
	 *
	 * @param key   Veri tabanındaki kolon ismi
	 * @param value kolondaki değerler
	 * @return Seçim string'i
	 */
	@NotNull
	static String createSelection(@NotNull String key, @NotNull List<String> value) {
		
		return String.format("%s in (%s)", key, String.join(",", value));
	}
	
	@NotNull
	static String createSelection(@NotNull String key) {
		
		return String.format("%s=?", key);
	}
	
}
