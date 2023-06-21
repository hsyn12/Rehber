package com.tr.hsyn.time.duration;


import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;


/**
 * Duration for the time specific works.
 * Do not allow overflowing of the time unit limits.
 * For example, the minute unit has a limit of 60.
 *
 * @see Unit
 */
public interface TimeDuration extends Duration {
	
	@Override
	default @NotNull TimeDuration plus(long value) {
		
		var plus = Math.abs(getValue() + value);
		
		return withValue(plus % getUnit().getLimit());
	}
	
	@Override
	default TimeDuration withValue(long value) {
		
		return of(getUnit(), value);
	}
	
	@Override
	default @NotNull TimeDuration plus(@NotNull Duration other) {
		
		if (!equalsByUnit(other)) other = other.getValueAs(getUnit());
		
		var plus = Math.abs(getValue() + other.getValue());
		
		return withValue(plus % getUnit().getLimit());
	}
	
	@Override
	@NotNull
	default TimeDuration getValueAs(@NotNull Unit unit) {
		
		return withDuration(Duration.super.getValueAs(unit));
	}
	
	/**
	 * Converts the {@link Duration} to the {@link TimeDuration}.
	 *
	 * @param duration {@link Duration}
	 * @return new {@link TimeDuration}
	 */
	default TimeDuration withDuration(@NotNull Duration duration) {
		
		return of(duration.getUnit(), duration.getValue());
	}
	
	/**
	 * Creates a new {@link TimeDuration}.
	 *
	 * @param unit  unit
	 * @param value value
	 * @return new {@link TimeDuration}
	 */
	@NotNull
	static TimeDuration of(Unit unit, long value) {
		
		return new TimeDurationImp(unit, value);
	}
	
	public static void main(String[] args) {
		
		@NotNull TimeDuration duration  = TimeDuration.of(Unit.MINUTE, 5);
		@NotNull TimeDuration duration2 = Duration.ofMinute(-7).toTimeDuration();
		TimeDuration          duration3 = duration.plus(duration2); // TimeDuration{type=minute, value=4}
		System.out.println(duration3.toString());
	}
}
