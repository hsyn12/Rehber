package tr.xyz.call

import tr.xyz.dict.DatBox
import tr.xyz.phonenumber.PhoneNumber

/**
 * Holds Call data.
 *
 * @property name the name of the call
 * @property number the number of the call
 * @property type the call type
 * @property time the time of the call
 * @property duration the duration of the call
 * @property extra the extra information
 * @constructor Create a Call object
 */
class Call(
	val name: String?,
	val number: PhoneNumber,
	@CallType val type: Int,
	val time: Long,
	val duration: Int,
	var extra: String?) : DatBox() {

	//@off
	val isIncoming: Boolean get() = type == Type.INCOMING || type == Type.INCOMING_WIFI
	val isOutgoing: Boolean get() = type == Type.OUTGOING || type == Type.OUTGOING_WIFI
	val isMissed: Boolean get()   = type == Type.MISSED
	val isRejected: Boolean get() = type == Type.REJECTED
	val isBlocked: Boolean get()  = type == Type.BLOCKED
  //@on

	/**
	 * Checks if the call is of the given type.
	 *
	 * @param type the type
	 * @return `true` if the call is of the given type
	 */
	fun isType(type: Int): Boolean = this.type == type

	override fun equals(other: Any?): Boolean = other is Call && time == other.time

	override fun hashCode(): Int = time.hashCode()

	override fun toString(): String = "Call($name, $number, $type, $time, $duration, $extra)"

	/**
	 * Compares this object with the specified object by [time].
	 *
	 * @param other the object to be compared
	 * @return a negative integer, zero or a positive integer as this object [time] is greater than,
	 *  equal to or less than the specified object [time].
	 */
	operator fun compareTo(other: Call): Int = other.time.compareTo(time)
}


