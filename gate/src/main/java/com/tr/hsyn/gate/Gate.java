package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleGate;

import org.jetbrains.annotations.NotNull;


/**
 * Gate.<br>
 * Entry-output point.
 */
public interface Gate {
	
	/**
	 * @return {@code true} if the gate is open
	 */
	boolean isOpen();
	
	/**
	 * Enters through the gate and close it.
	 *
	 * @return {@code true} if the gate is open, and the gate is closed, entering is successful.
	 *      {@code false} if it is closed already and entering is not successful.
	 */
	boolean enter();
	
	/**
	 * Exits through the gate and open it.
	 * Returns {@code true} from {@link #isOpen()} call after this call.
	 */
	void exit();
	
	/**
	 * Creates a new gate.
	 *
	 * @return a new gate
	 */
	@NotNull
	static Gate newGate() {
		
		return new SimpleGate();
	}
	
}
