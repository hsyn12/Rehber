package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.HistoryRanker;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class HistoryDurationComment implements ContactComment {
	
	private final CallCollection           callCollection = getCallCollection();
	private final Spanner                  comment        = new Spanner("\n");
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
		
		if (callCollection == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		// endregion
		
		//+ The list that has the duration of each contact.
		//+ And the order is by descending of the duration.
		List<Map.Entry<Contact, DurationGroup>> durationList = createContactHistoryDurationList(callCollection.getContacts());
		//+ First item of this list is the winner.
		//+ Possibly there are durations with the same or so close with each other.
		
		//+ The object that to convert the duration to string.
		DurationGroup.Stringer stringer = DurationGroup.Stringer.builder()
				.formatter(duration1 -> fmt("%d %s", duration1.getValue(), makePlural(duration1.getUnit().toString(), duration1.getValue())))//_ the formatted string to be used.
				.unit(Unit.YEAR, Unit.MONTH, Unit.DAY)//_ the units should be used.
				.zeros(false);//_ the zero durations should not be used.
		//+ The duration of this contact.
		DurationGroup thisDuration = HistoryRanker.getDuration(durationList, contact);
		
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
		int                               rank                = HistoryRanker.getRank(durationList, contact);
		boolean                           winner              = contact.getContactId() == winnerContact.getContactId();
		
		if (isTurkish) {
			
			comment.append("Kişinin arama geçmişi süresi ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(". Rehberdeki diğer kişilerle karşılaştırıldığında ")
					.append("arama geçmişi süreleri", getClickSpans(listener))
					.append(" listesinin ");
			
			if (winner) comment.append("ilk sırasında yer aldığı görülüyor \uEB52.\n");
			else comment.append(fmt("%d. sırasında yer aldığı görülüyor.\n", rank));
		}
		else {
			
			comment.append("This contact has ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(" call history. ")
					.append("Compared to others in the contacts ");
			
			if (winner) {
				
				comment.append("he/she appears in the first order in the ");
			}
			else {
				
				comment.append(fmt("he/she appears in the %d. order in the ", rank));
			}
			
			comment.append("call history durations", getClickSpans(listener))
					.append(" list.\n");
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
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
			
			Contact contact        = entry.getKey();
			String  durationString = stringer.durations(duration.getDurations()).toString();
			var     data           = new MostDurationData(contact.getName(), durationString, order++);
			
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
			
			assert callCollection != null;
			History history = callCollection.getHistoryOf(contact);
			
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
