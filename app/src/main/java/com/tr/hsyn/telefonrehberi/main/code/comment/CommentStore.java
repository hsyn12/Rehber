package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.app.Activity;

import org.jetbrains.annotations.NotNull;


public interface CommentStore {

    default String getString(int resourceId, Object... args) {

        return getActivity().getString(resourceId, args);
    }

    @NotNull
    Activity getActivity();
	
    default boolean isTurkishLanguage() {

        return getActivity().getResources().getConfiguration().getLocales().get(0).getLanguage().equals("tr");
    }
}
