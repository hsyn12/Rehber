package com.tr.hsyn.integer;


public class Integer implements Int {
	
	private final int val;
	
	public Integer(int val) {
		
		this.val = val;
	}
	
	@Override
	public final int getInt() {
		
		return val;
	}
	
	@Override
	public int hashCode() {
		
		return val;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Int && val == ((Int) obj).getInt();
	}
	
	@SuppressWarnings("DefaultLocale")
	@Override
	public String toString() {
		
		return String.format("Int{val=%d}", val);
	}
}
