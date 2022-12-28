package com.tr.hsyn.telefonrehberi.code;


import android.app.Activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * Görsel elemanları bulmanın bir başka yolu.
 * Görsel eleman talep edilmeden önce oluşturulmaz.
 *
 * @param <T> Görsel elemanın türü
 */
public class ViewFinder<T> {

    @IdRes
    public final int id;
    private      T   view;

    public ViewFinder(@IdRes int id) {

        this.id = id;
    }

    @SuppressWarnings("unchecked")
    public T get(@NonNull Fragment fragment) {

        return view != null ? view : (view = (T) fragment.requireView().findViewById(id));
    }

    @SuppressWarnings("unchecked")
    public T get(@NonNull Activity activity) {

        return view != null ? view : (view = (T) activity.findViewById(id));
    }

    public T get() {

        return view;
    }
}
