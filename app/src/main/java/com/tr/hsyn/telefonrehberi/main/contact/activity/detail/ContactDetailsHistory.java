package com.tr.hsyn.telefonrehberi.main.contact.activity.detail;


import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.tr.hsyn.bool.Bool;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.history.ActivityCallList;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.cast.PermissionHolder;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


/**
 * This class's duty is to take the call log
 * and show the call history of the selected contact in a new activity.
 * So, it needs to have {@link Manifest.permission#READ_CALL_LOG} permission.
 * It can request the permission to access the call log if needed
 * (the permission if not has been taken before,
 * it requests {@link PermissionHolder#CALL_LOG_PERMISSIONS}).
 * If permissions are requested,
 * permission results are sent to the subclasses which interested ones.<br>
 *
 * <p>
 * So, this is the class which is responsible for taking the call history.
 * Therefore,
 * classes who are subclasses of this class can request the call history
 * or can request the permissions through it.
 * And they should do so.
 * Thanks.
 */
public abstract class ContactDetailsHistory extends ContactDetailsHeadWay implements PermissionHolder {
	
	/** Request code for call log permissions */
	protected final int            RC_CALL_LOG     = 45;
	/** Gate for accessing the history data */
	private final   Gate           gateHistory     = DigiGate.newGate(1000L, this::hideProgress);
	/** Gate used to block input while showing history. */
	private final   Gate           gateShowHistory = AutoGate.newGate(1000L);
	protected       CallCollection callCollection;
	protected       History        history;
	/** Flag indicating whether the view for the contact history has been added to the UI. */
	private         boolean        historyViewAdded;
	/** Flag indicating whether we need to show the history (after receiving call log permissions). */
	private         boolean        isNewHistory    = true;
	/** Flag indicating whether call log permissions have been requested. */
	private         boolean        needShowHistory;
	/** Flag indicating whether a new history needs to be loaded. */
	private         boolean        isPermissionsRequested;
	
	/**
	 * @inheritDoc Prepares this activity for display by loading the call history for the
	 * 		contact associated with this activity.
	 * 		This method is called only one time by the superclass while the activity has been settings up.
	 * 		So this method is the starting point for this class and for subclasses.
	 */
	@Override
	protected void prepare() {
		//! This call must be first.
		super.prepare();
		
		//- If no any phone numbers, then no history
		
		if (Lister.exist(phoneNumbers)) {
			
			setHistory();
		}
	}
	
	/**
	 * Start to load the call history for the contact associated with this activity.
	 * This method is called when the activity is first created,
	 * and when the user touched the show history view (if a need be).
	 */
	private void setHistory() {
		
		Runny.run(this::showProgress);
		Work.on(this::getCallHistory)
				.onSuccess(this::addContactHistoryView)
				.onLast(this::hideProgress)
				.execute();
	}
	
	/**
	 * Requests call log permissions {@link PermissionHolder#CALL_LOG_PERMISSIONS} from the user.
	 * Overrides the method from {@link PermissionHolder}.
	 *
	 * @see #RC_CALL_LOG
	 * @see #onPermissionsResult(int, Map)
	 * @see #onCallPermissionsGrant()
	 * @see PermissionHolder#CALL_LOG_PERMISSIONS
	 */
	@CallSuper
	protected void requestCallPermissions() {
		
		if (isPermissionsRequested) {
			
			xlog.d("İzinler zaten istendi");
			return;
		}
		
		isPermissionsRequested = true;
		askPermissions(RC_CALL_LOG, CALL_LOG_PERMISSIONS);
	}
	
	/**
	 * Handles cases where call log permissions have been granted.
	 * Override this method to perform custom actions when permissions are granted.
	 *
	 * @see #requestCallPermissions()
	 */
	protected void onCallPermissionsGrant() {}
	
	/**
	 * Handles cases where call log permissions have been denied.
	 * Override this method to perform custom actions when permissions are denied.
	 *
	 * @see #requestCallPermissions()
	 */
	protected void onCallPermissionsDenied() {}
	
	/**
	 * Callback method called when the history each load.
	 * Override this method to perform custom actions when the history loaded
	 * (or at the first loading).
	 */
	protected void onHistoryLoad() {
		
		xlog.dx("Call history is updated");
	}
	
