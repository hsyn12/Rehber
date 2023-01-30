package com.tr.hsyn.gate;


import com.tr.hsyn.gate.dev.SimpleAutoGate;

import org.jetbrains.annotations.NotNull;


/**
 * Otomatik Kapı.<br>
 * Giriş yapıldıktan sonra kapının belirli bir süre sonunda otomatik açılmasını sağlar.<br>
 * {@link #enter()} metodu {@link #getInterval()} metodundan aldığı süreyi kullanır.<br>
 * {@link #enter(long)} metodu ise farklı süreler belirtilerek çıkış yapılmasını sağlar.<br>
 *
 * @see Gate
 */
public interface AutoGate extends Gate {
	
	/**
	 * @return Otomatik çıkış süresi (milisaniye)
	 */
	long getInterval();
	
	/**
	 * Eğer kapı açıksa {@code true} döner ve verilen süre boyunca kapıyı kapalı tutar.
	 *
	 * @param outAfterMillis Kapının kapalı kalacağı süre.
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner.Yani içeri girilirse {@code true} döner, girilemezse {@code false} döner ve hiç bir işlem yapılmaz.
	 */
	boolean enter(long outAfterMillis);
	
	/**
	 * Kapının otomatik açılma süresi sıfır olarak ayarlanmış yeni bir kapı oluşturur.
	 * Yani eğer {@link #enter()} metodu ile giriş yapılırsa, hemen ardından çıkış yapılır.
	 * {@link #enter(long)} metodu kullanılarak giriş yapılırsa yada
	 * {@link #getInterval()} ile uygun bir süre döndürülürse istenen zamanda otomatik çıkış yapılır.
	 */
	@NotNull
	static AutoGate newGate() {
		
		return new SimpleAutoGate();
	}
	
	/**
	 * Verilen süre, varsayılan çıkış süresi olarak ayarlanmış yeni bir kapı döndürür.
	 * {@link #enter()} metodu bu varsayılan süreyi kullanarak kapıyı otomatik açar.
	 * Yine de {@link #enter(long)} metodu ile farklı bir süre kullanılabilir.
	 *
	 * @param passInterval Kapının tekrar açılması için geçmesi gereken süre
	 */
	@NotNull
	static AutoGate newGate(long passInterval) {
		
		return new SimpleAutoGate(passInterval);
	}
	
}
