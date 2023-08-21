@file:JvmName("Times")

package tr.xyz.timek

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Milliseconds of the current time according to the system time. That milliseconds are the number
 * of milliseconds since January 1, 1970 00:00:00 GMT.
 */
val currentTimeMillis: Long get() = System.currentTimeMillis()

val DEFAULT_ZONE_OFFSET: ZoneOffset = ZoneOffset.of(ZoneId.systemDefault().rules.getOffset(Instant.now()).id)


/**
 * Represents a time in the timeline.
 *
 * @property millis The number of milliseconds since January 1, 1970 00:00:00 GMT.
 * If the value is negative, it represents a time before January 1, 1970 00:00:00 GMT.
 * If skipped, it represents the current time.
 * @constructor Creates a new time point with optional milliseconds.
 */
class Time(val millis: Long = currentTimeMillis) {
	
	operator fun minus(time: Time): Time = Time(millis - time.millis)
	operator fun plus(time: Time): Time = Time(millis + time.millis)
	operator fun compareTo(time: Time): Int = millis.compareTo(time.millis)
	operator fun compareTo(time: Long): Int = millis.compareTo(time)
	operator fun contains(time: Time): Boolean = millis >= time.millis
	operator fun contains(time: Long): Boolean = millis >= time
	/**
	 *  Creates a new time point.
	 * @param year year value
	 * @param month month value
	 * @param day day value
	 * @param hour hour value
	 * @param minute minute value
	 * @constructor Creates a new time point.
	 */
	constructor(year: Int = 0, month: Int = 1, day: Int = 1, hour: Int = 0, minute: Int = 0) : this(LocalDateTime.of(year, month, day, hour, minute).toEpochSecond(ZoneOffset.UTC) * 1000)
	/**
	 *
	 * @return formatted time string with default pattern [DEFAULT_DATE_TIME_PATTERN]
	 */
	override fun toString() = toString(DEFAULT_DATE_TIME_PATTERN)
	/**
	 *
	 * @param pattern pattern
	 * @return formatted time string
	 */
	fun toString(pattern: String) = toString(millis, pattern)
	
	companion object {
		
		/**
		 * ```
		 * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The following pattern letters are defined:
		 *
		 * Symbol  Meaning                     Presentation      Examples
		 * ------  -------                     ------------      -------
		 * G       era                         text              AD; Anno Domini; A
		 * u       year                        year              2004; 04
		 * y       year-of-era                 year              2004; 04
		 * D       day-of-year                 number            189
		 * M/L     month-of-year               number/text       7; 07; Jul; July; J
		 * d       day-of-month                number            10
		 *
		 * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
		 * Y       week-based-year             year              1996; 96
		 * w       week-of-week-based-year     number            27
		 * W       week-of-month               number            4
		 * E       day-of-week                 text              Tue; Tuesday; T
		 * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
		 * F       week-of-month               number            3
		 *
		 * a       am-pm-of-day                text              PM
		 * h       clock-hour-of-am-pm (1-12)  number            12
		 * K       hour-of-am-pm (0-11)        number            0
		 * k       clock-hour-of-am-pm (1-24)  number            0
		 *
		 * H       hour-of-day (0-23)          number            0
		 * m       minute-of-hour              number            30
		 * s       second-of-minute            number            55
		 * S       fraction-of-second          fraction          978
		 * A       milli-of-day                number            1234
		 * n       nano-of-second              number            987654321
		 * N       nano-of-day                 number            1234000000
		 *
		 * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
		 * z       time-zone name              zone-name         Pacific Standard Time; PST
		 * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
		 * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
		 * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
		 * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
		 *
		 * p       pad next                    pad modifier      1
		 *
		 * '       escape for text             delimiter
		 * ''      single quote                literal           '
		 * [       optional section start
		 * ]       optional section end
		 * #       reserved for future use
		 * {       reserved for future use
		 * }       reserved for future use
		 * ```
		 * *
		 *
		 * @see
		 *     [DateTimeFormatter](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)
		 */
		const val DEFAULT_DATE_TIME_PATTERN = "d MMMM yyyy EEEE HH:mm"
		/**
		 * @param time time
		 * @param pattern pattern
		 * @return formatted time string
		 * @see [DEFAULT_DATE_TIME_PATTERN]
		 */
		fun toString(time: Long, pattern: String = DEFAULT_DATE_TIME_PATTERN): String = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern));
		/**
		 *  Returns the duration between now and the given time point.
		 *
		 *
		 * @param time time point to compare to the now
		 * @return [Duration]
		 */
		fun howLongBefore(time: Long): Duration {
			
			val now: LocalDateTime = LocalDateTime.now()
			val date: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), DEFAULT_ZONE_OFFSET)
			
			require(!date.isAfter(now)) {"The time must be before now"}
			
			val year = (now.year - date.year).toLong()
			if (year >= 1) return Duration years year
			
			val days = (now.dayOfYear - date.dayOfYear).toLong()
			if (days > 30) return Duration months (days / 30)
			if (days >= 1) return Duration days days
			
			val hours = (now.minusHours(date.hour.toLong()).hour).toLong()
			if (hours >= 1) return Duration hours hours
			
			val minute = (now.minusMinutes(date.minute.toLong()).minute).toLong()
			if (minute >= 1) return Duration minutes minute
			
			return Duration seconds (now.minusSeconds(date.second.toLong()).second).toLong()
		}
	}
}

fun main() {
	
	val birthDay = Time(1981, 4, 12)
	println(birthDay)
	println(TimeDurations(birthDay.millis))
}






