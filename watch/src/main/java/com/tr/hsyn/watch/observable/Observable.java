package com.tr.hsyn.watch.observable;


import org.jetbrains.annotations.NotNull;


/**
 * <h1>Observable</h1>
 *
 * <p>
 * Değişimleri gözlenecek varlıklar için arayüz.
 * Bir varlık bu arayüz içine alınırsa,
 * {@link #setObserver(Observer)} metodu ile kaydedilen
 * dinleyici değişimlerden haberdar olur.
 * Bu varlıklar genel olarak basit türlerdir.
 * Integer, String vs.
 *
 * @param <T>
 * @author hsyn 2019-12-06 14:25:31
 * @see Observer
 */
public interface Observable<T> {
	
	/**
	 * Nesnenin değerini ilkler.
	 * Bu metot dinleyiciyi uyandırmaz.
	 *
	 * @param value değer
	 */
	void initValue(T value);
	
	/**
	 * Değeri döndür.
	 *
	 * @return değer
	 */
	T get();
	
	/**
	 * Değeri değiştirir.
	 * Bu metot dinleyiciyi uyandırır.
	 *
	 * @param value değer
	 */
	void set(T value);
	
	/**
	 * Değişimleri dinleyecek olan şahıs.
	 *
	 * @param observer dinleyici
	 */
	void setObserver(Observer<T> observer);
	
	default boolean compareAndSet(@NotNull T expected, T newValue) {
		
		if (expected.equals(get())) {
			
			set(newValue);
			return true;
		}
		
		return false;
	}
	
	@NotNull
	static <T> Observable<T> create() {
		
		return new Observe<>();
	}
	
	@NotNull
	static <T> Observable<T> create(T initValue) {
		
		var o = new Observe<T>();
		o.initValue(initValue);
		return o;
	}
}
