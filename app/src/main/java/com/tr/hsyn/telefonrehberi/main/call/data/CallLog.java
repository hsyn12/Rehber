package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterMostType;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterType;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Holds the call logs and provides methods for filtering, searching and analyzing.
 */
public interface CallLog extends CCollection, Ranker {
	
	/**
	 * The filter for All calls.
	 */
	int FILTER_ALL           = 0;
	/**
	 * The filter for Incoming calls.
	 */
	int FILTER_INCOMING      = 1;
	/**
	 * The filter for Outgoing calls.
	 */
	int FILTER_OUTGOING      = 2;
	/**
	 * The filter for missed calls
	 */
	int FILTER_MISSED        = 3;
	/**
	 * The filter for rejected calls
	 */
	int FILTER_REJECTED      = 4;
	/**
	 * The filter for no-named calls
	 */
	int FILTER_NO_NAMED      = 5;
	/**
	 * The filter for random calls
	 */
	int FILTER_RANDOM        = 6;
	/**
	 * The filter for most incoming
	 */
	int FILTER_MOST_INCOMING = 7;
	/**
	 * The filter for most outgoing
	 */
	int FILTER_MOST_OUTGOING = 8;
	/**
	 * The filter for most missed
	 */
	int FILTER_MOST_MISSED   = 9;
	/**
	 * The filter for most rejected
	 */
	int FILTER_MOST_REJECTED = 10;
	/**
	 * The filter for most speaking (incoming)
	 */
	int FILTER_MOST_SPEAKING = 11;
	/**
	 * The filter for most talking (outgoing)
	 */
	int FILTER_MOST_TALKING  = 12;
	
	/**
	 * Returns the calls of the contact.
	 *
	 * @param contact   the contact
	 * @param callTypes the call types to select
	 * @return the calls
	 */
	@NotNull
	List<Call> getCalls(@NotNull Contact contact, int @NotNull ... callTypes);
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	@NotNull
	List<Call> getById(long contactId);
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	@NotNull List<Call> getById(String contactId);
	
	/**
	 * @return new rank map for incoming calls
	 */
	@Override
	@NotNull
	default RankMap incomingRank() {
		
		return new RankMap(rankByQuantity(incomingCalls()));
	}
	
	/**
	 * @return new rank map for outgoing calls
	 */
	@Override
	@NotNull
	default RankMap outgoingRank() {
		
		return new RankMap(rankByQuantity(outgoingCalls()));
	}
	
	/**
	 * @return new rank map for missed calls
	 */
	@Override
	@NotNull
	default RankMap missedRank() {
		
		return new RankMap(rankByQuantity(missedCalls()));
	}
	
	/**
	 * @return new rank map for rejected calls
	 */
	@Override
	@NotNull
	default RankMap rejectedRank() {
		
		return new RankMap(rankByQuantity(rejectedCalls()));
	}
	
	/**
	 * @return new rank map for incoming calls, which have speaking duration
	 */
	@Override
	@NotNull
	default RankMap incomingDurationRank() {
		
		return new RankMap(rankByDuration(incomingCalls()));
	}
	
	/**
	 * @return new rank map for outgoing calls, which have speaking duration
	 */
	@Override
	@NotNull
	default RankMap outgoingDurationRank() {
		
		List<Call> outs = outgoingCalls().stream().filter(Call::isSpoken).collect(Collectors.toList());
		return new RankMap(rankByDuration(outs));
	}
	
	/**
	 * Checks if the list is not empty.
	 *
	 * @param list the list
	 * @param <T>  the type of the list
	 * @return {@code true} if the list is not empty
	 */
	default <T> boolean isNotEmpty(@NotNull List<T> list) {
		
		return !list.isEmpty();
	}
	
	/**
	 * Returns the history of the contact.
	 *
	 * @param contact the contact
	 * @return the history
	 * @see History
	 */
	@NotNull
	default History getHistoryOf(@NotNull Contact contact) {
		
		return History.of(contact, getById(contact.getContactId()));
	}
	
