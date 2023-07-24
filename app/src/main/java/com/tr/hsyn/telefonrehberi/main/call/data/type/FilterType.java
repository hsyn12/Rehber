package com.tr.hsyn.telefonrehberi.main.call.data.type;


import androidx.annotation.IntDef;

import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;


@IntDef(value = {
		CallLog.FILTER_ALL,
		CallLog.FILTER_INCOMING,
		CallLog.FILTER_OUTGOING,
		CallLog.FILTER_MISSED,
		CallLog.FILTER_REJECTED,
		CallLog.FILTER_NO_NAMED,
		CallLog.FILTER_RANDOM
})
public @interface FilterType {
}
