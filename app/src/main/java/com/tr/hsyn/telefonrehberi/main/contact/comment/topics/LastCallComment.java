package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


public class LastCallComment implements ContactComment {
	
	private final CallCollection           callCollection = getCallCollection();
	private final Spanner                  comment        = new Spanner();
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
		
		return Topic.LAST_CALL;
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
		
		History                   history         = callCollection.getHistoryOf(contact);
		com.tr.hsyn.calldata.Call lastCall        = history.getLastCall();
		int                       type            = lastCall.getCallType();
		int[]                     callTypes       = Res.getCallTypes(type);
		List<Call>                typedCalls      = history.getCallsByTypes(callTypes);
		String                    typeStr         = Res.getCallType(getActivity(), type);
		Duration                  timeBefore      = Time.howLongBefore(lastCall.getTime());
		ShowCall                  showCall        = new ShowCall(getActivity(), lastCall);
		View.OnClickListener      listener1       = view -> showCall.show();
		ShowCallsDialog           showCallsDialog = new ShowCallsDialog(getActivity(), typedCalls, history.contact().getName(), Stringx.format("%d %s", typedCalls.size(), typeStr));
		View.OnClickListener      listener        = view -> showCallsDialog.show();
		
		if (isTurkish) {
			
			// bu arama 3 gün önce olan bir cevapsız çağrı
			comment.append(getString(R.string.word_the_last_call), getClickSpans(listener1))
					.append(" ")
					.append(getString(R.string.word_date_before, timeBefore.getValue(), timeBefore.getUnit()))
					.append(" ")
					.append(getString(R.string.word_happened))
					.append(" ")
					.append(getString(R.string.word_a))
					.append(" ")
					.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
					.append(". ");
			
			if (typedCalls.size() == 1) {
				
				comment.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait tek ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(". ");
			}
			else {
				
				comment.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait ")
						.append(Stringx.format("%d %s", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
						.append("dan biri. ");
			}
		}
		else {
			
			// this call is from 3 days ago
			comment.append(getString(R.string.word_the_last_call), getClickSpans(listener1))
					.append(" ")
					.append(getString(R.string.word_is))
					.append(" ")
					.append(Stringx.format("%s", (typeStr.toLowerCase().charAt(0) == 'o' || typeStr.toLowerCase().charAt(0) == 'i') ? "an " : "a "))
					.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
					.append(" ")
					.append(getString(R.string.word_from))
					.append(" ")
					.append(getString(R.string.word_date_unit, timeBefore.getValue(), timeBefore.getUnit()))
					.append(Stringx.format("%s", timeBefore.getValue() > 1 ? "s " : " "))
					.append(getString(R.string.word_is_ago))
					.append(". ");
			
			if (typedCalls.size() == 1) {
				
				comment.append("And this ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" is only single ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" of this contact. ");
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
