package com.tr.hsyn.regex.dev;


public class IndexedChar implements Indexy {
	
	private final char c;
	private final int  index;
	
	public IndexedChar(char c, int index) {
		
		this.c     = c;
		this.index = index;
	}
	
	@Override
	public char getChar() {
		
		return c;
	}
	
	@Override
	public int getIndex() {
		
		return index;
	}
}
