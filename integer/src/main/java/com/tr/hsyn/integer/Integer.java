package com.tr.hsyn.integer;


/**
 * Daha hafif bir {@link java.lang.Integer} sınıfı
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
	public boolean equals(Object obj) {
		
		return obj instanceof Int && val == ((Int) obj).getInt();
	}
	
	public boolean equals(Int obj) {
		
		return obj != null && val == obj.getInt();
	}
	
	@Override
	public int hashCode() {
		
		return val;
	}
	
	@Override
	public String toString() {
		
		return "Int{" +
		       "val=" + val +
		       '}';
	}
}
