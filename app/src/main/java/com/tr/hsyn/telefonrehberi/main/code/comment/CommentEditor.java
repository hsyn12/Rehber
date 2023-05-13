package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.view.View;

import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spans;


public interface CommentEditor extends CommentStore {
	
	
	default int getTextColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.purple_500);
	}
	
	default String getString(int resourceId, Object... args) {
		
		return getActivity().getString(resourceId, args);
	}
	
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
}
