package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class CallDurationComment implements ContactComment {
	
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
		
		return Topic.CALL_DURATION;
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
		
		
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
}
