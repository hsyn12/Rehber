package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleAutoGate;

import org.jetbrains.annotations.NotNull;


/**
 * Auto Gate.<br>
 * This interface extends {@link Gate}.<br>
 * And allows the gate to open automatically after a certain time after enter.
 * So not need to call {@link #exit()} to open the gate.
 *
 * @see Gate
 */
public interface AutoGate extends Gate {
	
	/**
	 * @return duration for getting to open the gate after entering
	 */
	long getInterval();
	
	/**
	 * Enters through the gate and close it if it is open.
	 *
	 * @param outAfterMillis duration for keeping closed the gate.
	 * @return {@code true} if the gate is open, and the gate is closed, entering is successful.
	 *      {@code false} if it is closed already and entering is not successful.
	 */
	boolean enter(long outAfterMillis);
	
	/**
	 * Creates a new auto gate with zero intervals.
	 * So, after entering, will exit immediately.
	 * Or call {@link #enter(long)} to enter with a certain interval.
	 */
	@NotNull
	static AutoGate newGate() {
		
		return new SimpleAutoGate();
	}
	
	/**
	 * Creates a new auto gate with the given interval.
	 *
	 * @param passInterval the time it takes to automatic exit after entering
	 * @return a new auto gate
	 */
	@NotNull
	static AutoGate newGate(long passInterval) {
		
		return new SimpleAutoGate(passInterval);
	}
	
}
