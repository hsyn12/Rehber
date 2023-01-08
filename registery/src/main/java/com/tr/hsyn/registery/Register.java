package com.tr.hsyn.registery;


import com.tr.hsyn.registery.cast.Database;


public class Register<T> {
	
	private final Database<T> database;
	
	public Register(Database<T> database) {
		
		this.database = database;
		
		
	}
	
	
}