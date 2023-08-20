@file:JvmName("TimeUnits")

package tr.xyz.timek.unit

import java.util.*

/**
 * Defines the limits of the time units.
 */
sealed class Limits {
	
	companion object {
		
		const val MILLISECOND = 1000
		const val SECOND = 60
		const val MINUTE = 60
		const val HOUR = 24
		const val DAY = 30
		const val MONTH = 12
		const val YEAR = 999_999_999
	}
}

/**
 * Type of the time units.
 */
sealed class TimeUnit(val limit: Int) {
	
	operator fun contains(value: Int): Boolean = value <= limit
	operator fun compareTo(value: Int) = value - limit
	
	override fun toString(): String {
		return when (this) {
			is Year        -> BUNDLE.getString("YEAR")
			is Month       -> BUNDLE.getString("MONTH")
			is Day         -> BUNDLE.getString("DAY")
			is Hour        -> BUNDLE.getString("HOUR")
			is Minute      -> BUNDLE.getString("MINUTE")
			is Second      -> BUNDLE.getString("SECOND")
			is Millisecond -> BUNDLE.getString("MILLISECOND")
		}
	}
	
	companion object {
		val BUNDLE: ResourceBundle = ResourceBundle.getBundle("units")
		
		val YEAR = Year()
		val MONTH = Month()
		val DAY = Day()
		val HOUR = Hour()
		val MINUTE = Minute()
		val SECOND = Second()
		val MILLISECOND = Millisecond()
	}
}

class Year : TimeUnit(Limits.YEAR)
class Month : TimeUnit(Limits.MONTH)
class Day : TimeUnit(Limits.DAY)
class Hour : TimeUnit(Limits.HOUR)
class Minute : TimeUnit(Limits.MINUTE)
class Second : TimeUnit(Limits.SECOND)
class Millisecond : TimeUnit(Limits.MILLISECOND)