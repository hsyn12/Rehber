package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


public class MostCallItemViewData {
	
	private final String name;
	private final int    callSize;
	private final int    order;
	
	public MostCallItemViewData(String name, int callSize) {
		
		this.name     = name;
		this.callSize = callSize;
		order         = 0;
	}
	
	public MostCallItemViewData(String name, int callSize, int order) {
		
		this.name     = name;
		this.callSize = callSize;
		this.order    = order;
	}
	
	public int getOrder() {
		
		return order;
	}
	
	public String getName() {
		
		return name;
	}
	
	public int getCallSize() {
		
		return callSize;
	}
}
