package com.tr.hsyn.telefonrehberi.main.activity.call.backup;


import android.content.Intent;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.activity.call.showcalls.ShowCallsActivity;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public abstract class CallBackupActivityOperator extends CallBackupActivityDirector {
	
	protected final Key        SHOW_CALLS = Key.SHOW_CALLS;
	protected       boolean    isShowCalls;
	protected       List<Call> showCalls;
	protected       int        sizeShowCalls;
	
	protected abstract void restore(List<Call> calls);
	
	@Override
	protected void show() {
		
		takeCalls(this::show);
	}
	
	@CallSuper
	protected void show(@Nullable List<Call> calls) {
		
		if (calls == null) calls = new ArrayList<>(0);
		
		showCalls     = calls;
		sizeShowCalls = calls.size();
		
		Intent intent = new Intent(this, ShowCallsActivity.class);
		
		Blue.box(Key.SHOW_CALLS, showCalls);
		
		isShowCalls = true;
		startActivity(intent);
		Bungee.slideDown(this);
	}
	
	protected void takeCalls(@NonNull Consumer<List<Call>> consumer) {
		
		showProgress(true);
		
		Work.on(() -> callBackupDirectory.getItems(getBackup(selectedBackup).getName()))
				.onLast(() -> showProgress(false))
				.onError(this::onErrorWhenGettingCalls)
				.onSuccess(consumer)
				.execute();
	}
	
	protected void fixCalls() {
		
		if (sizeShowCalls != showCalls.size()) {
			
			showProgress(true);
			
			Work.on(() -> callBackupDirectory.override(getSelectedBackup().getName(), showCalls))
					.onLast(() -> {
						
						loadBackups();
						showCalls = null;
						Blue.remove(SHOW_CALLS);
					})
					.execute();
			
		}
		else {
			//yedek değişmedi
			xlog.i("The Backup has not changed");
			Blue.remove(SHOW_CALLS);
			showCalls = null;
		}
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		if (isShowCalls) {
			
			isShowCalls = false;
			fixCalls();
		}
	}
	
	@Override
	protected void restore() {
		
		takeCalls(this::restore);
	}
	
	private void onErrorWhenGettingCalls(Throwable throwable) {
		
		xlog.e("Yedek arama kayıtları alınırken bir hata meydana geldi");
		
		if (throwable != null)
			throwable.printStackTrace();
	}
}
