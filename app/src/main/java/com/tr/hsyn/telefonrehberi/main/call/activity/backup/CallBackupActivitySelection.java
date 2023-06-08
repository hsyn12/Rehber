package com.tr.hsyn.telefonrehberi.main.call.activity.backup;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.dialog.DialogBackupOptions;
import com.tr.hsyn.xlog.xlog;

import java.util.Arrays;


public abstract class CallBackupActivitySelection extends CallBackupActivityLoader {
	
	private final Gate gateSelect = Gate.newGate();
	protected     int  selectedBackup;
	
	protected abstract void show();
	
	protected abstract void restore();
	
	@Override
	public void onItemIndex(int index) {
		
		selectedBackup = index;
		
		if (gateSelect.enter()) {
			
			String[] options = getResources().getStringArray(R.array.backup_options);
			
			DialogBackupOptions dialog =
					new DialogBackupOptions(
							this,
							Arrays.asList(options),
							this::onOptionSelect);
			
			Runny.run(() -> {
				
				dialog.show();
				gateSelect.exit();
			}, 250);
		}
		else {
			
			xlog.i("Seçme işlemi tamamlanmadan yapılan yeni bir seçim işlemi reddedildi");
		}
	}
	
	private void onOptionSelect(int option) {
		
		if (gateSelect.enter()) {
			
			try {
				switch (option) {
					
					case 0:
						show();
						break;
					case 1:
						restore();
						break;
					case 2:
						deleteBackup(selectedBackup);
						break;
				}
			}
			finally {
				Runny.run(gateSelect::exit, 600);
			}
		}
	}
	
	
}
