package com.tr.hsyn.executors;


import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * Defines executors for normal-priority and low-priority threads.<br>
 * {@link #NORM_PRIORITY_EXECUTOR} executes the works on normal-priority threads.<br>
 * {@link #MIN_PRIORITY_EXECUTOR} executes the works on low-priority threads.<br>
 * <p>
 * All threads of these executors are on background and can be schedule with desired delays.<br>
 */
public interface Executors {
	
	/**
	 * Low-priority thread pool counts.
	 */
	int                         THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_LOW_PRIORITY  = 8;
	/**
	 * Normal-priority thread pool counts.
	 */
	int                         THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_NORM_PRIORITY = 8;
	/**
	 * The normal-priority thread priority.
	 */
	int                         NORM_PRIORITY                                    = Thread.NORM_PRIORITY;
	/**
	 * The low-priority thread priority.
	 */
	int                         MIN_PRIORITY                                     = Thread.MIN_PRIORITY;
	/**
	 * The executor, which {@link ScheduledThreadPoolExecutor} with normal priority.<br>
	 */
	ScheduledThreadPoolExecutor NORM_PRIORITY_EXECUTOR                           = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_NORM_PRIORITY, new PriorityThreadFactory(NORM_PRIORITY));
	/**
	 * The executor, which {@link ScheduledThreadPoolExecutor} with low priority.
	 */
	ScheduledThreadPoolExecutor MIN_PRIORITY_EXECUTOR                            = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE_FOR_EXECUTOR_WITH_LOW_PRIORITY, new PriorityThreadFactory(MIN_PRIORITY));
	
}
