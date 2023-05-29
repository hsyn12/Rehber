package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


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
	
	/**
	 * Returns the call types for the given call type.
	 * {@link CallType#INCOMING} and {@link CallType#INCOMING_WIFI} are same types.
	 * {@link CallType#OUTGOING} and {@link CallType#OUTGOING_WIFI} are same types.
	 * So, if the given call type is {@link CallType#MISSED} or {@link CallType#REJECTED},
	 * this method returns {@link CallType#MISSED} or {@link CallType#REJECTED}.
	 *
	 * @param callType the call type
	 * @return the call types
	 * @see CallType
	 */
	@NonNull
	public static int[] getCallTypes(int callType) {
		
		List<Integer> types = new ArrayList<>();
		types.add(callType);
		
		//@off
		switch (callType) {
			case CallType.INCOMING: types.add(CallType.INCOMING_WIFI);break;
			case CallType.OUTGOING: types.add(CallType.OUTGOING_WIFI);break;
			case CallType.INCOMING_WIFI: types.add(CallType.INCOMING);break;
			case CallType.OUTGOING_WIFI: types.add(CallType.OUTGOING);break;
			default: break;
		}//@on
		
		return Lister.toIntArray(types);
	}
}
