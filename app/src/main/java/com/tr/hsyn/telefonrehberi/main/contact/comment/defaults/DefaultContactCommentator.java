package com.tr.hsyn.telefonrehberi.main.contact.comment.defaults;


import android.view.View;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.HotListByQuantity;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankMate;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class implements the {@link ContactCommentator} interface and
 * provides default behavior for commenting on a contact.
 */
public class DefaultContactCommentator implements ContactCommentator {
	
	/**
	 * The comment object.
	 * All generated comments by the commentator appends into this object.
	 */
	protected final Spanner             comment = new Spanner();
	/**
	 * The history that has the all calls of the current contact.
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
	protected       boolean             isTurkish;
	protected       CallCollection      callCollection;
	protected       HotListByQuantity   hotListByQuantity;
	
	/**
	 * Constructs a new {@link DefaultContactCommentator} object with the given comment store.
	 *
	 * @param commentStore the comment store to use by this commentator
	 */
	public DefaultContactCommentator(@NotNull ContactCommentStore commentStore) {
		
		this.commentStore = commentStore;
		isTurkish         = commentStore.isTurkishLanguage();
		callCollection    = getCallCollection();
	}
	
	/**
	 * Returns the comment store used by this commentator.
	 *
	 * @return the comment store
	 */
	@Override
	public ContactCommentStore getCommentStore() {
		
		return commentStore;
	}
	
	/**
	 * Generates a comment on the specified contact.
	 * This method is the entry point to the commentator.
	 * So, it ensures that the contact is not null and not empty for the methods that called after.
	 *
	 * @param contact the contact to comment on
	 * @return the generated comment as a CharSequence
	 */
	@Override
	public @NotNull CharSequence commentOn(@NotNull Contact contact) {
		
		this.contact = contact;
		//todo history 
		this.history = contact.getData(ContactKey.HISTORY);
		
		//if history null or empty, no need to go any further.
		if (history != null) {
			if (history.isEmpty()) comment.append(commentStore.noHistory());
			else commentOnContact();
		}
		else comment.append(commentStore.historyNotFound());
		
		return comment;
	}
	
