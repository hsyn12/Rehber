package com.tr.hsyn.telefonrehberi.main.contact.fragment;

import android.os.Bundle;
import android.view.View;

import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.dialog.DialogContactFilter;
import com.tr.hsyn.telefonrehberi.main.cast.PermissionHolder;
import com.tr.hsyn.telefonrehberi.main.data.ContactLog;
import com.tr.hsyn.use.Use;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Provides to filter contacts.
 */
public abstract class ContactsFilter extends FragmentPageMenu implements PermissionHolder {

	private int          filter;
	private String[]     filters;
	private CharSequence title;

	@Override
	public int getFilter() {

		return filter;
	}

	@Override
	public void setFilter(int filter) {

		this.filter = -1;
		filter(filter);
	}

	@Override
	public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		filters = requireActivity().getResources().getStringArray(R.array.contact_filters);
		title   = getString(R.string.rehber);
	}

	@Override
	protected void onClickFilterMenu() {

		DialogContactFilter dialog = new DialogContactFilter(requireActivity(), this::filter, filter, filters);
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
	public void filter(int index) {

		Use.ifNotNull(CallLog.getCallLog(), log -> {

			if (index == filter) return;

			ContactLog contactLog = ContactLog.getLog();

			if (contactLog != null) {

				xlog.d(contactLog.getContacts().size() + " contacts");
			}
			else {

				xlog.d("Contacts not found");
				return;
			}


			filter = index;
			title  = filters[ filter ];

			if (filter == ContactLog.FILTER_ALL) {

				setList(ContactLog.getLogOrEmpty().getContacts());
			}
			else {
				List<History> historyList = log.getHistory();
				sortHistoryList(historyList);
				setHistoryList(historyList);
			}
		}).isNotUsed(() -> xlog.i("CallLog not found"));
	}

	/**
	 * Sorts the history list according to the filter.
	 *
	 * @param historyList The history list
	 */
	private void sortHistoryList(List<History> historyList) {

		switch (filter) {

			case ContactLog.FILTER_MOST_INCOMING:
				historyList.sort(History.Comparing.INCOMING);
				break;
			case ContactLog.FILTER_MOST_OUTGOING:
				historyList.sort(History.Comparing.OUTGOING);
				break;
			case ContactLog.FILTER_MOST_MISSED:
				historyList.sort(History.Comparing.MISSED);
				break;
			case ContactLog.FILTER_MOST_REJECTED:
				historyList.sort(History.Comparing.REJECTED);
				break;
			case ContactLog.FILTER_MOST_INCOMING_DURATION:
				historyList.sort(History.Comparing.INCOMING_DURATION);
				break;
			case ContactLog.FILTER_MOST_OUTGOING_DURATION:
				historyList.sort(History.Comparing.OUTGOING_DURATION);
				break;
			case ContactLog.FILTER_MOST_TOTAL_DURATION:
				historyList.sort(History.Comparing.TOTAL_DURATION);
				break;
			default: throw new IllegalArgumentException("Unknown filter : " + filter);
		}
	}

}
