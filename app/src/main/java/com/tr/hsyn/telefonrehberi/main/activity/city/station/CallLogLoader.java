package com.tr.hsyn.telefonrehberi.main.activity.city.station;


import androidx.annotation.CallSuper;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.time.Time;

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
		
		lastCallLogLoadingStartTime = Time.now();
		completeWork(() -> getCallLogLoader().load())
				.orTimeout(15L, TimeUnit.SECONDS)
				.whenCompleteAsync(this::onCallLogLoaded, getUIThreadExecutor());
	}
	
	/**
	 * @return Kayıtların en son yükleme işleminin başlama zamanı. Hiç yükleme yapılmamışsa {@code 0L}.
	 */
	protected final long getLastCallLogLoadingStartTime() {
		
		return lastCallLogLoadingStartTime;
	}
	
}
