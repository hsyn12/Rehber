package com.tr.hsyn.telefonrehberi.main.call.activity.random;


import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;
import com.tr.hsyn.message.GlobalMessage;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.dialog.DialogSelectCallTypes;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.listener.AdapterSelectContacts;
import com.tr.hsyn.telefonrehberi.main.call.activity.random.listener.DialogSelectContacts;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xlog.xlog;

import java.util.Calendar;


/**
 * <p>Menüyü yönetir.
 */
public abstract class RandomCallsActivityMenu extends RandomCallsActivityRutine implements CompoundButton.OnCheckedChangeListener {
	
	@Override
	protected void onDestroy() {
		
		rutine.stopRutine();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.random_calls_activity_menu, menu);
		return true;
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		
		rutine.delay();
		int id = item.getItemId();
		
		switch (id) {
			//@off
			case R.id.menu_random_calls_contacts:showContactSelection();return true;
			case R.id.menu_random_calls_date_start: onSelectedDateStart();return true;
			case R.id.menu_random_calls_date_end: onSelectedDateEnd();return true;
			case R.id.menu_random_calls_call_types: onMenuCallTypes(); return true;
			//@on
		}
		
		
		return true;
	}
	
	private void showContactSelection() {
		
		rutine.delay();
		
		if (getSelectedContacts().isEmpty()) {
			
			Show.snake(this, "Rehberde kayıtlı kimse yok", GlobalMessage.WARN);
			return;
		}
		
		AdapterSelectContacts adapter = new AdapterSelectContacts(getContacts(), getSelectedContacts());
		new DialogSelectContacts(this, adapter, this::onSelectContactsDialogClose);
	}
	
	private void onSelectContactsDialogClose() {
		
		rutine.delay();
		setSelectedContacts(getSelectedContacts());
		onContactsSelected();
	}
	
	protected abstract void onContactsSelected();
	
	@SuppressLint("InflateParams")
	private void onMenuCallTypes() {
		
		var typeNames = Lists.newArrayList(
				getString(R.string.call_type_incoming),
				getString(R.string.call_type_outgoing),
				getString(R.string.call_type_missed),
				getString(R.string.call_type_rejected)
		);
		
		var ids = Lists.newArrayList(
				R.id.check_box_incomming,
				R.id.check_box_outgoing,
				R.id.check_box_missed,
				R.id.check_box_rejected
		);
		
		var callTypes = Lists.newArrayList(
				getCallTypeIncomming(),
				getCallTypeOutgoing(),
				getCallTypeMissed(),
				getCallTypeRejected());
		
		new DialogSelectCallTypes(
				this,
				getLayoutInflater().inflate(R.layout.dialog_select_call_types, null, false),
				callTypes,
				typeNames,
				ids,
				this);
		
	}
	
	@SuppressLint("DefaultLocale")
	private void onSelectedDateStart() {
		
		rutine.delay();
		long dateStart = getDateStart();
		
		if (dateStart == 0L) dateStart = Time.FromNow.months(-6);
		
		var calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateStart);
		
		showCalendar(calendar, 0L, getDateEnd(), c -> {
			
			int day   = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH) + 1;
			int year  = c.get(Calendar.YEAR);
			
			setDateStart(Time.Pointer.at(day, month, year));
		});
	}
	
	@SuppressLint("DefaultLocale")
	private void onSelectedDateEnd() {
		
		rutine.delay();
		long dateEnd = getDateEnd();
		
		if (dateEnd == 0L) dateEnd = Time.now();
		
		var calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateEnd);
		
		showCalendar(calendar, getDateStart(), dateEnd, c -> {
			
			var day   = c.get(Calendar.DAY_OF_MONTH);
			var month = c.get(Calendar.MONTH);
			var year  = c.get(Calendar.YEAR);
			
			setDateEnd(Time.Pointer.at(day, month, year));
		});
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		var text = buttonView.getText();
		
		if (text.equals(getString(R.string.call_type_incoming))) {
			
			setCallTypeIncomming(isChecked);
		}
		else if (text.equals(getString(R.string.call_type_outgoing))) {
			
			setCallTypeOutgoing(isChecked);
		}
		else if (text.equals(getString(R.string.call_type_missed))) {
			
			setCallTypeMissed(isChecked);
		}
		else if (text.equals(getString(R.string.call_type_rejected))) {
			
			setCallTypeRejected(isChecked);
		}
		else {
			
			xlog.w("CallType unknown : %s", text);
		}
	}
}