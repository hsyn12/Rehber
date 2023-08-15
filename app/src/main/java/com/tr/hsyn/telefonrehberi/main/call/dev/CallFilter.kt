package com.tr.hsyn.telefonrehberi.main.call.dev

import androidx.annotation.IntDef

object Filter {
	/** The filter for All calls. */
	const val FILTER_ALL = 0
	/** The filter for Incoming calls. */
	const val FILTER_INCOMING = 1
	/** The filter for Outgoing calls. */
	const val FILTER_OUTGOING = 2
	/** The filter for missed calls */
	const val FILTER_MISSED = 3
	/** The filter for rejected calls */
	const val FILTER_REJECTED = 4
	/** The filter for no-named calls */
	const val FILTER_NO_NAMED = 5
	/** The filter for random calls */
	const val FILTER_RANDOM = 6
	/** The filter for most incoming */
	const val FILTER_MOST_INCOMING = 7
	/** The filter for most outgoing */
	const val FILTER_MOST_OUTGOING = 8
	/** The filter for most missed */
	const val FILTER_MOST_MISSED = 9
	/** The filter for most rejected */
	const val FILTER_MOST_REJECTED = 10
	/** The filter for most speaking */
	const val FILTER_MOST_SPEAKING = 11
	/** The filter for most talking */
	const val FILTER_MOST_TALKING = 12
	/** The filter for most total speaking */
	const val FILTER_MOST_TOTAL_DURATION = 13

}

@IntDef(
	Filter.FILTER_ALL,
	Filter.FILTER_INCOMING,
	Filter.FILTER_OUTGOING,
	Filter.FILTER_MISSED,
	Filter.FILTER_REJECTED,
	Filter.FILTER_NO_NAMED,
	Filter.FILTER_RANDOM,
	Filter.FILTER_MOST_INCOMING,
	Filter.FILTER_MOST_OUTGOING,
	Filter.FILTER_MOST_MISSED,
	Filter.FILTER_MOST_REJECTED,
	Filter.FILTER_MOST_SPEAKING,
	Filter.FILTER_MOST_TALKING,
	Filter.FILTER_MOST_TOTAL_DURATION)
annotation class CallFilter