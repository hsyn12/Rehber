package com.tr.hsyn.datboxer;


import com.tr.hsyn.datakey.DataKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * Veri saklayıcısı.<br>
 * Verileri bir anahtar ile saklar ve aynı anahtar ile erişim sağlar.
 *
 * @see DataKey
 */
public interface Objext {
	
	/**
	 * Kayıtlı veriyi döndürür.
	 *
	 * @param key Anahtar
	 * @param <T> Kayıtlı veri türü
	 * @return Veri, yoksa {@code null}
	 */
	<T> T getData(@NotNull DataKey key);
	
	/**
	 * Veriyi hem saklamayı hem silmeyi sağlar.
	 *
	 * @param key  Anahtar
	 * @param data Veri
	 * @param <T>  Veri türü
	 * @return Eğer veri {@code null} değilse kaydedilir ve aynı yerde saklanan önceki veri döndürülür.
	 * 		Daha önce veri kaydedilmemişse {@code null} döner.<br>
	 * 		Eğer veri {@code null} ise ve verilen anahtarla bir kayıt tutuluyorsa bu kayıt silinir ve silinen kayıt döndürülür.
	 */
	<T> T setData(@NotNull DataKey key, @Nullable T data);
	
	/**
	 * Bool veriler için {@code null} kontrolü yapar.
	 *
	 * @param key Anahtar
	 * @return Eğer istenen veri yoksa {@code false}, varsa bool veri
	 */
	boolean getBool(@NotNull DataKey key);
	
	/**
	 * Bool veriler için {@code null} kontrolü yapar.
	 *
	 * @param key          Anahtar
	 * @param defaultValue Verilen anahtarla kayıtlı bir veri yoksa döndürülecek değer
	 * @return Eğer istenen veri yoksa varsayılan değer, varsa bool veri
	 */
	boolean getBool(@NotNull DataKey key, boolean defaultValue);
	
	/**
	 * Integer veriler için {@code null} kontrolü yapar.
	 *
	 * @param key          Anahtar
	 * @param defaultValue Verilen anahtarla kayıtlı bir veri yoksa döndürülecek değer
	 * @return Eğer istenen veri yoksa varsayılan değer, varsa int veri
	 */
	int getInt(@NotNull DataKey key, int defaultValue);
	
	/**
	 * Returns the int value of the given key.
	 *
	 * @param key Key
	 * @return Int or {@code 0}
	 */
	int getInt(@NotNull DataKey key);
	
	/**
	 * Returns the long value of the given key.
	 *
	 * @param key Key
	 * @return Long or {@code 0L}
	 */
	long getLong(@NotNull DataKey key);
	
	/**
	 * Long veriler için {@code null} kontrolü yapar.
	 *
	 * @param key          Anahtar
	 * @param defaultValue Verilen anahtarla kayıtlı bir veri yoksa döndürülecek değer
	 * @return Eğer istenen veri yoksa varsayılan değer, varsa long veri
	 */
	long getLong(@NotNull DataKey key, long defaultValue);
	
	/**
	 * @return Tüm okunabilir yada yazılabilir veri anahtarları
	 */
	Set<DataKey> keySet();
	
	/**
	 * Verilen anahtarla kayıtlı bir veri olup olmadığını kontrol eder.
	 *
	 * @param key Veri anahtarı
	 * @return Anahtarda okuma yetkisi varsa ve bu anahtarla bir veri kayıtlıysa {@code true}, aksi {@code false}
	 */
	boolean exist(@NotNull DataKey key);
	
	
}
