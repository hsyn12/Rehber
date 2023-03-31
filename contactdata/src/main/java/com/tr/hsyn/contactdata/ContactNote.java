package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;


public interface ContactNote {
	
	/**
	 * @return Kişi için kaydedilmiş not
	 */
	@Nullable
	String getNote();
	
	void setNote(@Nullable String note);
}
