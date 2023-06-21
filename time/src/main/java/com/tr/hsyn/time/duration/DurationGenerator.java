package com.tr.hsyn.time.duration;


import com.tr.hsyn.scaler.Generator;

import org.jetbrains.annotations.NotNull;


/**
 * A duration generator
 */
public class DurationGenerator implements Generator<Duration> {
	
	private final long     step;
	private       Duration current;
	
	/**
	 * Creates a new DurationGenerator.
	 * The start point is zero minute.
	 *
	 * @param step duration step
	 */
	public DurationGenerator(long step) {
		
		current   = Duration.ofMinute(0);
		this.step = step;
	}
	
	/**
	 * Creates a new DurationGenerator.
	 *
	 * @param start start point
	 * @param step  duration step
	 */
	public DurationGenerator(@NotNull Duration start, long step) {
		
		current   = start;
		this.step = step;
	}
	
	@Override
	public Duration getNext() {
		
		return current = current.plus(step);
	}
}
