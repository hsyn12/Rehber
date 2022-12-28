package com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.telefonrehberi.R;

import java.util.List;


public class MostCallDialog {

    @SuppressLint("InflateParams")
    public MostCallDialog(Activity activity, List<MostCallItemViewData> mostCallItemViewDataList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(true);

        View view = activity.getLayoutInflater().inflate(R.layout.most_call_dialog, null, false);

        RecyclerView list = view.findViewById(R.id.most_call_list);

        list.setAdapter(new MostCallDialogAdapter(mostCallItemViewDataList));

        builder.setView(view);

        AlertDialog dialog = builder.create();

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;

        dialog.show();
    }
}
