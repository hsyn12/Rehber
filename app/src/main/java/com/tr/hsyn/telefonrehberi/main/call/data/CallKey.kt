package com.tr.hsyn.telefonrehberi.main.call.data

import tr.xyz.call.Call
import tr.xyz.kkey.Key

/**
 * Keys for call
 */
object CallKey {
	
	//@off
	/**
	 * Contact ID if the call number is registered in contacts,
	 * `null` otherwise.
	 */
	val CONTACT_ID       : Key = Key(1, "contactId")
	/**
	 * Note about the call if exists, `null` otherwise.
    */
	val NOTE             : Key = Key(2, "note")
	/**
	 * Deleted Date if the call is deleted, `null` otherwise.
    */
	val DELETED_DATE     : Key = Key(3, "deletedDate")
	/**
	 * Ringing Duration of the call in milliseconds if saved, `null` otherwise.
	 */
	val RINGING_DURATION : Key = Key(4, "ringingDuration")
	/**
	 * Labels of the call if saved, `null` otherwise.
    */
	val LABELS           : Key = Key(5, "labels")
	//@on
}

// region Extensions for call
// region var Call.contactId: Long?
var Call.contactId: Long?
	get() = this[CallKey.CONTACT_ID]
	set(value) {
		this[CallKey.CONTACT_ID] = value
	}
// endregion

// region var Call.note: String?
var Call.note: String?
	get() = this[CallKey.NOTE]
	set(value) {
		this[CallKey.NOTE] = value
	}
// endregion

// region var Call.deletedDate: Long?
var Call.deletedDate: Long?
	get() = this[CallKey.DELETED_DATE]
	set(value) {
		this[CallKey.DELETED_DATE] = value
	}
// endregion

// region var Call.ringingDuration: Long?
var Call.ringingDuration: Long?
	get() = this[CallKey.RINGING_DURATION]
	set(value) {
		this[CallKey.RINGING_DURATION] = value
	}
// endregion

// region var Call.labels: List<String>?
var Call.labels: List<String>?
	get() = this[CallKey.LABELS]
	set(value) {
		this[CallKey.LABELS] = value
	}
// endregion
// endregion

