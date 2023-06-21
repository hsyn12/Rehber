package com.tr.hsyn.time.duration;


import com.tr.hsyn.scaler.Generation;
import com.tr.hsyn.scaler.Generator;

import org.jetbrains.annotations.NotNull;


/**
 * A duration generator
 */
public class DurationGenerator implements Generator<Duration> {
	
	private final Duration step;
	private       Duration current;
	
	/**
	 * Creates a new DurationGenerator.
	 * The start point is zero minute.
	 *
	 * @param step duration step
	 */
	public DurationGenerator(Duration start, Duration step) {
		
		current   = start;
		this.step = step;
	}
	
	/**
	 * Creates a new DurationGenerator.
	 * Start point is zero of the given step.
	 *
	 * @param step duration step
	 */
	public DurationGenerator(Duration step) {
		
		this(Duration.of(step.getUnit(), 0), step);
	}
	
	/**
	 * Creates a new DurationGenerator.
	 * Start point is zero minute.
	 *
	 * @param step duration step
	 */
	public DurationGenerator(long step) {
		
		this(Duration.ofMinute(0), Duration.ofMinute(step));
	}
	
	/**
	 * Creates a new DurationGenerator.
	 * Step unit is unit of the given duration.
	 *
	 * @param start start point
	 * @param step  duration step
	 */
	public DurationGenerator(@NotNull Duration start, long step) {
		
		current   = start;
		this.step = Duration.of(start.getUnit(), step);
	}
	
	@Override
	public Generation<Duration> getGeneration(Duration start, Duration end, Duration step) {
		
		return new Generation<>() {
			
			private Duration current = start;
			
			@Override
			public Duration getStep() {
				
				return step;
			}
			
			@Override
			public Duration getStart() {
				
				return start;
			}
			
			@Override
			public Duration getEnd() {
				
				return null;
			}
			
			@Override
			public Duration getNext() {
				
				return current = current.plus(step);
			}
		};
	}
}
