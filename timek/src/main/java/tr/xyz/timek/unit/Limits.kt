package tr.xyz.timek.unit

/**
 * Defines the limit of time units.
 */
sealed class Limits {
	companion object {
		
		
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
		
		/**
		 *  The limit of the year.
		 */
		const val YEAR = 999999999
		
		/**
		 *  The limit of the month.
		 */
		const val MONTH = 11
		
		/**
		 *  The limit of the day.
		 */
		const val DAY = 29
		
		/**
		 *  The limit of the hour.
		 */
		const val HOUR = 23
		
		/**
		 *  The limit of the minute.
		 */
		const val MINUTE = 59
		
		/**
		 *  The limit of the second.
		 */
		const val SECOND = 59
		
		/**
		 *  The limit of the millisecond.
		 */
		const val MILLISECOND = 999
	}
}