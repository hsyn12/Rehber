package com.tr.hsyn.gate;


/**
 * Observable Gate.
 */
public interface ObserableGate extends Gate {
	
	/**
	 * Sets the gate observer.
	 *
	 * @param gateObserver observer
	 */
	void setGateObserver(GateObserver gateObserver);
}
