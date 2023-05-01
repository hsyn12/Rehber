package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import android.app.Activity;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;

import org.jetbrains.annotations.NotNull;


public class DefaultContactCommentStore implements ContactCommentStore {
	
	private final Activity activity;
	
	public DefaultContactCommentStore(@NotNull Activity context) {
		
		this.activity = context;
	}
	
	@Override
	public @NotNull CharSequence sizeCall(int size) {
		
		return getString(R.string.size_call, size);
	}
	
	@NotNull
	@Override
	public CharSequence theMostCallLog() {
		
		return getString(R.string.word_the_most_call);
	}
	
	@NotNull
	@Override
	public CharSequence thisContact() {
		
		return getString(R.string.word_this_contact);
	}
	
	@NotNull
	@Override
	public CharSequence contactHas() {
		
		return getString(R.string.word_contact_has);
	}
	
	@NotNull
	@Override
	public CharSequence hasOneOfThem(int count) {
		
		return getString(R.string.word_contact_has_one_of_them, count);
	}
	
	@NotNull
	@Override
	public Activity getActivity() {
		
		return activity;
	}
	
}
