package com.tr.hsyn.telefonrehberi.main.contact.comment.defaults;


import android.view.View;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.DurationRanker;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.Ranker;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.HistoryDurationComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.LastCallComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.QuantityComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.Topic;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.treadedwork.Threaded;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * This class implements the {@link ContactCommentator} interface and
 * provides default behavior for commenting on a contact.
 */
public class DefaultContactCommentator implements ContactCommentator, Threaded {
	
	/** The count of the comment */
	private static final int                      COUNT_OF_COMMENT       = 3;
	/**
	 * The comment object.
	 * All generated comments by the commentator appends into this object.
	 */
	protected final      Spanner                  comment                = new Spanner();
	private final        Map<Topic, CharSequence> comments               = new HashMap<>();
	private final        QuantityComment          quantityComment        = new QuantityComment();
	private final        LastCallComment          lastCallComment        = new LastCallComment();
	private final        HistoryDurationComment   historyDurationComment = new HistoryDurationComment();
	private final        Object                   gate                   = new Object();
	/** The history that has the all calls of the current contact. */
	protected            History                  history;
	/** The current contact */
	protected            Contact                  contact;
	/** The comment store */
	protected            ContactCommentStore      commentStore;
	protected            boolean                  isTurkish;
	protected            CallCollection           callCollection;
	private              Consumer<CharSequence>   callback;
	
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
	 * Returns the current contact commenting on.
	 *
	 * @return the current contact
	 */
	@Override
	public Contact getContact() {
		
		return contact;
	}
	
	@Override
	public void commentOn(@NotNull Contact contact, @NotNull Consumer<CharSequence> callback) {
		
		this.callback = callback;
		
		if (callCollection != null) {
			
			this.contact = contact;
			this.history = callCollection.getHistoryOf(contact);
			
			//if history is empty, no need to go any further.
			if (history.isEmpty()) {
				
				comment.append(commentStore.noHistory());
				returnComment();
			}
			else commentOnContact();
		}
		else {
			
			xlog.d("Not fount call collection");
			onMain(this::returnComment);
		}
	}
	
	private void returnComment() {
		
		onMain(() -> callback.accept(comment));
	}
	
	/**
	 * Generates the comments based on the call history of the current contact.
	 * Invokes private methods to generate person comments.
	 * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
	 */
	private void commentOnContact() {
		
		// Here start to generate the comment.
		// Call history is not 'null' and not empty at this point.
		
		xlog.d("Accessed the call history [contact='%s', size=%d]", contact.getName(), history.size());
		
		if (history.size() == 1) {
			commentOnSingleCall();
		}
		else {
			
			quantityComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			lastCallComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			historyDurationComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			
			/* this.comment.append(commentMostQuantity());
			this.comment.append(commentOnTheLastCall());
			this.comment.append(commentLastCallType());
			this.comment.append(firstLastCallComment());
			this.comment.append(commentOnDurations()); */
		}
		
	}
	
	private void onComment(@NotNull ContactComment contactComment) {
		
		synchronized (gate) {
			
			comments.put(contactComment.getTopic(), contactComment.getComment());
			boolean commentsCompleted = comments.size() == COUNT_OF_COMMENT;
			
			xlog.dx("Comment created : %s", contactComment.getTopic());
			xlog.d("Comment count : %d [commentsCompleted=%s]", comments.size(), commentsCompleted);
			
			if (commentsCompleted) {
				
				var commentList = comments.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByKey())
						.map(Map.Entry::getValue)
						.collect(Collectors.toList());
				
				Lister.loopWith(commentList, comment::append);
				
				returnComment();
			}
			else {
				
				xlog.d("Waiting other comments...");
			}
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
	@Deprecated
	@NonNull
	private CharSequence commentQuantity() {
		
		// Now, certainly the call history size is more than one.
		// The call history of the current contact has two calls at least.
		
		// Inform the user about the quantity of the call history.
		// For example, 'The contact has 2 calls'.
		
		Spanner              comment  = new Spanner();
		String               name     = contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName()) ? contact.getName() : Stringx.toTitle(getString(R.string.word_contact));
		View.OnClickListener listener = view -> new ShowCallsDialog(commentStore.getActivity(), history.getCalls(), contact.getName(), null).show();
		comment.append(name, Spans.bold(), Spans.foreground(getTextColor()));
		
		// Have two language resources forever, think so.
		if (isTurkish) {
			
			@NotNull String extension = WordExtension.getWordExt(name, Extension.TYPE_DATIVE);
			
			comment.append(Stringx.format("'%s %s ", extension, getString(R.string.word_has)))
					.append(Stringx.format("%s", getString(R.string.word_calls, history.size())), getClickSpans(listener))
					.append(" ")
					.append(getString(R.string.word_exist))
					.append(". ");
		}
		else {
			
			comment.append(" ")
					.append(getString(R.string.word_has))
					.append(" ")
					.append(Stringx.format("%s", getString(R.string.word_calls, history.size())), getClickSpans(listener))
					.append(". ");
		}
		
		return comment;
	}
	
