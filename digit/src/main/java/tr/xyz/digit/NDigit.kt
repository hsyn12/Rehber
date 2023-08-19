@file:JvmName("Digits")

package tr.xyz.digit

import kotlin.math.absoluteValue

/**
 * Defines a range.
 *
 * @property min minimum inclusive
 * @property max maximum exclusive
 */
interface Limited {
	val min: Int get() = 0
	val max: Int get() = Int.MAX_VALUE
}

/**
 * Defines a number in a range.
 *
 * @property digitValue digit value
 * @property cycle overflow count of the digit limit
 * @property left left Digit
 * @property right right Digit
 */
interface Digit : Limited {
	/**
	 * Digit value.
	 */
	var digitValue: Int
	/**
	 * Overflow count.
	 */
	val cycle: Int
	/**
	 * Left Digit.
	 */
	var left: Digit?
	/**
	 * Right Digit.
	 */
	var right: Digit?

	operator fun inc(): Digit {
		digitValue++
		return this
	}

	operator fun dec(): Digit {
		digitValue--
		return this
	}

	operator fun plus(value: Int): Digit {
		this.digitValue += value
		return this
	}

	operator fun minus(value: Int): Digit {
		this.digitValue -= value
		return this
	}

	/**
	 * Called when cycle is set to other than 0.
	 *
	 * @param cycle cycle count
	 */
	fun onCycle(cycle: Int) {
		digitValue += cycle
	}

	companion object {

		/**
		 * Creates a new [Digit].
		 *
		 * @param min minimum inclusive
		 * @param max maximum exclusive
		 * @param value digit value
		 * @return new [Digit]
		 */
		fun newDigit(min: Int = 0, max: Int = Int.MAX_VALUE, value: Int = 0): Digit = NDigit(max, min, value)
	}

}

class NDigit internal constructor(override val max: Int = Int.MAX_VALUE, override val min: Int = 0, digitValue: Int = 0) :
	Digit {

	override var left: Digit? = null
	override var right: Digit? = null
	private val interval = max - min

	override var cycle = 0
		private set(value) {
			field = value
			if (value > 0) left?.onCycle(value)
			else if (value < 0) right?.onCycle(value)
		}

	override var digitValue: Int = 0
		set(value) {
			cycle = 0
			field = if (value in min until max) value
			else {
				if (value >= max) {
					val _cycle = value / if (max != 0) max else 1
					if (_cycle == 0 || _cycle == 1) {
						cycle = 1
						min
					}
					else {
						cycle = _cycle
						value % interval
					}
				}
				else {
					val _cycle = min / if (value != 0) value else 1
					if (_cycle == 0 || _cycle == -1) {
						cycle = -1
						max - 1
					}
					else {
						cycle = _cycle
						value.absoluteValue % interval
					}
				}
			}
		}

	init {
		this.digitValue = digitValue
	}

	override fun toString(): String = "$digitValue"
}

fun main() {

	val seconds = Digit.newDigit(0, 60)
	val minute = Digit.newDigit(0, 60)
	seconds.left = minute
	seconds.right = minute


	Thread {
		while (true) {

			Thread.sleep(100)
			seconds.inc()
			println("${minute.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}")
		}
	}.start()


}
