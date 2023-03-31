package com.tr.hsyn.executors;


import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class PriorityThreadFactory implements ThreadFactory {
	
	private final        int           priority;
	private final        boolean       daemon;
	private final        String        namePrefix;
	private final        AtomicInteger threadNumber = new AtomicInteger(1);
	private static final AtomicInteger poolNumber   = new AtomicInteger(1);
	
	public PriorityThreadFactory(int priority) {
		
		this(priority, true);
	}
	
	public PriorityThreadFactory(int priority, boolean daemon) {
		
		this.priority = priority;
		this.daemon   = daemon;
		
		String priorityStr = priority == 5 ? "Norm" : priority < 5 ? "Low" : "High";
		
		namePrefix = "Worker [priority:" + priorityStr + " pool:" + poolNumber.getAndIncrement() + " thread:";
	}
	
	@SuppressWarnings("NullableProblems")
	@Override
	public Thread newThread(Runnable r) {
		
		String name = String.format(Locale.getDefault(), "%s%d]", namePrefix, threadNumber.getAndIncrement());
		Thread t    = new Thread(r, name);
		t.setDaemon(daemon);
		t.setPriority(priority);
		return t;
	}
}
