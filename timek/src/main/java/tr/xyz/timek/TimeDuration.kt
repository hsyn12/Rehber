@file:JvmName("Durations")

package tr.xyz.timek

import kotlin.math.absoluteValue

/**
 * Represents a duration of time with different units (year, month, day, hour, minute, second, millisecond).
 * The duration is calculated based on a given value in milliseconds.
 * ###
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

	init {

		val isNegative = value < 0
		var year = 0
		var month = 0
		var day = 0
		var hour = 0
		var minute = 0
		var second = 0
		var millisecond = 0
		var duration = value.absoluteValue

		while (true) {

			if (duration >= TimeMillis.YEAR) {
				year = (duration / TimeMillis.YEAR).toInt()
				if (isNegative) year = -year
				duration %= TimeMillis.YEAR
			}
			else if (duration >= TimeMillis.MONTH) {
				month = (duration / TimeMillis.MONTH).toInt()
				if (isNegative) month = -month
				duration %= TimeMillis.MONTH
			}
			else if (duration >= TimeMillis.DAY) {
				day = (duration / TimeMillis.DAY).toInt()
				if (isNegative) day = -day
				duration %= TimeMillis.DAY
			}
			else if (duration >= TimeMillis.HOUR) {
				hour = (duration / TimeMillis.HOUR).toInt()
				if (isNegative) hour = -hour
				duration %= TimeMillis.HOUR
			}
			else if (duration >= TimeMillis.MINUTE) {

				minute = (duration / TimeMillis.MINUTE).toInt()
				if (isNegative) minute = -minute
				duration %= TimeMillis.MINUTE
			}
			else if (duration >= TimeMillis.SECOND) {
				second = (duration / TimeMillis.SECOND).toInt()
				if (isNegative) second = -second
				duration %= TimeMillis.SECOND
			}
			else {
				millisecond = duration.toInt()
				if (isNegative) millisecond = -millisecond
				break
			}
		}

		durations = listOf(
			Duration.year(year),
			Duration.month(month),
			Duration.day(day),
			Duration.hour(hour),
			Duration.minute(minute),
			Duration.second(second),
			Duration.millisecond(millisecond)
		)
	}

	override fun toString() = "$year.$month.$day.$hour.$minute.$second.$millisecond"


}