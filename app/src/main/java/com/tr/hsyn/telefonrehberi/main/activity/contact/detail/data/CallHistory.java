package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Bir kişiye ait arama özeti bilgilerini tutar
 */
public class CallHistory {
	
	private final Contact                  contact;
	private final Map<Integer, List<Call>> calls;
	private       int                      incomingDuration;
	private       int                      outgoingDuration;
	
	public CallHistory(Contact contact, Map<Integer, List<Call>> calls) {
		
		this.contact = contact;
		this.calls   = calls;
	}
	
	/**
	 * @return Gelen aramalardaki toplam konuşma süresi (saniye)
	 */
	public int getIncomingDuration() {
		
		if (incomingDuration != 0) return incomingDuration;
		
		return incomingDuration = getIncomingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
	}
	
	/**
	 * @return Gelen aramalar
	 */
	@NonNull
	public List<Call> getIncomingCalls() {
		
		return getCalls(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	/**
	 * Verilen arama türlerine ait aramaları döndürür.
	 *
	 * @param callTypes Arama türleri
	 * @return Arama kayıtları
	 */
	@NonNull
	public List<Call> getCalls(int... callTypes) {
		
		List<Call> _calls = new ArrayList<>();
		
		Lister.loop(callTypes, type -> {
			
			var list = calls.get(type);
			
			if (list != null) _calls.addAll(list);
		});
		
		return _calls;
	}
	
	/**
	 * @return Giden aramalardaki toplam konuşma süresi (saniye)
	 */
	public int getOutgoingDuration() {
		
		if (outgoingDuration != 0) return outgoingDuration;
		
		return outgoingDuration = getOutgoingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
	}
	
	/**
	 * @return Giden aramalar
	 */
	@NonNull
	public List<Call> getOutgoingCalls() {
		
		return getCalls(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	/**
	 * @return Cevapsız aramalar
	 */
	@NonNull
	public List<Call> getMissedCalls() {
		
		return getCalls(Call.MISSED);
	}
	
	/**
	 * @return Reddedilen aramalar
	 */
	@NonNull
	public List<Call> getRejectedCalls() {
		
		return getCalls(Call.REJECTED);
	}
	
	public int getIncomingCallSize() {
		
		return getCallSize(Call.INCOMING) + getCallSize(Call.INCOMING_WIFI);
	}
	
	private int getCallSize(int callType) {
		
		return getCalls(callType).size();
	}
	
	public int getMissedCallSize() {
		
		return getCallSize(Call.MISSED);
	}
	
	public int getOutgoingCallSize() {
		
		return getCallSize(Call.OUTGOING) + getCallSize(Call.OUTGOING_WIFI);
	}
	
	public int getRejectedCallSize() {
		
		return getCallSize(Call.REJECTED);
	}
	
}
