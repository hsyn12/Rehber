package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface ContactEvent {
	
	@Nullable
	List<ContactDat> getEvents();
	
	void setEvents(@Nullable List<ContactDat> events);
}
