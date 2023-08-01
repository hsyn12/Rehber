package com.tr.hsyn.telefonrehberi.main.call.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.colors.ColorHolder;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.page.HaveCallAction;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.MostCallsActivity;
import com.tr.hsyn.telefonrehberi.main.call.activity.search.CallLogSearchInfo;
import com.tr.hsyn.telefonrehberi.main.call.cast.base.Filter;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.dialog.DialogFilters;
import com.tr.hsyn.telefonrehberi.main.cast.BackPressObserver;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;
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
	protected     CallLog               callLog;
	protected     int                   filter      = CallLog.FILTER_ALL;
	protected     String[]              filters     = new String[0];
	private       List<Call>            filteredCalls;
	private       ItemIndexListener     onCallAction;
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		filters = getResources().getStringArray(R.array.filters);
		setTitle(getFilterName(CallLog.FILTER_ALL));
	}
	
	@Override
	protected void onClickFilterMenu() {
		
		if (getList().isEmpty()) {
			
			Show.tost(getContext(), getString(R.string.empty_call_list));
			return;
		}
		
		assert getActivity() != null;
		
		DialogFilters filters = new DialogFilters(getActivity(), this::setFilter, filter, this.filters);
		filters.show();
	}
	
	@Override
	protected void listenBackPress() {
		
		// This callback will only be called when MyFragment is at least Started.
		
		requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
	}
	
	@Override
	protected void doNotListenBackPress() {
		
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
		
		xlog.d("Call Filter : %d [%s]", filter, getFilterName(filter));
	}
	
	@Override
	public String getFilterName() {
		
		if (filters != null)
			return filters[getFilter()];
		
		return "";
	}
	
	@Override
	public void onBackPressed() {
		
		if (adapter.isSelectionMode()) {
			
			cancelSelection();
		}
	}
	
	@Override
	public void setOnCallAction(ItemIndexListener onCallAction) {
		
		this.onCallAction = onCallAction;
	}
	
	@Override
	public void setList(@NonNull @NotNull List<Call> list) {
		
		super.setList(list);
		
		setTitle(filters[filter]);
		setFilter(filter);
		updateSubTitle();
		checkEmpty();
		
		refreshLayout.setRefreshing(false);
	}
	
	@Override
	public Call getItem(int index) {
		
		if (filter == CallLog.FILTER_ALL) return getList().get(index);
		
		return filteredCalls.get(index);
	}
	
	public String getFilterName(int filter) {
		
		if (filters != null)
			return filters[filter];
		
		return "?";
	}
	
	private void changeFilter(int filter) {
		
		if (filter <= 6) {
			
			if (filter == CallLog.FILTER_ALL) {
				
				filterAll();
			}
			else {
				
				filterByType(filter);
			}
		}
	}
	
	private void showMostCalls(int filter) {
		
		Blue.box(com.tr.hsyn.key.Key.MOST_CALLS_FILTER_TYPE, filter);
		startActivity(new Intent(getContext(), MostCallsActivity.class));
		Bungee.slideDown(getContext());
	}
	
	private void filterAll() {
		
		Blue.box(com.tr.hsyn.key.Key.CALL_LOG_FILTER, new CallLogSearchInfo(getList(), filter));
		
		if (filteredCalls != null) {
			
			filteredCalls.clear();
			filteredCalls = null;
		}
		
		adapter = new CallAdapter(getList(), this, colorHolder, this::onCallAction, this::onLongClickItem, this::onItemSelectionChange);
		recyclerView.setAdapter(adapter);
		
		String titleCallLog = getString(R.string.call_logs);
		
		setTitle(titleCallLog);
		checkEmpty();
		updateSubTitle();
	}
	
	private void filterByType(int type) {
		
		showProgress();
		
		Runny.run(() -> {
			
			switch (type) {
				
				case CallLog.FILTER_INCOMING:
					filteredCalls = getList().stream().filter(Call::isIncoming).collect(Collectors.toList());
					break;
				case CallLog.FILTER_OUTGOING:
					filteredCalls = getList().stream().filter(Type::isOutgoing).collect(Collectors.toList());
					break;
				case CallLog.FILTER_MISSED:
					filteredCalls = getList().stream().filter(Type::isMissed).collect(Collectors.toList());
					break;
				case CallLog.FILTER_REJECTED:
					filteredCalls = getList().stream().filter(Type::isRejected).collect(Collectors.toList());
					break;
				case CallLog.FILTER_NO_NAMED:
					filteredCalls = getList().stream().filter(c -> c.getName() == null || PhoneNumbers.isPhoneNumber(c.getName())).collect(Collectors.toList());
					break;
				
				case CallLog.FILTER_RANDOM:
					filteredCalls = getList().stream().filter(Call::isRandom).collect(Collectors.toList());
					break;
			}
			
			Runny.run(this::setFilterAdapter);
		}, false);
	}
	
	private void onCallAction(int index) {
		
		if (onCallAction != null) onCallAction.onItemIndex(index);
	}
	
	private void onItemSelectionChange(boolean isSelected) {
		
		if (isSelected) selectedItemsCounter++;
		else selectedItemsCounter--;
		
		
		setSubTitle(Stringx.format("%d / %d", adapter.getSize(), selectedItemsCounter));
	}
	
	private void setFilterAdapter() {
		
		if (filteredCalls != null) {
			
			adapter = new CallAdapter(filteredCalls, this, colorHolder, this::onCallAction, this::onLongClickItem, this::onItemSelectionChange);
			recyclerView.setAdapter(adapter);
			
			String f = filters[filter];
			
			if (f == null) {
				
				xlog.w("filter is null and title fucks up");
			}
			
			setTitle(f);
			updateSubTitle();
			checkEmpty();
			
			Blue.box(com.tr.hsyn.key.Key.CALL_LOG_FILTER, new CallLogSearchInfo(filteredCalls, filter));
		}
		else {
			
			xlog.d("Filtrelenen aramalar bulunamadı");
		}
		
		hideProgress();
	}
}
