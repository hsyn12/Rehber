package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleDigiGate;

import org.jetbrains.annotations.NotNull;


/**
 * Digital Gate.<br>
 * This interface extends {@link AutoGate},
 * and adds a cyclic structure and provides to execute a runnable on the exit.
 */
public interface DigiGate extends AutoGate {
	
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
	 * This method works for a gate that has an automatic exit duration and an exit operation.
	 * Normally, the gate keeps closed until {@link #exit()} is called,
	 * or until exits automatically after a certain duration.
	 * And during this closed state, all calls to the gate get rejected and returns {@code false}.<br>
	 * For example, say we have a gate that 3 seconds interval for the automatic exit.
	 * While the gate is closed,
	 * say the method {@link #enter()} is called when the automatic exit duration is left 1 second.
	 * In this situation, the call gets rejected.
	 * And that's it.
	 * After one second, the gate will open automatically.
	 * But before this scenario, if this method is called,
	 * when the {@link #enter()} is called,
	 * the interval duration is reset to 3 seconds.
	 * This means that the gate will keep closed until 3 seconds more,
	 * and the exit operation also will be delayed by 3 seconds.
	 * And all rejected calls will cause the same thing.<br>
	 * This is the same thing as searching something by writing the character in the search box.
	 * The search box waits for a specific duration to be pressed any character, and then search.
	 * The search box would be inefficient if it starts to search immediately.
	 * This method has the same logic too.
	 *
	 * @return this digital gate with loop structure
	 */
	DigiGate loop();
	
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
