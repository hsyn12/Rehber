package com.tr.hsyn.telefonrehberi.main.activity.call.random.service;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.Notifications;
import com.tr.hsyn.telefonrehberi.main.activity.call.random.RandomCallsActivity;
import com.tr.hsyn.telefonrehberi.main.activity.call.random.cast.GenerationArgs;
import com.tr.hsyn.telefonrehberi.main.activity.call.random.cast.GenerationService;
import com.tr.hsyn.telefonrehberi.main.activity.call.random.cast.ServiceReference;
import com.tr.hsyn.telefonrehberi.main.activity.call.random.listener.ProgressListener;
import com.tr.hsyn.telefonrehberi.main.code.call.act.CallGenerator;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.cast.GenerationExeption;
import com.tr.hsyn.telefonrehberi.main.code.cast.Generator;
import com.tr.hsyn.watch.observable.Observable;
import com.tr.hsyn.watch.observable.Observe;
import com.tr.hsyn.watch.observable.Observer;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


/**
 * Rastgele arama kaydı üretir ve sisteme ekler.
 * Bu servis Rehber ve arama kayıtları ile ilgili
 * izinlerin sağlandığını varsayar ve hiç bir izin kontrolü yapmaz.
 */
public class RandomCallService extends Service implements GenerationService {
	
	/**
	 * Üretimin durdurulmasını talep eder.
	 * Bu talep alındığında üretim durdurulur ve
	 * o ana kadar üretilmiş kayıtlar (varsa) sisteme eklenir.
	 */
	public static final  String                                  ACTION_STOP_GENERATION = "action_stop_generation";
	public static final  String                                  ACTION_OPEN_ACTIVITY   = "action_open_activity";
	private static final Observable<Boolean>                     running                = new Observe<>(false);
	/**
	 * Servis ön planda çalışıyorsa {@code true} değerindedir. Varsayılan {@code false} değeridir
	 * ve servis arka planda çalışır.
	 */
	public final         AtomicBoolean                           isForeground           = new AtomicBoolean(false);
	/**
	 * Servis, hem kendi durumunu hem de yapılan iş ile ilgili durumu
	 * mesaj olarak bu nesnede tutar. İlgili kişiler mesajları buradan okuyabilir.
	 */
	public final         StateMessages                           stateMessages          = new StateMessages();
	/**
	 * Üretilecek toplam arama kaydı sayısı
	 */
	private final        AtomicInteger                           totalProgress          = new AtomicInteger();
	/**
	 * Üretilen arama kaydı sayısı
	 */
	private final        AtomicInteger                           currentProgress        = new AtomicInteger();
	/**
	 * Üretilen arama kayıtları
	 */
	private final        List<Call>                              generatedCallList      = new ArrayList<>();
	/**
	 * Üretim başlamış ve devam etmekte ise {@code true} değerindedir.
	 */
	private final        Observable<Boolean>                     isWorking              = new Observe<>(false);
	private final        int                                     NOTIFICATION_ID        = 810412;
	private final        String                                  CHANNEL_ID             = "8112044796";
	private final        AtomicReference<ProgressListener<Call>> progressListener       = new AtomicReference<>(null);
	/**
	 * Arama kaydı üreticisi
	 */
	private              Generator<Call>                         callGenerator;
	private              Intent                                  stopIntent;
	private              Intent                                  openActivityIntent;
	private              NotificationManager                     notificationManager;
	private              Notifications                           notifications;
	/**
	 * 0 tamamlandı
	 * 1 durduruldu
	 * 2 durdu
	 */
	private              int                                     completionType         = -1;
	private              Consumer<Integer>                       onMissionCompleted;
	
	public static void listenRunning(@Nullable Observer<Boolean> listener) {
		
		running.setObserver(listener);
	}
	
