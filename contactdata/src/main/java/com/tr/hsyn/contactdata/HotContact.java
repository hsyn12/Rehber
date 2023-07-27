package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class HotContact {
	
	// region base fields : contact ID, name, image
	/**
	 * The contact ID in the system contact database
	 */
	private long   contactId;
	/**
	 * The name of the contact
	 */
	private String name;
	/**
	 * The image of the contact
	 */
	private String image;
	private String bigImage;
	// endregion
	
	// region rank related fields : type, type image
	/**
	 * This the string representing the type of the rank
	 */
	private String type;
	/**
	 * This the image representing the type of the rank
	 */
	private String typeImage;
	// endregion 
	
	// region contact details : phone numbers, emails, events, groups 
	/**
	 * The phone numbers of the contact.
	 * If the contact has no phone numbers, this field is null.
	 */
	private List<String>      numbers;
	/**
	 * The emails of the contact.
	 * If the contact has no emails, this field is null.
	 */
	private List<String>      emails;
	/**
	 * The events of the contact.
	 * If the contact has no events, this field is null.
	 */
	private List<ContactData> events;
	/**
	 * The groups of the contact.
	 * If the contact does not belong to any groups, this field is null.
	 */
	private List<String>      groups;
	private String            note;
	// endregion
	
	// region constructors
	public HotContact(long contactId, String name, String image) {
		
		this.contactId = contactId;
		this.name      = name;
		this.image     = image;
	}
	
	public HotContact(long contactId, String name, String image, String bigImage) {
		
		this.contactId = contactId;
		this.name      = name;
		this.image     = image;
		this.bigImage  = bigImage;
	}
	
	public HotContact(long contactId, String name, String image, String type, String typeImage, List<String> numbers, List<String> emails, List<ContactData> events, List<String> groups) {
		
		this.contactId = contactId;
		this.name      = name;
		this.image     = image;
		this.type      = type;
		this.typeImage = typeImage;
		this.numbers   = numbers;
		this.emails    = emails;
		this.events    = events;
		this.groups    = groups;
	}
	// endregion
	
	//region toString, hashCode, equals
	@Override
	public int hashCode() {
		
		return Objects.hash(contactId);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof HotContact && contactId == ((HotContact) obj).getContactId();
	}
	
	@Override
	public @NotNull String toString() {
		
		return "HotContact{" +
		       "contactId=" + contactId +
		       ", name='" + name + '\'' +
		       ", image='" + image + '\'' +
		       ", bigImage='" + bigImage + '\'' +
		       ", type='" + type + '\'' +
		       ", typeImage='" + typeImage + '\'' +
		       ", numbers=" + numbers +
		       ", emails=" + emails +
		       ", events=" + events +
		       ", groups=" + groups +
		       ", note='" + note + '\'' +
		       '}';
	}
	
	// endregion
	
	// region getters and setters
	public long getContactId() {
		
		return contactId;
	}
	
	public void setContactId(long contactId) {
		
		this.contactId = contactId;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public String getImage() {
		
		return image;
	}
	
	public void setImage(String image) {
		
		this.image = image;
	}
	
	public String getBigImage() {
		
		return bigImage;
	}
	
	public void setBigImage(String bigImage) {
		
		this.bigImage = bigImage;
	}
	
	public List<String> getGroups() {
		
		return groups;
	}
	
	public void setGroups(List<String> groups) {
		
		this.groups = groups;
	}
	
	public String getType() {
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
	}
	
	public String getTypeImage() {
		
		return typeImage;
	}
	
	public void setTypeImage(String typeImage) {
		
		this.typeImage = typeImage;
	}
	
	public List<String> getNumbers() {
		
		return numbers;
	}
	
	public void setNumbers(List<String> numbers) {
		
		this.numbers = numbers;
	}
	
	public List<String> getEmails() {
		
		return emails;
	}
	
	public void setEmails(List<String> emails) {
		
		this.emails = emails;
	}
	
	public List<ContactData> getEvents() {
		
		return events;
	}
	
	public void setEvents(List<ContactData> events) {
		
		this.events = events;
	}
	
	public String getNote() {
		
		return note;
	}
	
	public void setNote(String note) {
		
		this.note = note;
	}
	//endregion
	
	
}
