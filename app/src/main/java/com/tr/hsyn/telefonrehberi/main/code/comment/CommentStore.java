package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.app.Activity;

import org.jetbrains.annotations.NotNull;


/**
 * Provides helpers for generating comments and
 * helps all comment related things.
 * So, it's needed the activity {@link #getActivity()}.
 *
 * @author hsyn 16 Mayıs 2023 Salı 09:58
 */
public interface CommentStore {
	
	/**
	 * Returns a string representing the given resource id by using {@link #getActivity()} as context.
	 *
	 * @param resourceId the resource id to be used to get the string
	 * @param args       the arguments to be used in the string
	 * @return the string
	 */
	default String getString(int resourceId, Object... args) {
		
		return getActivity().getString(resourceId, args);
	}
	
	/**
	 * Return the activity.
	 *
	 * @return the activity
	 */
	@NotNull
	Activity getActivity();
	
	/**
	 * Determines if the current language is Turkish.
	 *
	 * @return {@code true} if the current language is Turkish
	 */
	default boolean isTurkishLanguage() {
		
		return getActivity().getResources().getConfiguration().getLocales().get(0).getLanguage().equals("tr");
	}
}
