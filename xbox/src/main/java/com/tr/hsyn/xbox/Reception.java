package com.tr.hsyn.xbox;


import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.definition.Writer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Reception {
	
	protected final Writer writer;
	
	public Reception(@NotNull Writer writer) {
		
		this.writer = writer;
	}
	
	/**
	 * Odaya erişim sağlar.
	 *
	 * @param key Oda anahtarı
	 * @param <T> Odada olduğı sanılan nesne türü
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
