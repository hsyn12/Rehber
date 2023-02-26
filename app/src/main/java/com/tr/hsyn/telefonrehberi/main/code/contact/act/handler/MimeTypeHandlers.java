package com.tr.hsyn.telefonrehberi.main.code.contact.act.handler;


import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class MimeTypeHandlers extends MimeTypeHandler {
	
	private final Map<String, MimeTypeHandler> handlers = new HashMap<>();
	private final LinkedList<MimeTypeHandler>  handled  = new LinkedList<>();
	
	public MimeTypeHandlers() {
		
		super("");
	}
	
	public MimeTypeHandlers(MimeTypeHandler... handlers) {
		
		this();
		
		Lister.loop(handlers, this::addHandler);
	}
	
	public void addHandler(MimeTypeHandler handler) {
		
		handlers.put(handler.getMimeType(), handler);
	}
	
	@Override
	public void handleMimeType(@NotNull String mimeType, String data1, String data2) {
		
		var handler = handlers.get(mimeType);
		
		if (handler != null) {
			
			handled.addFirst(handler);
			
			handler.handleMimeType(mimeType, data1, data2);
		}
		else {
			
			xlog.i("There is no mime type handler for this mime type : %s", mimeType);
		}
	}
	
	@Override
	public void applyResult(@NotNull Contact contact) {
		
		while (!handled.isEmpty()) {
			
			var handler = handled.removeFirst();
			
			handler.applyResult(contact);
		}
		
		contact.setData(ContactKey.DETAILS_APPLIED, true);
	}
}
