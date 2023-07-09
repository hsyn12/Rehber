package com.tr.hsyn.telefonrehberi.main.call.activity.backup;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.executors.MainExecutor;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;
import com.tr.hsyn.xlog.xlog;

import java.util.List;
import java.util.concurrent.Callable;


public abstract class CallBackupActivityDirector extends CallBackupActivitySelection {
	
	private final Gate gateAdd    = Gate.newGate();
	private final Gate gateDelete = Gate.newGate();
	
	protected abstract List<Call> getCallLogCalls();
	
	@Override
	protected void deleteBackup(int index) {
		
		//- Hem seçilerek hem de ikona dokunma olayı gerçekleşebileceği için bunu burada da kaydetmemiz gerek
		selectedBackup = index;
		
		if (Lister.isValidIndex(adapter.getCallBackups(), index)) {
			
			deleteBackup(getSelectedBackup().getName());
		}
	}
	
	@Override
	protected void addNewBackup() {
		
		if (callBackupDirectory == null) {
			
			requestDirectoryAccess();
			
			waitingNewBackup = true;
			return;
		}
		
		List<Call> calls = getCallLogCalls();
		
		if (calls == null || calls.isEmpty()) {
			
			Show.snake(this, "Hiç arama kaydı yok");
			return;
		}
		
		if (gateAdd.enter()) {
			
			showProgress(true);
			
			Work.on((Callable<Backup<Call>>) callBackupDirectory::newBackup)
					.onLast(() -> {
						
						showProgress(false);
						gateAdd.exit();
					}, 200)
					.onSuccess(backup -> {
						
						if (backup != null) {
							
							adapter.addBackup(backup);
							checkEmpty();
						}
					})
					.onError(e -> {
						
						if (e != null) {
							
							xlog.w("Yeni bir yedek oluştururken bir hata meydana geldi");
							e.printStackTrace();
						}
					})
					.execute();
		}
		else {
			
			xlog.i("Yedek oluşturma işlemi devam ederken yeni bir yedek oluşturma isteği kabul edilmiyor");
		}
	}
	
	protected final Backup<Call> getSelectedBackup() {
		
		return adapter.getBackup(selectedBackup);
	}
	
	protected final Backup<Call> getBackup(int index) {
		
		return adapter.getBackup(index);
	}
	
	protected void deleteBackup(String name) {
		
		if (gateDelete.enter()) {
			
			showProgress(true);
			
			Runny.complete(() -> callBackupDirectory.deleteBackup(name))
					.whenCompleteAsync((t, e) -> {
						
						if (e != null) {
							xlog.e("Yedek silinirken bir hata meydana geldi : %s", e);
							return;
						}
						
						adapter.removeBackup(selectedBackup);
						checkEmpty();
						showProgress(false);
						gateDelete.exit();
					}, MainExecutor.HOLDER.getMainExecutor());
			
			
		}
		else xlog.i("Yedek silme işlemi devam ediyor. Bu yüzden yeni bir silme işlemi isteği reddedildi");
		
	}
}
