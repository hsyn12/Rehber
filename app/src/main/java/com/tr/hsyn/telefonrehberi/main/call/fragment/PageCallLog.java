package com.tr.hsyn.telefonrehberi.main.call.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.cast.PageOwner;

import org.jetbrains.annotations.NotNull;


/**
 * Call log page.
 */
public class PageCallLog extends CallLogEvents {
	
	private boolean  ready;
	private Runnable onDeleteAll;
	
	@Override
	protected void deleteAll() {
		
		if (onDeleteAll != null) onDeleteAll.run();
	}
	
	@Override
	public void setOnDeleteAll(Runnable onDeleteAll) {
		
		this.onDeleteAll = onDeleteAll;
	}
	
	@Override
	public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		ready = true;
		if (onReady != null) onReady.run();
	}
	
	@Override
	public Call deleteItem(Call item) {
		
		return null;
	}
	
	@Override
	public boolean isReady() {
		
		return ready;
	}
	
	@Override
	protected int getAdapterSize() {
		
		if (getAdapter() != null)
			return getAdapter().getSize();
		
		return 0;
	}
	
	@Override
	public void setPageOwner(PageOwner pageOwner) {
		
	}
}
