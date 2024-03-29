package com.tr.hsyn.telefonrehberi.main.contact.comment.commentator;


import android.app.Activity;

import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.CommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.Moody;
import com.tr.hsyn.telefonrehberi.main.contact.comment.defaults.DefaultContactCommentStore;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


/**
 * This interface describes contact comment types.
 * Classes that making comment about a contact need to use this interface
 * or need to use classes/interface that implemented/extended this interface.
 * All comments are provided in here.
 */
public interface ContactCommentStore extends CommentStore {
	
	@NotNull
	CharSequence sizeCall(int size);
	
	@NotNull
	CharSequence theMostCallLog();
	
	@NotNull
	CharSequence thisContact();
	
	@NotNull
	CharSequence contactHas();
	
	@NotNull
	CharSequence hasOneOfThem(int count);
	
	default CharSequence historyNotFound() {
		
		return getString(R.string.history_not_found);
	}
	
	default CharSequence noHistory() {
		
		return getString(R.string.no_history);
	}
	
	default String getCallType(int callType) {
		
		switch (callType) {
			
			case Type.INCOMING: return getString(R.string.call_type_incoming);
			case Type.OUTGOING: return getString(R.string.call_type_outgoing);
			case Type.MISSED: return getString(R.string.call_type_missed);
			case Type.REJECTED: return getString(R.string.call_type_rejected);
			case Type.BLOCKED: return getString(R.string.call_type_blocked);
		}
		return getString(R.string.call_type_unknown);
	}
	
	/**
	 * Returns a string by getting all words from resource.
	 * Words are separated by space.
	 *
	 * @param words resource id list for words
	 * @return a string that separated by space
	 */
	@NotNull
	default String getWords(int @NotNull ... words) {
		
		StringBuilder clause = new StringBuilder();
		
		for (int word : words) clause.append(getString(word));
		
		return clause.toString();
	}
	
	/**
	 * @return a single call string
	 */
	default CharSequence singleCall() {
		
		return getString(R.string.single_call);
	}
	
	/**
	 * Returns a {@link ContactCommentStore} instance.
	 *
	 * @param activity the activity
	 * @param mood     the mood
	 * @return a {@link ContactCommentStore} instance
	 */
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
}
