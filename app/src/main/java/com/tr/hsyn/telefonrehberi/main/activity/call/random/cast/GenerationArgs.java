package com.tr.hsyn.telefonrehberi.main.activity.call.random.cast;


import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;

import java.util.List;


public interface GenerationArgs {
	
	@NonNull
	static GenerationArgs newArgs(int count, long start, long end, int maxDuration, Integer[] callTypes, boolean track, List<Contact> contacts) {
		
		return new GenerationArgs() {
			
			@Override
			public int getCount() {
				
				return count;
			}
			
			@Override
			public long getStart() {
				
				return start;
			}
			
			@Override
			public long getEnd() {
				
				return end;
			}
			
			@Override
			public int getMaxDuration() {
				
				return maxDuration;
			}
			
			@Override
			public Integer[] getCallTypes() {
				
				return callTypes;
			}
			
			@Override
			public boolean isTrackFree() {
				
				return track;
			}
			
			@Override
			public List<Contact> getContacts() {
				
				return contacts;
			}
		};
	}
	
	int getCount();
	
	long getStart();
	
	long getEnd();
	
	int getMaxDuration();
	
	Integer[] getCallTypes();
	
	boolean isTrackFree();
	
	List<Contact> getContacts();
	
}
