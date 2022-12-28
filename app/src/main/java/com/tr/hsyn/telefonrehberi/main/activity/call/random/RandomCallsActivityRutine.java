package com.tr.hsyn.telefonrehberi.main.activity.call.random;


import androidx.annotation.CallSuper;

import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.rutine.Rutine;


/**
 * Rutin işleri yönetir.
 */
public abstract class RandomCallsActivityRutine extends RandomCallsActivityRegister {

    /**
     * Rutine
     */
    protected final Rutine rutine = new Rutine(10_000L);

    @Override
    protected void onCreate() {

        super.onCreate();

        rutine.startRutine(i -> Runny.run(() -> onRutine(i)));
    }

    /**
     * Rutin işler.
     *
     * @param counter Rutin döngü sayısı
     */
    @CallSuper
    protected void onRutine(int counter) {
		
		/*long lifeTime = lifeWriter.getLifeStartTime();
		
		if (isServiceWorking()) {//- Üretim yapılıyor
			
			CharSequence msg = inGenerationSelector.getMin();
			
			if (!textDescription.getText().equals(msg)) {
				
				textDescription.animateText(msg);
			}
			else {
				
				
			}
		}
		else {
			
			int count = getCount();
			
			if (count != -1) {
				
				String msg = getString(R.string.idle_message_generation_count, count);
				
				if (!textDescription.getText().equals(msg)) {
					
					textDescription.animateText(msg);
				}
				else {
					
					
				}
			}
		}*/


    }

}
