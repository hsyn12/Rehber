package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleDigiGate;

import org.jetbrains.annotations.NotNull;


/**
 * Dijital kapı.<br>
 * Otomatik çıkış yapılmasını ve çıkışta bir iş ({@code Runnable}) çalıştırılmasını sağlar.<br>
 */
public interface DigiGate extends AutoGate, Looply {
	
	/**
	 * Kapıdan giriş talep eder.
	 *
	 * @param onExit Çıkış yapıldıktan sonra çalıştırılacak iş
	 * @return Giriş başarılı ise {@code true}, aksi halde {@code false}
	 */
	boolean enter(@NotNull Runnable onExit);
	
	/**
	 * @param onExit Çıkış işlemi
	 */
	void setOnExit(Runnable onExit);
	
	/**
	 * Otomatik çıkış süresi {@code 0L} olarak kaydedilir.
	 */
	@NotNull
	static DigiGate newGate() {
		
		return new SimpleDigiGate();
	}
	
	/**
	 * Otomatik çıkış süresi {@code 0L} olarak kaydedilir.
	 *
	 * @param onExit Çıkışta çalıştırılması istenen iş
	 */
	@NotNull
	static DigiGate newGate(Runnable onExit) {
		
		return new SimpleDigiGate(onExit);
	}
	
	/**
	 * Sınıf bu kurucu ile oluşturulduğunda {@link #enter()} ve {@link #enter(Runnable)} metotları
	 * buradaki verilen süreyi otomatik çıkış süresi olarak kullanır.
	 *
	 * @param passInterval Giriş yapıldıktan sonra otomatik olarak çıkış yapılması için geçmesi gereken süre
	 */
	@NotNull
	static DigiGate newGate(long passInterval) {
		
		return new SimpleDigiGate(passInterval);
	}
	
	/**
	 * Çıkış süresini ve çıkış işlemini belirler.
	 *
	 * @param passInterval Çıkış süresi
	 * @param onExit       Çıkış işlemi
	 */
	@NotNull
	static DigiGate newGate(long passInterval, Runnable onExit) {
		
		return new SimpleDigiGate(passInterval, onExit);
	}
	
	
}
