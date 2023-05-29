package com.tr.hsyn.telefonrehberi.main.contact.data.bank;


import android.content.Context;

import androidx.annotation.NonNull;


public interface DataBankClient<T> {
	
	int getId();
	
	@NonNull
	String getName();
	
	@NonNull
	Context getContext();
	
	//void setContext(@NonNull Context context);
	
	@NonNull
	Class<? extends T> getClazz();
}
