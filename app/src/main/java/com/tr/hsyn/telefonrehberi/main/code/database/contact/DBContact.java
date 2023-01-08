package com.tr.hsyn.telefonrehberi.main.code.database.contact;


import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;


/**
 * Kişiler veri tabanı arayüzü
 */
public interface DBContact extends Database<Contact> {
	
	String CONTACT_ID     = "contact_id";
	String NAME           = "name";
	String NUMBERS        = "numbers";
	String LOOK_COUNT     = "look_count";
	String SAVED_DATE     = "saved_date";
	String UPDATED_DATE   = "updated_date";
	String DELETED_DATE   = "deleted_date";
	String LAST_LOOK_DATE = "last_look_date";
	String EMAILS         = "emails";
	String LABELS         = "labels";
	
	List<String> getEmails(long id);
	
	boolean setEmails(long id, List<String> emails);
	
	Dates getDates(long id);
	
	boolean setDates(long id, @NotNull Dates dates);
	
	static List<String> getList(@Nullable String str) {
		
		if (str == null) return null;
		
		return Arrays.asList(str.split("\\|"));
	}
	
	static String getList(@NotNull List<String> array) {
		
		return Stringx.joinToString(array, "|");
	}
	
}
