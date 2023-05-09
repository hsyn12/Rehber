package com.tr.hsyn.telefonrehberi.main.code.comment;


public interface CommentEditor extends CommentStore {
	
	/**
	 * @return the color for the clickable text
	 */
	default int getClickColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.orange_500);
	}
	
	default int getForegroundColor() {
		
		return getActivity().getColor(com.tr.hsyn.rescolors.R.color.purple_500);
	}
}