	/**
	 * @return comment about last call type
	 */
	@Deprecated
	private @NotNull CharSequence commentLastCallType() {
		
		Spanner    comment    = new Spanner();
		Call       lastCall   = history.getLastCall();
		int        type       = lastCall.getCallType();
		int[]      callTypes  = Res.getCallTypes(type);
		List<Call> typedCalls = history.getCallsByTypes(callTypes);
		String     typeStr    = Res.getCallType(commentStore.getActivity(), type);
		
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
			
			ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), typedCalls, history.getContact().getName(), Stringx.format("%d %s", typedCalls.size(), typeStr));
			
			View.OnClickListener listener = view -> showCallsDialog.show();
			
			if (commentStore.isTurkishLanguage()) {
				
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
		
		return comment;
	}
	
	/**
	 * Generates a comment about the first and last call made to the contact.
	 *
	 * @return comment about first and last call
	 */
	@Deprecated
	private @NotNull CharSequence firstLastCallComment() {
		
		Spanner comment = new Spanner();
		
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
							.append(durationString, textSpans)
							.append(". ");
					
					if (isDurationGreaterThanHour) {
						
						comment.append(fmt("Yani bu kişiyle kabaca %s%s bir arama geçmişiniz var. ", longest.toString("%d %s"), WordExtension.getWordExt(longest.toString("%d %s"), Extension.TYPE_ABSTRACT)));
					}
					else {
						
						//+ The call history duration is less than 1-day.
						String msg = fmt("Yani bu kişiyle aranızda bir arama geçmişi olduğu pek söylenemez. ");
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
				if (isTurkish) msg = fmt("Bu kişi ile aranızda bir arama geçmişi yok. ");
				else msg = fmt("There is no communication history between you and this person. ");
				comment.append(msg);
			}
			
			comment.append(mostHistoryDurationComment());
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
		
		for (List<CallRank> rankList : map.values()) {
			
			for (CallRank callRank : rankList) {
				
				Call   call  = callRank.getCalls().get(0);
				String n     = call.getName();
				String name  = (n != null && !n.trim().isEmpty()) ? n : call.getNumber();
				int    size  = callRank.getCalls().size();
				int    order = callRank.getRank();
				var    data  = new MostCallItemViewData(name, size, order);
				
				if (CallKey.getContactId(call) == this.contact.getContactId()) data.setSelected(true);
				
				list.add(data);
			}
		}
		
		list.sort(Comparator.comparingInt(MostCallItemViewData::getCallSize).reversed());
		return list;
	}
	
	/**
	 * Returns a comment about the duration of the history.
	 *
	 * @return comment
	 */
	@NotNull
	private CharSequence mostHistoryDurationComment() {
		
		Spanner       comment  = new Spanner();
		List<Contact> contacts = getContacts();
		
		if (contacts == null || contacts.isEmpty()) {
			return comment;
		}
		
		//_ The list that has durations of all contacts
		List<Map.Entry<Contact, DurationGroup>> durationList = createContactHistoryDurationList(contacts);
		
		//! --------------------------------------------------------------
		int contactCount = contacts.size();
		int historyCount = durationList.size();
		
		comment.append(getHistoryCountComment(contactCount, historyCount));
		
		//+ winner item
		Map.Entry<Contact, DurationGroup> firstItem            = durationList.get(0);
		long                              firstContactId       = firstItem.getKey().getContactId();
		@NotNull List<MostDurationData>   mostDurationDataList = createMostHistoryDurationList(durationList);
		String                            title                = getString(R.string.title_most_call_history);
		String                            subtitle             = getString(R.string.size_contacts, mostDurationDataList.size());
		MostDurationDialog                dialog               = new MostDurationDialog(commentStore.getActivity(), mostDurationDataList, title, subtitle);
		View.OnClickListener              listener             = view -> dialog.show();
		boolean                           isWinner             = firstContactId == this.contact.getContactId();
		int                               rank                 = 0;
		
		int i = 0;
		for (Map.Entry<Contact, DurationGroup> item : durationList) {
			
			if (item.getKey().getContactId() == this.contact.getContactId()) {
				
				rank = i + 1;
				break;
			}
			
			i++;
		}
		
		if (isTurkish) {
			
			comment.append("Arama geçmişi süreleri", getClickSpans(listener))
					.append(fmt("ne göre bu kişi %d. sırada yer alıyor. ", rank));
			
		}
		else {
			
			comment.append("According to ")
					.append("the call history durations, ", getClickSpans(listener))
					.append(fmt(" this contact is in the %d. order. ", rank));
		}
		
		return comment;
	}
	
	/**
	 * Creates a list that consists of calculated history duration of each contact.
	 *
	 * @param contacts the list of contacts
	 * @return the list of contact history duration ordered by history duration in descending order.
	 * 		The first item has the most history duration.
	 */
	@NotNull
	private List<Map.Entry<Contact, DurationGroup>> createContactHistoryDurationList(@NotNull List<Contact> contacts) {
		
		List<Map.Entry<Contact, DurationGroup>> durationList = new ArrayList<>();
		
		for (Contact contact : contacts) {
			
			History history = callCollection.getHistoryOf(contact);
			
			if (history.isEmpty()) {
				
				//xlog.d("%s has no history", contact.getName());
				continue;
			}
			
			DurationGroup historyDuration = history.getHistoryDuration();
			
			if (!historyDuration.isZero())
				durationList.add(Map.entry(contact, historyDuration));
		}
		
		//_ Comparing by value makes the list in ascending order 
		durationList.sort(Map.Entry.comparingByValue());
		Collections.reverse(durationList);// descending order
		
		return durationList;
	}
	
	/**
	 * Creates a list of most duration items.
	 *
	 * @param durationList the list of duration
	 * @return the list of most duration items
	 */
	@NotNull
	private List<MostDurationData> createMostHistoryDurationList(@NotNull List<Map.Entry<Contact, DurationGroup>> durationList) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int order = 1;
		for (Map.Entry<Contact, DurationGroup> entry : durationList) {
			
			DurationGroup duration = entry.getValue();
			
			if (duration.isZero()) continue;
			
			Contact contact        = entry.getKey();
			String  durationString = getDurationString(duration, Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR);
			var     data           = new MostDurationData(contact.getName(), durationString, order++);
			
			if (this.contact.getContactId() == contact.getContactId()) data.setSelected(true);
			
			list.add(data);
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
		
		StringBuilder          sb        = new StringBuilder();
		@NotNull DurationGroup durations = duration.pickFrom(units);
		
		for (Duration _duration : durations.getDurations()) {
			
			if (_duration.isNotZero())
				sb.append(makePlural(_duration.toString("%d %s"), _duration.getValue())).append(" ");
		}
		
		return sb.toString().trim();
	}
	
	@NotNull
	private CharSequence getHistoryCountComment(int contactCount, int historyCount) {
		
		if (contactCount == historyCount)
			if (isTurkish) return "Rehberde bulunan herkesle bir arama geçmişiniz var. ";
			else
				return "You have a contact history with everyone in the contacts. ";
		
		if (isTurkish)
			return fmt("Rehberde bulunan %d kişi içinden %d kişi ile aranızda bir arama geçmişi var. ", contactCount, historyCount);
		else
			return fmt("There is a communication history between you and %d people out of %d people in your contacts. ", historyCount, contactCount);
	}
	
	@NotNull
	private CharSequence commentLastCallTypeRank() {
		
		Spanner rank     = new Spanner();
		Call    lastCall = history.getLastCall();
		
		
		return rank;
	}
	
	@NotNull
	private CharSequence commentOnDurations() {
		
		Spanner                      comment      = new Spanner();
		Map<Integer, List<CallRank>> rankMap      = DurationRanker.createRankMap(callCollection);
		int                          rank         = Ranker.getRank(contact, rankMap);
		List<MostDurationData>       durationList = createDurationList(rankMap);
		String                       title        = getString(R.string.title_speaking_durations);
		String                       subtitle     = getString(R.string.size_contacts, durationList.size());
		MostDurationDialog           dialog       = new MostDurationDialog(commentStore.getActivity(), durationList, title, subtitle);
		View.OnClickListener         listener     = view -> dialog.show();
		
		comment.append("Bu kişi ")
				.append("en çok konuştuğun", getClickSpans(listener))
				.append(" kişiler listesinde ")
				.append(fmt("%s. sırada. ", rank));
		
		return comment;
	}
	
	/**
	 * Creates a list of most duration items.
	 *
	 * @param rankMap the map of rank
	 * @return the list of most duration items
	 */
	@NotNull
	private List<MostDurationData> createDurationList(@NotNull Map<Integer, List<CallRank>> rankMap) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int rank = 1;
		while (true) {
			
			var rankList = rankMap.get(rank);
			
			if (rankList == null) break;
			
			for (CallRank callRank : rankList) {
				
				if (callRank.getDuration() == 0) continue;
				
				String name = callRank.getName();
				
				if (name == null || name.trim().isEmpty())
					name = getContactName(callRank.getCalls().get(0).getNumber());
				
				long contactId = contact.getContactId();
				
				var data = new MostDurationData(name, Time.formatSeconds((int) callRank.getDuration()), rank);
				
				if (contactId == CallKey.getContactId(callRank.getCalls().get(0)))
					data.setSelected(true);
				
				list.add(data);
			}
			
			rank++;
		}
		
		return list;
	}
	
	/**
	 * Returns the name of the contact.
	 *
	 * @param number the phone number
	 * @return the name or the number
	 */
	@NotNull
	private String getContactName(@NotNull String number) {
		
		List<Contact> contacts = getContacts();
		
		if (contacts == null || contacts.isEmpty())
			return number;
		
		
		for (Contact contact : contacts) {
			
			var numbers = ContactKey.getNumbers(contact);
			
			if (numbers == null || numbers.isEmpty()) continue;
			
			
			if (PhoneNumbers.existsNumber(numbers, number)) {
				
				String name = contact.getName();
				return name != null && !name.trim().isEmpty() ? name : number;
			}
		}
		
		return number;
	}
	
	
}
