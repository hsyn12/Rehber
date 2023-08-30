package tr.xyz.digit

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