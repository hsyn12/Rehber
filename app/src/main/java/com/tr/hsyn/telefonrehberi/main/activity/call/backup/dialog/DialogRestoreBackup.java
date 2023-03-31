package com.tr.hsyn.telefonrehberi.main.activity.call.backup.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.htext.base.HTextView;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.dialog.Dialog;


public class DialogRestoreBackup {

    protected HTextView   textProgress;
    protected TextView    textPercentProgress;
    protected ProgressBar progress;
    protected AlertDialog dialog;

    public DialogRestoreBackup(@NonNull Activity activity) {

        View view = Dialog.inflate(activity, R.layout.call_backup_restore);

        createDialog(activity, view);
    }

    protected void createDialog(Activity activity, View view) {

        init(view);

        dialog = Dialog.createDialog(activity, view);
        dialog.setCancelable(false);

        progress.setIndeterminate(false);
    }

    public void show() {

        dialog.show();
    }

    public void dismiss() {

        dialog.dismiss();
    }

    private void init(@NonNull View view) {

        //buttonClose         = view.findViewById(R.id.button_close);
        textProgress        = view.findViewById(R.id.text_process);
        progress            = view.findViewById(R.id.progress_restore);
        textPercentProgress = view.findViewById(R.id.text_percent_progress);

        //buttonClose.setOnClickListener(v -> dismiss());
    }

    public void setProgressText(String txt) {

        Runny.run(() -> textProgress.setText(txt));
    }

    public void setProgressText(String txt, boolean animate) {

        Runny.run(() -> {

            if (animate) textProgress.animateText(txt);
            else textProgress.setText(txt);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setProgressPercent(int percent) {

        Runny.run(() -> textPercentProgress.setText(percent + " %"));
    }

    public void setProgress(int progress) {

        this.progress.setProgress(progress);
    }

    public void incProgress() {

        Runny.run(() -> setProgress(progress.getProgress() + 1));
    }

    public void setMaxProgress(int max) {

        progress.setMax(max);
    }
	
	/*public void setCloseButtonEnabled(boolean enabled){
		
		buttonClose.setEnabled(enabled);
	}*/


}
