package com.tr.hsyn.selector;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Bir listeden eleman seçmeyi sağlayan bir seçici.<br>
 * Hem rastgele hem de en az veya en çok seçilenler seçilebilir.
 *
 * @param <T> Eleman türü
 */
public class ListItemSelector<T> implements ItemSelector<T> {

    private final List<T> list;
    private final int[]   counter;

    public ListItemSelector(@NotNull List<T> list) {

        if (list.isEmpty()) throw new EmptyListException();

        this.list = list;
        counter   = new int[list.size()];
    }

    @Override
    public @NotNull
    T selectAny() {

        int index = IntSelector.selectInt(list.size());

        counter[index]++;

        return list.get(index);
    }

    @Override
    public @NotNull
    T selectTheLeast() {

        int _i = findIndex(true);

        counter[_i]++;

        return list.get(_i);
    }

    @Override
    public @NotNull
    T selectTheMost() {

        int _i = findIndex(false);

        counter[_i]++;

        return list.get(_i);
    }

    @Override
    public @NotNull
    T selectFromTheLeast() {

        int       _i        = findIndex(true);
        Integer[] index     = findSameCountIndex(counter[_i]);
        int       randIndex = IntSelector.selectInt(index.length);

        counter[randIndex]++;

        return list.get(randIndex);
    }

    @Override
    public @NotNull
    T selectFromTheMost() {

        int       _i        = findIndex(false);
        Integer[] index     = findSameCountIndex(counter[_i]);
        int       randIndex = IntSelector.selectInt(index.length);

        counter[randIndex]++;

        return list.get(randIndex);
    }

    private int findIndex(boolean isMin) {

        int index = 0;

        for (int i = 1; i < counter.length; i++) {

            if (isMin) {

                if (counter[i] < counter[index]) index = i;
            }
            else {

                if (counter[i] > counter[index]) index = i;
            }
        }

        return index;
    }

    private Integer @NotNull [] findSameCountIndex(int value) {

        List<Integer> vals = new ArrayList<>();

        for (int i = 0; i < counter.length; i++) {

            int _val = counter[i];

            if (_val == value) vals.add(i);
        }

        return vals.toArray(new Integer[0]);
    }
}
