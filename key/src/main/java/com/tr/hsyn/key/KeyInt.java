package com.tr.hsyn.key;


import com.tr.hsyn.atom.AtomInt;

import org.jetbrains.annotations.NotNull;


public class KeyInt extends AtomInt {
	
	public KeyInt(int id, @NotNull String name) {
		
		super(id, name);
	}
	
	@NotNull
	public static KeyInt of(int id, @NotNull String name) {
		
		return new KeyInt(id, name);
	}
}
