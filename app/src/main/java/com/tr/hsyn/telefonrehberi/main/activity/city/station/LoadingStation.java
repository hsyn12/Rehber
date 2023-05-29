package com.tr.hsyn.telefonrehberi.main.activity.city.station;


import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * Burası köprünün diğer tarafı, Yükleme istasyonu.
 * Tüm bilgiler bu activity ile yüklenir.<br>
 */
public abstract class LoadingStation extends CallLogLoader {
	
	private boolean contactsLoaded, callsLoaded;
	
	@Override
	protected void onPageChange(int page) {
		
		super.onPageChange(page);
		
		if (getCurrentPage() == PAGE_CALL_LOG) {
			
			if (!hasCallLogPermissions()) {
				
				askCallLogPermissions();
			}
			else {
				
				if (!callsLoaded) loadCalls();
			}
		}
		else {
			
			if (!hasContactsPermissions()) {
				
				askContactPermissions();
			}
			else {
				
				if (!contactsLoaded) loadContacts();
			}
		}
	}
	
	@Override
	protected void loadCalls() {
		
		pageCallLog.showProgress();
		if (!hasCallLogPermissions()) {
			
			if (pageCallLog.isShowTime())
				askCallLogPermissions();
			
			return;
		}
		
		super.loadCalls();
	}
	
	@CallSuper
	@Override
	protected void loadContacts() {
		
		pageContacts.showProgress();
		
		if (!hasContactsPermissions()) {
			
			askContactPermissions();
			return;
		}
		
		super.loadContacts();
	}
	
	/**
	 * Kişiler yüklendiğinde buraya yönlendirilir.
	 *
	 * @param contacts Kişiler
	 */
	@Override
	@CallSuper
	protected void onContactsLoaded(List<Contact> contacts, @Nullable Throwable throwable) {
		
		contactsLoaded = true;
		
		if (throwable == null) {
			
			var loadTime = Time.now() - getLastContactsLoadingStartTime();
			
			pageContacts.setList(contacts);
			xlog.d("Contacts Loaded [size=%d, loadTime=%dms]", contacts.size(), loadTime);
			
			Blue.box(Key.CONTACTS, contacts);
		}
		else {
			
			if (throwable instanceof TimeoutException) {
				
				xlog.wx("Contacts could not loaded with defined duration");
			}
			else {
				
				xlog.ex("Contacts could not loaded", throwable);
			}
		}
		
		pageContacts.hideProgress();
	}
	
	@CallSuper
	@Override
	protected void onCallLogLoaded(List<Call> calls, Throwable throwable) {
		
		callsLoaded = true;
		
		if (throwable == null) {
			
			var loadTime = Time.now() - getLastContactsLoadingStartTime();
			
			pageCallLog.setList(calls);
			Blue.box(Key.CALL_LOG, calls);
			xlog.d("CallLog Loaded [size=%d, loadTime=%dms]", calls.size(), loadTime);
		}
		else {
			
			if (throwable instanceof TimeoutException) {
				
				xlog.wx("CallLog could not loaded with defined duration");
			}
			else {
				
				xlog.ex("CallLog could not loaded", throwable);
			}
		}
		
		pageCallLog.hideProgress();
	}
	
	@CallSuper
	@Override
	protected void onResume() {
		
		super.onResume();
		
		Boolean reCalls    = Blue.remove(Key.REFRESH_CALL_LOG);
		Boolean reContacts = Blue.remove(Key.REFRESH_CONTACTS);
		
		if (reContacts != null) {
			
			if (reContacts) {
				
				loadContacts();
				
				xlog.i("Contacts reloaded");
			}
		}
		
		if (reCalls != null) {
			
			if (reCalls) {
				
				loadCalls();
				
				xlog.i("Calls reloaded");
			}
		}
	}
	
	@CallSuper
	@Override
	protected void onGrantContactsPermissions() {
		
		xlog.d("Rehber izni onaylandı ✌");
		loadContacts();
	}
	
	@Override
	protected void onDeniedContactsPermissions(@NonNull Map<String, Boolean> result) {
		
		super.onDeniedContactsPermissions(result);
		
		pageContacts.hideProgress();
	}
	
	@CallSuper
	@Override
	protected void onGrantCallsPermissions() {
		
		xlog.d("Arama izni onaylandı ✌");
		loadCalls();
	}
	
	@Override
	protected void onDeniedCallsPermissions(@NonNull Map<String, Boolean> result) {
		
		super.onDeniedCallsPermissions(result);
		
		pageCallLog.hideProgress();
	}
	
	
}
