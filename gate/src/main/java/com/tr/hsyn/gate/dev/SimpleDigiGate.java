package com.tr.hsyn.gate.dev;


import com.tr.hsyn.gate.DelayedRunner;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.GateObserver;
import com.tr.hsyn.gate.ObserableGate;
import com.tr.hsyn.watch.observable.Observable;
import com.tr.hsyn.watch.observable.Observe;

import org.jetbrains.annotations.NotNull;


public class SimpleDigiGate extends SimpleAutoGate implements DigiGate, ObserableGate {
	
	/**
	 * Kapının açık-kapalı durumu
	 */
	protected final Observable<Boolean> gateState = new Observe<>(true);
	/**
	 * Her içeri girme talebi otomatik çıkış işleminin süresini sıfırlayacaksa {@code true}.
	 * Bu değişken {@code true} ise içeri giriş talepleri çıkış işlemini içeri
	 * girilse de girilmese de sıfırlar.
	 */
	protected       boolean             loopDelay;
	/**
	 * Gecikmeli iş çalıştırıcısı
	 */
	protected       DelayedRunner       delayedRunner;
	/**
	 * Kapının durumunu izleyen nesne
	 */
	private         GateObserver        gateObserver;
	/**
	 * Çıkış yapıldığında çalıştırılacak işlem
	 */
	private         Runnable            onExit;
	
	/**
	 * Otomatik çıkış süresi {@code 0L} olarak kaydedilir.
	 */
	public SimpleDigiGate() {
		
		this(0L, null);
	}
	
	/**
	 * Otomatik çıkış süresi {@code 0L} olarak kaydedilir.
	 *
	 * @param onExit Çıkışta çalıştırılması istenen iş
	 */
	public SimpleDigiGate(Runnable onExit) {
		
		this(0L, onExit);
	}
	
	/**
	 * Sınıf bu kurucu ile oluşturulduğunda {@link #enter()} ve {@link #enter(Runnable)} metotları
	 * buradaki verilen süreyi otomatik çıkış süresi olarak kullanır.
	 *
	 * @param passInterval Giriş yapıldıktan sonra otomatik olarak çıkış yapılması için geçmesi gereken süre
	 */
	public SimpleDigiGate(long passInterval) {
		
		this(passInterval, null);
	}
	
	/**
	 * Çıkış süresini ve çıkış işlemini belirler.
	 *
	 * @param passInterval Çıkış süresi
	 * @param onExit       Çıkış işlemi
	 */
	public SimpleDigiGate(long passInterval, Runnable onExit) {
		
		super(passInterval);
		gateState.setObserver(this::onUpdate);
		this.onExit = onExit;
	}
	
	@Override
	protected void open() {
		
		super.open();
		
		if (delayedRunner != null)
			delayedRunner.setDone(false);
		
		gateState.set(true);
	}
	
	@Override
	protected void close() {
		
		super.close();
		gateState.set(false);
	}
	
	protected void onUpdate(Boolean isOpen) {
		
		if (gateObserver != null) gateObserver.onGateStateChange(isOpen);
		
		if (isOpen && onExit != null) onExit.run();
	}
	
	@Override
	public void setGateObserver(GateObserver gateObserver) {
		
		this.gateObserver = gateObserver;
	}
	
	@Override
	public void setOnExit(Runnable onExit) {
		
		this.onExit = onExit;
	}
	
	/**
	 * @return Kapı açıksa içeri girilir ve (kapı kapatılarak) {@code true} döner, aksi halde {@code false} döner ve
	 * 		bu çağrı ile ilgili hiç bir işlem yapılmaz
	 */
	@Override
	public boolean enter() {
		
		if (super.enter()) {
			
			//Kapı izleniyor
			gateState.set(false);//Girdiğinde kapı kapanıyor
			return true;
		}
		
		return false;
	}
	
	/**
	 * Çağrı yapıldığında kapı açıksa kapıyı kapatır ve {@code true} döner.
	 * Dikkat edilmesi gereken şey: bu kapının {@link SimpleAutoGate} olduğudur.
	 * Bu sebeple çıkış daima otomatik yapılır. {@link #SimpleDigiGate(long)} kurucu metodunun
	 * kullanıldığına emin olunmalıdır. Bu kurucu metot ile verilen süre sonunda
	 * çıkış yapılır ve kapı açıldıktan sonra verilen çıkış işlemi (varsa) çalıştırılır.
	 * Eğer {@link #SimpleDigiGate()} kurucu metodu kullanılmış ise çıkış işleminde hiçbir gecikme uygulanmaz ve
	 * giriş yapıldığı gibi derhal çıkışa gidilir ve kapı açılarak <strong>çıkış işlemi</strong> gerçekleştirilir.
	 * Bu kurucu kullanıldıysa {@link #enter(long)} veya {@link #enter(long, Runnable)} metodunun kullanılması
	 * daha doğru olur.
	 * <br><br>
	 * <p>
	 * Eğer {@link #loopDelay} {@code true} ise, arka arkaya yapılan çağrılarda içeri girilse de
	 * girilmese de otomatik çıkış için belirtilen süre sıfırlanır ve baştan saymaya başlar.
	 * Yani süre tamamlanmadan yapılan her çağrı süreyi başa alır.
	 *
	 * @param onExit Çıkış işlemi
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner.Yani içeri girilirse {@code true} döner, girilemezse {@code false} döner ve hiçbir işlem yapılmaz
	 */
	@Override
	public boolean enter(@NotNull Runnable onExit) {
		
		return enter(getInterval(), onExit);
	}
	
