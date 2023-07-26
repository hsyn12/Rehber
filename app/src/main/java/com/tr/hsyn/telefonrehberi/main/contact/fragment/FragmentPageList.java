package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.page.IHaveProgress;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.ResourceUtil;
import com.tr.hsyn.telefonrehberi.dev.android.ui.swipe.ContactSwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.ContactAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class FragmentPageList extends FragmentPageColor implements IHaveProgress {
	
	protected ContactAdapter       adapter;
	private   ContactSwipeCallBack swipeCallBack;
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		swipeCallBack = new ContactSwipeCallBack(ItemTouchHelper.RIGHT, this, ResourceUtil.getBitmap(view.getContext(), R.drawable.telephone_call));
		swipeCallBack.setBgColor(colorHolder.getPrimaryColor());
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
	
	@Override
	public void changeColor(int color) {
		
		super.changeColor(color);
		
		swipeCallBack.setBgColor(color);
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
	
	protected void checkEmpty() {
		
		emptyView.setVisibility(adapter.getItems().isEmpty() ? View.VISIBLE : View.GONE);
	}
}
