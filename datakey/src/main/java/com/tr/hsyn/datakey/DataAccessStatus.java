package com.tr.hsyn.datakey;


public interface DataAccessStatus {
	
	/**
	 * @return Sadece okuma amaçlı ise {@code true}, değilse {@code false}
	 */
	boolean isReadable();
	
	boolean isWritable();
}
