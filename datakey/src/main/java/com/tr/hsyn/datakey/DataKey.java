package com.tr.hsyn.datakey;


import com.tr.hsyn.integer.Int;

import org.jetbrains.annotations.NotNull;


/**
 * Veri anahtarı.<br>
 * Nesnelerin bir veri yapısı içinde saklanmasını ve erişilmesini sağlar.
 */
public interface DataKey extends DataAccessStatus, Int {
	
	/**
	 * Okuma ve yazmaya açık yeni bir anahtar oluşturur.
	 *
	 * @param key  Anahtar değeri
	 * @param name Anahtar adı
	 * @return Yeni bir veri anahtarı
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name) {
		
		return new IntKey(key, name, true, true);
	}
	
	/**
	 * Yazmaya açık yeni bir anahtar oluşturur.
	 *
	 * @param key      Anahtar değeri
	 * @param name     Anahtar adı
	 * @param readable Okunabilirlik durumu
	 * @return Yeni bir veri anahtarı
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable) {
		
		return new IntKey(key, name, readable, true);
	}
	
	/**
	 * Yeni bir anahtar oluşturur.
	 *
	 * @param key      Anahtar değeri
	 * @param name     Anahtar adı
	 * @param readable Okunabilirlik durumu
	 * @param writable Yazılabilirlik durumu
	 * @return Yeni bir veri anahtarı
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable, boolean writable) {
		
		return new IntKey(key, name, readable, writable);
	}
	
	/**
	 * @return Veri adı
	 */
	String getName();
	
}
