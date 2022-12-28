package com.tr.hsyn.watch.observable;


/**
 * <h1>Observerx</h1>
 *
 * <p>
 * Dinleyici.
 *
 * @param <T> tür
 * @author hsyn 2019-12-06 14:44:36
 */
public interface Observer<T> {
	
	/**
	 * Değişen yeni değer.
	 *
	 * @param newValue yeni değer
	 */
	void onUpdate(T newValue);
}
