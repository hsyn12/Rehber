package com.tr.hsyn.telefonrehberi.main.call.activity.random;


import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.htext.base.HTextView;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;

import java.util.Calendar;
import java.util.function.Consumer;


/**
 * Prepares the views.
 */
public abstract class RandomCallsActivityView extends ActivityView {
	
	/**
	 * Primary color
	 */
	protected final int         primaryColor = Colors.getPrimaryColor();
	/**
	 * Visual element from which random generation number is taken
	 */
	protected       EditText    editTextGenerationCount;
	/**
	 * Production start button
	 */
	protected       Button      buttonStartGeneration;
	/**
	 * Progress bar
	 */
	protected       ProgressBar progressGeneration;
	/**
	 * Progress text
	 */
	protected       TextView    textProgress;
	/**
	 * Current progress text
	 */
	protected       HTextView   textCurrentProgress;
	/**
	 * Description
	 */
	protected       HTextView   textDescription;
	
	/**
	 * @return the number of generations
	 */
	protected abstract int getGenerationCount();
	
	@Override
	protected final int getLayoutId() {
		
		return R.layout.activity_random_calls;
	}
	
	@CallSuper
	@Override
	protected void onCreate() {
		
		editTextGenerationCount = findView(R.id.edit_text_generation_count);
		buttonStartGeneration   = findView(R.id.button_start);
		progressGeneration      = findView(R.id.progress_generation);
		textProgress            = findView(R.id.text_progress);
		textCurrentProgress     = findView(R.id.text_current_progress);
		textDescription         = findView(R.id.text_description);
		
		editTextGenerationCount.setText(String.valueOf(getGenerationCount()));
		buttonStartGeneration.setOnClickListener(this::onClickStartGeneration);
		editTextGenerationCount.setBackgroundTintList(ColorStateList.valueOf(primaryColor));
		editTextGenerationCount.setOnClickListener(this::onClickEditTextGeneration);
	}
	
	@Override
	protected boolean hasToolbar() {
		
		return true;
	}
	
	@Override
	protected int getToolbarResourceId() {
		
		return R.id.toolbar_random_calls;
	}
	
	@Override
	protected Runnable getNavigationClickListener() {
		
		return this::onBackPressed;
	}
	
	/**
	 * Called when the start button is clicked.
	 *
	 * @param view the start button
	 */
	protected void onClickStartGeneration(View view) {
		//- Pass
	}
	
	/**
	 * Called when the edit text is clicked.
	 *
	 * @param view the edit text
	 */
	protected void onClickEditTextGeneration(View view) {
		//- Pass
	}
	
	/**
	 * @return the number of generations entered
	 */
	protected int getEnteredGenerationCount() {
		
		Editable editable = editTextGenerationCount.getText();
		
		if (editable != null) {
			
			String count = editable.toString();
			
			if (!Stringx.removeAllWhiteSpaces(count).isEmpty()) {
				
				try {
					return Integer.parseInt(count);
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Shows the calendar.
	 *
	 * @param calendar the calendar
	 * @param minDate  the min date
	 * @param maxDate  the max date
	 * @param onSelect the on select callback
	 */
	@UiThread
	protected void showCalendar(@NonNull Calendar calendar, long minDate, long maxDate, @NonNull Consumer<Calendar> onSelect) {
		
		DatePickerDialog pickerDialog =
				new DatePickerDialog(
						this,
						(view, year, month, dayOfMonth) -> {
							
							Calendar c = Calendar.getInstance();
							c.set(year, month, dayOfMonth);
							
							onSelect.accept(c);
						},
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
		
		if (minDate != 0L) pickerDialog.getDatePicker().setMinDate(minDate);
		
		pickerDialog.getDatePicker().setMaxDate(maxDate);
		pickerDialog.show();
	}
	
}
