@file:JvmName("DurationExtensions")

package tr.xyz.timek.duration

/**
 * The object that indicates the [Duration].
 */
object dDuration
/**
 * The object that indicates the [TimeDuration].
 */
object tDuration

//@off
//+ ----------------------- Time Durations ----------------------------
/**
 * Converts an Int value into a [TimeDuration] object representing a number of milliseconds.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration].
 * @return The [TimeDuration] object with the specified number of milliseconds.
 * @receiver [Int]
 */
infix fun Int.milliseconds   (duration: tDuration)  : TimeDuration = TimeDuration milliseconds this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of seconds.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of seconds. 
 * @receiver [Int] 
 */
infix fun Int.seconds        (duration: tDuration)  : TimeDuration = TimeDuration seconds this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of minutes.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of minutes. 
 * @receiver [Int] 
 */
infix fun Int.minutes        (duration: tDuration)  : TimeDuration = TimeDuration minutes this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of hours.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of hours. 
 * @receiver [Int] 
 */
infix fun Int.hours          (duration: tDuration)  : TimeDuration = TimeDuration hours this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of days.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of days. 
 * @receiver [Int] 
 */
infix fun Int.days           (duration: tDuration)  : TimeDuration = TimeDuration days this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of months.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of months. 
 * @receiver [Int] 
 */
infix fun Int.months         (duration: tDuration)  : TimeDuration = TimeDuration months this
/**
 * Converts an Int value into a [TimeDuration] object representing a number of years.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of years. 
 * @receiver [Int] 
 */
infix fun Int.years          (duration: tDuration)  : TimeDuration = TimeDuration years this
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of milliseconds.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of milliseconds. 
 * @receiver [Long] 
 */
infix fun Long.milliseconds  (duration: tDuration)  : TimeDuration = TimeDuration milliseconds this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of seconds.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of seconds. 
 * @receiver [Long] 
 */
infix fun Long.seconds       (duration: tDuration)  : TimeDuration = TimeDuration seconds this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of minutes.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of minutes. 
 * @receiver [Long] 
 */
infix fun Long.minutes       (duration: tDuration)  : TimeDuration = TimeDuration minutes this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of hours.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of hours. 
 * @receiver [Long] 
 */
infix fun Long.hours         (duration: tDuration)  : TimeDuration = TimeDuration hours this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of days.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of days. 
 * @receiver [Long] 
 */
infix fun Long.days          (duration: tDuration)  : TimeDuration = TimeDuration days this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of months.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of months. 
 * @receiver [Long] 
 */
infix fun Long.months        (duration: tDuration)  : TimeDuration = TimeDuration months this.toInt()
/**
 * Converts a [Long] value into a [TimeDuration] object representing a number of years.
 *
 * @param duration The [tDuration] object to indicate the [TimeDuration]. 
 * @return The [TimeDuration] object with the specified number of years. 
 * @receiver [Long] 
 */
infix fun Long.years         (duration: tDuration)  : TimeDuration = TimeDuration years this.toInt()
//+ ----------------------- Durations ----------------------------
/**
 * Converts a [Int] value into a [Duration] object representing a number of milliseconds.
 *
 * @param duration The [dDuration] object to indicate the [Duration]. 
 * @return The [Duration] object with the specified number of milliseconds. 
 * @receiver [Int] 
 */
infix fun Int.milliseconds   (duration: dDuration)      : Duration     = Duration milliseconds this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of milliseconds.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of milliseconds.
 * @receiver [Int]
 */
infix fun Int.seconds        (duration: dDuration)      : Duration     = Duration seconds this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of minutes.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of minutes.
 * @receiver [Int]
 */
infix fun Int.minutes        (duration: dDuration)      : Duration     = Duration minutes this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of hours.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of hours. 
 * @receiver [Int] 
 */
infix fun Int.hours          (duration: dDuration)      : Duration     = Duration hours this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of days.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of days.
 * @receiver [Int]
 */
infix fun Int.days           (duration: dDuration)      : Duration     = Duration days this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of months.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of months. 
 * @receiver [Int] 
 */
infix fun Int.months         (duration: dDuration)      : Duration     = Duration months this.toLong()
/**
 * Converts a [Int] value into a [Duration] object representing a number of years.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of years.
 * @receiver [Int]
 */
infix fun Int.years          (duration: dDuration)      : Duration     = Duration years this.toLong()
/**
 * Converts a [Long] value into a [Duration] object representing a number of milliseconds.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of milliseconds.
 * @receiver [Long]
 */
infix fun Long.milliseconds  (duration: dDuration)      : Duration     = Duration milliseconds this
/**
 * Converts a [Long] value into a [Duration] object representing a number of seconds.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of seconds.
 * @receiver [Long]
 */
infix fun Long.seconds       (duration: dDuration)      : Duration     = Duration seconds this
/**
 * Converts a [Long] value into a [Duration] object representing a number of minutes.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of minutes.
 * @receiver [Long]
 */
infix fun Long.minutes       (duration: dDuration)      : Duration     = Duration minutes this
/**
 * Converts a [Long] value into a [Duration] object representing a number of hours.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of hours.
 * @receiver [Long]
 */
infix fun Long.hours         (duration: dDuration)      : Duration     = Duration hours this
/**
 * Converts a [Long] value into a [Duration] object representing a number of days.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of days.  
 * @receiver [Long]
 */
infix fun Long.days          (duration: dDuration)      : Duration     = Duration days this
/**
 * Converts a [Long] value into a [Duration] object representing a number of months.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of months.
 * @receiver [Long]
 */
infix fun Long.months        (duration: dDuration)      : Duration     = Duration months this
/**
 * Converts a [Long] value into a [Duration] object representing a number of years.
 *
 * @param duration The [dDuration] object to indicate the [Duration].
 * @return The [Duration] object with the specified number of years.
 * @receiver [Long]
 */
infix fun Long.years         (duration: dDuration)      : Duration     = Duration years this 
//@on


fun main() {
	val duration = 45 seconds tDuration with (1 minutes tDuration)
	val duration2 = 1 minutes tDuration
	
	println(duration.toStringNonZero())
}













