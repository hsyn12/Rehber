package com.tr.hsyn.contactdata;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.datboxer.DatBoxer;
import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.keep.Keep;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Rehberdeki bir kişiyi temsil eder.
 */
@Keep
public class Contact extends DatBoxer implements Identity {
	
	private static final DataKey NAME = DataKey.of(1, "name");
	private static final DataKey PIC  = DataKey.of(2, "pic");
	private final        long    contactId;
	
	public Contact(@NotNull Contact contact) {
		
		contactId = contact.contactId;
		
		for (var key : contact.keySet()) {
			
			setData(key, contact.getData(key));
		}
	}
	
	public Contact(long contactId, String name, String pic) {
		
		this.contactId = contactId;
		setPic(pic);
		setName(name);
	}
	
	@Nullable
	public String getPic() {return getData(PIC);}
	
	public void setPic(String pic) {
		
		setData(PIC, pic);
	}
	
	public long getContactId() {
		
		return contactId;
	}
	
	@Nullable
	public String getName() {return getData(NAME);}
	
	public void setName(String name) {
		
		setData(NAME, name);
	}
	
	@Override
	public long getId() {
		
		return contactId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Contact && contactId == ((Contact) obj).contactId;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hashCode(contactId);
	}
	
	@NotNull
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("Contact {");
		sb.append("contactId=").append(getContactId());
		
		for (DataKey i : keySet()) {
			//noinspection ConstantConditions
			sb.append(", ").append(i.getName()).append("=").append(getData(i).toString());
		}
		
		return sb.append("}").toString();
	}
}
