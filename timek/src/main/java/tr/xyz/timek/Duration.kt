@file:JvmName("Durations")

package tr.xyz.timek

import tr.xyz.digit.Digit
import java.util.*

sealed class Duration(val durationValue: Digit, val unit: TimeUnit) {

	val isZero get() = durationValue.digitValue == 0
	val isNotZero get() = durationValue.digitValue != 0

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
		fun millisecond(value: Int): Duration = Millis(value)
		fun second(value: Int): Duration = Second(value)
		fun minute(value: Int): Duration = Minute(value)
		fun hour(value: Int): Duration = Hour(value)
		fun day(value: Int): Duration = Day(value)
		fun month(value: Int): Duration = Month(value)
		fun year(value: Int): Duration = Year(value)
	}
}

class Year(value: Int = 0) : Duration(Digit.newDigit(value = value), TimeUnit.YEAR)
class Month(value: Int = 0) : Duration(Digit.newDigit(0, 12, value), TimeUnit.MONTH)
class Day(value: Int = 0) : Duration(Digit.newDigit(0, 30, value), TimeUnit.DAY)
class Hour(value: Int = 0) : Duration(Digit.newDigit(0, 24, value), TimeUnit.HOUR)
class Minute(value: Int = 0) : Duration(Digit.newDigit(0, 60, value), TimeUnit.MINUTE)
class Second(value: Int = 0) : Duration(Digit.newDigit(0, 60, value), TimeUnit.SECOND)
class Millis(value: Int = 0) : Duration(Digit.newDigit(0, 1000, value), TimeUnit.MILLISECOND)