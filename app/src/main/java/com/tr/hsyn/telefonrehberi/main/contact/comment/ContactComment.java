package com.tr.hsyn.telefonrehberi.main.contact.comment;


import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.Topic;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.treadedwork.Threaded;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


public interface ContactComment extends Threaded {
	
	default @Nullable CallCollection getCallCollection() {
		
		return Blue.getObject(Key.CALL_COLLECTION);
	}
	
	/**
	 * Returns a string representing the given resource ID by using {@link #getActivity()} as context.
	 *
	 * @param resourceId the resource id to be used to get the string
	 * @param args       the arguments to be used in the string
	 * @return the string
	 */
	default String getString(int resourceId, Object... args) {
		
		return getActivity().getString(resourceId, args);
	}
	
	/**
	 * Returns the color for the text for emphasis text.
	 *
	 * @return the color
	 */
	@ColorInt
	default int getTextColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.purple_500);
	}
	
	/**
	 * Returns the spans for the clickable text.
	 *
	 * @param listener the listener to be used for the click
	 * @return the spans
	 */
	default Span[] getClickSpans(View.OnClickListener listener) {
		
		return new Span[]{
				Spans.click(listener, getClickColor()),
				Spans.underline()
		};
	}
	
	/**
	 * @return the color for the clickable text
	 */
	@ColorInt
	default int getClickColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.orange_500);
	}
	
	default @NotNull String fmt(String text, Object... args) {
		
		return Stringx.format(text, args);
	}
	
	Activity getActivity();
	
	@NotNull CharSequence getComment();
	
	Topic getTopic();
	
	void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish);
	
	@NotNull
	Consumer<ContactComment> getCallback();
	
	default void returnComment() {
		
		onMain(() -> getCallback().accept(this));
	}
}