	/**
	 * Loads the call history for the contact associated with this activity.
	 *
	 * @return The call history for the contact, or an empty list if there is no history.
	 * @see #hasCallLogPermissions()
	 */
	@NotNull
	private History getCallHistory() {
		
		//- Eğer arama kayıtları en az bir kez yüklenmiş ise sorun yok
		//- Ancak yüklenmemiş ise, kayıtların buradan yüklenmesi biraz karışıklık yaratabilir.
		callCollection = Blue.getObject(Key.CALL_COLLECTION);
		
		//- Öncelikle arama kayıtları izinlerine bakılmalı
		if (hasCallLogPermissions()) {
			
			//- Arama kayıtları izinleri, kayıtların en az bir kez yüklendiğini gösterir
			//- Ancak bu yükleme, yükleme istasyonunda mı yoksa burada mı gerçekleşti bilmiyoruz
			//- Kullanıcı uygulamayı ilk kez açıp, kişiler listesinden bir tıkla buraya gelmiş olabilir
			//- Durum böyle ise bu değişkenin null olması gerek
			if (callCollection == null) {
				
				//- Burada anlıyoruz ki kullanıcı yükleme istasyonuna gitmemiş
				//- Bu durumda arama kayıtlarını buradan yüklememiz gerek
				
				Over.CallLog.getCallLogManager().load();
			}
			else {
				
				//- Demek ki arama kayıtları yükleme istasyonundan yüklenmiş
				//- Buradan sonrası sorun çıkarmaz
				
				xlog.d("Accessed the call logs");
			}
		}
		else {
			
			//- İzinler yok
			//- Demek ki zor yoldan ilerleyeceğiz
			//- İzinlerin cevabı geldiğinde yeni liste oluşturacağız
			//- İzinleri şimdi sormuyoruz çünkü kullanıcının tıklaması gerek
			//- Şimdilik boş dönüyoruz
			
			return History.ofEmpty(contact);
		}
		
		List<Call> calls = callCollection.getCallsByNumbers(phoneNumbers);
		calls.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
		//- This is the 'call history' of the selected contact
		return History.of(contact, calls);
	}
	
	/**
	 * Adds the view for showing the contact history.
	 * If the user touches this view, the history is shown by starting new activity.
	 * That is the view that starts the action.
	 *
	 * @param history The call history for the contact.
	 */
	private void addContactHistoryView(@NonNull History history) {
		
		this.history = history;
		
		//- Kişinin geçmişine yönlendirecek olan görünüm sadece bir kez eklenmeli
		if (!historyViewAdded) {
			
			historyViewAdded = true;
			
			View historyView = getLayoutInflater().inflate(R.layout.show_contact_history, mainContainer, false);
			
			mainContainer.addView(historyView);
			
			ImageView icon = historyView.findViewById(R.id.contact_history_icon);
			Colors.setTintDrawable(icon.getDrawable(), Colors.lighter(Colors.getPrimaryColor(), 0.2f));
			
			View view = historyView.findViewById(R.id.contact_history_item);
			
			view.setBackgroundResource(Colors.getRipple());
			view.setOnClickListener(this::onShowHistory);
		}
		
		//- Geçmişin boş olması, ya gerçekten herhangi bir geçmiş kayıt olmadığı
		//- yada izinlerden dolayı boş geldiği anlamına gelir
		
		if (history.isEmpty()) {
			
			if (hasCallLogPermissions()) {
				
				//- Geçmiş, izinlerden dolayı değil gerçekten olmadığı için boş
				//- Dolayısıyla hiç bir işlem yapmaya gerek yok
				TextView text = findViewById(R.id.text_show_calls);
				text.setText(getString(R.string.no_contact_history));
			}
			else {
				
				//- Geçmiş, izinlerden dolayı boş
				
				xlog.d("Kişinin geçmişi için izinler bekleniyor");
			}
		}
		
		// Burada arama geçmişinin güncellendiğini bildiriyoruz.
		// Bu sınıftaki görsel tıklandığında tüm geçmiş gösterilir.
		// Ama henüz böyle bir şey yok.
		// Sadece geçmiş ayarlandı.
		onHistoryLoad();
	}
	
