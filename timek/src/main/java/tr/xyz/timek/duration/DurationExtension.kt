@file:JvmName("DurationExtensions")

package tr.xyz.timek.duration

object dDuration
object tDuration
//@off
//+ ----------------------- Time Durations ----------------------------
infix fun Int.milliseconds   (duration: tDuration)  : TimeDuration = TimeDuration milliseconds this
infix fun Int.seconds        (duration: tDuration)  : TimeDuration = TimeDuration seconds this
infix fun Int.minutes        (duration: tDuration)  : TimeDuration = TimeDuration minutes this
infix fun Int.hours          (duration: tDuration)  : TimeDuration = TimeDuration hours this
infix fun Int.days           (duration: tDuration)  : TimeDuration = TimeDuration days this
infix fun Int.months         (duration: tDuration)  : TimeDuration = TimeDuration months this
infix fun Int.years          (duration: tDuration)  : TimeDuration = TimeDuration years this
infix fun Long.milliseconds  (duration: tDuration)  : TimeDuration = TimeDuration milliseconds this.toInt()
infix fun Long.seconds       (duration: tDuration)  : TimeDuration = TimeDuration seconds this.toInt()
infix fun Long.minutes       (duration: tDuration)  : TimeDuration = TimeDuration minutes this.toInt()
infix fun Long.hours         (duration: tDuration)  : TimeDuration = TimeDuration hours this.toInt()
infix fun Long.days          (duration: tDuration)  : TimeDuration = TimeDuration days this.toInt()
infix fun Long.months        (duration: tDuration)  : TimeDuration = TimeDuration months this.toInt()
infix fun Long.years         (duration: tDuration)  : TimeDuration = TimeDuration years this.toInt()
//+ ----------------------- Durations ----------------------------
infix fun Int.milliseconds   (duration: dDuration)      : Duration     = Duration milliseconds this.toLong() 
infix fun Int.seconds        (duration: dDuration)      : Duration     = Duration seconds this.toLong() 
infix fun Int.minutes        (duration: dDuration)      : Duration     = Duration minutes this.toLong()
infix fun Int.hours          (duration: dDuration)      : Duration     = Duration hours this.toLong() 
infix fun Int.days           (duration: dDuration)      : Duration     = Duration days this.toLong() 
infix fun Int.months         (duration: dDuration)      : Duration     = Duration months this.toLong() 
infix fun Int.years          (duration: dDuration)      : Duration     = Duration years this.toLong() 
infix fun Long.milliseconds  (duration: dDuration)      : Duration     = Duration milliseconds this 
infix fun Long.seconds       (duration: dDuration)      : Duration     = Duration seconds this 
infix fun Long.minutes       (duration: dDuration)      : Duration     = Duration minutes this 
infix fun Long.hours         (duration: dDuration)      : Duration     = Duration hours this 
infix fun Long.days          (duration: dDuration)      : Duration     = Duration days this 
infix fun Long.months        (duration: dDuration)      : Duration     = Duration months this 
infix fun Long.years         (duration: dDuration)      : Duration     = Duration years this 
//@on


fun main() {
	val duration = 45 seconds tDuration
	val duration2 = 1 minutes tDuration
	
	println(duration.with(duration2).toStringNonZero())
}













