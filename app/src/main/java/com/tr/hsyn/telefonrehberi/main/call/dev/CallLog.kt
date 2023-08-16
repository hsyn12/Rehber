package com.tr.hsyn.telefonrehberi.main.call.dev

import com.tr.hsyn.telefonrehberi.main.call.data.contactId
import tr.xyz.call.Call
import tr.xyz.contact.Contact

/**
 * Call log
 *
 * @constructor Create new Call log
 * @property calls calls
 */
class CallLog(val calls: List<Call>) {

	/** Call map by contact ID. */
	private val callMap = calls.groupBy(CallLog::createKey)
	/** Blocked calls. */
	val blockedCalls = calls.filter(Call::isBlocked)
	/** Incoming calls. */
	val incomingCalls = calls.filter(Call::isIncoming)
	/** Missed calls. */
	val missedCalls = calls.filter(Call::isMissed)
	/** Outgoing calls. */
	val outgoingCalls = calls.filter(Call::isOutgoing)
	/** Rejected calls. */
	val rejectedCalls = calls.filter(Call::isRejected)

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
	 * Gets calls of the given `contact` ID.
	 *
	 * @param id contact ID
	 * @return calls of the given contact ID
	 */
	operator fun get(id: Long): List<Call> = callMap[id.toString()] ?: emptyList()
	/**
	 * Gets calls of the given contact.
	 *
	 * @param contact contact
	 * @return calls of the given contact
	 */
	operator fun get(contact: Contact): List<Call> = callMap[contact.contactId.toString()] ?: emptyList()
	/**
	 * Gets calls of the given contact ID.
	 *
	 * @param id contact ID
	 * @return calls of the given contact ID
	 */
	operator fun get(id: String): List<Call> = callMap[id] ?: emptyList()
	/**
	 * Returns all calls of the given contact.
	 *
	 * @param contact contact
	 * @return calls of the given contact
	 */
	fun getCalls(contact: Contact) = calls.filter {it.contactId == contact.contactId}
	/**
	 * Creates a [RankList] from the incoming calls by the duration.
	 *
	 * @return the rank list that is ranked by the duration descending order
	 */
	fun rankByIncomingSpeakingDuration(): RankList = rankBySpeakingDuration(incomingCalls.groupBy(
		CallLog::createKey))
	/**
	 * Creates a [RankList] from the outgoing calls by the duration.
	 *
	 * @return the rank list that is ranked by the duration descending order
	 */
	fun rankByOutgoingSpeakingDuration(): RankList = rankBySpeakingDuration(outgoingCalls.groupBy(
		CallLog::createKey))
	/**
	 * Creates a [RankList] from the call map by the count of the call.
	 *
	 * @param callMap optional call map to rank. If it is skipped, the default
	 *     call map (all calls) is used.
	 * @return the rank list that is ranked by the call size descending order.
	 *     Which one has the most call count is in the first rank.
	 */
	fun rankBySize(callMap: Map<String, List<Call>> = this.callMap): RankList {

		val rankList = mutableListOf<Rank>()
		val sortedList = callMap.entries.toList().sortedByDescending {it.value.size}

		var rank = 1

		for (i in sortedList.indices) {

			rankList.add(Rank(sortedList[i].key, rank))

			if (i == sortedList.size - 1) break

			val next = sortedList[i + 1]

			if (next.value.size < sortedList[i].value.size) rank++
		}

		return RankList(rankList)
	}
	/**
	 * Creates a [RankList] from the call map by the duration.
	 *
	 * @param callMap optional call map to rank. If it is skipped, the default
	 *     call map (all calls) is used.
	 * @return the rank list that is ranked by the duration descending order
	 */
	fun rankBySpeakingDuration(callMap: Map<String, List<Call>> = this.callMap): RankList {

		val rankList = mutableListOf<Rank>()

		for (entry: Map.Entry<String, List<Call>> in callMap.entries) {

			val duration = entry.value.sumOf {it.duration}

			rankList.add(Rank(key = entry.key, duration = duration, size = entry.value.size))
		}

		rankList.sortByDescending {it.duration}

		var rank = 1
		for (i in rankList.indices) {

			rankList[i].rank = rank

			if (i == rankList.size - 1) break

			val next = rankList[i + 1]

			if (next.duration < rankList[i].duration) rank++
		}

		return RankList(rankList)
	}

	companion object {

		/**
		 * Returns the unique key for the given call.
		 *
		 * @param call call
		 * @return the key
		 */
		// generate a code document in kotlin for the following code :
		fun createKey(call: Call): String = if (call.contactId != 0L) call.contactId.toString() else call.number.formatted


	}


}