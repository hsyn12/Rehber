package com.tr.hsyn.colors;


public class Colorsky implements ColorHolder {
	
	private final int primaryColor;
	private final int lastColor;
	private final int ripple;
	
	public Colorsky(int primaryColor, int lastColor, int ripple) {
		
		this.primaryColor = primaryColor;
		this.lastColor    = lastColor;
		this.ripple       = ripple;
	}
	
	@Override
	public int getPrimaryColor() {
		
		return primaryColor;
	}
	
	@Override
	public int getLastColor() {
		
		return lastColor;
	}
	
	@Override
	public int getRipple() {
		
		return ripple;
	}
}
