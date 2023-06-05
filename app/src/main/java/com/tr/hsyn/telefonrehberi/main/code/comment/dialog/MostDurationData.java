package com.tr.hsyn.telefonrehberi.main.code.comment.dialog;


public class MostDurationData {
	
	private final String name;
	private final String text;
	private final int    order;
	
	public MostDurationData(String name, String text, int order) {
		
		this.name  = name;
		this.text  = text;
		this.order = order;
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
