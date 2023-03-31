package com.tr.hsyn.telefonrehberi.main.activity.call.random.dialog;


import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * <p>Rastgele arama kaydı üretiminde kullanılacak
 * arama türlerinin kullanıcı tarafından seçilmesini sağlar.
 * <p>
 */
public class DialogSelectCallTypes {


    /**
     * Arama türü seçimi için bir dialog oluşturur.
     * {@code callTypeStates, callTypeNames} ve {@code checkboxResourceIds} listeleri
     * birbirine paralel olmalı. Bu şekilde istenen sayıda arama türü eklenip çıkarılabilir.
     *
     * @param activity            Seçimi yaptıracak olan {@link Activity}
     * @param layout              Dialog görünümü
     * @param callTypeStates      Arama türlerinin durumu. Seçili ise {@code true}, değilse {@code false}.
     * @param callTypeNames       Arama türlerinin isimleri
     * @param checkboxResourceIds Arama türlerinin seçimi için kullanılacak {@link CheckBox} nesnelerinin görünüm içindeki resource id değerleri
     * @param listener            Seçimi dinleyecek olan nesne
     */
    public DialogSelectCallTypes(
            @NotNull final Activity activity,
            @NotNull final View layout,
            @NotNull final List<Boolean> callTypeStates,
            @NotNull final List<String> callTypeNames,
            @NotNull final List<Integer> checkboxResourceIds,
            @NotNull final CompoundButton.OnCheckedChangeListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);

        for (int i = 0; i < callTypeStates.size(); i++) {

            var cbox = layout.<CheckBox>findViewById(checkboxResourceIds.get(i));

            cbox.setText(callTypeNames.get(i));
            cbox.setChecked(callTypeStates.get(i));
            cbox.setOnCheckedChangeListener(listener);
            cbox.setButtonTintList(ColorStateList.valueOf(Colors.getPrimaryColor()));
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;

        alertDialog.show();
    }
}
