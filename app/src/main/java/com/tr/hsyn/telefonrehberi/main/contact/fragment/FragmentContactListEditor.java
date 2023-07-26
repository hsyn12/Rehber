package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import com.tr.hsyn.collection.Lister;
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
		
		@org.jetbrains.annotations.Nullable Contact contact = Lister.remove(getList(), index);
		
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
		
		String name = Stringx.removeAllWhiteSpaces(contact.getName());
		
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
