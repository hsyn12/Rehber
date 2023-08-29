@file:JvmName("TimedDurations")

package tr.xyz.timek

import androidx.annotation.IntRange
import tr.xyz.timek.unit.Limits
import tr.xyz.timek.unit.TimeDuration
import tr.xyz.timek.unit.TimeUnit
import kotlin.math.absoluteValue

/**
 * Represents a duration of time with units (year, month, day, hour, minute, second, millisecond).
 * The duration is calculated based on a given value in milliseconds.
 *
 * [TimeDurations] is a time duration, not a time point. And each unit has its own limit. For
 * example, the duration of a month is exactly 30 days, an hour is exactly 60 minutes, and a minute
 * is exactly 60 seconds, etc.
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
 * @constructor Creates a new time duration with optional value of zero.
 * @property value Number of milliseconds of the duration
 * @property durations List of calculated time durations
 * @property year Year part of the duration
 * @property month Month part of the duration
 * @property day Day part of the duration
 * @property hour Hour part of the duration
 * @property minute Minute part of the duration
 * @property second Second part of the duration
 * @property millisecond Millisecond part of the duration
 * @see TimeMillis
 * @see TimeDuration
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
	
	/**
	 * Creates a new time duration from a list of durations.
	 *
	 * @param durations List of durations
	 */
	constructor(vararg durations: Duration) : this(durations.sumOf {it.asMilliseconds})
	
	/**
	 * Creates a new time duration from a string that in form of '`23:59:59`'.
	 */
	constructor(value: String) : this(of(value))
	
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
	
	companion object {
		
		/**
		 * Checks the given value whether it is in the given unit range. If not, throws an
		 * [IllegalArgumentException].
		 *
		 * @param value duration value
		 * @param unit duration unit
		 */
		private fun validateTimeDuration(value: Int, unit: TimeUnit) {
			
			require(Limits.isInLimits(value, unit)) {"Invalid time duration. '${unit.toString().uppercase()}' must be in the range [${Limits.getRange(unit)}] but it was '$value'"}
		}
		
		/**
		 * Converts the duration string to milliseconds.
		 *
		 * ```
		 *
		 *    val timeDuration = TimeDurations("11:21:0:44:0")
		 *    println(timeDuration.toStringNonZero())
		 *    // 11 days 21 hours 44 seconds
		 * ```
		 *
		 * @param value Duration string in the format "`yyyy:mm:dd:hh:mm:ss:ms`". If not necessary to
		 *     use higher units, can be used "`mm:dd:hh:mm:ss`" or "`dd:hh:mm:ss`" or "`hh:mm:ss`" or
		 *     "`mm:ss`". If not necessary to use lower units, can be used zeros like that,
		 *     "`dd:0:mm:0`"
		 * @return milliseconds equivalent of the duration
		 */
		fun of(value: String): Long {
			
			val parts = value.split(":")
			
			parts.forEach {
				if (it.isEmpty()) throw IllegalArgumentException("Invalid time format: '$value'")
			}
			
			if (parts.size < 2 || parts.size > 7) throw IllegalArgumentException("Invalid time format: '$value'")
			
			return when (parts.size) {
				2    -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[1].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * 1000 + parts[1].toLong()
				}
				
				3    -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.MINUTE)
					validateTimeDuration(parts[1].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[2].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * TimeMillis.MINUTE + parts[1].toLong() * 1000 + parts[2].toLong()
				}
				
				4    -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.HOUR)
					validateTimeDuration(parts[1].toInt(), TimeUnit.MINUTE)
					validateTimeDuration(parts[2].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[3].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * TimeMillis.HOUR + parts[1].toLong() * TimeMillis.MINUTE + parts[2].toLong() * 1000 + parts[3].toLong()
				}
				
				5    -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.DAY)
					validateTimeDuration(parts[1].toInt(), TimeUnit.HOUR)
					validateTimeDuration(parts[2].toInt(), TimeUnit.MINUTE)
					validateTimeDuration(parts[3].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[4].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * TimeMillis.DAY + parts[1].toLong() * TimeMillis.HOUR + parts[2].toLong() * TimeMillis.MINUTE + parts[3].toLong() * 1000 + parts[4].toLong()
				}
				
				6    -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.MONTH)
					validateTimeDuration(parts[1].toInt(), TimeUnit.DAY)
					validateTimeDuration(parts[2].toInt(), TimeUnit.HOUR)
					validateTimeDuration(parts[3].toInt(), TimeUnit.MINUTE)
					validateTimeDuration(parts[4].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[5].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * TimeMillis.MONTH + parts[1].toLong() * TimeMillis.DAY + parts[2].toLong() * TimeMillis.HOUR + parts[3].toLong() * TimeMillis.MINUTE + parts[4].toLong() * 1000 + parts[5].toLong()
				}
				
				else -> {
					validateTimeDuration(parts[0].toInt(), TimeUnit.YEAR)
					validateTimeDuration(parts[1].toInt(), TimeUnit.MONTH)
					validateTimeDuration(parts[2].toInt(), TimeUnit.DAY)
					validateTimeDuration(parts[3].toInt(), TimeUnit.HOUR)
					validateTimeDuration(parts[4].toInt(), TimeUnit.MINUTE)
					validateTimeDuration(parts[5].toInt(), TimeUnit.SECOND)
					validateTimeDuration(parts[6].toInt(), TimeUnit.MILLISECOND)
					parts[0].toLong() * TimeMillis.YEAR + parts[1].toLong() * TimeMillis.MONTH + parts[2].toLong() * TimeMillis.DAY + parts[3].toLong() * TimeMillis.HOUR + parts[4].toLong() * TimeMillis.MINUTE + parts[5].toLong() * 1000 + parts[6].toLong()
				}
			}
		}
		
		/**
		 * Creates a new time duration builder.
		 *
		 * @return new [TimeDurationBuilder]
		 */
		fun builder(): TimeDurationBuilder = TimeDurationBuilder()
		
	}
	
	class TimeDurationBuilder {
		
		private var year = 0
		private var month = 0
		private var day = 0
		private var hour = 0
		private var minute = 0
		private var second = 0
		private var millisecond = 0
		
		/**
		 * Sets the year.
		 *
		 * @param value year
		 * @return this [TimeDurationBuilder]
		 */
		fun years(@IntRange(from = 0, to = Limits.YEAR.toLong()) value: Int): TimeDurationBuilder {
			year = value
			return this
		}
		
		/**
		 * Sets the month.
		 *
		 * @param value month
		 * @return this [TimeDurationBuilder]
		 */
		fun months(@IntRange(from = 0, to = Limits.MONTH.toLong()) value: Int): TimeDurationBuilder {
			month = value
			return this
		}
		
		/**
		 * Sets the day.
		 *
		 * @param value day
		 * @return this [TimeDurationBuilder]
		 */
		fun days(@IntRange(from = 0, to = Limits.DAY.toLong()) value: Int): TimeDurationBuilder {
			day = value
			return this
		}
		
		/**
		 * Sets the hour.
		 *
		 * @param value hour
		 * @return this [TimeDurationBuilder]
		 */
		fun hours(@IntRange(from = 0, to = Limits.HOUR.toLong()) value: Int): TimeDurationBuilder {
			hour = value
			return this
		}
		
		/**
		 * Sets the minute.
		 *
		 * @param value minute
		 * @return this [TimeDurationBuilder]
		 */
		fun minutes(@IntRange(from = 0, to = Limits.MINUTE.toLong()) value: Int): TimeDurationBuilder {
			minute = value
			return this
		}
		
		/**
		 * Sets the second.
		 *
		 * @param value second
		 * @return this [TimeDurationBuilder]
		 */
		fun seconds(@IntRange(from = 0, to = Limits.SECOND.toLong()) value: Int): TimeDurationBuilder {
			second = value
			return this
		}
		
		/**
		 * Sets the millisecond.
		 *
		 * @param value millisecond
		 * @return this [TimeDurationBuilder]
		 */
		fun milliseconds(@IntRange(from = 0, to = Limits.MILLISECOND.toLong()) value: Int): TimeDurationBuilder {
			millisecond = value
			return this
		}
		
		/**
		 * Builds the [TimeDurations].
		 *
		 * @return new [TimeDurations]
		 */
		fun build(): TimeDurations = TimeDurations(of("$year:$month:$day:$hour:$minute:$second:$millisecond"))
	}
}

fun main() {
	val timeDuration = TimeDurations.builder()
		.milliseconds(1)
		.seconds(100)
		.build()
	println(timeDuration.toStringNonZero())
	
}
