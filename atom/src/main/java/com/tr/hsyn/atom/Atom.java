package com.tr.hsyn.atom;


import com.tr.hsyn.entity.Entity;
import com.tr.hsyn.identity.Identity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * Kimlik oluşturur
 */
public class Atom implements Identity, Entity {
	
	private final long   id;
	private final String name;
	
	public Atom(long id, @NotNull String name) {
		
		this.id   = id;
		this.name = name;
	}
	
	@Override
	public final @NotNull String getName() {return name;}
	
	@Override
	public long getId() {return id;}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Atom && id == ((Atom) obj).getId();
	}
	
	@Override
	public int hashCode() {return Objects.hash(id);}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Atom{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       '}';
	}
}