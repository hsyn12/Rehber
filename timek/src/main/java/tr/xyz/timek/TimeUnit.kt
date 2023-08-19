package tr.xyz.timek

import java.util.*

/**
 * Type of the time units.
 */
enum class TimeUnit(val limit: Int) {

	YEAR(Int.MAX_VALUE),
	MONTH(12),
	DAY(365),
	HOUR(24),
	MINUTE(60),
	SECOND(60),
	MILLISECOND(1000);

	operator fun contains(value: Int): Boolean = value <= limit
	operator fun compareTo(value: Int) = value - limit

	override fun toString(): String {
		return when (this) {
			YEAR        -> BUNDLE.getString("YEAR")
			MONTH       -> BUNDLE.getString("MONTH")
			DAY         -> BUNDLE.getString("DAY")
			HOUR        -> BUNDLE.getString("HOUR")
			MINUTE      -> BUNDLE.getString("MINUTE")
			SECOND      -> BUNDLE.getString("SECOND")
			MILLISECOND -> BUNDLE.getString("MILLISECOND")
			else        -> ""
		}
	}

	/**
	 * Compares this [TimeUnit] with the given [TimeUnit].
	 *
	 * @param other [TimeUnit]
	 * @return `true` if this [TimeUnit] is greater than the given [TimeUnit].<br></br>
	 * Unit order is: [YEAR], [MONTH], [DAY], [HOUR], [MINUTE], [SECOND], [MILLISECOND].
	 * So, the greatest unit is [YEAR],
	 * the least unit is [MILLISECOND].
	 * But [ordinal] returns reversed order,
	 * so `YEAR.ordinal()` returns `0`,
	 * `MONTH.ordinal()` returns `1` etc.
	 */
	fun isGreaterThan(other: TimeUnit): Boolean = ordinal < other.ordinal
	/**
	 * Compares this [TimeUnit] with the given [TimeUnit].
	 *
	 * @param other [TimeUnit]
	 * @return `true` if this [TimeUnit] is less than the given [TimeUnit].<br></br>
	 * Unit order is: [YEAR], [MONTH], [DAY],[HOUR], [MINUTE], [SECOND], [MILLISECOND].
	 * So, the greatest unit is [YEAR],
	 * the least unit is [MILLISECOND].
	 * But [ordinal] returns reversed order,
	 * so `YEAR.ordinal()` returns `0`,
	 * `MONTH.ordinal()` returns `1` etc.
	 */
	fun isLessThan(other: TimeUnit): Boolean = ordinal > other.ordinal

	companion object {
		val BUNDLE: ResourceBundle = ResourceBundle.getBundle("units")
	}
}