package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import android.view.View;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.counter.Counter;
import com.tr.hsyn.daytimes.DayTime;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.NumberExtension;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.Res;
import com.tr.hsyn.telefonrehberi.code.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.code.call.CallOver;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * This class implements the {@link ContactCommentator} interface and
 * provides default behavior for commenting on a contact.
 */
public class DefaultContactCommentator implements ContactCommentator {
	
	/**
	 * The comment object.
	 * All generated comments by the commentator will be appended into this object
	 */
	protected final Spanner             comment = new Spanner();
	/**
	 * The list of all call log calls that also included the calls of the current contact
	 */
	protected final List<Call>          calls;
	/**
	 * History of the contact
	 */
	protected       History             history;
	/**
	 * The current contact
	 */
	protected       Contact             contact;
	/**
	 * The comment store
	 */
	protected       ContactCommentStore commentStore;
	
	/**
	 * Constructs a new {@link DefaultContactCommentator} object with the given comment store.
	 *
	 * @param commentStore the comment store to be used by this commentator
	 * @param calls        the list of all call log calls that also included the calls of the current contact
	 */
	public DefaultContactCommentator(ContactCommentStore commentStore, List<Call> calls) {
		
		this.commentStore = commentStore;
		this.calls        = calls;
	}
	
	/**
	 * Returns the comment store being used by this commentator.
	 *
	 * @return the comment store
	 */
	@Override
	public ContactCommentStore getCommentStore() {
		
		return commentStore;
	}
	
	/**
	 * Generates a comment on the specified contact.
	 *
	 * @param contact the contact to comment on
	 * @return the generated comment as a CharSequence
	 */
	@Override
	public @NotNull CharSequence commentOn(@NotNull Contact contact) {
		
		this.contact = contact;
		this.history = contact.getData(ContactKey.HISTORY);
		
		//if history null or empty, no need to go any further
		if (history != null) {
			if (history.isEmpty()) comment.append(commentStore.noHistory());
			else commentOnContact();
		}
		else comment.append(commentStore.historyNotFound());
		
		return comment;
	}
	
	/**
	 * Generates comments based on the call history of the current contact.
	 * Invokes private methods to generate individual comments.
	 * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
	 */
	private void commentOnContact() {
		
		// Here we start to generate the comment.
		// Call history is not 'null' and not empty at this point
		
		if (history.size() == 1) {
			commentOnSingleCall();
			return;
		}
		
		xlog.d("Accessed the call history [contact='%s', size=%d]", contact.getName(), history.size());
		
		Runny.run(() -> {
			
			this.comment.append(historyQuantityComment());
			this.comment.append(commentOnTheLastCall());
			this.comment.append(aboutLastCallType());
		});
	}
	
	/**
	 * Appends a comment about the single call to the {@link #comment} object
	 * managed by this commentator.
	 */
	private void commentOnSingleCall() {
		
		comment.append(commentStore.singleCall()).append(". ");
		commentOnTheSingleCall(history.get(0));
	}
	
	/**
	 * Returns a comment about the quantity of call history for the contact.
	 * For example, <code>'The contact has <u>2 calls</u>'</code>.
	 */
	@NonNull
	private CharSequence historyQuantityComment() {
		
		// Now, we are sure that the call history size has been more than one.
		// The call history of the current contact has two calls at least.
		
		// We want to inform the user about the quantity of the call history.
		// For example, 'The contact has 2 calls'
		
		Spanner              quantityComment = new Spanner();
		String               name            = contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName()) ? contact.getName() : Stringx.toTitle(getString(R.string.word_contact));
		View.OnClickListener listener        = view -> new ShowCallsDialog(commentStore.getActivity(), history.getHistory()).show();
		quantityComment.append(name, Spans.bold(), Spans.foreground(getTextColor()));
		
		// We have two language resources forever, I think
		if (commentStore.isTurkishLanguage()) {
			
			var extension = WordExtension.getWordExt(name, Extension.TYPE_TO);
			
			quantityComment.append(Stringx.format("'%s %s ", extension, getString(R.string.word_has)))
					.append(Stringx.format("%s", getString(R.string.word_calls, history.size())), getClickSpans(listener))
					.append(" ")
					.append(getString(R.string.word_exist))
					.append(". ");
		}
		else {
			
			quantityComment.append(" ")
					.append(getString(R.string.word_has))
					.append(" ")
					.append(Stringx.format("%s", getString(R.string.word_calls, history.size())), getClickSpans(listener))
					.append(". ");
		}
		
