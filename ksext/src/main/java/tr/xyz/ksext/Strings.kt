package tr.xyz.ksext

/**
 * Only digits in a string
 */
val String.digits: String get() = replace("[^0-9]".toRegex(), "")
/**
 * Only non-white-spaces characters in a string
 */
val String.nonWhiteSpaces: String get() = replace("\\s+".toRegex(), "")

