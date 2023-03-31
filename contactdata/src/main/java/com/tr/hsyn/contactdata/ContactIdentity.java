package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;


/**
 * Base information of contact
 */
public interface ContactIdentity {
	
	/**
	 * @return The contact id.
	 */
	long getContactId();
	
	/**
	 * @return The name of the contact
	 */
	@Nullable
	String getName();
}
