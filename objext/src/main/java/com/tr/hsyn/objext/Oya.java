package com.tr.hsyn.objext;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Nesneci başı
 */
public interface Oya {
	
	/**
	 * Yeni bir nesne oluşturur.
	 *
	 * @param <T> Yönetilecek nesne türü
	 * @return Yeni bir {@link Objex}
	 */
	@NotNull
	static <T> Objex<T> object() {
		
		return new Obje<>();
	}
	
	/**
	 * Yeni bir nesne oluşturur.
	 *
	 * @param obj Yönetilecek nesne
	 * @param <T> Yönetilecek nesne türü
	 * @return Yeni bir {@link Objex}
	 */
	@NotNull
	static <T> Objex<T> object(@Nullable T obj) {
		
		return new Obje<>(obj);
	}
	
	/**
	 * Yeni bir nesne oluşturur.
	 *
	 * @param <T> Yönetilecek nesne türü
	 * @return Yeni bir {@link Objex}
	 */
	@NotNull
	static <T> Objex<T> safeObject() {
		
		return new SafeObje<>();
	}
	
	/**
	 * Yeni bir nesne oluşturur.
	 *
	 * @param obj Yönetilecek nesne
	 * @param <T> Yönetilecek nesne türü
	 * @return Yeni bir {@link Objex}
	 */
	@NotNull
	static <T> Objex<T> safeObject(@Nullable T obj) {
		
		return new SafeObje<>(obj);
	}
}
