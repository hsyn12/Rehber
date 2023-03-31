package com.tr.hsyn.along;


import java.util.Objects;


/**
 * Double integer, Long.
 */
public final class DInt implements Long {
	
	private final long value;
	
	public DInt(long value) {
		
		this.value = value;
	}
	
	@Override
	public long getLong() {
		
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof DInt && value == ((DInt) obj).value;
	}
	
	public boolean equals(Long obj) {
		
		return obj != null && value == obj.getLong();
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hashCode(value);
	}
}