package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import androidx.annotation.StringRes;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
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
import java.util.Objects;
import java.util.function.Consumer;


public class QuantityComment implements ContactComment {
	
	/**
	 * Main {@link CallLogs} object that has the all call log calls.
	 */
	private final CallLogs                 callLogs = getCallLogs();
	/**
	 * The comment object to add the all comments into.
	 */
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
	
	
	private @NotNull CharSequence getNoCallComment(List<Contact> contacts, int callType, @NotNull View.OnClickListener listener) {
		
		Spanner comment = new Spanner();
		
		switch (callType) {
			case Call.INCOMING:
			case Call.INCOMING_WIFI:
				if (isTurkish) {
					if (contacts.size() == 1) comment.append("Bu kişi, seni hiç aramayan tek kişi.\n");
					else comment.append("Bu kişi, ")
							.append("seni hiç aramayan", getClickSpans(listener))
							.append(fmt(" %d kişiden biri.\n", contacts.size()));
				}
				else {
					if (contacts.size() == 1) comment.append("This contact is the only one who never called you.\n");
					else comment.append(fmt("This contact one of the %d contacts ", contacts.size()))
							.append("who never called you", getClickSpans(listener))
							.append(".\n");
				}
				break;
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI:
				if (isTurkish) {
					if (contacts.size() == 1) comment.append("Bu kişi, hiç aramadığın tek kişi.\n");
					else comment.append("Bu kişi, ")
							.append("hiç aramadığın", getClickSpans(listener))
							.append(fmt(" %d kişiden biri.\n", contacts.size()));
				}
				else {
					if (contacts.size() == 1) comment.append("This contact is the only one you never called to.\n");
					else comment.append(fmt("This contact one of the %d contacts ", contacts.size()))
							.append("you never called", getClickSpans(listener))
							.append(" to.\n");
				}
				break;
			case Call.MISSED:
				if (isTurkish) {
					if (contacts.size() == 1) comment.append("Bu kişi, hiç cevapsız çağrısı olmayan tek kişi.\n");
					else comment.append("Bu kişi, hiç ")
							.append("cevapsız çağrısı olmayan", getClickSpans(listener))
							.append(fmt(" %d kişiden biri.\n", contacts.size()));
				}
				else {
					if (contacts.size() == 1) comment.append("This person is the only one with no missed calls.\n");
					else comment.append(fmt("This contact one of the %d contacts with", contacts.size()))
							.append("no missed calls", getClickSpans(listener))
							.append(" .\n");
				}
				break;
			case Call.REJECTED:
				if (isTurkish) {
					if (contacts.size() == 1) comment.append("Bu kişi, hiç bir aramasını reddetmediğin tek kişi.\n");
					else comment.append("Bu kişi, hiç bir")
							.append("aramasını reddetmediğin", getClickSpans(listener))
							.append(fmt(" %d kişiden biri.\n", contacts.size()));
				}
				else {
					if (contacts.size() == 1) comment.append("This is the only person you never rejected a call from.\n");
					else comment.append(fmt("This contact one of the %d contacts you", contacts.size()))
							.append("never rejected", getClickSpans(listener))
							.append(" a call from.\n");
				}
				break;
			default: throw new IllegalArgumentException("Unknown call type : " + callType);
		}
		
		return comment;
	}
	
