package com.tr.hsyn.telefonrehberi.dev.android.dialog;


import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.tr.hsyn.telefonrehberi.R;


public class Oscar extends ConfirmDialog implements Dialog_YesNo {

    private Runnable     onReject;
    private CharSequence rejctText;

    public Oscar(@NonNull Activity activity) {

        super(activity);
    }

    @Override
    protected void onDialogReady(View dialogView) {

        Button reject = dialogView.findViewById(R.id.dialog_reject_button);

        if (rejctText != null) reject.setText(rejctText);
        else reject.setText(getActivity().getString(R.string.reject));

        reject.setOnClickListener(v -> {

            if (onReject != null) onReject.run();

            dismiss();
        });

    }

    @Override
    public Dialog_YesNo onReject(Runnable onReject) {

        this.onReject = onReject;
        return this;
    }

    @Override
    public Dialog_YesNo rejectText(CharSequence message) {

        rejctText = message;
        return this;
    }

    @Override
    public int getLayoutRecourceId() {

        return R.layout.dialog_yes_no;
    }
}
