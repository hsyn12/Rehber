package tr.xyz.call

import tr.xyz.contact.Contact
import tr.xyz.kkey.CONTACT_ID

class CallLog(val calls: List<Call>) {
	
	fun getCalls(contact: Contact) = calls.filter {it.name == contact.name}
	
	operator fun contains(call: Call) = calls.contains(call)
	
	/**
	 * Returns a unique key for the given call.
	 *
	 * @param call the call
	 * @return the key
	 */
	private fun getKey(call: Call): String {
		
	}
	
	fun Call.key(): String {
		
		val id: Long = (this[CONTACT_ID] ?: 0L).return if (id != 0L) id.toString() else number.formatted
	}
}