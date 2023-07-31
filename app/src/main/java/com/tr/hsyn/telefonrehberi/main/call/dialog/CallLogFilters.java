package com.tr.hsyn.telefonrehberi.main.call.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;


/**
 * A dialog created to filter call logs according to various criteria.
 * It will display the filtering criteria as a list,
 * and it will have the user to select from the list.
 */
public class CallLogFilters {
	
	private final AlertDialog       dialog;
	private final ItemIndexListener selectListener;
	
	@SuppressLint("InflateParams")
	public CallLogFilters(@NonNull Activity activity, @NonNull ItemIndexListener selectListener, int selected) {
		
		this.selectListener = selectListener;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		View                view    = activity.getLayoutInflater().inflate(com.tr.hsyn.callfilter.R.layout.call_filter_dialog, null, false);
		builder.setCancelable(true);
		builder.setView(view);
		
		RecyclerView list = view.findViewById(com.tr.hsyn.callfilter.R.id.list_call_filters);
		list.setAdapter(new AdapterCallFilter(activity.getResources().getStringArray(com.tr.hsyn.callfilter.R.array.call_filter_items), this::onSelected, selected));
		
		view.findViewById(com.tr.hsyn.callfilter.R.id.header).setBackgroundColor(Colors.getPrimaryColor());
		
		dialog = builder.create();
		
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
		dialog.show();
		
	}
	
	private void onSelected(int index) {
		
		selectListener.onItemIndex(index);
		
		dialog.dismiss();
	}
	
}
