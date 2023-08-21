@file:JvmName("Durations")

package tr.xyz.timek

import tr.xyz.digit.Digit
import tr.xyz.timek.unit.TimeUnit
import java.util.*

/**
 * Represents a duration.
 * Duration is an amount of time with certain unit (year, month, day, hour, minute, second, millisecond).
 * And this duration --is not-- a time duration.
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
	val asMilliseconds: Long
		get() {
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
	
	val asSeconds: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 1000
				TimeUnit.SECOND      -> value
				TimeUnit.MINUTE      -> value * 60
				TimeUnit.HOUR        -> value * 60 * 60
				TimeUnit.DAY         -> value * 24 * 60 * 60
				TimeUnit.MONTH       -> value * 30 * 24 * 60 * 60
				TimeUnit.YEAR        -> value * 365 * 24 * 60 * 60
			}
		}
	
	val asMinutes: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 60000
				TimeUnit.SECOND      -> value / 60
				TimeUnit.MINUTE      -> value
				TimeUnit.HOUR        -> value * 60
				TimeUnit.DAY         -> value * 24 * 60
				TimeUnit.MONTH       -> value * 30 * 24 * 60
				TimeUnit.YEAR        -> value * 365 * 24 * 60
			}
		}
	
	val asHours: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 3600000
				TimeUnit.SECOND      -> value / 3600
				TimeUnit.MINUTE      -> value / 60
				TimeUnit.HOUR        -> value
				TimeUnit.DAY         -> value * 24
				TimeUnit.MONTH       -> value * 30 * 24
				TimeUnit.YEAR        -> value * 365 * 24
			}
		}
	
	val asDays: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 86400000
				TimeUnit.SECOND      -> value / 86400
				TimeUnit.MINUTE      -> value / 1440
				TimeUnit.HOUR        -> value / 24
				TimeUnit.DAY         -> value
				TimeUnit.MONTH       -> value * 30
				TimeUnit.YEAR        -> value * 365
			}
		}
	
	val asMonths: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 2592000000
				TimeUnit.SECOND      -> value / 2592000
				TimeUnit.MINUTE      -> value / 43200
				TimeUnit.HOUR        -> value / 720
				TimeUnit.DAY         -> value / 30
				TimeUnit.MONTH       -> value
				TimeUnit.YEAR        -> value * 12
			}
		}
	
	val asYears: Long
		get() {
			return when (unit) {
				TimeUnit.MILLISECOND -> value / 31536000000
				TimeUnit.SECOND      -> value / 31536000
				TimeUnit.MINUTE      -> value / 525960
				TimeUnit.HOUR        -> value / 8760
				TimeUnit.DAY         -> value / 365
				TimeUnit.MONTH       -> value / 12
				TimeUnit.YEAR        -> value
			}
		}
	
	fun asUnit(unit: TimeUnit): Duration {
		return when (unit) {
			TimeUnit.MILLISECOND -> Duration milliseconds asMilliseconds
			TimeUnit.SECOND      -> Duration seconds asSeconds
			TimeUnit.MINUTE      -> Duration minutes asMinutes
			TimeUnit.HOUR        -> Duration hours asHours
			TimeUnit.DAY         -> Duration days asDays
			TimeUnit.MONTH       -> Duration months asMonths
			TimeUnit.YEAR        -> Duration years asYears
		}
	}
	
	infix fun to(unit: TimeUnit): Duration = asUnit(unit)
	
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




































