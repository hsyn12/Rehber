package com.tr.hsyn.telefonrehberi.main.data;


import androidx.annotation.IntDef;


@IntDef({
		CallLog.FILTER_MOST_INCOMING,
		CallLog.FILTER_MOST_OUTGOING,
		CallLog.FILTER_MOST_MISSED,
		CallLog.FILTER_MOST_REJECTED,
		CallLog.FILTER_MOST_SPEAKING,
		CallLog.FILTER_MOST_TALKING
})
public @interface MostFilter {
}
