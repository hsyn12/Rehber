package com.tr.hsyn.telefonrehberi.main.contact.comment;


import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CallRank {
	
	private final String     key;
	private final List<Call> calls;
	private final String     number;
	private       int        rank;
	private       long       incomingDuration;
	private       long       outgoingDuration;
	private       String     name;
	
	public CallRank(@NotNull String key, @NotNull List<Call> calls) {
		
		this(0, key, calls);
	}
	
	public CallRank(int rank, @NotNull String key, @NotNull List<Call> calls) {
		
		this.rank  = rank;
		this.key   = key;
		this.calls = calls;
		number     = calls.get(0).getNumber();
		name       = calls.get(0).getName();
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public long getDuration() {
		
		return incomingDuration + outgoingDuration;
	}
	
	public long getIncomingDuration() {
		
		return incomingDuration;
	}
	
	public void setIncomingDuration(long incomingDuration) {
		
		this.incomingDuration = incomingDuration;
	}
	
	public long getOutgoingDuration() {
		
		return outgoingDuration;
	}
	
	public void setOutgoingDuration(long outgoingDuration) {
		
		this.outgoingDuration = outgoingDuration;
	}
	
	public int getRank() {
		
		return rank;
	}
	
	public void setRank(int rank) {
		
		this.rank = rank;
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
	
	@NotNull
	@Override
	public String toString() {
		
		return "CallRank{" +
		       "rank=" + rank +
		       ", key='" + key + '\'' +
		       ", calls=" + calls.size() +
		       '}';
	}
}
