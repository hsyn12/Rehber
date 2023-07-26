package com.tr.hsyn.telefonrehberi.main.contact.fragment.hotlist;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.page.FragmentEvents;

import org.jetbrains.annotations.NotNull;


public abstract class HotContactsView extends FragmentEvents<Contact> {
	
	private CharSequence title;
	
	@Override
	protected CharSequence getTitle() {
		
		return title;
	}
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.fragment_hotlist_contacts;
	}
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		title = getString(R.string.rehber);
		recyclerView.setFastScrollListener(this);
	}
}
