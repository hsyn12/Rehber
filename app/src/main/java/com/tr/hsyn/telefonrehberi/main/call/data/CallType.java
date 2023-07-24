package com.tr.hsyn.telefonrehberi.main.call.data;


import androidx.annotation.IntDef;

import com.tr.hsyn.calldata.Call;


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
public @interface CallType {
}
