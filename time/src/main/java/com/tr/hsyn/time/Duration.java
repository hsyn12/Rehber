package com.tr.hsyn.time;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * Bir <b><i>süre</i></b> belirtmeyi sağlar.<br>
 * Süreler, belirli zaman birimleriyle gösterilir ({@link Unit}).<br><br>
 * <pre>
 *    var duration = Duration.of(Unit.MINUTE, 1981); // 1981 dakikalık bir süre
 *    //var duration = Duration.ofMinute(1981);// Yukarıdaki ile aynı
 * </pre>
 * <p>
 * Süre belirtirken herhangi bir kısıtlama yoktur,
 * yukarıda belirtilen dakika, olduğu gibi kaydedilir.
 * Yani zaman olarak işlem görmez,
 * çünkü eğer zaman olarak işlem görseydi,
 * dakika değeri en fazla {@code 59} olabilirdi.<br>
 * Yine de bir <b>süre</b> tam olarak zaman karşılığına çevrilebilir.<br><br>
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
	 * Sürenin tam olarak ne kadar zaman ettiğini döndürür.
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
	
	/**
	 * @return Sürenin zaman olarak milisaniye değeri
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
	 * @return Returns {@code true} if Duration is not empty, {@code false} otherwise
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
	
	
}
