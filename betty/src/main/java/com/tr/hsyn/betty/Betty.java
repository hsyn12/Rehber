package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Betty. Kötü durum senaryosu işleyicisi.
 * Hata üretmesi muhtemel kodların pratik biçimde ele alınmasını sağlar.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Betty<R> extends Bet<R> {
	
	protected Betty() {
		
	}
	
	protected Betty(Boolean checkout) {
		
		super(checkout);
	}
	
	protected Betty(@Nullable final Exception e) {
		
		super(e);
	}
	
	protected Betty(@Nullable final Exception e, Boolean checkout) {
		
		super(e, checkout);
	}
	
	protected Betty(@Nullable final Exception e, @Nullable final R v) {
		
		super(e, v);
	}
	
	protected Betty(@Nullable final Exception e, @Nullable final R v, Boolean checkout) {
		
		super(e, v, checkout);
	}
	
	protected Betty(@Nullable final R v) {
		
		super(v);
	}
	
	protected Betty(@Nullable final R v, Boolean checkout) {
		
		super(v, checkout);
	}
	
	/**
	 * Sonuç döndürmeyecek bir akış başlatmak için kısa yol.
	 *
	 * @param runnable Yaptırılmak istenen işlem
	 * @param <R>      Dönecek sonucun türü. Bu tür, bu işlem için {@link Void} olmalı.
	 * @return Bet
	 */
	@NotNull
	public static <R> Bet<R> bet(@NotNull final Runnable runnable) {
		
		return new Betty<R>().care(runnable);
	}
	
	public static <R> Bet<R> bet(boolean out) {
		
		return new Betty<R>().care(out);
	}
	
	/**
	 * Sonuç döndürecek bir akış başlatmak için kısa yol.
	 *
	 * @param callable Sonuç döndürecek olan işlem
	 * @param <R>      Dönecek sonucun türü
	 * @return Bet
	 */
	@NotNull
	public static <R> Bet<R> bet(@NotNull final Callable<R> callable) {
		
		return new Betty<R>().care(callable);
	}
	
	
	public static void main(String... args) {
		
		List<Long> list = new ArrayList<>();
		Runnable runnable = () -> {
			
			Long l = Collections.max(list);
			
			System.out.printf("Max element : %d\n", l);
		};
		Runnable runnable2 = () -> {System.out.println("runnable2 run");};
		Runnable runnable3 = () -> {System.out.println("runnable3 run");};
		Runnable runnable4 = () -> {System.out.println("runnable4 run");};
		
		
		Betty.bet(runnable)
				.call(runnable2)
				.onError((e) -> {System.out.println("Şakir göğüstüğün çiğdem error " + e);});
		
		
	}
}