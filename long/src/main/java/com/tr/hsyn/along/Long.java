package com.tr.hsyn.along;


import org.jetbrains.annotations.NotNull;


public interface Long {
	
	/**
	 * @return Nesnenin taşıdığı long değer
	 */
	long getLong();
	
	/**
	 * Yeni bir {@code Long} nesnesi oluşturur.
	 *
	 * @param value Nesnenin taşıyacağı değer
	 * @return {@code Long} nesnesi
	 */
	@NotNull
	static Long of(long value) {
		
		return new DInt(value);
	}
	
	
}
