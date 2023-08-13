package com.tr.hsyn.telefonrehberi.main.contact.data

import com.tr.hsyn.contactdata.ContactData
import tr.xyz.contact.Contact
import tr.xyz.kkey.Key

/**
 * Keys for contact.
 */
object Keys {
	
	//@off
	val BIG_PIC      : Key = Key(1, "bigPic")      
	val NUMBERS      : Key = Key(2, "numbers")     
	val EMAILS       : Key = Key(3, "emails")      
	val NOTE         : Key = Key(4, "note")        
	val EVENTS       : Key = Key(5, "events")      
	val GROUPS       : Key = Key(6, "groups")      
	val LABELS       : Key = Key(7, "labels")      
	val ACCOUNTS     : Key = Key(8, "accounts")    
	val DELETED_DATE : Key = Key(9, "deletedDate")
	//@on
	
}

var Contact.bigPic: String?
	get() = this[Keys.BIG_PIC]
	set(value) {
		this[Keys.BIG_PIC] = value
	}

var Contact.numbers: List<String>?
	get() = this[Keys.NUMBERS]
	set(value) {
		this[Keys.NUMBERS] = value
	}

var Contact.emails: List<String>?
	get() = this[Keys.EMAILS]
	set(value) {
		this[Keys.EMAILS] = value
	}

var Contact.note: String?
	get() = this[Keys.NOTE]
	set(value) {
		this[Keys.NOTE] = value
	}

var Contact.events: List<ContactData>?
	get() = this[Keys.EVENTS]
	set(value) {
		this[Keys.EVENTS] = value
	}

var Contact.groups: List<String>?
	get() = this[Keys.GROUPS]
	set(value) {
		this[Keys.GROUPS] = value
	}

var Contact.labels: List<String>?
	get() = this[Keys.LABELS]
	set(value) {
		this[Keys.LABELS] = value
	}

var Contact.accounts: List<String>?
	get() = this[Keys.ACCOUNTS]
	set(value) {
		this[Keys.ACCOUNTS] = value
	}

var Contact.deletedDate: Long?
	get() = this[Keys.DELETED_DATE]
	set(value) {
		this[Keys.DELETED_DATE] = value
	}

