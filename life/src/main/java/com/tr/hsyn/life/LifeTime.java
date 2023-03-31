package com.tr.hsyn.life;


import org.jetbrains.annotations.NotNull;


/**
 * Yaşam Süresi.<br>
 * Hayatın başlama ve bitiş zamanlarını tutar.
 *
 * @author hsyn 03 Şubat 2022 Perşembe 11:31:20
 */
public class LifeTime implements Life {
	
	private final String name;
	private       long   startTime;
	private       long   endTime;
	
	public LifeTime(@NotNull String name) {this.name = name;}
	
	public LifeTime(@NotNull String name, long start) {
		
		this.name      = name;
		this.startTime = start;
	}
	
	public LifeTime(@NotNull String name, long startTime, long endTime) {
		
		this.name      = name;
		this.startTime = startTime;
		this.endTime   = endTime;
	}
	
	/**
	 * Yaşam süresini döndürür (milisaniye).
	 * Doğru bir yaşam süresi, başlangıcı sıfırdan büyük ve bitişi başlangıç zamanından büyük olur.
	 *
	 * @return Yaşam süresi.
	 * 		Eğer yaşam başlamadıysa yada
	 * 		sona ermediyse yada
	 * 		başlama ve bitiş zamanı mantıksal olarak hatalı ise
	 *      {@code 0L} döndürür.
	 */
	long getLifeTime() {
		
		if (startTime == 0L || endTime == 0L || startTime >= endTime) return 0L;
		
		return endTime - startTime;
	}
	
	@NotNull
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public long getStartTime() {
		
		return startTime;
	}
	
	@NotNull
	@Override
	public Life startLife(long time) {
		
		this.startTime = time;
		return this;
	}
	
	@NotNull
	@Override
	public Life startLife() {
		
		this.startTime = System.currentTimeMillis();
		return this;
	}
	
	@Override
	public long getEndTime() {
		
		return endTime;
	}
	
	@NotNull
	@Override
	public Life endLife(long time) {
		
		this.endTime = time;
		return this;
	}
	
	@NotNull
	@Override
	public Life endLife() {
		
		endTime = System.currentTimeMillis();
		return this;
	}
	
	@Override
	public long getId() {
		
		return startTime;
	}
}
