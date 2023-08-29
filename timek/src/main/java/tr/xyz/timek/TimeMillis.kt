package tr.xyz.timek

/**
 * Defines all time units in milliseconds.
 */
interface TimeMillis {
	
	companion object {
		/**
		 * Milliseconds in a second
		 */
		const val SECOND = 1000L
		/**
		 * Milliseconds in a minute
		 */
		const val MINUTE = SECOND * 60
		/**
		 * Milliseconds in an hour
		 */
		const val HOUR = MINUTE * 60
		/**
		 * Milliseconds in a day
		 */
		const val DAY = HOUR * 24
		/**
		 * Milliseconds in a month
		 */
		const val MONTH = DAY * 30
		/**
		 * Milliseconds in a year
		 */
		const val YEAR = DAY * 365
	}
}