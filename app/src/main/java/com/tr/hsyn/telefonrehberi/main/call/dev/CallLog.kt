package com.tr.hsyn.telefonrehberi.main.call.dev

import com.tr.hsyn.telefonrehberi.main.call.data.contactId
import tr.xyz.call.Call
import tr.xyz.contact.Contact

/**
 * Call log
 *
 * @property calls calls
 * @constructor Create new Call log
 */
class CallLog(val calls: List<Call>) {

	/**
	 * Call map by contact ID
	 */
	private val callMap = calls.groupBy(this::key)
	/**
	 * Incoming calls
	 */
	val incomingCalls = calls.filter(Call::isIncoming)
	/**
	 * Outgoing calls
	 */
	val outgoingCalls = calls.filter(Call::isOutgoing)
	/**
	 * Missed calls
	 */
	val missedCalls = calls.filter(Call::isMissed)
	/**
	 * Rejected calls
	 */
	val rejectedCalls = calls.filter(Call::isRejected)
	/**
	 * Blocked calls
	 */
	val blockedCalls = calls.filter(Call::isBlocked)

	/**
	 * Gets calls of the given contact ID.
	 *
	 * @param id contact ID
	 * @return calls of the given contact ID
	 */
	operator fun get(id: String): List<Call>? = callMap[id]

	/**
	 * Gets calls of the given contact ID.
	 *
	 * @param id contact ID
	 * @return calls of the given contact ID
	 */
	operator fun get(id: Long): List<Call>? = callMap[id.toString()]

	/**
	 * Gets calls of the given contact.
	 *
	 * @param contact contact
	 * @return calls of the given contact
	 */
	operator fun get(contact: Contact): List<Call>? = callMap[contact.contactId.toString()]

	/**
	 * Checks if the given call exists.
	 *
	 * @param call call
	 */
	operator fun contains(call: Call) = calls.contains(call)

	/**
	 * Checks if the given contact has any call.
	 *
	 * @param contact contact
	 */
	operator fun contains(contact: Contact) = calls.any {(it.contactId) == contact.contactId}

	/**
	 * Returns all calls of the given contact.
	 *
	 * @param contact contact
	 * @return calls of the given contact
	 */
	fun getCalls(contact: Contact) = calls.filter {it.contactId == contact.contactId}

	/**
	 * Returns the key for the given call.
	 *
	 * @param call call
	 * @return the key
	 */
	fun key(call: Call): String = if (call.contactId != 0L) call.contactId.toString() else call.number.formatted

}