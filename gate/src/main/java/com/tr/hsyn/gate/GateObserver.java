package com.tr.hsyn.gate;


/**
 * Gate Observer.
 */
public interface GateObserver {
	
	/**
	 * Called when gate state change.
	 *
	 * @param isOpen {@code true} if the gate is open
	 */
	void onGateStateChange(boolean isOpen);
}
