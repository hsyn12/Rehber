package com.tr.hsyn.telefonrehberi.main.contact.fragment;


import android.content.Intent;
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

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.page.MenuShower;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.cast.PageOwner;
import com.tr.hsyn.telefonrehberi.main.contact.activity.search.ContactSearch;
import com.tr.hsyn.telefonrehberi.main.data.ContactLog;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuEditor;
import com.tr.hsyn.telefonrehberi.main.dev.menu.MenuManager;
import com.tr.hsyn.use.Use;
import com.tr.hsyn.xlog.xlog;


public abstract class FragmentPageMenu extends FragmentContactListEditor implements MenuProvider, MenuShower {
	
	private final Gate       gateMenuSelection = AutoGate.newGate(1000L);
	protected     PageOwner  pageOwner;
	private       int        menuPrepared;
	private       MenuEditor menuEditor;
	
	/**
	 * Called when the user clicks on the filter menu.
	 */
	protected abstract void onClickFilterMenu();
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		requireActivity().addMenuProvider(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void showTime(boolean showTime) {
		
		super.showTime(showTime);
		
		if (menuEditor != null) {
			
			menuEditor.setVisible(R.id.menu_search_contacts, showTime);
			menuEditor.setVisible(R.id.menu_filter, showTime);
		}
	}
	
	@Override
	public void onPrepareMenu(@NonNull Menu menu) {
		
		Use.ifNotNull(
				menu.findItem(R.id.menu_search_contacts),
				item -> item.setVisible(isShowTime() || menuPrepared == 0));
		
		menuPrepared++;
	}
	
	@Override
	public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
		
		menuInflater.inflate(R.menu.fragment_contacts_menu, menu);
		
		menuEditor = new MenuManager(menu, Lister.listOf(R.id.menu_search_contacts, R.id.menu_filter));
	}
	
	@Override
	public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
		
		if (pageOwner == null || pageOwner.getCurrentPage() == 1) return false;
		
		var callsLoaded    = CallLog.isLoaded();
		var contactsLoaded = ContactLog.isLoaded();
		
		if (callsLoaded && contactsLoaded) {
			
			if (gateMenuSelection.enter()) {
				
				int id = menuItem.getItemId();
				
				if (id == R.id.menu_search_contacts) {
					
					onClickSearch();
					return true;
				}
				
				if (id == R.id.menu_filter) {
					
					onClickFilterMenu();
					return true;
				}
			}
		}
		else {
			
			if (!contactsLoaded) {
				xlog.d("Contacts not loaded");
				Show.tost(requireActivity(), getString(R.string.contacts_not_loaded));
			}
			else {
				xlog.d("Call log not loaded");
				Show.tost(requireActivity(), getString(R.string.call_log_not_loaded));
			}
		}
		
		
		return false;
	}
	
	@Override
	public void showMenu(boolean show) {
		
		menuEditor.setVisible(menuEditor.getMenuItemResourceIds(), !show);
	}
	
	@Override
	public void setPageOwner(PageOwner pageOwner) {
		
		this.pageOwner = pageOwner;
	}
	
	protected void onClickSearch() {
		
		
		if (getList().isEmpty()) {
			
			Show.tost(getContext(), getString(R.string.no_contact));
			return;
		}
		
		startActivity(new Intent(getContext(), ContactSearch.class));
		Bungee.zoomFast(getContext());
	}
}
