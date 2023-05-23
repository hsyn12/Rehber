package com.tr.hsyn.along;


import org.jetbrains.annotations.NotNull;


public interface Int64 {
	
	/**
	 * Yeni bir {@code Long} nesnesi oluşturur.
	 *
	 * @param value Nesnenin taşıyacağı değer
	 * @return {@code Long} nesnesi
	 */
	@NotNull
	static Int64 of(long value) {
		
		return new DInt(value);
	}
	
	default boolean isZero() {
		
		return getLong() == 0;
	}
	
	/**
	 * @return Nesnenin taşıdığı long değer
	 */
	long getLong();
	
}
