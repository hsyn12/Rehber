package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleDigiGate;

import org.jetbrains.annotations.NotNull;


/**
 * Digital Gate.<br>
 * This interface extends {@link AutoGate},
 * and adds a cyclic structure and provides to execute a runnable at the exit.
 */
public interface DigiGate extends AutoGate, Looply {
	
	/**
	 * Enters through the gate and close it if it is open.
	 *
	 * @param onExit work to do on exit
	 * @return {@code true} if the gate is open, and the gate is closed, entering is successful.
	 *      {@code false} if it is closed already and entering is not successful.
	 */
	boolean enter(@NotNull Runnable onExit);
	
	/**
	 * @param onExit work to do on exit
	 */
	void setOnExit(Runnable onExit);
	
	/**
	 * Creates a new digital gate with zero intervals.
	 * So, after entering, will exit immediately.
	 * Or call {@link #enter(long)} to enter with a certain interval.
	 *
	 * @return a new digital gate
	 */
	@NotNull
	static DigiGate newGate() {
		
		return new SimpleDigiGate();
	}
	
	/**
	 * Creates a new digital gate with the given work to do on the exit.
	 * Interval is zero.
	 * So, after entering, will exit immediately.
	 * Or call {@link #enter(long)} to enter with a certain interval.
	 *
	 * @param onExit work to do on exit
	 * @return a new digital gate
	 */
	@NotNull
	static DigiGate newGate(Runnable onExit) {
		
		return new SimpleDigiGate(onExit);
	}
	
	/**
	 * Creates a new digital gate with the given interval.
	 *
	 * @param passInterval the time it takes to automatic exit after entering
	 * @return a new digital gate
	 */
	@NotNull
	static DigiGate newGate(long passInterval) {
		
		return new SimpleDigiGate(passInterval);
	}
	
	/**
	 * Creates a new digital gate.
	 *
	 * @param passInterval the time it takes to automatic exit after entering
	 * @param onExit       work to do on exit
	 * @return a new digital gate
	 */
	@NotNull
	static DigiGate newGate(long passInterval, Runnable onExit) {
		
		return new SimpleDigiGate(passInterval, onExit);
	}
	
	
}
