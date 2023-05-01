package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.CommentHelper;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class DefaultCommentHelper implements CommentHelper {
	
	private final ContactCommentStore commentStore;
	private final List<Call>          calls;
	
	public DefaultCommentHelper(ContactCommentStore commentStore, List<Call> calls) {
		
		this.commentStore = commentStore;
		this.calls        = calls;
	}
	
	@Override
	public @NotNull CharSequence afterLastCallComment(History history) {
		
		
		return "";
	}
}
