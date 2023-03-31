package com.tr.hsyn.along;


import org.jetbrains.annotations.NotNull;


public interface Long {
	
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
	
	/**
	 * @return Nesnenin taşıdığı long değer
	 */
	long getLong();
	
	
}
