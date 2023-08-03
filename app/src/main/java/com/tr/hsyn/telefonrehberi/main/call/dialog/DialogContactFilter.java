package com.tr.hsyn.telefonrehberi.main.call.dialog;


import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.Dialog;

import org.jetbrains.annotations.NotNull;


public class DialogContactFilter extends Dialog {
	
	private final AlertDialog       dialog;
	private final ItemIndexListener selectListener;
	
	public DialogContactFilter(@NotNull Activity activity, @NotNull ItemIndexListener selectListener, int selected, String[] filters) {
		
		this.selectListener = selectListener;
		
		AlertDialog.Builder builder = getBuilder(activity, R.layout.filter_dialog);
		
		RecyclerView list = findView(R.id.list_call_filters);
		list.setAdapter(new ContactFilterAdapter(filters, this::onSelected, selected));
		setHeight(list);
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
