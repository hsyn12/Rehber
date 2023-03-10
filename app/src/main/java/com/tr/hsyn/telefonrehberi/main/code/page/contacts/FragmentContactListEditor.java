package com.tr.hsyn.telefonrehberi.main.code.page.contacts;


import com.tr.hsyn.collection.Editor;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.page.ListEditor;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;


public abstract class FragmentContactListEditor extends FragmentPageList implements ListEditor<Contact> {
	
	@Override
	public void addItem(@NotNull Contact item) {
		
		int index = insert(item);
		
		adapter.notifyItemInserted(index);
	}
	
	@Override
	public Contact deleteItem(Contact item) {
		
		return deleteItem(indexOfItem(item));
	}
	
	@Override
	public Contact deleteItem(int index) {
		
		var contact = Editor.remove(getList(), index);
		
		if (contact != null) {
			
			adapter.notifyItemRemoved(index);
			
			xlog.d("Kişi listeden kaldırıldı : %s", contact.getName());
		}
		else {
			
			xlog.d("Kişi ana listede bulunamadı : %d", index);
		}
		
		return contact;
	}
	
	private int insert(@NotNull Contact contact) {
		
		String name = Stringx.trimWhiteSpaces(contact.getName());
		
		if (name.isEmpty()) {
			
			getList().add(contact);
			
			return getListSize() - 1;
		}
		
		int size = getListSize();
		
		for (int i = 0; i < size; i++) {
			
			if (PerfectSort.isSmall(name, getItem(i).getName())) {
				
				getList().add(i, contact);
				return i;
			}
		}
		
		getList().add(contact);
		
		return getListSize() - 1;
	}
	
}
