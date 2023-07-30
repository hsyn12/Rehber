package com.tr.hsyn.telefonrehberi.main.call.fragment;


import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL;
import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.page.MenuShower;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.CallBackupActivity;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.RandomCallsActivity;
import com.tr.hsyn.telefonrehberi.main.code.page.adapter.CallAdapter;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuEditor;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


/**
 * Manages the menu.
 */
public abstract class CallLogMenu extends CallLogTitle implements MenuProvider, MenuShower {
	
	protected final Gate       gateMenuSelection = AutoGate.newGate(1000L);
	protected       int        selectedItemsCounter;
	private         boolean    selectAllItem;
	private         int        menuPrepared;
	private         MenuEditor menuManager;
	
	/**
	 * Called when the filter menu item is clicked.
	 */
	protected abstract void onClickFilterMenu();
	
	/**
	 * Called when the 'delete all' menu item is clicked.
	 */
	protected abstract void deleteAll();
	
	/**
	 * Starts listening to the back press event.
	 */
	protected abstract void listenBackPress();
	
	/**
	 * Stops listening to the back press event.
	 */
	protected abstract void doNotListenBackPress();
	
	/**
	 * Called when the 'random calls' menu item is clicked.
	 * This is used to open the {@link RandomCallsActivity}.
	 */
	protected abstract void onClickRandomCallsMenu();
	
	/**
	 * Called when the 'backup' menu item is clicked.
	 * This is used to open the {@link CallBackupActivity} to back up the call logs.
	 */
	protected abstract void onClickBackupMenu();
	
	/**
	 * Called when the 'search' menu item is clicked.
	 */
	protected abstract void onClickSearch();
	
	/**
	 * The call list items are selectable,
	 * and when selection started by the user, the selection mode is activated.
	 * This method returns {@code true} if the user activated the selection mode,
	 * otherwise {@code false}.
	 *
	 * @return {@code true} if selection mode is active
	 */
	protected abstract boolean isSelectionMode();
	
	/**
	 * Called when the 'select all' menu item is clicked while the selection mode is active.
	 *
	 * @param select {@code true} for select all, {@code false} for clear all selections
	 */
	protected abstract void selectAllItems(boolean select);
	
	protected abstract void loopAdapterHolders(Consumer<CallAdapter.Holder> consumer);
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		requireActivity().addMenuProvider(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void showTime(boolean showTime) {
		
		super.showTime(showTime);
		
		if (!showTime && isSelectionMode()) {
			
			cancelSelection();
		}
		else {
			
			//var menu = menuManager.getMenu();
			
			menuManager.setVisible(R.id.menu_call_filter, showTime);
			menuManager.setVisible(R.id.menu_search_call, showTime);
		}
	}
	
	@Override
	public void onPrepareMenu(@NonNull Menu menu) {
		
		if (menuPrepared++ == 0) {
			
			menu.findItem(R.id.menu_call_filter).setVisible(false);
			menu.findItem(R.id.menu_search_call).setVisible(false);
			menu.findItem(R.id.menu_call_backup).setVisible(false);
			menu.findItem(R.id.menu_random_calls_activity).setVisible(false);
			menu.findItem(R.id.delete_all_calls).setVisible(false);
			
			return;
		}
		
		boolean b = isShowTime();
		
		menu.findItem(R.id.menu_call_filter).setVisible(b);
		menu.findItem(R.id.menu_random_calls_activity).setVisible(b);
		menu.findItem(R.id.delete_all_calls).setVisible(b);
		menu.findItem(R.id.menu_call_backup).setVisible(b);
		menu.findItem(R.id.menu_random_calls_activity).setVisible(b);
		menu.findItem(R.id.delete_all_calls).setVisible(b);
	}
	
	@Override
	public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
		
		menuInflater.inflate(R.menu.fragment_call_log_menu, menu);
		
		menuManager = new MenuManager(menu, Lister.listOf(
				R.id.menu_call_backup,
				R.id.menu_call_filter,
				R.id.menu_random_calls_activity,
				R.id.delete_all_calls,
				R.id.menu_random_calls_activity,
				R.id.delete_all_calls,
				R.id.menu_search_call
		));
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
		
		if (getCurrentPage() == 0) return false;
		
		if (!gateMenuSelection.enter()) return false;
		
		switch (menuItem.getItemId()) {
			
			case R.id.menu_random_calls_activity:
				
				onClickRandomCallsMenu();
				return true;
			
			case R.id.menu_call_backup:
				
				onClickBackupMenu();
				return true;
			
			case R.id.menu_call_filter:
				
				onClickFilterMenu();
				return true;
			
			case R.id.menu_search_call:
				
				onClickSearch();
				return true;
			
			case R.id.delete_all_calls:
				
				deleteAll();
				return true;
			
			case R.id.call_log_menu_select_all:
				
				selectAllItems(selectAllItem = !selectAllItem);
				return true;
		}
		
		return false;
	}
	
