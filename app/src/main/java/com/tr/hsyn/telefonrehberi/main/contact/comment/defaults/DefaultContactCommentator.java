package com.tr.hsyn.telefonrehberi.main.contact.comment.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.data.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.RankMap;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.CallDurationComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.HistoryDurationComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.LastCallComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.QuantityComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.Topic;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.duration.Duration;
import com.tr.hsyn.treadedwork.Threaded;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * This class implements the {@link ContactCommentator} interface and
 * provides default behavior for commenting on a contact.<br>
 * <p>
 * The calling to the {@link #commentOn(Contact, Consumer)} method
 * generates the comments about the contact.
 */
public class DefaultContactCommentator implements ContactCommentator, Threaded {
	
	/**
	 * The comment object.
	 * All generated comments by the commentator appends into this object.
	 */
	protected final Spanner                  comment                = new Spanner();
	/**
	 * Topics of the comment.
	 */
	private final   List<Topic>              TOPICS                 = Arrays.asList(
			Topic.QUANTITY,
			Topic.LAST_CALL,
			Topic.HISTORY_DURATION,
			Topic.CALL_DURATION
	);
	/** The count of the comment */
	private final   int                      COUNT_OF_COMMENT       = TOPICS.size();
	private final   Map<Topic, CharSequence> comments               = new HashMap<>();
	private final   QuantityComment          quantityComment        = new QuantityComment();
	private final   LastCallComment          lastCallComment        = new LastCallComment();
	private final   HistoryDurationComment   historyDurationComment = new HistoryDurationComment();
	private final   CallDurationComment      callDurationComment    = new CallDurationComment();
	private final   Object                   gate                   = new Object();
	/** The history that has the all calls of the current contact. */
	protected       History                  history;
	/** The current contact */
	protected       Contact                  contact;
	/** The comment store */
	protected       ContactCommentStore      commentStore;
	protected       boolean                  isTurkish;
	protected       CallLog                  callLog;
	private         Consumer<CharSequence>   callback;
	
	/**
	 * Constructs a new {@link DefaultContactCommentator} object with the given comment store.
	 *
	 * @param commentStore the comment store to use by this commentator
	 */
	public DefaultContactCommentator(@NotNull ContactCommentStore commentStore) {
		
		this.commentStore = commentStore;
		isTurkish         = commentStore.isTurkishLanguage();
		callLog           = getCallCollection();
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
	 * Returns the current contact that has been commented on.
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
		
		if (callLog != null) {
			
			this.contact = contact;
			this.history = callLog.getHistoryOf(contact);
			
			//if history is empty, no need to go any further.
			if (history.isEmpty()) {
				
				comment.append(commentStore.noHistory());
				returnComment();
			}
			else commentOnContact();
		}
		else {
			
			xlog.d("Not found the call collection");
			returnComment();
		}
	}
	
	/**
	 * Generates the comments based on the call history of the current contact.
	 * Invokes private methods to generate person comments.
	 * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
	 */
	private void commentOnContact() {
		
		// Here start to generate the comment.
		// The Call history is not 'null' and not empty at this point.
		
		xlog.dx("Accessed the call history [contact='%s', size=%d]", contact.getName(), history.size());
		
		if (history.size() == 1) {
			commentOnSingleCall();
		}
		else {
			quantityComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			lastCallComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			historyDurationComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
			callDurationComment.createComment(contact, commentStore.getActivity(), this::onComment, isTurkish);
		}
	}
	
	private void returnComment() {
		
		onMain(() -> callback.accept(comment));
	}
	
	/**
	 * Controls the creation of a comment.
	 *
	 * @param contactComment the comment
	 */
	private void onComment(@NotNull ContactComment contactComment) {
		
		synchronized (gate) {
			
			comments.put(contactComment.getTopic(), contactComment.getComment());
			boolean commentsCompleted = comments.size() == COUNT_OF_COMMENT;
			var     waitingComments   = new ArrayList<>(TOPICS);
			waitingComments.removeAll(comments.keySet());
			
			xlog.dx("Comment created : %s [commentCount=%d, commentsCompleted=%s, waitingComments=%s]", contactComment.getTopic(), comments.size(), commentsCompleted, waitingComments);
			
			if (commentsCompleted) {
				
				var commentList = comments.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByKey())
						.map(Map.Entry::getValue)
						.collect(Collectors.toList());
				
				Lister.loopWith(commentList, comment::append);
				
				returnComment();
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
		returnComment();
	}
	
	/**
	 * Appends a comment about the given call to the {@link #comment} object.
	 *
	 * @param call the call to comment on
	 */
	private void commentOnTheSingleCall(@NotNull Call call) {
		
		Duration             timeBefore = Time.howLongBefore(call.getTime());
		String               callType   = Res.Call.getCallType(commentStore.getActivity(), call.getCallType());
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
	private CharSequence commentOnDurations() {
		
		Spanner                comment      = new Spanner();
		RankMap                rankMap      = callLog.rankByDuration();
		int                    rank         = rankMap.getRank(contact);
		List<MostDurationData> durationList = createDurationList(rankMap);
		String                 title        = getString(R.string.title_speaking_durations);
		String                 subtitle     = getString(R.string.size_contacts, durationList.size());
		MostDurationDialog     dialog       = new MostDurationDialog(commentStore.getActivity(), durationList, title, subtitle);
		View.OnClickListener   listener     = view -> dialog.show();
		
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
	private List<MostDurationData> createDurationList(@NotNull RankMap rankMap) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int rank = 1;
		while (true) {
			
			var rankList = rankMap.getRank(rank);
			
			if (rankList.isEmpty()) break;
			
			for (CallRank callRank : rankList) {
				
				if (callRank.getDuration() == 0) continue;
				
				String name = callRank.getName();
				
				if (name == null || name.trim().isEmpty())
					name = getContactName(callRank.getCalls().get(0).getNumber());
				
				long contactId = contact.getContactId();
				
				var data = new MostDurationData(name, Time.formatSeconds((int) callRank.getDuration()), rank);
				
				if (contactId == Key.getContactId(callRank.getCalls().get(0)))
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
