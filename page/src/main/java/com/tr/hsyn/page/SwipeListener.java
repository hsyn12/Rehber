package com.tr.hsyn.page;


/**
 * <h1>SwipeListener</h1>
 * <p>
 * Listedeki elemanların kaydırma olayı dinleyicisi
 */
public interface SwipeListener {

    /**
     * Kaydırma gerçekleştiğinde.
     *
     * @param index Kaydırılan elemanın index'i
     */
    void onSwipe(int index);
}
