package tr.xyz.call

import androidx.annotation.IntDef

@IntDef(
	Type.INCOMING, Type.INCOMING_WIFI,
	Type.OUTGOING, Type.OUTGOING_WIFI,
	Type.MISSED,
	Type.REJECTED,
	Type.BLOCKED,
	Type.UNREACHED,
	Type.UNRECEIVED,
	Type.GET_REJECTED,
	Type.UNKNOWN)
annotation class CallType

object Type {

	//region Call Types
	/**
	 * Incoming call type
	 */
	const val INCOMING = 1
	/**
	 * Outgoing call type
	 */
	const val OUTGOING = 2
	/**
	 * Missed a call type
	 */
	const val MISSED = 3
	/**
	 * Rejected call type
	 */
	const val REJECTED = 5
	/**
	 * Blocked call type
	 */
	const val BLOCKED = 6
	/**
	 * Incoming internet call type
	 */
	const val INCOMING_WIFI = 1000
	/**
	 * Outgoing internet call type
	 */
	const val OUTGOING_WIFI = 1001
	/**
	 * Unreached call type
	 */
	const val UNREACHED = 9001
	/**
	 * Unreceived call type
	 */
	const val UNRECEIVED = 9002
	/**
	 * Get rejected call type
	 */
	const val GET_REJECTED = 9003
	/**
	 * Unknown call type
	 */
	const val UNKNOWN = 545454
	//endregion



}


