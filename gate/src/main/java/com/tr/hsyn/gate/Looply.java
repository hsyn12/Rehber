package com.tr.hsyn.gate;


/**
 * It informs that a gate has a cyclic structure.
 */
public interface Looply extends Gate {
	
	/**
	 * @return Looply
	 */
	Looply loop();
}
