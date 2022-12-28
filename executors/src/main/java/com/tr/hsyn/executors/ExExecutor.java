package com.tr.hsyn.executors;


import java.util.concurrent.Executor;


public interface ExExecutor extends Executor {
	
	void execute(Runnable command, long delay);
	
	void cancel(Runnable command);
}
