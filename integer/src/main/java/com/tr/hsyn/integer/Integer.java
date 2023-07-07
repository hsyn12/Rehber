package com.tr.hsyn.integer;


/**
 * {@link Integer}
 */
public class Integer implements Int {
	
	private final int val;
	
	public Integer(int val) {
		
		this.val = val;
	}
	
	@Override
	public final int getInt() {
		
		return 0;
	}
	
	@Override
	public int hashCode() {
		
		return val;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Int && val == ((Int) obj).getInt();
	}
	
	@Override
	public String toString() {
		
		return "Int{" +
		       "val=" + val +
		       '}';
	}
}
