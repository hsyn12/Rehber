package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.dialog.DialogFilters;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public abstract class ContactsFilter extends FragmentPageMenu {
	
	private int          filter;
	private String[]     filters;
	private CharSequence title;
	
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
		
		CallLog log = getCallLog();
		
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
			
			List<CallRank> rankList = new ArrayList<>();
			
			for (var contact : contacts) {
				
				var calls = log.getCalls(contact);
				var rank  = new CallRank(String.valueOf(contact.getContactId()), calls);
				rankList.add(rank);
				rank.setContact(contact);
				int duration = calls.stream().filter(Call::isIncoming).mapToInt(Call::getDuration).sum();
				rank.setIncomingDuration(duration);
				int outDuration = calls.stream().filter(Call::isOutgoing).mapToInt(Call::getDuration).sum();
				rank.setOutgoingDuration(outDuration);
			}
			
			switch (filter) {
				
				case CallLog.FILTER_MOST_INCOMING:
					rankList.sort(CallRank::compareToIncoming);
					break;
				case CallLog.FILTER_MOST_OUTGOING:
					rankList.sort(CallRank::compareToOutgoing);
					break;
				case CallLog.FILTER_MOST_MISSED:
					rankList.sort(CallRank::compareToMissed);
					break;
				case CallLog.FILTER_MOST_REJECTED:
					rankList.sort(CallRank::compareToRejected);
					break;
				case CallLog.FILTER_MOST_SPEAKING:
					rankList.sort(CallRank::compareToIncomingDuration);
					break;
				case CallLog.FILTER_MOST_TALKING:
					rankList.sort(CallRank::compareToOutgoingDuration);
					break;
				case CallLog.FILTER_MOST_TOTAL_DURATION:
					rankList.sort(CallRank::compareToDuration);
					break;
				default: throw new IllegalArgumentException("Unknown filter : " + filter);
			}
			
			setRankList(rankList);
			adapter.setFiltered(true);
			adapter.setFilter(filter);
		}
	}
	
	protected @Nullable CallLog getCallLog() {
		
		return Blue.getObject(Key.CALL_LOG);
	}
	
}
