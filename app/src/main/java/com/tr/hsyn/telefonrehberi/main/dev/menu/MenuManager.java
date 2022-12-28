package com.tr.hsyn.telefonrehberi.main.dev.menu;


import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.tr.hsyn.use.Use;

import java.util.ArrayList;
import java.util.List;


public class MenuManager implements MenuEditor {

    private final Menu           menu;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final List<Integer>  menuItemIds;

    public MenuManager(@NonNull Menu menu, @NonNull List<Integer> menuItemIds) {

        this.menu        = menu;
        this.menuItemIds = menuItemIds;

        menuItemIds.forEach(this::addMenuItem);
    }

    private void addMenuItem(int menuItemId) {

        Use.ifNotNull(menu.findItem(menuItemId), menuItems::add);
    }

    @NonNull
    @Override
    public List<MenuItem> getMenuItems() {

        return menuItems;
    }

    @Override
    public List<Integer> getMenuItemResourceIds() {

        return menuItemIds;
    }

    @Override
    public Menu getMenu() {

        return menu;
    }
}
