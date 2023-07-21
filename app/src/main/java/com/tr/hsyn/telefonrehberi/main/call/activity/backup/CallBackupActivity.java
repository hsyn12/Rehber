package com.tr.hsyn.telefonrehberi.main.call.activity.backup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


//todo Dosya yazma işlemini Android 10 ve yukarısı için düzenle

/**
 * <h2>CallBackupActivity</h2>
 * <p>
 * Arama kayıtları yedekleme işleri.<br>
 * Yedekleri görüntüleme, yeni yedek oluşturma ve var olan yedeği silme yada güncelleme hizmeti sunar.
 *
 * @author hsyn 15 Mart 2022 Salı 21:41:21
 */
public class CallBackupActivity extends CallBackupActivityRestore {
	
	@Override
	protected List<Call> getCallLogCalls() {
		
		return Blue.getObject(Key.CALL_LOG_CALLS);
	}
	
	@Override
	protected void onErrorWhenLoadingBackups(@NonNull Throwable throwable) {
		
		super.onErrorWhenLoadingBackups(throwable);
		
		String errorMessage = Stringx.format("Arama kaydı yedekleri yüklenemiyor");
		
		xlog.e(errorMessage);
		
		throwable.printStackTrace();
		
		Show.tost(this, errorMessage);
	}
	
	@Override
	protected void setCallBackups(@Nullable List<Backup<Call>> callBackups) {
		
		super.setCallBackups(callBackups);
		
		xlog.d("Call backup size : %d", callBackups != null ? callBackups.size() : -1);
	}
}
