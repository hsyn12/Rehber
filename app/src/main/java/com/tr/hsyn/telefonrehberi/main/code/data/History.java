package com.tr.hsyn.telefonrehberi.main.code.data;

import android.annotation.SuppressLint;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tr.xyz.contact.Contact;


/**
 * Defines the history of a contact.
 * History means all calls of a contact.
 */
public final class History {

	private final Contact    contact;
	private final List<Call> calls;
	private final List<Call> incoming;
	private final List<Call> outgoing;
	private final List<Call> missed;
	private final List<Call> rejected;
	private final int        incomingDuration;
	private final int        outgoingDuration;

	public History(@NotNull Contact contact, @NotNull List<Call> calls) {

		this.contact  = contact;
		this.calls    = calls;
		this.incoming = calls.stream().filter(c -> c.getCallType() == Call.INCOMING || c.getCallType() == Call.INCOMING_WIFI).collect(Collectors.toList());
		this.outgoing = calls.stream().filter(c -> c.getCallType() == Call.OUTGOING || c.getCallType() == Call.OUTGOING_WIFI).collect(Collectors.toList());
		this.missed   = calls.stream().filter(c -> c.getCallType() == Call.MISSED).collect(Collectors.toList());
		this.rejected = calls.stream().filter(c -> c.getCallType() == Call.REJECTED).collect(Collectors.toList());

		this.incomingDuration = (int) incoming.stream().mapToLong(Call::getDuration).sum();
		this.outgoingDuration = (int) outgoing.stream().filter(Call::isSpoken).mapToLong(Call::getDuration).sum();
	}

	@Override
	public int hashCode() {

		return Objects.hash(contact.getContactId());
	}

	@Override
	public boolean equals(Object obj) {

		return obj instanceof Contact o && contact.getContactId() == o.getContactId();
	}

	@SuppressLint("DefaultLocale")
	@Override
	public @NotNull String toString() {

		return String.format("History{contact=%s, calls=%d}", contact, calls.size());
	}

	/**
	 * Returns the contact that this history belongs to.
	 *
	 * @return the contact
	 */
	public @NotNull Contact getContact() {

		return contact;
	}

	/**
	 * Returns all calls that between the contact and the user.
	 *
	 * @return the calls
	 */
	@NotNull
	public List<Call> getCalls() {

		return calls;
	}

	/**
	 * Returns the incoming calls of the contact that related to this history object.
	 *
	 * @return the incoming calls
	 */
	public List<Call> getIncomingCalls() {

		return incoming;
	}

	/**
	 * Returns the outgoing calls of the contact that related to this history object.
	 *
	 * @return the outgoing calls
	 */
	public List<Call> getOutgoingCalls() {

		return outgoing;
	}

	/**
	 * Returns the missed calls of the contact that related to this history object.
	 *
	 * @return the missed calls
	 */
	public List<Call> getMissedCalls() {

		return missed;
	}

	/**
	 * Returns the rejected calls of the contact that related to this history object.
	 *
	 * @return the rejected calls
	 */
	public List<Call> getRejectedCalls() {

		return rejected;
	}

	/**
	 * Returns the total incoming call duration.
	 *
	 * @return the duration
	 */
	public int getIncomingDuration() {

		return incomingDuration;
	}

	/**
	 * Returns the total outgoing call duration.
	 *
	 * @return the duration
	 */
	public int getOutgoingDuration() {

		return outgoingDuration;
	}

	/**
	 * Returns the total duration.
	 *
	 * @return the duration
	 */
	public int getTotalDuration() {

		return incomingDuration + outgoingDuration;
	}

	/**
	 * Returns the call at the given index
	 *
	 * @param index the index
	 *
	 * @return the call
	 */
	public Call get(int index) {

		return getCalls().get(index);
	}

	/**
	 * Returns the duration of the call history.
	 *
	 * @return the {@link DurationGroup}
	 */
	public DurationGroup getHistoryDuration() {

		if (size() > 1) {

			Call firstCall     = getFirstCall();
			Call lastCall      = getLastCall();
			long estimatedTime = lastCall.getTime() - firstCall.getTime();
			return Time.toDuration(estimatedTime);
		}

		return DurationGroup.EMPTY;
	}

	/**
	 * Returns the size of the call history of the contact.
	 *
	 * @return the size of the call history
	 */
	public int size() {

		return getCalls().size();
	}

	/**
	 * Returns the oldest call.
	 *
	 * @return the first call
	 */
	public Call getFirstCall() {

		return getCalls().get(size() - 1);
	}

	/**
	 * Returns the most recent call.
	 *
	 * @return the most recent call
	 */
	public Call getLastCall() {

		return getCalls().get(0);
	}

