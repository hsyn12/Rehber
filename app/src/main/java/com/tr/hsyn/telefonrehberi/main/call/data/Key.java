package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.datakey.DataKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Arama kaydı için veri anahtarlarını tanımlar.
 */
public interface Key {
	
	/**
	 * Arama kaydının contact id değeri (rehberde kayıtlı ise)
	 */
	DataKey CONTACT_ID       = DataKey.of(50, "contactId");
	/**
	 * Kayıt için eklenmiş not
	 */
	DataKey NOTE             = DataKey.of(51, "note");
	/**
	 * Kaydın silinme zamanı
	 */
	DataKey DELETED_DATE     = DataKey.of(52, "deletedDate");
	/**
	 * Gelen aramalarda telefonun çalma süresi
	 */
	DataKey RINGING_DURATION = DataKey.of(53, "ringingDuration");
	/**
	 * Arama kaydının rastgele üretilme durumu
	 */
	@Deprecated(forRemoval = true)
	DataKey RANDOM = DataKey.of(54, "random");
	/**
	 * Aramanın takip türü.
	 * <ul>
	 *    <li>0 - Hiçbir bilgi yok</li>
	 *    <li>1 - Takip edildi</li>
	 *    <li>2- Takip edilmedi</li>
	 * </ul>
	 */
	DataKey TRACK_TYPE = DataKey.of(55, "trackType");
	/**
	 * Etiketler
	 */
	DataKey LABELS     = DataKey.of(56, "labels");
	
	/**
	 * Verilen kişiden istenen bilgiyi döndürür.
	 *
	 * @param contact Kişi
	 * @param key     Veri anahtarı
	 * @param <T>     Veri türü
	 * @return T data
	 */
	@Nullable
	static <T> T get(@NotNull Contact contact, @NotNull DataKey key) {
		
		return contact.getData(key);
	}
	
	/**
	 * Verilen kişiye bilgiyi kaydeder yada siler.
	 *
	 * @param call Arama
	 * @param key  Veri anahtarı
	 * @param data Kaydedilecek bilgi (silmek için {@code null})
	 * @param <T>  Veri türü
	 * @return Veri kaydedilirse, bu anahtarla kayıtlı olan önceki bilgi döner, ilk defa kaydediliyorsa {@code null} döner.
	 * 		Veri siliniyorsa silinen veri döner, veri yoksa {@code null} döner.
	 */
	@Nullable
	static <T> T set(@NotNull Call call, @NotNull DataKey key, T data) {
		
		return call.setData(key, data);
	}
	
	/**
	 * @param call Arama kaydı
	 * @return Numara rehberde kayıtlı ise contact id değeri, değilse {@code 0}
	 */
	static long getContactId(@NotNull Call call) {
		
		return call.getLong(CONTACT_ID, 0L);
	}
	
	static void setContactId(@NotNull Call call, long contactId) {
		
		call.setData(CONTACT_ID, contactId);
		call.setExtra(Calls.createExtraInfo(call));
	}
	
	@Nullable
	static String getNote(@NotNull Call call) {
		
		return call.getData(NOTE);
	}
	
	static boolean isRandom(@NotNull Call call) {
		
		return call.getBool(RANDOM);
	}
	
}
