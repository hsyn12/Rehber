package com.tr.hsyn.telefonrehberi.main.call.data.type;


import androidx.annotation.IntDef;

import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;


@IntDef({
		CallLog.FILTER_MOST_INCOMING,
		CallLog.FILTER_MOST_OUTGOING,
		CallLog.FILTER_MOST_MISSED,
		CallLog.FILTER_MOST_REJECTED,
		CallLog.FILTER_MOST_SPEAKING,
		CallLog.FILTER_MOST_TALKING
})
public @interface FilterMostType {
}
