package com.tr.hsyn.key;


import com.tr.hsyn.atom.Atom;

import org.jetbrains.annotations.NotNull;


public class Key extends Atom {
	
	public static final Key CONTACTS = Key.of(1, "Contacts");
	public static final Key CALL_LOG = Key.of(2, "Call Log");
	public static final Key MESSAGE  = Key.of(3, "Message");
	
	public Key(int id, @NotNull String name) {
		
		super(id, name);
	}
	
	@NotNull
	public static Key of(int id, @NotNull String name) {
		
		return new Key(id, name);
	}
}
