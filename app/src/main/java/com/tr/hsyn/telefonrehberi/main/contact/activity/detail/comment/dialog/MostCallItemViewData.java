package com.tr.hsyn.telefonrehberi.main.contact.activity.detail.comment.dialog;


public class MostCallItemViewData {
	
	private final String name;
	private final int    callSize;
	
	public MostCallItemViewData(String name, int callSize) {
		
		this.name     = name;
		this.callSize = callSize;
	}
	
	public String getName() {
		
		return name;
	}
	
	public int getCallSize() {
		
		return callSize;
	}
}
