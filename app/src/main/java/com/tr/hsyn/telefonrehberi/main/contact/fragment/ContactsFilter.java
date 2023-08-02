package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.dialog.DialogFilters;
import com.tr.hsyn.telefonrehberi.main.code.data.History;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class ContactsFilter extends FragmentPageMenu {
	
	private int          filter;
	private String[]     filters;
	private CharSequence title;
	
	@Override
	public int getFilter() {
		
		return filter;
	}
	
	@Override
	public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		filters = requireActivity().getResources().getStringArray(R.array.contact_filters);
		title   = getString(R.string.rehber);
	}
	
	@Override
	protected void onClickFilter() {
		
		DialogFilters dialog = DialogFilters.newInstance(requireActivity(), this::onFilterSelected, filter == 0 ? 0 : filter - 6, filters);
		dialog.show();
	}
	
	@Override
	protected CharSequence getTitle() {
		
		return title;
	}
	
	/**
	 * Listener for filter selection.
	 *
	 * @param index The index of the selected filter
	 */
	private void onFilterSelected(int index) {
		
		CallLog log = CallLog.getCallLog();
		
		if (log == null) {
			
			xlog.i("No call log");
			return;
		}
		
		if (index == filter) return;
		
		filter = index + 6;//+ scale factor
		title  = filters[index];
		List<Contact> contacts = MainContacts.getContacts();
		
		if (index == CallLog.FILTER_ALL) {
			
			setList(contacts);
		}
		else {
			
			List<History> historyList = log.getHistory();
			sortHistoryList(historyList);
			setHistoryList(historyList);
		}
	}
	
	private void sortHistoryList(List<History> historyList) {
		
		switch (filter) {
			
			case CallLog.FILTER_MOST_INCOMING:
				historyList.sort(History.Comparing.INCOMING);
				break;
			case CallLog.FILTER_MOST_OUTGOING:
				historyList.sort(History.Comparing.OUTGOING);
				break;
			case CallLog.FILTER_MOST_MISSED:
				historyList.sort(History.Comparing.MISSED);
				break;
			case CallLog.FILTER_MOST_REJECTED:
				historyList.sort(History.Comparing.REJECTED);
				break;
			case CallLog.FILTER_MOST_SPEAKING:
				historyList.sort(History.Comparing.INCOMING_DURATION);
				break;
			case CallLog.FILTER_MOST_TALKING:
				historyList.sort(History.Comparing.OUTGOING_DURATION);
				break;
			case CallLog.FILTER_MOST_TOTAL_DURATION:
				historyList.sort(History.Comparing.TOTAL_DURATION);
				break;
			default: throw new IllegalArgumentException("Unknown filter : " + filter);
		}
	}
	
}