	/**
	 * Listener for the show history event.
	 *
	 * @param view Clicked view
	 */
	protected final void onShowHistory(View view) {
		
		//- view null ise bu metodu biz kendimiz çağırmışız demektir
		//- Kapıya takılmamak için bunu kontrol etmemiz gerek
		//- Çünkü geçmişi yenilerken kapının açılma süresinden daha önce gelmiş olma ihtimalimiz yüksek
		//- Eğer görünüme kullanıcı tıklıyorsa kapıya takılmak zorunda,
		//- biz tıklıyorsak (metodu biz çağırıyorsak) direk girmemiz lazım
		
		if (gateHistory.enter() || view == null) {
			
			//- Karışıklık burada başlıyor
			//- Çünkü kişinin geçmişine gidildiyse ve bir silme işlemi gerçekleşti ise
			//- arama kayıtları için bir yenileme bilgisi kaydedilir
			//- Bu bilgi aslında ana ekran için
			//- Ancak kullanıcı buradan çıkmadığı için bazı ayarlamalar yapmamız gerekiyor
			if (Over.CallLog.Calls.isUpdated().bool()) {
				
				//- Evet kayıtların yenilenmesi gerek
				//- Ancak bu bilgiye ana ekranın ihtiyaç duyması silinmesini engelliyor
				//- Biz de kendi değişkenimizi kullanıyoruz
				if (!isNewHistory) {
					
					//- Arama kayıtları her yüklendiğinde isNewHistory true oluyor
					//- Eğer true değilse tekrar yenilememiz gerek
					refreshHistory();
					return;
				}
				
				//- Kayıtlar yenilenmiş, bir sonraki yenileme için false yapıyoruz
				//- Eğer bunu yapmazsak activity kısır bir döngüye girer
				isNewHistory = false;
			}
			
			needShowHistory = false;
			
			if (history != null) {
				
				if (!history.isEmpty()) {
					
					showCalls(history.getCalls());
				}
				else {
					
					//- İzin gerekli ise iste
					if (!hasCallLogPermissions()) {
						
						needShowHistory = true;
						requestCallPermissions();
					}
					else xlog.d("No call history");
				}
			}
		}
	}
	
	/**
	 * Shows a progress indicator.
	 */
	protected void showProgress() {
		
		progressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hides the progress indicator.
	 */
	protected void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
	}
	
	/**
	 * Callback method called when a permission request is made.
	 * Overrides the method from {@link PermissionHolder}.
	 *
	 * @param requestCode The request code for the permission request.
	 * @param result      A mapping of permissions to grant/deny outcomes.
	 * @see #RC_CALL_LOG
	 */
	@CallSuper
	@Override
	public void onPermissionsResult(int requestCode, Map<String, Boolean> result) {
		
		if (requestCode == RC_CALL_LOG) {
			
			//- Arama kayıtları izinleri var mı
			var grant = result.values().stream().allMatch(Boolean::booleanValue);
			
			if (grant) {
				
				//- Gerekli izni aldık, geçmişi yenileyelim
				if (needShowHistory) refreshHistory();
				else setHistory();
				
				//- Yukarıdakileri de uyarmayı unutmayalım
				onCallPermissionsGrant();
			}
			else {
				
				xlog.d("Arama kaydı izinleri reddedildi ");
				needShowHistory = false;
				onCallPermissionsDenied();
			}
		}
		
		isPermissionsRequested = false;
	}
	
	/**
	 * Refreshes the call history for the contact associated with this activity.
	 */
	private void refreshHistory() {
		
		Runny.run(this::showProgress);
		
		Work.on(this::getCallHistory)
				.onSuccess(h -> {
					
					isNewHistory = true;
					onShowHistory(null);
					onHistoryLoad();
				})
				.onLast(this::hideProgress)
				.execute();
	}
	
	/**
	 * Shows the call history for the contact associated with this activity.
	 */
	protected final void showCalls(List<Call> calls) {
		
		if (gateShowHistory.enter()) {
			
			xlog.dx("Show call history for : %s", contact.getName());
			
			//- Geçmişi kaydet
			Blue.box(Key.SHOW_CALLS, calls);
			
			Runny.run(() -> {
				
				startActivity(new Intent(this, ActivityCallList.class));
				Bungee.slideRight(this);
			}, 150);
		}
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//- Arama kayıtlarında bir güncelleme varsa bilgilerin tekrar düzenlenmesi gerek
		
		if (Over.CallLog.Calls.isUpdated(Bool.NONE).bool()) {
			
			xlog.d("There is an update of call log");
			setHistory();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		onPermissionResult(requestCode, permissions, grantResults);
	}
	
}