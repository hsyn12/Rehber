package com.tr.hsyn.time.duration;


import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class DurationImp implements Duration {
	
	/**
	 * Time unit
	 */
	private final Unit unit;
	/**
	 * Value of time
	 */
	private final long value;
	
	/**
	 * Creates a new Duration.
	 *
	 * @param unit  Unit of the time
	 * @param value Amount of the time
	 */
	public DurationImp(Unit unit, long value) {
		
		this.unit  = unit;
		this.value = value;
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
		
		return toString("Duration{unit=%2$s, value=%1$d}");
	}
	
	/**
	 * Returns formatted {@linkplain String} representation of this {@linkplain Duration}.
	 *
	 * @param formatted Formatted {@linkplain String} representation.
	 *                  For example: <code>"%d %s"</code>.
	 *                  The first parameter is the value, the second parameter is the unit.
	 *                  Can be changed the order of the parameters like this <code>"%2$s %1$d"</code> (as known).
	 * @return Formatted {@linkplain String} representation
	 */
	@NotNull
	public String toString(@NotNull String formatted) {
		
		return String.format(formatted, value, unit);
	}
	
	/**
	 * @return the unit of this {@linkplain Duration} object
	 */
	public Unit getUnit() {
		
		return unit;
	}
	
	/**
	 * @return duration value of this {@linkplain Duration} object
	 */
	public long getValue() {
		
		return value;
	}
	
	@Override
	public int compareTo(@NotNull Duration duration) {
		
		if (duration.isUnit(this.unit)) {
			return Long.compare(this.value, duration.getValue());
		}
		
		return duration.getUnit().ordinal() - this.unit.ordinal();
	}
	
	
}
