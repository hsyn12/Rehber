package com.tr.hsyn.along;


import java.util.Objects;


/**
 * Double integer, Long.
 */
public final class DInt {
	
	private final long value;
	
	public DInt(long value) {
		
		this.value = value;
	}
	
	public long getLong() {
		
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof DInt && value == ((DInt) obj).value;
	}
	
	public boolean equals(DInt obj) {
		
		return obj != null && value == obj.value;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hashCode(value);
	}
}