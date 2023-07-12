package com.tr.hsyn.telefonrehberi.dev;


import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;


/**
 * <h1>FixedSizeDeque</h1>
 * Belirli sayıda eleman tutacak.
 * Sayı aşılırsa sondan silinecek.
 *
 * @param <E> Eleman türü
 */
public class FixedSizeDeque<E> extends ConcurrentLinkedDeque<E> {
	
	private final int         fixedSize;
	private       Consumer<E> exceedFunction;
	
	public FixedSizeDeque(int size) {
		
		this.fixedSize = size;
	}
	
	public FixedSizeDeque(int size, Consumer<E> exceedFunction) {
		
		this.fixedSize      = size;
		this.exceedFunction = exceedFunction;
	}
	
	@Override
	public void addFirst(E e) {
		
		super.addFirst(e);
		checkSize();
	}
	
	@Override
	public void addLast(E e) {
		
		super.addLast(e);
		checkSize();
	}
	
	@Override
	public boolean add(E e) {
		
		addFirst(e);
		
		checkSize();
		
		return true;
	}
	
	@Override
	public boolean addAll(@NotNull Collection<? extends E> c) {
		
		for (E e : c) addFirst(e);
		return true;
	}
	
	public Consumer<E> getExceedFunction() {
		
		return exceedFunction;
	}
	
	public int getFixedSize() {
		
		return fixedSize;
	}
	
	private void checkSize() {
		
		if (size() > fixedSize) {
			
			E e = this.removeLast();
			
			if (exceedFunction != null) exceedFunction.accept(e);
		}
		
	}
}
