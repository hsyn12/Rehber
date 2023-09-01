package tr.xyz.timek.duration

import androidx.annotation.IntRange
import tr.xyz.digit.Digit
import tr.xyz.timek.TimeMillis

/**
 * Provides to define a duration of time with one unit (year, month, day, hour, minute, second,
 * millisecond).
 *
 * Each unit has its own limit. For example, the duration of a month is exactly `30` days, an hour
 * is exactly `60` minutes, and a minute is exactly `60` seconds etc.
 *
 * ```
 *
 *    val timeDuration = TimeDuration minutes 55
 *    println("$timeDuration")
 *    // 55 minutes
 * ```
 *
 * @constructor Creates a new time duration with value and its unit.
 * @property unit [TimeUnit] of the duration
 * @property value value of the duration
 * @property isNotZero indicates if the duration is not zero
 * @property isZero indicates if the duration is zero
 * @property toMilliseconds milliseconds equivalent of the duration
 * @see TimeUnit
 * @see Digit
 * @see TimeMillis
 */
class TimeDuration(value: Int, val unit: TimeUnit) {
	
	/**
	 * [Digit] object that represents the duration
	 */
	var value: Digit = Digit.newDigit(0, Limits.maxOf(unit))
	
	/**
	 * Indicates if the duration is not zero
	 */
	val isNotZero: Boolean get() = value.digitValue != 0
	
	/**
	 * Indicates if the duration is zero
	 */
	val isZero: Boolean get() = value.digitValue == 0
	
	init {
		
		this.value.digitValue = value
	}
	
	/**
	 * Returns the milliseconds equivalent of the duration
	 */
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
	
	private fun checkUnit(unit: TimeUnit) {
		if (isNotUnit(unit)) throw IllegalArgumentException("Units must be same to be able to be operated : $unit and ${this.unit}")
	}
	
	/**
	 * Checks if the unit is the same
	 */
	fun isUnit(unit: TimeUnit) = unit == this.unit
	
	/**
	 * Checks if the unit is different
	 */
	fun isNotUnit(unit: TimeUnit) = unit != this.unit
	
	/**
	 * Increments the duration
	 */
	operator fun inc(): TimeDuration {
		value++
		return this
	}
	
	/**
	 * Decrements the duration
	 */
	operator fun dec(): TimeDuration {
		value--
		return this
	}
	
	/**
	 * Returns a new [TimeDuration] with the value added.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be
	 * thrown. And after the addition, the result duration will have the same unit as this. Moreover,
	 * if overflow occurs, the result will be truncated.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 55
	 *    val otherDuration = TimeDuration minutes 10
	 *
	 *    println(timeDuration + otherDuration) // 5 minutes
	 * ```
	 *
	 * Remember, [TimeDuration] always has a limit and certainly cannot exceed the limit. This method
	 * is for the time durations that never cause overflow. If you need to add durations that can
	 * cause overflow, use [plusAssign] and set the [Digit.left] to observe the positive overflow or
	 * set the [Digit.right] to observe the negative overflow. Or set the both. Because of the not
	 * returning a new [TimeDuration], the overflow will be observable. While [TimeDuration] uses the
	 * [Digit] to represent the value, the all is done with that.
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
		checkUnit(other.unit)
		return TimeDuration(value.digitValue + other.value.digitValue, unit)
	}
	
	/**
	 * Returns a new [TimeDuration] with the value subtracted. This operation will not be caused to
	 * change the value of this duration.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be
	 * thrown. And after the addition, the result duration will have the same unit as this. Moreover,
	 * if overflow occurs, the result will be truncated.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 5
	 *    val otherDuration = TimeDuration minutes 10
	 *    println(timeDuration - otherDuration) // 55 minutes
	 * ```
	 *
	 * Remember, [TimeDuration] always has a limit and certainly cannot exceed the limit. This method
	 * is for the time durations that never cause overflow. If you need to add durations that can
	 * cause overflow, use [minusAssign] and set the [Digit.left] to observe the positive overflow or
	 * set the [Digit.right] to observe the negative overflow. Or set the both. Because of the not
	 * returning a new [TimeDuration], the overflow will be observable. While [TimeDuration] uses the
	 * [Digit] to represent the value, the all is done with that.
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
	 *
	 * @param other [TimeDuration] to subtract
	 * @return new [TimeDuration]
	 */
	operator fun minus(other: TimeDuration): TimeDuration {
		checkUnit(other.unit)
		return TimeDuration(value.digitValue - other.value.digitValue, unit)
	}
	
