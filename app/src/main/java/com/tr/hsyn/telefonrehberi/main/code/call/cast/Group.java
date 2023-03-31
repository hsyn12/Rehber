package com.tr.hsyn.telefonrehberi.main.code.call.cast;


import androidx.annotation.Nullable;
import androidx.annotation.Size;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Bir nesne grubunu temsil eder.
 * <b>anahtar</b> olarak gruplanan elemanlardan bir nesne,
 * <b>değer</b> olarak ise o nesne listesini tutar.<br>
 * Yani {@code T -> List<T>} yapısını sağlar.
 * Sıralama ile ilgili yerlerde kullanılır.
 *
 * @param <T> Temsil edilen grup elemanının türü.
 */
public class Group<T> {
	
	/**
	 * Anahatar
	 */
	private final T       value;
	/**
	 * Değer
	 */
	private final List<T> list;
	private       int     extra;
	private       int     rank;
	
	public Group(@NotNull @Size(min = 1) List<T> list) {
		
		this.list = list;
		value     = list.get(0);
	}
	
	public T getValue() {
		
		return value;
	}
	
	public List<T> getList() {
		
		return list;
	}
	
	public int getExtra() {
		
		return extra;
	}
	
	public void setExtra(int extra) {
		
		this.extra = extra;
	}
	
	public int getRank() {
		
		return rank;
	}
	
	public void setRank(int rank) {
		
		this.rank = rank;
	}
	
	/**
	 * @return grup sayısı
	 */
	public int size() {
		
		return list.size();
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		
		return obj instanceof Group && value.equals(obj);
	}
	
	@Override
	public int hashCode() {
		
		return value.hashCode();
	}
}
