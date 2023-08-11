package tr.xyz.call

import tr.xyz.dict.DatBox
import tr.xyz.phonenumber.PhoneNumber

/**
 * Call data.
 *
 * @property name the name of the call
 * @property number the number of the call
 * @property callType the call type
 * @property time the time of the call
 * @property duration the duration of the call
 * @property extra the extra information
 * @constructor Create a Call object
 */
class Call(
	val name: String,
	val number: PhoneNumber,
	val callType: Int,
	val time: Long,
	val duration: Int,
	val extra: String?) : DatBox() {
	
	val isIncoming: Boolean get() = callType == INCOMING || callType == INCOMING_WIFI
	val isOutgoing: Boolean get() = callType == OUTGOING || callType == OUTGOING_WIFI
	val isMissed: Boolean get() = callType == MISSED
	val isRejected: Boolean get() = callType == REJECTED
	val isBlocked: Boolean get() = callType == BLOCKED
	
	fun isType(type: Int): Boolean = callType == type
	
	override fun equals(other: Any?): Boolean = other is Call && time == other.time
	
	override fun hashCode(): Int = time.hashCode()
	
	override fun toString(): String = "Call($name, $number, $callType, $time, $duration, $extra)"
	
	fun copy(name: String = this.name, number: PhoneNumber = this.number, callType: Int = this.callType, time: Long = this.time, duration: Int = this.duration, extra: String? = this.extra): Call = Call(name, number, callType, time, duration, extra)
	
	operator fun compareTo(other: Call): Int = other.time.compareTo(time)
	
	
}