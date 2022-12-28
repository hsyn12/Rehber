package com.tr.hsyn.page;


import java.util.ArrayList;
import java.util.List;


public interface ListEditor<T> {

    void addItem(T item);

    T deleteItem(T item);

    T deleteItem(int index);

    default List<T> deleteAllItems() {

        return new ArrayList<>(0);
    }
}
