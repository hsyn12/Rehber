package com.tr.hsyn.datakey;


/**
 * Bilginin okunabilir ve yazılabilirlik durumu bildirir.
 */
public interface DataAccessStatus {
	
	/**
	 * @return Bilgi, okumaya açık ise {@code true}, değilse {@code false}
	 */
	boolean isReadable();
	
	/**
	 * @return Bilgi, yazmaya açık ise {@code true}, değilse {@code false}
	 */
	boolean isWritable();
}
