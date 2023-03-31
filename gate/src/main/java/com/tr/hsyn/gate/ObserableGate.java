package com.tr.hsyn.gate;


/**
 * Kapının açık-kapalı durumunu izlemeyi sağlar.
 */
public interface ObserableGate extends Gate {
	
	/**
	 * @param gateObserver İzleyici
	 */
	void setGateObserver(GateObserver gateObserver);
}