	private void evaluateCalls() {
		
		assert this.callLogs != null;
		History history = callLogs.getHistoryOf(contact);
		
		//+ no any calls
		if (history.isEmpty()) {
			
			List<Contact> contactsHasNoCall = callLogs.getContactsByCalls(Objects::isNull);
			
			if (contactsHasNoCall.contains(contact)) {
				
				noCallsComment(contactsHasNoCall);
			}
		}
		//+ the history is not empty
		else {
			
			comment.append(getQuantityComment(history)).append("\n");
			//+ call ranks
			CallRank incomingRank = getCallRank(Call.INCOMING);
			CallRank outgoingRank = getCallRank(Call.OUTGOING);
			CallRank missedRank   = getCallRank(Call.MISSED);
			CallRank rejectedRank = getCallRank(Call.REJECTED);
			//+ ranks
			int iRank = incomingRank == null ? 0 : incomingRank.getRank();
			int oRank = outgoingRank == null ? 0 : outgoingRank.getRank();
			int mRank = missedRank == null ? 0 : missedRank.getRank();
			int rRank = rejectedRank == null ? 0 : rejectedRank.getRank();
			//+ no incoming
			if (iRank == 0) {
				if (mRank > 0 || rRank > 0) {
					if (mRank > 0 && rRank > 0) {
						
						List<Call> mCalls = this.callLogs.getCalls(CallType::isMissed);
						List<Call> rCalls = this.callLogs.getCalls(CallType::isRejected);
						
						if (isTurkish) comment.append("Bu kişinin hiç bir aramasını cevaplamadın. ")
								.append(fmt("Gelen %d aramayı reddettin ve %d aramayı da cevapsız bıraktın.\n", mCalls.size(), rCalls.size()));
						else comment.append("You did not answer any calls from this contact.")
								.append(fmt("You rejected %d calls and did not answer %d calls.\n", rCalls.size(), mCalls.size()));
					}
					else if (rRank > 0) {
						
						List<Call> rCalls = this.callLogs.getCalls(CallType::isRejected);
						if (isTurkish) comment.append("Bu kişinin hiç bir aramasını cevaplamadın. ")
								.append(fmt("Gelen %d aramayı da reddettin.\n", rCalls.size()));
						else comment.append("You did not answer any calls from this contact.")
								.append(fmt("You rejected %d calls.\n", rCalls.size()));
					}
					else {
						
						List<Call> mCalls = this.callLogs.getCalls(CallType::isMissed);
						if (isTurkish) comment.append("Bu kişinin hiç bir aramasını cevaplamadın. ")
								.append(fmt("Gelen %d aramayı da cevapsız bıraktın.\n", mCalls.size()));
						else comment.append("You did not answer any calls from this contact.")
								.append(fmt("You missed %d calls.\n", mCalls.size()));
					}
				}
				else {
					List<Contact>        hasNoCall = getContactsHasNoCall(this.callLogs.createByCallType(Call.INCOMING));
					View.OnClickListener listener  = v -> new ContactListDialog(getActivity(), hasNoCall, getString(R.string.no_incoming_calls), getString(R.string.size_contacts, hasNoCall.size())).show();
					comment.append(getNoCallComment(hasNoCall, Call.INCOMING, listener));
				}
			}
			//+ no outgoing
			if (oRank == 0) {
				List<Contact>        hasNoCall = getContactsHasNoCall(this.callLogs.createByCallType(Call.OUTGOING));
				View.OnClickListener listener  = v -> new ContactListDialog(getActivity(), hasNoCall, getString(R.string.no_outgoing_calls), getString(R.string.size_contacts, hasNoCall.size())).show();
				comment.append(getNoCallComment(hasNoCall, Call.OUTGOING, listener));
			}
			//+ no missed
			if (mRank == 0) {
				//+ no missed but has incoming and no rejected
				if (iRank > 0 && rRank == 0) {
					
					List<Call> iCalls = history.getIncomingCalls();
					
					if (iCalls.size() > 5) {
						
						View.OnClickListener listener = createCallListener(iCalls, R.string.incoming_calls);
						
						if (isTurkish) comment.append("Bu kişiden ")
								.append("gelen tüm aramaları ", getClickSpans(listener))
								.append("cevapladın.\n");
						else comment.append("You answered ")
								.append("all calls", getClickSpans(listener))
								.append(" from this contact.\n");
					}
				}
			}
			assert this.callLogs != null;
			//+ rejected
			if (rRank == 0 && iRank > 0) {//+ must be having an incoming call
				CallLogs             rejectedLogs = this.callLogs.createByCallType(Call.REJECTED);
				List<Contact>        hasNoCall    = rejectedLogs.getContactsByCalls(Objects::isNull);
				View.OnClickListener listener     = createContactListener(hasNoCall, R.string.no_rejected_calls);
				comment.append(getNoCallComment(hasNoCall, Call.REJECTED, listener));
			}
			//+ incoming
			if (iRank == 1) {
				Map<Integer, List<CallRank>> incomingRankMap  = createRankMap(Call.INCOMING);
				int                          iCount           = incomingRank.getRankCount();
				int                          size             = incomingRankMap.values().stream().map(List::size).reduce(0, Integer::sum);
				View.OnClickListener         incomingListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(incomingRankMap), getString(R.string.most_incoming_calls), getString(R.string.size_contacts, size)).show();
				comment.append(getComment(incomingListener, iCount, Call.INCOMING));
			}
			//+ outgoing
			if (oRank == 1) {
				Map<Integer, List<CallRank>> outgoingRankMap  = createRankMap(Call.OUTGOING);
				int                          oCount           = outgoingRank.getRankCount();
				int                          size             = outgoingRankMap.values().stream().map(List::size).reduce(0, Integer::sum);
				View.OnClickListener         outgoingListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(outgoingRankMap), getString(R.string.most_outgoing_calls), getString(R.string.size_contacts, size)).show();
				comment.append(getComment(outgoingListener, oCount, Call.OUTGOING));
			}
			//+ missed
			if (mRank == 1) {
				Map<Integer, List<CallRank>> missedRankMap  = createRankMap(Call.MISSED);
				int                          mCount         = missedRank.getRankCount();
				int                          size           = missedRankMap.values().stream().map(List::size).reduce(0, Integer::sum);
				View.OnClickListener         missedListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(missedRankMap), getString(R.string.most_missed_calls), getString(R.string.size_contacts, size)).show();
				comment.append(getComment(missedListener, mCount, Call.MISSED));
			}
			//+ rejected
			if (rRank == 1) {
				Map<Integer, List<CallRank>> rejectedRankMap  = createRankMap(Call.REJECTED);
				int                          rCount           = rejectedRank.getRankCount();
				int                          size             = rejectedRankMap.values().stream().map(List::size).reduce(0, Integer::sum);
				View.OnClickListener         rejectedListener = v -> new MostCallDialog(getActivity(), createMostCallItemList(rejectedRankMap), getString(R.string.most_rejected_calls), getString(R.string.size_contacts, size)).show();
				comment.append(getComment(rejectedListener, rCount, Call.REJECTED));
			}
		}
	}
	
	@NotNull
	private View.OnClickListener createContactListener(@NotNull List<Contact> contacts, @StringRes int title) {
		
		return v -> new ContactListDialog(getActivity(), contacts, getString(title), getString(R.string.size_contacts, contacts.size())).show();
	}
	
	@NotNull
	private View.OnClickListener createCallListener(List<Call> calls, @StringRes int title) {
		
		return v -> new ShowCallsDialog(activity, calls, getString(title), null).show();
	}
	
	/**
	 * Returns the comment.
	 *
	 * @param listener  listener
	 * @param rankCount rank count
	 * @param callType  call type
	 * @return the comment
	 */
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
		//@off
		assert this.callLogs != null;
		switch (callType) {
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return CallLogs.createRankMap(this.callLogs.getIncomingCalls(), Call.INCOMING);
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return CallLogs.createRankMap(this.callLogs.getOutgoingCalls(), Call.OUTGOING);
			case Call.MISSED:        return CallLogs.createRankMap(this.callLogs.getMissedCalls(), Call.MISSED);
			case Call.REJECTED:      return CallLogs.createRankMap(this.callLogs.getRejectedCalls(), Call.REJECTED);
			default:                 throw new IllegalArgumentException("Unknown call type: " + callType);
		}
		//@on
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
