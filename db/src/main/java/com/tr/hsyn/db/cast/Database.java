package com.tr.hsyn.db.cast;


/**
 * Veri tabanı arayüzü.
 *
 * @param <T> Veri türü
 */
public interface Database<T> extends Adder<T>, Updater<T>, Deleter<T>, Finder<T>, Querist<T> {
	
	
}
