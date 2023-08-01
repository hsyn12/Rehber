package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Contacts page.
 */
public class FragmentContacts extends ContactsFilter {
	
	protected boolean ready;
	
	@Override
	public boolean isReady() {
		
		return ready;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		showTime(true);
		ready = true;
		
		if (onReady != null) {
			
			onReady.run();
			onReady = null;
		}
	}
	
	@Override
	protected int getAdapterSize() {
		
		if (adapter != null)
			return adapter.getSize();
		
		return 0;
	}
	
	@Override
	public void onSwipe(int index) {
		
		adapter.notifyItemChanged(index);
		super.onSwipe(index);
	}
}