	/**
	 * Returns the calls by the filter.
	 *
	 * @param filter the filter
	 * @return the calls
	 * @see FilterType
	 */
	@NotNull
	default List<Call> getCalls(@FilterType int filter) {
		
		//@off
		switch (filter) {
			case FILTER_ALL:      return getCalls();
			case FILTER_INCOMING: return incomingCalls();
			case FILTER_OUTGOING: return outgoingCalls();
			case FILTER_MISSED:   return missedCalls();
			case FILTER_REJECTED: return rejectedCalls();
			case FILTER_NO_NAMED: return getCalls(Call::isNoNamed);
			case FILTER_RANDOM:   return getCalls(Call::isRandom);
			default:              throw new IllegalArgumentException("Invalid filter: " + filter);
		}//@on
	}
	
	/**
	 * Returns a rank map for the filter.
	 *
	 * @param filter the filter
	 * @return the rank map
	 */
	@NotNull
	default RankMap getMostCalls(@FilterMostType int filter) {
		
		switch (filter) {
			case FILTER_MOST_INCOMING: return incomingRank();
			case FILTER_MOST_OUTGOING: return outgoingRank();
			case FILTER_MOST_MISSED: return missedRank();
			case FILTER_MOST_REJECTED: return rejectedRank();
			case FILTER_MOST_SPEAKING: return incomingDurationRank();
			case FILTER_MOST_TALKING: return outgoingDurationRank();
			default: throw new IllegalArgumentException("Invalid filter: " + filter);
		}
	}
	
	static boolean isCallTypeFilter(int filter) {
		
		
		return
		
		
	}
	
