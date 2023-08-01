package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.dialog.DialogFilters;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class ContactsFilter extends FragmentPageMenu {
	
	private int           filter;
	private String[]      filters;
	private List<Contact> filteredContacts;
	
	@Override
	public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		filters = requireActivity().getResources().getStringArray(R.array.contact_filters);
	}
	
	@Override
	protected void onClickFilter() {
		
		var dialog = DialogFilters.newInstance(requireActivity(), this::onSelected, filter, filters);
		dialog.show();
	}
	
	/**
	 * Listener for filter selection.
	 *
	 * @param index The index of the selected filter
	 */
	private void onSelected(int index) {
		
		if (index == filter) return;
		
		filter = index + 7;
		
		var log = getCallLog();
		
		if (log == null) {
			
			xlog.i("No call log");
			return;
		}
		
		var rankMap = log.getMostCalls(filter);
		
	}
	
	protected @Nullable CallLog getCallLog() {
		
		return Blue.getObject(Key.CALL_LOG);
	}
	
}
