package com.tr.hsyn.time.duration;


import com.tr.hsyn.scaler.Generation;
import com.tr.hsyn.scaler.Generator;


public class DurationGroupGenerator implements Generator<DurationGroup> {
	
	private final Duration      step;
	private       DurationGroup current;
	
	public DurationGroupGenerator() {
		
		this(Duration.ofMinute(1), DurationGroup.EMPTY);
	}
	
	public DurationGroupGenerator(Duration step) {
		
		this(step, DurationGroup.EMPTY);
	}
	
	public DurationGroupGenerator(DurationGroup start) {
		
		this(Duration.ofMinute(1), start);
	}
	
	public DurationGroupGenerator(Duration step, DurationGroup start) {
		
		this.step    = step != null ? step : Duration.ofMinute(1);
		this.current = start != null ? start : DurationGroup.EMPTY;
	}
	
	
	@Override
	public Generation<DurationGroup> getGeneration(DurationGroup start, DurationGroup end, DurationGroup step) {
		
		return new Generation<DurationGroup>() {
			
			private DurationGroup current = start;
			
			@Override
			public DurationGroup getStep() {
				
				return step;
			}
			
			@Override
			public DurationGroup getStart() {
				
				return start;
			}
			
			@Override
			public DurationGroup getEnd() {
				
				return end;
			}
			
			@Override
			public DurationGroup getNext() {
				
				return current = current.plus(step);
			}
		};
	}
}
