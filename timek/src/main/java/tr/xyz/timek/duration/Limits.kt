package tr.xyz.timek.duration

/**
 * Defines the limit of time units.
 */
sealed class Limits {
	
	companion object {
		//@off
		/**
		 * The limit of the year.
		 */
		const val YEAR        = 999999999
		/**
		 * The limit of the month.
		 */
		const val MONTH       = 12
		/**
		 * The limit of the day.
		 */
		const val DAY         = 30
		/**
		 * The limit of the hour.
		 */
		const val HOUR        = 24
		/**
		 * The limit of the minute.
		 */
		const val MINUTE      = 60
		/**
		 * The limit of the second.
		 */
		const val SECOND      = 60
		
		/**
		 * The limit of the millisecond.
		 */
		const val MILLISECOND = 1000
		
		val YEAR_RANGE        = 0..YEAR
		val MONTH_RANGE       = 0..MONTH
		val DAY_RANGE         = 0..DAY
		val HOUR_RANGE        = 0..HOUR
		val MINUTE_RANGE      = 0..MINUTE
		val SECOND_RANGE      = 0..SECOND
		val MILLISECOND_RANGE = 0..MILLISECOND
		//@on
		
		fun getRange(unit: TimeUnit): IntRange = when (unit) {
			TimeUnit.YEAR        -> YEAR_RANGE
			TimeUnit.MONTH       -> MONTH_RANGE
			TimeUnit.DAY         -> DAY_RANGE
			TimeUnit.HOUR        -> HOUR_RANGE
			TimeUnit.MINUTE      -> MINUTE_RANGE
			TimeUnit.SECOND      -> SECOND_RANGE
			TimeUnit.MILLISECOND -> MILLISECOND_RANGE
		}
		
		fun isInLimits(value: Int, unit: TimeUnit): Boolean {
			
			return when (unit) {
				TimeUnit.YEAR        -> value in YEAR_RANGE
				TimeUnit.MONTH       -> value in MONTH_RANGE
				TimeUnit.DAY         -> value in DAY_RANGE
				TimeUnit.HOUR        -> value in HOUR_RANGE
				TimeUnit.MINUTE      -> value in MINUTE_RANGE
				TimeUnit.SECOND      -> value in SECOND_RANGE
				TimeUnit.MILLISECOND -> value in MILLISECOND_RANGE
			}
		}
		
		fun of(unit: TimeUnit): Int {
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