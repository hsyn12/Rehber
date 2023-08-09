package com.tr.hsyn.telefonrehberi.main.activity.city.station;


import androidx.annotation.CallSuper;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Arama kayıtlarını alıp getirmekle sorumlu.<br>
 * Kayıtları alma işlemi her başlatıldığında bir değişken güncel zamanla kaydedilir.<br>
 * Bu zaman bilgisi {@link #getLastCallLogLoadingStartTime()} metoduyla elde edilebilir.
 */
public abstract class CallLogLoader extends ContactsLoader {
	
	/**
	 * Kayıtları alma işleminin başlama zamanı
	 */
	private long lastCallLogLoadingStartTime;
	
	/**
	 * Kişiler veri tabanından alındığında çağrılır.
	 *
	 * @param calls     Arama kayıtları
	 * @param throwable Varsa hata, yoksa {@code null}
	 */
	protected abstract void onCallLogLoaded(List<Call> calls, Throwable throwable);
	
	/**
	 * Kayıtları veri tabanından alınma işlemini başlatır.
	 */
	@CallSuper
	protected void loadCalls() {
		
		Blue.box(Key.CALL_LOG_LOADING, true);
		lastCallLogLoadingStartTime = Time.now();
		Runny.complete(() -> getCallLogLoader().load())
			.orTimeout(3L, TimeUnit.MINUTES)
			.whenCompleteAsync(this::onCallLogLoaded, getUIThreadExecutor());
	}
	
	/**
	 * @return Kayıtların en son yükleme işleminin başlama zamanı. Hiç yükleme yapılmamışsa {@code 0L}.
	 */
	protected final long getLastCallLogLoadingStartTime() {
		
		return lastCallLogLoadingStartTime;
	}
	
}
