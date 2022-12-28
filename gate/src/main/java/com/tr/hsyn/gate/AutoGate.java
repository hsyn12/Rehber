package com.tr.hsyn.gate;


import com.tr.hsyn.execution.Runny;


/**
 * Otomatik Kapı. 
 * Giriş yapıldıktan sonra kapı kapanır ve belirli bir süre
 * geçince kapı otomatik açılır.
 * Otomatik açılma süresi {@link #AutoGate(long)} kurucusu ile verilebilir yada
 * {@link #enter(long)} metodu ile giriş yapılarak bu süre belirtilebilir.
 * Belirtilen süre sonunda kapı açılır ve çıkış yapılmış olur.
 *
 * @see Gate
 */
public class AutoGate extends Base {
	
	/**
	 * Kapının açılması için geçmesi gereken süre
	 */
	protected final long interval;

	/**
	 * Bu kurucu, kapının otomatik açılma süresini sıfır olarak kaydeder.
	 * Yani eğer bu kurucu ile nesne başlatılırsa ve
	 * {@link #enter()} metodu ile giriş yapılırsa, hemen ardından çıkış yapılır.
	 * Eğer spesifik bir çıkış süresi isteniyorsa ya {@link #AutoGate(long)} kurucusu kullanılmalı yada
	 * {@link #enter(long)} metodu kullanılarak giriş yapılmalı.
	 */
	public AutoGate() {

		interval = 0L;
	}
	
	/**
	 * Verilen süre, varsayılan çıkış süresi olarak kaydedilir.
	 * {@link #enter()} metodu bu süreyi kullanarak kapıyı otomatik açar.
	 *
	 * @param passInterval Kapının tekrar açılması için geçmesi gereken süre
	 */
	public AutoGate(long passInterval) {
		
		interval = passInterval;
	}

	/**
	 * 
	 * @return Otomatik çıkış süresi (milisaniye)
	 */
	public final long getInterval() {
		
		return interval;
	}
	
	/**
	 * Eğer kapı açıksa {@code true} döner ve belirtilen ({@link #interval}) süre boyunca kapıyı kapalı tutar.
	 * Kapının otomatik olarak açılması için geçmesi gereken süre {@link #AutoGate(long)} kurucu metodu ile verilmiş
	 * olduğu umulan süredir.
	 *
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner.Yani içeri girilirse {@code true} döner, girilemezse {@code false} döner ve hiç bir işlem yapılmaz.
	 */
	@Override
	public boolean enter() {
		
		if (super.enter()) {
			
			Runny.run(this::exit, interval);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Eğer kapı açıksa {@code true} döner ve verilen süre boyunca kapıyı kapalı tutar.
	 *
	 * @param outAfterMillis Kapının kapalı kalacağı süre.
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner.Yani içeri girilirse {@code true} döner, girilemezse {@code false} döner ve hiç bir işlem yapılmaz.
	 */
	public final boolean enter(long outAfterMillis) {
		
		if (super.enter()) {
			
			Runny.run(this::exit, outAfterMillis);
			
			return true;
		}
		
		return false;
	}
}
