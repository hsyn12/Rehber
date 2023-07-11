package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleGate;

import org.jetbrains.annotations.NotNull;


/**
 * Gate.<br>
 * Entry-output point.<br>
 * This interface defines a logical gate that can be opened and closed.<br>
 * So this logic has two states: open and closed.<br>
 * Corresponding to these states, the logic has two actions: enter and exit.<br>
 * This interface defines only three methods:
 * {@link #isOpen()}, {@link #enter()} and {@link #exit()}.
 * To be able to enter the gate, the gate must be opened.<br>
 * {@link #isOpen()} checks if the gate is open.
 * But not needed to check if the gate is open,
 * the call {@link #enter()} is enough and returns {@code true} if the gate is open.
 * If {@link #enter()} returns {@code true} means entering is successful,
 * and the gate gets closed.<br>
 * The gate keep closed until {@link #exit()} is called.<br><br>
 *
 * <pre>
 * var gate = Gate.newGate();
 *
 * if (gate.enter())
 *    System.out.println("Entering is successful.");
 *
 * if (gate.enter())
 *     System.out.println("Trying to enter");
 * else {
 *
 *     if (!gate.isOpen()) {
 *
 *        System.out.println("Gate is closed.");
 *        gate.exit();
 *
 *        if (gate.enter())
 *            System.out.println("Entering is successful again");
 *    }
 * }
 * //Entering is successful.
 * //Gate is closed.
 * //Entering is successful again
 * </pre>
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
	
	
	public static void main(String[] args) {
		
		var gate = Gate.newGate();
		
		if (gate.enter())
			System.out.println("Entering is successful.");
		
		if (gate.enter())
			System.out.println("Trying to enter");
		else {
			
			if (!gate.isOpen()) {
				
				System.out.println("Gate is closed.");
				gate.exit();
				
				if (gate.enter())
					System.out.println("Entering is successful again");
			}
		}
	}
	
}
