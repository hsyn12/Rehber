package com.tr.hsyn.atom;


import com.tr.hsyn.entity.Entity;
import com.tr.hsyn.identity.Identity;

import org.jetbrains.annotations.NotNull;


/**
 * Kimlik oluşturur
 */
public class Atom implements Identity, Entity {
	
	private final int    id;
	private final String name;
	
	public Atom(int id, @NotNull String name) {
		
		this.id   = id;
		this.name = name;
	}
	
	@Override
	public final @NotNull String getName() {return name;}
	
	@Override
	public final int getId() {return id;}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Atom && id == ((Atom) obj).getId();
	}
	
	@Override
	public int hashCode() {return id;}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Atom{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       '}';
	}
}