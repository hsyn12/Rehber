package com.tr.hsyn.telefonrehberi.main.call.activity.random;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.CallSuper;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.cast.GenerationService;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.cast.ServiceReference;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.listener.ProgressListener;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.service.RandomCallService;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;


/**
 * Provides service-related information and actions.
 */
public abstract class RandomCallsActivityService extends RandomCallsActivityView implements ProgressListener<Call> {
	
	/**
	 * Service connection object
	 */
	private final ServiceConnection serviceConnection = new RandomCallServiceConnection();
	/**
	 * Random production service
	 */
	protected     GenerationService randomCallService;
	/**
	 * Generation start time
	 */
	private       long              timeGenerationStart;
	
	/**
	 * Called when the Activity connects to {@link RandomCallService}.
	 */
	@CallSuper
	protected void onServiceConnected() {
		
		xlog.d("Service connected");
		
		//- Olayları dinle
		randomCallService.listenGeneration(this);
		randomCallService.listenServiceWorking(this::onServiceWorkingState);
		randomCallService.onMissionCompleted(this::onMissionCompleted);
	}
	
	/**
	 * Each time the production is completed, the task is completed.
	 * This method is called every time the task is completed.
	 *
	 * @param compilationType Completion status of the task.<br>
	 *                        <code>1</code> - The user stops generation<br>
	 *                        <code>2</code> - The generation stopped by unknown reason<br>
	 *                        <code>3</code> - The generation stopped by an exception<br>
	 */
	@CallSuper
	protected void onMissionCompleted(int compilationType) {
		
		xlog.d("Mission completed");
	}
	
	/**
	 * Servisin üretim durumu her değiştiğinde çağrılır.
	 *
	 * @param isWorking Servis üretimdeyse {@code true}, üretim durmuşsa {@code false}
	 */
	@CallSuper
	protected void onServiceWorkingState(boolean isWorking) {
		
		xlog.d("onServiceWorkingState : %s", isWorking);
		
		if (isWorking) onGenerationStart();
		else onGenerationStop();
	}
	
	/**
	 * Rastgele üretim başladığında çağrılır.
	 */
	@CallSuper
	protected void onGenerationStart() {
		
		timeGenerationStart = System.currentTimeMillis();
		xlog.d("Generation started [%s]", Time.toString(timeGenerationStart, "HH:mm:SSS"));
	}
	
	/**
	 * Rastgele üretim durduğunda çağrılır.
	 */
	@CallSuper
	protected void onGenerationStop() {
		
		xlog.d("Generation stopped [%dms]", System.currentTimeMillis() - timeGenerationStart);
	}
	
	/**
	 * Activity'nin servis bağlantısı sona erdiğinde çağrılır.
	 */
	@CallSuper
	protected void onServiceDisconnected() {
		
		xlog.d("Service disconnected");
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		RandomCallService.listenRunning(this::onServiceRunningStateChange);
	}
	
	/**
	 * Servisin çalışma durumu her değiştiğinde çağrılır.
	 *
	 * @param isRunning Servis çalışıyorsa {@code true}, çalışmıyorsa {@code false}
	 */
	protected void onServiceRunningStateChange(boolean isRunning) {
		
		if (isRunning) onServiceStartRunning();
		else onServiceStopRunning();
	}
	
	/**
	 * {@link RandomCallService} çalışmaya başladığında çağrılır.
	 * Çalışmadan kasıt üretimin başlaması değildir,
	 * servisin hafızaya yüklenip hazır hale gelmesidir.
	 * Bu çağrıdan önce servise yönelik tüm çağrılar geçersizdir.
	 * Bu çağrıdan sonra servis bağlantısı emri verilir, ayrıca bağlanma isteği
	 * vermeye gerek yoktur.
	 */
	@CallSuper
	protected void onServiceStartRunning() {
		
		xlog.d("Service start running");
		
		bindRandomCallsService();
	}
	
	/**
	 * Servise bağlanır
	 */
	protected final void bindRandomCallsService() {
		
		bindService(new Intent(this, RandomCallService.class), serviceConnection, 0);
	}
	
	/**
	 * {@link RandomCallService} tamamen durduğunda çağrılır.
	 */
	@CallSuper
	protected void onServiceStopRunning() {
		
		xlog.d("Service stop running");
		if (randomCallService != null) randomCallService = null;
	}
	
	/**
	 * Rastgele arama kaydı üretimi yapan servisi başlatır.<br><br>
	 * Servis çalışmaya başladığında {@link #onServiceStartRunning()}
	 * metodu çağrılır.<br>
	 * Activity servise bağlandığında {@link #onServiceConnected()}
	 * metodu çağrılır.<br>
	 * Servis üretime başladığında {@link #onGenerationStart()}
	 * metodu çağrılır.<br><br>
	 * Bu metotlarla servisin gidişatı kontrol edilir.
	 */
	protected final void startRandomCallService() {
		
		if (!isServiceRunning()) {
			
			Intent intent = new Intent(this, RandomCallService.class);
			startService(intent);
		}
	}
	
	/**
	 * @return Servis çalışıyorsa {@code true}, çalışmıyorsa {@code false}
	 */
	protected final boolean isServiceRunning() {
		
		return RandomCallService.isRunning();
	}
	
	/**
	 * Servisi durdur.
	 * Eğer servis üretim yapmıyorsa zaten çalışmıyordur.
	 * Eğer üretim yoksa ve çalışıyorsa,
	 * tamamlanan üretimin kaydını yapmaktadır ve kısa süre sonra sonlanacaktır.
	 * Bu sebeple durdurma işlemi yanlızca aktif üretim yapılmakta ise icraa edilir.
	 */
	protected final void stopRandomCallService() {
		
		if (isServiceWorking()) {
			
			randomCallService.stopGeneration();
		}

		/*var intent = new Intent(this, RandomCallService.class);

		stopService(intent);*/
	}
	
	/**
	 * @return Rastgele üretim yapılmakta ise {@code true}, değilse {@code false}
	 */
	protected final boolean isServiceWorking() {
		
		return isServiceConnected() && randomCallService.isInGeneration();
	}
	
	/**
	 * @return Servis bağlı ise {@code true}, değilse {@code false}
	 */
	protected final boolean isServiceConnected() {
		
		return randomCallService != null;
	}
	
	@Override
	protected void onDestroy() {
		
		RandomCallService.listenRunning(null);
		super.onDestroy();
	}
	
	/**
	 * Servis bağlantı sınıfı
	 */
	protected final class RandomCallServiceConnection implements ServiceConnection {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			randomCallService = ((ServiceReference) service).getService();
			RandomCallsActivityService.this.onServiceConnected();
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			randomCallService = null;
			RandomCallsActivityService.this.onServiceDisconnected();
		}
	}
	
	
}
