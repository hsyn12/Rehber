package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;


public class Res {
	
	/**
	 * Returns the call type string for the given call type.
	 *
	 * @param context the context
	 * @param type    the call type
	 * @return the call type string
	 */
	@NonNull
	public static String getCallType(@NotNull Context context, int type) {
		
		switch (type) {
			
			case Type.INCOMING:
			case Type.INCOMING_WIFI: return context.getString(R.string.call_type_incoming);
			case Type.OUTGOING:
			case Type.OUTGOING_WIFI: return context.getString(R.string.call_type_outgoing);
			case Type.MISSED: return context.getString(R.string.call_type_missed);
			case Type.REJECTED: return context.getString(R.string.call_type_rejected);
			case Type.BLOCKED: return context.getString(R.string.call_type_blocked);
			
			default: return "";
		}
	}
	
	
}
