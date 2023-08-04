package com.tr.hsyn.telefonrehberi.main.activity.city;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.page.MenuShower;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.android.dialog.Dialog;
import com.tr.hsyn.telefonrehberi.main.activity.city.station.LoadingStation;
import com.tr.hsyn.telefonrehberi.main.activity.color.ColorsActivity;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.contact.activity.detail.ContactDetails;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactsReader;
import com.tr.hsyn.telefonrehberi.main.data.ContactLog;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuEditor;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuManager;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.use.Use;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <h2>BlackTower</h2>
 * <p>
 * Kara Kule. Bazı önemli olayları izler ve işler.
 *
 * @author hsyn 01 Haziran 2022 Çarşamba 11:22
 */
public abstract class BlackTower extends LoadingStation implements MenuProvider, MenuShower, SwipeRefreshLayout.OnRefreshListener {
	
	/**
	 * Otomatik kapılar için bekleme süresi.<br>
	 * Bu süre sonunda kapı açılır.
	 */
	protected final long                           GATE_WAIT_DURATION   = 2000L;
	private final   AtomicBoolean                  loadingCompleted     = new AtomicBoolean(false);
	/**
	 * Renk değişiminin bildirileceği callback.<br>
	 */
	private final   ActivityResultLauncher<Intent> colorChangeCallBack  = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), c -> {
		
		int    result = c.getResultCode();
		Intent data   = c.getData();
		
		if (result == RESULT_OK) {
			
			if (data != null) {
				
				int selectedColor = data.getIntExtra(ColorsActivity.SELECTED_COLOR, -1);
				
				if (selectedColor != -1) {
					
					Runny.run(() -> onChangeColor(selectedColor));
				}
			}
		}
	});
	/**
	 * Rehbere yeni bir kişi eklendiğinde haberdar edilecek olan callback
	 */
	private final   ActivityResultLauncher<Intent> newContactCallBack   = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), c -> {
		
		if (c.getResultCode() == RESULT_OK) {
			
			loadContacts();
		}
	});
	/**
	 * Kişiler listesi üzerindeki seçim işleminin hızını düzenler.<br>
	 * Listeden bir kişi seçildiğinde, {@link #GATE_WAIT_DURATION} süresi geçilmeden tekrar bir seçim yapılamaz.
	 */
	private final   Gate                           keepContactSelection = AutoGate.newGate(GATE_WAIT_DURATION);
	/**
	 * Arama kayıtları listesi üzerindeki seçim işleminin hızını düzenler.<br>
	 * Listeden bir kişi seçildiğinde, {@link #GATE_WAIT_DURATION} süresi geçilmeden tekrar bir seçim yapılamaz.
	 */
	private final   Gate                           keepCallSelection    = AutoGate.newGate(GATE_WAIT_DURATION);
	/**
	 * Telefon aramalarını kontrol eder ve {@link #GATE_WAIT_DURATION} süresi geçilmeden
	 * sonraki aramayı kabul etmez.
	 */
	private final   Gate                           keepCallAction       = AutoGate.newGate(GATE_WAIT_DURATION);
	/**
	 * {@linkplain MenuEditor}
	 */
	private         MenuEditor                     menuManager;
	
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		
		Blue.box(Key.RELATION_DEGREE, 1);
		addMenuProvider(this);
		
		setListeners();
		//lifeStart();
		
		
	}
	
	@Override
	protected void onClickNewContact(View view) {
		
		newContactCallBack.launch(ContactsReader.createNewContactIntent());
	}
	
	@Override
	protected void onDestroy() {
		
		//lifeEnd();
		super.onDestroy();
	}
	
	@Override
	public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
		
		menuInflater.inflate(R.menu.activity_main_menu, menu);
		menuManager = new MenuManager(menu, Lister.listOf(R.id.main_menu_colors));
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onMenuItemSelected(@NonNull @NotNull MenuItem item) {
		
		int id = item.getItemId();
		
		//noinspection SwitchStatementWithTooFewBranches
		switch (id) {
			
			case R.id.main_menu_colors:
				
				Intent intent = new Intent(this, ColorsActivity.class);
				
				colorChangeCallBack.launch(intent);
				
				Bungee.slideUp(this);
				return true;
			
		}
		
		return false;
	}
	
	/**
	 * Sayfa yenileme talebi gönderildi.
	 */
	@Override
	public void onRefresh() {
		
		if (currentPage == PAGE_CONTACTS) loadContacts();
		else loadCalls();
	}
	
	@Override
	public void showMenu(boolean show) {
		
		pageContacts.showMenu(show);
		pageCallLog.showMenu(show);
		menuManager.setVisible(menuManager.getMenuItemResourceIds(), show);
	}
	
	@Override
	protected void onCallLogLoaded(List<Call> calls, Throwable throwable) {
		
		super.onCallLogLoaded(calls, throwable);
		
		if (loadingCompleted.getAndSet(true)) {
			
			if (throwable == null)
				setGlobals();
		}
		
		Blue.remove(Key.CALL_LOG_LOADING);
	}
	
	@Override
	protected void onContactsLoaded(List<Contact> contacts, @Nullable Throwable throwable) {
		
		super.onContactsLoaded(contacts, throwable);
		
		if (loadingCompleted.getAndSet(true)) {
			
			if (throwable == null)
				setGlobals();
		}
		
		Blue.remove(Key.CONTACTS_LOADING);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		Use.ifNotNull(Over.Contacts.getSelectedContact(), this::checkSelectedContact);
	}
	
	private void setGlobals() {
		
		ContactLog.createGlobal(Objects.requireNonNull(Blue.getObject(Key.CONTACTS)));
		CallLog.createGlobal(Objects.requireNonNull(Blue.getObject(Key.CALL_LOG)));
	}
	
	/**
	 * Dinleyicileri ayarla.
	 */
	private void setListeners() {
		
		//! Sayfa hazır olduğunda yükleme işlemi başlatılacak
		//! Uygulama döngüsü burada başlıyor
		pageContacts.setOnReady(this::loadContacts);
		pageCallLog.setOnReady(this::loadCalls);
		
		pageContacts.setScrollListener(this);
		pageContacts.setPageOwner(this);
		//pageCallLog.setScrollListener(this);
		pageCallLog.setPageOwner(this);
		
		pageContacts.setItemSelectListener(this::onContactSelected);
		pageCallLog.setItemSelectListener(this::onCallSelected);
		pageContacts.setSwipeListener(this::onContactSwipe);
		pageCallLog.setSwipeListener(this::onCallSwipe);
		pageCallLog.setOnCallAction(this::onCallAction);
		pageCallLog.setOnDeleteAll(this::onDeleteAllCalls);
		pageContacts.setRefreshListener(this);
	}
	
	/**
	 * Kişi seçildi.
	 *
	 * @param index Seçilen kişinin listedeki index'i
	 */
	@CallSuper
	protected void onContactSelected(int index) {
		
		//- Kişi listesinden bir kişi seçildi (kullanıcı tarafından)
		//- Art arda seçimleri görmezden gel 
		if (keepContactSelection.enter()) {
			
			Contact selectedContact = pageContacts.getItem(index);
			Blue.box(com.tr.hsyn.key.Key.SELECTED_CONTACT, selectedContact);
			startActivity(new Intent(this, ContactDetails.class));
			Bungee.slideRight(this);
			
			xlog.d("Contact Selected : %s", selectedContact);
		}
	}
	
	/**
	 * Arama kaydı seçildi.
	 *
	 * @param index Seçilen kaydın listedeki index'i
	 */
	@CallSuper
	protected void onCallSelected(int index) {
		
		if (keepCallSelection.enter()) {
			
			com.tr.hsyn.calldata.Call call = pageCallLog.getItem(index);
			
			xlog.d("Selected : %s", call);
			
			Blue.box(Key.SELECTED_CALL, call);
		}
	}
	
	@CallSuper
	protected void onContactSwipe(int index) {
		
		Contact contact = pageContacts.getItem(index);
		
		xlog.d("Swipe : %s", contact);
	}
	
	@CallSuper
	protected void onCallSwipe(int index) {
		
		
		Call call = pageCallLog.getItem(index);
		pageCallLog.deleteItem(index);
		String number = Stringx.overWrite(call.getNumber());
		
		if (getCallStory().delete(call)) {
			
			xlog.d("Arama kaydı silindi : %s", number);
		}
		else {
			
			xlog.d("Arama kaydı silinemedi : %s", number);
		}
	}
	
	@CallSuper
	protected void onCallAction(int index) {
		
		if (keepCallAction.enter()) {
			
			//todo Aramayı gerçekleştir
			
			Call call = pageCallLog.getItem(index);
			xlog.d("Call action : %s", Stringx.overWrite(call.getNumber()));
		}
		
	}
	
	private void onDeleteAllCalls() {
		
		Dialog dialog = Dialog.newDialog(this);
		
		dialog.title(getString(R.string.confirm_delete_action))
				.message(new Spanner()
						         .append(Stringx.format("%s ", pageCallLog.getFilterName()), Spans.bold())
						         .append(getString(R.string.will_delete)))
				.confirmText(getString(R.string.delete_all))
				.onConfirm(() -> deleteCalls(pageCallLog.deleteAllItems(), dialog))
				.show();
		
		
		/*DialogConfirmation confirmation = new DialogConfirmation(this);
		confirmation.title(getString(R.string.confirm_delete_action))
				.positiveButton(getString(R.string.delete_all), (d, w) -> deleteCalls(pageCallLog.deleteAll(), d))
				.message(
						new Spanner()
								.append(Stringx.format("%s ", pageCallLog.getFilterName()), Spans.bold())
								.append(getString(R.string.will_delete)))
				.show();*/
	}
	
	private void deleteCalls(List<com.tr.hsyn.calldata.Call> deletedCalls, @NonNull Dialog dialog) {
		
		onBackground(() -> {
			
			int count = getCallStory().delete(deletedCalls);
			
			if (count == deletedCalls.size()) {
				
				xlog.d("%d arama kaydı silindi", count);
			}
			else {
				
				xlog.d("%d arama kaydından %d tanesi silindi", deletedCalls.size(), count);
			}
		});
		
		dialog.dismiss();
	}
	
	/**
	 * Seçilen kişinin silinme durumunu kontrol eder.<br>
	 * Bazı yerlerde veri tabanına direk erişim vardır ancak görsel bağlantıları yoktur,
	 * böyle yerlerde kişi ile ilgili güncellemeler veri tabanı üzerinde yapılır ve
	 * dönüşte kişinin ilişkili olduğu görsel nesneler kişiyi kontrol ederek
	 * bir güncelleme olup olmadığa bakar ve
	 * sadece görsel güncellemeyi icraa eder.<br>
	 * Bu metot kişiyi kontrol eder ve silinmiş ise kişiyi kişiler listesinden çıkarır.
	 *
	 * @param selectedContact Seçilen kişi
	 */
	private void checkSelectedContact(@NonNull Contact selectedContact) {
		
		if (ContactKey.isDeleted(selectedContact)) {
			
			onSelectedContactDeleted(selectedContact);
		}
	}
	
	protected void onSelectedContactDeleted(@NonNull Contact contact) {
		
		xlog.d("Selected contact deleted : %s", contact);
		pageContacts.deleteItem(contact);
	}
}
