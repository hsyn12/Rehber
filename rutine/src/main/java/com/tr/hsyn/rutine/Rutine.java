package com.tr.hsyn.rutine;


import com.tr.hsyn.xlog.xlog;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Belirtilen sürede döngüsünü tamamlayan bir yapı.
 * Döngü sonsuzdur, durdurulana kadar her döngüde dinleyicisini uyarır.
 *
 * @author hsyn 13 Mart 2022 Pazar 11:59:24
 */
public class Rutine {
	
	/**
	 * Cycle Counter
	 */
	private final        AtomicInteger  counter          = new AtomicInteger(0);
	/**
	 * Start-Stop button
	 */
	private final        AtomicBoolean  run              = new AtomicBoolean(false);
	private final        AtomicBoolean  pause            = new AtomicBoolean(false);
	/**
	 * Rutine time
	 */
	private final        AtomicLong     time             = new AtomicLong();
	private final        AtomicLong     delay            = new AtomicLong(0);
	/**
	 * Rutine interval
	 */
	private final        long           interval;
	private              long           rutineStartTime;
	private              RutineListener rutineListener;
	private static final long           DEFAULT_INTERVAL = 5000L;
	private              Thread         threadRutine;
	private final        Object         CYCLE            = new Object();
	private final        AtomicBoolean  reallyPause      = new AtomicBoolean(false);
	
	/**
	 * Yeni bir Rutine oluştur.
	 * Rutine aralığı varsayılan 5 saniyedir.
	 */
	public Rutine() {
		
		this(DEFAULT_INTERVAL, false);
	}
	
	/**
	 * Yeni bir Rutine oluştur.
	 * Rutine aralığı varsayılan 5 saniyedir.
	 *
	 * @param start {@code true} ise döngü çalışmaya başlar
	 */
	public Rutine(boolean start) {
		
		this(DEFAULT_INTERVAL, start);
	}
	
	/**
	 * Yeni bir Rutine oluştur.
	 *
	 * @param interval Rutine süresi. Bu süre her aşıldığında bir döngü tamamlanmış olur.
	 */
	public Rutine(long interval) {
		
		this(interval, false);
	}
	
	/**
	 * Yeni bir Rutine oluştur.
	 *
	 * @param interval Rutine süresi. Bu süre her aşıldığında bir döngü tamamlanmış olur.
	 * @param start    {@code true} ise döngü çalışmaya başlar
	 */
	public Rutine(long interval, boolean start) {
		
		delay.set(this.interval = interval);
		
		if (start) startRutine();
	}
	
	/**
	 * Döngüyü ertele.
	 * Her erteleme, belirlenen rutin süresinin başa dönmesini sağlar.
	 * Mesela 5 saniye olan bir rutin süresi için 3. saniyede erteleme yapılırsa,
	 * sonraki döngü, ertelemenin yapıldığı andan 5 saniye sonrasına ayarlanır.
	 * Bu şekilde belirnen rutin süresi daima sabit kalır.
	 * Daima belirlenen süre sonunda döngü gerçekleşir.
	 */
	public void delay() {
		
		if (run.get()) {
			
			long now = System.currentTimeMillis();
			
			//! Ertele
			time.set(now);
		}
	}
	
	/**
	 * Rutin döngüyü askıya alır.
	 * {@link #resume()} metodu çağrılana kadar askıda kalır.
	 * {@link #resume()} metodu çağrıldığında döngü başa döner ve
	 * belirlenen aralıkta dönmeye yeniden başlar.
	 * Yani döngü askıya alınmadan önce {@link #delay} metodu ile
	 * erteleme yapılmış ise bu erteleme geçersiz kalır ve
	 * ilk kez başlıyormuş gibi davranır.
	 * Ancak döngünün ilk başladığı zaman hala geçerlidir ve
	 * {@link #getRutineStartTime()} metodu ile alınabilir.
	 */
	public void pause() {
		
		pause.compareAndSet(false, true);
	}
	
