package com.tr.hsyn.telefonrehberi.main.code.cast;


import java.util.List;


public interface ItemAdapter<T> {

    void notifyItemChanged(int index);

    void notifyItemRemoved(int index);

    void notifyItemRangeRemoved(int indexStart, int indexEnd);

    int getSize();

    List<T> getItems();

    void setItems(List<T> items);

    void clearItems();
}
