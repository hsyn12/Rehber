package com.tr.hsyn.watch;


import org.jetbrains.annotations.NotNull;

public interface Watch {
	
	int getId();
	
	Runnable getRunnable();
	
	long getRunTime();
	
	@NotNull
	static Watch that(Runnable runnable, long time) {
		
		return new FutureAction(runnable, time);
	}
}
