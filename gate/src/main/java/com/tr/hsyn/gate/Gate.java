package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleGate;

import org.jetbrains.annotations.NotNull;


/**
 * Kapı.
 * Giriş-çıkış noktası.
 */
public interface Gate {
	
	/**
	 * @return Kapı açıksa {@code true}, kapalıysa {@code false}
	 */
	boolean isOpen();
	
	/**
	 * Kapıdan giriş yapılmasını sağlar.
	 *
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner, kapı kapalıysa giriş yapılamaz ve {@code false} döner.
	 */
	boolean enter();
	
	/**
	 * Çıkış yapar ve kapı açılır
	 */
	void exit();
	
	@NotNull
	static Gate newGate() {
		
		return new SimpleGate();
	}
	
}
