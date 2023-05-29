package com.tr.hsyn.telefonrehberi.main.call.activity.showcalls;


import com.tr.hsyn.bungee.Bungee;


public class ShowCallsActivity extends ShowCallsActivitySet {

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Bungee.slideUp(this);
    }
}
