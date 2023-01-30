package com.tr.hsyn.gate;


/**
 * Kapının durumunu dinleyen sınıf
 */
public interface GateObserver {
	
	/**
	 * Kapının durumunun değiştiğini bildirir.
	 *
	 * @param isOpen Kapı açıldıysa {@code true}, kapandıysa {@code false}
	 */
	void onGateStateChange(boolean isOpen);
}
