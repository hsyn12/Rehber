package com.tr.hsyn.telefonrehberi.main.contact.comment;


import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
	private final List<Call> incomingCalls;
	private final List<Call> outgoingCalls;
	private final List<Call> missedCalls;
	private final List<Call> rejectedCalls;
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
		if (!calls.isEmpty())
			name = calls.get(0).getName();
		
		incomingCalls = calls.stream().filter(c -> Lister.contains(new int[]{Call.INCOMING, Call.INCOMING_WIFI}, c.getCallType())).collect(Collectors.toList());
		outgoingCalls = calls.stream().filter(c -> Lister.contains(new int[]{Call.OUTGOING, Call.OUTGOING_WIFI}, c.getCallType())).collect(Collectors.toList());
		missedCalls   = calls.stream().filter(Call::isMissed).collect(Collectors.toList());
		rejectedCalls = calls.stream().filter(Call::isRejected).collect(Collectors.toList());
	}
	
	/**
	 * Empty rank
	 */
	public CallRank() {
		
		key           = null;
		calls         = null;
		incomingCalls = null;
		outgoingCalls = null;
		missedCalls   = null;
		rejectedCalls = null;
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
	
	public List<Call> getIncomingCalls() {
		
		return incomingCalls;
	}
	
	public List<Call> getOutgoingCalls() {
		
		return outgoingCalls;
	}
	
	public List<Call> getMissedCalls() {
		
		return missedCalls;
	}
	
	public List<Call> getRejectedCalls() {
		
		return rejectedCalls;
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
	@NotNull
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
	
	public List<Call> getCalls(int... callTypes) {
		
		if (callTypes == null || callTypes.length == 0) return calls;
		
		if (callTypes.length == 1)
			return calls.stream().filter(c -> c.getCallType() == callTypes[0]).collect(Collectors.toList());
		
		return calls.stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.toList());
	}
	
	public int size() {
		
		return calls.size();
	}
	
	public History toHistory() {
		
		return History.of(contact, calls);
	}
	
	/**
	 * Compares the call ranks by incoming call size for descending order.
	 *
	 * @param other the other call rank to compare
	 * @return 0 if sizes equal, -1 if other has higher size, 1 if other has lower size
	 */
	public int compareToIncoming(@NotNull CallRank other) {
		
		return compareByType(other, Call.INCOMING);
	}
	
	/**
	 * Compares the call ranks by incoming call size for descending order.
	 *
	 * @param other the other call rank to compare
	 * @return 0 if sizes equal, -1 if other has higher size, 1 if other has lower size
	 */
	public int compareToOutgoing(@NotNull CallRank other) {
		
		return compareByType(other, Call.OUTGOING);
	}
	
	/**
	 * Compares the call ranks by missed call size for descending order.
	 *
	 * @param other the other call rank to compare
	 * @return 0 if sizes equal, -1 if other has higher size, 1 if other has lower size
	 */
	public int compareToMissed(@NotNull CallRank other) {
		
		return compareByType(other, Call.MISSED);
	}
	
	/**
	 * Compares the call ranks by rejected call size for descending order.
	 *
	 * @param other the other call rank to compare
	 * @return 0 if sizes equal, -1 if other has higher size, 1 if other has lower size
	 */
	public int compareToRejected(@NotNull CallRank other) {
		
		return compareByType(other, Call.REJECTED);
	}
	
	/**
	 * Compares the call ranks by size according to the call type for descending order.
	 *
	 * @param other    the other call rank to compare
	 * @param callType the call type
	 * @return 0 if sizes equal, -1 if other has higher size, 1 if other has lower rank
	 */
	public int compareByType(@NotNull CallRank other, int callType) {
		
		List<Call> typedCalls;
		List<Call> typedOtherCalls;
		
		switch (callType) {
			
			case Call.INCOMING:
				typedCalls = getCalls(Call.INCOMING, Call.INCOMING_WIFI);
				typedOtherCalls = other.getCalls(Call.INCOMING, Call.INCOMING_WIFI);
				break;
			case Call.OUTGOING:
				typedCalls = getCalls(Call.OUTGOING, Call.OUTGOING_WIFI);
				typedOtherCalls = other.getCalls(Call.OUTGOING, Call.OUTGOING_WIFI);
				break;
			case Call.MISSED:
				typedCalls = getCalls(Call.MISSED);
				typedOtherCalls = other.getCalls(Call.MISSED);
				break;
			case Call.REJECTED:
				typedCalls = getCalls(Call.REJECTED);
				typedOtherCalls = other.getCalls(Call.REJECTED);
				break;
			default: throw new IllegalArgumentException("Invalid call type: " + callType);
		}
		
		return typedOtherCalls.size() - typedCalls.size();
	}
	
	public int compareToIncomingDuration(@NotNull CallRank other) {
		
		return (int) (other.getIncomingDuration() - incomingDuration);
		
	}
	
	public int compareToOutgoingDuration(@NotNull CallRank other) {
		
		return (int) (other.getOutgoingDuration() - outgoingDuration);
	}
	
	public int compareToDuration(@NotNull CallRank other) {
		
		return (int) (other.getDuration() - getDuration());
	}
}
