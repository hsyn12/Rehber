package com.tr.hsyn.integer;


import org.jetbrains.annotations.NotNull;


/**
 * {@link Integer}
 */
public interface Int {
	
	/**
	 * Yeni bir {@code Int} nesnesi oluşturur.
	 *
	 * @param val {@code Int} nesnesinin tutacağı {@code int} değer
	 * @return Yeni bir {@link Int} nesnesi
	 */
	@NotNull
	static Int of(int val) {
		
		return new Integer(val);
	}
	
	/**
	 * @return int bir değer
	 */
	int getInt();
	
	
}
