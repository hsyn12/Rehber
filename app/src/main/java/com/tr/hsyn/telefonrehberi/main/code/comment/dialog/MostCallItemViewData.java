package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


public class MostCallItemViewData {
	
	private final String  name;
	private final int     callSize;
	private final int     order;
	private       boolean selected;
	
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
	
	public boolean isSelected() {
		
		return selected;
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
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