	/**
	 * Çağrı yapıldığında kapı açıksa kapıyı kapatır ve {@code true} döner.
	 * Dikkat edilmesi gereken şey: bu kapının {@link SimpleAutoGate} olduğudur.
	 * Bu sebeple çıkış daima otomatik yapılır. {@link #SimpleDigiGate(long)} kurucu metodunun
	 * kullanıldığına emin olunmalıdır. Bu kurucu metot ile verilen süre sonunda
	 * çıkış yapılır ve kapı açıldıktan sonra verilen çıkış işlemi çalıştırılır.
	 * Eğer {@link #SimpleDigiGate()} kurucu metodu kullanılmış ise çıkış işleminde hiçbir gecikme uygulanmaz ve
	 * giriş yapıldığı gibi derhal çıkışa gidilir ve kapı açılarak <strong>çıkış işlemi</strong> gerçekleştirilir.
	 * Bu kurucu kullanıldıysa {@link #enter(long)} veya bu metodunun kullanılması
	 * daha doğru olur.
	 * <br><br>
	 * <p>
	 * Eğer {@link #loopDelay} {@code true} ise, arka arkaya yapılan çağrılarda içeri girilse de
	 * girilemese de otomatik çıkış için belirtilen süre sıfırlanır ve baştan saymaya başlar.
	 * Yani süre tamamlanmadan yapılan her çağrı süreyi başa alır.
	 *
	 * @param outAfter Bu süre sonunda çıkış yapılır
	 * @param onExit   Çıkışta çalıştırılması gereken iş
	 * @return Çağrı yapıldığında kapı açıksa kapanır ve {@code true} döner.Yani içeri girilirse {@code true} döner, girilemezse {@code false} döner ve hiçbir işlem yapılmaz
	 */
	private boolean enter(long outAfter, @NotNull Runnable onExit) {
		
		if (loopDelay) {
			
			if (isOpen()) {
				
				close();
				this.onExit = onExit;
				delayedRunner.setAction(this::open).run(outAfter);
				return true;
			}
			else {
				
				delayedRunner.cancel();
				delayedRunner.run(outAfter);
			}
		}
		else {
			
			if (enter()) {
				
				this.onExit = onExit;
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void exit() {
		
		super.exit();
		gateState.set(true);//Çıktığında kapı açılıyor
	}
	
	/**
	 * Çıkış işlemi gerçekleştirilmeyecek olan giriş-çıkışlarda bu metodun hiçbir etkisi yoktur.
	 * Ancak bir çıkış işlemi verildiyse ({@link #enter(Runnable)}) çalışma şekli değişir.<br>
	 * Normalde giriş gerçekleştiğinde, {@link #getInterval()} süresi sonunda
	 * çıkış otomatik yapılır ve verilen çıkış işlemi varsa icra edilir.
	 * Ve bu tek seferlik yapılan bir işlemdir. Gir --> Süreyi Bekle --> Çık --> Çıkış işlemini yap.<br>
	 * Ancak bu metot çağrıldıktan sonra bu iş tek seferlik olmaktan çıkar ve sürekli tekrar eder.
	 * Tekrar işlemini kapıyı açıp kapatarak da gerçekleştirebilirsin ancak
	 * süreyi düzenleyemezsin. Bu çağrıdan sonra her giriş çağrısı ({@link #enter(Runnable)})
	 * içeri girilse de girilmese de işleyen otomatik çıkış süresini sıfırlar ve
	 * baştan saymasını sağlar. Böylece çıkış da geçiktirilmiş olur.
	 * Süre tamamlanmadan önce {@link #enter(Runnable)} veya {@link #enter(long, Runnable)} metoduna yapılan her çağrı çıkışı geciktirir.
	 * <br>
	 * Bu işin yapılmasının sebebi, aynı işlem için arka arkaya yapılan çağrıları kontrol etmek.
	 * Mesela bir arama ekranında bir harfe bastığın anda arama işleminin hemen gerçekleşmesi
	 * yerine, aranacak kelime tamamen yazıldıktan sonra işlemin gerçekleşmesi daha uygundur.
	 * Harf girişi için uygun bir süre belirlenir ve bu süre boyunca herhangi bir harf girişi olmazsa
	 * aranan kelimenin yazılmış olduğu varsayılır ve arama işlemi yapılır.
	 * Ancak belirlenen süre dolmadan önce harf girişi yapılırsa süre sıfırlanır ve
	 * yeni bir giriş beklenir süre tekrar dolana kadar.
	 * <br>
	 * Buradaki amaç da tamamen budur. Bu kapı nesnesini yoğun bir girişin olduğu yere koyarsan
	 * yapılan tüm girişler kontrol altına alınmış olur.
	 *
	 * @return DigiGate
	 */
	@Override
	public DigiGate loop() {
		
		delayedRunner = new DelayedRunner();
		loopDelay     = true;
		return this;
	}
	
}
