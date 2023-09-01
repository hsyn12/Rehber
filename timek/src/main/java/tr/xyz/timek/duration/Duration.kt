package tr.xyz.timek.duration

import java.util.*
import kotlin.reflect.KProperty

/**
 * Represents a duration.
 *
 * Duration is an amount of time with certain unit (year, month, day, hour, minute, second,
 * millisecond). And this duration **is not** a time duration. So, the time units have no limit. For
 * example, can be defined a duration of a month with value `2_000`.
 *
 * ```
 *
 *    val _duration = Duration months 2000
 *    println(_duration) // 2000 months
 * ```
 *
 * And a duration can be converted to any unit.
 *
 * ```
 *
 *    val _duration = Duration months 2000 to TimeUnit.YEAR
 *    println(_duration) // 166 years
 *    println(_duration to TimeUnit.DAY) // 60590 days
 * ```
 *
 * If a duration is converted to bigger unit, it will be truncated.
 *
 * @constructor Creates a new duration
 * @property value value of the duration.
 * @property unit unit of the duration
 * @see TimeUnit
 */
class Duration(val value: Long, val unit: TimeUnit) {
	
	// region Properties
	/**
	 * `true` if the duration is zero
	 */
	fun isZero(): Boolean = value == 0L
	
	/**
	 * `true` if the duration is not zero
	 */
	fun isNotZero(): Boolean = value != 0L
	
	/**
	 * Millisecond equivalent of the current duration
	 */
	val asMilliseconds: Long by ValueConvertor(TimeUnit.MILLISECOND)
	
	/**
	 * Seconds equivalent of the current duration
	 */
	val asSeconds: Long by ValueConvertor(TimeUnit.SECOND)
	
	/**
	 * Minutes equivalent of the current duration
	 */
	val asMinutes: Long by ValueConvertor(TimeUnit.MINUTE)
	
	/**
	 * Hours equivalent of the current duration
	 */
	val asHours: Long by ValueConvertor(TimeUnit.HOUR)
	
	/**
	 * Days equivalent of the current duration
	 */
	val asDays: Long by ValueConvertor(TimeUnit.DAY)
	
	/**
	 * Months equivalent of the current duration
	 */
	val asMonths: Long by ValueConvertor(TimeUnit.MONTH)
	
	/**
	 * Years equivalent of the current duration
	 */
	val asYears: Long by ValueConvertor(TimeUnit.YEAR)
	
	// endregion
	
	// region private class ValueConvertor(private val unit: TimeUnit) {
	/**
	 * A utility class for converting durations between different time units.
	 *
	 * @property unit The target time unit for conversion.
	 */
	private class ValueConvertor(private val unit: TimeUnit) {
		
		operator fun getValue(ref: Duration, property: KProperty<*>): Long = when (unit) {
			
			TimeUnit.MILLISECOND -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value
				TimeUnit.SECOND      -> ref.value * 1000
				TimeUnit.MINUTE      -> ref.value * 1000 * 60
				TimeUnit.HOUR        -> ref.value * 1000 * 60 * 60
				TimeUnit.DAY         -> ref.value * 1000 * 60 * 60 * 24
				TimeUnit.MONTH       -> ref.value * 1000 * 60 * 60 * 24 * 30
				TimeUnit.YEAR        -> ref.value * 1000 * 60 * 60 * 24 * 365
			}
			
			TimeUnit.SECOND      -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 1000
				TimeUnit.SECOND      -> ref.value
				TimeUnit.MINUTE      -> ref.value * 60
				TimeUnit.HOUR        -> ref.value * 60 * 60
				TimeUnit.DAY         -> ref.value * 60 * 60 * 24
				TimeUnit.MONTH       -> ref.value * 60 * 60 * 24 * 30
				TimeUnit.YEAR        -> ref.value * 60 * 60 * 24 * 365
			}
			
