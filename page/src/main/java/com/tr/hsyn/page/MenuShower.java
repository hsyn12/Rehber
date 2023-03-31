package com.tr.hsyn.page;


/**
 * Menü göstericisi
 */
public interface MenuShower {

    /**
     * Menü durumunu değiştirir.
     *
     * @param show Gösterilecekse {@code true}, gizlenecek/kaldırılacak ise {@code false}
     */
    void showMenu(boolean show);
}