	/**
	 * Eğer döngü {@link #pause()} metodu ile askıya alınmışsa
	 * döngüyü yeniden uyandırır. Döngü askıya alınmamışsa bu çağrının hiçbir etkisi yoktur.
	 */
	public void resume() {
		
		if (reallyPause.compareAndSet(true, false)) {
			
			pause.set(false);
			
			synchronized (CYCLE) {
				
				CYCLE.notify();
			}
		}
	}
	
	/**
	 * Sonsuz bir döngüde belirlenen aralıklarla kontrol işlemini başlatır.
	 * Rutin daha önce başlatılmışsa bu çağrının hiç bir etkisi olmaz.
	 */
	public void startRutine() {
		
		if (run.get()) {
			
			System.out.println("Rutine already running");
			return;
		}
		
		xlog.w("Rutine is activated");
		run.set(true);
		time.set(rutineStartTime = System.currentTimeMillis());
		
		threadRutine = new Thread(() -> {
			
			try {
				
				//noinspection ConstantConditions
				if (false) throw new InterruptedException();
				
				//! Rutine Cycle
				while (run.get()) {
					
					//- Enter cycle
					
					//- First wait
					sleep(delay.get());
					
					if (pause.get()) {
						
						delay.set(interval);
						reallyPause.set(true);
						
						synchronized (CYCLE) {
							
							CYCLE.wait();
						}
						
						continue;
					}
					
					//- And Go
					
					//! Rutine time
					long wakeupTime = time.get() + interval;
					long now        = System.currentTimeMillis();
					//! Left duration for Rutine Time
					long timeLeft = Math.abs(wakeupTime - now);
					
					if (wakeupTime <= now || timeLeft <= 500L) {
						
						//! Belirlenen zamana ulaşıldı
						int count = counter.incrementAndGet();
						time.set(now);
						delay.set(interval);
						
						if (rutineListener != null) rutineListener.onRutine(count);
					}
					else {
						
						delay.set(timeLeft);
					}
				}//- while loop end here
			}
			catch (InterruptedException ignore) {}
			
			run.set(false);
			
			xlog.w("Rutine is inactivated");
			
		});
		
		threadRutine.setDaemon(true);
		threadRutine.start();
	}
	
	public void startRutine(RutineListener listener) {
		
		this.rutineListener = listener;
		startRutine();
	}
	
	/**
	 * Döngüyü sonlandır.
	 * Döngü, {@link #pause()} metodu ile askıya alınmış olsa bile
	 * döngüyü tamamen sonlandırır.
	 */
	public void stopRutine() {
		
		if (pause.compareAndSet(true, false)) {
			
			synchronized (CYCLE) {
				
				CYCLE.notify();
			}
		}
		
		if (run.compareAndSet(true, false)) {
			
			threadRutine.interrupt();
		}
	}
	
	private void sleep(long delay) {
		
		if (delay <= 0) return;
		
		if (run.get()) {
			
			try {
				Thread.sleep(delay);
			}
			catch (InterruptedException ignore) {}
		}
	}
	
	/**
	 * @return Döngü süresi
	 */
	public final long getInterval() {
		
		return interval;
	}
	
	public final RutineListener getRutineListener() {
		
		return rutineListener;
	}
	
	public void setRutineListener(RutineListener rutineListener) {
		
		this.rutineListener = rutineListener;
	}
	
	/**
	 * @return Rutin işlemin ilk başladığı zaman
	 */
	public final long getRutineStartTime() {
		
		return rutineStartTime;
	}
	
	/**
	 * @return Rutin işlemin her döngüde güncellenen zamanı
	 */
	public final long getRutineTime() {
		
		return time.get();
	}
	
	/**
	 * @return Rutin işlem çalışıyor ise {@code true}
	 */
	public final boolean isRunning() {
		
		return run.get();
	}
	
	/**
	 * Rutine Listener
	 */
	public interface RutineListener {
		
		/**
		 * Belirlenen süre aşıldığında çağrılır. (arka planda)
		 *
		 * @param cycleCount Döngü sayısı
		 */
		void onRutine(int cycleCount);
	}
}