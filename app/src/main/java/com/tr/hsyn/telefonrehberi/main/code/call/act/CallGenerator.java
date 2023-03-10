package com.tr.hsyn.telefonrehberi.main.code.call.act;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.random.Randoom;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.cast.Generator;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


/**
 * Rastgele arama kaydı üreticisi.
 *
 * @author hsyn 13 Şubat 2022 Pazar 11:38:48
 */

public class CallGenerator implements Generator<Call> {
	
	private final List<Contact> contacts;
	private final int           maxDuration;
	private final Integer[]     types;
	private final long          today;
	private final long          start;
	private final long          end;
	private final boolean       trackFree;
	
	/**
	 * Arama kaydı üreticisi oluştur.
	 *
	 * @param contacts    Kayıtların üretileceği kişiler listesi.
	 * @param startDate   Üretilen kayıtların arama zamanları için geçmiş sınırı. Kayıtlar bu zamandan daha öncesine ait olamayacak.
	 *                    Eğer bu değer sıfır yada negatif verilirse yada şimdiki zamandan büyük ise, şimdiki zamandan altı ay öncesi olarak tanımlanacak.
	 * @param endDate     Üretilen kayıtların arama zamanları için gelecek sınırı. Kayıtlar bu zamandan daha ilerisine ait olamayacak.
	 *                    Eğer bu değer   sıfır yada negatif verilirse yada şimdiki zamandan büyük ise, şimdiki zaman olarak tanımlanacak.
	 * @param maxDuration Konuşma olan kayıtlar için (gelen-giden aramalar) maksimum konuşma süresi (saniye). Daima sıfırdan büyük olacak.
	 * @param types       Üretilecek kayıtları arama türleri. En az bir tane tür belirtilmeli, aksi halde kayıtlar gelen-giden arama türlerinde üretilir.
	 * @param trackFree   Takip bilgisi eklensinsin mi
	 */
	public CallGenerator(
			@Size(min = 1) @NonNull List<Contact> contacts,
			long startDate,
			long endDate,
			int maxDuration,
			@NonNull Integer[] types,
			boolean trackFree) {
		
		this.contacts    = contacts;
		this.maxDuration = maxDuration < 1 ? 10 : maxDuration;
		this.trackFree   = trackFree;
		this.types       = types.length > 1 ? types : new Integer[]{CallType.INCOMING, CallType.OUTGOING};
		today            = Time.now();
		
		//- Başlama zamanı bugünden büyükse biraz geri alalım
		if (startDate > today || startDate <= 0) start = Time.NotNow.months(-6);
		else start = startDate;
		
		//- Bitiş zamanı bugünden büyükse today yapalım
		if (endDate > today || endDate <= 0) end = today;
		else end = endDate;
	}
	
	/**
	 * Rastgele Arama kaydı üretir.
	 *
	 * @return Arama kaydı. Eğer kişinin telefon numarası yoksa yada sahip olduğu numara geçerli bir telefon numarası değilse {@code null}.
	 */
	@Override
	@Nullable
	@SuppressLint("DefaultLocale")
	public Call generate() {
		
		Contact      contact;
		List<String> numbers;
		String       number;
		
		while (true) {
			
			contact = getContact();
			numbers = contact.getData(ContactKey.NUMBERS);
			
			if (numbers == null) {
				
				contacts.remove(contact);
				
				if (contacts.isEmpty()) {
					
					xlog.d("Rehberde kimse yok");
					return null;
				}
			}
			else {
				
				number = numbers.get(Randoom.getInt(numbers.size()));
				
				if (!PhoneNumbers.isPhoneNumber(number)) {
					
					xlog.d("Geçersiz numara : %s", number);
				}
				else break;
			}
		}
		
		
		int  typeIndex       = Randoom.getInt(types.length);
		int  callType        = types[typeIndex];
		long date            = end == 0 ? Randoom.getLong(start, today) : Randoom.getLong(start, end);
		int  duration        = 0;
		int  trackType       = trackFree ? Randoom.getInt(-1, 2) : 0;
		long ringingDuration = 0L;
		
		if (typeIndex < 2) {
			
			if (Randoom.getBool()) {
				
				duration = Randoom.getInt(maxDuration);
			}
		}
		
		if (trackType == 1) {
			
			if (callType == Call.INCOMING || callType == Call.MISSED || callType == Call.REJECTED) {
				
				ringingDuration = Randoom.getLong(50_000L);//- En fazla 50 saniye çalmış olsun
			}
		}
		
		Call call = new Call(contact.getName(), number, callType, date, duration);
		
		call.setData(CallKey.RINGING_DURATION, ringingDuration);
		call.setData(CallKey.TRACK_TYPE, trackType);
		call.setData(CallKey.RANDOM, true);
		call.setExtra(Calls.createExtraInfo(call));
		return call;
	}
	
	private Contact getContact() {
		
		return contacts.get(Randoom.getInt(contacts.size()));
	}
}
