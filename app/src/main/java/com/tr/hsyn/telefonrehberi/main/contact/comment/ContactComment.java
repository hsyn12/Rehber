package com.tr.hsyn.telefonrehberi.main.contact.comment;


import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.topics.Topic;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.treadedwork.Threaded;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * This interface defines how to comment on a contact.
 */
public interface ContactComment extends Threaded {
	
	boolean isTurkish();
	
	/**
	 * Some information needs to create a dialog and needs to get text, color, etc. from the resources.
	 *
	 * @return the activity
	 */
	Activity getActivity();
	
	/**
	 * Returns the created comment.
	 *
	 * @return the comment
	 */
	@NotNull CharSequence getComment();
	
	/**
	 * Returns the topic of the comment.
	 *
	 * @return the topic
	 * @see Topic
	 */
	Topic getTopic();
	
	/**
	 * Creates a comment.
	 *
	 * @param contact   contact
	 * @param activity  activity
	 * @param callback  callback
	 * @param isTurkish isTurkish
	 */
	void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish);
	
	/**
	 * Returns the callback that called when the comment created.
	 *
	 * @return the callback
	 */
	@NotNull
	Consumer<ContactComment> getCallback();
	
	default CallLog getCallLogs() {
		
		return Blue.getObject(Key.CALL_LOGS);
	}
	
	/**
	 * Returns a string representing the given resource ID by using {@link #getActivity()} as context.
	 *
	 * @param resourceId the resource ID to be used to get the string
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
	
	default Span[] getTextStyle() {
		
		return new Span[]{
				Spans.foreground(getTextColor()),
				Spans.bold()
		};
	}
	
	/**
	 * Returns the plural form of the given word based on the count for only english.
	 *
	 * @param word  the word to make plural
	 * @param count the count
	 * @return the plural form of the word
	 */
	@NotNull
	default String makePlural(@NotNull String word, long count) {
		
		if (isTurkish() || count < 2) return word;
		
		return word + "s";
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
	
	/**
	 * Returns formatted text.
	 *
	 * @param text the text
	 * @param args the arguments
	 * @return the formatted text
	 */
	default @NotNull String fmt(String text, Object... args) {
		
		return Stringx.format(text, args);
	}
	
	/**
	 * Returns the comment to the callback.
	 */
	default void returnComment() {
		
		onMain(() -> getCallback().accept(this));
	}
	
	
}
