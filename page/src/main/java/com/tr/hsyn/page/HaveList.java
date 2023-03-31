package com.tr.hsyn.page;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Liste sahibi.
 *
 * @param <T> Liste eleman türü
 */
public interface HaveList<T> {

    /**
     * @return Liste
     */
    @NotNull
    List<T> getList();

    /**
     * Listeyi kaydet.
     *
     * @param list Liste
     */
    void setList(@NotNull List<T> list);

    T getItem(int index);

    default int indexOfItem(T item) {

        return getList().indexOf(item);
    }

    default int getListSize() {

        return getList().size();
    }

}