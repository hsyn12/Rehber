package com.tr.hsyn.telefonrehberi.main.activity.contact.detail;


import com.tr.hsyn.telefonrehberi.main.code.comment.contact.ContactCommentator;
import com.tr.hsyn.xlog.xlog;

import java.util.Locale;


public class ContactDetailsAbout extends ContactDetailsMenu {
	
	
	//- Burada kişi hakkında bazı kayda değer bilgileri
	//- kompozisyon halinde sunmak istiyoruz.
	
	protected String getLocaleLanguage() {
		
		return Locale.getDefault().getLanguage();
	}
	
	protected boolean isTurkishLocale() {
		
		return getLocaleLanguage().equals("tr");
	}
	
	private void onComments() {
		
		xlog.d("Locale : %s", getLocaleLanguage());
		
		var commentator = ContactCommentator.createCommentator(this);
		
		var comments = commentator.commentate(contact);
		xlog.d(comments);
		
	}
	
}
