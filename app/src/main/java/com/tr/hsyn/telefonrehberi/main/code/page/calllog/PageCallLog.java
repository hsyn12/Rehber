package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.telefonrehberi.app.Res;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;


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
	public List<Call> deleteAllItems() {
		
		
		var deletedCalls = Lister.listOf(getAdapter().getItems());
		getAdapter().clearItems();
		
		//- Filtreleme durumuna göre silinen elemanları ana listeden de çıkaralım
		if (Res.Calls.FILTER_ALL != filter) {
			
			var count = Lister.removeItems(getList(), deletedCalls);
			
			if (count == deletedCalls.size()) {
				
				xlog.d("Silinen %d arama da listeden çıkarıldı", count);
			}
			else {
				
				xlog.d("%d arama silindi ancak %d tanesi listeden çıkarılabildi", deletedCalls.size(), count);
			}
		}
		
		
		updateSubTitle();
		checkEmpty();
		return deletedCalls;
	}
	
	@Override
	public boolean isReady() {
		
		return ready;
	}
	
	
}
