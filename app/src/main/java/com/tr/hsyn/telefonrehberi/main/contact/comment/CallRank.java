package com.tr.hsyn.telefonrehberi.main.contact.comment;


import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


/**
 * Defines rank information for a key or a contact.<br>
 * This information determines the rank of the key or the contact.<br>
 * This class is something like a {@link Map.Entry}.
 * It stores the key or the contact and the calls
 * that belong to the key or the contact.
 * And yes, a contact also is a key.
 * The other information is extra.
 * For example,
 * the rankCount ({@link #getRankCount()}) is the number of keys that has the same rank.
 */
public class CallRank {
	
	private final String     key;
	private final List<Call> calls;
	private       int        rank;
	private       long       incomingDuration;
	private       long       outgoingDuration;
	private       String     name;
	private       Contact    contact;
	private       int        rankCount;
	
	/**
	 * Creates a new call rank.
	 *
	 * @param key   the key. It must be unique.
	 * @param calls the calls
	 */
	public CallRank(@NotNull String key, @NotNull List<Call> calls) {
		
		this(0, key, calls);
	}
	
	/**
	 * Creates a new call rank.
	 *
	 * @param rank  the rank
	 * @param key   the key. It must be unique.
	 * @param calls the calls
	 */
	public CallRank(int rank, @NotNull String key, @NotNull List<Call> calls) {
		
		this.rank  = rank;
		this.key   = key;
		this.calls = calls;
		name       = calls.get(0).getName();
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
	
	/**
	 * @return number of keys those have the same rank
	 */
	public int getRankCount() {
		
		return rankCount;
	}
	
	public void setRankCount(int rankCount) {
		
		this.rankCount = rankCount;
	}
	
	/**
	 * @return the contact associated with this object or {@code null}.
	 */
	@Nullable
	public Contact getContact() {
		
		return contact;
	}
	
	public void setContact(Contact contact) {
		
		this.contact = contact;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	/**
	 * @return total speaking duration (incoming + outgoing)
	 */
	public long getDuration() {
		
		return incomingDuration + outgoingDuration;
	}
	
	/**
	 * @return the incoming speaking duration
	 */
	public long getIncomingDuration() {
		
		return incomingDuration;
	}
	
	public void setIncomingDuration(long incomingDuration) {
		
		this.incomingDuration = incomingDuration;
	}
	
	/**
	 * @return the outgoing speaking duration
	 */
	public long getOutgoingDuration() {
		
		return outgoingDuration;
	}
	
	public void setOutgoingDuration(long outgoingDuration) {
		
		this.outgoingDuration = outgoingDuration;
	}
	
	/**
	 * @return the rank
	 */
	public int getRank() {
		
		return rank;
	}
	
	public void setRank(int rank) {
		
		this.rank = rank;
	}
	
	/**
	 * Key can be phone number, or contact ID, or whatever else is unique.
	 *
	 * @return the key
	 */
	public String getKey() {
		
		return key;
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	public int size() {
		
		return calls.size();
	}
}
