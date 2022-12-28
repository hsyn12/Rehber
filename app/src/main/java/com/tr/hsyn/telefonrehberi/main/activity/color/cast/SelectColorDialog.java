package com.tr.hsyn.telefonrehberi.main.activity.color.cast;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class SelectColorDialog implements ItemIndexListener {

    private final List<ColorModel>        colors = new ArrayList<>();
    private final String[]                colorNames;
    private final WeakReference<Activity> activityWeakReference;
    private final int[]                   colorValues;
    private final int                     selectedIndex;
    private final ItemIndexListener       itemSelectListener;
    private       AlertDialog             dialog;

    public SelectColorDialog(Activity activity, String[] colorNames, int[] colorValues, int selectedIndex, ItemIndexListener selectListener) {

        activityWeakReference   = new WeakReference<>(activity);
        this.colorNames         = colorNames;
        this.colorValues        = colorValues;
        this.selectedIndex      = selectedIndex;
        this.itemSelectListener = selectListener;
        generateColorModels();

        setupView();
    }

    public static AlertDialog.Builder alertDialogBuilder(Context context) {

        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.DefaultDialogTheme));
    }

    @SuppressLint("InflateParams")
    private void setupView() {

        Activity            activity       = activityWeakReference.get();
        LayoutInflater      layoutInflater = activity.getLayoutInflater();
        View                view           = layoutInflater.inflate(R.layout.dialog_change_color, null, false);
        AlertDialog.Builder builder        = alertDialogBuilder(activity);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new SelectColorAdapter(colors).setClickListener(this, selectedIndex));

        builder.setCancelable(true);
        builder.setView(view);
        dialog                                              = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;

        dialog.setOnDismissListener((d) -> onItemIndex(-1));

        dialog.show();
    }

    private void generateColorModels() {

        for (int i = 0; i < colorNames.length; i++) {
            colors.add(new ColorModel(colorValues[i], colorNames[i]));
        }
    }

    @Override
    public void onItemIndex(int position) {

        final long delay = 600L;
        Runny.run(this::close, delay);

        if (itemSelectListener != null) itemSelectListener.onItemIndex(position);
    }

    private void close() {

        dialog.dismiss();

        //- Kapanış
        if (itemSelectListener != null) itemSelectListener.onItemIndex(-1);
    }

}
