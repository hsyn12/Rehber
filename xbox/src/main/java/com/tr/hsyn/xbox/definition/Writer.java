package com.tr.hsyn.xbox.definition;


import com.tr.hsyn.key.Key;

import org.jetbrains.annotations.NotNull;


/**
 * <h2>Writer</h2>
 * <p>
 * Anahtar kayıt görevlisi.<br>
 * Kaydettiği bilgi;
 * bir anahtarın ({@link Key}) eklenmesi ({@link #add(Key)}),
 * çıkarılması ({@link #remove(Key)})
 * ve anahtara erişilmesidir ({@link #interact(Key)}).<br>
 *
 * @author hsyn 1 Ocak 2023 Pazartesi 17:33
 * @see Key
 */
public interface Writer {
	
	/**
	 * Anahtarın çıkarıldığı bilgisini kaydeder.
	 *
	 * @param key Çıkarılan anahtar
	 */
	void remove(@NotNull Key key);
	
	/**
	 * Anahtara erişildiği bilgisini kaydeder.
	 *
	 * @param key Erişilen anahtar
	 */
	void interact(@NotNull Key key);
	
	/**
	 * Anahtarın eklenlediği bilgisini kaydeder.
	 *
	 * @param key Eklenen anahtar
	 */
	void add(@NotNull Key key);
	
	/**
	 * Anahtar kayıt işlerini tamamen kapatır.
	 */
	void close();
}
