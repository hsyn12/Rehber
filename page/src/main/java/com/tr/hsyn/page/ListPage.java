package com.tr.hsyn.page;


import androidx.annotation.NonNull;

import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.selection.SelectionInformation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Bir nesne listesini temsil eder.
 * Tüm listelerin temel sınıfı ve yol göstericidir.
 *
 * @param <T> Liste elemanı türü
 */
public abstract class ListPage<T> extends ShowPage implements ItemIndexListener, Preparation, HaveList<T>, SelectionInformation, IHaveProgress, ListEditor<T>, MenuShower, HaveCallAction, DeleteAction, SwipeInformer, HasFilter {
	
	/**
	 * Sayfa gösterime hazır oluğunda çağrılır.
	 * Bu runnable metodu çağrılmadan önce sayfa elemanları ile ilgili bir çağrı
	 * hata ile sonuçlanır.
	 * Sayfayı kullanan üst nesne bu değişkene {@link #setOnReady(Runnable)} metodu ile
	 * atama yapar ve çağrılmasını bekler.
	 */
	protected Runnable          onReady;
	/**
	 * Liste
	 */
	private   List<T>           list = new ArrayList<>();
	/**
	 * Listeden bir elemana dokunulduğunda çağrılacak iş
	 */
	private   ItemIndexListener itemSelectListener;
	
	@Override
	public final void onItemIndex(int position) {
		
		if (itemSelectListener != null) itemSelectListener.onItemIndex(position);
	}
	
	/**
	 * Bir liste elemanına dokunulduğunda çağrılacak işi kaydeder.
	 *
	 * @param itemSelectListener dinleyici
	 */
	@Override
	public void setItemSelectListener(ItemIndexListener itemSelectListener) {
		
		this.itemSelectListener = itemSelectListener;
	}
	
	/**
	 * @return sayfanın sorumlu olduğu nesne listesi
	 */
	@Override
	@NotNull
	public List<T> getList() {
		
		return list;
	}
	
	/**
	 * Listeyi kaydet.
	 *
	 * @param list liste
	 */
	@Override
	public void setList(@NonNull List<T> list) {
		
		this.list = list;
	}
	
	/**
	 * Sayfa hizmet vermeye hazır olduğunda çağrılacak runnable nesnesini kaydeder.
	 *
	 * @param onReady Sayfa hazır olduğunda çalıştırılacak iş
	 */
	@Override
	public void setOnReady(Runnable onReady) {
		
		if (isReady()) {
			
			if (onReady != null) {
				onReady.run();
				return;
			}
		}
		
		this.onReady = onReady;
	}
	
}
