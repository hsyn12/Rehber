package com.tr.hsyn.executors;


import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * Executors
 */
public interface Executors {
	
	int                         THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_LOW_PRIORITY  = 8;
	int                         THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_NORM_PRIORITY = 8;
	int                         THREAD_PRIORITY_BACKGROUND                       = Thread.NORM_PRIORITY;
	int                         THREAD_PRIORITY_LOWEST                           = Thread.MIN_PRIORITY;
	ScheduledThreadPoolExecutor NORM_PRIORITY_EXECUTOR                           = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_NORM_PRIORITY, new PriorityThreadFactory(THREAD_PRIORITY_BACKGROUND));
	ScheduledThreadPoolExecutor MIN_PRIORITY_EXECUTOR                            = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_LOW_PRIORITY, new PriorityThreadFactory(THREAD_PRIORITY_LOWEST));
	
}
