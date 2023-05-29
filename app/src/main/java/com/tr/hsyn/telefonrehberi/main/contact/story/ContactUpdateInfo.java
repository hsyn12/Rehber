package com.tr.hsyn.telefonrehberi.main.contact.story;


import org.jetbrains.annotations.NotNull;


/**
 * Kişi güncelleme bilgileri
 */
public class ContactUpdateInfo {
	
	private final long   time = System.currentTimeMillis();
	private       long   id;
	private       String oldName;
	private       String newName;
	private       String oldNumber;
	private       String newNumber;
	
	public long getTime() {
		
		return time;
	}
	
	public long getId() {
		
		return id;
	}
	
	public void setId(long id) {
		
		this.id = id;
	}
	
	public String getOldName() {
		
		return oldName;
	}
	
	public String getNewName() {
		
		return newName;
	}
	
	public String getOldNumber() {
		
		return oldNumber;
	}
	
	public String getNewNumber() {
		
		return newNumber;
	}
	
	public void setName(String oldName, String newName) {
		
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public void setNumber(String oldNumber, String newNumber) {
		
		this.oldNumber = oldNumber;
		this.newNumber = newNumber;
	}
	
	
	@NotNull
	@Override
	public String toString() {
		
		return "ContactUpdateInfo{" +
		       "time=" + time +
		       ", id=" + id +
		       ", oldName='" + oldName + '\'' +
		       ", newName='" + newName + '\'' +
		       ", oldNumber='" + oldNumber + '\'' +
		       ", newNumber='" + newNumber + '\'' +
		       '}';
	}
}
