package com.tr.hsyn.telefonrehberi.main.activity.city.station;


import androidx.annotation.CallSuper;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.app.UIThread;
import com.tr.hsyn.telefonrehberi.main.activity.city.BigBank;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Kişileri veri tabanından alıp getirmekle sorumlu.<br>
 * Kişileri alma işlemi her başlatıldığında bir değişken güncel zamanla kaydedilir.<br>
 * Bu zaman bilgisi {@link #getLastContactsLoadingStartTime()} metoduyla elde edilebilir.
 */
public abstract class ContactsLoader extends BigBank implements UIThread {
	
	/**
	 * Kişileri alma işleminin başlama zamanı
	 */
	private long contactsLoadStartTime;
	
	/**
	 * Kişiler veri tabanından alındığında çağrılır.
	 *
	 * @param contacts  Kişi listesi
	 * @param throwable Varsa hata, yoksa {@code null}
	 */
	protected abstract void onContactsLoaded(List<Contact> contacts, Throwable throwable);
	
	/**
	 * Kişilerin veri tabanından alınma işlemini başlatır.
	 */
	@CallSuper
	protected void loadContacts() {
		
		Blue.box(Key.CONTACTS_LOADING, true);
		contactsLoadStartTime = Time.now();
		
		Runny.complete(() -> getContactsLoader().load())
			.orTimeout(3L, TimeUnit.MINUTES)
			.whenCompleteAsync(this::onContactsLoaded, getUIThreadExecutor());
	}
	
	/**
	 * @return Kişilerin en son yükleme işleminin başlama zamanı. Hiç yükleme yapılmamışsa {@code 0L}.
	 */
	protected final long getLastContactsLoadingStartTime() {
		
		return contactsLoadStartTime;
	}
	
	
}