	/**
	 * Returns the current contact commenting on.
	 *
	 * @return the current contact
	 */
	@Override
	public Contact getContact() {
		
		return contact;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public @NotNull CharSequence commentOnTheLastCall() {
		
		Spanner                   commentOnTheLastCall = new Spanner();
		com.tr.hsyn.calldata.Call lastCall             = history.getLastCall();
		String                    callType             = Res.getCallType(commentStore.getActivity(), lastCall.getCallType());
		Duration                  timeBefore           = Time.howLongBefore(lastCall.getTime());
		ShowCall                  showCall             = new ShowCall(commentStore.getActivity(), lastCall);
		View.OnClickListener      listener1            = view -> showCall.show();
		
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
	
	/**
	 * Generates the comments based on the call history of the current contact.
	 * Invokes private methods to generate person comments.
	 * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
	 */
	private void commentOnContact() {
		
		if (callCollection == null) return;
		hotListByQuantity = callCollection.getHotListByQuantity();
		
		// Here start to generate the comment.
		// Call history is not 'null' and not empty at this point.
		
		xlog.d("Accessed the call history [contact='%s', size=%d]", contact.getName(), history.size());
		
		if (history.size() == 1) {
			commentOnSingleCall();
		}
		else {
			this.comment.append(commentQuantity());
			this.comment.append(commentMostQuantity());
			this.comment.append(commentOnTheLastCall());
			this.comment.append(commentLastCallType());
			this.comment.append(firstLastCallComment());
		}
		
	}
	
	/**
	 * Appends a comment about the single call to the {@link #comment} object that
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
	private CharSequence commentQuantity() {
		
		// Now, certainly the call history size is more than one.
		// The call history of the current contact has two calls at least.
		
		// Inform the user about the quantity of the call history.
		// For example, 'The contact has 2 calls'.
		
		Spanner              quantityComment = new Spanner();
		String               name            = contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName()) ? contact.getName() : Stringx.toTitle(getString(R.string.word_contact));
		View.OnClickListener listener        = view -> new ShowCallsDialog(commentStore.getActivity(), history.getCalls()).show();
		quantityComment.append(name, Spans.bold(), Spans.foreground(getTextColor()));
		
		// Have two language resources forever, think so.
		if (isTurkish) {
			
			var extension = WordExtension.getWordExt(name, Extension.TYPE_DATIVE);
			
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
	 * Returns the comment about the most quantity of calls.
	 *
	 * @return the comment
	 */
	private @NotNull CharSequence commentMostQuantity() {
		
		var            com        = new Spanner();
		CallCollection collection = getCallCollection();
		
		if (collection == null) {
			
			xlog.w("Cannot find call collection");
			return com;
		}
		
		var ranks = new RankList(collection.getNumberedCalls());
		ranks.makeRanks();
		
		Map<Integer, List<CallRank>> map      = ranks.getRankMap();
		RankMate                     rankMate = new RankMate(map);
		var                          numbers  = ContactKey.getNumbers(contact);
		
		if (numbers == null) return com;
		
		int rank = rankMate.getRank(numbers);
		
		if (rank != -1) {
			
			var callRankList = map.get(rank);
			assert callRankList != null;
			int rankCount = callRankList.size();
			var mostList  = createMostCallItemList(map);
			var dialog    = new MostCallDialog(commentStore.getActivity(), mostList);
			
			xlog.w("rank=%d, rankCount=%d", rank, rankCount);
			
			if (isTurkishLanguage()) {
				
				com.append("Ve ")
						.append("en fazla arama", getClickSpans(view -> dialog.show()))
						.append(" kaydına sahip kişiler listesinde ");
				
				if (rankCount <= 1) com.append("tek başına ");
				else com.append(fmt("%d kişi ile birlikte ", rankCount));
				
				com.append(fmt("%d. sırada. ", rank));
			}
			else {
				
				com.append(fmt("And in the %d. place ", rank));
				
				var hotList = new Spanner().append("the hot list", getClickSpans(view -> dialog.show()));
				
				if (rankCount == 1) com.append("alone ");
				else com.append(fmt("together with %d contact(s) ", rankCount));
				
				com.append("in ")
						.append(hotList)
						.append(" of the call quantity. ");
			}
		}
		
		return com;
	}
	
	/**
	 * @return comment about last call type
	 */
	private @NotNull CharSequence commentLastCallType() {
		
		Spanner                   commentAboutLastCallType = new Spanner();
		com.tr.hsyn.calldata.Call lastCall                 = history.getLastCall();
		int                       type                     = lastCall.getCallType();
		
		int[]      callTypes  = Res.getCallTypes(type);
		List<Call> typedCalls = history.getCallsByTypes(callTypes);
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
			
			var             historyCalls    = history.getCallsByTypes(callTypes);
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
	 * Generates a comment about the first and last call made to the contact.
	 *
	 * @return comment about first and last call
	 */
	private @NotNull CharSequence firstLastCallComment() {
		
		var comment = new Spanner();
		
		if (history.size() > 1) {
			
			DurationGroup duration                  = history.getHistoryDuration();
			Unit[]        durationUnits             = {Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR};
			Duration      longest                   = duration.getGreatestUnit();
			String        durationString            = getDurationString(duration, durationUnits);
			boolean       isDurationGreaterThanHour = longest.isGreaterByUnit(Unit.HOUR);
			Span[] textSpans = {
					Spans.bold(),
					Spans.foreground(Colors.lighter(Colors.getPrimaryColor(), 0.35f))
			};
			
			comment.append("\n");
			
			if (longest.isNotZero()) {
				
				if (isTurkish) {
					
					comment.append("Kişinin ilk arama kaydı ile son arama kaydı arasında geçen zaman tam olarak ")
							.append(durationString, textSpans);
					
					if (isDurationGreaterThanHour) {
						
						comment.append(fmt("Yani bu kişiyle kabaca %s%s bir iletişim geçmişiniz var. ", longest.toString("%d %s"), WordExtension.getWordExt(longest.toString(), Extension.TYPE_ABSTRACT)));
					}
					else {
						
						//+ The call history duration is less than 1-day.
						String msg = fmt("Yani bu kişiyle aranızda bir iletişim geçmişi olduğu pek söylenemez. ");
						comment.append(msg);
					}
				}
				else {
					
					comment.append("The exact time elapsed between the contact's first call record and the last call record is ")
							.append(durationString, textSpans)
							.append(". ");
					
					if (isDurationGreaterThanHour) {
						comment.append(fmt("So you have roughly %s of contact history with this person. ", makePlural(longest.toString("%d %s"), longest.getValue())));
					}
					else {
						
						//+ The call history duration is less than 1-day.
						String msg = fmt("So, it cannot be said that there is a communication history between you and this person. ");
						comment.append(msg);
					}
				}
			}
			else {
				
				String msg;
				if (isTurkish) msg = fmt("Bu kişi ile aranızda bir görüşme geçmişi yok. ");
				else msg = fmt("There is no communication history between you and this person. ");
				comment.append(msg);
			}
			
			comment.append(mostHistoryDurationComment(duration));
		}
		
		return comment;
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
	 * Creates a list of most call items.
	 *
	 * @param map the map of call ranks
	 * @return the list of most call items
	 */
	@NotNull
	private List<MostCallItemViewData> createMostCallItemList(@NotNull Map<Integer, List<CallRank>> map) {
		
		List<MostCallItemViewData> list = new ArrayList<>();
		
		for (var rankList : map.values()) {
			
			for (CallRank callRank : rankList) {
				
				var call  = callRank.getCalls().get(0);
				var n     = call.getName();
				var name  = (n != null && !n.isBlank()) ? n : call.getNumber();
				var size  = callRank.getCalls().size();
				var order = callRank.getRank();
				list.add(new MostCallItemViewData(name, size, order));
			}
		}
		
		list.sort(Comparator.comparingInt(MostCallItemViewData::getCallSize).reversed());
		return list;
	}
	
	/**
	 * Returns a comment about the duration of the history.
	 *
	 * @param duration the history duration of the current contact
	 * @return comment
	 */
	@NotNull
	private CharSequence mostHistoryDurationComment(@NotNull DurationGroup duration) {
		
		// region prepares all contacts history, and the duration list
		var                         comment   = new Spanner();
		Map<Contact, DurationGroup> durations = new HashMap<>();
		
		var contacts = getContacts();
		
		if (contacts == null || contacts.isEmpty())
			return comment;
		
		//? put the all durations into the map
		for (Contact contact : contacts) {
			
			History history = ContactKey.getHistory(contact);
			
			if (history == null || history.isEmpty())
				history = History.getHistory(contact);
			
			if (history.isEmpty()) {
				
				//xlog.d("%s has no history", contact.getName());
				continue;
			}
			
			var historyDuration = history.getHistoryDuration();
			
			if (!historyDuration.isZero())
				durations.put(contact, historyDuration);
		}
		
		//+ The list that has durations of all contacts
		List<Map.Entry<Contact, DurationGroup>> durationList = new ArrayList<>(durations.entrySet());
		
		//+ Comparing by value makes the list in ascending order 
		durationList.sort(Map.Entry.comparingByValue());
		Collections.reverse(durationList);// descending order
		
		// endregion
		
		//! --------------------------------------------------------------
		int contactCount = contacts.size();
		int historyCount = durationList.size();
		
		comment.append(getHistoryCountComment(contactCount, historyCount));
		
		//+ at least 3 history duration
		if (historyCount > 3) {
			
			//+ winner item
			Map.Entry<Contact, DurationGroup> firstItem      = durationList.get(0);
			long                              firstContactId = firstItem.getKey().getContactId();
			DurationGroup                     firstDuration  = firstItem.getValue();
			
			var                  mostDurationDataList = createMostDurationList(durationList);
			var                  dialog               = new MostDurationDialog(commentStore.getActivity(), mostDurationDataList);
			View.OnClickListener listener             = view -> dialog.show();
			boolean              isWinner             = firstContactId == this.contact.getContactId();
			
			
			if (isTurkish) {
				
				comment.append("En uzun arama geçmişine sahip kişiler")
						.append(" listesine bakılırsa ");
				
				if (isWinner) {
					
					comment.append("bu kişi en uzun arama geçmişine sahip kişi olduğundan dolayı senin için önemli bir kişi olduğu söylenebilir. ");
				}
				
			}
			else {
				
				comment.append("There is a list called ")
						.append("the most history duration", getClickSpans(listener))
						.append(" that consists of all contacts in your contacts that contacted you. ");
				
				if (isWinner) comment.append("And this contact is the contact that have the most history duration according to that list. ");
				else comment.append("The contact that have the most history duration has ")
						.append(fmt("%s", firstDuration), Spans.foreground(getTextColor()))
						.append(" history duration. ");
			}
		}
		
		return comment;
	}
	
	@NotNull
	private List<MostDurationData> createMostDurationList(@NotNull List<Map.Entry<Contact, DurationGroup>> durationList) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int order = 1;
		for (var entry : durationList) {
			
			var contact        = entry.getKey();
			var duration       = entry.getValue();
			var durationString = getDurationString(duration, Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR);
			list.add(new MostDurationData(contact.getName(), durationString, order++));
		}
		
		return list;
	}
	
	/**
	 * Returns the duration of the history as a string.
	 *
	 * @param duration the history duration
	 * @param units    the units during the history to display
	 * @return the string
	 */
	@NotNull
	private String getDurationString(@NotNull DurationGroup duration, Unit @NotNull ... units) {
		
		var sb        = new StringBuilder();
		var durations = duration.pickFrom(units);
		
		for (var _duration : durations.getDurations()) {
			
			if (_duration.isNotZero())
				sb.append(makePlural(_duration.toString("%d %s"), _duration.getValue())).append(" ");
		}
		
		return sb.toString().trim();
	}
	
	@NotNull
	private CharSequence getHistoryCountComment(int contactCount, int historyCount) {
		
		if (contactCount == historyCount)
			if (isTurkish) return "Rehberde bulunan herkesle bir iletişim geçmişiniz var. ";
			else
				return "You have a contact history with everyone in the contacts. ";
		
		if (isTurkish)
			return fmt("Rehberde bulunan %d kişi içinden %d kişi ile aranızda bir iletişim geçmişi var. ", contactCount, historyCount);
		else
			return fmt("There is a communication history between you and %d people out of %d people in your contacts. ", historyCount, contactCount);
	}
	
	private CharSequence commentLastCallTypeRank() {
		
		Spanner rank     = new Spanner();
		Call    lastCall = history.getLastCall();
		
		
		return rank;
	}
	
	
}
