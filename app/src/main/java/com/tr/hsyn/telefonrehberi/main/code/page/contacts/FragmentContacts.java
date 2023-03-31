package com.tr.hsyn.telefonrehberi.main.code.page.contacts;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Kişiler
 */
public class FragmentContacts extends FragmentPageMenu {

    protected boolean ready;

    @Override
    public boolean isReady() {

        return ready;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        showTime(true);
        ready = true;

        if (onReady != null) {

            onReady.run();
            onReady = null;
        }
    }


}
