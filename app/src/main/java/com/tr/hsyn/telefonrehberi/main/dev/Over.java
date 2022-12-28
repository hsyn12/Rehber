package com.tr.hsyn.telefonrehberi.main.dev;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.bool.Bool;
import com.tr.hsyn.buildkeys.BuildKeys;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.use.Use;
import com.tr.hsyn.xbox.Blue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <h1>Over</h1>
 * <p>
 * Programın her yerinden erişilmesi gereken bazı kodlara erişim yöntemi sağlar.
 * Sınıf tamamen {@link Blue} sınıfı üzerinden çalışır.
 * {@code Blue} sınıfı kullanılırken erişilmek istenen nesnenin anahtarı bilinmek zorunda.
 * Ancak bu sınıf ({@code Over}) {@link BuildKeys} sınıfında tanımlanan nesne kodlarını
 * kullandırmak yerine daha açık ve anlaşılır metotlar sunar.
 * <br>
 *
 * <pre><code>
 * Story&lt;Contact&gt; contactManager = Blue.get(BuildKeys.CONTACT_STORY); // yerine
 * Story&lt;Contact&gt; contactManager = Over.Contact.getContactManager(); // kullanılır
 * // İkisi de aynı nesneyi döndürür
 * </code></pre>
 *
 * @see Blue
 * @see BuildKeys
 */
public interface Over {
	
	/**
	 * Program ana dizini
	 */
	interface App {
		
		/**
		 * @return Application context
		 */
		static Context getContext() {return Blue.getObject(BuildKeys.CONTEXT);}
		
	}
	
	/**
	 * Rehber dizini
	 */
	interface Contacts {
		
		static List<Contact> getContacts() {
			
			return Blue.getObject(BuildKeys.CONTACTS);
		}
		
		/**
		 * @return Kişi listesinden en son seçilmiş kişiyi döndürür.
		 */
		static Contact getSelectedContact() {
			
			return Blue.getObject(BuildKeys.CONTACT_SELECTED);
		}
		
		/**
		 * Rehber listesinin yenilenmesi gerektiğini bildiren bir işareti set eder.
		 */
		static void refreshContacts() {
			
			Blue.box(BuildKeys.SIGN_REFRESH_CONTACTS, true);
		}
		
		/**
		 * Rehber yöneticisini döndürür.
		 *
		 * @return Rehber yöneticisi
		 */
		static Story<Contact> getContactManager() {
			
			return Blue.getObject(BuildKeys.CONTACT_STORY);
		}
		
		static void addDeleted(Contacts... contacts) {
			
			List<Contacts> contactList = getDeleted();
			
			if (contactList == null) {
				
				contactList = new ArrayList<>();
				Blue.box(BuildKeys.DELETED_CONTACTS, contactList);
			}
			
			contactList.addAll(Arrays.asList(contacts));
			
		}
		
		static List<Contacts> getDeleted() {
			
			return Blue.getObject(BuildKeys.DELETED_CONTACTS);
		}
	}
	
	/**
	 * Arama kayıtları dizini
	 */
	interface CallLog {
		
		/**
		 * Arama kayıtları yöneticisini döndürür.
		 *
		 * @return Arama kayıtları yöneticisi
		 */
		static Story<Call> getCallLogManager() {
			
			return Blue.getObject(BuildKeys.CALL_STORY);
		}
		
		/**
		 * Arama kayıtlarında kalıcı bir değişiklik olduğunu ve
		 * yenilenmesi gerektiğini bildiren bir işaret kaydeder.
		 */
		static void refreshCallLog() {
			
			Blue.box(BuildKeys.REFRESH_CALL_LOG, true);
		}
		
		/**
		 * Ama kayıtlarında kalıcı bir değişiklik yapıldığında,
		 * bu işlemi yapan kod kayıtların değiştiğini ve yenilenmesi gerektiğini bildiren bir bilgi kaydeder.
		 * Bu metot kaydedilen bu bilgiyi kontrol eder ve yenilenme gerekliliği varsa {@code true} döndürür.
		 * Metot sadece kontrol eder, bilgiyi değiştirmez.
		 *
		 * @return Arama kayıtlarının yenilenmesi gerekiyorsa {@code true}
		 */
		static boolean needRefreshCallLog() {
			
			Boolean b = Blue.getObject(BuildKeys.REFRESH_CALL_LOG);
			
			return b != null && b;
		}
		
		/**
		 * Arama kayıtlarında kalıcı bir değişiklik olmuş ise {@code true} döner
		 * ve verilen yeni değeri set eder.
		 * Verilen yeni değerin set edilmesi için herhangi bir koşul yoktur,
		 * dönen değer her ne olursa olsun verilen değer set edilir.
		 *
		 * @param newValue Yeni değer
		 * @return Arama kayıtlarında güncelleme olmuş ise {@code true}
		 */
		static boolean needRefreshCallLog(@Nullable Boolean newValue) {
			
			boolean b = needRefreshCallLog();
			
			Blue.box(BuildKeys.REFRESH_CALL_LOG, newValue);
			
			return b;
		}
		
		/**
		 * Arama kayıtları listesi
		 */
		interface Calls {
			
			/**
			 * @return {@code true} if calls updated
			 */
			@NonNull
			static Bool isUpdated() {
				
				return new Bool(Blue.getObject(BuildKeys.CALL_LOG_UPDATED));
			}
			
			static Bool isUpdated(@NonNull Bool newValue) {
				
				var u = isUpdated();
				
				if (!u.equals(newValue))
					Blue.box(BuildKeys.CALL_LOG_UPDATED, newValue.bool());
				
				return u;
			}
			
			/**
			 * Listenin durumunu değiştir.
			 *
			 * @param state Yeni durum
			 */
			static void setUpdated(@NonNull Bool state) {
				
				Blue.box(BuildKeys.CALL_LOG_UPDATED, state.getObject());
			}
			
			@Nullable
			static List<Call> getCalls() {
				
				return Blue.getObject(BuildKeys.CALL_LOG);
			}
			
			static void setCalls(List<Call> calls) {
				
				Blue.box(BuildKeys.CALL_LOG, calls);
			}
			
			/**
			 * Global arama kayıtları listesi editörü.
			 * Sadece elindeki liste üzerinde değişiklik yapar.
			 * Veri tabanı yada sistem kayıtları ile hiçbir ilgisi yoktur.
			 */
			interface Editor {
				
				static void add(Call call) {
					
					Use.ifNotNull(getCalls(), calls -> {
						
						calls.add(call);
						Calls.setUpdated(Bool.TRUE);
					});
				}
				
				static void set(int index, Call call) {
					
					Use.ifNotNull(getCalls(), calls -> calls.set(index, call));
				}
				
				/**
				 * Arama kayıtlarını siler.
				 *
				 * @param calls Ana listeden silinecek kayıtlar
				 * @return Silinebilen kayıt sayısı
				 */
				static int delete(Call... calls) {
					
					List<Call> callList = getCalls();
					int        deleted  = 0;
					
					if (callList == null || calls == null || calls.length == 0) return deleted;
					
					//noinspection ForLoopReplaceableByForEach
					for (int i = 0; i < calls.length; i++) {
						
						int index = callList.indexOf(calls[i]);
						
						if (index != -1) {
							
							callList.remove(index);
							deleted++;
						}
					}
					
					if (deleted != 0) Calls.setUpdated(Bool.TRUE);
					
					return deleted;
				}
			}
		}
		
		
	}
	
	
}
