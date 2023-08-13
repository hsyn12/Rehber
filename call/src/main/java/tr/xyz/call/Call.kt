package tr.xyz.call

import tr.xyz.dict.DatBox
import tr.xyz.phonenumber.PhoneNumber
import tr.xyz.phonenumber.asPhoneNumber

/**
 * Call data.
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
	val name: String,
	val number: PhoneNumber,
	val type: Int,
	val time: Long,
	val duration: Int,
	var extra: String?) : DatBox() {
	
	val isIncoming: Boolean get() = type == INCOMING || type == INCOMING_WIFI
	val isOutgoing: Boolean get() = type == OUTGOING || type == OUTGOING_WIFI
	val isMissed: Boolean get() = type == MISSED
	val isRejected: Boolean get() = type == REJECTED
	val isBlocked: Boolean get() = type == BLOCKED
	
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
	 * Creates a copy of this call object with the given name,
	 * number, call type, time, duration and extra.
	 *
	 * @param name name
	 * @param number number
	 * @param callType call type
	 * @param time time
	 * @param duration duration
	 * @param extra extra
	 * @return the new call
	 */
	fun copy(name: String = this.name, number: String = this.number.asString, callType: Int = this.type, time: Long = this.time, duration: Int = this.duration, extra: String? = this.extra): Call = Call(name, number.asPhoneNumber, callType, time, duration, extra)
	
	/**
	 * Compares this object with the specified object by [time].
	 *
	 * @param other the object to be compared
	 * @return a negative integer, zero or a positive integer as this object [time] is greater than,
	 *  equal to or less than the specified object [time].
	 */
	operator fun compareTo(other: Call): Int = other.time.compareTo(time)
	
	
}