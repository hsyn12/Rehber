package com.tr.hsyn.telefonrehberi.main.call.activity;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtoolbar.Toolbarx;
import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.RankMap;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.data.CallLog;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Shows the most calls by a filter.<br>
 * The filters can be found in {@link CallLog}.
 * For example, the most incoming calls filter is {@link CallLog#FILTER_MOST_INCOMING}.
 * When this activity is started with this filter, it will show the most incoming calls.
 * The activity finds the filter from the key {@link Key#MOST_CALLS_FILTER_TYPE}
 * by calling the {@link Blue#getObject(Key)} method.
 * So,
 * only the one thing to do is to set the key {@link Key#MOST_CALLS_FILTER_TYPE} with the filter.
 */
public class MostCallsActivity extends ActivityView {
	
	/** The filter */
	private final int                     FILTER  = Objects.requireNonNull(Blue.getObject(Key.MOST_CALLS_FILTER_TYPE));
	private final CallLog                 callLog = Blue.getObject(Key.CALL_LOG);
	private       List<Call>              filteredCalls;
	private       ProgressBar             progressBar;
	private       Drawable                imgType;
	private       String                  textType;
	private       View                    emptyView;
	private       RecyclerView            list;
	private       List<MostCallsItemData> data;
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.most_calls_activity;
	}
	
	@Override
	protected void onCreate() {
		
		progressBar = findView(R.id.most_calls_progress);
		list        = findView(R.id.most_calls_list);
		emptyView   = findView(R.id.empty);
		
		setToolbar();
		
		showProgress();
		Work.on(() -> {
					filter();
					
					List<CallRank> ranks = makeRank();
					
					if (ranks != null) return makeItemData(ranks);
					
					return new ArrayList<MostCallsItemData>(0);
				})
				.onSuccess(this::showList)
				.onError(xlog::e)
				.onLast(this::hideProgress)
				.execute();
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideUp(this);
	}
	
	private void setToolbar() {
		
		Toolbar toolbar = findView(R.id.most_calls_toolbar);
		
		Toolbarx.setToolbar(this, toolbar, this::onBackPressed);
		
		assert getSupportActionBar() != null;
		getSupportActionBar().setTitle(CallLog.getCallFilterName(this, FILTER));
	}
	
	/**
	 * Sets the list by the filter.
	 */
	private void filter() {
		
		switch (FILTER) {
			
			case CallLog.FILTER_MOST_INCOMING:
				imgType = AppCompatResources.getDrawable(this, R.drawable.incoming_call);
				filteredCalls = callLog.incomingCalls();
				textType = getString(R.string.call_type_incoming);
				break;
			case CallLog.FILTER_MOST_OUTGOING:
				imgType = AppCompatResources.getDrawable(this, R.drawable.outgoing_call);
				filteredCalls = callLog.outgoingCalls();
				textType = getString(R.string.call_type_outgoing);
				break;
			case CallLog.FILTER_MOST_MISSED:
				imgType = AppCompatResources.getDrawable(this, R.drawable.missed_call);
				filteredCalls = callLog.missedCalls();
				textType = getString(R.string.call_type_missed);
				break;
			case CallLog.FILTER_MOST_REJECTED:
				imgType = AppCompatResources.getDrawable(this, R.drawable.rejected_call);
				filteredCalls = callLog.rejectedCalls();
				textType = getString(R.string.call_type_rejected);
				break;
			case CallLog.FILTER_MOST_SPEAKING:
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = callLog.getCalls(c -> c.isIncoming() && c.isSpoken());
				break;
			case CallLog.FILTER_MOST_TALKING:
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = callLog.getCalls(c -> c.isOutgoing() && c.isSpoken());
				break;
			
			default:
				xlog.w("There is no proper type for filter : %d [%s]", FILTER, CallLog.getCallFilterName(this, FILTER));
		}
	}
	
	/**
	 * Makes a list of {@link CallRank}.
	 * A rank is information about the calls for a phone number.
	 * A list is created, and it is ordered by duration or by quantity.
	 * And each phone number gets a rank in this ordering.
	 *
	 * @return the list of {@link CallRank}
	 */
	@Nullable
	private List<CallRank> makeRank() {
		
		if (filteredCalls == null || filteredCalls.isEmpty()) {
			
			xlog.d("There is no filtered calls");
			return null;
		}
		
		xlog.d("Filter : %d", FILTER);
		
		return (FILTER == CallLog.FILTER_MOST_SPEAKING || FILTER == CallLog.FILTER_MOST_TALKING) ?
				CallLog.createRankListByDuration(filteredCalls) :
				new RankMap(CallLog.rankByQuantity(filteredCalls)).getCallRanks();
	}
	
	/**
	 * Converts a list of {@link CallRank} to a list of {@link MostCallsItemData}.
	 *
	 * @param ranks the list of {@link CallRank}
	 * @return the list of {@link MostCallsItemData}
	 */
	private List<MostCallsItemData> makeItemData(@NotNull List<CallRank> ranks) {
		
		return ranks.stream().map(this::createItemData).collect(Collectors.toList());
	}
	
	/**
	 * Shows the list of {@link MostCallsItemData}.
	 *
	 * @param data the list of {@link MostCallsItemData}
	 */
	private void showList(@NotNull List<MostCallsItemData> data) {
		
		this.data = data;
		
		if (!data.isEmpty()) {
			
			emptyView.setVisibility(View.GONE);
			
			list.setAdapter(new MostCallsAdapter(data, this::onClickItem));
		}
		else {
			emptyView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Converts a {@link CallRank} to a {@link MostCallsItemData}.
	 *
	 * @param callRank the {@link CallRank}
	 * @return the {@link MostCallsItemData}
	 */
	@NotNull
	private MostCallsItemData createItemData(@NotNull CallRank callRank) {
		
		TextDrawable rank = TextDrawable.builder()
				.buildRound(String.valueOf(callRank.getRank()), Colors.COLOR_GENERATOR.getRandomColor());
		
		String txt;
		
		if (FILTER == CallLog.FILTER_MOST_SPEAKING || FILTER == CallLog.FILTER_MOST_TALKING) {
			
			txt = Time.formatSeconds((int) (callRank.getIncomingDuration() + callRank.getOutgoingDuration()));
		}
		else {
			
			txt = callRank.size() + " " + textType;
		}
		
		return new MostCallsItemData(callRank.getName(), txt, imgType, rank);
	}
	
	/**
	 * Callback for the click event of the list item.
	 *
	 * @param index the index of the list item to be clicked
	 */
	private void onClickItem(int index) {
		
		MostCallsItemData item = data.get(index);
		
		xlog.d(item);
		
		
	}
	
	private void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
	}
	
	private void showProgress() {
		
		progressBar.setVisibility(View.VISIBLE);
	}
	
}
