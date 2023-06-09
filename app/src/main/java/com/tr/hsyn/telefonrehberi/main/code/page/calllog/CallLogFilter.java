package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import android.content.Intent;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.page.HaveCallAction;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.app.Res;
import com.tr.hsyn.telefonrehberi.main.activity.call.MostCallsActivity;
import com.tr.hsyn.telefonrehberi.main.activity.call.search.CallLogSearchInfo;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Filter;
import com.tr.hsyn.telefonrehberi.main.code.cast.BackPressObserver;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;
import com.tr.hsyn.telefonrehberi.main.dev.dialog.CallLogFilters;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;


public abstract class CallLogFilter extends CallList implements Filter, HaveCallAction, BackPressObserver {
	
	public final  ColorHolder           colorHolder = Colors.getColorHolder();
	private final OnBackPressedCallback callback    = new OnBackPressedCallback(true) {
		
		@Override
		public void handleOnBackPressed() {
			
			this.remove();
			onBackPressed();
		}
	};
	protected     int                   filter      = Res.Calls.FILTER_ALL;
	private       List<Call>            filteredCalls;
	private       ItemIndexListener     onCallAction;
	
	@Override
	protected void onClickFilterMenu() {
		
		if (getList().isEmpty()) {
			
			Show.tost(getContext(), getString(R.string.empty_call_list));
			return;
		}
		
		assert getActivity() != null;
		
		CallLogFilters filters = new CallLogFilters(getActivity(), this::onFilterSelected, filter);
		
	}
	
	protected void onFilterSelected(int index) {
		
		setFilter(index);
	}
	
	@Override
	protected void listenBackPress() {
		
		// This callback will only be called when MyFragment is at least Started.
		
		requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
	}
	
	@Override
	protected void dontListenBackPress() {
		
		callback.remove();
	}
	
	@Override
	public int getFilter() {
		
		return filter;
	}
	
	@Override
	public void setFilter(int filter) {
		
		changeFilter(filter);
		
		if (filter != this.filter) {
			
			if (filter <= 6) this.filter = filter;
			else showMostCalls(filter);
		}
		
		//xlog.d("Call Filter : %d [%s]", filter, getFilterName(filter));
	}
	
	private void changeFilter(int filter) {
		
		if (filter <= 6) {
			
			if (filter == Res.Calls.FILTER_ALL) {
				
				filterAll();
			}
			else {
				
				filterByType(filter);
			}
		}
	}
	
	private void showMostCalls(int filter) {
		
		Blue.box(Key.MOST_CALLS_FILTER_TYPE, filter);
		startActivity(new Intent(getContext(), MostCallsActivity.class));
		Bungee.slideDown(getContext());
	}
	
	private void filterByType(int type) {
		
		showProgress();
		
		Runny.run(() -> {
			
			switch (type) {
				
				case Res.Calls.FILTER_INCOMING:
					filteredCalls = getList().stream().filter(Call::isIncoming).collect(Collectors.toList());
					break;
				case Res.Calls.FILTER_OUTGOING:
					filteredCalls = getList().stream().filter(CallType::isOutgoing).collect(Collectors.toList());
					break;
				case Res.Calls.FILTER_MISSED:
					filteredCalls = getList().stream().filter(CallType::isMissed).collect(Collectors.toList());
					break;
				case Res.Calls.FILTER_REJECTED:
					filteredCalls = getList().stream().filter(CallType::isRejected).collect(Collectors.toList());
					break;
				case Res.Calls.FILTER_NO_NAMED:
					filteredCalls = getList().stream().filter(c -> c.getName() == null || PhoneNumbers.isPhoneNumber(c.getName(), true)).collect(Collectors.toList());
					break;
				
				case Res.Calls.FILTER_RANDOM:
					filteredCalls = getList().stream().filter(call -> call.getBool(CallKey.RANDOM)).collect(Collectors.toList());
					break;
			}
			
			Runny.run(this::setFilterAdapter);
		}, false);
	}
	
	private void setFilterAdapter() {
		
		if (filteredCalls != null) {
			
			adapter = new CallAdapter(filteredCalls, this, colorHolder, this::onCallAction, this::onLongClickItem, this::onItemSelectionChange);
			recyclerView.setAdapter(adapter);
			
			var f = filters[filter];
			
			if (f == null) {
				
				xlog.w("filter is null and title fucks up");
			}
			
			setTitle(f);
			updateSubTitle();
			checkEmpty();
			
			Blue.box(Key.CALL_LOG_FILTER, new CallLogSearchInfo(filteredCalls, filter));
		}
		else {
			
			xlog.d("Filtrelenen aramalar bulunamadı");
		}
		
		hideProgress();
	}
	
	private void onItemSelectionChange(boolean isSelected) {
		
		if (isSelected) selectedItemsCounter++;
		else selectedItemsCounter--;
		
		
		setSubTitle(Stringx.format("%d / %d", adapter.getSize(), selectedItemsCounter));
	}
	
	@Override
	public void onBackPressed() {
		
		if (adapter.isSelectionMode()) {
			
			cancelSelection();
		}
	}
	
	private void onCallAction(int index) {
		
		if (onCallAction != null) onCallAction.onItemIndex(index);
	}
	
	@Override
	public void setOnCallAction(ItemIndexListener onCallAction) {
		
		this.onCallAction = onCallAction;
	}
	
	private void filterAll() {
		
		Blue.box(Key.CALL_LOG_FILTER, new CallLogSearchInfo(getList(), filter));
		
		if (filteredCalls != null) {
			
			filteredCalls.clear();
			filteredCalls = null;
		}
		
		adapter = new CallAdapter(getList(), this, colorHolder, this::onCallAction, this::onLongClickItem, this::onItemSelectionChange);
		recyclerView.setAdapter(adapter);
		
		var titleCallLog = getString(R.string.call_log);
		
		setTitle(titleCallLog);
		checkEmpty();
		updateSubTitle();
	}
	
	@Override
	public void setList(@NonNull @NotNull List<Call> list) {
		
		super.setList(list);
		
		setFilter(filter);
		setTitle(filters[filter]);
		updateSubTitle();
		checkEmpty();
		
		refreshLayout.setRefreshing(false);
	}
	
	@Override
	public Call getItem(int index) {
		
		if (filter == Res.Calls.FILTER_ALL) return getList().get(index);
		
		return filteredCalls.get(index);
	}
}
