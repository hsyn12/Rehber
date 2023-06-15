package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.RankByHistory;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class HistoryDurationComment implements ContactComment {
	
	private final CallCollection           callCollection = getCallCollection();
	private final Spanner                  comment        = new Spanner();
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	private       Contact                  contact;
	
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
		
		this.contact  = contact;
		this.callback = callback;
		this.activity = activity;
		
		if (callCollection == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		
		List<Map.Entry<String, DurationGroup>> durationList = RankByHistory.createRankMap(callCollection);
		//+ We have a list that has the duration of each phone number.
		//+ And the order is by descending of the duration.
		
		Map.Entry<String, DurationGroup> longestDuration = durationList.get(0);
		Long                             id              = callCollection.getContactId(longestDuration.getKey());
		
		if (id != null) {
			
			if (id == contact.getId()) {
				
				
			}
		}
		
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
}
