package com.tr.hsyn.telefonrehberi.main.activity.call.showcalls;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;


public abstract class ShowCallsActivitySet extends ShowCallsActivityView {
	
	protected final Key              SHOW_CALLS = Key.SHOW_CALLS;
	protected final Gate             gateDelete = AutoGate.newGate(600L);
	protected       List<Call>       calls;
	protected       ShowCallsAdapter adapter;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		calls = Blue.getObject(SHOW_CALLS);
		
		if (calls == null) {
			
			xlog.e("Gösterilecek Arama kayıtları bulunamadı");
		}
		
		list.setAdapter(adapter = new ShowCallsAdapter(calls != null ? calls : new ArrayList<>(0), this::onSelect, this::onDelete));
		
		checkEmpty();
		setSubTitle();
	}
	
	protected void onDelete(int index) {
		
		if (gateDelete.enter()) {
			
			calls.remove(index);
			adapter.notifyItemRemoved(index);
			checkEmpty();
			
			setSubTitle();
		}
		
	}
	
	protected void setSubTitle() {
		
		getToolbar().setSubtitle(String.valueOf(adapter.getItemCount()));
	}
	
	protected void onDeleteAll() {
		
		adapter.notifyItemRangeRemoved(0, calls.size());
		calls.clear();
		checkEmpty();
		setSubTitle();
	}
	
	protected void onSelect(int index) {}
	
}
