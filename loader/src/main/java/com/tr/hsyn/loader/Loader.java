package com.tr.hsyn.loader;


/**
 * Yükleyici.
 *
 * @param <T> Yüklenecek nesne türü
 */
public interface Loader<T> extends LoadListener<T> {
	
	/**
	 * Yüklemeyi başlat.
	 */
	void startLoading(LoadListener<T> listener);
	
	/**
	 * Yüklemeyi durdur.
	 */
	void stopLoading();
	
	/**
	 * @return Yükleme işleminin o anki adımı.
	 */
	int getCurrentProgress();
	
	/**
	 * @return Yükleme işleminin toplam yapacağı adım sayısı.
	 */
	int getMaxProgress();
	
}
