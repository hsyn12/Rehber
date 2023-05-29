package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.telefonrehberi.main.contact.data.bank.DataBankClient;
import com.tr.hsyn.telefonrehberi.main.contact.data.bank.Err;


public class DataBankErrorClient implements DataBankClient<Err> {
	
	private final Context context;
	
	public DataBankErrorClient(@NonNull Context context) {
		
		this.context = context;
	}
	
	@Override
	public int getId() {
		
		return 33;
	}
	
	@NonNull
	@Override
	public String getName() {
		
		return "AppInErrors";
	}
	
	@NonNull
	@Override
	public Context getContext() {
		
		return context;
	}
	
	@NonNull
	@Override
	public Class<? extends Err> getClazz() {
		
		return Err.class;
	}
}
