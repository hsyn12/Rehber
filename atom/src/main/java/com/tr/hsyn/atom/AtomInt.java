package com.tr.hsyn.atom;


public class AtomInt {
	
	private final int    id;
	private final String name;
	
	public AtomInt(int id, String name) {
		
		this.id   = id;
		this.name = name;
	}
	
	public final int getId() {
		
		return id;
	}
	
	public final String getName() {
		
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof AtomInt && id == ((AtomInt) obj).id;
	}
	
	public boolean equals(AtomInt obj) {
		
		return obj != null && id == obj.id;
	}
	
	@Override
	public int hashCode() {
		
		return id;
	}
}
