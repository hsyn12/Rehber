package com.tr.hsyn.message;


import android.app.Activity;

import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;


//@off

/**
 * <h1>IMessage</h1>
 * 
 * <p>
 *    Mesaj
 * 
 * @author hsyn 19 Aralık 2019 Perşembe 18:18:45
 */
//@on
public interface IMessage<T> {
	
	int INFO  = 0;
	int WARN  = 1;
	int ERROR = 2;
	
	long DURATION_SHORT   = 3_000L;
	long DURATION_DEFAULT = 5_000L;
	long DURATION_LONG    = 10_000L;
	
	/**
	 * Varsayılan geçikme
	 */
	long DELAY = 100L;
	
	/**
	 * @param activity mesajın gösterileceği activity
	 */
	T showOn(Activity activity);
	
	int getRelationDegree();
	
	CharSequence getMessage();
	
	default boolean isNotRelated() {
		
		int relationDegree = getRelationDegree();
		
		if (relationDegree != 0) {
			
			Integer degree = Blue.getObject(Key.RELATION_DEGREE);
			
			if (degree != null) {
				
				xlog.d("Tanımlanmış ilişki derecesi : %d", degree);
				xlog.d("Gereken    ilişki derecesi  : %d", relationDegree);
				
				if (relationDegree > degree) {
					
					xlog.d("Bu mesajı yayınlamak için gereken ilişki derecesi sağlanamadığı için mesaj iptal ediliyor : %s", getMessage());
					return true;
				}
				else {
					
					xlog.d("İlişki derecesi sağlandı");
				}
			}
			else {
				
				xlog.d("İlişki derecesi tanımlı değil, mesaj iptal ediliyor : %s", getMessage());
				return true;
			}
		}
		
		return false;
	}
	
}
