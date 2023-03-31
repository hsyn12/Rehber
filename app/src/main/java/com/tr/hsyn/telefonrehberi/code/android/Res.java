package com.tr.hsyn.telefonrehberi.code.android;


import android.content.Context;

import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;


public class Res {
	
	public static String getCallType(@NotNull Context context, int type) {
		
		switch (type) {
			
			case CallType.INCOMING:
			case CallType.INCOMING_WIFI: return context.getString(R.string.incomming_call);
			case CallType.OUTGOING:
			case CallType.OUTGOING_WIFI: return context.getString(R.string.outgoing_call);
			case CallType.MISSED: return context.getString(R.string.missed_call);
			case CallType.REJECTED: return context.getString(R.string.rejected_call);
			case CallType.BLOCKED: return context.getString(R.string.blocked_call);
			
			default: return "";
		}
	}
}