	@Override
	public void showMenu(boolean show) {
		
		if (show) {
			
			//- Buna gerek yok, ayarlamayı aşağıda yapacağız
			//menuManager.clearMenuItems();
			
			listenBackPress();
			
			//- Kullanıcı bir liste elemanına uzun basılı tutarsa seçim modu aktif olur.
			//- Burada menüyü komple yok edip, sadece iki simge göstermeliyiz.
			//- Simgenin biri seçili elemanları silme,
			//- diğeri toplu seçim işlemi için. Yani 'hepsini seç' yada 'tüm seçimi kaldır' şeklinde.
			//- Tabi tüm menüyü kaldırmak için çevre illere de haber salmamız
			//- gerek çünkü tek menü elemanı sadece şuanki bu sayfaya ait değil.
			//- Menüye eleman ekleyen herkes elemanlarını ikinci bir emre kadar çekmeli.
			
			//- Ana listeyi değiştirmiyoruz, yeni bir liste oluşturuyoruz
			List<Integer> list = Lister.listOf(menuManager.getMenuItemResourceIds());
			//- Bunlar seçim modu aktif oluşca iş yapacak menü elemanları
			List<Integer> selectionMenuItems = Lister.listOf(
					R.id.call_log_menu_delete_all,
					R.id.call_log_menu_select_all
			);
			//- Bu menü elemanlarını yeni oluşturduğumuz listeden çıkarıyoruz
			list.removeAll(selectionMenuItems);
			//- Seçim işlemleri için kullanılan menü elemanlarını görünür hale getir.
			menuManager.setVisible(selectionMenuItems, true);
			//- Ve diğer menü elemanlarını kaldır
			menuManager.setVisible(list, false);
			
			//- Görüntüyü güncelle
			loopAdapterHolders(holder -> {
				
				holder.selection.setVisibility(View.VISIBLE);
				holder.action.setVisibility(View.GONE);
			});
			
			getMainActivity().changeTabBehavior(SCROLL_FLAG_NO_SCROLL | SCROLL_FLAG_ENTER_ALWAYS);
		}
		else {
			
			doNotListenBackPress();
			selectedItemsCounter = 0;
			cancelSelection();
			updateSubTitle();
			
			boolean visible = isShowTime();
			
			menuManager.setVisible(menuManager.getMenuItemResourceIds(), visible);
			
			menuManager.setVisible(R.id.call_log_menu_delete_all, false);
			menuManager.setVisible(R.id.call_log_menu_select_all, false);
			
			loopAdapterHolders(holder -> {
				
				holder.selection.setChecked(false);
				holder.selection.setVisibility(View.GONE);
				holder.action.setVisibility(View.VISIBLE);
			});
			
			getMainActivity().changeTabBehavior(SCROLL_FLAG_SNAP | SCROLL_FLAG_ENTER_ALWAYS | SCROLL_FLAG_SCROLL);
		}
	}
	
	protected void cancelSelection() {
		
		getMainMenu().showMenu(true);
	}
	
	protected void onLongClickItem(int position) {
		
		//- Bu metot, seçim modu aktif iken çağrılmıyor
		//- Bu yüzden bu çağrı güvenli
		getMainMenu().showMenu(false);
	}
	
	
}
