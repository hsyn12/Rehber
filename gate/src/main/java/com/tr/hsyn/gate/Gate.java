package com.tr.hsyn.gate;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Kapı.
 * Giriş-çıkış noktası.
 */
public interface Gate {
	
	/**
	 * @return Kapı açıksa {@code true}, kapalıysa {@code false}
	 */
	boolean isGateOpen();

	/**
	 * Kapıyı aç.
	 */
	void openTheGate();

	/**
	 * Kapıyı kapat.
	 */
	void closeTheGate();
	
	@NotNull
	static Gate createGate(){
		
		return new Gate() {

			private final AtomicBoolean _gate = new AtomicBoolean(true);
			
			@Override
			public boolean isGateOpen() {return _gate.get();}

			@Override
			public void openTheGate() {_gate.set(true);}

			@Override
			public void closeTheGate() {_gate.set(false);}
		};
	}
	
}
