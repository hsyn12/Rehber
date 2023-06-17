package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.HistoryRanker;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

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
		
		this.contact   = contact;
		this.callback  = callback;
		this.activity  = activity;
		this.isTurkish = isTurkish;
		
		if (callCollection == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		
		//+ The duration map of each phone number.
		Map<String, DurationGroup> historyRankMap = HistoryRanker.createRankMap(callCollection);
		
		//+ The list that has the duration of each phone number.
		//+ And the order is by descending of the duration.
		List<Map.Entry<String, DurationGroup>> durationList = HistoryRanker.createRankList(historyRankMap);
		//+ First item of this list is the winner.
		//+ Possibly there are durations with the same or so close with each other.
		
		//+ The duration of this contact.
		DurationGroup duration = HistoryRanker.getDuration(historyRankMap, contact);
		
		if (duration == null) {
			
			xlog.d("Could not accessed the 'history duration' of this contact.");
			returnComment();
			return;
		}
		
		//+ The object that to convert the duration to string.
		DurationGroup.Stringer stringer = DurationGroup.Stringer.builder()
				.formatter(duration1 -> fmt("%d %s", duration1.getValue(), makePlural(duration1.getUnit().toString(), duration1.getValue())))//_ the formatted string to be used.
				.unit(Unit.YEAR, Unit.MONTH, Unit.DAY)//_ the units should be used.
				.zeros(false);//_ the zero durations should not be used.
		
		String durationString = stringer.durations(duration.getDurations()).toString();
		
		if (isTurkish) {
			
			comment.append("Kişinin arama geçmişi süresi ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(". ");
		}
		else {
			
			comment.append("This contact has ")
					.append(fmt("%s", durationString), getTextStyle())
					.append(" call history. ");
			
		}
		
		
		Map.Entry<String, DurationGroup> longestDurationItem = durationList.get(0);
		DurationGroup                    longestDuration     = longestDurationItem.getValue();
		Contact                          _contact            = callCollection.getContact(longestDurationItem.getKey());
		//String                           msg                 = fmt("Longest duration : %s [contact=%s]", longestDuration, _contact);
		
		if (_contact != null) {
			
			long id = _contact.getContactId();
			
			if (id == contact.getId()) {
				
				//+ The current contact has the longest duration.
				
				
			}
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
}
