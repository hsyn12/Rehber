package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import androidx.annotation.NonNull;

import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Account;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.ContactData;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.ContactIdentity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;


@Keep
class BContact implements Contact {
	
	/**
	 * Kişinin id değeri
	 */
	private final long              contactId;
	/**
	 * Kişinin ismi
	 */
	private       String            name;
	/**
	 * Kişinin küçük resmi
	 */
	private       String            pic;
	/**
	 * Kişinin büyük resmi
	 */
	private       String            bigPic;
	/**
	 * Kişinin telefon numaraları
	 */
	private       List<String>      numbers;
	/**
	 * Kişinin mail adresleri
	 */
	private       List<String>      emails;
	/**
	 * Kişi için not
	 */
	private       String            note;
	/**
	 * Kişiye ait olaylar
	 */
	private       List<ContactData> events;
	/**
	 * Kişinin bağlı olduğu gruplar
	 */
	private       List<String>      groups;
	/**
	 * Kişinin bakılma sayısı
	 */
	private       int               lookCount;
	/**
	 * Kaydın bazı tarihleri
	 */
	private       Dates             dates;
	private       Set<Label>        labels;
	private       List<Account>     accounts;
	
	BContact(@NotNull Contact other) {
		
		this.contactId = other.getContactId();
		this.name      = other.getName();
		this.pic       = other.getPic();
		this.bigPic    = other.getBigPic();
		this.numbers   = other.getNumbers();
		this.emails    = other.getEmails();
		this.note      = other.getNote();
		this.events    = other.getEvents();
		this.groups    = other.getGroups();
		this.lookCount = other.getLookCount();
		this.dates     = other.getDates();
	}
	
	BContact(long contactId, String name, String pic) {
		
		this(contactId, name, pic, null);
	}
	
	BContact(long contactId, String name, String pic, String bigPic) {
		
		this(contactId, name, pic, bigPic, null, null, null, null, null, 0, null, null);
	}
	
	BContact(long contactId, String name, String pic, String bigPic, List<String> numbers, List<String> emails, String note, List<ContactData> events, List<String> groups, int lookCount, Dates dates, Set<Label> labels) {
		
		this.contactId = contactId;
		this.name      = name;
		this.pic       = pic;
		this.bigPic    = bigPic;
		this.numbers   = numbers;
		this.emails    = emails;
		this.note      = note;
		this.events    = events;
		this.groups    = groups;
		this.lookCount = lookCount;
		this.dates     = dates;
		this.labels    = labels;
	}
	
	@Override
	public long getContactId() {
		
		return contactId;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public String getPic() {
		
		return pic;
	}
	
	@Override
	public String getBigPic() {
		
		return bigPic;
	}
	
	@Override
	public void setBigPic(String pic) {bigPic = pic;}
	
	@Override
	public List<String> getNumbers() {
		
		return numbers;
	}
	
	@Override
	public void setNumbers(List<String> numbers) {
		
		this.numbers = numbers;
	}
	
	@Override
	public List<String> getEmails() {
		
		return emails;
	}
	
	@Override
	public void setEmails(List<String> emails) {
		
		this.emails = emails;
	}
	
	@Override
	public String getNote() {
		
		return note;
	}
	
	@Override
	public void setNote(String note) {
		
		this.note = note;
	}
	
	@Override
	public List<ContactData> getEvents() {
		
		return events;
	}
	
	@Override
	public void setEvents(List<ContactData> events) {
		
		this.events = events;
	}
	
	@Override
	public List<String> getGroups() {
		
		return groups;
	}
	
	@Override
	public void setGroups(List<String> groups) {
		
		this.groups = groups;
	}
	
	@Override
	public int getLookCount() {
		
		return lookCount;
	}
	
	@Override
	public void setLookCount(int count) {
		
		lookCount = count;
	}
	
	@Override
	public Dates getDates() {
		
		if (dates == null) dates = Dates.newDates();
		
		return dates;
	}
	
	@Override
	public void setDates(Dates dates) {
		
		this.dates = dates;
	}
	
	@Override
	public List<Account> getAccounts() {
		
		return accounts;
	}
	
	@Override
	public void setAccounts(List<Account> accounts) {
		
		this.accounts = accounts;
	}
	
	@NonNull
	@Override
	public String toString() {
		
		return "Contact {" +
		       "contactId=" + contactId +
		       ", name='" + name + '\'' +
		       ", pic='" + pic + '\'' +
		       ", bigPic='" + bigPic + '\'' +
		       ", numbers=" + numbers +
		       ", emails=" + emails +
		       ", note='" + note + '\'' +
		       ", events=" + events +
		       ", groups=" + groups +
		       ", lookCount=" + lookCount +
		       ", dates=" + dates +
		       ", labels=" + labels +
		       '}';
	}
	
	@Override
	public final @NotNull Set<Label> getLabels() {
		
		return labels;
	}
	
	@Override
	public void setLabels(@Nullable Set<Label> labels) {
		
		this.labels = labels;
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Contact && contactId == ((ContactIdentity) o).getContactId();
	}
	
	@Override
	public int hashCode() {
		
		return (int) contactId;
	}
}
