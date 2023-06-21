package com.tr.hsyn.time.duration;


import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;


public class TimeDurationImp extends DurationImp implements TimeDuration {
	
	public TimeDurationImp(@NotNull Unit unit, long value) {
		
		super(unit, value % unit.getLimit());
	}
	
	
}
