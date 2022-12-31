package com.tr.hsyn.news.dispatcher;


import org.jetbrains.annotations.NotNull;


/**
 * Haber dağıtıcısına sahip yetkili sınıf.
 * Bu sınıf dağıtıcıyı değiştirebilir.
 */
public interface DispatchEditor {
	
	@NotNull NewsDispatcher getDispatcher();
	
	void setDispatcher(@NotNull NewsDispatcher dispatcher);
}
