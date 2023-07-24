package com.tr.hsyn.calldata;


import androidx.annotation.IntDef;


/**
 * Defines the call types constants.
 */
@IntDef(value = {
		Call.INCOMING,
		Call.OUTGOING,
		Call.MISSED,
		Call.REJECTED,
		Call.BLOCKED,
		Call.INCOMING_WIFI,
		Call.OUTGOING_WIFI,
		Call.UNKNOWN
})
public @interface CallType {}
