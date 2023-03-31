package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.app.Activity;

import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults.DefaultContactCommentStore;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


public interface ContactCommentStore {
	
	@NotNull
	static ContactCommentStore createCommentStore(@NotNull Activity activity, @NotNull Moody mood) {
		
		switch (mood) {
			
			case DEFAULT:
				return new DefaultContactCommentStore(activity);
			case HAPPY:
				xlog.d("Not yet HAPPY");
		}
		
		
		return new DefaultContactCommentStore(activity);
	}
	
	@NotNull
	Activity getActivity();
	
	@NotNull
	CharSequence unknown();
	
	@NotNull
	CharSequence noHistory();
	
	@NotNull
	CharSequence savedDate();
	
	@NotNull
	CharSequence historySize(int size);
	
	@NotNull
	CharSequence sizeCall(int size);
	
	@NotNull
	CharSequence historySizeOnly(int size);
	
	@NotNull
	CharSequence theMostCallLog();
	
	@NotNull
	CharSequence thisContact();
	
	@NotNull
	CharSequence thisContactHas();
	
	@NotNull
	CharSequence contactHas();
	
	@NotNull
	CharSequence hasOneOfThem(int count);
	
	@NotNull
	CharSequence has();
	
	@NotNull
	CharSequence hasRecordOf();

	/*@NotNull
	String getString(int resourceId, Object... args);*/
	
	
}
