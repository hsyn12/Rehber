package com.tr.hsyn.telefonrehberi.main.dev.tost;

import android.content.Context;

import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.main.dev.Over;


public class Tost {

    private static Context getContext() {

        return Over.App.getContext();
    }


    public static void show(CharSequence message) {

        Show.tost(getContext(), message);
    }


}
