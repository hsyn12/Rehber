package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;

import java.util.List;


public class CallRank {
	
	private final int        rank;
	private final String     key;
	private final List<Call> calls;
	
	public CallRank(int rank, String key, List<Call> calls) {
		
		this.rank  = rank;
		this.key   = key;
		this.calls = calls;
	}
	
	public int getRank() {
		
		return rank;
	}
	
	/**
	 * Key can be phone number, or contact id, or whatever else.
	 *
	 * @return the key
	 */
	public String getKey() {
		
		return key;
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	@Override
	public int hashCode() {
		
		return key.hashCode();
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		
		return obj instanceof CallRank && key.equals(((CallRank) obj).key);
	}
}
