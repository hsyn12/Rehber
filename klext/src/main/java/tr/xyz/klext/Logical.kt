package tr.xyz.klext

/**
 * Calls the specified function after the any receiver.
 *
 * ```
 *
 *    val anyDigit = 9
 *    val anyString = "Hello"
 *    val anyUnit = println(anyString)
 *    val anyAction = {anyString + anyDigit}
 *
 *    anyDigit then {anyString}
 *    anyString then {anyDigit}
 *    anyUnit then {anyAction()}
 * ```
 *
 * @param f Function0<R> that returns the result
 * @return specified function return value
 * @receiver [T]
 */
infix fun <T, R> T.then(f: () -> R): R = f()