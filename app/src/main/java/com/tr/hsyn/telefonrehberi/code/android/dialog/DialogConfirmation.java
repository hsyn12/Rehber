package com.tr.hsyn.telefonrehberi.code.android.dialog;


import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.telefonrehberi.R;


public class DialogConfirmation {

    private final AlertDialog.Builder builder;
    private       AlertDialog         dialog;

    public DialogConfirmation(Activity activity) {

        builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(activity.getString(R.string.cancel), (dialog1, which) -> dismiss());
        builder.setCancelable(true);
    }

    public DialogConfirmation title(CharSequence title) {

        builder.setTitle(title);
        return this;
    }

    public DialogConfirmation message(CharSequence message) {

        builder.setMessage(message);
        return this;
    }

    public DialogConfirmation positiveButton(CharSequence text, DialogInterface.OnClickListener onConfirm) {

        builder.setPositiveButton(text, onConfirm);
        return this;
    }

    public DialogConfirmation negativeButton(CharSequence text, DialogInterface.OnClickListener onReject) {

        builder.setPositiveButton(text, onReject);
        return this;
    }

    public DialogConfirmation setCancelable(boolean cancelable) {

        builder.setCancelable(cancelable);
        return this;
    }

    public void show() {

        dialog                                              = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;

        dialog.show();
    }

    public void dismiss() {

        if (dialog != null) dialog.dismiss();
    }


}
