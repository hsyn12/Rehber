package com.tr.hsyn.telefonrehberi.main.activity.call.backup;


import android.content.ContentResolver;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.executors.MainExecutor;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.call.backup.dialog.DialogRestoreBackup;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


public abstract class CallBackupActivityRestore extends CallBackupActivityOperator {
	
	
	@Override
	protected void restore(List<Call> calls) {
		
		if (calls == null || calls.isEmpty()) {
			
			xlog.i("No calls to restore");
			return;
		}
		
		DialogRestoreBackup dialog = new DialogRestoreBackup(this);
		
		dialog.show();
		
		restore(calls, dialog);
	}
	
	private void restore(List<Call> calls, DialogRestoreBackup dialog) {
		
		ContentResolver resolver = getContentResolver();
		int             size     = calls.size();
		dialog.setProgress(0);
		dialog.setMaxProgress(size);
		
		List<Call> systemCalls = getCallLogCalls();
		
		if (systemCalls != null) {
			
			Work.on(() -> {
						
						for (int i = 0; i < size; i++) {
							
							Call call = calls.get(i);
							
							if (!systemCalls.contains(call)) {
								
								if (Calls.add(resolver, call) != null) {
									
									xlog.d("Restored : %s", call);
									dialog.setProgressText(call.getNumber());
								}
								else {
									
									xlog.w("Can't retored : %s", call);
								}
							}
							else {
								
								xlog.d("This call is already exist : %s", call);
							}
							
							dialog.incProgress();
							dialog.setProgressPercent(((i + 1) * 100) / size);
						}
						
						Blue.box(Key.REFRESH_CALL_LOG, Boolean.TRUE);
						
						return null;
					})
					.onLast(() -> {
						
						dialog.setProgressText(getString(R.string.txt_completed), true);
						
						MainExecutor.run(dialog::dismiss);
					}, 1000L)
					.execute();
		}
	}
	
}
