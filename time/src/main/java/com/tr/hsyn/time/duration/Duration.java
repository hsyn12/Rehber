package com.tr.hsyn.time.duration;


import com.tr.hsyn.random.Randoom;
import com.tr.hsyn.scaler.Generatable;
import com.tr.hsyn.scaler.Generation;
import com.tr.hsyn.scaler.Generator;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * Allows specifying a <b><i>duration<i></b>.<br>
 * Durations are shown in specific units ({@link Unit}).<br><br>
 * <pre>
 *    var duration = Duration.of(Unit.MINUTE, 1981); // 1981 minutes duration
 *    //var duration = Duration.ofMinute(1981);// same as above
 * </pre>
 * <p>
 * There are no restrictions when specifying the duration,
 * the that minute is saved as it is.
 * So it is not treated as time, because if it were treated as time,
 * the minute value could be at most {@code 59}.<br>
 * However,
 * a <b>duration</b> can be converted to a time by calling {@link #toTimeDuration()}<br><br>
 *
 * <pre>
 * var duration = Duration.ofMinute(1981);
 * System.out.println(duration.toTimeDuration());
 * // [Duration{type=day, value=1}, Duration{type=hour, value=9}, Duration{type=minute, value=1}]
 * </pre>
 *
 * @see Unit
 * @see DurationGroup
 */
public interface Duration extends Comparable<Duration>, Generatable<Duration> {
	
	@Override
	default Generator<Duration> getGenerator() {
		
		return (start, end, step) -> {
			
			if (start.equalsByUnit(end) && end.equalsByUnit(step))
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
						
						return end;
					}
					
					@Override
					public boolean hasNext() {
						
						return current.lessThan(end);
					}
					
					@Override
					public Duration getNext() {
						
						if (hasNext()) return current = current.plus(step);
						
						throw new IndexOutOfBoundsException("End is less than start : " + start + " - " + end);
					}
				};
			
			throw new IllegalArgumentException("Units must be the same : " + start.getUnit() + " - " + end.getUnit() + " - " + step.getUnit());
		};
	}
	
	/**
	 * @return {@code true} if duration value is zero, {@code false} otherwise
	 */
	default boolean isZero() {
		
		return getValue() == 0L;
	}
	
	/**
	 * @return {@code true} if duration value is not zero, {@code false} otherwise
	 */
	default boolean isNotZero() {
		
		return getValue() != 0L;
	}
	
	/**
	 * If this duration is not zero then calls the given {@code consumer}.
	 *
	 * @param consumer consumer to be called
	 */
	default void isNotZero(@NotNull Consumer<Duration> consumer) {
		
		if (getValue() != 0L) consumer.accept(this);
	}
	
	/**
	 * Adds {@code value} to the {@link Duration}.
	 * İf the given value is negative, made subtract.
	 *
	 * <pre>
	 *     var duration = Duration.of(Unit.MINUTE, 5);
	 *     var duration2 = Duration.ofMinute(-1);
	 *     var duration3 = duration.plus(duration2); // Duration{type=minute, value=4}
	 *     var duration4 = duration.plus(-1L); // Duration{type=minute, value=4}
	 * </pre>
	 *
	 * @param value Amount of the time
	 * @return new {@link Duration}
	 */
	@NotNull
	default Duration plus(long value) {
		
		return new DurationImp(getUnit(), getValue() + value);
	}
	
	/**
	 * @param other duration
	 * @return {@code true} if this {@link Duration} is less than other
	 */
	default boolean lessThan(@NotNull Duration other) {
		
		return this.compareTo(other) < 0;
	}
	
	/**
	 * @param other duration
	 * @return {@code true} if this {@link Duration} is less than other or equal
	 */
	default boolean lessThanOrEqual(@NotNull Duration other) {
		
		return this.compareTo(other) <= 0;
	}
	
	/**
	 * @param other duration
	 * @return {@code true} if this {@link Duration} is greater than other
	 */
	default boolean greaterThan(@NotNull Duration other) {
		
		return this.compareTo(other) > 0;
	}
	
	/**
	 * @param other duration
	 * @return {@code true} if this {@link Duration} is greater than other or equal
	 */
	default boolean greaterThanOrEqual(@NotNull Duration other) {
		
		return this.compareTo(other) >= 0;
	}
	
	/**
	 * Adds {@code Duration} to this {@link Duration}.
	 * İf the given duration is negative, made subtract.
	 * İf the given {@linkplain Duration} has a different {@linkplain Unit},
	 * it is converted to this {@linkplain Duration} unit.
	 * So returned {@link Duration} unit is same as this {@linkplain Duration} unit.
	 *
	 * <pre>
	 *     var duration = Duration.of(Unit.MINUTE, 5);
	 *     var duration2 = Duration.ofMinute(-1);
	 *     var duration3 = duration.plus(duration2); // Duration{type=minute, value=4}
	 *     var duration4 = duration.plus(-1L); // Duration{type=minute, value=4}
	 * </pre>
	 *
	 * @param other Other {@linkplain Duration} object to add
	 * @return new {@link Duration}
	 */
	@NotNull
	default Duration plus(@NotNull Duration other) {
		
		if (isDifferentByUnit(other)) {
			
			Duration d = other.getValueAs(getUnit());
			return new DurationImp(getUnit(), getValue() + d.getValue());
		}
		
		return new DurationImp(getUnit(), getValue() + other.getValue());
	}
	
	@NotNull
	String toString(@NotNull String formatted);
	
	/**
	 * @return the unit of this {@linkplain Duration} object
	 */
	@NotNull
	Unit getUnit();
	
	/**
	 * @return duration value of this {@linkplain Duration} object
	 */
	long getValue();
	
	/**
	 * Determines whether this <code>Duration</code> is different from other {@linkplain Duration} by unit and value.
	 *
	 * @param other Other {@linkplain Duration} object
	 * @return {@code true} if this <code>Duration</code> is not equal to other {@link Duration}.
	 */
	default boolean isDifferent(@NotNull Duration other) {
		
		return !this.equals(other);
	}
	
	/**
	 * Checks whether this <code>Duration</code> unit is different
	 * from given <code>Duration</code> {@link Unit}.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> unit is different
	 * 		from given {@link Unit} by unit
	 */
	default boolean isDifferentByUnit(@NotNull Unit other) {
		
		return !getUnit().equals(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> unit is different from given <code>Duration</code> unit.
	 *
	 * @param other Other {@linkplain Duration} object
	 * @return {@code true} if this <code>Duration</code> unit is different from given {@link Duration} unit
	 */
	default boolean isDifferentByUnit(@NotNull Duration other) {
		
		return isDifferentByUnit(other.getUnit());
	}
	
	/**
	 * Determines whether this <code>Duration</code> unit is greater than given {@link Unit}.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> unit is greater than given {@link Unit}
	 * @see Unit#isGreaterThan(Unit)
	 */
	default boolean isGreaterByUnit(@NotNull Unit other) {
		
		return getUnit().isGreaterThan(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> unit is less than given {@link Unit}.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> unit is less than given {@link Unit}
	 * @see Unit#isLessThan(Unit)
	 */
	default boolean isLessByUnit(@NotNull Unit other) {
		
		return getUnit().isLessThan(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> is equal to given {@link Unit}.
	 *
	 * @param unit the unit
	 * @return {@code true} if this <code>Duration</code> is equal to given {@link Unit}
	 */
	default boolean isUnit(@NotNull Unit unit) {
		
		return getUnit().equals(unit);
	}
	
	/**
	 * Determines whether this unit of the duration is equal to given unit of the duration.
	 *
	 * @param duration the duration
	 * @return {@code true} if this unit of the duration is equal to given unit of the duration
	 */
	default boolean equalsByUnit(@NotNull Duration duration) {
		
		return getUnit().equals(duration.getUnit());
	}
	
	/**
	 * Returns exactly how long the duration is as date format.
	 *
	 * <pre>
	 * var duration = Duration.ofMinute(1981);
	 * System.out.println(duration.toTimeDuration());
	 * // [Duration{type=day, value=1}, Duration{type=hour, value=9}, Duration{type=minute, value=1}]
	 * </pre>
	 *
	 * @return {@link DurationGroup}
	 */
	@NotNull
	default DurationGroup toTimeDuration() {
		
		return Time.toDuration(toMilliseconds());
	}
	
	/**
	 * @return as milliseconds of this {@linkplain Duration}
	 */
	default long toMilliseconds() {
		
		long value = getValue();
		
		//@off
		switch (getUnit()) {
			case MILLISECOND : return value;
			case SECOND :      return value * 1000;
			case MINUTE :      return value * 60000;
			case HOUR  :       return value * 3600000;
			case DAY :         return value * 86400000;
			case MONTH  :      return value * 259200000;
			case YEAR :        return value * 36500000;
		}//@on
		
		return 0;
	}
	
	/**
	 * Converts the duration to the given unit
	 *
	 * @param unit the unit
	 * @return {@link Duration}
	 */
	@NotNull
	default Duration getValueAs(@NotNull Unit unit) {
		
		long value = toMilliseconds();
		//@off
		switch (unit) {
			case MILLISECOND : return new DurationImp(unit, value);
			case SECOND : return new DurationImp(unit, value / 1000);
			case MINUTE : return new DurationImp(unit, value / 60000);
			case HOUR : return new DurationImp(unit, value / 3600000);
			case DAY : return new DurationImp(unit, value / 86400000);
			case MONTH : return new DurationImp(unit, value / 259200000);
			case YEAR : return new DurationImp(unit, value / 36500000);
		}//@on
		
		throw new IllegalArgumentException("Unknown unit: " + unit);
	}
	
	static void main(String[] args) {
		
		@NotNull Duration duration  = Duration.of(Unit.MINUTE, 5);
		@NotNull Duration duration2 = Duration.ofMinute(-1);
		Duration          duration3 = duration.plus(duration2); // Duration{type=minute, value=4}
		System.out.println(duration3.toString());
	}
	
	/**
	 * Creates a new Duration with given minutes.
	 *
	 * @param minutes Number of minutes
	 */
	@NotNull
	static Duration ofMinute(long minutes) {
		
		return of(Unit.MINUTE, minutes);
	}
	
	/**
	 * Creates a new Duration.
	 *
	 * @param unit  Unit of time
	 * @param value Amount of time
	 * @return Duration
	 */
	@NotNull
	static Duration of(@NotNull Unit unit, long value) {
		
		return new DurationImp(unit, value);
	}
	
	@NotNull
	static DurationGroup of(@NotNull List<Duration> durations) {
		
		return DurationGroup.of(durations);
	}
	
	@NotNull
	static DurationGroup of(@NotNull Duration... durations) {
		
		return DurationGroup.of(new ArrayList<>(Arrays.asList(durations)));
	}
	
	/**
	 * Creates a new Duration with value of zero.
	 *
	 * @param unit Unit of time
	 */
	@NotNull
	static Duration ofZero(@NotNull Unit unit) {return new DurationImp(unit, 0L);}
	
	/**
	 * Creates a new Duration with given years.
	 *
	 * @param years Number of years
	 */
	@NotNull
	static Duration ofYear(long years) {return of(Unit.YEAR, years);}
	
	/**
	 * Creates a new Duration with given months.
	 *
	 * @param months Number of months
	 */
	@NotNull
	static Duration ofMonth(long months) {
		
		return of(Unit.MONTH, months);
	}
	
	/**
	 * Creates a new Duration with given days.
	 *
	 * @param days Number of days
	 */
	@NotNull
	static Duration ofDay(long days) {return of(Unit.DAY, days);}
	
	/**
	 * Creates a new Duration with given hours.
	 *
	 * @param hours Number of hours
	 */
	@NotNull
	static Duration ofHour(long hours) {
		
		return of(Unit.HOUR, hours);
	}
	
	/**
	 * Creates a new Duration with given seconds.
	 *
	 * @param seconds Number of seconds
	 */
	@NotNull
	static Duration ofSecond(long seconds) {
		
		return of(Unit.SECOND, seconds);
	}
	
	/**
	 * Creates a new Duration with given milliseconds.
	 *
	 * @param milliseconds Number of milliseconds
	 **/
	@NotNull
	static Duration ofMillisecond(long milliseconds) {
		
		return of(Unit.MILLISECOND, milliseconds);
	}
	
	interface Random {
		
		long MAX_VALUE = 100L;
		
		@NotNull
		static Duration getNext() {
			
			return new DurationImp(Unit.values()[Randoom.getInt(Unit.values().length)], Randoom.getLong(MAX_VALUE));
		}
	}
}
