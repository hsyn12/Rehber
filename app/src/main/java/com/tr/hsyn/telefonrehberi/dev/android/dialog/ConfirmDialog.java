package com.tr.hsyn.telefonrehberi.dev.android.dialog;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;


/**
 * <h1>ConfirmDialog</h1>
 * <p>
 * Tek bir onaylama butonu olan basit bir dialog.
 *
 * @author hsyn 21.07.2022 12:30:56
 */
public class ConfirmDialog implements Dialog {

    private final AlertDialog.Builder dialogBuilder;
    private final Activity            activity;
    private       AlertDialog         dialog;
    private       CharSequence        title;
    private       CharSequence        message;
    private       CharSequence        confirmText;
    private       Runnable            onConfirm;
    private       int                 animationResource = Integer.MIN_VALUE;


    public ConfirmDialog(@NonNull Activity activity) {

        this.activity = activity;
        dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setCancelable(true);
    }

    protected void onDialogReady(View dialogView) {}

    @Override
    public Dialog title(CharSequence title) {

        this.title = title;
        return this;
    }

    @Override
    public Dialog message(CharSequence message) {

        this.message = message;
        return this;
    }

    @Override
    public Dialog confirmText(CharSequence message) {

        this.confirmText = message;
        return this;
    }

    @Override
    public Dialog cancelable(boolean cancelable) {

        dialogBuilder.setCancelable(cancelable);
        return this;
    }

    @Override
    public Dialog onConfirm(Runnable onConfirm) {

        this.onConfirm = onConfirm;
        return this;
    }

    @Override
    public Dialog animationResource(int animationResource) {

        this.animationResource = animationResource;
        return this;
    }

    @Override
    public Activity getActivity() {

        return activity;
    }

    @Override
    public int getLayoutRecourceId() {

        return R.layout.dialog_confirm;
    }

    @Override
    public Dialog show() {

        int  color = Colors.lighter(Colors.getPrimaryColor(), .15f);
        View view  = activity.getLayoutInflater().inflate(getLayoutRecourceId(), null, false);

        View     head    = view.findViewById(R.id.dialog_confirm_head);
        View     body    = view.findViewById(R.id.dialog_confirm_body);
        TextView title   = view.findViewById(R.id.dialog_confirm_title);
        TextView message = view.findViewById(R.id.dialog_confirm_message);
        Button   confirm = view.findViewById(R.id.dialog_confirm_button);


        confirm.setTextColor(color);
        head.setBackgroundColor(color);
        //body.setBackgroundColor(Colors.lighter(color, .9f));

        if (this.title != null) title.setText(this.title);
        else title.setText(activity.getString(R.string.confirm_title));

        if (confirmText != null) confirm.setText(confirmText);
        else confirm.setText(activity.getString(R.string.confirm));

        if (this.message != null) message.setText(this.message);
        else message.setText(activity.getString(R.string.confirm_message));

        confirm.setOnClickListener(v -> {

            if (onConfirm != null) {

                onConfirm.run();
                dismiss();
            }
        });

        dialog = dialogBuilder.create();
        dialog.setView(view);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.oval_background));

        dialog.getWindow().getAttributes().windowAnimations = animationResource == Integer.MIN_VALUE ? R.style.InBounceOutAway : animationResource;

        onDialogReady(view);
        dialog.show();
        return this;
    }

    @Override
    public void dismiss() {

        if (dialog != null) {

            dialog.dismiss();
        }

    }
}
