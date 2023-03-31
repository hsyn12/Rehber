package com.tr.hsyn.xbox.definition;


import com.tr.hsyn.key.Key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Resepsiyon.<br>
 * Misafirlerin odalarına giriş çıkışlarını gerçekleştirir.<br>
 * Resepsiyon bir yazıcıya sahiptir ve tüm işlemleri kayıt altına alabilir.
 */
public abstract class Reception {
	
	/**
	 * Yazıcı
	 */
	protected final Writer writer;
	
	/**
	 * Yeni bir resepsiyon oluşturur.
	 *
	 * @param writer Kayıt Görevlisi
	 */
	public Reception(@NotNull Writer writer) {
		
		this.writer = writer;
	}
	
	/**
	 * Odaya erişim sağlar.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Odada olduğu sanılan nesne türü
	 * @return Oda sakini, yoksa {@code null}
	 */
	@Nullable
	public abstract <T> T place(@NotNull Key key);
	
	/**
	 * Misafiri odaya yerleştirir.
	 *
	 * @param key    Oda anahtarı
	 * @param object Misafir nesne
	 * @param <T>    Nesne türü
	 * @return Odadaki önceki misafir, yoksa {@code null}
	 */
	@Nullable
	public abstract <T> T place(@NotNull Key key, @NotNull T object);
	
	public abstract boolean exist(@NotNull Key key);
	
	/**
	 * Odayı boşaltır.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Odadaki misafir nesnenin türü
	 * @return Odadaki misafir nesne
	 */
	public abstract <T> T exit(@NotNull Key key);
	
	/**
	 * Resepsiyonu kapatır.
	 * Tüm odalar boşaltılır.
	 */
	public abstract void close();
}