	public static boolean isRunning() {
		
		return running.get();
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifications       = new Notifications(this);
		stopIntent          = new Intent(this, RandomCallService.class);
		openActivityIntent  = new Intent(this, RandomCallsActivity.class);
		stopIntent.setAction(ACTION_STOP_GENERATION);
		
		var channel = notifications.createNewChannel("RandomCalls", CHANNEL_ID);
		
		var channelState = notifications.isChannelEnable(channel);
		
		xlog.d("'%s' channel state : %s", channel.getName(), channelState);
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		running.compareAndSet(false, true);
		
		if (intent != null) {
			
			var action = intent.getAction();
			
			if (ACTION_STOP_GENERATION.equals(action)) {
				
				xlog.i("Stop Action received");
				isWorking.set(false);
			}
			else if (ACTION_OPEN_ACTIVITY.equals(action)) {
				
				startActivity(new Intent(this, RandomCallsActivity.class));
			}
		}
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void startGeneration(@NonNull final GenerationArgs args) {
		
		if (args.getContacts().isEmpty()) {
			
			xlog.w("Kişiler yok");
			return;
		}
		
		callGenerator = new CallGenerator(args.getContacts(), args.getStart(), args.getEnd(), args.getMaxDuration(), args.getCallTypes(), args.isTrackFree());
		
		Runny.run(() -> {
			
			xlog.d("Generation working on : %s", Thread.currentThread().getName());
			
			totalProgress.set(args.getCount());
			
			currentProgress.set(0);
			generatedCallList.clear();
			isWorking.set(true);
			int       errorTryCounter = 0;
			Exception exception       = null;
			
			
			while (isWorking.get() && currentProgress.get() < args.getCount()) {
				
				Call call;
				
				try {call = callGenerator.generate();}
				catch (Exception e) {
					e.printStackTrace();
					
					completionType = 3;
					exception      = e;
					break;
				}
				
				if (call == null) {
					
					xlog.d("Call null");
					
					//- null dönmesi bir hata olduğunu gösterir.
					//- Hatalar belirli bir sayıya ulaşırsa üretim iptal edilecek
					errorTryCounter++;
					
					if (errorTryCounter >= 100) {
						
						completionType = 3;
						exception      = new GenerationExeption();
						xlog.d("Hata tekrar sayısı : %d, üretim durduruluyor", errorTryCounter);
						break;
					}
					
					continue;
				}
				
				generatedCallList.add(call);
				int current = currentProgress.incrementAndGet();
				
				if (isForeground.get()) {
					
					Runny.run(() -> {
						
						NotificationCompat.Builder builder = getNotBuilder();
						
						builder.setContentText(Stringx.format("%d / %d", current, args.getCount()));
						builder.setProgress(args.getCount(), current, false);
						
						notificationManager.notify(NOTIFICATION_ID, builder.build());
					});
				}
				
				var listener = progressListener.get();
				
				if (listener != null) {
					
					Runny.run(() -> listener.onProgress(call, current, args.getCount()));
				}
				else {
					
					xlog.d("Progress not listening");
				}
				
			}//- While loop end here
			
			if (currentProgress.get() >= args.getCount()) {//- Umulan çıkış
				
				xlog.d("Üretim tamamlandı");
				completionType = 0;
			}
			else {
				
				if (isWorking.get()) {
					
					xlog.d("Üretim durdu!!");
					completionType = 2;
				}
				else {
					
					xlog.d("Üretim durduruldu");
					completionType = 1;
				}
			}
			
			isWorking.set(false);
			onGenerationCompleted(exception);
		}, false);
	}
	
	@Override
	public void stopGeneration() {
		
		if (isWorking.compareAndSet(true, false)) xlog.d("Generation stopped");
		else xlog.d("There is no generation");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		
		stateMessages.add("Servis bağlanıyor");
		return new RandomCallsBinder();
	}
	
	@Override
	public void onDestroy() {
		
		running.set(false);
		super.onDestroy();
	}
	
	@Override
	public void listenServiceWorking(@Nullable Observer<Boolean> listener) {
		
		isWorking.setObserver(listener);
	}
	
	@Override
	public void listenGeneration(ProgressListener<Call> progressListener) {
		
		this.progressListener.set(progressListener);
	}
	
	/**
	 * Activity servisten ayrıldığında üretim bildirim alanında devam edecek.
	 * Üretimin gidişatı bildirim ile takip edilecek.
	 * Bildirime dokunulduğunda activity yeniden açılır.
	 * Ayrıca <b>durdur</b> butonuna basarak direk bildirim üzerinden üretim tamamen durdurulabilecek.
	 *
	 * @return NotificationCompat.Builder
	 */
	@SuppressLint("UnspecifiedImmutableFlag")
	@NonNull
	private NotificationCompat.Builder getNotBuilder() {
		
		return new NotificationCompat.Builder(this, CHANNEL_ID)
				.setStyle(new NotificationCompat.BigTextStyle())
				.setContentTitle(getString(R.string.generating_random_calls))
				.setSmallIcon(R.mipmap.call)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setProgress(totalProgress.get(), currentProgress.get(), true)
				.setContentIntent(PendingIntent.getActivity(this, 2, openActivityIntent, 0))
				.setAutoCancel(true)
				.setColor(Color.YELLOW)
				.setOnlyAlertOnce(true)
				.setOngoing(true)
				.setLights(Color.MAGENTA, 600, 3000)
				.addAction(R.drawable.delete_icon, getString(R.string.stop), PendingIntent.getService(this, 1, stopIntent, 0));
	}
	
	@Override
	public boolean isInGeneration() {
		
		return isWorking.get();
	}
	
	@Override
	public void setForeground(boolean isForeground) {
		
		if (this.isForeground.get() != isForeground) {
			
			if (isForeground) startFore();
			else stopFore();
			
			this.isForeground.set(isForeground);
		}
	}
	
	private void onGenerationCompleted(@Nullable Exception exception) {
		
		xlog.d("Üretilen arama kaydı sayısı : %d", generatedCallList.size());
		
		if (exception != null) {
			
			xlog.d("Üretim hata ile sonlandı : %s", exception.getMessage());
		}
		
		if (!generatedCallList.isEmpty()) {
			
			xlog.d("Üretilen %d kayıt kaydedilecek", generatedCallList.size());
			
			int count = Calls.add(getContentResolver(), generatedCallList);
			
			if (count == generatedCallList.size()) {
				
				xlog.d("Üretilen tüm arama kayıtları sisteme eklendi");
			}
			else {
				
				if (count > 0) {
					
					xlog.d("Üretilen %d arama kaydından %d tanesi başarılı bir şekilde sisteme eklendi", count);
				}
				else {
					
					xlog.d("Üretilen arama kayıtları sisteme eklenemedi");
				}
			}
			
			if (onMissionCompleted != null) Runny.run(() -> onMissionCompleted.accept(completionType));
			else xlog.d("Mission not listening");
		}
		else {
			
			xlog.d("Hiç arama kaydı üretilmedi");
		}
		
		setForeground(false);
		stopSelf();
	}
	
	@Override
	public void onMissionCompleted(Consumer<Integer> consumer) {
		
		this.onMissionCompleted = consumer;
	}
	
	private void startFore() {
		
		startForeground(NOTIFICATION_ID, getNotBuilder().build());
	}
	
	private void stopFore() {
		
		stopForeground(true);
	}
	
	public static final class StateMessages {
		
		private final LinkedList<String> messages = new LinkedList<>();
		
		public String pop() {
			
			return messages.pop();
		}
		
		void add(@NonNull String message, Object... args) {
			
			messages.addLast(String.format(message, args));
		}
		
	}
	
	public final class RandomCallsBinder extends Binder implements ServiceReference {
		
		@Override
		public GenerationService getService() {
			
			return RandomCallService.this;
		}
		
	}
	
}