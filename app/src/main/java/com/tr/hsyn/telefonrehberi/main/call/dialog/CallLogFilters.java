package com.tr.hsyn.telefonrehberi.main.call.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.Dialog;


/**
 * A dialog created to filter call logs according to various criteria.
 * It will display the filtering criteria as a list,
 * and it will have the user to select from the list.
 */
public class CallLogFilters extends Dialog {
	
	private final AlertDialog       dialog;
	private final ItemIndexListener selectListener;
	
	@SuppressLint("InflateParams")
	public CallLogFilters(@NonNull Activity activity, @NonNull ItemIndexListener selectListener, int selected) {
		
		this.selectListener = selectListener;
		
		AlertDialog.Builder builder = getBuilder(activity, R.layout.call_filter_dialog);
		
		RecyclerView list = findView(R.id.list_call_filters);
		list.setAdapter(new AdapterCallFilter(activity.getResources().getStringArray(R.array.call_filter_items), this::onSelected, selected));
		
		rootView.findViewById(R.id.header).setBackgroundColor(Colors.getPrimaryColor());
		
		dialog = builder.create();
		
		setAnimation(R.style.DialogAnimationBounce);
	}
	
	@Override
	protected AlertDialog getDialog() {
		
		return dialog;
	}
	
	private void onSelected(int index) {
		
		selectListener.onItemIndex(index);
		
		dialog.dismiss();
	}
}
