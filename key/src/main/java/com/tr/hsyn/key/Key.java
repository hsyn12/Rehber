package com.tr.hsyn.key;


import com.tr.hsyn.atom.Atom;

import org.jetbrains.annotations.NotNull;


public class Key extends Atom {
	
	public static final Key EMPTY                  = Key.of(0, "-");
	public static final Key CONTACTS               = Key.of(1, "Contacts");
	public static final Key CALL_LOG               = Key.of(2, "Call Log");
	public static final Key MESSAGE                = Key.of(3, "Message");
	public static final Key CONTACT_LIST_UPDATED   = Key.of(4, "Contact List Updated");
	public static final Key NEW_CONTACTS           = Key.of(5, "New Contacts");
	public static final Key DELETED_CONTACTS       = Key.of(6, "Deleted Contacts");
	public static final Key SELECTED_CALL          = Key.of(7, "Call Selected");
	public static final Key SELECTED_CONTACT       = Key.of(8, "Contact Selected");
	public static final Key RELATION_DEGREE        = Key.of(9, "Relation Degree");
	public static final Key CONTEXT                = Key.of(10, "Context");
	public static final Key SHOW_CALLS             = Key.of(11, "Show Calls");
	public static final Key REFRESH_CALL_LOG       = Key.of(12, "Refresh CallLog");
	public static final Key MOST_CALLS_FILTER_TYPE = Key.of(13, "Most Calls Filter Type");
	public static final Key CALL_LOG_FILTER        = Key.of(14, "CallLog Filter");
	public static final Key CALL_STORY             = Key.of(15, "Call Story");
	@Deprecated(forRemoval = true)
	public static final Key CONTACT_STORY          = Key.of(16, "Contact Story");
	public static final Key REFRESH_CONTACTS       = Key.of(17, "Sign Refresh Contacts");
	@Deprecated(forRemoval = true)
	public static final Key CALL_HISTORY           = Key.of(18, "Call History");
	public static final Key CALL_LOG_UPDATED       = Key.of(19, "CallLog Updated");
	
	public Key(long id, @NotNull String name) {
		
		super(id, name);
	}
	
	@NotNull
	public static Key of(long id, @NotNull String name) {
		
		return new Key(id, name);
	}
	
	@NotNull
	public static Key ofEmpty() {
		
		return EMPTY;
	}
	
	public boolean isEmpty() {
		
		return this.equals(EMPTY);
	}
}
