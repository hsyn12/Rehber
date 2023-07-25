package com.tr.hsyn.telefonrehberi.main.call.story;


import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.Calls;
import com.tr.hsyn.telefonrehberi.main.call.data.Database;
import com.tr.hsyn.telefonrehberi.main.call.data.Key;
import com.tr.hsyn.telefonrehberi.main.contact.data.bank.system.SystemContacts;
import com.tr.hsyn.telefonrehberi.main.dev.Story;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Arama kayıtları yöneticisi.<br>
 *
 * @see com.tr.hsyn.registery.cast.Database
 */
@Keep
public class CallStory implements Story<Call> {
	
	private final com.tr.hsyn.registery.cast.Database<Call> database;
	private final ContentResolver                           contentResolver;
	
	public CallStory(com.tr.hsyn.registery.cast.Database<Call> database, ContentResolver contentResolver) {
		
		this.database        = database;
		this.contentResolver = contentResolver;
	}
	
	/**
	 * Kendi içerisinde hem sistem kayıtlarını hem veri tabanı kayıtlarını yükler ve
	 * ilk yükleme haricinde hep veri tabanı kayıtlarını döndürür.
	 * Dönen liste, eklenen ve silinen kayıtlar işlenmiş bir listedir.
	 *
	 * @return Arama kayıtları
	 */
	@Override
	public List<Call> load() {
		
		//- Buradan veri tabanı listesi dönmeli
		
		List<com.tr.hsyn.calldata.Call> systemCalls   = loadFromSystem();
		List<com.tr.hsyn.calldata.Call> databaseCalls = loadFromDatabase();
		
		//xlog.d("Arama kayıtları alındı. Sistemde %d, veri tabanında %d arama kaydı var", systemCalls.size(), databaseCalls.size());
		
		if (databaseCalls.isEmpty()) {
			
			if (!systemCalls.isEmpty()) {
				
				xlog.d("Veri tabanında arama kaydı yok ancak sistemde %d arama var. Sanırım bu ilk yükleme", systemCalls.size());
				
				int success = database.add(systemCalls);
				
				if (success == systemCalls.size()) {
					
					xlog.d("Tüm kayıtlar veri tabanına kaydedildi");
				}
				else {
					
					xlog.d("%d arama kaydından %d tanesi veri tabanına kaydedildi", systemCalls.size(), success);
				}
				
				systemCalls.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
				updateInfo(systemCalls);
				
				return systemCalls;
			}
			else {
				xlog.d("Sistemde arama kaydı yok");
			}
		}
		else {
			
			if (systemCalls.isEmpty()) {
				
				xlog.d("Arama kaydı veri tabanında %d kayıt var ancak sistemde hiç kayıt yok. Kullanıcının kayıtları sistemden sildiğini düşünüyorum. Silinen kayıtlar veri tabanında silinmiş olarak işaretlenecek", databaseCalls.size());
				
				long now = System.currentTimeMillis();
				
				databaseCalls.forEach(c -> c.setData(Key.DELETED_DATE, now));
				database.update(databaseCalls);
				xlog.d("Call collection created [system]");
				return new ArrayList<>(0);
			}
			else {
				
				//- Karşılaştırmanın yapılması gereken yol
				
				//xlog.d("Arama kayıtları değişiklikleri kontrol ediliyor");
				
				@NotNull List<Call> deletedCalls = Lister.difference(databaseCalls, systemCalls);
				@NotNull List<Call> newCalls     = Lister.difference(systemCalls, databaseCalls);
				
				if (deletedCalls.isEmpty() && newCalls.isEmpty()) {
					
					xlog.d("Arama kayıtlarında bir değişiklik yok");
				}
				else {
					
					if (!deletedCalls.isEmpty()) {
						
						xlog.d("Silinen %d arama kaydı var", deletedCalls.size());
						
						long now = System.currentTimeMillis();
						
						deletedCalls.forEach(c -> c.setData(Key.DELETED_DATE, now));
						
						database.update(deletedCalls);
						
						databaseCalls.removeAll(deletedCalls);
					}
					
					if (!newCalls.isEmpty()) {
						
						xlog.d("%d yeni arama kaydı var [%s]", newCalls.size(), newCalls);
						
						int count = database.add(newCalls);
						databaseCalls.addAll(newCalls);
						
						xlog.d("%d calls added into the database", count);
					}
				}
			}
		}
		
		databaseCalls.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
		updateInfo(databaseCalls);
		return databaseCalls;
	}
	
	/**
	 * @return Veri tabanında silinmemiş tüm kayıtları döndürür.
	 */
	@Override
	public List<Call> loadFromDatabase() {
		
		return database.queryAll(Database.DELETED_DATE + "=0");
	}
	
	/**
	 * @return Tüm sistem kayıtları
	 */
	@Override
	public List<com.tr.hsyn.calldata.Call> loadFromSystem() {
		
		return Calls.getCalls(contentResolver);
	}
	