	/**
	 * Adds another [TimeDuration] to this duration. This operation will be caused to change the
	 * value of this duration.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be
	 * thrown. And after the addition, the result duration will have the same unit as this. Moreover,
	 * if overflow occurs, the result will be truncated and the (positive) overflow count will be
	 * forwarded to the `left` digit of this digit (if its exists).
	 *
	 * ```
	 *
	 *    val timeDuration        = TimeDuration minutes 55
	 *    val otherDuration       = TimeDuration minutes 10
	 *    val hour                = TimeDuration hours 0
	 *    timeDuration.value.left = hour.value
	 *
	 *    timeDuration += otherDuration
	 *    println("$hour $timeDuration")
	 *    // 1 hours 5 minutes
	 * ```
	 *
	 * Although the hour object has a value of zero, it takes the value `1` after the operation. This
	 * is the `cycle count`. Positive overflow will be forwarded to the `left` digit. But negative
	 * overflow **will not be forwarded** to the `right` digit. Because this has no any meaning for
	 * time durations.
	 *
	 * Positive overflow will be occurred when the addition produces a duration value that greater
	 * than or equal to the [max] limit.
	 *
	 * @param other TimeDuration
	 */
	operator fun plusAssign(other: TimeDuration) {
		checkUnit(other.unit)
		value.plusAssign(other.value)
	}
	
	/**
	 * Subtracts another [TimeDuration] from this duration. This operation will be caused to change
	 * the value of this duration.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be
	 * thrown. And after the addition, if overflow occurs, the result will be truncated and the
	 * (positive) overflow count will be forwarded to the `left` digit of this digit (if its exists).
	 *
	 * Positive overflow will be occurred when the addition produces a duration value that greater
	 * than or equal to the `max` limit.
	 *
	 * ```
	 *
	 *    val timeDuration = TimeDuration minutes 55
	 *    val otherDuration = 10
	 *    val hour = TimeDuration hours 0
	 *    timeDuration.value.left = hour.value
	 *    timeDuration += otherDuration
	 *    println("$hour $timeDuration")
	 *    // 1 hours 5 minutes
	 * ```
	 *
	 * @param other value to subtract
	 * @see Limits
	 */
	operator fun plusAssign(other: Int) {
		value.plusAssign(other)
	}
	
	/**
	 * Subtracts another [TimeDuration] from this duration. This operation will be caused to change
	 * the value of this duration.
	 *
	 * Caution : [other] duration must have the same unit as this, otherwise an exception will be
	 * thrown. And after the subtraction, if overflow occurs, the result will be truncated and
	 * the (negative) overflow count will be forwarded to the `right` digit of this digit (if its
	 * exists).
	 *
	 * @param other TimeDuration
	 */
	operator fun minusAssign(other: TimeDuration) {
		value.minusAssign(other.value)
	}
	
	/**
	 * Subtracts another [Int] from this duration. This operation will be caused to change the value
	 * of this duration. And after the subtraction, if overflow occurs, the result will be truncated
	 * and the (negative) overflow count will be forwarded to the `right` digit of this digit (if its
	 * exists).
	 *
	 * @param other Int
	 */
	operator fun minusAssign(other: Int) {
		value.minusAssign(other)
	}
	
	/**
	 * Returns a string representation of the duration with the value and init in form of `value
	 * unit` (for example `1 hours`).
	 */
	override fun toString(): String = "$value $unit"
	
	infix fun with(tDuration: TimeDuration): TimeDurations = TimeDurations(this, tDuration)
	
	companion object {
		/**
		 * Returns a new [TimeDuration] with the value and unit.
		 */
		fun of(value: Int, unit: TimeUnit) = TimeDuration(value, unit)
		
		/**
		 * Returns a new [TimeDuration] with the value in milliseconds.
		 */
		infix fun milliseconds(@IntRange(from = 0, to = 999) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MILLISECOND)
		
		/**
		 * Returns a new [TimeDuration] with the value in seconds.
		 */
		infix fun seconds(@IntRange(from = 0, to = 59) value: Int): TimeDuration = TimeDuration(value, TimeUnit.SECOND)
		
		/**
		 * Returns a new [TimeDuration] with the value in minutes.
		 */
		infix fun minutes(@IntRange(from = 0, to = 59) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MINUTE)
		
		/**
		 * Returns a new [TimeDuration] with the value in hours.
		 */
		infix fun hours(@IntRange(from = 0, to = 23) value: Int): TimeDuration = TimeDuration(value, TimeUnit.HOUR)
		
		/**
		 * Returns a new [TimeDuration] with the value in days.
		 */
		infix fun days(@IntRange(from = 0, to = 29) value: Int): TimeDuration = TimeDuration(value, TimeUnit.DAY)
		
		/**
		 * Returns a new [TimeDuration] with the value in months.
		 */
		infix fun months(@IntRange(from = 0, to = 11) value: Int): TimeDuration = TimeDuration(value, TimeUnit.MONTH)
		
		/**
		 * Returns a new [TimeDuration] with the value in years.
		 */
		infix fun years(@IntRange(from = 0, to = 999_999_999) value: Int): TimeDuration = TimeDuration(value, TimeUnit.YEAR)
	}
	
}

fun main() {
	val timeDuration = TimeDuration hours 0
	println(timeDuration)
}