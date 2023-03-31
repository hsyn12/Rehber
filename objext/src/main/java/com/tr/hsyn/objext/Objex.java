package com.tr.hsyn.objext;


import org.jetbrains.annotations.Nullable;


/**
 * Bir nesne için üst katman oluşturur.
 * Nesne erişimi üst katmandan yapılır.
 *
 * @param <T> Yönetilecek nesne türü
 */
public interface Objex<T> {
	
	/**
	 * @return Kapsanan nesne
	 */
	@Nullable T getValue();
	
	/**
	 * Kapsanan nesneye değer atar.
	 *
	 * @param obj Nesne değeri
	 */
	void setValue(@Nullable T obj);
	
	/**
	 * Nesneye her değer atandığında çağrılacak bir dinleyici kaydeder.
	 *
	 * @param listener Dinleyici
	 */
	void listenChange(@Nullable ChangeListener<T> listener);
	
}