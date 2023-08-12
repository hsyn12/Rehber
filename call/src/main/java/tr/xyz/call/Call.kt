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
	val extra: String?) : DatBox() {
	
	val isIncoming: Boolean get() = type == INCOMING || type == INCOMING_WIFI
	val isOutgoing: Boolean get() = type == OUTGOING || type == OUTGOING_WIFI
	val isMissed: Boolean get() = type == MISSED
	val isRejected: Boolean get() = type == REJECTED
	val isBlocked: Boolean get() = type == BLOCKED
	
	fun isType(type: Int): Boolean = this.type == type
	
	override fun equals(other: Any?): Boolean = other is Call && time == other.time
	
	override fun hashCode(): Int = time.hashCode()
	
	override fun toString(): String = "Call($name, $number, $type, $time, $duration, $extra)"
	
	fun copy(name: String = this.name, number: String = this.number.asString, callType: Int = this.type, time: Long = this.time, duration: Int = this.duration, extra: String? = this.extra): Call = Call(name, number.asPhoneNumber, callType, time, duration, extra)
	
	operator fun compareTo(other: Call): Int = other.time.compareTo(time)
	
	
}