	/**
	 * Veri tabanına yeni kayıt ekler.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean addIntoDatabase(com.tr.hsyn.calldata.Call call) {
		
		return database.add(call);
	}
	
	/**
	 * Kaydı önce sistemden sonra veri tabanından siler.
	 * Başarı durumu, her iki yerden de silinirse {@code true},
	 * herhangi birinden silinme işlemi başarısız olursa {@code false} döndürür.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean delete(com.tr.hsyn.calldata.Call call) {
		
		if (deleteFromSystem(call)) {
			
			if (updateFromDatabase(call)) {
				
				return true;
			}
			else {
				
				xlog.d("Arama kaydı sistemden silindi ancak veri tabanından silinemedi : %s", Stringx.overWrite(call.getNumber()));
			}
		}
		else {
			
			xlog.d("Arama kaydı silinemedi : %s", Stringx.overWrite(call.getNumber()));
		}
		
		
		return false;
	}
	
	@Override
	public boolean deleteFromDatabase(com.tr.hsyn.calldata.Call item) {
		
		return database.delete(item);
	}
	
	@Override
	public int deleteFromDatabase(List<? extends com.tr.hsyn.calldata.Call> items) {
		
		return database.delete(items);
	}
	
	/**
	 * Kaydı günceller.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean updateFromDatabase(@NonNull com.tr.hsyn.calldata.Call call) {
		
		return database.update(call, String.valueOf(call.getTime()));
	}
	
	@Override
	public int updateFromDatabase(@NotNull List<? extends com.tr.hsyn.calldata.Call> items) {
		
		return (int) items.stream().filter(this::updateFromDatabase).count();
	}
	
	/**
	 * Kaydı günceller.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean updateFromSystem(com.tr.hsyn.calldata.Call call) {
		
		return Calls.update(contentResolver, call);
	}
	
	/**
	 * Sisteme yeni kayıt ekler.
	 *
	 * @param call Kayıt
	 * @return Yeni eklenen kaydın adresi
	 */
	@Override
	public boolean addIntoSystem(com.tr.hsyn.calldata.Call call) {
		
		return Calls.add(contentResolver, call) != null;
	}
	
	/**
	 * Veri tabanına yeni kayıt ekler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde eklenen kayıt sayısı
	 */
	@Override
	public int addIntoDatabase(List<? extends com.tr.hsyn.calldata.Call> calls) {
		
		return database.add(calls);
	}
	
	/**
	 * Sisteme yeni kayıt ekler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde eklenen kayıt sayısı
	 */
	@Override
	public int addIntoSystem(List<? extends com.tr.hsyn.calldata.Call> calls) {
		
		return Calls.add(contentResolver, calls);
	}
	
	/**
	 * Kaydı siler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde silinen kayıt sayısı
	 */
	@Override
	public int deleteFromSystem(List<? extends com.tr.hsyn.calldata.Call> calls) {
		
		return Calls.delete(contentResolver, calls);
	}
	
	/**
	 * Kaydı siler.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean deleteFromSystem(@NotNull com.tr.hsyn.calldata.Call call) {
		
		return Calls.delete(contentResolver, call.getTime());
	}
	
	@Override
	public int delete(List<? extends com.tr.hsyn.calldata.Call> items) {
		
		int count = deleteFromSystem(items);
		
		long time = Time.now();
		items.forEach(c -> c.setData(Key.DELETED_DATE, time));
		
		updateFromDatabase(items);
		
		return count;
	}
	
	/**
	 * Updates the calls for its name and contact ID.
	 *
	 * @param calls Calls to update
	 */
	private void updateInfo(@NotNull List<Call> calls) {
		
		Map<String, Long>   ids   = new HashMap<>();
		Map<String, String> names = new HashMap<>();
		
		for (Call call : calls) {
			
			long id = Key.getContactId(call);
			
			if (id == 0L || Stringx.isNoboe(call.getName())) {
				
				String  number   = PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.MINIMUM_NUMBER_LENGTH);
				Long    _id      = ids.get(number);
				boolean isUpdate = false;
				
				if (_id == null) {
					
					Contact contact = SystemContacts.getContact(contentResolver, call.getNumber());
					
					if (contact != null) {
						
						String name = contact.getName();
						
						boolean isName = Nina.regex("[^0-9+]").find(name).isValid();
						
						if (isName) {
							isUpdate = true;
							call.setName(name);
							names.put(number, name);
							xlog.d("Call owner found : %s", name);
						}
						else xlog.d("Call owner not found : %s", call.getNumber());
						
						
						if (contact.getContactId() != 0) {
							isUpdate = true;
							Key.setContactId(call, contact.getId());
							ids.put(number, contact.getContactId());
						}
					}
				}
				else {
					
					Key.setContactId(call, _id);
					call.setName(names.get(number));
					isUpdate = true;
				}
				
				
				if (isUpdate) {
					boolean systemUpdated   = updateFromSystem(call);
					boolean databaseUpdated = updateFromDatabase(call);
					
					if (systemUpdated && databaseUpdated) {
						xlog.d("Call updated : %s", call);
					}
					else {
						if (!systemUpdated) xlog.d("Call cannot updated [system] : %s", call);
						if (!databaseUpdated) xlog.d("Call cannot updated  [database] : %s", call);
					}
				}
			}
		}
	}
	
}
