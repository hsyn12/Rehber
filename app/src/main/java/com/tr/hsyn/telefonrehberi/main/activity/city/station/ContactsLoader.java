package com.tr.hsyn.telefonrehberi.main.activity.city.station;


import androidx.annotation.CallSuper;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.app.UIThread;
import com.tr.hsyn.telefonrehberi.main.activity.city.BigBank;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.treadedwork.ThreadedWork;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Kişileri veri tabanından alıp getirmekle sorumlu.<br>
 * Kişileri alma işlemi her başlatıldığında bir değişken güncel zamanla kaydedilir.<br>
 * Bu zaman bilgisi {@link #getLastContactsLoadingStartTime()} metoduyla elde edilebilir.
 */
public abstract class ContactsLoader extends BigBank implements ThreadedWork, UIThread {
	
	/**
	 * Kişileri alma işleminin başlama zamanı
	 */
	private long contactsLoadStartTime;
	
	/**
	 * Kişilerin veri tabanından alınma işlemini başlatır.
	 */
	@CallSuper
	protected void loadContacts() {
		
		contactsLoadStartTime = Time.now();
		completeWork(() -> getContactsLoader().load())
				.orTimeout(15L, TimeUnit.SECONDS)
				.whenCompleteAsync(this::onContactsLoaded, getUIThreadExecutor());
	}
	
	/**
	 * Kişiler veri tabanından alındığında çağrılır.
	 *
	 * @param contacts  Kişi listesi
	 * @param throwable Varsa hata, yoksa {@code null}
	 */
	protected abstract void onContactsLoaded(List<Contact> contacts, Throwable throwable);
	
	/**
	 * @return Kişilerin en son yükleme işleminin başlama zamanı. Hiç yükleme yapılmamışsa {@code 0L}.
	 */
	protected final long getLastContactsLoadingStartTime() {
		
		return contactsLoadStartTime;
	}
	
	
}
