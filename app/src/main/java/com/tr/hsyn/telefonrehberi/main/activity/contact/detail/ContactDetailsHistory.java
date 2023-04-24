package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.history.ActivityCallList;
import com.tr.hsyn.telefonrehberi.main.code.cast.PermissionHolder;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Kişinin arama geçmişini yönetir.
 * <p>Arama geçmişi, kişinin tüm arama türlerini içerir.
 */
public abstract class ContactDetailsHistory extends ContactDetailsHeadWay implements PermissionHolder {
	
	/**
	 * Arama kayıtlarına erişim izni için kullanılan kod.
	 * İzin isteği alt sınıflardan geldiğinde ve sonuç {@link #onPermissionsResult(int, Map)} metodu ile
	 * kontrol edilecekse bu kod ile kontrol edilir.
	 * Ancak izinlerin tamamı onaylanırsa #{@link #onCallPermissionsGrant()} metodu daha pratiktir ve
	 * herhangi bir kontrol yapmaya gerek olmaz.
	 */
	protected final int        RC_CALL_LOG     = 45;
	private final   Gate       gateHistory     = DigiGate.newGate(1000L, this::hideProgress);
	private final   Gate       gateShowHistory = AutoGate.newGate(1000L);
	/**
	 * Kişinin arama geçmişi
	 */
	protected       List<Call> history;
	private         boolean    historyViewAdded;
	private         boolean    isNewHistory    = true;
	private         boolean    needShowHistory;
	private         boolean    isPermissionsRequested;
	
	@Override
	protected void prepare() {
		
		super.prepare();
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		//- Telefon numarası yoksa hiçbir işleme gerek yok
		if (numbers != null && !numbers.isEmpty()) {
			
			setHistory();
		}
	}
	
	/**
	 * Arama kayıtlarına erişim için gereken izinleri kullanıcıdan ister.
	 * İzinlerin sonuçları {@link #onPermissionsResult(int, Map)} metodundan ya da
	 * eğer tüm izinler onaylanır ise {@link #onCallPermissionsGrant()} metodundan alınabilir.
	 * Arama kayıtları izinleri istenirken {@link #RC_CALL_LOG} kodu kullanılır.
	 *
	 * @see #RC_CALL_LOG
	 * @see #onPermissionsResult(int, Map)
	 * @see #onCallPermissionsGrant()
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
	 * Arama kayıtları izinleri kullanıcı tarafından onaylandığında çağrılır.
	 */
	protected void onCallPermissionsGrant() {}
	
	protected void onCallPermissionsDenied() {}
	
	/**
	 * Kişinin arama geçmişi her güncellendiğinde çağrılır.
	 * İlk yükleme de dahil.
	 */
	protected void onHistoryUpdate() {
		
		xlog.dx("Arama geçmişi güncellendi");
	}
	
	/**
	 * Kişinin arama geçmişini set eder
	 *
	 * @see #history
	 */
	protected final void setHistory() {
		
		Runny.run(this::showProgress);
		Work.on(this::getHistory)
				.onSuccess(this::addContactHistoryView)
				.onLast(this::hideProgress)
				.execute();
	}
	
	/**
	 * @return Kişiye ait tüm arama kayıtları
	 */
	protected final List<Call> getHistory() {
		
		//- Eğer arama kayıtları en az bir kez yüklenmiş ise sorun yok
		//- Ancak yüklenmemiş ise, kayıtların buradan yüklenmesi biraz karışıklık yaratabilir.
		List<Call> calls = Over.CallLog.Calls.getCalls();
		
		//- Öncelikle arama kayıtları izinlerine bakılmalı
		if (hasCallLogPermissions()) {
			
			//- Arama kayıtları izinleri, kayıtların en az bir kez yüklendiğini gösterir
			//- Ancak bu yükleme, yükleme istasyonunda mı yoksa burada mı gerçekleşti bilmiyoruz
			//- Kullanıcı uygulamayı ilk kez açıp, kişiler listesinden bir tıkla buraya gelmiş olabilir
			//- Durum böyle ise bu değişkenin null olması gerek
			
			if (calls == null) {
				
				//- Burada anlıyoruzki kullanıcı yükleme istasyonuna gitmemiş
				//- Bu durumda arama kayıtlarını buradan yüklememiz gerek
				
				calls = Over.CallLog.getCallLogManager().load();
			}
			else {
				
				//- Demekki arama kayıtları yükleme istasyonundan yüklenmiş
				//- Buradan sonrası sorun çıkarmaz
				
				xlog.d("Arama kayıtlarına erişim sağlandı");
			}
		}
		else {
			
			//- İzinler yok
			//- Demekki zor yoldan ilerleyeceğiz
			//- İzinlerin cevabı geldiğinde yeni liste oluşturacağız
			//- İzinleri şimdi sormuyoruz çünkü kullanıcının tıklaması gerek
			//- Şimdilik boş dönüyoruz
			
			return new ArrayList<>(0);
		}
		
		//- Kişiye ait arama kayıtlarını döndür
		
		return calls.stream()
				.filter(c -> PhoneNumbers.containsNumber(contact.getData(ContactKey.NUMBERS), c.getNumber()))
				.collect(Collectors.toList());
	}
	
