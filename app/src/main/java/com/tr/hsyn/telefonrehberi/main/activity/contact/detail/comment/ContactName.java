package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;


public class ContactName {
	
	private final String  name;
	private final boolean isContact;
	
	public ContactName(String name) {
		
		if (name == null || name.isBlank() || PhoneNumbers.isPhoneNumber(name)) {
			
			this.name = "numara";
			isContact = false;
		}
		else {
			
			this.name = name;
			isContact = true;
		}
	}
	
	public String getName() {
		
		return name;
	}
	
	public boolean isContact() {
		
		return isContact;
	}
	
	public String getNameTitled() {
		
		return Stringx.toTitle(name);
	}
	
}