		return quantityComment;
	}
	
	/**
	 * @return comment about last call type
	 */
	private @NotNull CharSequence aboutLastCallType() {
		
		Spanner commentAboutLastCallType = new Spanner();
		Call    lastCall                 = history.getLastCall();
		int     type                     = lastCall.getCallType();
		
		int[]      callTypes  = Res.getCallTypes(type);
		List<Call> typedCalls = history.getCalls(callTypes);
		String     typeStr    = Res.getCallType(commentStore.getActivity(), type);
		
		if (typedCalls.size() == 1) {
			
			if (commentStore.isTurkishLanguage()) {
				
				commentAboutLastCallType.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait tek ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(". ");
			}
			else {
				
				commentAboutLastCallType.append("And this ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" is only single ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" of this contact. ");
			}
		}
		else {
			
			var             historyCalls    = history.getCalls(callTypes);
			ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), historyCalls, history.getContact().getName(), Stringx.format("%d %s", historyCalls.size(), typeStr));
			
			View.OnClickListener listener = view -> showCallsDialog.show();
			
			if (commentStore.isTurkishLanguage()) {
				
				commentAboutLastCallType.append("Ve bu ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" kişiye ait ")
						.append(Stringx.format("%d %s", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
						.append("dan biri. ");
			}
			else {
				//and this call is one of the 33 outgoing calls
				commentAboutLastCallType.append("And this ")
						.append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
						.append(" is one of the ")
						.append(Stringx.format("%d %ss", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
						.append(". ");
			}
		}
		
		return commentAboutLastCallType;
	}
	
	/**
	 * Appends a comment about the given call to the {@link #comment} object.
	 *
	 * @param call the call to comment on
	 */
	private void commentOnTheSingleCall(@NotNull Call call) {
		
		Duration             timeBefore = Time.howLongBefore(call.getTime());
		String               callType   = Res.getCallType(commentStore.getActivity(), call.getCallType());
		View.OnClickListener listener1  = view -> new ShowCall(commentStore.getActivity(), call).show();
		
		if (commentStore.isTurkishLanguage()) {
			
			// bu arama 3 gün önce olan bir cevapsız çağrı
			comment.append(getString(R.string.word_this_call), getClickSpans(listener1))
					.append(" ")
					.append(getString(R.string.word_date_before, timeBefore.getValue(), timeBefore.getUnit()))
					.append(" ")
					.append(getString(R.string.word_happened))
					.append(" ")
					.append(getString(R.string.word_a))
					.append(Stringx.format("%s", callType.toLowerCase()), Spans.bold())
					.append(". ");
		}
		else {
			
			// this call is from 3 days ago
			comment.append(getString(R.string.word_this_call), getClickSpans(listener1))
					.append(" ")
					.append(getString(R.string.word_is_from))
					.append(" ")
					.append(getString(R.string.word_date_unit, timeBefore.getValue(), timeBefore.getUnit()))
					.append(Stringx.format("%s", timeBefore.getValue() > 1 ? "s " : " "))
					.append(getString(R.string.word_is_ago))
					.append(". ");
			
		}
		
	}
	
	/**
	 * Returns the current contact being commented on.
	 *
	 * @return the current contact
	 */
	@Override
	public Contact getContact() {
		
		return contact;
	}
	
	/**
	 * @return
	 * @inheritDoc
	 */
	@Override
	public @NotNull CharSequence commentOnTheLastCall() {
		
		Spanner              commentOnTheLastCall = new Spanner();
		Call                 lastCall             = history.getLastCall();
		String               callType             = Res.getCallType(commentStore.getActivity(), lastCall.getCallType());
		Duration             timeBefore           = Time.howLongBefore(lastCall.getTime());
		ShowCall             showCall             = new ShowCall(commentStore.getActivity(), lastCall);
		View.OnClickListener listener1            = view -> showCall.show();
		
		if (commentStore.isTurkishLanguage()) {
			
			// bu arama 3 gün önce olan bir cevapsız çağrı
			commentOnTheLastCall.append(getString(R.string.word_the_last_call), getClickSpans(listener1))
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
			commentOnTheLastCall.append(getString(R.string.word_the_last_call), getClickSpans(listener1))
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
		
		return commentOnTheLastCall;
	}
	
	private CharSequence commentLastCallTypeRank() {
		
		Spanner rank     = new Spanner();
		Call    lastCall = history.getLastCall();
		
		
		return rank;
	}
	
	/**
	 * Generates a comment when there is no call history for the contact.
	 * Appends the generated comment to the {@link #comment} object managed by this commentator.
	 */
	private void noHistoryComment() {
		
		comment.append("Bu kişi ile hiçbir iletişimin yok. ");
	}
	
	/**
	 * Generates a comment about the contact's most frequent contacts based on their call history.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
	private void mostCallComments() {
		
		if (calls != null) {
			
			//- Telefon numarası bir anahtar gibi kullanılacak
			String phoneNumber = PhoneNumbers.formatNumber(history.get(0).getNumber(), 10);
			
			//- Telefon numarasına karşı, numaraya ait arama kayıtlarından oluşan bir liste
			List<Group<Call>> groups = CallOver.groupByNumber((x, y) -> Integer.compare(y.size(), x.size()));
			
			//- Arama kayıtlarındaki kişi sayısı
			int differentPerson = groups.size();
			
			//todo Arama kayıtlarını oluşturan kişi sayısı çok düşükse
			// yapılacak inceleme pek kayda değer olmayabilir.
			
			//- Bu kişinin arama kayıtları, tüm arama kayıtlarının yüzde kaçı oluyor?
			int percent = (history.size() * 100) / calls.size();
			
			if (percent > 0)
				comment.append(Stringx.format("Bu kayıtlar, tüm arama kayıtlarının yüzde %d'%s oluyor. ", percent, NumberExtension.getNumberExt(percent, NumberExtension.TYPE_DAY)));
			
			//todo yüzde değeri 1'e ulaşmıyorsa ne olacak
			
			//comment.append(Stringx.format("Tüm arama kayıtları %d farklı kişiden oluşmakta. ", differentPerson));
			
			//- Bay Sayman. Listedeki elemanları sayacak
			Counter<Group<Call>> counter = new Counter<>(groups);
			
			//- en çok kaydı olan eleman
			Group<Call> winner = groups.get(0);
			
			//- Aynı arama kaydı sayısına sahip kişi sayısı
			//- Yani en çok arama kaydına sahip kaç kişi olduğunu buluyoruz
			long count = counter.count(winner, Group::size);
			
			//- Bu kişi en çok arama kaydına sahip kişi mi?
			if (PhoneNumbers.equalsOrContains(phoneNumber, winner.getValue().getNumber())) {
				
				var viewData = Lister.map(groups, e -> {
					
					var name = e.getValue().getName();
					
					if (name == null) name = e.getValue().getNumber();
					
					return new MostCallItemViewData(name, e.size());
				});
				View.OnClickListener clickListener = v -> new MostCallDialog(commentStore.getActivity(), viewData);
				
				int color = getColor(com.tr.hsyn.rescolors.R.color.orange_500);
				
				if (count == 1) {
					
					comment.append(commentStore.thisContact())
							.append(" ")
							.append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
							.append(" ")
							.append(commentStore.contactHas())
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip kişi : %s", groups);
				}
				else {
					
					comment.append(commentStore.thisContact())
							.append(" ")
							.append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
							.append(" ")
							.append(commentStore.hasOneOfThem((int) count))
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip %d kişiden biri : %s", count, groups);
				}
			}
		}
		else {
			
			comment.append("Arama kayıtları alınamadı");
		}
		
		
		xlog.i("Most comments completed");
	}
	
	/**
	 * Generates a comment about the first and last call made to the contact.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
	private void addFirstLastCallComment() {
		
		if (history.size() > 1) {
			
			//- Kişinin en eski arama kaydı
			Call   firstCall     = history.getFirstCall();
			Call   lastCall      = history.getLastCall();
			var    duration      = Time.toDuration(lastCall.getTime() - firstCall.getTime());
			Unit[] durationUnits = {Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE};
			
			Span[] textSpans = {
					Spans.bold(),
					Spans.foreground(Colors.getPrimaryColor())
			};
			
			comment.append("İlk arama kaydının tarihi ")
					.append(Stringx.format("%s. ", Time.toString(firstCall.getTime())), textSpans)
					.append("Son arama kaydı ise ")
					.append(Stringx.format("%s. ", Time.toString(lastCall.getTime())), textSpans);
			
			comment.append("Bu iki tarih arasında geçen zaman tam olarak ")
					.append(Stringx.format("%s. ", DayTime.toString(commentStore.getActivity(), duration, durationUnits)), textSpans);
			
			xlog.d("Kişiye ait ilk arama kaydının tarihi : %s [%d]", Time.toString(firstCall.getTime()), firstCall.getTime());
			
			
		}
	}
	
	
}
