package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;

import org.jetbrains.annotations.NotNull;


public interface CommentHelper {
	
	/**
	 * It is called after the last call comment.
	 *
	 * @param history Contact history
	 * @return the comment
	 * @see ContactCommentator#commentOnTheLastCall()
	 */
	@NotNull CharSequence afterLastCallComment(History history);
	
}
