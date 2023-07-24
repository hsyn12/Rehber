package com.tr.hsyn.telefonrehberi.main.call.cast.base;


public class GenerationExeption extends RuntimeException {
	
	public GenerationExeption() {
		
		super();
	}
	
	public GenerationExeption(String message) {
		
		super(message);
	}
	
	public GenerationExeption(String message, Throwable cause) {
		
		super(message, cause);
	}
	
	public GenerationExeption(Throwable cause) {
		
		super(cause);
	}
	
	protected GenerationExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
