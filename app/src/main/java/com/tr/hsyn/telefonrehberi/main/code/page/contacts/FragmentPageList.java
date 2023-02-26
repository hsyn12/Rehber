package com.tr.hsyn.telefonrehberi.main.code.page.contacts;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.page.IHaveProgress;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.ResourceUtil;
import com.tr.hsyn.telefonrehberi.code.android.ui.swipe.ContactSwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.ContactAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class FragmentPageList extends FragmentPageColor implements IHaveProgress {
	
	protected ContactAdapter       adapter;
	private   ContactSwipeCallBack swipeCallBack;
	
	protected void checkEmpty() {
		
		emptyView.setVisibility(adapter.getItems().isEmpty() ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		swipeCallBack = new ContactSwipeCallBack(ItemTouchHelper.RIGHT, this, ResourceUtil.getBitmap(view.getContext(), R.drawable.telephone_call));
		swipeCallBack.setBgColor(colorHolder.getPrimaryColor());
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
	
	@Override
	public void setList(@NonNull @NotNull List<Contact> list) {
		
		super.setList(list);
		
		adapter = new ContactAdapter(list, this, colorHolder);
		recyclerView.setAdapter(adapter);
		
		//header.setTitle(title);
		header.setSubTitle(String.valueOf(list.size()));
		
		checkEmpty();
	}
	
	@Override
	public void changeColor(int color) {
		
		super.changeColor(color);
		
		swipeCallBack.setBgColor(color);
	}
	
	@Override
	public void showProgress() {
		
		progressBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
		
		refreshLayout.setRefreshing(false);
	}
	
	@Override
	public Contact getItem(int index) {
		
		return getList().get(index);
	}
}
