package com.tr.hsyn.telefonrehberi.main.contact.activity.detail;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


/**
 * CallSummary means to look more general about the call history of the selected contact.
 * This is the best sight on 'call history'.
 * Because all communication gets clear at first look.
 */
public abstract class CallSummary extends ContactDetailsHistory {
	
	/**
	 * Gate used to block input while showing summary
	 */
	private final Gate      gateSummary = AutoGate.newGate(1000L);
	/**
	 * The view, which the {@link #callSummaryView} placed into.
	 */
	private       ViewGroup mainLayout;
	/**
	 * Indicates whether the history has updated.
	 */
	private       boolean   historyUpdated;
	/**
	 * Indicates whether it needs to show the summary
	 */
	private       boolean   needShowSummary;
	/**
	 * Indicates whether the summary view has added
	 */
	private       boolean   summaryViewAdded;
	/**
	 * The view for the summary
	 */
	private       View      callSummaryView;
	/**
	 * Indicates whether the summary opened
	 */
	private       boolean   isOpenedSummary;
	/**
	 * Indicates whether any open summary
	 */
	private       boolean   anyOpenSummary;
	
	/**
	 * @inheritDoc Prepares the summary view.
	 * 		This is the entry point of all works.
	 */
	@Override
	protected void prepare() {
		//! This call must be first
		super.prepare();
		//_ mainLayout already in there
		mainLayout = findView(R.id.call_summary_body);
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers == null || numbers.isEmpty()) {
			//_ if no numbers, do not show anything
			removeFromDetailView(mainLayout);
			return;
		}
		
		//- Bu, arama özeti görünümünün başlığıdır ve mainLayout içinde halihazırda bulunmaktadır
		View summaryHeader = mainLayout.findViewById(R.id.call_summary_header);
		summaryHeader.setBackgroundResource(Colors.getRipple());
		
		//- Bu başlığa tıklanana kadar arama özeti ile ilgili hiçbir işlem yapılmayacak
		summaryHeader.setOnClickListener(this::onClickSummaryHeader);
		
		ImageView icon = findView(summaryHeader, R.id.call_summary_icon);
		
