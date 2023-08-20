@file:JvmName("Durations")

package tr.xyz.timek

import tr.xyz.digit.Digit
import java.util.*

/**
 * Represents a duration.
 * Duration is an amount of time with certain unit (year, month, day, hour, minute, second, millisecond).
 * And this duration is a time duration.
 * So, the time units have a limit.
 * For example, maximum value of an hour is 23,
 * maximum value of a `minute` is 59 etc.
 *
 *
 * @property durationValue [Digit] of the duration.
 * @property unit unit of the duration
 * @constructor Creates a new duration
 * @see TimeUnit
 */
sealed class Duration(val durationValue: Digit, val unit: TimeUnit) {
	/**
	 *  `true` if the duration is zero
	 */
	val isZero: Boolean get() = durationValue.digitValue == 0
	/**
	 *  `true` if the duration is not zero
	 */
	val isNotZero: Boolean get() = durationValue.digitValue != 0

	fun toMillis(): Long {
		return when (this) {
			is Millis -> durationValue.digitValue.toLong()
			is Second -> durationValue.digitValue.toLong() * 1000
			is Minute -> durationValue.digitValue.toLong() * 60 * 1000
			is Hour   -> durationValue.digitValue.toLong() * 60 * 60 * 1000
			is Day    -> durationValue.digitValue.toLong() * 24 * 60 * 60 * 1000
			is Month  -> durationValue.digitValue.toLong() * 30 * 24 * 60 * 60 * 1000
			is Year   -> durationValue.digitValue.toLong() * 365 * 24 * 60 * 60 * 1000
		}
	}

	operator fun plus(duration: Int): Duration {
		return when (this) {
			is Millis -> Millis(duration + durationValue.digitValue)
			is Second -> Second(duration + durationValue.digitValue)
			is Minute -> Minute(duration + durationValue.digitValue)
			is Hour   -> Hour(duration + durationValue.digitValue)
			is Day    -> Day(duration + durationValue.digitValue)
			is Month  -> Month(duration + durationValue.digitValue)
			is Year   -> Year(duration + durationValue.digitValue)
		}
	}

	operator fun plus(duration: Duration): Duration {
		return when (this) {
			is Millis -> Millis(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Second -> Second(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Minute -> Minute(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Hour   -> Hour(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Day    -> Day(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Month  -> Month(duration.durationValue.digitValue + duration.durationValue.digitValue)
			is Year   -> Year(duration.durationValue.digitValue + duration.durationValue.digitValue)
		}
	}

	operator fun minus(duration: Duration): Duration {
		return when (this) {
			is Millis -> Millis(durationValue.digitValue - duration.durationValue.digitValue)
			is Second -> Second(durationValue.digitValue - duration.durationValue.digitValue)
			is Minute -> Minute(durationValue.digitValue - duration.durationValue.digitValue)
			is Hour   -> Hour(durationValue.digitValue - duration.durationValue.digitValue)
			is Day    -> Day(durationValue.digitValue - duration.durationValue.digitValue)
			is Month  -> Month(durationValue.digitValue - duration.durationValue.digitValue)
			is Year   -> Year(durationValue.digitValue - duration.durationValue.digitValue)
		}
	}

	operator fun minus(duration: Int): Duration {
		return when (this) {
			is Millis -> Millis(durationValue.digitValue - duration)
			is Second -> Second(durationValue.digitValue - duration)
			is Minute -> Minute(durationValue.digitValue - duration)
			is Hour   -> Hour(durationValue.digitValue - duration)
			is Day    -> Day(durationValue.digitValue - duration)
			is Month  -> Month(durationValue.digitValue - duration)
			is Year   -> Year(durationValue.digitValue - duration)
		}
	}

	override fun toString() = "${durationValue.digitValue}"

	override fun equals(other: Any?): Boolean = other is Duration && durationValue.digitValue == other.durationValue.digitValue && unit == other.unit
	override fun hashCode() = Objects.hash(durationValue.digitValue, unit)

	companion object {
		infix fun milliseconds(value: Int): Duration = Millis(value)
		infix fun seconds(value: Int): Duration = Second(value)
		infix fun minutes(value: Int): Duration = Minute(value)
		infix fun hours(value: Int): Duration = Hour(value)
		infix fun days(value: Int): Duration = Day(value)
		infix fun months(value: Int): Duration = Month(value)
		infix fun years(value: Int): Duration = Year(value)
	}
}

class Year(value: Int = 0) : Duration(Digit.newDigit(value = value), TimeUnit.YEAR)
class Month(value: Int = 0) : Duration(Digit.newDigit(0, 12, value), TimeUnit.MONTH)
class Day(value: Int = 0) : Duration(Digit.newDigit(0, 30, value), TimeUnit.DAY)
class Hour(value: Int = 0) : Duration(Digit.newDigit(0, 24, value), TimeUnit.HOUR)
class Minute(value: Int = 0) : Duration(Digit.newDigit(0, 60, value), TimeUnit.MINUTE)
class Second(value: Int = 0) : Duration(Digit.newDigit(0, 60, value), TimeUnit.SECOND)
class Millis(value: Int = 0) : Duration(Digit.newDigit(0, 1000, value), TimeUnit.MILLISECOND)