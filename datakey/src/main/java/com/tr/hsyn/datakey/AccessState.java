package com.tr.hsyn.datakey;


/**
 * The access state contract.
 */
public interface AccessState {
	
	/**
	 * Indicates whether the key is readable.
	 *
	 * @return {@code true} if the key is readable
	 */
	boolean isReadable();
	
	/**
	 * Indicates whether the key is writable.
	 *
	 * @return {@code true} if the key is writable
	 */
	boolean isWritable();
}
