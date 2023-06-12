package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;


import android.app.Activity;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankMate;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class MostQuantityComment implements ContactComment {
	
	private final CallCollection           callCollection = getCallCollection();
	private final Spanner                  comment        = new Spanner();
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	private       Contact                  contact;
	
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
		
		return Topic.MOST_QUANTITY;
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
		
		onBackground(() -> {
			
			RankList ranks = new RankList(callCollection.getNumberedCalls());
			ranks.makeQuantityRanks();
			
			Map<Integer, List<CallRank>> map      = ranks.getRankMap();
			RankMate                     rankMate = new RankMate(map);
			@Nullable List<String>       numbers  = ContactKey.getNumbers(contact);
			
			if (numbers != null) {
				
				int rank = rankMate.getRank(numbers);
				
				if (rank != -1) {
					
					List<CallRank> callRankList = map.get(rank);
					assert callRankList != null;
					int                                 rankCount = callRankList.size();
					@NotNull List<MostCallItemViewData> mostList  = createMostCallItemList(map);
					
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
	
	private void returnComment() {
		
		onMain(() -> callback.accept(this));
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
