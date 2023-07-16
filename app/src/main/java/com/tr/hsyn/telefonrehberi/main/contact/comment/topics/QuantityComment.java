package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.scaler.Quantity;
import com.tr.hsyn.scaler.Scaler;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLogs;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ContactListDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class QuantityComment implements ContactComment {
	
	private final CallLogs                 callLogs = getCallLogs();
	private final Spanner                  comment  = new Spanner();
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	private       Contact                  contact;
	private       boolean                  isTurkish;
	
	@Override
	public boolean isTurkish() {
		
		return isTurkish;
	}
	
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
		
		return Topic.QUANTITY;
	}
	
	@Override
	public void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish) {
		
		// region Initialization
		this.contact   = contact;
		this.callback  = callback;
		this.activity  = activity;
		this.isTurkish = isTurkish;
		
		if (callLogs == null) {
			
			xlog.d(activity.getString(R.string.call_collection_is_null));
			returnComment();
			return;
		}
		// endregion
		
		evaluateCalls();
		returnComment();
		
		/* onBackground(() -> {
			
			Map<Integer, List<CallRank>> rankMap = CallCollection.createRankMap(callCollection.getMapIdToCalls(), CallCollection.QUANTITY_COMPARATOR);
			int                          rank    = CallCollection.getRank(rankMap, contact);
			
			xlog.d("Rank=%d", rank);
			
			if (rank != -1) {
				
				List<CallRank> callRankList = rankMap.get(rank);
				assert callRankList != null;
				int                        rankCount = callRankList.size();
				List<MostCallItemViewData> mostList  = createMostCallItemList(rankMap);
				
				onMain(() -> {
					
					String         title    = getString(R.string.title_most_calls);
					String         subtitle = getString(R.string.size_contacts, mostList.size());
					MostCallDialog dialog = new MostCallDialog(getActivity(), mostList, title, subtitle);
					
					if (isTurkish) {
						
						comment.append("Ve ")
								.append("en fazla arama", getClickSpans(view -> dialog.show()))
								.append(" kaydına sahip kişiler listesinde ");
						
						if (rankCount <= 1) comment.append("tek başına ");
						else comment.append(fmt("%d kişi ile birlikte ", rankCount));
						
						comment.append(fmt("%d. sırada. ", rank));
					}
					else {
						
						comment.append(fmt("And in the %d. place ", rank));
						
						Spanner hotList = new Spanner().append("the hot list", getClickSpans(view -> dialog.show()));
						
						if (rankCount == 1) comment.append("alone ");
						else comment.append(fmt("together with %d contact(s) ", rankCount));
						
						comment.append("in ")
								.append(hotList)
								.append(" of the call quantity. ");
					}
					
					returnComment();
				});
			}
			else returnComment();
		}); */
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
	
	@NotNull
	private ContactListDialog createContactListDialog(@NotNull List<Contact> contacts, @NotNull String title, @NotNull String subtitle) {
		
		return new ContactListDialog(activity, contacts, title, subtitle);
	}
	
	/**
	 * Returns the list of contacts with no calls according to the given call logs.
	 *
	 * @return the list of contacts with no calls
	 */
	private @NotNull List<Contact> getContactsHasNoCall(@NotNull CallLogs callLogs) {
		
		List<Contact> contacts           = CallLogs.getContactsWithNumber();
		List<Contact> contactsHasNoCalls = new ArrayList<>();
		
		if (contacts == null) {
			
			xlog.d(activity.getString(R.string.can_not_access_the_contacts));
			return new ArrayList<>();
		}
		
		for (Contact contact : contacts) {
			
			List<Call> calls = callLogs.getMapIdToCalls().get(String.valueOf(contact.getId()));
			
			if (calls == null) contactsHasNoCalls.add(contact);
		}
		
		return contactsHasNoCalls;
	}
	
	private void evaluateCalls() {
		
		assert callLogs != null;
		History history = callLogs.getHistoryOf(contact);
		
		//+ no any calls
		if (history.isEmpty()) {
			
			List<Contact> contactsHasNoCall = getContactsHasNoCall(callLogs);
			
			if (contactsHasNoCall.contains(contact)) {
				
				noCallsComment(contactsHasNoCall);
			}
		}
		//+ the history is not empty
		else {
			
			comment.append(getQuantityComment(history)).append("\n");
			//+ call ranks
			var incomingRank = getCallRank(Call.INCOMING);
			var outgoingRank = getCallRank(Call.OUTGOING);
			var missedRank   = getCallRank(Call.MISSED);
			var rejectedRank = getCallRank(Call.REJECTED);
			//+ ranks
			int iRank = incomingRank == null ? 0 : incomingRank.getRank();
			int oRank = outgoingRank == null ? 0 : outgoingRank.getRank();
			int mRank = missedRank == null ? 0 : missedRank.getRank();
			int rRank = rejectedRank == null ? 0 : rejectedRank.getRank();
			//+ rank counts
			int iCount = incomingRank == null ? 0 : incomingRank.getRankCount();
			int oCount = outgoingRank == null ? 0 : outgoingRank.getRankCount();
			int mCount = missedRank == null ? 0 : missedRank.getRankCount();
			int rCount = rejectedRank == null ? 0 : rejectedRank.getRankCount();
			//+ rank maps
			var incomingRankMap = createRankMap(Call.INCOMING);
			var outgoingRankMap = createRankMap(Call.OUTGOING);
			var missedRankMap   = createRankMap(Call.MISSED);
			var rejectedRankMap = createRankMap(Call.REJECTED);
			//+ listeners
			View.OnClickListener incomingListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(incomingRankMap), getString(R.string.most_incoming_calls), fmt(" %d %s", incomingRankMap.size(), getString(R.string.contact))).show();
			View.OnClickListener outgoingListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(outgoingRankMap), getString(R.string.most_outgoing_calls), fmt(" %d %s", outgoingRankMap.size(), getString(R.string.contact))).show();
			View.OnClickListener missedListener   = v -> new MostCallDialog(getActivity(), createMostCallItemList(missedRankMap), getString(R.string.most_missed_calls), fmt(" %d %s", missedRankMap.size(), getString(R.string.contact))).show();
			View.OnClickListener rejectedListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(rejectedRankMap), getString(R.string.most_rejected_calls), fmt(" %d %s", rejectedRankMap.size(), getString(R.string.contact))).show();
			
			if (iRank == 1) {
				//+ incoming
				comment.append(getComment(incomingListener, iCount, Call.INCOMING));
			}
			
			if (oRank == 1) {
				//+ outgoing
				comment.append(getComment(outgoingListener, oCount, Call.OUTGOING));
			}
			
			if (mRank == 1) {
				//+ missed
				comment.append(getComment(missedListener, mCount, Call.MISSED));
			}
			
			if (rRank == 1) {
				//+ rejected
				comment.append(getComment(rejectedListener, rCount, Call.REJECTED));
			}
		}
	}
	
	private @NotNull CharSequence getComment(View.OnClickListener listener, int rankCount, int callType) {
		
		Spanner comment = new Spanner();
		
		switch (callType) {
			
			case Call.INCOMING:
				if (isTurkish) {
					
					comment.append("En çok arayanlar", getClickSpans(listener))
							.append(" listesinde ");
					
					if (rankCount == 1) comment.append("tek başına ");
					else comment.append(fmt("%d kişi ile birlikte ", rankCount - 1));
					
					comment.append("birinci sırada.\n");
				}
				else {
					
					comment.append("In the first place ");
					
					if (rankCount == 1) comment.append("alone ");
					else comment.append(fmt("together with %d contact(s) ", rankCount - 1));
					
					comment.append("in the ")
							.append("most incoming", getClickSpans(listener))
							.append(" call list.\n");
				}
				break;
			case Call.OUTGOING:
				if (isTurkish) {
					
					comment.append("En çok arananlar", getClickSpans(listener))
							.append(" listesinde ");
					
					if (rankCount == 1) comment.append("tek başına ");
					else comment.append(fmt("%d kişi ile birlikte ", rankCount - 1));
					
					comment.append("birinci sırada.\n");
				}
				else {
					
					comment.append("In the first place ");
					
					if (rankCount == 1) comment.append("alone ");
					else comment.append(fmt("together with %d contact(s) ", rankCount - 1));
					
					comment.append("in the ")
							.append("most outgoing", getClickSpans(listener))
							.append(" call list.\n");
					
				}
				break;
			case Call.MISSED:
				if (isTurkish) {
					
					comment.append("En çok çağrı bırakanlar", getClickSpans(listener))
							.append(" listesinde ");
					
					if (rankCount == 1) comment.append("tek başına ");
					else comment.append(fmt("%d kişi ile birlikte ", rankCount - 1));
					
					comment.append("birinci sırada.\n");
				}
				else {
					
					comment.append("In the first place ");
					
					if (rankCount == 1) comment.append("alone ");
					else comment.append(fmt("together with %d contact(s) ", rankCount - 1));
					
					comment.append("in the ")
							.append("most missed", getClickSpans(listener))
							.append(" call list.\n");
				}
				break;
			case Call.REJECTED:
				if (isTurkish) {
					
					comment.append("En çok reddedilenler", getClickSpans(listener))
							.append(" listesinde ");
					
					if (rankCount == 1) comment.append("tek başına ");
					else comment.append(fmt("%d kişi ile birlikte ", rankCount - 1));
					
					comment.append("birinci sırada.\n");
				}
				else {
					
					comment.append("In the first place ");
					
					if (rankCount == 1) comment.append("alone ");
					else comment.append(fmt("together with %d contact(s) ", rankCount - 1));
					
					comment.append("in the ")
							.append("most rejected", getClickSpans(listener))
							.append(" call list.\n");
				}
				break;
		}
		
		return comment;
	}
	
	/**
	 * Returns the call rank of the given call type for the contact.
	 *
	 * @param callType the call type
	 * @return the call rank or {@code null} if not found
	 */
	@Nullable
	private CallRank getCallRank(int callType) {
		
		Map<Integer, List<CallRank>> rankMap;
		
		switch (callType) {
			
			case Call.INCOMING:
			case Call.INCOMING_WIFI:
				rankMap = createRankMap(Call.INCOMING);
				break;
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI:
				rankMap = createRankMap(Call.OUTGOING);
				break;
			case Call.MISSED:
				rankMap = createRankMap(Call.MISSED);
				break;
			case Call.REJECTED:
				rankMap = createRankMap(Call.REJECTED);
				break;
			default: throw new IllegalArgumentException("Unknown call type : " + callType);
		}
		
		int            rank      = CallLogs.getRank(rankMap, contact);
		List<CallRank> candidate = rankMap.get(rank);
		CallRank       callRank  = CallLogs.getCallRank(rankMap, rank, contact);
		
		if (callRank != null) {
			
			callRank.setContact(contact);
			//noinspection DataFlowIssue
			callRank.setRankCount(candidate.size());
		}
		
		return callRank;
	}
	
	private @NotNull Map<Integer, List<CallRank>> createRankMap(int callType) {
		
		assert this.callLogs != null;
		switch (callType) {
			
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return CallLogs.create(this.callLogs.getIncomingCalls()).makeRank();
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return CallLogs.create(this.callLogs.getOutgoingCalls()).makeRank();
			case Call.MISSED: return CallLogs.create(this.callLogs.getMissedCalls()).makeRank();
			case Call.REJECTED: return CallLogs.create(this.callLogs.getRejectedCalls()).makeRank();
			default: throw new IllegalArgumentException("Unknown call type: " + callType);
		}
	}
	
	/**
	 * Comments the contact, which has the most incoming calls.
	 *
	 * @param history the history
	 */
	private void incomingComment(@NotNull History history) {
		
		List<Call> incomingCalls = history.getIncomingCalls();
		//+ create a call logs with only incoming calls
		assert this.callLogs != null;
		CallLogs                     incomingCallLogs = CallLogs.create(this.callLogs.getIncomingCalls());
		Map<Integer, List<CallRank>> rankMap          = incomingCallLogs.makeRank();
		int                          rank             = CallLogs.getRank(rankMap, contact);
		List<CallRank>               candidate        = rankMap.get(rank);
		
		if (incomingCalls.isEmpty()) {
			
			noIncomingComment(getContactsHasNoCall(incomingCallLogs));
		}
		else {
			//+ incoming calls are not empty
			//+ from the most callers or from the fewest callers?
			
			if (rank == 1) {
				//+ from the most callers
				
				View.OnClickListener listener = v -> new MostCallDialog(getActivity(), createMostCallItemList(rankMap), getString(R.string.most_incoming_calls), String.valueOf(rankMap.size()));
				
				if (isTurkish) {
					
					comment.append("Bu kişi seni ")
							.append(fmt("en çok arayan"), getClickSpans(listener));
					
					assert candidate != null;
					if (candidate.size() == 1) {
						
						comment.append(" kişi. ");
					}
					else {
						
						comment.append(fmt(" %d kişiden biri. ", candidate.size()));
					}
				}
				else {
					assert candidate != null;
					if (candidate.size() == 1) {
						comment.append("This contact is")
								.append("the most caller", getClickSpans(listener))
								.append(" to you. ");
					}
					else {
						
						comment.append(fmt("The contact is the one of %d contact who is ", candidate.size()))
								.append("the most caller", getClickSpans(listener))
								.append(" to you. ");
					}
				}
			}
		}
	}
	
	/**
	 * Comments the contact, which has the most outgoing calls.
	 *
	 * @param history the history of the contact
	 */
	private void mostOutgoingComment(@NotNull History history) {
		
		List<Call> outgoingCalls = history.getOutgoingCalls();
		//+ create a call logs with only incoming calls
		assert this.callLogs != null;
		CallLogs                     outgoingCallLogs = CallLogs.create(this.callLogs.getOutgoingCalls());
		Map<Integer, List<CallRank>> rankMap          = outgoingCallLogs.makeRank();
		int                          rank             = CallLogs.getRank(rankMap, contact);
		List<CallRank>               candidate        = rankMap.get(rank);
		
		
		if (outgoingCalls.isEmpty()) {
			
			noOutgoingComment(getContactsHasNoCall(outgoingCallLogs));
		}
		else {
			//+ incoming calls are not empty
			//+ from the most callers or from the fewest callers?
			
			if (rank == 1) {
				//+ from the most callers
				
				View.OnClickListener listener = v -> new MostCallDialog(getActivity(), createMostCallItemList(rankMap), getString(R.string.most_incoming_calls), String.valueOf(rankMap.size()));
				
				if (isTurkish) {
					
					comment.append("Bu kişi ")
							.append(fmt("en çok aradığın"), getClickSpans(listener));
					
					assert candidate != null;
					if (candidate.size() == 1) {
						
						comment.append(" kişi. ");
					}
					else {
						
						comment.append(fmt(" %d kişiden biri. ", candidate.size()));
					}
				}
				else {
					assert candidate != null;
					if (candidate.size() == 1) {
						comment.append("This contact is the contact that ")
								.append("your most calling to", getClickSpans(listener))
								.append(" . ");
					}
					else {
						
						comment.append(fmt("The contact is the one of %d contact who is ", candidate.size()))
								.append("your most calling to", getClickSpans(listener))
								.append(" to you. ");
					}
				}
			}
		}
	}
	
	/**
	 * The comment for the contacts, which have no any incoming call.
	 *
	 * @param contactsHasNoCall the list of contacts with no incoming call
	 */
	private void noIncomingComment(List<Contact> contactsHasNoCall) {
		
		if (isTurkish) {
			
			if (contactsHasNoCall.size() == 1) {
				
				comment.append("Bu kişi, rehberinde seni aramayan tek kişi. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCall, "Aramayanlar", fmt("%d Kişi", contactsHasNoCall.size()));
				
				comment.append("Bu kişi, rehberinde ")
						.append("seni hiç aramayan", getClickSpans(view -> dialog.show()))
						.append(fmt(" %d kişiden biri. ", contactsHasNoCall.size()));
			}
		}
		else {
			if (contactsHasNoCall.size() == 1) {
				
				comment.append("This contact is the only contact who do not call you in your contacts. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCall, "Contacts Who Do Not Call You", fmt("%d Contacts", contactsHasNoCall.size()));
				
				comment.append(fmt("The contact is the one of %d contacts who ", contactsHasNoCall.size()))
						.append("do not call you", getClickSpans(view -> dialog.show()))
						.append(" . ");
			}
		}
	}
	
	private void noOutgoingComment(List<Contact> contactsHasNoCall) {
		
		if (isTurkish) {
			
			if (contactsHasNoCall.size() == 1) {
				
				comment.append("Bu kişi, rehberdeki aramadığın tek kişi. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCall, "Aranmayanlar", fmt("%d Kişi", contactsHasNoCall.size()));
				
				comment.append("Bu kişi, rehberindeki ")
						.append("hiç aranmayan", getClickSpans(view -> dialog.show()))
						.append(fmt(" %d kişiden biri. ", contactsHasNoCall.size()));
			}
		}
		else {
			if (contactsHasNoCall.size() == 1) {
				
				comment.append("This contact is the only contact that you do not call in your contacts. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCall, "Contacts That You Do Not Call", fmt("%d Contacts", contactsHasNoCall.size()));
				
				comment.append(fmt("The contact is the one of %d contacts that ", contactsHasNoCall.size()))
						.append(" you do not call", getClickSpans(view -> dialog.show()))
						.append(" . ");
			}
		}
	}
	
	/**
	 * The comment for the contacts, which have no any call.
	 *
	 * @param contactsHasNoCalls the list of contacts with no any call
	 */
	private void noCallsComment(List<Contact> contactsHasNoCalls) {
		
		if (isTurkish) {
			
			if (contactsHasNoCalls.size() == 1) {
				
				comment.append("Bu kişi, rehberinde arama kaydı olmayan tek kişi. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCalls, "Arama Kaydı Olmayanlar", fmt("%d Kişi", contactsHasNoCalls.size()));
				
				comment.append("Bu kişi, rehberinde ")
						.append("arama kaydı olmayan", getClickSpans(view -> dialog.show()))
						.append(fmt(" %d kişiden biri. ", contactsHasNoCalls.size()));
			}
		}
		else {
			if (contactsHasNoCalls.size() == 1) {
				
				comment.append("This contact is the only contact that has no any call logs in your contacts. ");
			}
			else {
				ContactListDialog dialog = createContactListDialog(contactsHasNoCalls, "Contacts Without Call Log", fmt("%d Contacts", contactsHasNoCalls.size()));
				
				comment.append(fmt("The contact is the one of %d contacts that has ", contactsHasNoCalls.size()))
						.append("no any call", getClickSpans(view -> dialog.show()))
						.append(" logs. ");
			}
		}
	}
	
	/**
	 * Returns the contacts that no any incoming calls.
	 *
	 * @return the contacts that no any incoming calls
	 */
	private @Nullable List<Contact> getContactsHasNoIncoming() {
		
		assert callLogs != null;
		
		List<Contact> contacts              = CallLogs.getContactsWithNumber();
		List<Contact> contactsHasNoIncoming = new ArrayList<>();
		
		if (contacts == null) {
			
			xlog.d(activity.getString(R.string.can_not_access_the_contacts));
			return null;
		}
		
		CallLogs incomingCallsLog = CallLogs.create(callLogs.getIncomingCalls());
		
		for (Contact contact : contacts) {
			
			List<Call> calls = incomingCallsLog.getMapIdToCalls().get(String.valueOf(contact.getId()));
			
			if (calls == null) contactsHasNoIncoming.add(contact);
		}
		
		return contactsHasNoIncoming;
	}
	
	private void evaluateIncoming() {
		
		assert callLogs != null;
		History    history = callLogs.getHistoryOf(contact);
		List<Call> calls   = history.getIncomingCalls();
		
		if (calls.isEmpty()) {
			
			return;
		}
		
		Map<Integer, List<CallRank>> incomingRankMap = callLogs.getMostIncoming();
		
		if (!incomingRankMap.isEmpty()) {
			
			int            rank   = CallLogs.getRank(incomingRankMap, contact);
			List<CallRank> winner = incomingRankMap.get(1);
			assert winner != null;
			int rankCount = winner.size();
			
			if (rank == 1) {
				
				List<MostCallItemViewData> mostList = createMostCallItemList(incomingRankMap);
				String                     title    = getString(R.string.title_most_incoming_calls);
				String                     subtitle = getString(R.string.size_contacts, mostList.size());
				MostCallDialog             dialog   = new MostCallDialog(activity, mostList, title, subtitle);
				
				if (isTurkish()) {
					
					comment.append(fmt("%s", contact.getName()))
							.append(" seni ")
							.append("en çok arayan", getClickSpans(view -> dialog.show()));
					
					if (rankCount == 1) comment.append(" kişi. ");
					else comment.append(fmt("%d kişiden biri. ", winner.size()));
				}
				else {
					
					comment.append(fmt("%s", contact.getName()));
					
					if (winner.size() == 1) comment.append(" is the person ");
					else comment.append(" is one of the persons");
					
					comment.append("who calls you the most", getClickSpans(view -> dialog.show()))
							.append(". ");
				}
			}
			else {
				
				Quantity quantity = Scaler.makeQuantity(calls.size(), 10, 6f);
				
				if (quantity.isMin()) {
					
					if (isTurkish) {
						
						comment.append("Bu kişi seni en az arayanlardan biri. ");
					}
					else {
						
						comment.append("This person is one of the least calling for you. ");
					}
				}
			}
		}
	}
	
	@NotNull
	private CharSequence getQuantityComment(History history) {
		
		Spanner              comment  = new Spanner();
		String               name     = contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName()) ? contact.getName() : Stringx.toTitle(getString(R.string.word_contact));
		View.OnClickListener listener = view -> new ShowCallsDialog(activity, history.getCalls(), contact.getName(), null).show();
		
		comment.append(name, Spans.bold(), Spans.foreground(getTextColor()));
		
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
				
				Call                 call  = callRank.getCalls().get(0);
				String               n     = call.getName();
				String               name  = (n != null && !n.trim().isEmpty()) ? n : call.getNumber();
				int                  size  = callRank.getCalls().size();
				int                  order = callRank.getRank();
				MostCallItemViewData data  = new MostCallItemViewData(name, size, order);
				
				if (CallKey.getContactId(call) == contact.getContactId()) data.setSelected(true);
				
				list.add(data);
			}
		}
		
		list.sort(Comparator.comparingInt(MostCallItemViewData::getCallSize).reversed());
		return list;
	}
}