	/**
	 * Sorts the history.
	 *
	 * @param comparator the comparator
	 */
	public void sort(Comparator<Call> comparator) {

		getCalls().sort(comparator);
	}

	/**
	 * Returns the calls of the contact that related to this history object by the given call types.
	 *
	 * @param types the call types
	 *
	 * @return the calls
	 */
	public @NotNull List<Call> getCalls(int @NotNull ... types) {

		return getCalls().stream().filter(c -> Lister.IntArray.contains(types, c.getCallType())).collect(Collectors.toList());
	}

	/**
	 * Returns all calls of the contact that related to this history object by matching the given predicate.
	 *
	 * @param predicate the predicate
	 *
	 * @return the calls
	 */
	public @NotNull List<Call> getCalls(@NotNull Predicate<Call> predicate) {

		return getCalls().stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Returns the size of the given call type.
	 *
	 * @param callType the call type
	 *
	 * @return the size
	 */
	public int size(@CallType int callType) {

		int[] types = CallLog.getCallTypes(callType);

		if (types.length == 1) {

			return (int) getCalls().stream().filter(call -> call.getCallType() == callType).count();
		}

		return (int) getCalls().stream().filter(call -> call.getCallType() == callType || call.getCallType() == types[ 1 ]).count();
	}

	/**
	 * Returns whether the call history is empty.
	 *
	 * @return {@code true} if the call history is empty
	 */
	public boolean isEmpty() {

		return getCalls().isEmpty();
	}

	/**
	 * Creates a new history for the given contact.
	 *
	 * @param contact the contact
	 *
	 * @return the history
	 */
	public static History getHistory(@NotNull Contact contact) {

		CallLog collection = Blue.getObject(Key.CALL_LOG);

		if (collection == null) return ofEmpty(contact);

		List<Call> calls = collection.getById(contact.getContactId());

		return of(contact, calls);
	}

	/**
	 * Creates a new empty history for the given contact.
	 *
	 * @param contact the contact used by this history
	 *
	 * @return the history for the given contact
	 */
	@NotNull
	public static History ofEmpty(@NotNull Contact contact) {

		return new History(contact, new ArrayList<>(0));
	}

	/**
	 * Creates a new history for the given contact.
	 *
	 * @param contact the contact used by this history
	 *
	 * @return the history for the given contact
	 */
	public static @NotNull History of(@NotNull Contact contact, @NotNull List<Call> calls) {

		return new History(contact, calls);
	}

	/**
	 * Returns the calls of the given call types from the given list of calls.
	 *
	 * @param calls the calls
	 * @param types the call types
	 *
	 * @return the calls
	 */
	public static @NotNull List<Call> getCalls(@NotNull List<Call> calls, int @NotNull ... types) {

		List<Call> _calls = new ArrayList<>();

		for (int type : types) {

			for (Call call : calls) {

				if (type == call.getCallType() && !_calls.contains(call)) {

					_calls.add(call);
				}
			}
		}

		return _calls;
	}

	/**
	 * Comparator for the history
	 */
	public enum Comparing implements Comparator<History> {

		/**
		 * Compares the history with the incoming calls size for descending order.
		 */
		INCOMING,
		/**
		 * Compares the history with the outgoing calls size for descending order.
		 */
		OUTGOING,
		/**
		 * Compares the history with the missed calls size for descending order.
		 */
		MISSED,
		/**
		 * Compares the history with the rejected calls size for descending order.
		 */
		REJECTED,
		/**
		 * Compares the history with the total incoming duration for descending order.
		 */
		INCOMING_DURATION,
		/**
		 * Compares the history with the total outgoing duration for descending order.
		 */
		OUTGOING_DURATION,
		/**
		 * Compares the history with the total duration for descending order.
		 */
		TOTAL_DURATION;

		@Override
		public int compare(History o1, History o2) {

			if (o1 == null && o2 == null) return 0;
			if (o1 == null || o2 == null) return 1;

			switch (this) {
				//@off
				case INCOMING:          return o2.getIncomingCalls().size() - o1.getIncomingCalls().size();
				case OUTGOING:          return o2.getOutgoingCalls().size() - o1.getOutgoingCalls().size();
				case MISSED:            return o2.getMissedCalls().size() - o1.getMissedCalls().size();
				case REJECTED:          return o2.getRejectedCalls().size() - o1.getRejectedCalls().size();
				case INCOMING_DURATION: return o2.getIncomingDuration() - o1.getIncomingDuration();
				case OUTGOING_DURATION: return o2.getOutgoingDuration() - o1.getOutgoingDuration();
				case TOTAL_DURATION:    return o2.getTotalDuration() - o1.getTotalDuration();
			}//@on

			throw new IllegalArgumentException("Impossible value: " + this);
		}
	}
}
