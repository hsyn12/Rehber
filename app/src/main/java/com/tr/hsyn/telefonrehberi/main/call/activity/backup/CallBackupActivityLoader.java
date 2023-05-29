package com.tr.hsyn.telefonrehberi.main.call.activity.backup;


import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.Dialog;
import com.tr.hsyn.telefonrehberi.dev.registery.DirectoryEditor;
import com.tr.hsyn.telefonrehberi.dev.registery.Register;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.tool.CallBackupDirectory;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;
import com.tr.hsyn.telefonrehberi.main.dev.backup.BackupDirectory;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;


public abstract class CallBackupActivityLoader extends CallBackupActivityView {
	
	private final String                backupDirectoryName = "call_backups";
	protected     BackupDirectory<Call> callBackupDirectory;
	protected     boolean               waitingNewBackup;
	private       Register              backupRegister;
	
	protected void requestDirectoryAccess() {
		
		Dialog dialog = Dialog.newDialog_YesNo(this);
		
		dialog.title(getString(R.string.select_directory))
				.message(getString(R.string.msg_directory_selection))
				.onConfirm(this::_requestDirectoryAccess)
				.onReject(this::onRejectedDirectorySelection)
				.show();
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		showEmptyView(true);
		
		var mainDirectory = DirectoryEditor.createInstance();
		
		if (mainDirectory.getDirectoryTree() != null) {
			
			var document = DocumentFile.fromTreeUri(this, mainDirectory.getDirectoryTree());
			
			if (document != null && !document.canRead()) {
				
				//- okuma erişimi yok
				requestDirectoryAccess();
				return;
			}
			
			
			backupRegister      = Register.openDocument(backupDirectoryName);
			callBackupDirectory = new CallBackupDirectory(backupRegister);
			loadBackups();
		}
		else {
			
			requestDirectoryAccess();
		}
	}
	
	protected void loadBackups() {
		
		showProgress(true);
		
		Work.on(callBackupDirectory::getBackups)
				.onSuccess(this::setCallBackups)
				.onError(this::onErrorWhenLoadingBackups)
				.onLast(() -> showProgress(false))
				.execute();
	}
	
	protected void onErrorWhenLoadingBackups(@NonNull Throwable throwable) {
		
		setCallBackups(new ArrayList<>(0));
	}
	
	protected void setCallBackups(@Nullable List<Backup<Call>> callBackups) {
		
		adapter.setCallBackups(callBackups != null ? callBackups : new ArrayList<>(0));
		checkEmpty();
	}
	
	private void onDirectoryAccessResult(Uri uri) {
		
		if (uri == null) {
			
			xlog.d("Directory is not selected");
			return;
		}
		
		xlog.d("Directory is selected : %s", uri);
		
		var mainDirectory = DirectoryEditor.createInstance();
		
		
		getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		
		mainDirectory.saveDirectoryTree(uri);
		//- Ana Dizin Kaydedildi ✌
		//------------
		
		backupRegister      = Register.openDocument(backupDirectoryName);
		callBackupDirectory = new CallBackupDirectory(backupRegister);
		loadBackups();
		
		if (waitingNewBackup) {
			
			waitingNewBackup = false;
			addNewBackup();
		}
	}
	
	private void onRejectedDirectorySelection() {
		
		xlog.d("Directory selection rejected");
		
		//- Buradan sonra uygulama dizinini kullanabilirsin
	}
	
	private void _requestDirectoryAccess() {
		
		DirectoryEditor.requestDirectoryTreeAccess(this, this::onDirectoryAccessResult);
	}
	
	
}
