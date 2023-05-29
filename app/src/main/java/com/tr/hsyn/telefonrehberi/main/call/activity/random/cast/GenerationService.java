package com.tr.hsyn.telefonrehberi.main.call.activity.random.cast;


import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.listener.ProgressListener;
import com.tr.hsyn.watch.observable.Observer;


public interface GenerationService extends GenerationRuler {
	
	void setFore(boolean isForeground);
	
	void listenServiceWorking(@Nullable Observer<Boolean> listener);
	
	void listenGeneration(ProgressListener<Call> progressListener);
	
}
