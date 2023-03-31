package com.tr.hsyn.life;


import com.tr.hsyn.executors.MinExecutor;

import org.jetbrains.annotations.NotNull;


/**
 * Life Writer.<br>
 * Nesnenin hayatını kaydetmek için görevli yazıcı.
 */
public interface LifeWriter {
	
	@NotNull
	LifeRecorder getLifeRecorder();
	
	/**
	 * @return Hayat sahibi
	 */
	@NotNull
	Life getLife();
	
	/**
	 * Hayatı başlat
	 */
	default void lifeStart() {
		
		MinExecutor.run(() -> getLifeRecorder().add(getLife()));
	}
	
	/**
	 * Hayatı bitir.
	 */
	default void lifeEnd() {
		
		getLifeRecorder().endLife(getLife());
	}
	
	/**
	 * Yeni bir yazıcı oluştur.
	 *
	 * @param liveName     Hayat sahibinin ismi
	 * @param lifeDatabase Kaydedici
	 * @return Yeni bir yazıcı
	 */
	@NotNull
	static LifeWriter create(@NotNull String liveName, @NotNull LifeRecorder lifeDatabase) {
		
		return new ObjectLifeWriter(lifeDatabase, liveName);
	}
	
}
