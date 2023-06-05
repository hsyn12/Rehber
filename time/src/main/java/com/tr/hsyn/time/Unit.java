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
	YEAR,
	/**
	 * Month
	 */
	MONTH,
	/**
	 * DAY
	 */
	DAY,
	/**
	 * Hour
	 */
	HOUR,
	/**
	 * MINUTES
	 */
	MINUTE,
	/**
	 * Seconds
	 */
	SECOND,
	/**
	 * Millisecond
	 */
	MILLISECOND;
	
	static final ResourceBundle BUNDLE = ResourceBundle.getBundle("units");
	
	@NotNull
	@Override
	public String toString() {
		
		switch (this) {
			
			case YEAR: return BUNDLE.getString("YEAR");
			case MONTH: return BUNDLE.getString("MONTH");
			case DAY: return BUNDLE.getString("DAY");
			case HOUR: return BUNDLE.getString("HOUR");
			case MINUTE: return BUNDLE.getString("MINUTE");
			case SECOND: return BUNDLE.getString("SECOND");
			case MILLISECOND: return BUNDLE.getString("MILLISECOND");
			default: return "";
		}
	}
	
	/**
	 * Compares this {@link Unit} with the given {@link Unit}.
	 *
	 * @param other {@link Unit}
	 * @return {@code true} if this {@link Unit} is greater than the given {@link Unit}.<br>
	 * 		Unit order is: {@link #YEAR}, {@link #MONTH}, {@link #DAY},
	 *      {@link #HOUR}, {@link #MINUTE},
	 *      {@link #SECOND}, {@link #MILLISECOND}.
	 * 		So, the greatest unit is {@link #YEAR},
	 * 		the least unit is {@link #MILLISECOND}.
	 * 		But {@link #ordinal()} returns reversed order,
	 * 		so <code>YEAR.ordinal()</code> returns {@code 0},
	 * 		<code>MONTH.ordinal()</code> returns {@code 1} etc.
	 */
	public boolean isGreaterByUnit(@NotNull Unit other) {
		
		return this.ordinal() < other.ordinal();
	}
	
	/**
	 * Compares this {@link Unit} with the given {@link Unit}.
	 *
	 * @param other {@link Unit}
	 * @return {@code true} if this {@link Unit} is less than the given {@link Unit}.<br>
	 * 		Unit order is: {@link #YEAR}, {@link #MONTH}, {@link #DAY},
	 *      {@link #HOUR}, {@link #MINUTE},
	 *      {@link #SECOND}, {@link #MILLISECOND}.
	 * 		So, the greatest unit is {@link #YEAR},
	 * 		the least unit is {@link #MILLISECOND}.
	 * 		But {@link #ordinal()} returns reversed order,
	 * 		so <code>YEAR.ordinal()</code> returns {@code 0},
	 * 		<code>MONTH.ordinal()</code> returns {@code 1} etc.
	 */
	public boolean isLessByUnit(@NotNull Unit other) {
		
		return this.ordinal() > other.ordinal();
	}
}
