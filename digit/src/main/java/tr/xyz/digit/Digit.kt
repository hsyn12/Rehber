package tr.xyz.digit

/**
 * Defines a number that seems simple.
 *
 * ```
 *
 *    val row = Digit.newDigit(4)
 *    val col = Digit.newDigit(5)
 *    row *= col
 *    println(row) // 20
 * ```
 *
 * A digit can have a limit. The `min` value is included in the digit, and
 * the `max` value is excluded.
 *
 * ```
 *
 *    val row = Digit.newDigit(min = 0, max = 10, digitValue = 4)
 *    val col = Digit.newDigit(6)
 *    row *= col
 *    println(row) // 4
 * ```
 *
 * This prints `4` because `4 * 6 = 24` and the max limit is `10`. So,
 * the maximum value of the digit is exceeded. The `row` digit cycles two
 * times to account for the result. Range of the `row` is `10` (included
 * zero) and remains `4` to reach the `20` after two cycles. The `row`
 * digit take this `4`, because this number is in the range of `0` to `10`
 * limits (___included zero and excluded `10`___). If the `row` digit were
 * connected to any digit, the occurring two cycle would be forwarded to
 * that digit.
 *
 * ```
 *
 *    val row = Digit.newDigit(min = 0, max = 10, digitValue = 4)
 *    val col = Digit.newDigit(6)
 *    val anotherRow = Digit.newDigit(6)
 *
 *    row.left = anotherRow
 *
 *    row *= col
 *    println(row) // 4
 *    println(anotherRow) // 8
 * ```
 *
 * Although the value of the `anotherRow` object is `6`, it has taken the
 * value `8` with the overflow cycles that occur in the `row` object to
 * which it is connected.
 *
 * In this way, the digits can be connected to each other. When a digit
 * overflowed along the positive direction, it is forwarded to the `left`
 * digit and cycles to `min`. And when a digit overflowed along the
 * negative direction, it is forwarded to the `right` digit and cycles to
 * `max`.
 *
 * ```
 *
 *    val digit = Digit.newDigit(0, 5, 4)
 *    digit += 1
 *    println("$digit") // 0
 *    digit -= 1
 *    println("$digit") // 4
 * ```
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
	/**
	 * Range of the digit.
	 */
	val range: Int
	
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
	
	operator fun plus(value: Int): Digit {
		digitValue += value
		return this
	}
	
	operator fun minus(value: Int): Digit {
		digitValue -= value
		return this
	}
	
	operator fun plus(value: Digit): Digit {
		digitValue += value.digitValue
		return this
	}
	
	operator fun minus(value: Digit): Digit {
		digitValue -= value.digitValue
		return this
	}
	
	/**
	 * Adds the value to the digit. If the result overflows, it is truncated
	 * and forwards the overflow count to the `right` digit for positive to the
	 * `left` digit for negative (if exists) overflows.
	 *
	 * @param value value to add
	 */
	operator fun plusAssign(value: Int) {
		digitValue += value
	}
	
	/**
	 * Subtracts the value from the digit. If the result overflows, it is
	 * truncated and forwards the overflow count to the `right` digit for
	 * positive to the `left` digit for negative (if exists) overflows.
	 *
	 * @param value value to subtract
	 */
	operator fun minusAssign(value: Int) {
		digitValue -= value
	}
	
	/**
	 * Adds the value to the digit. If the result overflows, it is truncated
	 * and forwards the overflow count to the `right` digit for positive to the
	 * `left` digit for negative (if exists) overflows.
	 *
	 * @param value value to add
	 */
	operator fun plusAssign(value: Digit) {
		digitValue += value.digitValue
	}
	
	/**
	 * Subtracts the value from the digit. If the result overflows, it is
	 * truncated and forwards the overflow count to the `right` digit for
	 * positive to the `left` digit for negative (if exists) overflows.
	 *
	 * @param value value to subtract
	 */
	operator fun minusAssign(value: Digit) {
		digitValue -= value.digitValue
	}
	
	/**
	 * Multiplies the current [Digit] value by the value. If the result
	 * overflows, it is truncated and forwards the overflow count to the
	 * `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to multiply
	 */
	operator fun timesAssign(value: Int) {
		digitValue *= value
	}
	
	/**
	 * Multiplies the current [Digit] value by the value. If the result
	 * overflows, it is truncated and forwards the overflow count to the
	 * `right` digit for positive to the `left` digit for negative (if exists)
	 * overflows.
	 *
	 * @param value value to multiply
	 */
	operator fun timesAssign(value: Digit) {
		println("$digitValue * $value")
		digitValue *= value.digitValue
	}
	
	operator fun times(value: Int): Digit {
		return newDigit(digitValue * value)
	}
	
	operator fun times(value: Digit): Digit {
		return newDigit(digitValue * value.digitValue)
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
		 * Creates a new [Digit].
		 *
		 * @param digitValue digit value
		 * @return new [Digit]
		 */
		fun newDigit(digitValue: Int = 0): Digit = NDigit(digitValue = digitValue)
	}
}

