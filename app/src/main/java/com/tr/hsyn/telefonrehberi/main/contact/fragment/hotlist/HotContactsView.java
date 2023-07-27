package com.tr.hsyn.telefonrehberi.main.contact.fragment.hotlist;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;


public abstract class HotContactsView extends Fragment {
	
	private CharSequence title;
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		title = getString(R.string.rehber);
		
	}
	
	protected CharSequence getTitle() {
		
		return title;
	}
	
	protected int getLayoutId() {
		
		return R.layout.fragment_hotlist_contacts;
	}
}
