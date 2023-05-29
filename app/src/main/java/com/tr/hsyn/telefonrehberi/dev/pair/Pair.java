package com.tr.hsyn.telefonrehberi.dev.pair;


import org.jetbrains.annotations.NotNull;


public class Pair<P, R> {
	
	private final P first;
	private final R second;
	
	public Pair(P first, R second) {
		
		this.first  = first;
		this.second = second;
	}
	
	@NotNull
	public static <P, R> Pair<P, R> of(P p, R r) {
		
		return new Pair<>(p, r);
	}
	
	public P getFirst() {
		
		return first;
	}
	
	public R getSecond() {
		
		return second;
	}
}
