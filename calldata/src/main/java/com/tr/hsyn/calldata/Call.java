package com.tr.hsyn.calldata;


import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.label.Mabel;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * interface for database call
 */
public interface Call extends SystemCall, Serializable, Mabel, Deletable, Identity {
	
	/**
	 * @return contact id
	 */
	long getContactId();
	
	void setContactId(long id);
	
	/**
	 * @return note
	 */
	String getNote();
	
	void setNote(String note);
	
	/**
	 * @return ringing duration
	 */
	long getRingingDuration();
	
	@NotNull
	Call withDeletedDate(long date);
	
	boolean equals(final long contactId);
	
	@NotNull
	static Call newCall(String name, String number, int type, long time, int duration, String extra, long contactId, String note, long deletedDate, long ringingDuration) {
		
		return new BCall(name, number, type, time, duration, extra, contactId, deletedDate, note, ringingDuration);
	}
	
	@NotNull
	static Call newCall(String name, String number, int type, long time, int duration, String extra) {
		
		return new BCall(name, number, type, time, duration, extra);
	}
	
}
