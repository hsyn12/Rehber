package com.tr.hsyn.telefonrehberi.main.dev.menu;


import android.view.Menu;


/**
 * Menü sahibi
 */
public interface MenuOwner {

    /**
     * Sahip olunan menüyü döndürür.
     *
     * @return Yönetilen menü
     */
    Menu getMenu();
}
