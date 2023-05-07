package com.tr.hsyn.telefonrehberi.main.code.story.call;


import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.telefonrehberi.main.code.database.call.CallDatabase;
import com.tr.hsyn.telefonrehberi.main.dev.Story;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Arama kayıtları yöneticisi.<br>
 *
 * @see Database
 */
@Keep
public class CallStory implements Story<Call> {
	
	private final Database<Call>  database;
	private final ContentResolver contentResolver;
	
	public CallStory(Database<Call> database, ContentResolver contentResolver) {
		
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
		
		List<Call> systemCalls   = loadFromSystem();
		List<Call> databaseCalls = loadFromDatabase();
		
		//xlog.d("Arama kayıtları alındı. Sistemde %d, veri tabanında %d arama kaydı var", systemCalls.size(), databaseCalls.size());
		
		if (databaseCalls.isEmpty()) {
			
			if (!systemCalls.isEmpty()) {
				
				xlog.d("Veri tabanında arama kaydı yok ancak sistemde %d arama var. Sanırım bu ilk yükleme", systemCalls.size());
				
				var success = database.add(systemCalls);
				
				if (success == systemCalls.size()) {
					
					xlog.d("Tüm kayıtlar veri tabanına kaydedildi");
				}
				else {
					
					xlog.d("%d arama kaydından %d tanesi veri tabanına kaydedildi", systemCalls.size(), success);
				}
				
				systemCalls.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
				
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
				
				databaseCalls.forEach(c -> c.setData(CallKey.DELETED_DATE, now));
				
				database.update(databaseCalls);
				return new ArrayList<>(0);
			}
			else {
				
				//- Karşılaştırmanın yapılması gereken yol
				
				//xlog.d("Arama kayıtları değişiklikleri kontrol ediliyor");
				
				var deletedCalls = Lister.difference(databaseCalls, systemCalls);
				var newCalls     = Lister.difference(systemCalls, databaseCalls);
				
				
				if (deletedCalls.isEmpty() && newCalls.isEmpty()) {
					
					xlog.d("Arama kayıtlarında bir değişiklik yok");
				}
				else {
					
					if (!deletedCalls.isEmpty()) {
						
						xlog.d("Silinen %d arama kaydı var", deletedCalls.size());
						
						long now = System.currentTimeMillis();
						
						deletedCalls.forEach(c -> c.setData(CallKey.DELETED_DATE, now));
						
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
		
		//Arama kayıtlarının diğer ayrıntılarına burada bakılmayacak
		//! Hangi diğer ayrıntıları?
		
		databaseCalls.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
		return databaseCalls;
	}
	
	/**
	 * @return Veri tabanında silinmemiş tüm kayıtları döndürür.
	 */
	@Override
	public List<Call> loadFromDatabase() {
		
		return database.queryAll(CallDatabase.DELETED_DATE + "=0");
	}
	
	/**
	 * @return Tüm sistem kayıtları
	 */
	@Override
	public List<Call> loadFromSystem() {
		
		return Calls.getCalls(contentResolver);
	}
	
	/**
	 * Veri tabanına yeni kayıt ekler.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean addIntoDatabase(Call call) {
		
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
	public boolean delete(Call call) {
		
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
	
	/**
	 * Kaydı siler.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean deleteFromSystem(@NotNull Call call) {
		
		return Calls.delete(contentResolver, call.getTime());
	}
	
	@Override
	public int delete(List<? extends Call> items) {
		
		int count = deleteFromSystem(items);
		
		long time = Time.now();
		items.forEach(c -> c.setData(CallKey.DELETED_DATE, time));
		
		updateFromDatabase(items);
		
		return count;
	}
	
	@Override
	public int updateFromDatabase(@NotNull List<? extends Call> items) {
		
		return (int) items.stream().filter(this::updateFromDatabase).count();
	}
	
	/**
	 * Kaydı günceller.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean updateFromSystem(Call call) {
		
		return Calls.update(contentResolver, call);
	}
	
	/**
	 * Sisteme yeni kayıt ekler.
	 *
	 * @param call Kayıt
	 * @return Yeni eklenen kaydın adresi
	 */
	@Override
	public boolean addIntoSystem(Call call) {
		
		return Calls.add(contentResolver, call) != null;
	}
	
	/**
	 * Veri tabanına yeni kayıt ekler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde eklenen kayıt sayısı
	 */
	@Override
	public int addIntoDatabase(List<? extends Call> calls) {
		
		return database.add(calls);
	}
	
	/**
	 * Sisteme yeni kayıt ekler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde eklenen kayıt sayısı
	 */
	@Override
	public int addIntoSystem(List<? extends Call> calls) {
		
		return Calls.add(contentResolver, calls);
	}
	
	/**
	 * Kaydı siler.
	 *
	 * @param calls Kayıtlar
	 * @return Başarılı bir şekilde silinen kayıt sayısı
	 */
	@Override
	public int deleteFromSystem(List<? extends Call> calls) {
		
		return Calls.delete(contentResolver, calls);
	}
	
	/**
	 * Kaydı günceller.
	 *
	 * @param call Kayıt
	 * @return Başarı durumu
	 */
	@Override
	public boolean updateFromDatabase(@NonNull Call call) {
		
		return database.update(call, String.valueOf(call.getTime()));
	}
	
	@Override
	public boolean deleteFromDatabase(Call item) {
		
		return database.delete(item);
	}
	
	@Override
	public int deleteFromDatabase(List<? extends Call> items) {
		
		return database.delete(items);
	}
	
}
