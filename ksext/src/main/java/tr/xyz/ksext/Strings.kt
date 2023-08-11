package tr.xyz.ksext

/**
 * Digits in a string
 */
val String.digits: String get() = replace("[^0-9]".toRegex(), "")
/**
 * Non-white-spaces in a string
 */
val String.nonWhiteSpaces: String get() = replace("\\s+".toRegex(), " ")