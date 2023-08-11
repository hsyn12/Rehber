package tr.xyz.call

import tr.xyz.contact.Contact
import tr.xyz.kiext.isNotZero
import tr.xyz.kkey.CONTACT_ID

class CallLog(val calls: List<Call>) {
	
	private val callMap = calls.groupBy(this::key)
	val incomingCalls = calls.filter(Call::isIncoming)
	val outgoingCalls = calls.filter(Call::isOutgoing)
	val missedCalls = calls.filter(Call::isMissed)
	val rejectedCalls = calls.filter(Call::isRejected)
	val blockedCalls = calls.filter(Call::isBlocked)
	
	operator fun get(id: String): List<Call>? = callMap[id]
	operator fun get(id: Long): List<Call>? = callMap[id.toString()]
	operator fun get(contact: Contact): List<Call>? = callMap[contact.contactId.toString()]
	operator fun contains(call: Call) = calls.contains(call)
	operator fun contains(contact: Contact) = calls.any {(it[CONTACT_ID] ?: 0L) == contact.contactId}
	
	fun getCalls(contact: Contact) = calls.filter {
		val id = it[CONTACT_ID] ?: 0L
		id.isNotZero && id == contact.contactId
	}
	
	fun key(call: Call): String {
		val id: Long = call[CONTACT_ID] ?: 0L
		return if (id.isNotZero) id.toString() else call.number.formatted
	}
}