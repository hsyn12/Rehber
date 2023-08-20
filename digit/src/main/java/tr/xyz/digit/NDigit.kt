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
 * Defines a number that seems simple.
 * ```
 *
 * val row = Digit.newDigit(4)
 * val col = Digit.newDigit(5)
 * row *= col
 * println(row) // 20
 * ```
 *
 * A digit can have a limit.
 * ```
 *
 * val row = Digit.newDigit(min = 0, max = 9, digitValue = 4)
 * val col = Digit.newDigit(5)
 * row *= col
 * println(row) // 2
 * ```
 *
 * This prints `2` because `4 * 5 = 20` and the limit is `9`.
 * So, the maximum value of the digit is exceeded.
 * The `row` digit cycles two times to account for the result.
 * So, `2 * 9 = 18` and remains `2` to reach the `20`. The `row` digit take this `2`,
 * because this number is in the range of `0` to `9` limits.
 * If the `row` digit were connected to any digit,
 * the occurring two cycle would be forwarded to that digit.
 * ```
 *
 * val row = Digit.newDigit(min = 0, max = 9, digitValue = 4)
 * val col = Digit.newDigit(5)
 * val anotherRow = Digit.newDigit(6)
 *
 * row.left = anotherRow
 *
 * row *= col
 * println(row) // 2
 * println(anotherRow) // 8
 * ```
 * Although the value of the `anotherRow` object is `6`,
 * it has taken the value `8` with the overflow cycles that occur in the `row` object to which it is connected.
 *
 * In this way, the digits can be connected to each other.
 * When a digit overflowed along the positive direction, it is forwarded to the `left` digit.
 * And when a digit overflowed along the negative direction, it is forwarded to the `right` digit.
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
	
	operator fun plusAssign(value: Int) {
		digitValue += value
	}
	
	operator fun minusAssign(value: Int) {
		digitValue -= value
	}
	
	operator fun plusAssign(value: Digit) {
		digitValue += value.digitValue
	}
	
	operator fun minusAssign(value: Digit) {
		digitValue -= value.digitValue
	}
	
	operator fun plus(value: Int): Digit = newDigit(min, max, digitValue + value)
	
	operator fun minus(value: Int): Digit = newDigit(min, max, digitValue - value)
	
	operator fun times(value: Int): Digit = newDigit(min, max, digitValue * value)
	
	operator fun times(value: Digit): Digit = newDigit(min, max, digitValue * value.digitValue)
	
	operator fun timesAssign(value: Int) {
		digitValue *= value
	}
	
	operator fun timesAssign(value: Digit) {
		digitValue *= value.digitValue
	}
	
	/**
	 * Called when [cycle] is set to other than `0`.
	 *
	 * @param cycle cycle count. This number can be negative.
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
		 * @param digitValue digit value
		 * @return new [Digit]
		 */
		fun newDigit(min: Int = 0, max: Int = Int.MAX_VALUE, digitValue: Int = 0): Digit = NDigit(max, min, digitValue)
		
		/**
		 *  Creates a new [Digit].
		 *
		 * @param digitValue digit value
		 * @return new [Digit]
		 */
		fun newDigit(digitValue: Int = 0): Digit = NDigit(digitValue = digitValue)
	}
}

class NDigit internal constructor(override val max: Int = Int.MAX_VALUE, override val min: Int = 0, digitValue: Int = 0) :
	Digit {
	
	private val interval = max - min
	override var left: Digit? = null
	override var right: Digit? = null
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
	
	val row = Digit.newDigit(min = 0, max = 9, digitValue = 4)
	val col = Digit.newDigit(5)
	val anotherRow = Digit.newDigit(6)
	
	row.left = anotherRow
	
	row *= col
	println(row) // 2
	println(anotherRow) // 8
	
}
