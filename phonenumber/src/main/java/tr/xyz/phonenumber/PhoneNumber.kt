package tr.xyz.phonenumber

import tr.xyz.ksext.digits
import tr.xyz.ksext.nonWhiteSpaces

/**
 * The regular expression for phone number.
 */
val PHONE_NUMBER_REGEX = "\\+?[0-9]{10,15}".toRegex() //+xxxxx5434937530;

/**
 * The minimum phone number length.
 */
const val MINIMUM_NUMBER_LENGTH = 10
/**
 * The maximum phone number length.
 */
const val MAXIMUM_NUMBER_LENGTH = 15

@JvmInline
value class PhoneNumber(val number: String) {
	
	val isPhoneNumber: Boolean get() = number.matches(PHONE_NUMBER_REGEX)
	val formatted: String get() = format()
	
	fun normalize(): String = number.replace("[^0-9+]".toRegex(), "")
	
	/**
	 * Formats the number to the given size.
	 * Eliminates all non-numeric characters and
	 * trims the number from the left to the given size.<br></br><br></br>
	 *
	 *
	 * `format("+90 543 493 7530", 10);` // "5434937530"<br></br>
	 * `format("+90 543 493 7530", 7);` //  "4937530"<br></br>
	 *
	 * @param size   number size
	 * @return formatted number
	 */
	fun format(size: Int = MINIMUM_NUMBER_LENGTH): String {
		val _number = number.digits
		return if (_number.length <= size) _number else _number.substring(_number.length - size)
	}
	
	fun equal(other: PhoneNumber): Boolean = number.digits == other.number.digits
	
	operator fun contains(other: PhoneNumber): Boolean {
		
		val d1 = number.digits
		val d2 = other.number.digits
		
		if (d1.length < MINIMUM_NUMBER_LENGTH || d1.length > MAXIMUM_NUMBER_LENGTH ||
		    d2.length < MINIMUM_NUMBER_LENGTH || d2.length > MAXIMUM_NUMBER_LENGTH)
			return false
		
		return d1 == d2 || d1.contains(d2) || d2.contains(d1)
	}
	
	fun beautify(): String {
		
		if (!isPhoneNumber) return number
		
		val _number = number.nonWhiteSpaces
		
		return when (_number.length) {
			11   -> "%s %s %s".format(_number.substring(0, 4), _number.substring(4, 7), _number.substring(7))
			12   -> "%s %s %s %s".format(_number.substring(0, 2), _number.substring(2, 5), _number.substring(5, 8), _number.substring(8))
			13   -> "%s %s %s %s".format(_number.substring(0, 3), _number.substring(3, 6), _number.substring(6, 9), _number.substring(9))
			10   -> "%s %s %s".format(_number.substring(0, 3), _number.substring(3, 6), _number.substring(6))
			else -> number
		}
	}
	
	
}

fun String.toPhoneNumber(): PhoneNumber = PhoneNumber(this)