	protected final void onClickShowHistory(View view) {
		
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
				
				//- Kayıtlar yenilendi, bir sonraki yenileme için false yapıyoruz
				//- Eğer bunu yapmazsak activity kısır bir döngüye girer
				
				isNewHistory = false;
			}
			
			needShowHistory = false;
			
			if (history != null) {
				
				if (!history.isEmpty()) {
					
					showHistory(history);
				}
				else {
					
					//- İzin gerekli ise isteyelim
					if (!hasCallLogPermissions()) {
						
						needShowHistory = true;
						requestCallPermissions();
					}
					else
						xlog.d("No call history");
				}
			}
		}
	}
	
	/**
	 * Arama kayıtlarını göster.
	 *
	 * @param history Arama kayıtları
	 */
	protected final void showHistory(List<Call> history) {
		
		if (gateShowHistory.enter()) {
			
			xlog.dx("Show call history for : %s", contact.getName());
			
			//- Geçmişi kaydet
			Blue.box(Key.CALL_HISTORY, history);
			
			Runny.run(() -> {
				
				startActivity(new Intent(this, ActivityCallList.class));
				Bungee.slideRight(this);
			}, 150);
		}
	}
	
	protected void showProgress() {
		
		progressBar.setVisibility(View.VISIBLE);
	}
	
	protected void hideProgress() {
		
		progressBar.setVisibility(View.GONE);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		onPermissionResult(requestCode, permissions, grantResults);
	}
	
	/**
	 * Bu activity sadece arama kayıtları izinlerine ihtiyaç duyuyor.
	 * Ve sadece arama kayıtları izinleri talep ediyor.
	 *
	 * @param requestCode requestCode
	 * @param result      [Permission : true/false] şeklinde izin sonuçlarını taşır
	 */
	@CallSuper
	@Override
	public void onPermissionsResult(int requestCode, Map<String, Boolean> result) {
		
		if (requestCode == RC_CALL_LOG) {
			
			//- Arama kayıtları izinleri var mı
			var grant = result.values().stream().allMatch(Boolean::booleanValue);
			
			if (grant) {
				
				//- Gerekli izni aldık, geçmişi yenileyelim
				if (needShowHistory)
					refreshHistory();
				else setHistory();
				
				//- Yukardakileri de uyarmayı unutmayalım
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
	 * Kişi geçmişini yeniler.
	 */
	private void refreshHistory() {
		
		Runny.run(this::showProgress);
		
		Work.on(this::getHistory)
				.onSuccess(h -> {
					
					history      = h;
					isNewHistory = true;
					
					onClickShowHistory(null);
					onHistoryUpdate();
				})
				.onLast(this::hideProgress)
				.execute();
	}
	
	private void addContactHistoryView(@NonNull List<Call> history) {
		
		this.history = history;
		Blue.box(Key.CALL_HISTORY, history);
		
		//- Kişinin geçmişine yönlendirecek olan görünüm sadece bir kez eklenmeli
		
		if (!historyViewAdded) {
			
			View historyView = getLayoutInflater()
					.inflate(R.layout.show_contact_history, mainContainer, false);
			
			mainContainer.addView(historyView);
			
			ImageView icon = historyView.findViewById(R.id.contact_history_icon);
			Colors.setTintDrawable(icon.getDrawable(), Colors.lighter(Colors.getPrimaryColor(), 0.2f));
			
			View view = historyView.findViewById(R.id.contact_history_item);
			
			view.setBackgroundResource(Colors.getRipple());
			view.setOnClickListener(this::onClickShowHistory);
			
			historyViewAdded = true;
		}
		
		//- Geçmişin boş olması, ya gerçekten herhangi bir geçmiş kayıt olmadığı
		//- yada izinlerden dolayı boş geldiği anlamına gelir
		
		if (history.isEmpty()) {
			
			if (hasCallLogPermissions()) {
				
				//- Geçmiş, izinlerden dolayı değil gerçekten olmadığı için boş
				
				TextView text = findViewById(R.id.text_show_calls);
				text.setText(getString(R.string.no_contact_history));
			}
			else {
				
				//- Geçmiş, izinlerden dolayı boş
				
				xlog.d("Kişinin geçmişi için izinler bekleniyor");
			}
		}
		
		onHistoryUpdate();
	}
	
}
