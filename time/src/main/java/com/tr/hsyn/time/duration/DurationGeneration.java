package com.tr.hsyn.time.duration;


import com.tr.hsyn.scaler.Generation;

import org.jetbrains.annotations.NotNull;


public class DurationGeneration implements Generation<Duration> {
	
	private final Duration start;
	private final Duration end;
	private final Duration step;
	private       Duration current;
	
	public DurationGeneration(@NotNull Duration start, @NotNull Duration end, @NotNull Duration step) {
		
		this.start = start;
		this.end   = end;
		this.step  = step;
	}
	
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
		
		return end;
	}
	
	@Override
	public boolean hasNext() {
		
		return current.compareTo(end) < 0;
	}
	
	@Override
	public Duration next() {
		
		return current = current.plus(step);
	}
	
	
}
