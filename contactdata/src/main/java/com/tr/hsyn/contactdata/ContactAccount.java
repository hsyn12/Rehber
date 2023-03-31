package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface ContactAccount {
	
	@Nullable
	List<Account> getAccounts();
	
	void setAccounts(@Nullable List<Account> accounts);
}
