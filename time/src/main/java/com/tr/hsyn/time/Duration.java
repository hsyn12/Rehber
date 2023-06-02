package com.tr.hsyn.time;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * Allows specifying a <b><i>duration<i></b>.<br>
 * Durations are shown in specific units ({@link Unit}).<br><br>
 * <pre>
 *    var duration = Duration.of(Unit.MINUTE, 1981); // 1981 minutes duration
 *    //var duration = Duration.ofMinute(1981);// same as above
 * </pre>
 * <p>
 * There are no restrictions when specifying the duration,
 * the aforementioned minute is saved as it is.
 * So it is not treated as time, because if it were treated as time,
 * the minute value could be at most {@code 59}.<br>
 * However, a <b>duration</b> can be converted to an exact time equivalent.<br><br>
 *
 * <pre>
 * var duration = Duration.ofMinute(1981);
 * System.out.println(duration.toTimeDuration());
 * // [Duration{type=DAY, value=1}, Duration{type=HOUR, value=9}, Duration{type=MINUTE, value=1}]
 * </pre>
 *
 * @see Unit
 */
public class Duration {
	
	/**
	 * Time unit
	 */
	private final Unit unit;
	/**
	 * Time amount
	 */
	private final long value;
	
	/**
	 * Creates a new Duration.
	 *
	 * @param unit  Type of the time unit
	 * @param value Amount of the time
	 */
	Duration(@NotNull Unit unit, long value) {
		
		this.unit  = unit;
		this.value = value;
	}
	
	/**
	 * @return Returns {@code true} if Duration is empty, {@code false} otherwise
	 */
	public boolean isZero() {
		
		return value == 0L;
	}
	
	/**
	 * @return Returns {@code true} if <code>Duration</code> value is not zero, {@code false} otherwise
	 */
	public boolean isNotZero() {
		
		return value != 0L;
	}
	
	/**
	 * Süreye ekleme yapar.<br>
	 * Verilen süre negatif ise çıkarma yapmış olur.
	 *
	 * @param value Süreye eklenecek değer
	 * @return Yeni bir {@link Duration} nesnesi
	 */
	public Duration plus(long value) {
		
		return new Duration(this.unit, this.value + value);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(unit, value);
	}
	
	/**
	 * Checks whether this Duration is equal to other {@link Duration}.
	 *
	 * @param o Other {@linkplain Duration} object
	 * @return Returns {@code true} if {@link Unit} and {@link #value} are equal
	 */
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Duration && unit.equals(((Duration) o).getUnit()) && value == ((Duration) o).getValue();
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return value + " " + unit;
	}
	
	/**
	 * @return Type of the unit
	 */
	public Unit getUnit() {
		
		return unit;
	}
	
	/**
	 * @return Amount of the time
	 */
	public long getValue() {
		
		return value;
	}
	
	/**
	 * @param other Other {@linkplain Duration} object
	 * @return {@code true} if this <code>Duration</code> is different from other {@link Duration}.
	 */
	public boolean isDifferent(@NotNull Duration other) {
		
		return !this.equals(other);
	}
	
	/**
	 * Checks whether this <code>Duration</code> is different from given {@link Unit} by unit.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> is different from given {@link Unit} by unit
	 */
	public boolean isDifferentByUnit(@NotNull Unit other) {
		
		return !unit.equals(other);
	}
	
	public boolean isGreaterByUnit(@NotNull Unit other) {
		
		return unit.isGreaterByUnit(other);
	}
	
	public boolean isLessByUnit(@NotNull Unit other) {
		
		return unit.isLessByUnit(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> is equal to given {@link Unit}.
	 *
	 * @param unit Type of the unit
	 * @return {@code true} if this <code>Duration</code> is equal to given {@link Unit}
	 */
	public boolean is(@NotNull Unit unit) {
		
		return this.unit.equals(unit);
	}
	
	/**
	 * Returns exactly how long the duration is.
	 *
	 * <pre>
	 * var duration = Duration.ofMinute(1981);
	 * System.out.println(duration.toTimeDuration());
	 * // [Duration{type=DAY, value=1}, Duration{type=HOUR, value=9}, Duration{type=MINUTE, value=1}]
	 * </pre>
	 *
	 * @return {@link DurationGroup}
	 */
	public DurationGroup toTimeDuration() {
		
		return Time.toDuration(toMilliseconds());
	}
	
	/**
	 * @return time in milliseconds
	 */
	public long toMilliseconds() {
		
		switch (unit) {
			case MILLISECOND:
				return value;
			case SECOND:
				return value * 1000;
			case MINUTE:
				return value * 60000;
			case HOUR:
				return value * 3600000;
			case DAY: return value * 86400000;
			case MONTH: return value * 259200000;
			case YEAR: return value * 36500000;
			
		}
		
		throw new IllegalArgumentException("This is impossible : " + unit);
	}
	
	public static void main(String[] args) {
		
		var duration = Duration.ofMinute(1981);
		var times    = duration.toTimeDuration();
		System.out.println(duration.toTimeDuration());
		System.out.println(Time.ToString(times.toLocalDateTime()));
	}
	
	/**
	 * Creates a new Duration with given minutes.
	 *
	 * @param minutes Number of minutes
	 */
	@NotNull
	public static Duration ofMinute(long minutes) {
		
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
	public static Duration of(@NotNull Unit unit, long value) {
		
		return new Duration(unit, value);
	}
	
	@NotNull
	public static DurationGroup of(@NotNull List<Duration> durations) {
		
		return new DurationGroup(durations);
	}
	
	@NotNull
	public static DurationGroup of(@NotNull Duration... durations) {
		
		return new DurationGroup(List.of(durations));
	}
	
	/**
	 * Creates a new Duration with value of zero.
	 *
	 * @param unit Unit of time
	 */
	@NotNull
	public static Duration ofZero(@NotNull Unit unit) {return new Duration(unit, 0L);}
	
	/**
	 * Creates a new Duration with given years.
	 *
	 * @param years Number of years
	 */
	@NotNull
	public static Duration ofYear(long years) {return of(Unit.YEAR, years);}
	
	/**
	 * Creates a new Duration with given months.
	 *
	 * @param months Number of months
	 */
	@NotNull
	public static Duration ofMonth(long months) {
		
		return of(Unit.MONTH, months);
	}
	
	/**
	 * Creates a new Duration with given days.
	 *
	 * @param days Number of days
	 */
	@NotNull
	public static Duration ofDay(long days) {return of(Unit.DAY, days);}
	
	/**
	 * Creates a new Duration with given hours.
	 *
	 * @param hours Number of hours
	 */
	@NotNull
	public static Duration ofHour(long hours) {
		
		return of(Unit.HOUR, hours);
	}
	
	/**
	 * Creates a new Duration with given seconds.
	 *
	 * @param seconds Number of seconds
	 */
	@NotNull
	public static Duration ofSecond(long seconds) {
		
		return of(Unit.SECOND, seconds);
	}
	
	/**
	 * Creates a new Duration with given milliseconds.
	 *
	 * @param milliseconds Number of milliseconds
	 **/
	@NotNull
	public static Duration ofMillisecond(long milliseconds) {
		
		return of(Unit.MILLISECOND, milliseconds);
	}
	
}
