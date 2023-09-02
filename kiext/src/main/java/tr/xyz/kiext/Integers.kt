package tr.xyz.kiext

/**
 * Checks if the value is zero
 */
val Int.isZero: Boolean get() = this == 0

/**
 * Checks if the value is not zero
 */
val Int.isNotZero: Boolean get() = this != 0

/**
 * Checks if the value is zero
 */
val Long.isZero: Boolean get() = this == 0L

/**
 * Checks if the value is not zero
 */
val Long.isNotZero: Boolean get() = this != 0L

/**
 * Checks if the value is zero and if it is, returns the given value, otherwise returns this.
 *
 * @param thenValue value to return if the value is zero
 * @return If the value is zero, returns the given value, otherwise returns this
 * @receiver [Int]
 */
fun Int.ifZeroThen(thenValue: Int): Int = if (isZero) thenValue else this

/**
 * Checks if the value is zero and if it is, returns the given function's return value, otherwise
 * returns this value.
 *
 * @param then `Function1<Int, Int>` that returns the value if this value is zero
 * @return If this value is zero, returns the given function's return value, otherwise returns this
 * @receiver [Int]
 */
fun Int.ifZeroThen(then: (Int) -> Int): Int = if (isZero) then(this) else this
