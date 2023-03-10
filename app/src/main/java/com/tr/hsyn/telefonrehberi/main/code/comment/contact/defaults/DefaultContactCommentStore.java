package com.tr.hsyn.telefonrehberi.main.code.comment.contact.defaults;


import android.app.Activity;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;

import org.jetbrains.annotations.NotNull;


public class DefaultContactCommentStore implements ContactCommentStore {
	
	private final Activity activity;
	
	public DefaultContactCommentStore(@NotNull Activity context) {
		
		this.activity = context;
	}
	
	@NotNull
	@Override
	public Activity getActivity() {
		
		return activity;
	}
	
	@NotNull
	@Override
	public CharSequence unknown() {
		
		return getString(R.string.unknown);
	}
	
	@NotNull
	@Override
	public CharSequence noHistory() {
		
		return getString(R.string.no_history);
	}
	
	@NotNull
	@Override
	public CharSequence savedDate() {
		
		return getString(R.string.saved_date);
	}
	
	@NotNull
	@Override
	public CharSequence historySize(int size) {
		
		return getString(R.string.history_size, size);
	}
	
	@Override
	public @NotNull CharSequence sizeCall(int size) {
		
		return getString(R.string.size_call, size);
	}
	
	@NotNull
	@Override
	public CharSequence historySizeOnly(int size) {
		
		return getString(R.string.history_size_only, size);
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
	public CharSequence thisContactHas() {
		
		return getString(R.string.word_contact_has_record);
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
	public CharSequence has() {
		
		return getString(R.string.word_has);
	}
	
	@NotNull
	@Override
	public CharSequence hasRecordOf() {
		
		return getString(R.string.word_contact_has_record_of);
	}
	
	@NotNull
	public CharSequence getString(int resourceId, Object... args) {
		
		return activity.getString(resourceId, args);
	}
	
}