			TimeUnit.MINUTE      -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 60000
				TimeUnit.SECOND      -> ref.value / 60
				TimeUnit.MINUTE      -> ref.value
				TimeUnit.HOUR        -> ref.value * 60
				TimeUnit.DAY         -> ref.value * 60 * 24
				TimeUnit.MONTH       -> ref.value * 60 * 24 * 30
				TimeUnit.YEAR        -> ref.value * 60 * 24 * 365
			}
			
			TimeUnit.HOUR        -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 3600000
				TimeUnit.SECOND      -> ref.value / 3600
				TimeUnit.MINUTE      -> ref.value / 60
				TimeUnit.HOUR        -> ref.value
				TimeUnit.DAY         -> ref.value * 24
				TimeUnit.MONTH       -> ref.value * 24 * 30
				TimeUnit.YEAR        -> ref.value * 24 * 365
			}
			
			TimeUnit.DAY         -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 86400000
				TimeUnit.SECOND      -> ref.value / 86400
				TimeUnit.MINUTE      -> ref.value / 1440
				TimeUnit.HOUR        -> ref.value / 24
				TimeUnit.DAY         -> ref.value
				TimeUnit.MONTH       -> ref.value * 30
				TimeUnit.YEAR        -> ref.value * 365
			}
			
			TimeUnit.MONTH       -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 2592000000
				TimeUnit.SECOND      -> ref.value / 2592000
				TimeUnit.MINUTE      -> ref.value / 43200
				TimeUnit.HOUR        -> ref.value / 720
				TimeUnit.DAY         -> ref.value / 30
				TimeUnit.MONTH       -> ref.value
				TimeUnit.YEAR        -> ref.value * 12
			}
			
			TimeUnit.YEAR        -> when (ref.unit) {
				TimeUnit.MILLISECOND -> ref.value / 31536000000
				TimeUnit.SECOND      -> ref.value / 31536000
				TimeUnit.MINUTE      -> ref.value / 525600
				TimeUnit.HOUR        -> ref.value / 8760
				TimeUnit.DAY         -> ref.value / 365
				TimeUnit.MONTH       -> ref.value / 12
				TimeUnit.YEAR        -> ref.value
			}
		}
	}
	// endregion
	
	// region Member functions
	/**
	 * Converts the duration to the given unit.
	 *
	 * @param unit [TimeUnit] convert to
	 * @return new [Duration] with the specified unit. If unit is bigger than the current unit, it
	 *     will be truncated
	 */
	infix fun to(unit: TimeUnit): Duration {
		return when (unit) {
			TimeUnit.MILLISECOND -> asMilliseconds milliseconds dDuration
			TimeUnit.SECOND      -> asSeconds seconds dDuration
			TimeUnit.MINUTE      -> asMinutes minutes dDuration
			TimeUnit.HOUR        -> asHours hours dDuration
			TimeUnit.DAY         -> asDays days dDuration
			TimeUnit.MONTH       -> asMonths months dDuration
			TimeUnit.YEAR        -> asYears years dDuration
		}
	}
	/**
	 * Returns a new [Duration] with the value and the same current unit.
	 *
	 * @param value The value of the new duration
	 * @return A new [Duration] object with the value and the same current unit
	 */
	fun newDuration(value: Long) = Duration(value, unit)
	// endregion
	
	// region Operator functions
	/**
	 * Adds the given [duration] to the current duration and returns a new [Duration] object.
	 *
	 * @param duration The duration to be added.
	 * @return A new [Duration] object representing the sum of the current duration and the given
	 *     duration.
	 */
	operator fun plus(duration: Long): Duration = newDuration(value + duration)
	/**
	 * Adds the given [duration] to the current duration and returns a new [Duration] object.
	 *
	 * @param duration The duration to be added.
	 * @return A new [Duration] object representing the sum of the current duration and the given
	 *     duration. If the given duration unit is different than the current duration unit, it will
	 *     be converted to the current duration unit and if it is bigger than the current unit, it
	 *     will be truncated.
	 */
	operator fun plus(duration: Duration): Duration = newDuration(value + (duration to unit).value)
	/**
	 * Subtracts the given [duration] to the current duration and returns a new [Duration] object.
	 *
	 * @param duration The duration to be subtracted.
	 * @return A new [Duration] object representing the minus of the current duration and the given
	 *     duration. If the given duration unit is different than the current duration unit, it will
	 *     be converted to the current duration unit and if it is bigger than the current unit, it
	 *     will be truncated.
	 */
	operator fun minus(duration: Duration): Duration = newDuration(value - (duration to unit).value)
	/**
	 * Subtracts the given [duration] from the current duration and returns a new [Duration] with the
	 * result.
	 *
	 * @param duration The duration to be subtracted
	 * @return A new [Duration] object representing the minus of the current duration and the given
	 *     duration.
	 */
	operator fun minus(duration: Long): Duration = newDuration(value - duration)
	// endregion
	
	// region Overrides functions 
	override fun toString() = "$value $unit"
	override fun equals(other: Any?): Boolean = other is Duration && value == other.value && unit == other.unit
	override fun hashCode() = Objects.hash(value, unit)
	// endregion
	
	// region Companion functions
	companion object {
		/**
		 * Returns a new [Duration] with the value and unit.
		 *
		 * @param value value
		 * @param unit [TimeUnit]
		 * @return new [Duration]
		 */
		fun of(value: Long, unit: TimeUnit) = Duration(value, unit)
		/**
		 * Returns a new [Duration] with the value in milliseconds.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun milliseconds(value: Long): Duration = Duration(value, TimeUnit.MILLISECOND)
		/**
		 * Returns a new [Duration] with the value in seconds.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun seconds(value: Long): Duration = Duration(value, TimeUnit.SECOND)
		/**
		 * Returns a new [Duration] with the value in minutes.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun minutes(value: Long): Duration = Duration(value, TimeUnit.MINUTE)
		/**
		 * Returns a new [Duration] with the value in hours.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun hours(value: Long): Duration = Duration(value, TimeUnit.HOUR)
		/**
		 * Returns a new [Duration] with the value in days.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun days(value: Long): Duration = Duration(value, TimeUnit.DAY)
		/**
		 * Returns a new [Duration] with the value in months.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun months(value: Long): Duration = Duration(value, TimeUnit.MONTH)
		/**
		 * Returns a new [Duration] with the value in years.
		 *
		 * @param value value
		 * @return new [Duration]
		 */
		infix fun years(value: Long): Duration = Duration(value, TimeUnit.YEAR)
	}
	// endregion
}




































