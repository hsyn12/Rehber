package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

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
		
		History                   history    = callCollection.getHistoryOf(contact);
		com.tr.hsyn.calldata.Call lastCall   = history.getLastCall();
		String                    callType   = Res.getCallType(getActivity(), lastCall.getCallType());
		Duration                  timeBefore = Time.howLongBefore(lastCall.getTime());
		ShowCall                  showCall   = new ShowCall(getActivity(), lastCall);
		View.OnClickListener      listener1  = view -> showCall.show();
		
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
					.append(Stringx.format("%s", callType.toLowerCase()), Spans.bold())
					.append(". ");
		}
		else {
			
			// this call is from 3 days ago
			comment.append(getString(R.string.word_the_last_call), getClickSpans(listener1))
					.append(" ")
					.append(getString(R.string.word_is))
					.append(" ")
					.append(Stringx.format("%s", (callType.toLowerCase().charAt(0) == 'o' || callType.toLowerCase().charAt(0) == 'i') ? "an " : "a "))
					.append(Stringx.format("%s", callType.toLowerCase()), Spans.bold())
					.append(" ")
					.append(getString(R.string.word_from))
					.append(" ")
					.append(getString(R.string.word_date_unit, timeBefore.getValue(), timeBefore.getUnit()))
					.append(Stringx.format("%s", timeBefore.getValue() > 1 ? "s " : " "))
					.append(getString(R.string.word_is_ago))
					.append(". ");
			
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
}
