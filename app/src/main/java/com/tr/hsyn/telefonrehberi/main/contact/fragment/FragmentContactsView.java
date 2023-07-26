package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.page.FragmentEvents;


public abstract class FragmentContactsView extends FragmentEvents<Contact> {
	
	private CharSequence title;
	
	@Override
	protected CharSequence getTitle() {
		
		return title;
	}
	
	@Override
	protected final int getLayoutId() {
		
		return R.layout.fragment_contacts;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		title = getString(R.string.rehber);
		recyclerView.setFastScrollListener(this);
	}
	
}
