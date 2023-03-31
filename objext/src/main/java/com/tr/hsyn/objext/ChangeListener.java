package com.tr.hsyn.objext;


import org.jetbrains.annotations.Nullable;


/**
 * Nesnenin değer değişimi izleyicisi
 */
@FunctionalInterface
public interface ChangeListener<T> {
	
	/**
	 * Nesnenin değeri değiştiğinde çağrılır.
	 *
	 * @param obj Nesne
	 */
	void onChange(@Nullable T obj);
}
