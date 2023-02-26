package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface ContactMail {
	
	@Nullable
	List<String> getEmails();
	
	void setEmails(@Nullable List<String> emails);
	
}
