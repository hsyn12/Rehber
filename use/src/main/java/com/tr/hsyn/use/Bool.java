package com.tr.hsyn.use;


import org.jetbrains.annotations.NotNull;


public class Bool {
	
	public static final Bool    FALSE = new Bool();
	public static final Bool    TRUE  = new Bool(true);
	private final       boolean bool;
	
	public Bool() {bool = false;}
	
	public Bool(boolean bool) {this.bool = bool;}
	
	@NotNull
	public static Bool of(boolean bool) {
		
		return bool ? TRUE : FALSE;
	}
	
	@NotNull
	public Bool ifTrue(@NotNull Runnable runnable) {
		
		if (bool) {
			
			runnable.run();
			return TRUE;
		}
		
		return FALSE;
	}
	
	@NotNull
	public Bool ifFalse(@NotNull Runnable runnable) {
		
		if (!bool) {
			
			runnable.run();
			return FALSE;
		}
		
		return TRUE;
	}
	
	
}
