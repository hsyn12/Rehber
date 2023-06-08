package com.tr.hsyn.telefonrehberi.main.call.activity.random;


import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.cast.GenerationArgs;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;


/**
 * <p>Üretimin başlatılması ve izlenmesinden sorumlu.
 * <p>Servis bağlantısı sadece bu sınıf tarafından talep edilir.
 * Kullanıcı üretimi başlatmak istediğinde, şartlar uygunsa servis çalıştırılır,
 * uygun değilse, bu uygunsuzluğun sebebi kullanıcıya bildirilir.
 */
public abstract class RandomCallsActivityGeneration extends RandomCallsActivityMenu {
	
	protected boolean waitingGeneration;
	protected long    progressWriteTime;
	protected int     lastMissionState;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		progressGeneration.setIndeterminate(false);
		
		if (isServiceWorking()) {
			
			buttonStartGeneration.setEnabled(false);
			randomCallService.setFore(false);
		}
	}
	
	@Override
	protected void onClickStartGeneration(View view) {
		
		super.onClickStartGeneration(view);
		
		int count = getEnteredGenerationCount();
		
		xlog.d("Üretilecek arama kaydı sayısı : %d", count);
		
		if (count > 0) {
			
			if (count != getGenerationCount()) {
				
				setGenerationCount(count);
			}
			
			progressGeneration.setMax(count);
			progressGeneration.setMin(0);
			progressGeneration.setProgress(0);
			
			waitingGeneration = true;
			buttonStartGeneration.setEnabled(false);
			editTextGenerationCount.setEnabled(false);
			startRandomCallService();
		}
		else {
			
			Show.snake(this, getString(R.string.invalid_generation_number));
		}
	}
	
	@CallSuper
	@Override
	protected void onServiceConnected() {
		
		super.onServiceConnected();
		
		if (waitingGeneration) {
			
			waitingGeneration = false;
			startGeneration();
		}
	}
	
	@Override
	protected void onMissionCompleted(int compilationType) {
		
		super.onMissionCompleted(compilationType);
		lastMissionState = compilationType;
		
		switch (lastMissionState) {
			
			case 1:
				generationStopped();
				break;
			case 2:
				generationStop();
				break;
			case 3:
				onGenerationException();
				break;
			
		}
		
		buttonStartGeneration.setEnabled(true);
		editTextGenerationCount.setEnabled(true);
		
		progressGeneration.setIndeterminate(false);
		progressGeneration.setProgress(0);
		
		Blue.box(Key.REFRESH_CALL_LOG, Boolean.TRUE);
		
		Runny.run(() -> textCurrentProgress.animateText(getString(R.string.generated_calls_saved)), 1000);
		Runny.run(() -> textCurrentProgress.animateText(getString(R.string.ready_for_generation)), 4000);
	}
	
	@Override
	protected void onGenerationStop() {
		
		super.onGenerationStop();
		
		Runny.run(() -> progressGeneration.setIndeterminate(true));
		
		Runny.run(() -> textCurrentProgress.animateText(getString(R.string.saving)), 300);
	}
	
	protected void startGeneration() {
		
		randomCallService.startGeneration(GenerationArgs.newArgs(getEnteredGenerationCount(), getDateStart(), getDateEnd(), 60 * 60, getCallTypes(), false, getContacts()));
	}
	
	protected void onGenerationException() {
		
		
	}
	
	/**
	 * Üretim bilinmeyen bir sebeple durdu
	 */
	private void generationStop() {
		
		
	}
	
	/**
	 * Kullanıcı tarafından durduruldu
	 */
	private void generationStopped() {
		
		
	}
	
	@SuppressLint({"DefaultLocale", "SetTextI18n"})
	@Override
	public void onProgress(@NonNull Call value, int currentProgress, int total) {
		
		long now = Time.now();
		
		if (now - progressWriteTime > 300) {
			
			textCurrentProgress.animateText(value.getNumber());
			textProgress.setText(String.format("%d %%", (currentProgress * 100) / total));
			progressWriteTime = Time.now();
		}
		
		progressGeneration.setProgress(currentProgress + 1);
		
		if (currentProgress >= total) {
			
			textProgress.setText("100 %");
		}
	}
}
