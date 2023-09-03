package tr.xyz.timek.duration

/**
 * Defines the limit of the time units.
 */
sealed class Limits {
	
	companion object {
		//@off
		/**
		 * The limit of the year.
		 */
		const val YEAR        = 1_000_000
		/**
		 * The limit of the month.
		 */
		const val MONTH       = 11
		/**
		 * The limit of the day.
		 */
		const val DAY         = 29
		/**
		 * The limit of the hour.
		 */
		const val HOUR        = 23
		/**
		 * The limit of the minute.
		 */
		const val MINUTE      = 59
		/**
		 * The limit of the second.
		 */
		const val SECOND      = 59
		
		/**
		 * The limit of the millisecond.
		 */
		const val MILLISECOND = 999
		
		/**
		 * The range of the year `[0..999_999_999]`.
		 */
		val RANGE_YEAR        = 0..YEAR
		/**
		 * The range of the month `[1..11]`.
		 */
		val RANGE_MONTH       = 0..MONTH
		/**
		 * The range of the day `[1..29]`.
		 */
		val RANGE_DAY         = 0..DAY
		/**
		 * The range of the hour `[0..23]`.
		 */
		val RANGE_HOUR        = 0..HOUR
		/**
		 * The range of the minute `[0..59]`.
		 */
		val RANGE_MINUTE      = 0..MINUTE
		/**
		 * The range of the second `[0..59]`.
		 */
		val RANGE_SECOND      = 0..SECOND
		/**
		 * The range of the millisecond `[0..999]`.
		 */
		val RANGE_MILLISECOND = 0..MILLISECOND
		//@on
		
		/**
		 * Returns the range of the given unit.
		 *
		 * @param unit [TimeUnit]
		 * @return [IntRange]
		 */
		fun rangeOf(unit: TimeUnit): IntRange = when (unit) {
			TimeUnit.YEAR        -> RANGE_YEAR
			TimeUnit.MONTH       -> RANGE_MONTH
			TimeUnit.DAY         -> RANGE_DAY
			TimeUnit.HOUR        -> RANGE_HOUR
			TimeUnit.MINUTE      -> RANGE_MINUTE
			TimeUnit.SECOND      -> RANGE_SECOND
			TimeUnit.MILLISECOND -> RANGE_MILLISECOND
		}
		
		/**
		 * Checks the given value whether it is in the given unit range.
		 *
		 * @param value value
		 * @param unit unit
		 * @return `true` if the value is in the range
		 */
		fun isInLimits(value: Int, unit: TimeUnit): Boolean {
			return when (unit) {
				TimeUnit.YEAR        -> value in RANGE_YEAR
				TimeUnit.MONTH       -> value in RANGE_MONTH
				TimeUnit.DAY         -> value in RANGE_DAY
				TimeUnit.HOUR        -> value in RANGE_HOUR
				TimeUnit.MINUTE      -> value in RANGE_MINUTE
				TimeUnit.SECOND      -> value in RANGE_SECOND
				TimeUnit.MILLISECOND -> value in RANGE_MILLISECOND
			}
		}
		
		/**
		 * Returns the maximum value of the given unit.
		 *
		 * @param unit [TimeUnit]
		 * @return [Int]
		 */
		fun maxOf(unit: TimeUnit): Int {
			return when (unit) {
				TimeUnit.YEAR        -> YEAR
				TimeUnit.MONTH       -> MONTH
				TimeUnit.DAY         -> DAY
				TimeUnit.HOUR        -> HOUR
				TimeUnit.MINUTE      -> MINUTE
				TimeUnit.SECOND      -> SECOND
				TimeUnit.MILLISECOND -> MILLISECOND
			}
		}
		
		
	}
}