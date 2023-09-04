package tr.xyz.kandom

import java.security.SecureRandom

/**
 * Provides methods that generate random numbers or boolean values.
 */
class Kandom {
	
	companion object {
		
		private val random = SecureRandom()
		
		/**
		 * Next random integer.
		 *
		 * @param endExclusive end limit
		 * @return [0, endExclusive) a number
		 */
		fun nextInt(endExclusive: Int = Int.MAX_VALUE) = random.nextInt(endExclusive)
		
		/**
		 * Next random integer
		 *
		 * @param startInclusive start number
		 * @param endExclusive end number
		 * @return [0, endExclusive) a number
		 */
		fun nextInt(startInclusive: Int = 0, endExclusive: Int = Int.MAX_VALUE) = random.nextInt(endExclusive - startInclusive) + startInclusive
		
		/**
		 * Next random long
		 *
		 * @param endExclusive end limit
		 * @return [0, endExclusive) a number
		 */
		fun nextLong(endExclusive: Long = Long.MAX_VALUE) = random.nextLong(endExclusive)
		
		/**
		 * Next random long.
		 *
		 * @param startInclusive start number
		 * @param endExclusive end number
		 * @return [0, endExclusive) a number
		 */
		fun nextLong(startInclusive: Long = 0L, endExclusive: Long = Long.MAX_VALUE) = random.nextLong(endExclusive - startInclusive) + startInclusive
		
		/**
		 * Next boolean.
		 *
		 * @param percent give it a shot with a percentage. If it is 100 or greater than 100, it will
		 *     definitely return {@code true}. If it is 0 or less than zero, it will certainly return
		 *     {@code false}. On the percentages that in the range (1-99), returning `true` will be
		 *     more likely as it rises. The default value is `50`.
		 * @return next boolean
		 */
		fun nextBool(percent: Int = 50): Boolean {
			if (percent >= 100) return true
			if (percent <= 0) return false
			return nextInt(100 / percent) == 0
		}
	}
	
}