package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


public class MostDurationData {
	
	private final String  name;
	private final String  text;
	private final int     order;
	private       boolean selected;
	
	public MostDurationData(String name, String text, int order) {
		
		this.name  = name;
		this.text  = text;
		this.order = order;
	}
	
	public boolean isSelected() {
		
		return selected;
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getText() {
		
		return text;
	}
	
	public int getOrder() {
		
		return order;
	}
}
