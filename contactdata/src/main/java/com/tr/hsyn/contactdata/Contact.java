package com.tr.hsyn.contactdata;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.datboxer.DatBoxer;
import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.keep.Keep;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Represents a contact in the contacts.
 */
@Keep
public class Contact extends DatBoxer implements Identity {
	
	private final long   contactId;
	private final String name;
	private final String pic;
	
	public Contact(@NotNull Contact contact) {
		
		contactId = contact.contactId;
		name      = contact.name;
		pic       = contact.pic;
	}
	
	public Contact(long contactId, String name, String pic) {
		
		this.contactId = contactId;
		this.name      = name;
		this.pic       = pic;
	}
	
	@Override
	public long getId() {
		
		return contactId;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hashCode(contactId);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Contact && contactId == ((Contact) obj).contactId;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("Contact {");
		sb.append("contactId=").append(getContactId()).append(", ")
				.append("name=").append(name);
		
		for (DataKey i : keySet()) {
			//noinspection ConstantConditions
			sb.append(", ").append(i.getName()).append("=").append(getData(i).toString());
		}
		
		return sb.append("}").toString();
	}
	
	@Nullable
	public String getPic() {
		
		return pic;
	}
	
	@Nullable
	public String getName() {
		
		return name;
	}
	
	public long getContactId() {
		
		return contactId;
	}
}
