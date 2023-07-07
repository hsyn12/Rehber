package com.tr.hsyn.datakey;


import com.tr.hsyn.integer.Integer;

import org.jetbrains.annotations.NotNull;


/**
 * Int data key.<br>
 * Provides the key of data by int value.<br>
 */
public final class IntKey extends Integer implements DataKey {
	
	private final boolean readable;
	private final boolean writable;
	private final String  name;
	
	public IntKey(int val, @NotNull String name) {
		
		this(val, name, true, true);
	}
	
	public IntKey(int keyValue, @NotNull String name, boolean readable) {
		
		this(keyValue, name, readable, true);
	}
	
	public IntKey(int keyValue, @NotNull String name, boolean readable, boolean writable) {
		
		super(keyValue);
		this.readable = readable;
		this.writable = writable;
		this.name     = name;
	}
	
	@Override
	public boolean isReadable() {
		
		return readable;
	}
	
	@Override
	public boolean isWritable() {
		
		return writable;
	}
	
	@Override
	public @NotNull String toString() {
		
		return "IntKey{" +
		       super.toString() + " " +
		       "readable=" + readable +
		       ", writable=" + writable +
		       '}';
	}
	
	@Override
	public String getName() {
		
		return name;
	}
}
