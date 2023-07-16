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
import com.tr.hsyn.telefonrehberi.main.call.Group;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLogs;
import com.tr.hsyn.telefonrehberi.main.call.data.CallOver;
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
 * The filters can be found in {@link CallLogs}.
 * For example, the most incoming calls filter is {@link CallLogs#FILTER_MOST_INCOMING}.
 * When this activity is started with this filter, it will show the most incoming calls.
 * The activity finds the filter from the key {@link Key#MOST_CALLS_FILTER_TYPE}
 * by calling the {@link Blue#getObject(Key)} method.
 * So,
 * only the one thing to do is to set the key {@link Key#MOST_CALLS_FILTER_TYPE} with the filter.
 * This is not so haard.
 */
public class MostCallsActivity extends ActivityView {
	
	/** The filter */
	private final int                     FILTER   = Objects.requireNonNull(Blue.getObject(Key.MOST_CALLS_FILTER_TYPE));
	private final CallLogs                callLogs = Blue.getObject(Key.CALL_LOGS);
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
					
					List<Group<Call>> ranks = makeRank();
					
					if (ranks != null) return makeItemData(ranks);
					
					return new ArrayList<MostCallsItemData>(0);
				})
				.onSuccess(this::showList)
				.onError(xlog::e)
				.onLast(this::hideProgress);
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
		getSupportActionBar().setTitle(CallLogs.getCallFilterName(this, FILTER));
	}
	
	private void filter() {
		
		switch (FILTER) {
			
			case CallLogs.FILTER_MOST_INCOMING:
				imgType = AppCompatResources.getDrawable(this, R.drawable.incoming_call);
				filteredCalls = callLogs.getIncomingCalls();
				textType = getString(R.string.call_type_incoming);
				break;
			case CallLogs.FILTER_MOST_OUTGOING:
				imgType = AppCompatResources.getDrawable(this, R.drawable.outgoing_call);
				filteredCalls = callLogs.getOutgoingCalls();
				textType = getString(R.string.call_type_outgoing);
				break;
			case CallLogs.FILTER_MOST_MISSED:
				imgType = AppCompatResources.getDrawable(this, R.drawable.missed_call);
				filteredCalls = callLogs.getMissedCalls();
				textType = getString(R.string.call_type_missed);
				break;
			case CallLogs.FILTER_MOST_REJECTED:
				imgType = AppCompatResources.getDrawable(this, R.drawable.rejected_call);
				filteredCalls = callLogs.getRejectedCalls();
				textType = getString(R.string.call_type_rejected);
				break;
			case CallLogs.FILTER_MOST_SPEAKING:
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = callLogs.getCalls(c -> c.isIncoming() && c.isSpoken());
				break;
			case CallLogs.FILTER_MOST_TALKING:
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = callLogs.getCalls(c -> c.isOutgoing() && c.isSpoken());
				break;
			
			default:
				xlog.w("There is no proper type for filter : %d [%s]", FILTER, CallLogs.getCallFilterName(this, FILTER));
		}
	}
	
	@Nullable
	private List<Group<Call>> makeRank() {
		
		if (filteredCalls == null || filteredCalls.isEmpty()) {
			
			xlog.d("There is no filtered calls");
			return null;
		}
		
		xlog.d("Filter : %d", FILTER);
		
		List<Group<Call>> groups = CallOver.groupByNumber(filteredCalls);
		
		if (FILTER == CallLogs.FILTER_MOST_SPEAKING || FILTER == CallLogs.FILTER_MOST_TALKING) {
			//- Extra değerlerini ata
			CallOver.accumulateByDuration(groups);
			CallOver.makeByExtra(groups);
			//MisterLister.makeRanks(groups, 1);
		}
		else {
			
			CallOver.makeBySize(groups);
			//groups.sort((x, y) -> Integer.compare(y.size(), x.size()));
			//MisterLister.makeRanks(groups, 0);
		}
		
		return groups;
	}
	
	private List<MostCallsItemData> makeItemData(@NotNull List<Group<Call>> groups) {
		
		return groups.stream().map(this::createItemData).collect(Collectors.toList());
	}
	
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
	
	@NotNull
	private MostCallsItemData createItemData(@NotNull Group<Call> group) {
		
		TextDrawable rank = TextDrawable.builder()
				.buildRound(String.valueOf(group.getRank()), Colors.COLOR_GENERATOR.getRandomColor());
		
		String txt;
		
		if (FILTER == CallLogs.FILTER_MOST_SPEAKING || FILTER == CallLogs.FILTER_MOST_TALKING) {
			
			txt = Time.formatSeconds(group.getExtra());
		}
		else {
			
			txt = group.size() + " " + textType;
		}
		
		return new MostCallsItemData(group.getValue().getName(), txt, imgType, rank);
	}
	
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
