package com.tr.hsyn.objext;


import org.jetbrains.annotations.Nullable;


public class Obje<T> implements Objex<T> {
	
	private ChangeListener<T> listener;
	private T                 object;
	
	Obje() {}
	
	Obje(@Nullable T obj) {
		
		object = obj;
	}
	
	@Nullable
	@Override
	public T getValue() {
		
		return object;
	}
	
	@Override
	public void setValue(@Nullable T obj) {
		
		object = obj;
		
		if (listener != null) listener.onChange(obj);
	}
	
	@Override
	public void listenChange(@Nullable ChangeListener<T> listener) {
		
		this.listener = listener;
	}
}
