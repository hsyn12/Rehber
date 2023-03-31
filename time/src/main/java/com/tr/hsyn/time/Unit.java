package com.tr.hsyn.time;


import org.jetbrains.annotations.NotNull;


/**
 * Type of the time units.
 * En çok kullanılan zaman birimlerini tanımlar.
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
	
	@NotNull
	@Override
	public String toString() {
		
		switch (this) {
			
			case YEAR: return "yıl";
			case MONTH: return "ay";
			case DAY: return "gün";
			case HOUR: return "saat";
			case MINUTE: return "dakika";
			case SECOND: return "saniye";
			case MILLISECOND: return "milisaniye";
			default: return "";
		}
	}
}
