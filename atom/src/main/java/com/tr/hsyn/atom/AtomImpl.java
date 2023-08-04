package com.tr.hsyn.atom;


import org.jetbrains.annotations.NotNull;


public class AtomImpl implements Atom {
	
	private final long   id;
	private final String name;
	
	public AtomImpl(long id, @NotNull String name) {
		
		this.name = name;
		this.id   = id;
	}
	
	@Override
	public @NotNull String getName() {
		
		return name;
	}
	
	@Override
	public long getId() {
		
		return id;
	}
	
	@Override
	public String toString() {
		
		return "Atom{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       '}';
	}
}
