package com.tr.hsyn.telefonrehberi.main.contact.data

import com.tr.hsyn.contactdata.ContactData
import tr.xyz.contact.Contact
import tr.xyz.kkey.Key

/**
 * Keys for contact.
 */
object ContactKey {
	
	//@off
	val PIC          : Key = Key(10, "pic")
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

// region Extensions for contact
// region var Contact.pic: String?
var Contact.pic: String?
	get() = this[ContactKey.PIC]
	set(value) {
		this[ContactKey.PIC] = value
	}
// endregion

// region var Contact.bigPic: String?
var Contact.bigPic: String?
	get() = this[ContactKey.BIG_PIC]
	set(value) {
		this[ContactKey.BIG_PIC] = value
	}
// endregion

// region var Contact.numbers: List<String>?
var Contact.numbers: List<String>?
	get() = this[ContactKey.NUMBERS]
	set(value) {
		this[ContactKey.NUMBERS] = value
	}
// endregion

// region var Contact.emails: List<String>?
var Contact.emails: List<String>?
	get() = this[ContactKey.EMAILS]
	set(value) {
		this[ContactKey.EMAILS] = value
	}
// endregion

// region var Contact.note: String?
var Contact.note: String?
	get() = this[ContactKey.NOTE]
	set(value) {
		this[ContactKey.NOTE] = value
	}
// endregion

// region var Contact.events: List<ContactData>?
var Contact.events: List<ContactData>?
	get() = this[ContactKey.EVENTS]
	set(value) {
		this[ContactKey.EVENTS] = value
	}
// endregion

// region var Contact.groups: List<String>?
var Contact.groups: List<String>?
	get() = this[ContactKey.GROUPS]
	set(value) {
		this[ContactKey.GROUPS] = value
	}
// endregion

// region var Contact.labels: List<String>?
var Contact.labels: List<String>?
	get() = this[ContactKey.LABELS]
	set(value) {
		this[ContactKey.LABELS] = value
	}
// endregion

// region var Contact.accounts: List<String>?
var Contact.accounts: List<String>?
	get() = this[ContactKey.ACCOUNTS]
	set(value) {
		this[ContactKey.ACCOUNTS] = value
	}
// endregion

// region var Contact.deletedDate: Long?
var Contact.deletedDate: Long?
	get() = this[ContactKey.DELETED_DATE]
	set(value) {
		this[ContactKey.DELETED_DATE] = value
	}
// endregion
// endregion

