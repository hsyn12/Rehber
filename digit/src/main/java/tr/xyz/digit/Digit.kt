package tr.xyz.digit

import tr.xyz.kiext.ifZeroThen

class Digit(value: Int = 0, override val min: Int = -1_000_000, override val max: Int = 1_000_000) : Limited {
	/**
	 * Range of the digit.
	 */
	val range: Int = (max - min) + 1
	
	var cycle = 0
		private set(value) {
			field = value
			if (value > 0) left?.onCycle(value)
			else if (value < 0) right?.onCycle(value)
		}
	
	var digitValue: Int = 0
		set(value) {
			cycle = 0
			field = if (value in min..max) value
			else {
				if (value > max) {
					// println("value > max. value = $value, max = $max, range = $range")
					cycle = (value / range).ifZeroThen(1)
					min + (value - max - 1 % range)
				}
				else { //+ value < min
					// println("value < min. value = $value, min = $min, range = $range")
					cycle = (min / range).ifZeroThen(-1)
					max - (min - value - 1 % range)
				}
			}
		}
	
	/**
	 * Left Digit.
	 */
	var left: Digit? = null
	
	/**
	 * Right Digit.
	 */
	var right: Digit? = null
	
	init {
		require(min >= -1_000_000) {"Min value must be greater than -1_000_000, but [min : $min]"}
		require(max <= 1_000_000) {"Max value must be less than 1_000_000, but [max : $max]"}
		require(max > min) {"Max value must be greater than min value, but [max : $max, min : $min]"}
		this.digitValue = value
	}
	
	/**
	 * Increments the digit value by one.
	 *
	 * @return this [Digit]
	 */
	operator fun inc(): Digit {
		digitValue++
		return this
	}
	
	/**
	 * Decrements the digit value by one.
	 *
	 * @return this [Digit]
	 */
	operator fun dec(): Digit {
		digitValue--
		return this
	}
	
	operator fun plus(value: Int): Digit = Digit(digitValue + value)
	
	operator fun minus(value: Int): Digit = Digit(digitValue - value)
	
	operator fun plus(value: Digit): Digit = Digit(digitValue + value.digitValue)
	
	operator fun minus(value: Digit): Digit = Digit(digitValue - value.digitValue)
	
	/**
	 * Adds the value to the digit. If the result overflows, it is truncated and forwards the
	 * overflow count to the `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to add
	 */
	operator fun plusAssign(value: Int) {
		digitValue += value
	}
	
	/**
	 * Subtracts the value from the digit. If the result overflows, it is truncated and forwards the
	 * overflow count to the `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to subtract
	 */
	operator fun minusAssign(value: Int) {
		digitValue -= value
	}
	
	/**
	 * Adds the value to the digit. If the result overflows, it is truncated and forwards the
	 * overflow count to the `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to add
	 */
	operator fun plusAssign(value: Digit) {
		digitValue += value.digitValue
	}
	
	/**
	 * Subtracts the value from the digit. If the result overflows, it is truncated and forwards the
	 * overflow count to the `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to subtract
	 */
	operator fun minusAssign(value: Digit) {
		digitValue -= value.digitValue
	}
	
	/**
	 * Multiplies the current [Digit] value by the value. If the result overflows, it is truncated
	 * and forwards the overflow count to the `right` digit for positive to the `left` digit for
	 * negative (if exists) overflows.
	 *
	 * @param value value to multiply
	 */
	operator fun timesAssign(value: Int) {
		digitValue *= value
	}
	
	/**
	 * Multiplies the current [Digit] value by the value. If the result overflows, it is truncated
	 * and forwards the overflow count to the `right` digit for positive to the `left` digit for
	 * negative (if exists) overflows.
	 *
	 * @param value value to multiply
	 */
	operator fun timesAssign(value: Digit) {
		println("$digitValue * $value")
		digitValue *= value.digitValue
	}
	
	operator fun times(value: Int): Digit = Digit(digitValue * value)
	
	operator fun times(value: Digit): Digit = Digit(digitValue * value.digitValue)
	
	/**
	 * Called when [cycle] is set to other than `0`.
	 *
	 * @param cycle cycle count. This number can be negative.
	 */
	fun onCycle(cycle: Int) {
		digitValue += cycle
	}
	
	override fun toString(): String = "$digitValue"
	override fun equals(other: Any?): Boolean = other is Digit && digitValue == other.digitValue
	override fun hashCode(): Int = digitValue
	
}

