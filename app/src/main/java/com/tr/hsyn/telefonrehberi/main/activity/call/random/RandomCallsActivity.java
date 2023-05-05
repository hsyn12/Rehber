package com.tr.hsyn.telefonrehberi.main.activity.call.random;


import android.view.View;

import androidx.annotation.NonNull;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.metadata.Creator;
import com.tr.hsyn.metadata.Description;
import com.tr.hsyn.selector.ItemSelector;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.xbox.Blue;

import java.util.List;


@Creator("hsyn_tr")
@Description("Sınıfın amacı rastgele arama kayıtları üretmek. Üretim, " +
             "rehberdeki kişiler üzerinden yapılır. Bu kişiler isteğe göre seçilebilir. " +
             "Üretilen arama kayıtları sistem kayıtlarına eklenir. " +
             "TelefonRehberi kendi ürettiği kayıtlarla gerçek kayıtları " +
             "birbirinden ayırt edebilir ancak sistem bunu ayırt edemez. Sisteme göre, " +
             "üretilen kayıtların gerçek kayıtlardan hiçbir farkı yoktur.")
public class RandomCallsActivity extends RandomCallsActivityGeneration {
	
	protected ItemSelector<CharSequence> inGenerationSelector;
	protected ItemSelector<CharSequence> nonGenerationSelector;
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		onContactsSelected();
	}
	
	@Override
	protected void onClickStartGeneration(View view) {
		
		super.onClickStartGeneration(view);
		rutine.delay();
	}
	
	@Override
	protected void onContactsSelected() {
		
		if (getSelectedContacts().isEmpty()) {
			
			buttonStartGeneration.setEnabled(false);
			
			if (getContacts().isEmpty()) {
				
				Show.snake(this, getString(R.string.warn_no_any_contact));
			}
			else {
				
				Show.snake(this, getString(R.string.warn_waiting_contact_selection));
			}
		}
		else {
			
			if (!isServiceWorking()) {
				
				buttonStartGeneration.setEnabled(true);
				textCurrentProgress.animateText("Üretim başlatılmaya hazır");
			}
		}
	}
	
	@NonNull
	@Override
	protected List<Contact> getContacts() {
		
		return Lister.listOf(Blue.<List<Contact>>getObject(Key.CONTACTS));
	}
	
	@Override
	protected void onClickEditTextGeneration(View view) {
		
		super.onClickEditTextGeneration(view);
		
		rutine.delay();
	}
}
