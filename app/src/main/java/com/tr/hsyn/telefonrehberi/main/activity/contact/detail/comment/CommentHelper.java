package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;

import org.jetbrains.annotations.NotNull;


public interface CommentHelper {
	
	@NotNull CharSequence afterLastCallComment(History history);
	
}
