package com.tr.hsyn.life;


import org.jetbrains.annotations.NotNull;

/**
 * Hayat. Bir başlangıcı ve bir sonu olan kısıtlı süreyi temsil eder.
 */
public interface Life {
	
	/**
	 * @return Hayatın ismi
	 */
	String getName();
	
	/**
	 * @return Hayatın başlama zamanı
	 */
	long getStartTime();
	
	/**
	 * Hayatı başlat.
	 *
	 * @param time Zaman
	 */
	Life startLife(long time);
	
	/**
	 * Hayatı başlat.
	 */
	Life startLife();
	
	/**
	 * @return Hayatın bitiş zamanı
	 */
	long getEndTime();
	
	/**
	 * Hayatı bitir.
	 *
	 * @param time Zaman
	 */
	Life endLife(long time);
	
	/**
	 * Hayatı bitir.
	 */
	Life endLife();
	
	@NotNull
	static Life newLife(@NotNull String name) {
		
		return new LifeTime(name);
	}
	
	@NotNull
	static Life newLife(@NotNull String name, long start) {
		
		return new LifeTime(name, start);
	}
	
	@NotNull
	static Life newLife(@NotNull String name, long startTime, long endTime) {
		
		return new LifeTime(name, startTime, endTime);
	}
}
