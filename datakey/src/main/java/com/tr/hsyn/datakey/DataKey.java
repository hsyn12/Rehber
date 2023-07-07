package com.tr.hsyn.datakey;


import com.tr.hsyn.integer.Int;

import org.jetbrains.annotations.NotNull;


/**
 * Defines the key of data.<br>
 */
public interface DataKey extends AccessState, Int {
	
	/**
	 * Gets the name of the data/key.
	 *
	 * @return name of the data/key
	 */
	String getName();
	
	/**
	 * Creates a new data key with has read and write access right.
	 *
	 * @param key  key value
	 * @param name name of the data/key
	 * @return new data key with read and write access right
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name) {
		
		return of(key, name, true, true);
	}
	
	/**
	 * Creates a new data key have write access right.
	 *
	 * @param key      key value
	 * @param name     name of the data/key
	 * @param readable read access right
	 * @return new data key with write access right
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable) {
		
		return of(key, name, readable, true);
	}
	
	/**
	 * Creates a new data key.
	 *
	 * @param key      key value
	 * @param name     name of the data/key
	 * @param readable read access right
	 * @param writable write access right
	 * @return new data key
	 */
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable, boolean writable) {
		
		return new IntKey(key, name, readable, writable);
	}
	
}