	/**
	 * Returns the contacts that have or have no calls.<br><br>
	 * Get all the contacts that have incoming calls,
	 * but have no outgoing and rejected calls,<br>
	 * <pre>getContacts(true, false, null, false, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that having only rejected calls,<br>
	 * <pre>getContacts(false, false, false, true, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that have incoming calls,<br>
	 * <pre>getContacts(true, null, null, null, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that have only incoming calls,<br>
	 * <pre>getContacts(true, false, false, false, 1);</pre>
	 * <br>
	 *
	 * @param incoming have calls or not. {@code null} if not specified.
	 * @param outgoing have calls or not. {@code null} if not specified.
	 * @param missed   have calls or not. {@code null} if not specified.
	 * @param rejected have calls or not. {@code null} if not specified.
	 * @param minSize  the minimum number of calls. The number greater than zero means does not select empty calls.
	 * @return the contacts that match the all criteria together or empty list if no matches.
	 */
	default @NotNull List<Contact> selectContacts(@Nullable Boolean incoming, @Nullable Boolean outgoing, @Nullable Boolean missed, @Nullable Boolean rejected, int minSize) {
		
		List<Contact> contacts = MainContacts.getWithNumber();
		
		if (contacts.isEmpty()) return new ArrayList<>();
		
		List<Contact> _contacts = new ArrayList<>();
		
		for (Contact contact : contacts) {
			
			Predicate<List<Call>> ip = null;
			CallLog               il = null;
			Predicate<List<Call>> op = null;
			CallLog               ol = null;
			Predicate<List<Call>> mp = null;
			CallLog               ml = null;
			Predicate<List<Call>> rp = null;
			CallLog               rl = null;
			Boolean               ir = null;
			Boolean               or = null;
			Boolean               mr = null;
			Boolean               rr = null;
			
			if (incoming != null) {
				ip = incoming ? this::isNotEmpty : List::isEmpty;
				ip = ip.and(c -> c.size() >= minSize);
				il = create(incomingCalls());
			}
			if (outgoing != null) {
				op = outgoing ? this::isNotEmpty : List::isEmpty;
				op = op.and(c -> c.size() >= minSize);
				ol = create(outgoingCalls());
			}
			if (missed != null) {
				mp = missed ? this::isNotEmpty : List::isEmpty;
				mp = mp.and(c -> c.size() >= minSize);
				ml = create(missedCalls());
			}
			if (rejected != null) {
				rp = rejected ? this::isNotEmpty : List::isEmpty;
				rp = rp.and(c -> c.size() >= minSize);
				rl = create(rejectedCalls());
			}
			
			if (ip != null) ir = ip.test(il.getCalls(contact));
			if (op != null) or = op.test(ol.getCalls(contact));
			if (mp != null) mr = mp.test(ml.getCalls(contact));
			if (rp != null) rr = rp.test(rl.getCalls(contact));
			
			if ((ir != null ? ir : true) &&
			    (or != null ? or : true) &&
			    (mr != null ? mr : true) &&
			    (rr != null ? rr : true))
				_contacts.add(contact);
		}
		
		return _contacts;
		
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	default @NotNull List<Contact> getContactsByCalls(@NotNull Predicate<@NotNull List<Call>> predicate) {
		
		return MainContacts.getWithNumber().stream()
				.filter(contact -> predicate.test(getCalls(contact)))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns a rank map by quantity.
	 *
	 * @return the rank map
	 */
	default RankMap rankByQuantity() {
		
		return rankByQuantity(getCalls());
	}
	
	/**
	 * Returns a rank map by duration.
	 *
	 * @return the rank map
	 */
	default RankMap rankByDuration() {
		
		return rankByDuration(getCalls());
	}
	
	
	/**
	 * Creates a new call log.
	 *
	 * @param calls the calls
	 * @return the call log
	 */
	@NotNull
	static CallLog createGlobal(List<Call> calls) {
		
		CallLog collection = new CallLogger(calls);
		
		Blue.box(com.tr.hsyn.key.Key.CALL_LOG, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new call log.
	 *
	 * @param calls the calls
	 * @return new call log
	 */
	@NotNull
	static CallLog create(List<Call> calls) {
		
		return new CallLogger(calls);
	}
	
	/**
	 * Returns the duration of the contact.
	 *
	 * @param durations the list of entry of contact to duration
	 * @param contact   the contact
	 * @return the duration
	 */
	@Nullable
	static DurationGroup getDuration(@NotNull List<Map.Entry<Contact, DurationGroup>> durations, Contact contact) {
		
		if (contact == null) return null;
		
		return durations.stream()
				.filter(e -> e.getKey().getContactId() == contact.getContactId())
				.findFirst().map(Map.Entry::getValue)
				.orElse(null);
	}
	
	/**
	 * Returns a unique key for the given call.
	 *
	 * @param call the call
	 * @return the key
	 */
	@NotNull
	static String getKey(@NotNull Call call) {
		
		long id = call.getLong(Key.CONTACT_ID, 0L);
		
		if (id != 0L) return id + "";
		
		return PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.MINIMUM_NUMBER_LENGTH);
	}
	
	/**
	 * Returns the call types for the given call type.
	 * {@link Type#INCOMING} and {@link Type#INCOMING_WIFI} are same types.
	 * {@link Type#OUTGOING} and {@link Type#OUTGOING_WIFI} are same types.
	 * So, if the given call type is {@link Type#MISSED} or {@link Type#REJECTED},
	 * this method returns {@link Type#MISSED} or {@link Type#REJECTED}.
	 *
	 * @param callType the call type
	 * @return the call types
	 * @see Type
	 */
	@CallType
	static int @NotNull [] getCallTypes(@CallType int callType) {
		
		if (callType == Call.MISSED || callType == Call.REJECTED) return new int[]{callType};
		
		switch (callType) {
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return new int[]{Call.OUTGOING, Call.OUTGOING_WIFI};
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return new int[]{Call.INCOMING, Call.INCOMING_WIFI};
			default: throw new IllegalArgumentException("Unknown call type: " + callType);
		}
	}
	
	/**
	 * Returns the valid call types.
	 *
	 * @return the valid call types
	 */
	static int @NotNull [] getCallTypes() {
		
		return new int[]{
				Call.INCOMING,
				Call.INCOMING_WIFI,
				Call.OUTGOING,
				Call.OUTGOING_WIFI,
				Call.MISSED,
				Call.REJECTED
		};
	}
}
