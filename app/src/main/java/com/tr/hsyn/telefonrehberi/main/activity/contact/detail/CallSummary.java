package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.CallHistory;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Kişi için arama özeti görünümü.
 * Arama özeti, kişiye ait aramalara
 * en genel haliyle bir bakış atmayı sağlar.
 * <p>
 * Türlerine göre kişinin arama kyıtları sayısını ve toplam konuşma süresini gösterir.
 */
public abstract class CallSummary extends ContactDetailsHistory {
	
	/*
	 * Olay, kullanıcının ilgili görünüm elemanına dokunmasıyla başlar.
	 * Yani kullanıcı kişiye ait arama özetini görmek ister ve ilgili elemana tıklar.
	 * Burasının 'kişi detayları' activity'si olduğunu hatırla.
	 * Yani bu arama özeti tek bir kişiye ait tüm arama kayıtlarından oluşturmaktadır.
	 * Gelen giden cevapsız ve reddedilen aramaların toplam sayısını ve
	 * konuşma olan (gelen ve giden) aramaların toplam konuşma sürelerini ve
	 * bu ikisinin de konuşma sürelerinin toplamı gösterilecek.
	 * Arama özeti denen şey bu.
	 * Ancak kişiye ait bir arama kaydı yoksa bu görsel ekranda olmayacak.
	 * */
	
	
	/**
	 * Özet görünümüne dokunma sıklığı sınırı koyuyoruz.
	 * Kullanıcı 1 saniyede sadece bir kez dokunabilecek.
	 */
	private final Gate      gateSummary = AutoGate.newGate(1000L);
	/**
	 * Arama özeti ile ilgili tüm görsel elemanlar bu elemanın içinde yer alacak.
	 */
	private       ViewGroup mainLayout;
	/**
	 * Arama özeti, kişinin arama geçmişi kullanılarak oluşturulduğu için
	 * arama geçmişi güncellendiğinde bu değişken {@code true} değerini alacak
	 */
	private       boolean   historyUpdated;
	/**
	 * Arama özeti talebi izinler yokken gerçekleşirse,
	 * izinler onaylandığında kullanıcının ikinci bir talepte bulunmasına gerek kalmasın diye
	 * bu değişken {@code true} ise direk gösterilir.
	 */
	private       boolean   needShowSummary;
	/**
	 * Arama özeti görseli ekrana sadece bir kez yüklenecek,
	 * diğer güncellemelerde ise varolan görseller yeni bilgilerle güncellenecek.
	 * Bu değişken {@code true} ise görsel eklenmiş demektir ve tekrar ekleme yapılmaması gerektiğini bildirir.
	 */
	private       boolean   summaryViewAdded;
	/**
	 * Arama özeti görseli
	 */
	private       View      callSummaryView;
	/**
	 * Arama özeti görseli gösterimdeyse {@code true}
	 */
	private       boolean   isOpenedSummary;
	/**
	 * Arama özeti görseli en az bir kez gösterilmiş ise {@code true}
	 */
	private       boolean   anyOpenSummary;
	
	@Override
	protected void prepare() {
		
		super.prepare();
		
		
		//- Activity'nin bu bölümü arama özetlerini gösterecek
		//- Ana görselleri hazırlayalım
		
		//- Arama özeti görseli bu elemanın içinde olacak
		mainLayout = findView(R.id.call_summary_body);
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers == null || numbers.isEmpty()) {
			
			removeDetailView(mainLayout);
			return;
		}
		
		
		//- Bu, arama özeti görünümünün başlığıdır ve mainLayout içinde halihazırda bulanmaktadır
		View summaryHeader = mainLayout.findViewById(R.id.call_summary_header);
		summaryHeader.setBackgroundResource(Colors.getRipple());
		
		//- Bu başlığa tıklanana kadar arama özeti ile ilgili hiçbir işlem yapılmayacak
		summaryHeader.setOnClickListener(this::onClickSummaryHeader);
		
		ImageView icon = findView(summaryHeader, R.id.call_summary_icon);
		
		Colors.setTintDrawable(icon.getDrawable(), Colors.lighter(Colors.getPrimaryColor(), 0.2f));
		
