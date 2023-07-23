package com.tr.hsyn.telefonrehberi.main.data;


import androidx.annotation.IntDef;


@IntDef(value = {
		CallLog.FILTER_ALL,
		CallLog.FILTER_INCOMING,
		CallLog.FILTER_OUTGOING,
		CallLog.FILTER_MISSED,
		CallLog.FILTER_REJECTED,
		CallLog.FILTER_NO_NAMED,
		CallLog.FILTER_RANDOM
})
public @interface CallFilter {
}
