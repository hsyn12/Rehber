package com.tr.hsyn.atom;


import com.tr.hsyn.entity.Entity;
import com.tr.hsyn.identity.Identity;

import org.jetbrains.annotations.NotNull;


/**
 * The entity contract.
 * Provides a name, and an ID for the entity.
 */
public interface Atom extends Identity, Entity {
	
	@Override
	default @NotNull String getName() {
		
		return getClass().getCanonicalName();
	}
	
	@Override
	default long getId() {return getName().hashCode();}
	
	@NotNull
	static Atom create(long id, @NotNull String name) {
		
		return new AtomImpl(id, name);
	}
}