package com.tr.hsyn.watch;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;


public class FutureAction implements Watch {
	
	private final Runnable runnable;
	private final long     runTime;
	private final int      id = ThreadLocalRandom.current().nextInt();
	
	public FutureAction(@NotNull Runnable runnable, long runTime) {
		
		this.runnable = runnable;
		this.runTime  = runTime;
	}
	
	@Override
	@NotNull
	public Runnable getRunnable() {
		
		return runnable;
	}
	
	@Override
	public long getRunTime() {
		
		return runTime;
	}
	
	@Override
	public int getId() {
		
		return id;
	}
}