		Colors.setTintDrawable(icon.getDrawable(), Colors.lighter(Colors.getPrimaryColor(), 0.2f));
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void onCallPermissionsGrant() {
		
		super.onCallPermissionsGrant();
		
		//- Kullanıcı arama özeti talebinde bulunmuş ise
		if (needShowSummary) {
			
			showCallSummary();
		}
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	protected void onCallPermissionsDenied() {
		
		needShowSummary = false;
	}
	
	/**
	 * @inheritDoc Call history is the list of call of the selected contact gets kept in the super class.
	 * 		Needs to keep track of its updates.
	 * 		This method gets called from the super class
	 * 		when the history gets updated or in the first loading.
	 */
	@CallSuper
	@Override
	protected void onHistoryLoad() {
		
		super.onHistoryLoad();
		
		// if no history, do not show anything
		if (history.isEmpty()) {
			
			mainLayout.setVisibility(View.GONE);
			//removeFromDetailView(mainLayout);
			return;
		}
		else {
			
			mainLayout.setVisibility(View.VISIBLE);
		}
		
		
		//- Arama özeti görünümü ya ilk kez yüklenecek
		//- yada yüklenmiş olan görsel güncellenecek.
		if (!historyUpdated && needShowSummary) {
			
			//_ historyUpdated sadece ilk açılışta false gelir.
			
			showCallSummary();
			return;
		}
		
		//- Daha önce açılmışsa bilgilerin güncellenmesi gerek
		if (anyOpenSummary) {
			
			xlog.d("Arama özeti hazırlanıyor");
			
			//- Arama geçmişinin güncellendiğini bildir
			historyUpdated = true;
			//- Bilgileri yenile
			updateSummary();
		}
		//- Daha önce açılmamışsa sadece işaretliyoruz
		else historyUpdated = true;
	}
	
	/**
	 * Called when the summary header gets clicked.
	 * And when the call summary gets opened in the same activity.
	 *
	 * @param view The view
	 */
	private void onClickSummaryHeader(@NonNull View view) {
		
		//! Genel Özet
		//- Bütün olay burada başlıyor
		//- Burası, kullanıcının arama özetlerini görmek için dokunduğu görsel elemanın click olayı
		//- Arama özetinin olduğu asıl görsel bu olaydan sonra hazırlanıp
		//- mainLayout içerisine konularak ekranda yerini alacak
		//! ----------------------------------------------------------------------------------------------
		
		
		//- Hızlı dokunuşları bertaraf et
		if (!gateSummary.enter()) return;
		
		//- Arama özeti gösterildikten sonra öylece kalması düşünülüyordu.
		//- Ancak sonradan, kullanıcı görünüme her dokunduğunda göster-gizle
		//- işlemi yapılmasına karar verildi.
		//- Bu minvalde, kullanıcı görünüme tıkladığında
		//- arama özetinin en az bir kez açılıp açılmadığını bilmemiz gerek.
		//- Eğer açılmışsa gizlenmesi gerek.
		if (anyOpenSummary) {
			
			animateCallSummary();
			return;
			
			//- Bu noktadan sonrasına gitmemeliyiz
			//- Çünkü bu göster-gizle işlemi
		}
		
		
		//- Arama özeti ancak gerekli izinler varsa gösterilebilir
		//- Bu izinler arama kaydı okuma ve yazma izinleridir
		//- Ancak bir üst sınıf izin işlerini işlediği için
		//- burada tekrar bir izin olayına girmek istemiyoruz
		//- ContactDetailsHistory activity'si bizim bir üst sınıfımız (süper sınıfımız) ve
		//- bu üst sınıf bizim için bazı faydalı metotlar sunmakta.
		//- Bunlardan biri requestCallPermissions(). Bu metot izin talebinde bulunmamızı sağlar
		//- İkincisi ise onCallPermissionsGrant. Bu metot, arama kaydı izinleri
		//- kullanıcı tarafından onaylandığında çağrılıyor.
		//- onPermissionsResult(int requestCode, Map<String, Boolean> result) metodu da var,
		//- ancak biz bunu kullanmayacağız çünkü metodun imzasına bakarsan tüm
		//- izinlerin sonuçlarını verdiğini görürsün, ki baktığına da eminim.
		//- Sonuç olarak tüm sonuçları kontrol etmek yerine,
		//- izinler onaylandıysa onCallPermissionsGrant() metodunu kullanmanın
		//- daha basit ve sade olacağı konusunda hemfikir olma ihtimalimizin,
		//- tüm sonuçları alarak içinden ihtiyacımız olan izinleri kontrol ederek
		//- neticeye ulaşmayı tercih etme ihtimalimizden daha yüksek olduğunu düşünmek geliyor içimden.
		//- İzin isteme ve sonucunu alma işimiz bu şekilde olacak.
		
		
		//- Eğer izinler yoksa görünümün açılmasına gerek de yok demeye de gerek yok
		//- Bu sebeple ilk önce izinleri kontrol edelim
		if (hasCallLogPermissions()) {
			
			//- Kişinin arama geçmişi üst sınıfta hazırlanıyor.
			//- Eğer hazır ise bu değişken true olmalı
			if (historyUpdated) {
				
				//- O halde arama özetini göstermek için önümüzde hiçbir engel yok
				showCallSummary();
			}
			else {
				
				//- Arama geçmişi hazır değilse bekliyoruz.
				//- Çünkü üst sınıf kişinin arama geçmişini her açılışta hazırlıyor
				xlog.d("Arama geçmişi bekleniyor");
				
				//- Kullanıcı arama özetini görmek istedi ancak arama geçmişi hazır değil,
				//- bu sebeple geçmiş hazır olduğunda ikinci bir istekte
				//- bulunma zahmetine girmemesi için bir işaret kaydediyoruz
				needShowSummary = true;
			}
		}
		else {
			
			//- Kullanıcı arama özetini görmek istedi ancak erişim yok,
			//- bu sebeple izinler onaylanırsa ikinci bir istekte
			//- bulunma zahmetine girmemesi için bir işaret kaydediyoruz
			needShowSummary = true;
			//- Üst sınıftan erişim talebinde bulunuyoruz.
			//- Sonucu onCallPermissionsGrant() metodu ile alacağız
			requestCallPermissions();
		}
	}
	
	/**
	 * Animates the call summary. Show-hide.
	 */
	private void animateCallSummary() {
		
		if (isOpenedSummary) {
			
			isOpenedSummary = false;
			ViewAnimator.on(callSummaryView).alpha(1, 0).pivotY(0).scaleY(1, 0).translationY(0, -150).duration(200).onStop(() -> callSummaryView.setVisibility(View.GONE)).start();
			
			ViewAnimator.on(mainLayout.findViewById(R.id.expand_indicator)).rotation(180, 0).duration(500).start();
		}
		else {
			
			isOpenedSummary = true;
			anyOpenSummary  = true;
			
			callSummaryView.setVisibility(View.VISIBLE);
			
			ViewAnimator.on(callSummaryView).alpha(0, 1).pivotY(0).scaleY(0, 1).translationY(-150, 0).duration(200).start();
			
			ViewAnimator.on(mainLayout.findViewById(R.id.expand_indicator)).rotation(0, 180).duration(500).start();
		}
	}
	
	/**
	 * Shows the call summary in the first time.
	 * This method gets called only once or called when the history gets updated.
	 */
	private void showCallSummary() {
		
		setupSummaryViews(history);
		animateCallSummary();
		needShowSummary = false;
	}
	
	/**
	 * Sets up the summary views.
	 *
	 * @param callHistory The call history
	 */
	private void setupSummaryViews(@NonNull final History callHistory) {
		
		if (callSummaryView == null) {
			
			//- Arama özetinin görsel elemanları sadece bir kez oluşturulur
			//- Bu değişken null değilse daha önce oluşturulmuş demektir ve
			//- ikinci kez oluşturmaya gerek yoktur.
			//- Şuan null görünüyor ve görünümü oluşturuyoruz
			callSummaryView = inflateSummaryView();
		}
		
		//- Önümüzde iki yol var.
		//- Eğer buraya kullanıcı talebiyle gelinmişse,
		//- arama özeti görseli ekrana eklenir.
		//- Yok eğer bir güncelleme sebebi ile buraya gelinmişse,
		//- arama özeti görünümü halihazırda var olacak ve
		//- görsel elemanların bilgileri güncellenmekle yetinilecek
		
		
		View incomingRow = null;
		View outgoingRow = null;
		View missedRow   = null;
		View rejectedRow = null;
		View totalRow    = null;
		
		
		if (historyUpdated) {
			
			//! --------------------------------------------------------------------------------
			//! Arama özetindeki görsel elemanlar
			//! --------------------------------------------------------------------------------
			TextView incomingCall         = callSummaryView.findViewById(R.id.incoming_call);
			TextView outgoingCall         = callSummaryView.findViewById(R.id.outgoing_call);
			TextView missedCall           = callSummaryView.findViewById(R.id.missed_call);
			TextView rejectedCall         = callSummaryView.findViewById(R.id.rejected_call);
			TextView incomingCallDuration = callSummaryView.findViewById(R.id.incoming_call_duration);
			TextView outgoingCallDuration = callSummaryView.findViewById(R.id.outgoing_call_duration);
			TextView totalCall            = callSummaryView.findViewById(R.id.total_call);
			TextView totalCallDuration    = callSummaryView.findViewById(R.id.total_call_duration);
			
			incomingRow = callSummaryView.findViewById(R.id.incoming_row);
			outgoingRow = callSummaryView.findViewById(R.id.outgoing_row);
			missedRow   = callSummaryView.findViewById(R.id.missed_row);
			rejectedRow = callSummaryView.findViewById(R.id.rejected_row);
			totalRow    = callSummaryView.findViewById(R.id.total_row);
			//! ---------------------------------------------------------------------------------
			
			//- Arama geçmişi güncellenmiş.
			//- Biz de görsel elemanlardaki bilgileri güncelleyelim
			
			int incomingSize = callHistory.size(CallType.INCOMING);
			int outgoingSize = callHistory.size(CallType.OUTGOING);
			int missedSize   = callHistory.size(CallType.MISSED);
			int rejectedSize = callHistory.size(CallType.REJECTED);
			
			incomingCall.setText(String.valueOf(incomingSize));
			outgoingCall.setText(String.valueOf(outgoingSize));
			missedCall.setText(String.valueOf(missedSize));
			rejectedCall.setText(String.valueOf(rejectedSize));
			
			int incomingDuration = callHistory.getDuration(CallType.INCOMING, CallType.INCOMING_WIFI);
			int outgoingDuration = callHistory.getDuration(CallType.OUTGOING, CallType.OUTGOING_WIFI);
			
			incomingCallDuration.setText(Time.formatSeconds(incomingDuration));
			outgoingCallDuration.setText(Time.formatSeconds(outgoingDuration));
			
			totalCall.setText(String.valueOf(incomingSize + outgoingSize + missedSize + rejectedSize));
			totalCallDuration.setText(Time.formatSeconds(incomingDuration + outgoingDuration));
			
			incomingRow.setOnClickListener(v -> showCalls(callHistory.getCallsByTypes(CallType.INCOMING, CallType.INCOMING_WIFI)));
			outgoingRow.setOnClickListener(v -> showCalls(callHistory.getCallsByTypes(CallType.OUTGOING, CallType.OUTGOING_WIFI)));
			missedRow.setOnClickListener(v -> showCalls(callHistory.getCallsByTypes(CallType.MISSED)));
			rejectedRow.setOnClickListener(v -> showCalls(callHistory.getCallsByTypes(CallType.REJECTED)));
			
			totalRow.setOnClickListener(v -> showCalls(history.getCalls()));
		}
		
		if (!summaryViewAdded) {
			
			//- Buradaki işler sadece bir kez yapılacak
			summaryViewAdded = true;
			
			int ripple = Colors.getRipple();
			callSummaryView.findViewById(R.id.header_row).setBackgroundResource(ripple);
			
			if (incomingRow == null) return;
			
			incomingRow.setBackgroundResource(ripple);
			outgoingRow.setBackgroundResource(ripple);
			missedRow.setBackgroundResource(ripple);
			rejectedRow.setBackgroundResource(ripple);
			totalRow.setBackgroundResource(ripple);
			
			mainLayout.addView(callSummaryView);
		}
	}
	
	/**
	 * Inflates the summary view.
	 *
	 * @return The summary view
	 */
	private View inflateSummaryView() {
		
		return getLayoutInflater().inflate(R.layout.call_details_summary, mainLayout, false);
	}
	
	/**
	 * Remakes the summary view with updated history
	 */
	private void updateSummary() {
		
		if (callLogs != null) {
			
			History history = callLogs.getHistoryOf(contact);
			
			setupSummaryViews(history);
			onSummaryUpdate();
		}
		else {
			
			xlog.d("The CallLogs is null");
		}
	}
	
	/**
	 * Called when the summary updated or at the first time loading.
	 */
	private void onSummaryUpdate() {
		
		//- Arama geçmişi güncellenmemiş ise normal göster-gizle işlemi yapılır
		if (!historyUpdated) {
			
			animateCallSummary();
		}
		else {
			
			xlog.d("Arama özeti güncellendi");
			//- Arama geçmişi güncellenmiş.
			//- Bu noktada arama geçmişinin güncellenmesinden dolayı
			//- görsel elemanlar da güncellenmiş duruma geldi.
			//- Yani güncelleme işlemi tamamlandı
			historyUpdated = false;
		}
		
		//- Eğer arama geçmişi güncellenmiş ise
		//- görsel hangi durumdaysa (gizli yada görünür) öyle kalmaya devam edecek.
	}
}
