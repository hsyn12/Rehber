package com.tr.hsyn.objext;


import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;


class SafeObje<T> implements SafeObjex<T> {
	
	private final AtomicReference<T> object = new AtomicReference<>();
	private       ChangeListener<T>  listener;
	
	SafeObje() {}
	
	SafeObje(@Nullable T obj) {object.set(obj);}
	
	@Override
	public @Nullable T getValue() {
		
		return object.get();
	}
	
	@Override
	public void setValue(@Nullable T obj) {
		
		object.set(obj);
		
		if (listener != null) listener.onChange(obj);
	}
	
	@Override
	public void listenChange(@Nullable ChangeListener<T> listener) {
		
		this.listener = listener;
	}
}
