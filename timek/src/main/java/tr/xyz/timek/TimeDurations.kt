@file:JvmName("TimedDurations")

package tr.xyz.timek

import androidx.annotation.IntRange
import tr.xyz.digit.Digit
import tr.xyz.timek.unit.TimeUnit
import kotlin.math.absoluteValue

/**
 * Defines the limit of time units.
 */
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
		/**
		 *  The limit of the year.
		 */
		const val YEAR = 999999999
		/**
		 *  The limit of the month.
		 */
		const val MONTH = 12
		/**
		 *  The limit of the day.
		 */
		const val DAY = 30
		/**
		 *  The limit of the hour.
		 */
		const val HOUR = 24
		/**
		 *  The limit of the minute.
		 */
		const val MINUTE = 60
		/**
		 *  The limit of the second.
		 */
		const val SECOND = 60
		/**
		 *  The limit of the millisecond.
		 */
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
 * an hour is exactly 60 minutes, and a minute is exactly 60 seconds etc.
 *
 * ```
 *
 * val timeDuration = TimeDurations(TimeMillis.MINUTE * 6565)
 * println(timeDuration.toStringNonZero())
 * println(timeDuration.toString())
 * println(timeDuration.toString("%1\$04d years %2\$02d months %3\$02d days %4\$02d hours %5\$02d minutes %6\$02d seconds %7\$03d milliseconds"))
 * // toStringNonZero : 4 days 13 hours 25 minutes
 * // toString        : 0 years 0 months 4 days 13 hours 25 minutes 0 seconds 0 milliseconds
 * // toString(format): 0000 years 00 months 04 days 13 hours 25 minutes 00 seconds 000 milliseconds
 * ```
 *
 * @property value  number of milliseconds to calculate the duration
 * @property durations list of calculated durations
 * @property year calculated duration of year
 * @property month calculated duration of month
 * @property day calculated duration of day
 * @property hour calculated duration of hour
 * @property minute calculated duration of minute
 * @property second calculated duration of second
 * @property millisecond calculated duration of millisecond
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
/**
 * Provides to define a duration of time with one unit (year, month, day, hour, minute, second, millisecond).
 *
 * Each unit has its own limit.
 * For example, the duration of a month is exactly `30` days,
 * an hour is exactly `60` minutes, and a minute is exactly `60` seconds etc.
 *
 * ```
 *
 *    val timeDuration = TimeDuration minutes 55
 *    println("$timeDuration")
 *    // 55 minutes
 * ```
 *
 * @property unit [TimeUnit] of the duration
 * @property value value of the duration
 * @property isNotZero indicates if the duration is not zero
 * @property isZero indicates if the duration is zero
 * @property toMilliseconds milliseconds equivalent of the duration
 * @constructor Creates a new time duration with value and its unit.
 * @see TimeMillis
 * @see TimeUnit
 * @see Digit
 */
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
	
	/**
	 * Returns a new [TimeDuration] with the value added.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be thrown.
	 * And after the addition, the result duration will have the same unit as this.
	 * Moreover, if overflow occurs, the result will be truncated.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 55
	 *    val otherDuration = TimeDuration minutes 10
	 *
	 *    println(timeDuration + otherDuration) // 5 minutes
	 * ```
	 *
	 * Remember, [TimeDuration] always has a limit and certainly cannot exceed the limit.
	 * This method is for the time durations that never cause overflow.
	 * If you need to add durations that can cause overflow, use [plusAssign] and
	 * set the [Digit.left] to observe the positive overflow or
	 * set the [Digit.right] to observe the negative overflow.
	 * Or set the both.
	 * Because of the not returning a new [TimeDuration], the overflow will be observable.
	 * While [TimeDuration] uses the [Digit] to represent the value,
	 * the all is done with that.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 55
	 *    val otherDuration = TimeDuration minutes 10
	 *    val overflow = TimeDuration hours 0
	 *    timeDuration.value.left = overflow.value
	 *    timeDuration += otherDuration
	 *    println("$overflow $timeDuration") // 1 hours 5 minutes
	 * ```
	 *
	 * @param other [TimeDuration] to add
	 * @return new [TimeDuration]
	 */
	operator fun plus(other: TimeDuration): TimeDuration {
		if (other.unit != unit) throw IllegalArgumentException("Units must be same to be able to be operated : $unit and ${other.unit}")
		return TimeDuration(value.digitValue + other.value.digitValue, unit)
	}
	
	/**
	 * Returns a new [TimeDuration] with the value subtracted.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be thrown.
	 * And after the addition, the result duration will have the same unit as this.
	 * Moreover, if overflow occurs, the result will be truncated.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 5
	 *    val otherDuration = TimeDuration minutes 10
	 *    println(timeDuration - otherDuration) // 55 minutes
	 * ```
	 *
	 * Remember, [TimeDuration] always has a limit and certainly cannot exceed the limit.
	 * This method is for the time durations that never cause overflow.
	 * If you need to add durations that can cause overflow, use [minusAssign] and
	 * set the [Digit.left] to observe the positive overflow or
	 * set the [Digit.right] to observe the negative overflow.
	 * Or set the both.
	 * Because of the not returning a new [TimeDuration], the overflow will be observable.
	 * While [TimeDuration] uses the [Digit] to represent the value,
	 * the all is done with that.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 5
	 *    val otherDuration = TimeDuration minutes 10
	 *    val overflow = Digit.newDigit(3)
	 *    timeDuration.value.right = overflow
	 *    timeDuration -= otherDuration
	 *    println("$timeDuration (overflow left $overflow)")
	 *    // 55 minutes (overflow left 2)
	 * ```
	 * @param other TimeDuration
	 * @return TimeDuration
	 */
	operator fun minus(other: TimeDuration): TimeDuration {
		if (other.unit != unit) throw IllegalArgumentException("Units must be same to be able to be operated : $unit and ${other.unit}")
		return TimeDuration(value.digitValue - other.value.digitValue, unit)
	}
	/**
	 * Adds another [TimeDuration] to this duration.
	 * This operation will be caused to change the value of this duration.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be thrown.
	 * And after the addition, the result duration will have the same unit as this.
	 * Moreover, if overflow occurs, the result will be truncated and
	 * the (positive) overflow count will be forwarded to the `left` digit of this digit (if its exists).
	 *~~~
	 *
	 *    val timeDuration        = TimeDuration minutes 55
	 *    val otherDuration       = TimeDuration minutes 10
	 *    val hour                = TimeDuration hours 0
	 *    timeDuration.value.left = hour.value
	 *
	 *    timeDuration += otherDuration
	 *    println("$hour $timeDuration")
	 *    // 1 hours 5 minutes
	 * ~~~
	 *
	 * Although the hour object has a value of zero, it takes the value `1` after the operation.
	 * This is the `cycle count`.
	 * Positive overflow will be forwarded to the `left` digit.
	 * But negative overflow **will not be forwarded** to the `right` digit.
	 * Because this has no any meaning for time durations.
	 *
	 * Positive overflow will be occurred
	 * when the addition result produces a duration value that greater than or equal to the [max] limit.
	 *
	 *
	 * @param other TimeDuration
	 */
	operator fun plusAssign(other: TimeDuration) {
		if (other.unit != unit) throw IllegalArgumentException("Units must be same to be able to be operated : $unit and ${other.unit}")
		value.plusAssign(other.value)
	}
	
	operator fun plusAssign(other: Int) {
		value.plusAssign(other)
	}
	
	operator fun minusAssign(other: TimeDuration) {
		value.minusAssign(other.value)
	}
	
	operator fun minusAssign(other: Int) {
		value.minusAssign(other)
	}
	
	override fun toString(): String = "$value $unit"
	
	companion object {
		
		fun of(value: Int, unit: TimeUnit) = TimeDuration(value, unit)
		infix fun milliseconds(@IntRange(from = 0, to = 999) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MILLISECOND)
		infix fun seconds(@IntRange(from = 0, to = 59) value: Int): TimeDuration = TimeDuration(value, TimeUnit.SECOND)
		infix fun minutes(@IntRange(from = 0, to = 59) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MINUTE)
		infix fun hours(@IntRange(from = 0, to = 23) value: Int): TimeDuration = TimeDuration(value, TimeUnit.HOUR)
		infix fun days(@IntRange(from = 0, to = 30) value: Int): TimeDuration = TimeDuration(value, TimeUnit.DAY)
		infix fun months(@IntRange(from = 0, to = 11) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MONTH)
		infix fun years(@IntRange(from = 0, to = 999_999_999) value: Int): TimeDuration = TimeDuration(value, TimeUnit.YEAR)
	}
	
}

fun main() {
	val timeDuration = TimeDuration minutes 55
	println("$timeDuration")
	// val otherDuration = TimeDuration minutes 10
	// val hour = TimeDuration hours 0
	// timeDuration.value.left = hour.value
	// timeDuration += otherDuration
	
	
}
