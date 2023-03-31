package com.tr.hsyn.watch.observable;


import java.util.concurrent.atomic.AtomicReference;


/**
 * <h1>Observer</h1>
 *
 * <p>
 * Gözlemleyici. {@link Observable} arayüzünü uygulayan sınıf.
 *
 * @param <T> tür
 * @author hsyn 2019-12-06 14:32:58
 * @see BasicObserver
 * @see Observable
 */
public final class Observe<T> extends BasicObserver<T> implements Observable<T> {

	private final AtomicReference<T> v = new AtomicReference<>();

	public Observe() {}

	public Observe(T value) {

		v.set(value);
	}

	@Override
	public void initValue(T value) {

		v.set(value);
	}

	@Override
	public T get() {

		return v.get();
	}

	@Override
	public void set(T value) {

		v.set(value);
		notifyChange(value);
	}
}
