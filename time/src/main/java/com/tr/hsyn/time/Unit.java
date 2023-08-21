package com.tr.hsyn.time;

import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 * Type of the time units.
 */
public enum Unit {
	/**
	 * Year
	 */
	YEAR(Long.MAX_VALUE),
	/**
	 * Month
	 */
	MONTH(12L),
	/**
	 * DAY
	 */
	DAY(30L),
	/**
	 * Hour
	 */
	HOUR(24L),
	/**
	 * MINUTES
	 */
	MINUTE(60L),
	/**
	 * Seconds
	 */
	SECOND(60L),
	/**
	 * Millisecond
	 */
	MILLISECOND(1000L);
	
	static final  ResourceBundle BUNDLE = ResourceBundle.getBundle("units");
	private final long           limit;
	
	Unit(long limit) {this.limit = limit;}
	
	@NotNull
	@Override
	public String toString() {
		
		switch (this) {
			
			case YEAR: return BUNDLE.getString("Year");
			case MONTH: return BUNDLE.getString("Month");
			case DAY: return BUNDLE.getString("Day");
			case HOUR: return BUNDLE.getString("Hour");
			case MINUTE: return BUNDLE.getString("Minute");
			case SECOND: return BUNDLE.getString("Second");
			case MILLISECOND: return BUNDLE.getString("Millisecond");
			default: return "";
		}
	}
	
	/**
	 * Determines whether the value is in the limit.
	 *
	 * @param value Value
	 *
	 * @return {@code true} if the value is in the limit
	 */
	public boolean inLimit(long value) {
		
		return value <= limit;
	}
	
	/**
	 * Determines whether the value is out of the limit.
	 *
	 * @param value Value
	 *
	 * @return {@code true} if the value is out of the limit
	 */
	public boolean overLimit(long value) {
		
		return value > limit;
	}
	
	public long getLimit() {
		
		return limit;
	}
	
	/**
	 * Compares this {@link Unit} with the given {@link Unit}.
	 *
	 * @param other {@link Unit}
	 *
	 * @return {@code true} if this {@link Unit} is greater than the given {@link Unit}.<br>
	 * 	Unit order is: {@link #YEAR}, {@link #MONTH}, {@link #DAY},
	 *   {@link #HOUR}, {@link #MINUTE},
	 *   {@link #SECOND}, {@link #MILLISECOND}.
	 * 	So, the greatest unit is {@link #YEAR},
	 * 	the least unit is {@link #MILLISECOND}.
	 * 	But {@link #ordinal()} returns reversed order,
	 * 	so <code>YEAR.ordinal()</code> returns {@code 0},
	 * 	<code>MONTH.ordinal()</code> returns {@code 1} etc.
	 */
	public boolean isGreaterThan(@NotNull Unit other) {
		
		return this.ordinal() < other.ordinal();
	}
	
	/**
	 * Compares this {@link Unit} with the given {@link Unit}.
	 *
	 * @param other {@link Unit}
	 *
	 * @return {@code true} if this {@link Unit} is less than the given {@link Unit}.<br>
	 * 	Unit order is: {@link #YEAR}, {@link #MONTH}, {@link #DAY},
	 *   {@link #HOUR}, {@link #MINUTE},
	 *   {@link #SECOND}, {@link #MILLISECOND}.
	 * 	So, the greatest unit is {@link #YEAR},
	 * 	the least unit is {@link #MILLISECOND}.
	 * 	But {@link #ordinal()} returns reversed order,
	 * 	so <code>YEAR.ordinal()</code> returns {@code 0},
	 * 	<code>MONTH.ordinal()</code> returns {@code 1} etc.
	 */
	public boolean isLessThan(@NotNull Unit other) {
		
		return this.ordinal() > other.ordinal();
	}
}
