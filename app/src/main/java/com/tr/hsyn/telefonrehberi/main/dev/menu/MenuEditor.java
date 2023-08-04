package com.tr.hsyn.telefonrehberi.main.dev.menu;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tr.hsyn.use.Use;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


/**
 * Menü işlemlerini tanımlar.
 * Menüye menü elemanı ekleme ve çıkarma yöntemleri sağlar.
 * {@link Fragment#onCreateOptionsMenu(Menu, MenuInflater)} metodunun {@code deprecated} olarak
 * işaretlenmesi sonrası, çok sayfalı activity'lerde menü değişimi sırasında yaşanan sorunlar
 * bu sınıfın yazılmasına yol açtı. Ve bu sınıf tüm menüyü yöneten üst bir katman niteliğinde tanımlandı.
 * Menü sunmakta olan her sınıf bu sınıfı uygulayarak menü yönetimini bir derece daha iyi
 * yönetebilir.
 */
public interface MenuEditor extends MenuOwner {
	
	/**
	 * Yönetilen menü içerisindeki ilgilenilen menü elemanları listesini döndürür.
	 * Dönen liste, {@link #getMenuItemResourceIds()} metodu ile verilmiş elemanlardan oluşmaktadır.
	 *
	 * @return menü elemanları listesi
	 */
	@NonNull
	List<MenuItem> getMenuItems();
	
	/**
	 * Yönetilmek istenen menünün elemanlarına ait id değerleri döndürür.
	 *
	 * @return menu item ids
	 */
	List<Integer> getMenuItemResourceIds();
	
	/**
	 * Yönetmek üzere kaydedilmiş olan menü elemanları içinden istenen menü elemanını döndürür.
	 * Bu metot {@link #getMenuItemResourceIds()} metodu ile dönen liste içerisinde arama yapar.
	 *
	 * @param menuItemId menü elemanının id değeri
	 * @return menü item, yoksa {@code null}
	 */
	@Nullable
	default MenuItem getMenuItem(int menuItemId) {
		
		return getMenuItems().stream().filter(it -> it.getItemId() == menuItemId).findFirst().orElse(null);
	}
	
	/**
	 * Sistem menüsünde arama yapar.
	 *
	 * @param menuItemId menuItemId
	 * @return menu item or {@code null}
	 */
	@Nullable
	default MenuItem findMenuItem(int menuItemId) {
		
		return getMenu().findItem(menuItemId);
	}
	
	default void setVisible(int menuItemId, boolean visible) {
		
		Use.ifNotNull(findMenuItem(menuItemId), m -> m.setVisible(visible))
				.isNotUsed(() -> xlog.d("MenuItem not found"));
	}
	
	default void setDisable(int menuItemId, boolean disable) {
		
		Use.ifNotNull(findMenuItem(menuItemId), m -> m.setVisible(disable));
	}
	
	default void setVisible(@NonNull List<Integer> menuItemIds, boolean visible) {
		
		for (int i = 0; i < menuItemIds.size(); i++) {
			
			Use.ifNotNull(findMenuItem(menuItemIds.get(i)), m -> m.setVisible(visible));
		}
	}
	
	default void setDisable(@NonNull List<Integer> menuItemIds, boolean disable) {
		
		for (int i = 0; i < menuItemIds.size(); i++) {
			
			Use.ifNotNull(findMenuItem(menuItemIds.get(i)), m -> m.setVisible(disable));
		}
	}
	
}
