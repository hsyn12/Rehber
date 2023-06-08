package com.tr.hsyn.telefonrehberi.main.code.page.calllog;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.page.ListEditor;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.data.Res;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


public abstract class CallLogEditor extends CallLogColor implements ListEditor<Call> {
	
	@Override
	public void addItem(Call item) {
		
		updateSubTitle();
	}
	
	@Override
	public Call deleteItem(int index) {
		
		Call deleted = getItem(index);
		
		getAdapter().getItems().remove(index);
		getAdapter().notifyItemRemoved(index);
		
		
		if (Res.Calls.FILTER_ALL != filter) {
			
			int _index = getList().indexOf(deleted);
			
			if (_index != -1) {
				
				getList().remove(_index);
			}
			else {
				
				xlog.d("Silinen kayıt ana listede bulunamadı : %s", Stringx.overWrite(deleted.getNumber()));
			}
		}
		
		updateSubTitle();
		checkEmpty();
		
		return deleted;
	}
	
	@Override
	public List<Call> deleteAllItems() {
		
		@org.jetbrains.annotations.NotNull List<Call> deletedCalls = Lister.listOf(getAdapter().getItems());
		getAdapter().clearItems();
		
		//- Filtreleme durumuna göre silinen elemanları ana listeden de çıkaralım
		if (Res.Calls.FILTER_ALL != filter) {
			
			int count = Lister.removeItems(getList(), deletedCalls);
			
			if (count == deletedCalls.size()) {
				
				xlog.d("Silinen %d arama da listeden çıkarıldı", count);
			}
			else {
				
				xlog.d("%d arama silindi ancak %d tanesi listeden çıkarılabildi", deletedCalls.size(), count);
			}
		}
		
		
		return deletedCalls;
	}
}
