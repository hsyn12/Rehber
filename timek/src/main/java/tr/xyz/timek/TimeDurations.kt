@file:JvmName("TimedDurations")

package tr.xyz.timek

import androidx.annotation.IntRange
import tr.xyz.digit.Digit
import tr.xyz.timek.unit.TimeUnit
import kotlin.math.absoluteValue

sealed class Limits {
	
	companion object {
		
		fun of(unit: TimeUnit): Int {
			return when (unit) {
				TimeUnit.YEAR        -> YEAR
				TimeUnit.MONTH       -> MONTH
				TimeUnit.DAY         -> DAY
				TimeUnit.HOUR        -> HOUR
				TimeUnit.MINUTE      -> MINUTE
				TimeUnit.SECOND      -> SECOND
				TimeUnit.MILLISECOND -> MILLISECOND
			}
		}
		
		const val YEAR = 999999999
		const val MONTH = 12
		const val DAY = 30
		const val HOUR = 24
		const val MINUTE = 60
		const val SECOND = 60
		const val MILLISECOND = 1000
	}
}

/**
 * Represents a duration of time with units (year, month, day, hour, minute, second, millisecond).
 * The duration is calculated based on a given value in milliseconds.
 * ```
 * ```
 * [TimeDurations] is a time duration,
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
class TimeDurations(val value: Long = 0) {
	
	val durations: List<TimeDuration>
	val year: TimeDuration get() = durations[0]
	val month: TimeDuration get() = durations[1]
	val day: TimeDuration get() = durations[2]
	val hour: TimeDuration get() = durations[3]
	val minute: TimeDuration get() = durations[4]
	val second: TimeDuration get() = durations[5]
	val millisecond: TimeDuration get() = durations[6]
	
	val nonZeroDurations: List<TimeDuration> get() = durations.filter {it.isNotZero}
	val nonZeroUnits: List<TimeUnit> get() = nonZeroDurations.map {it.unit}
	
	constructor(vararg durations: Duration) : this(durations.sumOf {it.asMilliseconds})
	
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
		
		val yearDuration = TimeDuration years year
		val monthDuration = TimeDuration months month
		val dayDuration = TimeDuration days day
		val hourDuration = TimeDuration hours hour
		val minuteDuration = TimeDuration minutes minute
		val secondDuration = TimeDuration seconds second
		val millisecondDuration = TimeDuration milliseconds millisecond
		
		yearDuration.value.right = monthDuration.value
		monthDuration.value.right = dayDuration.value
		dayDuration.value.right = hourDuration.value
		hourDuration.value.right = minuteDuration.value
		minuteDuration.value.right = secondDuration.value
		secondDuration.value.right = millisecondDuration.value
		millisecondDuration.value.left = secondDuration.value
		secondDuration.value.left = minuteDuration.value
		minuteDuration.value.left = hourDuration.value
		hourDuration.value.left = dayDuration.value
		dayDuration.value.left = monthDuration.value
		monthDuration.value.left = yearDuration.value
		
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
	
	override fun toString() = "$year $month $day $hour $minute $second $millisecond"
	
	fun toString(format: String) = format.format(year.value, month.value, day.value, hour.value, minute.value, second.value, millisecond.value)
	
	fun toString(vararg units: TimeUnit): String {
		
		return buildString {
			for (duration in durations) {
				if (units.contains(duration.unit)) append("${duration.value} ${duration.unit}").append(" ")
			}
		}.trim()
	}
	
	fun toStringNonZero(): String = toString(*nonZeroUnits.toTypedArray())
	
	operator fun compareTo(other: TimeDurations): Int = value.compareTo(other.value)
	operator fun compareTo(other: Long): Int = value.compareTo(other)
	
	operator fun plusAssign(duration: TimeDuration) {
		when (duration.unit) {
			TimeUnit.YEAR        -> year.value += duration.value
			TimeUnit.MONTH       -> month.value += duration.value
			TimeUnit.DAY         -> day.value += duration.value
			TimeUnit.HOUR        -> hour.value += duration.value
			TimeUnit.MINUTE      -> minute.value += duration.value
			TimeUnit.SECOND      -> second.value += duration.value
			TimeUnit.MILLISECOND -> millisecond.value += duration.value
		}
	}
	
	operator fun minusAssign(duration: TimeDuration) {
		when (duration.unit) {
			TimeUnit.YEAR        -> year.value -= duration.value
			TimeUnit.MONTH       -> month.value -= duration.value
			TimeUnit.DAY         -> day.value -= duration.value
			TimeUnit.HOUR        -> hour.value -= duration.value
			TimeUnit.MINUTE      -> minute.value -= duration.value
			TimeUnit.SECOND      -> second.value -= duration.value
			TimeUnit.MILLISECOND -> millisecond.value -= duration.value
		}
	}
}

class TimeDuration(value: Int, val unit: TimeUnit) {
	
	var value: Digit = Digit.newDigit(0, Limits.of(unit))
	val isNotZero: Boolean get() = value.digitValue != 0
	val isZero: Boolean get() = value.digitValue == 0
	
	init {
		
		this.value.digitValue = value
	}
	
	val toMilliseconds: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value.digitValue.toLong()
				TimeUnit.SECOND      -> value.digitValue.toLong() * 1000
				TimeUnit.MINUTE      -> value.digitValue.toLong() * 1000 * 60
				TimeUnit.HOUR        -> value.digitValue.toLong() * 1000 * 60 * 60
				TimeUnit.DAY         -> value.digitValue.toLong() * 1000 * 60 * 60 * 24
				TimeUnit.MONTH       -> value.digitValue.toLong() * 1000 * 60 * 60 * 24 * 30
				TimeUnit.YEAR        -> value.digitValue.toLong() * 1000 * 60 * 60 * 24 * 365
			}
		}
	
	operator fun inc(): TimeDuration {
		value++
		return this
	}
	
	operator fun dec(): TimeDuration {
		value--
		return this
	}
	
	operator fun plusAssign(other: TimeDuration) {
		value += other.value
	}
	
	operator fun plusAssign(other: Int) {
		value += other
	}
	
	operator fun minusAssign(other: TimeDuration) {
		value -= other.value
	}
	
	operator fun minusAssign(other: Int) {
		value.digitValue -= other
	}
	
	override fun toString(): String = "$value $unit"
	
	companion object {
		
		fun of(value: Int, unit: TimeUnit) = TimeDuration(value, unit)
		infix fun milliseconds(@IntRange(from = 0, to = 1000) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MILLISECOND)
		infix fun seconds(@IntRange(from = 0, to = 60) value: Int): TimeDuration = TimeDuration(value, TimeUnit.SECOND)
		infix fun minutes(@IntRange(from = 0, to = 60) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MINUTE)
		infix fun hours(@IntRange(from = 0, to = 24) value: Int): TimeDuration = TimeDuration(value, TimeUnit.HOUR)
		infix fun days(@IntRange(from = 0, to = 30) value: Int): TimeDuration = TimeDuration(value, TimeUnit.DAY)
		infix fun months(@IntRange(from = 0, to = 12) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MONTH)
		infix fun years(@IntRange(from = 0, to = 999_999_999) value: Int): TimeDuration = TimeDuration(value, TimeUnit.YEAR)
	}
	
}

fun main() {
	val timeDuration = TimeDuration minutes 55
	val left = TimeDuration hours 0
	val right = TimeDuration seconds 0
	
	timeDuration.value.left = left.value
	timeDuration.value.right = right.value
	println(timeDuration)
	timeDuration += 8
	println(timeDuration)
	println("left  : $left")
	println("right : $right")
	
	
}
