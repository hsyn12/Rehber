package com.tr.hsyn.telefonrehberi.main.activity.call.random.listener;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;


public class DialogSelectContacts {


    @SuppressLint("InflateParams")
    public DialogSelectContacts(
            @NonNull final Activity activity,
            @NonNull final AdapterSelectContacts adapter,
            @NonNull Runnable onDismiss) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_contacts_selection, null, false);
        builder.setView(view);

        view.findViewById(R.id.header).setBackgroundColor(Colors.getPrimaryColor());

        RecyclerView list = view.findViewById(R.id.list_select_contacts);
        list.setAdapter(adapter);

        CheckBox selectAll = view.findViewById(R.id.check_box_select_all);
        selectAll.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

        selectAll.setOnCheckedChangeListener((bt, b) -> adapter.selectAll(b));

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;

        alertDialog.setOnDismissListener(dialog -> onDismiss.run());
        alertDialog.show();
    }

}