		//- Görüldüğü üzere giriş çok sade
		//- Esas olay kullanıcının talebi ile başlar
	}
	
	@Override
	protected void onHistoryUpdate() {
		
		super.onHistoryUpdate();
		
		
		if (history == null || history.isEmpty()) {
			
			removeDetailView(mainLayout);
			return;
		}
		
		
		//- Arama özeti görünümü ya ilk kez yüklenecek
		//- yada yüklenmiş olan görsel güncellenecek.
		if (!historyUpdated && needShowSummary) {
			
			//- historyUpdated sadece ilk açılışta false gelir.
			
			showCallSummary();
			return;
		}
		
		//- Daha önce açılmışsa bilgilerin güncellenmesi gerek
		if (anyOpenSummary) {
			
			xlog.d("Arama özeti hazırlanıyor");
			
			//- Arama geçmişinin güncellendiğini bildirelim
			historyUpdated = true;
			//- Bilgileri yenile
			updateSummary();
		}
		//- Daha önce açılmamışsa sadece işaretliyoruz
		else historyUpdated = true;
	}
	
	/**
	 * Üst sınıf, arama izinlerinin kullanıcı tarafından onaylandığını
	 * bu metot ile alt sınıflara bildirir.
	 * Bu metot çağrıldığında tüm arama kaydı izinleri sağlanmış demektir.
	 */
	@Override
	protected void onCallPermissionsGrant() {
		
		super.onCallPermissionsGrant();
		
		//- Kullanıcı arama özeti talebinde bulunmuş ise
		if (needShowSummary) {
			
			showCallSummary();
		}
	}
	
	@Override
	protected void onCallPermissionsDenied() {
		
		needShowSummary = false;
	}
	
	/**
	 * Kişinin arama özetini gösterme talebidir.
	 * Kullanıcının ilgili görünüme dokunması sonucu çağrılır.
	 *
	 * @param view Arama özetinin gösterilmesi için kullanıcının dokunduğu görsel eleman
	 */
	private void onClickSummaryHeader(@NonNull View view) {
		
		//! Genel Özet
		//- Bütün olay burada başlıyor
		//- Burası, kullanıcının arama özetlerini görmek için dokunduğu görsel elemanın click olayı
		//- Arama özetinin olduğu asıl görsel bu olaydan sonra hazırlanıp
		//- mainLayout içerisine konularak ekranda yerini alacak
		//! ----------------------------------------------------------------------------------------------
		
		
		//- Hızlı dokunuşları bertaraf edelim
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
		//- izinlerin sonuçlarını verdiğini görürürsün, ki baktığına da eminim.
		//- Sonuç olarak tüm sonuçları kontrol etmek yerine,
		//- izinler onaylandıysa onCallPermissionsGrant() metodunu kullanmanın
		//- daha basit ve sade olacağı kosunda hemfikir olma ihtimalimizin,
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
	 * Arama özetini göster
	 */
	private void showCallSummary() {
		
		if (callSummaryView == null) {
			
			//- Arama özetinin görsel elemanları sadece bir kez oluşturulur
			//- Bu değişken null değilse daha önce oluşturulmuş demektir ve
			//- ikinci kez oluşturmaya gerek yoktur.
			//- Şuan null görünüyor ve görünümü oluşturuyoruz
			callSummaryView = createSummaryView();
		}
		
		//- Arama özetini R.layout.call_details_summary.xml
		//- layout dosyası temsil ediyor.
		//- Bu layout birçok görsel eleman bulunduruyor.
		//- Bu görsel elemanların hazırlanması ve gerekli bilgilerle doldurması için
		//- önce arka planda gerekli bilgileri hazırlayan bir metoda,
		//- sonra da bilgileri görselere aktaran bir başka metoda yönlendiriyoruz
		Work.on(() -> createSummaryInfo(history))
				.onSuccess(this::setupSummaryViews)
				.onError(Throwable::printStackTrace)
				.onLast(this::animateCallSummary)
				.execute();
		
		//- Arama geçmişi yeniden güncellenirse haberimiz olsun
		
		needShowSummary = false;
	}
	
	/**
	 * Bu metot, arama özeti görselindeki elemanları
	 * yeni bilgilerle günceller.
	 * Ve işlem tamamlandığında {@link #onSummaryUpdated()} metodunu çağırır.
	 */
	private void updateSummary() {
		
		Work.on(() -> createSummaryInfo(history))
				.onSuccess(this::setupSummaryViews)
				.onError(Throwable::printStackTrace)
				.onLast(this::onSummaryUpdated)
				.execute();
	}
	
	/**
	 * Arama özeti görsel elemanları yeni bilgilerle güncellendiğinde çağrılır.
	 */
	private void onSummaryUpdated() {
		
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
	
	private void animateCallSummary() {
		
		if (isOpenedSummary) {
			
			isOpenedSummary = false;
			ViewAnimator.on(callSummaryView)
					.alpha(1, 0)
					.pivotY(0)
					.scaleY(1, 0)
					.translationY(0, -150)
					.duration(200)
					.onStop(() -> callSummaryView.setVisibility(View.GONE))
					.start();
			
			ViewAnimator.on(mainLayout.findViewById(R.id.expand_indicator))
					.rotation(180, 0)
					.duration(500)
					.start();
		}
		else {
			
			isOpenedSummary = true;
			anyOpenSummary  = true;
			
			callSummaryView.setVisibility(View.VISIBLE);
			
			ViewAnimator.on(callSummaryView)
					.alpha(0, 1)
					.pivotY(0)
					.scaleY(0, 1)
					.translationY(-150, 0)
					.duration(200)
					.start();
			
			ViewAnimator.on(mainLayout.findViewById(R.id.expand_indicator))
					.rotation(0, 180)
					.duration(500)
					.start();
		}
	}
	
	/**
	 * Verilen arama geçmişini kullanarak kişi için arama özeti bilgisi oluşturur.
	 *
	 * @param history Kişinin arama geçmişi
	 * @return Arama özeti bilgileri
	 */
	@NonNull
	private CallHistory createSummaryInfo(@NonNull List<Call> history) {
		
		//- Arama özeti, kişinin arama kayıtlarını türlerine göre ayırır.
		//- Ve bu türlerle ilgili bilgiler sunar
		
		var calls = history.stream().collect(Collectors.groupingBy(Call::getType));
		
		return new CallHistory(contact, calls);
	}
	
	/**
	 * Arama özeti görselini oluşturur.
	 *
	 * @return Arama özeti görseli
	 */
	private View createSummaryView() {
		
		return getLayoutInflater().inflate(R.layout.call_details_summary, mainLayout, false);
	}
	
	/**
	 * Arama özeti görsel elemanlarını verilen bilgilerle günceller.
	 *
	 * @param callHistory Arama özeti bilgisi
	 */
	private void setupSummaryViews(@NonNull final CallHistory callHistory) {
		
		//- Eğer buraya kullanıcı talebiyle gelinmişse,
		//- arama özeti görseli ekrana eklenir.
		//- Yok eğer bir güncelleme sebebi ile buraya gelinmişse,
		//- arama özeti görünümü halihazırda varolacak ve
		//- sadece görsel elemanların bilgileri güncellenecek
		
		
		View incommingRow = null;
		View outgoingRow  = null;
		View missedRow    = null;
		View rejectedRow  = null;
		View totalRow     = null;
		
		
		if (historyUpdated) {
			
			//! --------------------------------------------------------------------------------
			//! Arama özetindeki görsel elemanlar
			//! --------------------------------------------------------------------------------
			TextView incommingCall         = callSummaryView.findViewById(R.id.incomming_call);
			TextView outgoingCall          = callSummaryView.findViewById(R.id.outgoing_call);
			TextView missedCall            = callSummaryView.findViewById(R.id.missed_call);
			TextView rejectedCall          = callSummaryView.findViewById(R.id.rejected_call);
			TextView incommingCallDuration = callSummaryView.findViewById(R.id.incomming_call_duration);
			TextView outgoingCallDuration  = callSummaryView.findViewById(R.id.outgoing_call_duration);
			TextView totalCall             = callSummaryView.findViewById(R.id.total_call);
			TextView totalCallDuration     = callSummaryView.findViewById(R.id.total_call_duration);
			
			incommingRow = callSummaryView.findViewById(R.id.incomming_row);
			outgoingRow  = callSummaryView.findViewById(R.id.outgoing_row);
			missedRow    = callSummaryView.findViewById(R.id.missed_row);
			rejectedRow  = callSummaryView.findViewById(R.id.rejected_row);
			totalRow     = callSummaryView.findViewById(R.id.total_row);
			//! ---------------------------------------------------------------------------------
			
			//- Arama geçmişi güncellenmiş.
			//- Biz de görsel elemanlardaki bilgileri güncelleyelim
			
			int incommingSize = callHistory.getIncomingCallSize();
			int outgoingSize  = callHistory.getOutgoingCallSize();
			int missedSize    = callHistory.getMisedCallSize();
			int rejectedSize  = callHistory.getRejectedCallSize();
			
			incommingCall.setText(String.valueOf(incommingSize));
			outgoingCall.setText(String.valueOf(outgoingSize));
			missedCall.setText(String.valueOf(missedSize));
			rejectedCall.setText(String.valueOf(rejectedSize));
			
			incommingCallDuration.setText(Time.formatSeconds(callHistory.getIncomingDuration()));
			outgoingCallDuration.setText(Time.formatSeconds(callHistory.getOutgoingDuration()));
			
			totalCall.setText(String.valueOf(incommingSize + outgoingSize + missedSize + rejectedSize));
			totalCallDuration.setText(Time.formatSeconds(callHistory.getIncomingDuration() + callHistory.getOutgoingDuration()));
			
			incommingRow.setOnClickListener(v -> showHistory(callHistory.getIncommingCalls()));
			outgoingRow.setOnClickListener(v -> showHistory(callHistory.getOutgoingCalls()));
			missedRow.setOnClickListener(v -> showHistory(callHistory.getMissedCalls()));
			rejectedRow.setOnClickListener(v -> showHistory(callHistory.getRejectedCalls()));
			totalRow.setOnClickListener(v -> showHistory(history));
		}
		
		
		if (!summaryViewAdded) {
			
			//- Buradaki işler sadece bir kez yapılacak
			summaryViewAdded = true;
			
			var ripple = Colors.getRipple();
			callSummaryView.findViewById(R.id.header_row).setBackgroundResource(ripple);
			
			if (incommingRow == null) return;
			
			incommingRow.setBackgroundResource(ripple);
			outgoingRow.setBackgroundResource(ripple);
			missedRow.setBackgroundResource(ripple);
			rejectedRow.setBackgroundResource(ripple);
			totalRow.setBackgroundResource(ripple);
			
			mainLayout.addView(callSummaryView);
		}
	}
}
