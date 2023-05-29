package com.tr.hsyn.telefonrehberi.dev;


import com.tr.hsyn.execution.Runny;

import org.jetbrains.annotations.NotNull;


/**
 * <h1>Delayed</h1>
 * <p>
 * Belirlenen bir süre sonunda çalıştırılacak bir işlem tanımlamayı sağlar.
 */
public class DelayedRunner {
	
	/**
	 * İş
	 */
	protected Runnable action;
	/**
	 * Yapılacak işin işleme alınıp alınmadığını bildirir
	 */
	
	protected boolean  done;
	protected boolean  onBackground;
	
	public DelayedRunner() {}
	
	/**
	 * @param action Çalıştırılacak işlem
	 */
	public DelayedRunner(@NotNull Runnable action) {
		
		this.action = () -> {
			done = true;
			onStart();
			action.run();
			onComplete();
		};
	}
	
	/**
	 * Verilen iş çalışmaya başladı
	 */
	protected void onStart() {
		
		//xlog.d("İşlem çalışmaya başladı");
	}
	
	/**
	 * Verilen işlem tamamlandı
	 */
	protected void onComplete() {
		
		//xlog.d("İşlem tamamlandı");
	}
	
	public Runnable getAction() {
		
		return action;
	}
	
	public DelayedRunner setAction(@NotNull Runnable action) {
		
		this.action = () -> {
			done = true;
			onStart();
			action.run();
			onComplete();
		};
		
		return this;
	}
	
	public boolean isDone() {
		
		return done;
	}
	
	public void setDone(boolean done) {
		
		this.done = done;
	}
	
	public boolean isOnBackground() {
		
		return onBackground;
	}
	
	public DelayedRunner onBackground() {
		
		this.onBackground = true;
		return this;
	}
	
	public DelayedRunner onBackground(boolean onBackground) {
		
		this.onBackground = onBackground;
		return this;
	}
	
	/**
	 * Verilen süre sonunda işlem çalıştırılır.
	 *
	 * @param delay Süre
	 */
	public void run(long delay) {
		
		Runny.run(action, !onBackground, delay);
	}
	
	/**
	 * Süre tamamlanmadan önce işlemin iptal edilmesini sağlar.
	 * Eğer yapılacak iş işleme konulduysa iptal edilemez.
	 * {@link #isDone()} metodu {@code true} dönerse iptal işlemi %99 başarılı olur.
	 */
	public void cancel() {
		
		Runny.cancel(action, onBackground);
	}
}
