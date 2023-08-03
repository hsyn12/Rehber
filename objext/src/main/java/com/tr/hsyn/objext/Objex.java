package com.tr.hsyn.objext;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Bir nesne için üst katman oluşturur.
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
	
	/**
	 * Creates a new {@link Objex}.
	 *
	 * @param <T> type of object
	 * @return new {@link Objex}
	 */
	static @NotNull <T> Objex<T> object() {
		
		return new Obje<>();
	}
	
	/**
	 * Creates a new {@link Objex}.
	 *
	 * @param obj object
	 * @param <T> type of object
	 * @return new {@link Objex}
	 */
	static <T> @NotNull Objex<T> object(@Nullable T obj) {
		
		return new Obje<>(obj);
	}
}