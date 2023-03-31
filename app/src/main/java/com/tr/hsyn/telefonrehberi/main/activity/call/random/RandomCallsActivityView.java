package com.tr.hsyn.telefonrehberi.main.activity.call.random;


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
 * Görsel elemanları hazırlar.
 */
public abstract class RandomCallsActivityView extends ActivityView {

    protected final int         primaryColor = Colors.getPrimaryColor();
    /**
     * Rastgele üretim sayısının alındığı görsel eleman
     */
    protected       EditText    editTextGenerationCount;
    /**
     * Üretimi başlatan buton
     */
    protected       Button      buttonStartGeneration;
    protected       ProgressBar progressGeneration;
    protected       TextView    textProgress;
    protected       HTextView   textCurrentProgress;
    protected       HTextView   textDescription;

    protected abstract int getGenerationCount();

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

    protected void onClickStartGeneration(View view) {
        //- Pass
    }

    protected void onClickEditTextGeneration(View view) {
        //- Pass
    }

    @Override
    protected final int getLayoutId() {

        return R.layout.activity_random_calls;
    }

    protected int getEnteredGenerationCount() {

        Editable editable = editTextGenerationCount.getText();

        if (editable != null) {

            String count = editable.toString();

            if (!Stringx.trimWhiteSpaces(count).isEmpty()) {

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

    @UiThread
    protected void showCalendar(@NonNull Calendar calendar, long minDate, long maxDate, @NonNull Consumer<Calendar> onSelect) {

        DatePickerDialog pickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {

                            var c = Calendar.getInstance();
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
