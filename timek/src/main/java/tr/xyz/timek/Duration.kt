@file:JvmName("Durations")

package tr.xyz.timek

import tr.xyz.digit.Digit
import tr.xyz.timek.unit.TimeUnit
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
 * @property value [Digit] of the duration.
 * @property unit unit of the duration
 * @constructor Creates a new duration
 * @see TimeUnit
 */
class Duration(val value: Long, val unit: TimeUnit) {
	/**
	 *  `true` if the duration is zero
	 */
	val isZero: Boolean get() = value == 0L
	/**
	 *  `true` if the duration is not zero
	 */
	val isNotZero: Boolean get() = value != 0L
	
	/**
	 * Converts the duration to milliseconds
	 *
	 * @return milliseconds value of the duration
	 */
	fun toMillis(): Long {
		return when (unit) {
			TimeUnit.MILLISECOND -> value
			TimeUnit.SECOND      -> value * 1000
			TimeUnit.MINUTE      -> value * 60 * 1000
			TimeUnit.HOUR        -> value * 60 * 60 * 1000
			TimeUnit.DAY         -> value * 24 * 60 * 60 * 1000
			TimeUnit.MONTH       -> value * 30 * 24 * 60 * 60 * 1000
			TimeUnit.YEAR        -> value * 365 * 24 * 60 * 60 * 1000
		}
	}
	
	infix fun to(unit: TimeUnit): Duration = when (unit) {
		TimeUnit.MILLISECOND -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration milliseconds value
			TimeUnit.SECOND      -> Duration milliseconds (value * 1000)
			TimeUnit.MINUTE      -> Duration milliseconds (value * 60 * 1000)
			TimeUnit.HOUR        -> Duration milliseconds (value * 60 * 60 * 1000)
			TimeUnit.DAY         -> Duration milliseconds (value * 24 * 60 * 60 * 1000)
			TimeUnit.MONTH       -> Duration milliseconds (value * 30 * 24 * 60 * 60 * 1000)
			TimeUnit.YEAR        -> Duration milliseconds (value * 365 * 24 * 60 * 60 * 1000)
		}
		
		TimeUnit.SECOND      -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration seconds (value / 1000)
			TimeUnit.SECOND      -> Duration seconds value
			TimeUnit.MINUTE      -> Duration seconds (value * 60)
			TimeUnit.HOUR        -> Duration seconds (value * 60 * 60)
			TimeUnit.DAY         -> Duration seconds (value * 24 * 60 * 60)
			TimeUnit.MONTH       -> Duration seconds (value * 30 * 24 * 60 * 60)
			TimeUnit.YEAR        -> Duration seconds (value * 365 * 24 * 60 * 60)
		}
		
		TimeUnit.MINUTE      -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration minutes (value / 1000)
			TimeUnit.SECOND      -> Duration minutes (value / 60)
			TimeUnit.MINUTE      -> Duration minutes value
			TimeUnit.HOUR        -> Duration minutes (value * 60)
			TimeUnit.DAY         -> Duration minutes (value * 24 * 60)
			TimeUnit.MONTH       -> Duration minutes (value * 30 * 24 * 60)
			TimeUnit.YEAR        -> Duration minutes (value * 365 * 24 * 60)
		}
		
		TimeUnit.HOUR        -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration hours (value / 1000)
			TimeUnit.SECOND      -> Duration hours (value / 60 / 60)
			TimeUnit.MINUTE      -> Duration hours (value / 60)
			TimeUnit.HOUR        -> Duration hours value
			TimeUnit.DAY         -> Duration hours (value * 24)
			TimeUnit.MONTH       -> Duration hours (value * 30 * 24)
			TimeUnit.YEAR        -> Duration hours (value * 365 * 24)
		}
		
		TimeUnit.DAY         -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration days (value / 1000 / 60 / 60 / 24)
			TimeUnit.SECOND      -> Duration days (value / 60 / 60 / 24)
			TimeUnit.MINUTE      -> Duration days (value / 60 / 24)
			TimeUnit.HOUR        -> Duration days (value / 24)
			TimeUnit.DAY         -> Duration days value
			TimeUnit.MONTH       -> Duration days (value * 30)
			TimeUnit.YEAR        -> Duration days (value * 365)
		}
		
		TimeUnit.MONTH       -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration months (value / 1000 / 60 / 60 / 24 / 30)
			TimeUnit.SECOND      -> Duration months (value / 60 / 60 / 24 / 30)
			TimeUnit.MINUTE      -> Duration months (value / 60 / 24 / 30)
			TimeUnit.HOUR        -> Duration months (value / 24 / 30)
			TimeUnit.DAY         -> Duration months (value / 30)
			TimeUnit.MONTH       -> Duration months value
			TimeUnit.YEAR        -> Duration months (value * 12)
		}
		
		TimeUnit.YEAR        -> when (this.unit) {
			TimeUnit.MILLISECOND -> Duration years (value / 1000 / 60 / 60 / 24 / 30 / 12)
			TimeUnit.SECOND      -> Duration years (value / 60 / 60 / 24 / 30 / 12)
			TimeUnit.MINUTE      -> Duration years (value / 60 / 24 / 30 / 12)
			TimeUnit.HOUR        -> Duration years (value / 24 / 30 / 12)
			TimeUnit.DAY         -> Duration years (value / 30 / 12)
			TimeUnit.MONTH       -> Duration years (value / 12)
			TimeUnit.YEAR        -> Duration years value
		}
	}
	
	operator fun plus(duration: Long): Duration = newDuration(value + duration)
	
	operator fun plus(duration: Duration): Duration = if (unit == duration.unit) newDuration(value + duration.value) else newDuration(value + (duration to unit).value)
	
	operator fun minus(duration: Duration): Duration = if (unit == duration.unit) newDuration(value - duration.value) else newDuration(value - (duration to unit).value)
	
	operator fun minus(duration: Long): Duration = newDuration(value - duration)
	
	override fun toString() = "$value $unit"
	
	override fun equals(other: Any?): Boolean = other is Duration && value == other.value && unit == other.unit
	override fun hashCode() = Objects.hash(value, unit)
	fun newDuration(value: Long) = Duration(value, unit)
	
	companion object {
		
		fun newDuration(value: Long, unit: TimeUnit) = Duration(value, unit)
		infix fun milliseconds(value: Long): Duration = Duration(value, TimeUnit.MILLISECOND)
		infix fun seconds(value: Long): Duration = Duration(value, TimeUnit.SECOND)
		infix fun minutes(value: Long): Duration = Duration(value, TimeUnit.MINUTE)
		infix fun hours(value: Long): Duration = Duration(value, TimeUnit.HOUR)
		infix fun days(value: Long): Duration = Duration(value, TimeUnit.DAY)
		infix fun months(value: Long): Duration = Duration(value, TimeUnit.MONTH)
		infix fun years(value: Long): Duration = Duration(value, TimeUnit.YEAR)
	}
}

fun main() {
	
	val duration = Duration minutes 55 to TimeUnit.SECOND
	println(duration)
	
}




































