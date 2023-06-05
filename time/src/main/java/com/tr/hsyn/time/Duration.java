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
 * // [Duration{type=day, value=1}, Duration{type=hour, value=9}, Duration{type=minute, value=1}]
 * </pre>
 *
 * @see Unit
 * @see DurationGroup
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
	 * @return {@code true} if duration value is zero, {@code false} otherwise
	 */
	public boolean isZero() {
		
		return value == 0L;
	}
	
	/**
	 * @return {@code true} if duration value is not zero, {@code false} otherwise
	 */
	public boolean isNotZero() {
		
		return value != 0L;
	}
	
	/**
	 * Adds {@code value} to this {@link Duration}.
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
	public Duration plus(long value) {
		
		return new Duration(this.unit, this.value + value);
	}
	
	/**
	 * Adds {@code Duration} to this {@link Duration}.
	 * İf the given duration is negative, made subtract.
	 * İf the given {@linkplain Duration} has a different {@linkplain Unit},
	 * it is converted to this {@linkplain Duration} unit.
	 * So returned {@link Duration} unit will be same as this {@linkplain Duration} unit.
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
	public Duration plus(@NotNull Duration other) {
		
		if (isDifferent(other)) {
			
			var d = other.getValueAs(this.unit);
			return new Duration(this.unit, this.value + d.getValue());
		}
		
		return new Duration(this.unit, this.value + value);
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(unit, value);
	}
	
	/**
	 * Checks whether this <code>Duration</code> is equal to other {@link Duration}.
	 *
	 * @param o Other {@linkplain Duration} object
	 * @return {@code true} if {@link Unit} and {@link #value} are equal
	 */
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Duration && unit.equals(((Duration) o).getUnit()) && value == ((Duration) o).getValue();
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return toString("Duration{type=%2$s, value=%1$d}");
	}
	
	/**
	 * Returns formatted {@linkplain String} representation of this {@linkplain Duration}.
	 *
	 * @param formatted Formatted {@linkplain String} representation.
	 *                  For example: <code>"%d %s"</code>.
	 *                  The first parameter is the value, the second parameter is the unit.
	 *                  But can be changed the order of the parameters like this <code>"%2$s %1$d"</code> (as known).
	 * @return Formatted {@linkplain String} representation
	 */
	@NotNull
	public String toString(@NotNull String formatted) {
		
		return String.format(formatted, value, unit);
	}
	
	/**
	 * @return the unit of this {@linkplain Duration}
	 */
	public Unit getUnit() {
		
		return unit;
	}
	
	/**
	 * @return duration value of this {@linkplain Duration}
	 */
	public long getValue() {
		
		return value;
	}
	
	/**
	 * @param other Other {@linkplain Duration} object
	 * @return {@code true} if this <code>Duration</code> is not equal to other {@link Duration}.
	 */
	public boolean isDifferent(@NotNull Duration other) {
		
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
	public boolean isDifferentByUnit(@NotNull Unit other) {
		
		return !unit.equals(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> unit is greater than given {@link Unit}.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> unit is greater than given {@link Unit}
	 * @see Unit#isGreaterByUnit(Unit)
	 */
	public boolean isGreaterByUnit(@NotNull Unit other) {
		
		return unit.isGreaterByUnit(other);
	}
	
	/**
	 * Determines whether this <code>Duration</code> unit is less than given {@link Unit}.
	 *
	 * @param other Other {@linkplain Unit}
	 * @return {@code true} if this <code>Duration</code> unit is less than given {@link Unit}
	 * @see Unit#isLessByUnit(Unit)
	 */
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
	public DurationGroup toTimeDuration() {
		
		return Time.toDuration(toMilliseconds());
	}
	
	/**
	 * @return as milliseconds of this {@linkplain Duration}
	 */
	public long toMilliseconds() {
		//@off
		switch (unit) {
			case MILLISECOND: return value;
			case SECOND:      return value * 1000;
			case MINUTE:      return value * 60000;
			case HOUR:        return value * 3600000;
			case DAY:         return value * 86400000;
			case MONTH:       return value * 259200000;
			case YEAR:        return value * 36500000;
			
		}//@on
		
		throw new IllegalArgumentException("This is impossible : " + unit);
	}
	
	/**
	 * Converts the duration to the given unit
	 *
	 * @param unit Type of the unit
	 * @return {@link Duration}
	 */
	public Duration getValueAs(@NotNull Unit unit) {
		
		long value = toMilliseconds();
		//@off
		switch (unit) {
			case MILLISECOND: return new Duration(unit, value);
			case SECOND:      return new Duration(unit, value / 1000);
			case MINUTE:      return new Duration(unit, value / 60000);
			case HOUR:        return new Duration(unit, value / 3600000);
			case DAY:         return new Duration(unit, value / 86400000);
			case MONTH:       return new Duration(unit, value / 259200000);
			case YEAR:        return new Duration(unit, value / 36500000);
		}//@on
		
		throw new IllegalArgumentException("This is impossible : " + unit);
		
	}
	
	public static void main(String[] args) {
		
		var duration  = Duration.of(Unit.MINUTE, 5);
		var duration2 = Duration.ofMinute(-1);
		var duration3 = duration.plus(duration2); // Duration{type=minute, value=4}
		System.out.println(duration3.toString());
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
