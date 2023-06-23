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
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
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
	
	private final CallCollection           callCollection = getCallCollection();
	private final Spanner                  comment        = new Spanner();
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
		
		if (callCollection == null) {
			
			xlog.d(activity.getString(R.string.call_collection_is_null));
			returnComment();
			return;
		}
		// endregion
		
		//comment.append(getQuantityComment(isTurkish));
		evaluateIncoming();
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
	
	/**
	 * Returns the list of contacts with no calls.
	 *
	 * @return the list of contacts with no calls
	 */
	private @Nullable List<Contact> getContactHasNoCalls() {
		
		assert callCollection != null;
		
		List<Contact> contacts           = callCollection.getContacts();
		List<Contact> contactsHasNoCalls = new ArrayList<>();
		
		if (contacts == null) {
			
			xlog.d(activity.getString(R.string.can_not_access_the_contacts));
			return null;
		}
		
		for (Contact contact : contacts) {
			
			//+ skip contacts that have no number
			if (hasNumber(contact)) {
				
				var calls = callCollection.getMapIdToCalls().get(String.valueOf(contact.getId()));
				
				if (calls == null) contactsHasNoCalls.add(contact);
			}
		}
		
		return contactsHasNoCalls;
	}
	
	private void evaluateCalls() {
		
		List<Contact> contactsHasNoCalls = getContactHasNoCalls();
		
		if (contactsHasNoCalls == null) return;
		
		if (contactsHasNoCalls.contains(contact)) {
			
			xlog.d("Contact has no any calls");
			
			
			if (isTurkish) {
				
				
			}
			else {
				
				if (contactsHasNoCalls.size() == 1) {
					
					comment.append("This contact is the only contact that has no any call logs in your contacts. ");
				}
				else {
					
					
					comment.append(fmt("The contact is the one of %d contacts that has ", contactsHasNoCalls.size()))
							.append("no any call")
							.append(" logs. ");
				}
				
				
			}
			
		}
	}
	
	/**
	 * Checks if the contact has any real phone number.
	 *
	 * @param contact the contact
	 * @return {@code true} if the contact has a number
	 */
	private boolean hasNumber(@NotNull Contact contact) {
		
		var numbers = ContactKey.getNumbers(contact);
		
		if (numbers == null || numbers.isEmpty()) return false;
		
		boolean hasNumber = false;
		for (var number : numbers) {
			
			if (PhoneNumbers.isPhoneNumber(number)) {
				
				hasNumber = true;
				break;
			}
		}
		
		return hasNumber;
	}
	
	/**
	 * Returns the contacts that no any incoming calls.
	 *
	 * @return the contacts that no any incoming calls
	 */
	private @Nullable List<Contact> getContactsHasNoIncoming() {
		
		assert callCollection != null;
		
		List<Contact> contacts              = callCollection.getContacts();
		List<Contact> contactsHasNoIncoming = new ArrayList<>();
		
		if (contacts == null) {
			
			xlog.d(activity.getString(R.string.can_not_access_the_contacts));
			return null;
		}
		
		List<Call> incomingCalls = callCollection.getIncomingCalls();
		
		for (Contact contact : contacts) {
			
			if (hasNumber(contact)) {
				
				var calls = callCollection.getMapIdToCalls().get(String.valueOf(contact.getId()));
				
				if (calls == null) contactsHasNoIncoming.add(contact);
			}
		}
		
		return contactsHasNoIncoming;
	}
	
	private void evaluateIncoming() {
		
		assert callCollection != null;
		History    history = callCollection.getHistoryOf(contact);
		List<Call> calls   = history.getIncomingCalls();
		
		if (calls.isEmpty()) {
			
			return;
		}
		
		Map<Integer, List<CallRank>> incomingRankMap = callCollection.getMostIncoming();
		
		if (!incomingRankMap.isEmpty()) {
			
			int            rank   = CallCollection.getRank(incomingRankMap, contact);
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
	private CharSequence getQuantityComment(boolean isTurkish) {
		
		Spanner comment = new Spanner();
		assert callCollection != null;
		History              history  = callCollection.getHistoryOf(contact);
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
