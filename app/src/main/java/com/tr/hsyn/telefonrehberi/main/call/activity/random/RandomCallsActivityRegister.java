package com.tr.hsyn.telefonrehberi.main.call.activity.random;


import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.register.Register;
import com.tr.hsyn.time.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Bilgi giriş-çıkışını sağlar.
 */
public abstract class RandomCallsActivityRegister extends RandomCallsActivityService {
	
	protected final String      CALL_TYPE_STATE_INCOMMING = "state_incomming";
	protected final String      CALL_TYPE_STATE_OUTGOING  = "state_outgoing";
	protected final String      CALL_TYPE_STATE_MISSED    = "state_missed";
	protected final String      CALL_TYPE_STATE_REJECTED  = "state_rejected";
	protected final String      GENERATION_COUNT          = "generation_count";
	private final   String      SELECTED_CONTACTS         = "selected_contacts";
	private final   String      GENERATION_DATE_START     = "date_start";
	private final   String      GENERATION_DATE_END       = "date_end";
	private         Register    register;
	private         long        dateEnd;
	private         long        dateStart;
	private         int         generationCount;
	private         Set<String> selectedContacts;
	private         boolean     callTypeIncomming;
	private         boolean     callTypeOutgoing;
	private         boolean     callTypeMissed;
	private         boolean     callTypeRejected;
	
	@Override
	protected void onCreate() {
		
		register          = Register.on(this, "random_calls_activity");
		dateStart         = register.getLong(GENERATION_DATE_START, Time.FromNow.months(-6));
		dateEnd           = register.getLong(GENERATION_DATE_END, Time.now());
		selectedContacts  = _getSelectedContacts();
		generationCount   = register.getInt(GENERATION_COUNT, 10);
		callTypeIncomming = getCallType(CALL_TYPE_STATE_INCOMMING);
		callTypeOutgoing  = getCallType(CALL_TYPE_STATE_OUTGOING);
		callTypeMissed    = getCallType(CALL_TYPE_STATE_MISSED);
		callTypeRejected  = getCallType(CALL_TYPE_STATE_REJECTED);
		
		super.onCreate();
	}
	
	@NonNull
	private Set<String> _getSelectedContacts() {
		
		Set<String> selectedContacts = register.getStringSet(SELECTED_CONTACTS, null);
		
		if (selectedContacts == null) {
			
			selectedContacts = new HashSet<>(getContacts().size());
			
			selectedContacts.addAll(getContacts().stream().map(Contact::getContactId).map(String::valueOf).collect(Collectors.toList()));
		}
		
		return selectedContacts;
	}
	
	private boolean getCallType(@NonNull final String key) {
		
		return register.getBoolean(key, true);
	}
	
	@NonNull
	protected abstract List<Contact> getContacts();
	
	@NonNull
	@SuppressWarnings("ConstantConditions")
	protected final Integer[] getCallTypes() {
		
		Map<Boolean, List<Integer>> types = new HashMap<>();
		
		types.put(true, new ArrayList<>());
		types.put(false, new ArrayList<>());
		
		types.get(getCallTypeIncomming()).add(com.tr.hsyn.calldata.Call.INCOMING);
		types.get(getCallTypeOutgoing()).add(com.tr.hsyn.calldata.Call.OUTGOING);
		types.get(getCallTypeMissed()).add(com.tr.hsyn.calldata.Call.MISSED);
		types.get(getCallTypeRejected()).add(com.tr.hsyn.calldata.Call.REJECTED);
		
		List<Integer> _types = types.get(true);
		
		//- Eğer tüm arama türleri
		//- seçim dışı bırakılırsa
		//- tüm arama türleri kullanılsın
		if (_types.isEmpty()) return types.get(false).toArray(new Integer[0]);
		
		return _types.toArray(new Integer[0]);
	}
	
	protected final boolean getCallTypeIncomming() {
		
		return callTypeIncomming;
	}
	
	protected final void setCallTypeIncomming(boolean set) {
		
		setCallType(CALL_TYPE_STATE_INCOMMING, callTypeIncomming = set);
	}
	
	protected final void setCallType(@NonNull final String key, boolean set) {
		
		register.edit().putBoolean(key, set).apply();
	}
	
	protected final boolean getCallTypeOutgoing() {
		
		return callTypeOutgoing;
	}
	
	protected final void setCallTypeOutgoing(boolean set) {
		
		setCallType(CALL_TYPE_STATE_OUTGOING, callTypeOutgoing = set);
	}
	
	protected final boolean getCallTypeMissed() {
		
		return callTypeMissed;
	}
	
	protected final void setCallTypeMissed(boolean set) {
		
		setCallType(CALL_TYPE_STATE_MISSED, callTypeMissed = set);
	}
	
	protected final boolean getCallTypeRejected() {
		
		return callTypeRejected;
	}
	
	protected final void setCallTypeRejected(boolean set) {
		
		setCallType(CALL_TYPE_STATE_REJECTED, callTypeRejected = set);
	}
	
	protected final long getDateStart() {
		
		return dateStart;
	}
	
	protected final void setDateStart(long dateStart) {
		
		register.edit().putLong(GENERATION_DATE_START, this.dateStart = dateStart).apply();
	}
	
	protected final long getDateEnd() {
		
		return dateEnd;
	}
	
	protected final void setDateEnd(long dateEnd) {
		
		register.edit().putLong(GENERATION_DATE_END, this.dateEnd = dateEnd).apply();
	}
	
	@Override
	protected final int getGenerationCount() {
		
		return generationCount;
	}
	
	protected final void setGenerationCount(int generationCount) {
		
		register.edit().putInt(GENERATION_COUNT, this.generationCount = generationCount).apply();
	}
	
	protected final Set<String> getSelectedContacts() {
		
		return selectedContacts;
	}
	
	protected final void setSelectedContacts(@NonNull Set<String> selectedContacts) {
		
		register.edit().putStringSet(SELECTED_CONTACTS, selectedContacts).apply();
	}
}
