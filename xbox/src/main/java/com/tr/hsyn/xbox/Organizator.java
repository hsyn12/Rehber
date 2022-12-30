package com.tr.hsyn.xbox;


import com.tr.hsyn.buildkeys.BuildKeys;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Nesnelerin takip edilebilmesine yardım eder.<br>
 * Bir nesneye takipçi ekler yada takibi sonlandırır.<br>
 * Bu sınıf sadece takipçileri kaydeder. Takipçilerin takip ettiği nesneler sayısal bir değer olarak tutulur.<br>
 * Takip edilen nesnelerin asılları, bu sınıfı kullanan kodun kontrolündedir.
 */
@SuppressWarnings("ConstantConditions")
public final class Organizator {
	
	@SuppressWarnings("FieldCanBeLocal")
	private final int DEBUG_DEGREE = 3;
	
	private final Map<Integer, List<Consumer<Object>>> followers = new HashMap<>();
	
	/**
	 * Buluşma bekleyen takipçi var mı kontrol eder.
	 * Verilen nesnenin takipçisi varsa buluşturulur.
	 * Yoksa buluşma kaydı <u>yapılmaz</u>.
	 * Buluşma gerçekleşirse takip sonlandırılır, nesnenin takibi biter.
	 *
	 * @param key    Buluşmak istenilen nesnenin kayıt anahtarı
	 * @param object Buluşmak istenilen nesne
	 * @param <T>    Buluşmak istenilen nesnenin türü
	 */
	public <T> boolean meet(int key, T object) {
		
		if (this.followers.get(key) != null) {
			
			this.followers.remove(key).forEach(follower -> follower.accept(object));
			
			if (DEBUG_DEGREE > 0)
				xlog.i("Meeting is completed for '%d' [%s]", key, BuildKeys.getName(key));
			
			return true;
		}
		else {
			
			if (DEBUG_DEGREE > 1)
				xlog.i("No meeting for '%d' [%s]", key, BuildKeys.getName(key));
		}
		
		return false;
	}
	
	/**
	 * Verilen kayıt anahtarıyla bağlı nesneyi takip eder.
	 * Bir nesne bu anahtarla işlem gördüğünde takipçi ile buluşturulur.
	 *
	 * @param key      Kayıt anahtarı
	 * @param consumer Takipçi
	 * @param <T>      Takip edilen nesnenin türü
	 */
	@SuppressWarnings("unchecked")
	public <T> void follow(int key, Consumer<T> consumer) {
		
		var followers = this.followers.get(key);
		
		if (followers != null) {
			
			if (!followers.contains(consumer)) {
				
				followers.add((Consumer<Object>) consumer);
			}
		}
		else {
			
			followers = new ArrayList<>(1);
			followers.add((Consumer<Object>) consumer);
			this.followers.put(key, followers);
		}
	}
	
	/**
	 * Verilen anahtara ait tüm takipçileri siler.
	 *
	 * @param key Takip edilen
	 */
	public void unFollow(int key) {
		
		followers.remove(key);
	}
	
	/**
	 * Tüm takipçileri siler.
	 */
	public void clear() {
		
		followers.clear();
	}
}
