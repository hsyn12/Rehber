package com.tr.hsyn.telefonrehberi.code.android.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.telefonrehberi.R;


public class DialogProgress implements DialogInterface.OnDismissListener, DialogInterface.OnShowListener {

    private final AlertDialog.Builder builder;
    private final View                view;
    private final ProgressBar         progressBar;
    private final TextView            progressText;
    private       AlertDialog         dialog;
    private       Runnable            onOpen;
    private       Runnable            onDismiss;

    public DialogProgress(Activity activity, int layoutId) {

        builder = new AlertDialog.Builder(activity);

        view         = activity.getLayoutInflater().inflate(layoutId, null, false);
        progressBar  = view.findViewById(R.id.progress_dialog);
        progressText = view.findViewById(R.id.text_progress_dialog);
        builder.setView(view);
    }

    @SuppressLint("SetTextI18n")
    public void setProgress(int progress) {

        progressBar.setProgress(progress);
        progressText.setText(progress + "%");
    }

    public DialogProgress title(CharSequence title) {

        view.<TextView>findViewById(R.id.title_dialog).setText(title);
        return this;
    }

    public DialogProgress onOpenDialog(Runnable onOpen) {

        this.onOpen = onOpen;
        return this;
    }

    public DialogProgress onDismissDialog(Runnable onDismiss) {

        this.onDismiss = onDismiss;
        return this;
    }

    public void show() {

        dialog                                              = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
        dialog.setCancelable(false);
        dialog.setOnDismissListener(this);
        dialog.setOnShowListener(this);
    }

    public void dismiss() {

        if (dialog != null) {

            dialog.dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {

        if (onOpen != null) onOpen.run();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (onDismiss != null) onDismiss.run();
    }
}
