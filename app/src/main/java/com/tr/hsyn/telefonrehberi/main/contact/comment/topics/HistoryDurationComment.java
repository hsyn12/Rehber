package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.util.Pair;
import android.view.View;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class HistoryDurationComment implements ContactComment {
	
	private final CallLog                  callLog = getCallLogs();
	private final Spanner                  comment = new Spanner("\n");
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	private       Contact                  contact;
	private       boolean                  isTurkish;
	
	
	@Override
	public boolean isTurkish() {
		
		return isTurkish;
	}
	
	@Override
	public Activity getActivity() {
		
		return activity;
	}
	
	@Override
	public @NotNull CharSequence getComment() {
		
		return comment;
	}
	
	@Override
	public Topic getTopic() {
		
		return Topic.HISTORY_DURATION;
	}
	
	@Override
	public void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish) {
		
		// region Initialization
		this.contact   = contact;
		this.callback  = callback;
		this.activity  = activity;
		this.isTurkish = isTurkish;
		
		if (callLog == null) {
			
			xlog.d(getString(R.string.call_collection_is_null));
			returnComment();
			return;
		}
		// endregion
		
		//+ The list that has the duration of each contact.
		//+ And the order is by descending of the duration.
		List<Map.Entry<Contact, DurationGroup>> durationList = createContactHistoryDurationList(CallLog.getContactsWithNumber());
		//+ First item of this list is the winner.
		//+ Possibly there are durations with the same or so close with each other.
		
		
		//+ The object that to convert the duration to string.
		DurationGroup.Stringer stringer = DurationGroup.Stringer.builder()
				.formatter(duration1 -> fmt("%d %s", duration1.getValue(), makePlural(duration1.getUnit().toString(), duration1.getValue())))//_ the formatted string to be used.
				.units(Unit.YEAR, Unit.MONTH, Unit.DAY)//_ the units should be used.
				.zeros(false);//_ the zero durations should not be used.
		//+ The duration of this contact.
		DurationGroup thisDuration = CallLog.getDuration(durationList, contact);
		
		if (thisDuration == null) {
			
			xlog.d("Could not accessed the 'history duration' of this contact.");
			returnComment();
			return;
		}
		
		Map.Entry<Contact, DurationGroup> longestDurationItem = durationList.get(0);
		Contact                           winnerContact       = longestDurationItem.getKey();
		List<MostDurationData>            durationDataList    = createMostHistoryDurationList(durationList, stringer);
		String                            title               = getString(R.string.title_most_call_history);
		String                            subtitle            = getString(R.string.size_contacts, durationDataList.size());
		MostDurationDialog                dialog              = new MostDurationDialog(getActivity(), durationDataList, title, subtitle);
		View.OnClickListener              listener            = view -> dialog.show();
		String                            durationString      = stringer.durations(thisDuration.getDurations()).toString();//+ Duration string of this contact.
		int                               rank                = CallLog.getRank(durationList, contact);
		boolean                           winner              = contact.getContactId() == winnerContact.getContactId();
		
		if (isTurkish) {
			
			comment.append("Kişinin arama geçmişi süresi ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(". Rehberdeki diğer kişilerle karşılaştırıldığında ")
					.append("arama geçmişi süreleri", getClickSpans(listener))
					.append(" listesinin ");
			
			if (winner) comment.append("ilk sırasında yer aldığı görülüyor. ");
			else comment.append(fmt("%d. sırasında yer aldığı görülüyor. ", rank));
		}
		else {
			
			comment.append("This contact has ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(" call history. ")
					.append("Compared to others in the contacts ");
			
			if (winner) {
				
				comment.append("he/she appears in the first");
			}
			else {
				
				comment.append(fmt("he/she appears in the %d.", rank));
			}
			
			comment.append(" order in the ")
					.append("call history durations", getClickSpans(listener))
					.append(" list. ");
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
	
	/**
	 * Groups the contacts according to the history duration difference between.
	 *
	 * @param durationList the contact-duration list
	 * @return the map that grouped by history duration difference.
	 * 		Key is the name of group,
	 * 		value is the contact list that belongs to this group.
	 * 		Group names (keys) are integer and start from zero and advanced by 1.
	 * 		The most valuable group is zero and advances to less valuable one by one.
	 */
	private @NotNull Map<Integer, List<Contact>> groupContactsByDifference(@NotNull List<Map.Entry<Contact, DurationGroup>> durationList) {
		
		long longest          = durationList.remove(0).getValue().getTotalDurationAs(Unit.MILLISECOND).getValue();
		int  durationListSize = durationList.size();
		//+ index --> [contact --> duration difference]
		Map<Integer, Pair<Contact, Long>> differenceIndices = new HashMap<>();
		
		//+ set the differences
		for (int i = 0; i < durationListSize; i++) {
			
			Map.Entry<Contact, DurationGroup> entry           = durationList.get(i);
			long                              currentDuration = entry.getValue().getTotalDurationAs(Unit.MILLISECOND).getValue();
			differenceIndices.put(i, new Pair<>(entry.getKey(), Math.abs(longest - currentDuration)));
			longest = currentDuration;
		}
		
		//noinspection SimplifyStreamApiCallChains
		List<Map.Entry<Integer, Pair<Contact, Long>>> differenceList = differenceIndices.entrySet()
				.stream()
				.sorted(Comparator.comparingInt(Map.Entry::getKey))
				.collect(Collectors.toList());
		
		Long minDifference    = differenceList.stream().map(Map.Entry::getValue).map(p -> p.second).min(Long::compareTo).orElse(0L);
		Long maxDifference    = differenceList.stream().map(Map.Entry::getValue).map(p -> p.second).max(Long::compareTo).orElse(0L);
		long middleDifference = (minDifference + maxDifference) / 2;
		//+ group number --> [contact list] This is the difference group
		Map<Integer, List<Contact>> group = new HashMap<>();
		//+ group name of each difference group
		int groupCounter = 0;
		
		//+ Group call histories by differences between
		for (int i = 0; i < differenceList.size(); i++) {
			
			//+ index --> [contact --> difference]
			Map.Entry<Integer, Pair<Contact, Long>> entry      = differenceList.get(i);
			Pair<Contact, Long>                     difference = entry.getValue();
			
			if (difference.second >= middleDifference) {
				groupCounter++;
			}
			
			List<Contact> g = group.computeIfAbsent(groupCounter, j -> new ArrayList<>());
			g.add(difference.first);
		}
		
		return group;
	}
	
	/**
	 * Creates a list of duration items.
	 *
	 * @param durationList the list of duration
	 * @return the list of most duration items
	 */
	@NotNull
	private List<MostDurationData> createMostHistoryDurationList(@NotNull List<Map.Entry<Contact, DurationGroup>> durationList, @NotNull DurationGroup.Stringer stringer) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int order = 1;
		for (Map.Entry<Contact, DurationGroup> entry : durationList) {
			
			DurationGroup duration = entry.getValue();
			
			if (duration.isZero()) continue;
			
			Contact          contact        = entry.getKey();
			String           durationString = stringer.durations(duration.getDurations()).toString();
			MostDurationData data           = new MostDurationData(contact.getName(), durationString, order++);
			
			if (this.contact.getContactId() == contact.getContactId()) data.setSelected(true);
			
			list.add(data);
		}
		
		
		return list;
	}
	
	/**
	 * Creates a list that consists of calculated history duration of each contact.
	 *
	 * @param contacts the list of contacts
	 * @return the list of contact history duration ordered by history duration in descending order.
	 * 		The first item has the most history duration.
	 */
	@NotNull
	private List<Map.Entry<Contact, DurationGroup>> createContactHistoryDurationList(@NotNull List<Contact> contacts) {
		
		List<Map.Entry<Contact, DurationGroup>> durationList = new ArrayList<>();
		
		for (Contact contact : contacts) {
			
			assert callLog != null;
			History history = callLog.getHistoryOf(contact);
			
			if (history.isEmpty()) {
				
				//xlog.d("%s has no history", contact.getName());
				continue;
			}
			
			DurationGroup historyDuration = history.getHistoryDuration();
			
			if (!historyDuration.isZero())
				durationList.add(Map.entry(contact, historyDuration));
		}
		
		//_ Comparing by value makes the list in ascending order 
		durationList.sort(Map.Entry.comparingByValue());
		Collections.reverse(durationList);// descending order
		
		return durationList;
	}
}
