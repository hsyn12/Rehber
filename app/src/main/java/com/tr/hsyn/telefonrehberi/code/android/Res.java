package com.tr.hsyn.telefonrehberi.code.android;


import android.content.Context;

import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;


public class Res {
	
	public static String getCallType(@NotNull Context context, int type) {
		
		switch (type) {
			
			case CallType.INCOMING:
			case CallType.INCOMING_WIFI: return context.getString(R.string.call_type_incoming);
			case CallType.OUTGOING:
			case CallType.OUTGOING_WIFI: return context.getString(R.string.call_type_outgoing);
			case CallType.MISSED: return context.getString(R.string.call_type_missed);
			case CallType.REJECTED: return context.getString(R.string.call_type_rejected);
			case CallType.BLOCKED: return context.getString(R.string.call_type_blocked);
			
			default: return "";
		}
	}
}
