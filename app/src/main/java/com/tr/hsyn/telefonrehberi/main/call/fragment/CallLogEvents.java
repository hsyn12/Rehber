package com.tr.hsyn.telefonrehberi.main.call.fragment;


import android.Manifest;
import android.content.Intent;

import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.Permissions;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.CallBackupActivity;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.RandomCallsActivity;
import com.tr.hsyn.telefonrehberi.main.call.activity.search.CallLogSearch;


public abstract class CallLogEvents extends CallLogEditor {
	
	@Override
	protected void onClickRandomCallsMenu() {
		
		if (getContext() == null) return;
		
		if (!Permissions.hasPermissions(getContext(), Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALL_LOG)) {
			
			Show.tost(getContext(), getString(R.string.no_permissions));
			return;
		}
		
		Intent i = new Intent(getContext(), RandomCallsActivity.class);
		startActivity(i);
		Bungee.slideRight(getContext());
	}
	
	@Override
	protected void onClickBackupMenu() {
		
		startActivity(new Intent(getContext(), CallBackupActivity.class));
		Bungee.slideRight(getContext());
	}
	
	@Override
	protected void onClickSearch() {
		
		if (getList().isEmpty()) {
			
			Show.tost(getContext(), getString(R.string.empty_call_list));
			return;
		}
		
		startActivity(new Intent(getContext(), CallLogSearch.class));
		Bungee.zoomFast(getActivity());
	}
	
	
}
