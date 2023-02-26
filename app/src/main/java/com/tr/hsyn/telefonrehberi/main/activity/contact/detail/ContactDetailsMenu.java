package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.message.SMessage;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.telefonrehberi.main.dev.contact.system.SystemContacts;
import com.tr.hsyn.xlog.xlog;


public class ContactDetailsMenu extends CallSummary {
	
	
	private final ActivityResultLauncher<Intent> editCallBack = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), c -> {
		
		xlog.d("Kişiyi düzenleme işlemi tamamlandı");
		
		if (c.getResultCode() == RESULT_OK) {
			
			onEditCompleted();
			
			if (c.getData() != null && c.getData().getData() != null) {
				
				var data = c.getData().getData();
				
				SystemContacts.testUri(data, this);
			}
		}
	});
	
	protected void onEditCompleted() {
		
		Over.Contacts.refreshContacts();
		finishAndRemoveTask();
	}
	
	/**
	 * Kullanıcı menüden kişiyi silmek istediğinde
	 */
	@CallSuper
	protected void onClickDeleteMenu() {
		
		//- Nihai silme işlemi bu kişiyi dönüşte kontrol edecek olan kişiye ait.
		contact.setData(ContactKey.DELETED_DATE, true);
		
		//- Dön
		onBackPressed();
	}
	
	/**
	 * Kişi için düzenleme sayfasına yönlendirir. (menu item click)
	 */
	protected void onClickEditMenu() {
		
		editContact();
	}
	
	/**
	 * Kişiyi edit etmek için telefon uygulamasına postala (action button click)
	 */
	@Override
	protected final void editContact() {
		
		try {
			
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.getContactId()));
			intent.putExtra("finishActivityOnSaveCompleted", true);
			
			editCallBack.launch(intent);
			//startActivityForResult(intent, RC_EDIT_CONTACT);
		}
		catch (Exception e) {
			
			SMessage.builder()
					.message("Bu işlem için yüklü bir uygulama bulunmuyor")
					.build()
					.showOn(this);
		}
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideLeft(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_contact_details_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		switch (item.getItemId()) {
			
			case R.id.delete_contact_menu_item:
				
				onClickDeleteMenu();
				return true;
			
			case R.id.edit_contact_menu_item:
				
				onClickEditMenu();
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
