package com.tr.hsyn.page;


import androidx.annotation.CallSuper;

import com.tr.hsyn.shower.ShowTimer;
import com.tr.xyz.fragment.Fragment;


/**
 * Bir fragment sayfası.
 */
public abstract class ShowPage extends Fragment implements ShowTimer {

    /**
     * Sayfanın aktif olup olmadığını anlatır.
     */
    private boolean showTime;

    /**
     * @return sayfa aktif ise {@code true}, değil ise {@code false}
     */
    @Override
    public boolean isShowTime() {

        return showTime;
    }

    /**
     * Bu metot, sayfanın aktif durumu değiştiğinde çağrılır.
     *
     * @param showTime Gösterimde ise {@code true} değilse {@code false}
     */
    @CallSuper
    @Override
    public void showTime(boolean showTime) {

        this.showTime = showTime;
    }
}
