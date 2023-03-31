package com.tr.hsyn.telefonrehberi.main.activity.call;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xtoolbar.Toolbarx;
import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.betty.Betty;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.app.Res;
import com.tr.hsyn.telefonrehberi.code.call.CallOver;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MostCallsActivity extends ActivityView {
	
	private final int                     FILTER = Objects.requireNonNull(Blue.getObject(Key.MOST_CALLS_FILTER_TYPE));
	private final List<Call>              calls  = Blue.getObject(Key.CALL_LOG);
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
		
		setTitle();
		
		Betty.bet(() -> {
					
					filter();
					
					var ranks = makeRank();
					
					if (ranks != null) return makeItemData(ranks);
					
					return new ArrayList<MostCallsItemData>(0);
					
				})
				.onSuccess(this::showList)
				.onError(xlog::e);
		
	}
	
	private void showList(@NotNull List<MostCallsItemData> data) {
		
		this.data = data;
		
		if (!data.isEmpty()) {
			
			emptyView.setVisibility(View.GONE);
			
			list.setAdapter(new MostCallsAdapter(data, this::onClickItem));
		}
		
		hideProgress();
	}
	
	private void onClickItem(int index) {
		
		var item = data.get(index);
		
		xlog.d(item);
		
		
	}
	
	private void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
	}
	
	private void showProgress() {
		
		progressBar.setVisibility(View.VISIBLE);
	}
	
	private void filter() {
		
		switch (FILTER) {
			
			case Res.Calls.FILTER_MOST_INCOMING:
				
				imgType = AppCompatResources.getDrawable(this, R.drawable.incomming_call);
				filteredCalls = calls.stream().filter(Call::isIncoming).collect(Collectors.toList());
				textType = getString(R.string.incomming_call);
				break;
			
			case Res.Calls.FILTER_MOST_OUTGOING:
				
				imgType = AppCompatResources.getDrawable(this, R.drawable.outgoing_call);
				filteredCalls = calls.stream().filter(Call::isOutgoing).collect(Collectors.toList());
				textType = getString(R.string.outgoing_call);
				break;
			
			case Res.Calls.FILTER_MOST_MISSED:
				
				imgType = AppCompatResources.getDrawable(this, R.drawable.missed_call);
				filteredCalls = calls.stream().filter(Call::isOutgoing).collect(Collectors.toList());
				textType = getString(R.string.missed_call);
				break;
			
			case Res.Calls.FILTER_MOST_REJECTED:
				
				imgType = AppCompatResources.getDrawable(this, R.drawable.rejected_call);
				filteredCalls = calls.stream().filter(Call::isOutgoing).collect(Collectors.toList());
				textType = getString(R.string.rejected_call);
				break;
			
			
			case Res.Calls.FILTER_MOST_SPEAKING:
				
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = calls.stream().filter(Call::isIncoming).filter(Call::isSpoken).collect(Collectors.toList());
				break;
			
			case Res.Calls.FILTER_MOST_TALKING:
				
				imgType = AppCompatResources.getDrawable(this, com.tr.hsyn.resarrowdrawable.R.drawable.clock);
				filteredCalls = calls.stream().filter(Call::isOutgoing).filter(Call::isSpoken).collect(Collectors.toList());
				break;
			
			default:
				xlog.w("There is no proper type for filter : %d [%s]", FILTER, Res.Calls.getCallFilterName(this, FILTER));
		}
	}
	
	@Nullable
	private List<Group<Call>> makeRank() {
		
		if (filteredCalls == null || filteredCalls.isEmpty()) {
			
			xlog.d("There is no filtered calls");
			return null;
		}
		
		xlog.d("Filtered type : %d", FILTER);
		
		List<Group<Call>> groups = CallOver.groupByNumber(filteredCalls);
		
		if (FILTER == Res.Calls.FILTER_MOST_SPEAKING || FILTER == Res.Calls.FILTER_MOST_TALKING) {
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
	
	@NotNull
	private MostCallsItemData createItemData(@NotNull Group<Call> group) {
		
		var rank = TextDrawable.builder()
				.buildRound(String.valueOf(group.getRank()), Colors.COLOR_GENERATOR.getRandomColor());
		
		String txt;
		
		if (FILTER == Res.Calls.FILTER_MOST_SPEAKING || FILTER == Res.Calls.FILTER_MOST_TALKING) {
			
			txt = Time.formatSeconds(group.getExtra());
		}
		else {
			
			txt = group.size() + " " + textType;
		}
		
		return new MostCallsItemData(group.getValue().getName(), txt, imgType, rank);
	}
	
	private void setTitle() {
		
		Toolbar toolbar = findView(R.id.most_calls_toolbar);
		
		Toolbarx.setToolbar(this, toolbar, this::onBackPressed);
		
		assert getSupportActionBar() != null;
		getSupportActionBar().setTitle(Res.Calls.getCallFilterName(this, FILTER));
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideUp(this);
	}
	
}
