package com.tr.hsyn.time.duration;


import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;


public class TimeDurationImp extends DurationImp implements TimeDuration {
	
	public TimeDurationImp(@NotNull Unit unit, long value) {
		
		super(unit, value);
		
		if (unit.overLimit(value))
			throw new IllegalArgumentException("Value is out of limit");
	}
	
	@Override
	public @NotNull Duration plus(long value) {
		
		var v = getValue() + value;
		
		if (getUnit().inLimit(v)) return super.plus(value);
		else {
			
			var current = v % getUnit().getLimit();
			
			
		}
	}
	
	@Override
	public @NotNull Duration plus(@NotNull Duration other) {
		
		return super.plus(other);
	}
}
