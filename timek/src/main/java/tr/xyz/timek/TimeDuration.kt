@file:JvmName("TimeDurations")

package tr.xyz.timek

import tr.xyz.timek.unit.TimeUnit
import kotlin.math.absoluteValue

/**
 * Represents a duration of time with units (year, month, day, hour, minute, second, millisecond).
 * The duration is calculated based on a given value in milliseconds.
 * ```
 * ```
 * [TimeDuration] is a time duration,
 * not a time point. And each unit has its own limit.
 * For example, the duration of a month is exactly 30 days,
 * an hour is exactly 60 minutes, and a minute is exactly 60 seconds.
 *
 * ```
 *
 * val timeDuration = TimeDuration(TimeMillis.MINUTE * 6565)
 * println(timeDuration.toStringNonZero())
 * println(timeDuration.toString())
 * println(timeDuration.toString("%1\$04d years %2\$02d months %3\$02d days %4\$02d hours %5\$02d minutes %6\$02d seconds %7\$03d milliseconds"))
 * // toStringNonZero : 4 day 13 hour 25 minute
 * // toString        : 0 years 0 months 4 days 13 hours 25 minutes 0 seconds 0 milliseconds
 * // toString(format): 0000 years 00 months 04 days 13 hours 25 minutes 00 seconds 000 milliseconds
 * ```
 *
 * @property value  number of milliseconds to calculate the duration
 * @property durations list of calculated durations
 * @property year duration of year
 * @property month duration of month
 * @property day duration of day
 * @property hour duration of hour
 * @property minute duration of minute
 * @property second duration of second
 * @property millisecond duration of millisecond
 * @constructor Creates a new time duration with optional value of zero.
 * @see TimeMillis
 */
class TimeDuration(val value: Long = 0) {
	
	val durations: List<Duration>
	val year: Duration get() = durations[0]
	val month: Duration get() = durations[1]
	val day: Duration get() = durations[2]
	val hour: Duration get() = durations[3]
	val minute: Duration get() = durations[4]
	val second: Duration get() = durations[5]
	val millisecond: Duration get() = durations[6]
	
	val nonZeroDurations: List<Duration> get() = durations.filter {it.isNotZero}
	val nonZeroUnits: List<TimeUnit> get() = nonZeroDurations.map {it.unit}
	
	constructor(vararg duration: Duration) : this(duration.sumOf {it.toMillis()})
	
	init {
		
		val isNegative = value < 0
		var year = 0
		var month = 0
		var day = 0
		var hour = 0
		var minute = 0
		var second = 0
		var millisecond = 0
		var _duration = value.absoluteValue
		
		while (true) {
			
			if (_duration >= TimeMillis.YEAR) {
				year = (_duration / TimeMillis.YEAR).toInt()
				if (isNegative) year = -year
				_duration %= TimeMillis.YEAR
			}
			else if (_duration >= TimeMillis.MONTH) {
				month = (_duration / TimeMillis.MONTH).toInt()
				if (isNegative) month = -month
				_duration %= TimeMillis.MONTH
			}
			else if (_duration >= TimeMillis.DAY) {
				day = (_duration / TimeMillis.DAY).toInt()
				if (isNegative) day = -day
				_duration %= TimeMillis.DAY
			}
			else if (_duration >= TimeMillis.HOUR) {
				hour = (_duration / TimeMillis.HOUR).toInt()
				if (isNegative) hour = -hour
				_duration %= TimeMillis.HOUR
			}
			else if (_duration >= TimeMillis.MINUTE) {
				
				minute = (_duration / TimeMillis.MINUTE).toInt()
				if (isNegative) minute = -minute
				_duration %= TimeMillis.MINUTE
			}
			else if (_duration >= TimeMillis.SECOND) {
				second = (_duration / TimeMillis.SECOND).toInt()
				if (isNegative) second = -second
				_duration %= TimeMillis.SECOND
			}
			else {
				millisecond = _duration.toInt()
				if (isNegative) millisecond = -millisecond
				break
			}
		}
		
		val yearDuration = Duration years year
		val monthDuration = Duration months month
		val dayDuration = Duration days day
		val hourDuration = Duration hours hour
		val minuteDuration = Duration minutes minute
		val secondDuration = Duration seconds second
		val millisecondDuration = Duration milliseconds millisecond
		
		millisecondDuration.value.left = secondDuration.value
		secondDuration.value.left = minuteDuration.value
		minuteDuration.value.left = hourDuration.value
		hourDuration.value.left = dayDuration.value
		dayDuration.value.left = monthDuration.value
		monthDuration.value.left = yearDuration.value
		yearDuration.value.right = monthDuration.value
		monthDuration.value.right = dayDuration.value
		dayDuration.value.right = hourDuration.value
		hourDuration.value.right = minuteDuration.value
		minuteDuration.value.right = secondDuration.value
		secondDuration.value.right = millisecondDuration.value
		
		durations = listOf(
			yearDuration,
			monthDuration,
			dayDuration,
			hourDuration,
			minuteDuration,
			secondDuration,
			millisecondDuration,
		)
	}
	
	override fun toString() = "$year years $month months $day days $hour hours $minute minutes $second seconds $millisecond milliseconds"
	
	fun toString(format: String) = format.format(year.value.digitValue, month.value.digitValue, day.value.digitValue, hour.value.digitValue, minute.value.digitValue, second.value.digitValue, millisecond.value.digitValue)
	
	fun toString(vararg units: TimeUnit): String {
		
		return buildString {
			for (duration in durations) {
				if (units.contains(duration.unit)) append("${duration.value.digitValue} ${duration.unit}").append(" ")
			}
		}.trim()
	}
	
	fun toStringNonZero(): String = toString(*nonZeroUnits.toTypedArray())
	
	operator fun compareTo(other: TimeDuration): Int = value.compareTo(other.value)
	operator fun compareTo(other: Long): Int = value.compareTo(other)
	
	operator fun plus(duration: Duration) = TimeDuration(value + duration.toMillis())
}

fun main() {
	
	val minutes = Duration minutes 6
	val timeDuration = TimeDuration(minutes)
	println(timeDuration.toString())
	println(timeDuration + (Duration minutes 55))
	println(timeDuration.toString("%1\$04d years %2\$02d months %3\$02d days %4\$02d hours %5\$02d minutes %6\$02d seconds %7\$03d milliseconds"))
}
