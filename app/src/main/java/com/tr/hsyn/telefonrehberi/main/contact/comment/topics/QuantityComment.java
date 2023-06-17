package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;
import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.Ranker;
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
	
	@Override
	public boolean isTurkish() {
		
		return false;
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
		
		this.contact  = contact;
		this.callback = callback;
		this.activity = activity;
		
		if (callCollection == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		
		comment.append(getQuantityComment(isTurkish));
		
		onBackground(() -> {
			
			Map<Integer, List<CallRank>> rankMap = Ranker.createRankMap(callCollection.getMapNumberToCalls(), Ranker.QUANTITY_COMPARATOR);
			List<String>                 numbers = ContactKey.getNumbers(contact);
			
			if (numbers != null) {
				
				int rank = Ranker.getRank(contact, rankMap);
				
				if (rank != -1) {
					
					List<CallRank> callRankList = rankMap.get(rank);
					assert callRankList != null;
					int                                 rankCount = callRankList.size();
					@NotNull List<MostCallItemViewData> mostList  = createMostCallItemList(rankMap);
					
					onMain(() -> {
						
						String         title    = getString(R.string.title_most_calls);
						String         subtitle = getString(R.string.size_contacts, mostList.size());
						MostCallDialog dialog   = new MostCallDialog(getActivity(), mostList, title, subtitle);
						
						//xlog.w("rank=%d, rankCount=%d", rank, rankCount);
						
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
			}
			else returnComment();
		});
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
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
				
				Call   call  = callRank.getCalls().get(0);
				String n     = call.getName();
				String name  = (n != null && !n.trim().isEmpty()) ? n : call.getNumber();
				int    size  = callRank.getCalls().size();
				int    order = callRank.getRank();
				var    data  = new MostCallItemViewData(name, size, order);
				
				if (CallKey.getContactId(call) == contact.getContactId()) data.setSelected(true);
				
				list.add(data);
			}
		}
		
		list.sort(Comparator.comparingInt(MostCallItemViewData::getCallSize).reversed());
		return list;
	}
}
