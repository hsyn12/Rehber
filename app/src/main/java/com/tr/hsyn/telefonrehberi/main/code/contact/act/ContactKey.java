package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.datakey.IntKey;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Account;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.ContactDat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;


public interface ContactKey {
	
	DataKey BIG_PIC         = new IntKey(3, "bigPic");
	DataKey NUMBERS         = new IntKey(4, "numbers");
	DataKey EMAILS          = new IntKey(5, "emails");
	DataKey NOTE            = new IntKey(6, "note");
	DataKey EVENTS          = new IntKey(7, "events");
	DataKey GROUPS          = new IntKey(8, "groups");
	DataKey LABELS          = new IntKey(9, "labels");
	DataKey ACCOUNTS        = new IntKey(10, "accounts");
	DataKey DELETED         = new IntKey(11, "deleted");
	DataKey DETAILS_APPLIED = new IntKey(12, "detailsApplied");
	
	interface GETTER {
		
		@Nullable
		static String getBigPic(@NotNull Contact contact) {
			
			return contact.getData(BIG_PIC);
		}
		
		@Nullable
		static List<String> getNumbers(@NotNull Contact contact) {
			
			return contact.getData(NUMBERS);
		}
		
		@Nullable
		static List<String> getEmails(@NotNull Contact contact) {
			
			return contact.getData(EMAILS);
		}
		
		@Nullable
		static List<ContactDat> getEvents(@NotNull Contact contact) {
			
			return contact.getData(EVENTS);
		}
		
		@Nullable
		static String getNote(@NotNull Contact contact) {
			
			return contact.getData(NOTE);
		}
		
		@Nullable
		static List<String> getGroups(@NotNull Contact contact) {
			
			return contact.getData(GROUPS);
		}
		
		@Nullable
		static List<String> getAccounts(@NotNull Contact contact) {
			
			return contact.getData(ACCOUNTS);
		}
		
		@Nullable
		static Set<Label> getLabels(@NotNull Contact contact) {
			
			return contact.getData(LABELS);
		}
		
		static boolean isDeleted(@NotNull Contact contact) {
			
			Boolean deleted = contact.getData(LABELS);
			return deleted != null ? deleted : false;
		}
	}
	
	interface SETTER {
		
		static void setBigPic(@NotNull Contact contact, String pic) {
			
			contact.setData(BIG_PIC, pic);
		}
		
		static void setNumbers(@NotNull Contact contact, List<String> numbers) {
			
			contact.setData(NUMBERS, numbers);
		}
		
		static void setEmails(@NotNull Contact contact, List<String> emails) {
			
			contact.setData(EMAILS, emails);
		}
		
		static void setEvents(@NotNull Contact contact, List<ContactDat> events) {
			
			contact.setData(EVENTS, events);
		}
		
		static void setLabels(@NotNull Contact contact, Set<Label> labels) {
			
			contact.setData(LABELS, labels);
		}
		
		static void setGroups(@NotNull Contact contact, List<String> groups) {
			
			contact.setData(GROUPS, groups);
		}
		
		static void setAccounts(@NotNull Contact contact, List<Account> accounts) {
			
			contact.setData(ACCOUNTS, accounts);
		}
		
		static void setNote(@NotNull Contact contact, String note) {
			
			contact.setData(NOTE, note);
		}
		
		static void setDeleted(@NotNull Contact contact, boolean deleted) {
			
			contact.setData(DELETED, deleted);
		}
	}
	
}
