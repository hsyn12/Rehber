package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.view.View;

import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spans;

import org.jetbrains.annotations.NotNull;


/**
 * Extends {@link CommentStore} in first hand.
 * Moreover, it provides a closer insight into the comments.
 * Text color, underline and clickable text, etc.
 */
public interface CommentEditor extends CommentStore {
	
	/**
	 * Returns the color associated with the given resource ID.
	 *
	 * @param id the resource ID of the color
	 * @return the color associated with the given resource ID
	 */
	default int getColor(int id) {
		
		return getActivity().getColor(id);
	}
	
	/**
	 * Returns the color for the text for emphasis text.
	 *
	 * @return the color
	 */
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
	default int getClickColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.orange_500);
	}
	
	default @NotNull String fmt(String text, Object... args) {
		
		return Stringx.format(text, args);
	}
}
