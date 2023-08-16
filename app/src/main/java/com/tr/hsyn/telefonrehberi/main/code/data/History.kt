@file:Suppress("JoinDeclarationAndAssignment")

package com.tr.hsyn.telefonrehberi.main.code.data

import android.annotation.SuppressLint
import com.tr.hsyn.calldata.Call
import com.tr.hsyn.calldata.CallType
import com.tr.hsyn.collection.Lister
import com.tr.hsyn.key.Key
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog
import com.tr.hsyn.time.Time
import com.tr.hsyn.time.duration.DurationGroup
import com.tr.hsyn.xbox.Blue
import tr.xyz.contact.Contact
import java.util.*

/** Defines the history of a contact. History means all calls of a contact. */
class History(val contact: Contact, val calls: List<Call>) {

	/** Incoming calls of the contact that related to this history object. */
	val incomingCalls: List<Call>
	/** Outgoing calls of the contact that related to this history object. */
	val outgoingCalls: List<Call>
	/** Missed calls of the contact that related to this history object. */
	val missedCalls: List<Call>
	/** Rejected calls of the contact that related to this history object. */
	val rejectedCalls: List<Call>
	/** Total incoming call duration. */
	var incomingDuration = 0
	/** Total outgoing call duration. */
	var outgoingDuration = 0

	init {
		//@off
		incomingCalls    = calls.filter {c: Call -> c.callType == Call.INCOMING || c.callType == Call.INCOMING_WIFI}
		outgoingCalls    = calls.filter {c: Call -> c.callType == Call.OUTGOING || c.callType == Call.OUTGOING_WIFI}
		missedCalls      = calls.filter {c: Call -> c.callType == Call.MISSED}
		rejectedCalls    = calls.filter {c: Call -> c.callType == Call.REJECTED}
		incomingDuration = incomingCalls.sumOf {it.duration}
		outgoingDuration = outgoingCalls.sumOf {it.duration}
		//@on
	}

	override fun hashCode() = contact.id.hashCode()
	override fun equals(other: Any?) = other is Contact && contact.id == other.id

	@SuppressLint("DefaultLocale")
	override fun toString() = "History{$contact, ${calls.size}}"

	/**
	 * Returns the total duration.
	 *
	 * @return the duration
	 */
	val totalDuration get() = incomingDuration + outgoingDuration

	/**
	 * Returns the call at the given index
	 *
	 * @param index the index
	 * @return the call
	 */
	operator fun get(index: Int): Call = calls[index]
	/**
	 * Returns the size of the call history of the contact.
	 *
	 * @return the size of the call history
	 */
	val size: Int get() = calls.size
	/**
	 * Returns the oldest call.
	 *
	 * @return the first call
	 */
	val firstCall: Call get() = calls.first()
	/**
	 * Returns the most recent call.
	 *
	 * @return the most recent call
	 */
	val lastCall: Call get() = calls.last()

	/**
	 * Returns the duration of the call history.
	 *
	 * @return the [DurationGroup]
	 */
	val historyDuration: DurationGroup by lazy {if (size > 1) Time.toDuration(lastCall.time - firstCall.time) else DurationGroup.EMPTY}

	/**
	 * Returns the calls of the contact that related to this history object by
	 * the given call types.
	 *
	 * @param types the call types
	 * @return the calls
	 */
	fun filter(vararg types: Int) = calls.filter {Lister.IntArray.contains(types, it.callType)}

	/**
	 * Returns all calls of the contact that related to this history object by
	 * matching the given predicate.
	 *
	 * @param predicate the predicate
	 * @return the calls
	 */
	fun filter(predicate: (Call) -> Boolean) = calls.filter(predicate)

	/**
	 * Returns the size of the given call type.
	 *
	 * @param callType the call type
	 * @return the size
	 */
	fun size(@CallType callType: Int): Int {
		val types = CallLog.getCallTypes(callType)
		return if (types.size == 1) {
			filter().stream().filter {call: Call -> call.callType == callType}
				.count().toInt()
		}
		else filter().stream().filter {call: Call ->
			call.callType == callType || call.callType == types[1]
		}.count().toInt()
	}

	val isEmpty: Boolean get() = size == 0

	/**
	 * Creates a new history for the given contact.
	 *
	 * @param contact the contact
	 * @return the history
	 */
	fun getHistory(contact: Contact): History {
		val collection = Blue.getObject<CallLog>(Key.CALL_LOG) ?: return ofEmpty(contact)
		val calls: List<Call> = collection.getById(contact.id)
		return of(contact, calls)
	}

	companion object {

		/**
		 * Creates a new empty history for the given contact.
		 *
		 * @param contact the contact used by this history
		 * @return the history for the given contact
		 */
		fun ofEmpty(contact: Contact) = History(contact, ArrayList<Call>(0))

		/**
		 * Creates a new history for the given contact.
		 *
		 * @param contact the contact used by this history
		 * @return the history for the given contact
		 */
		fun of(contact: Contact, calls: List<Call>) = History(contact, calls)

	}



	/**
	 * Returns the calls of the given call types from the given list of calls.
	 *
	 * @param calls the calls
	 * @param types the call types
	 * @return the calls
	 */
	fun filter(calls: List<Call>, vararg types: Int): List<Call> {
		val _calls: MutableList<Call> = ArrayList()
		for (type in types) {
			for (call in calls) {
				if (type == call.callType && !_calls.contains(call)) {
					_calls.add(call)
				}
			}
		}
		return _calls
	}

	/** Comparator for the history. */
	enum class Comparing : Comparator<History?> {
		/** Compares the history with the incoming calls size for descending order. */
		INCOMING,
		/** Compares the history with the outgoing calls size for descending order. */
		OUTGOING,
		/** Compares the history with the missed calls size for descending order. */
		MISSED,
		/** Compares the history with the rejected calls size for descending order. */
		REJECTED,
		/**
		 * Compares the history with the total incoming duration for descending
		 * order.
		 */
		INCOMING_DURATION,
		/**
		 * Compares the history with the total outgoing duration for descending
		 * order.
		 */
		OUTGOING_DURATION,
		/** Compares the history with the total duration for descending order. */
		TOTAL_DURATION;

		override fun compare(o1: History?, o2: History?): Int {

			if (o1 == null && o2 == null) return 0
			if (o1 == null) return 1
			if (o2 == null) return 0

			return when (this) {
				INCOMING          -> o2.incomingCalls.size - o1.incomingCalls.size
				OUTGOING          -> o2.outgoingCalls.size - o1.outgoingCalls.size
				MISSED            -> o2.missedCalls.size - o1.missedCalls.size
				REJECTED          -> o2.rejectedCalls.size - o1.rejectedCalls.size
				INCOMING_DURATION -> o2.incomingDuration - o1.incomingDuration
				OUTGOING_DURATION -> o2.outgoingDuration - o1.outgoingDuration
				TOTAL_DURATION    -> o2.totalDuration - o1.totalDuration
			}
		}
	}
}
