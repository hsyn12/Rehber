@file:JvmName("TimeUnits")

package tr.xyz.timek.unit

import java.util.*

/**
 * Type of the time unit.
 *
 * @param LIMIT limit of the time unit
 * @param ORDER order of the time unit
 */
enum class TimeUnit {
	YEAR,
	MONTH,
	DAY,
	HOUR,
	MINUTE,
	SECOND,
	MILLISECOND;
	
	override fun toString(): String {
		return when (this) {
			YEAR        -> BUNDLE.getString("Year")
			MONTH       -> BUNDLE.getString("Month")
			DAY         -> BUNDLE.getString("Day")
			HOUR        -> BUNDLE.getString("Hour")
			MINUTE      -> BUNDLE.getString("Minute")
			SECOND      -> BUNDLE.getString("Second")
			MILLISECOND -> BUNDLE.getString("Millisecond")
		}
	}
	
	companion object {
		val BUNDLE: ResourceBundle = ResourceBundle.getBundle("units")
	}
}
