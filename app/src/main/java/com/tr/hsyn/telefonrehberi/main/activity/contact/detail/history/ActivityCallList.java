package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.history;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.story.call.CallStory;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a screen which shows the call history of the selected contact.
 * Selected contact should have been set by {@link Key#SELECTED_CONTACT} key,
 * otherwise it will throw an exception.<br>
 * Any call can be deleted from the list.
 * If any call is deleted, {@link Key#REFRESH_CALL_LOG} key is set to true.<br>
 */
public class ActivityCallList extends ActivityCallHistoryView {
	
	/**
	 * Manager for call log
	 */
	private final CallStory          callStory = Blue.getObject(Key.CALL_STORY);
	/**
	 * Call history list of the selected contact
	 */
	private       List<Call>         calls;
	/**
	 * Adapter for the list
	 */
	private       CallHistoryAdapter adapter;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		Contact contact = Blue.getObject(Key.SELECTED_CONTACT);
		
		if (contact == null) throw new IllegalArgumentException("Contact is null");
		
		calls = Blue.getObject(Key.SHOW_CALLS);
		
		if (calls == null) calls = new ArrayList<>(0);
		
		if (!calls.isEmpty()) {
			
			var aCall = calls.get(0);
			var name  = aCall.getName();
			
			setTitle(name != null ? name : aCall.getNumber());
			
			if (name == null) checkName();
		}
		else {
			
			setTitle(getString(R.string.call_logs));
		}
		
		setSubtitle(String.valueOf(calls.size()));
		
		adapter = new CallHistoryAdapter(calls == null ? new ArrayList<>(0) : calls, this::onDeleted);
		
		list.setAdapter(adapter);
		
		updateSize();
	}
	
	/**
	 * If the contact is registered in the Contacts, it checks its name and writes it to the toolbar.
	 */
	private void checkName() {
		
		Runny.run(() -> {
			
			var call = calls.get(0);
			
			if (call.getLong(CallKey.CONTACT_ID, 0L) != 0L) {
				
				var name = Calls.getContactName(getContentResolver(), call.getNumber());
				
				if (name != null) {
					
					Runny.run(() -> setTitle(name));
				}
				else {
					
					xlog.d("Kişinin ismi bulunamadı");
				}
			}
			
		}, false);
	}
	
	/**
	 * Called when the user wants to delete a record in the list. A permanent deletion is performed here.
	 *
	 * @param index index of the record to be deleted
	 */
	private void onDeleted(int index) {
		
		//- Silinecek kayıt
		Call call = calls.get(index);
		calls.remove(index);//- Sil
		adapter.notifyItemRemoved(index);
		updateSize();
		
		Runny.run(() -> {
			
			boolean deleted = callStory.delete(call);
			
			if (deleted) {
				
				xlog.d("Silindi : %s", call);
			}
			else {
				
				xlog.d("Arama kaydı silinemedi : %s", call);
			}
			
			//- Arama kayıtlarında kalıcı bir değişiklik oldu
			//- Bu bilgiye ihtiyaç duyanlar için bir işaret bırak
			
			Over.CallLog.refreshCallLog();
			Over.CallLog.Calls.Editor.delete(call);
		}, false);
	}
	
	/**
	 * Updates the record count when deletion is done
	 */
	private void updateSize() {
		
		setSubtitle(String.valueOf(calls.size()));
		
		if (calls.isEmpty()) emptyView.setVisibility(View.VISIBLE);
		else emptyView.setVisibility(View.GONE);
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideLeft(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.call_history_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		if (item.getItemId() == R.id.menu_delete_all) {
			
			deleteAll();
			return true;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Called when the user wants to delete all call history from the menu.
	 * And permanently deletes all records.
	 */
	private void deleteAll() {
		
		if (calls.isEmpty()) return;
		
		adapter.notifyItemRangeRemoved(0, calls.size() - 1);
		
		var deletedCalls = new ArrayList<>(calls);
		calls.clear();
		
		updateSize();
		
		Runny.run(() -> {
			
			var deleted = callStory.deleteFromSystem(deletedCalls);
			
			if (deleted == deletedCalls.size()) {
				
				long now = System.currentTimeMillis();
				deletedCalls.forEach(c -> c.setData(CallKey.DELETED_DATE, now));
				
				var _deleted = callStory.updateFromDatabase(deletedCalls);
				
				if (_deleted == deletedCalls.size()) {
					
					xlog.d("Tüm geçmiş silindi");
				}
				else {
					
					if (_deleted > 0) {
						
						xlog.d("Arama geçmişi sistemden silindi ancak veri tabanından tamamı silinemedi. %d kayıt silindi, %d kayıt silinemedi", _deleted, deletedCalls.size() - _deleted);
					}
					else {
						
						xlog.d("Arama geçmişi veri tabanından silinemedi");
					}
				}
			}
			else {
				
				if (deleted > 0) {
					
					xlog.d("%d arama kaydı gerçmişten silindi ancak %d kayıt silinemedi", deleted, deletedCalls.size() - deleted);
				}
				else {
					
					xlog.d("Geçmiş silinemedi");
				}
			}
			
			//- Arama kayıtlarında kalıcı bir değişiklik oldu
			//- Bu bilgiye ihtiyaç duyanlar için bir işaret bırak
			
			Over.CallLog.refreshCallLog();
			Over.CallLog.Calls.Editor.delete(deletedCalls.toArray(new Call[0]));
		}, false);
	}
}
