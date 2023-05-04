package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.code.android.Res;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.CommentHelper;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DefaultCommentHelper implements CommentHelper {
	
	private final ContactCommentStore commentStore;
	private final List<Call>          calls;
	
	public DefaultCommentHelper(ContactCommentStore commentStore, List<Call> calls) {
		
		this.commentStore = commentStore;
		this.calls        = calls;
	}
	
	/**
	 * This method is called from a {@link ContactCommentator}
	 * to comment on the contact after the last call comment.
	 *
	 * @param history Contact history
	 * @return the comment
	 */
	@Override
	public @NotNull CharSequence afterLastCallComment(@NotNull History history) {
		
		Spanner       comment  = new Spanner();
		Call          lastCall = history.getLastCall();
		List<Integer> types    = new ArrayList<>();
		int           type     = lastCall.getType();
		
		types.add(type);
		
		switch (type) {//@off
			case CallType.INCOMING: types.add(CallType.INCOMING_WIFI);break;
			case CallType.OUTGOING: types.add(CallType.OUTGOING_WIFI);break;
			case CallType.INCOMING_WIFI: types.add(CallType.INCOMING);break;
			case CallType.OUTGOING_WIFI: types.add(CallType.OUTGOING);break;
		}//@on
		
		var callTypes  = Lister.toIntArray(types);
		var typedCalls = history.getCalls(callTypes);
		var typeStr    = Res.getCallType(commentStore.getActivity(), type);
		
		if (typedCalls.size() == 1) {
			
			comment.append("Ve bu arama kişiye ait tek ")
					.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
					.append(". ");
		}
		else {
			
			var             calls           = history.getCalls(callTypes);
			ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), calls, history.getContact().getName(), Stringx.format("%d %s", calls.size(), typeStr));
			
			View.OnClickListener listener = View -> showCallsDialog.show();
			
			comment.append("Ve bu arama kişiye ait ")
					.append(Stringx.format("%d %s", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, commentStore.getClickColor()), Spans.underline())
					.append("dan biri. ");
		}
		
		return comment;
	}
	
}
