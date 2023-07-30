package com.tr.hsyn.telefonrehberi.main.call.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.page.SwipeListener;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.ResourceUtil;
import com.tr.hsyn.telefonrehberi.dev.android.ui.swipe.SwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public abstract class CallList extends CallLogMenu {
	
	protected CallAdapter   adapter;
	protected SwipeCallBack swipeCallBack;
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		swipeCallBack = new SwipeCallBack(ItemTouchHelper.LEFT, this, ResourceUtil.getBitmap(view.getContext(), R.drawable.delete_white));
		swipeCallBack.setBgColor(Colors.getPrimaryColor());
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
	
	@Override
	public void setSwipeListener(SwipeListener swipeListener) {
		
		this.swipeListener = swipeListener;
	}
	
	@Override
	public void onSwipe(int index) {
		
		adapter.notifyItemChanged(index);
		
		super.onSwipe(index);
		
	}
	
	@Override
	protected boolean isSelectionMode() {
		
		return getAdapter().isSelectionMode();
	}
	
	@Override
	protected void selectAllItems(boolean select) {
		
		loopAdapterHolders(holder -> {
			
			holder.selection.setChecked(select);
			holder.itemView.invalidate();
		});
		
		getAdapter().selectAllItem(select);
		
		if (select) selectedItemsCounter = getAdapter().getSize();
		else selectedItemsCounter = 0;
		
		setSubTitle(Stringx.format("%d / %d", getAdapter().getSize(), selectedItemsCounter));
	}
	
	@Override
	protected void loopAdapterHolders(Consumer<CallAdapter.Holder> consumer) {
		
		int count = recyclerView.getChildCount();
		
		for (int i = 0; i < count; i++) {
			
			CallAdapter.Holder holder = (CallAdapter.Holder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
			
			consumer.accept(holder);
		}
	}
	
	@Override
	public void showTime(boolean showTime) {
		
		super.showTime(showTime);
	}
	
	@Override
	protected void cancelSelection() {
		
		super.cancelSelection();
		getAdapter().cancelSelection();
	}
	
	/**
	 * Filtreleme seçeneği, ana listeden farklı listeler oluşturduğu için,
	 * o an geçerli olan liste adapter üzerinde olan listedir.
	 *
	 * @return Adapter
	 */
	protected CallAdapter getAdapter() {
		
		return adapter;
	}
	
	protected void checkEmpty() {
		
		if (adapter != null) {
			
			emptyView.setVisibility(adapter.getSize() > 0 ? View.GONE : View.VISIBLE);
		}
	}
}
