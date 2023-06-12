package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


public class LastCallTypeComment implements ContactComment {
	
	private final Spanner                  comment        = new Spanner();
	private final CallCollection           callCollection = getCallCollection();
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	
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
		
		return Topic.LAST_CALL_TYPE;
	}
	
	@Override
	public void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish) {
		
		this.callback = callback;
		this.activity = activity;
		
		if (callCollection == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		
		History    history    = callCollection.getHistoryOf(contact);
		Call       lastCall   = history.getLastCall();
		int        type       = lastCall.getCallType();
		int[]      callTypes  = Res.getCallTypes(type);
		List<Call> typedCalls = history.getCallsByTypes(callTypes);
		String     typeStr    = Res.getCallType(getActivity(), type);
		
		if (typedCalls.size() == 1) {
			
			if (isTurkish) {
				
				comment.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait tek ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(". ");
			}
			else {
				
				comment.append("And this ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" is only single ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" of this contact. ");
			}
		}
		else {
			
			ShowCallsDialog showCallsDialog = new ShowCallsDialog(getActivity(), typedCalls, history.contact().getName(), Stringx.format("%d %s", typedCalls.size(), typeStr));
			
			View.OnClickListener listener = view -> showCallsDialog.show();
			
			if (isTurkish) {
				
				comment.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait ")
						.append(Stringx.format("%d %s", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
						.append("dan biri. ");
			}
			else {
				//and this call is one of the 33 outgoing calls
				comment.append("And this ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" is one of the ")
						.append(Stringx.format("%d %ss", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
						.append(". ");
			}
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
}
