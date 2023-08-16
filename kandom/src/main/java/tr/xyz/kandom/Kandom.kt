package tr.xyz.kandom

import java.security.SecureRandom

/** It provides methods that generate random numbers or boolean values. */
class Kandom {

	companion object {

		private val random = SecureRandom()

		/** Next random boolean. */
		val nextBool get() = random.nextBoolean()
		/** Next random integer. */
		val nextInt get() = random.nextInt()
		/** Next random long. */
		val nextLong get() = random.nextLong()
		/**
		 * Next random integer
		 *
		 * @param endExclusive end limit
		 * @return [0, endExclusive) a number
		 */
		fun nextInt(endExclusive: Int) = random.nextInt(endExclusive)
		/**
		 * Next random integer
		 *
		 * @param startInclusive start number
		 * @param endExclusive end number
		 * @return [0, endExclusive) a number
		 */
		fun nextInt(startInclusive: Int, endExclusive: Int) = random.nextInt(endExclusive - startInclusive) + startInclusive
		/**
		 * Next random long
		 *
		 * @param endExclusive end limit
		 * @return [0, endExclusive) a number
		 */
		fun nextLong(endExclusive: Long) = random.nextLong(endExclusive)
		/**
		 * Next random long.
		 *
		 * @param startInclusive start number
		 * @param endExclusive end number
		 * @return [0, endExclusive) a number
		 */
		fun nextLong(startInclusive: Long, endExclusive: Long) = random.nextLong(endExclusive - startInclusive) + startInclusive

		/**
		 * Next boolean.
		 *
		 * @param percent give it a shot with a percentage. If it is 100 or greater
		 *     than 100 it will definitely return {@code true}. If it is 0 or less
		 *     than 0 it will certainly return {@code false}. On the percentages
		 *     that in the range (1-99), returning `true` will be more likely as it
		 *     rises.
		 * @return next boolean
		 */
		fun nextBool(percent: Int): Boolean {
			if (percent >= 100) return true
			if (percent <= 0) return false
			return nextInt(100 / percent) == 0
		}
	}

}